package multipart;

import com.alibaba.fastjson.JSON;
import org.slf4j.LoggerFactory;
import utils.io.FileUtil;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class MultipartParser {
static org.slf4j.Logger logger = LoggerFactory.getLogger(MultipartParser.class);

/**
 * 解析形如attr1=value1;attr2=value2;attr3=value3的字符串
 *
 * @param header:形如attr1=value1;attr2=value2;attr3=value3的字符串
 * @param attrName:需要查找的属性名称
 */
public static String parseAttr(String header, String attrName) {
    String[] kvs = header.split(";");
    for (String kv : kvs) {
        int sep = kv.indexOf('=');
        if (sep == -1) continue;
        String k = kv.substring(0, sep).trim(), v = kv.substring(sep + 1).trim();
        if (v.startsWith("\"") && v.endsWith("\"")) v = v.substring(1, v.length() - 1);
        if (k.equalsIgnoreCase(attrName)) {
            return v;
        }
    }
    return null;
}

/**
 * byte数组中的查找操作，相当于字符串中的indexOf函数
 */
static int indexOf(byte[] all, byte[] searching, int start) {
    for (int i = start; i < all.length; i++) {
        boolean found = true;
        for (int j = 0; j < searching.length; j++) {
            if (all[j + i] != searching[j]) {
                found = false;
                break;
            }
        }
        if (found) return i;
    }
    return -1;
}

/**
 * 将byte数组转化为字符串
 */
static String gets(byte[] array, int begInclude, int endExclude, String charset) throws UnsupportedEncodingException {
    return new String(array, begInclude, endExclude - begInclude, charset);
}

/**
 * HTTP请求就是ascii编码的，不能用其它格式进行解码，否则会乱套
 */
public static Map<String, Part> parse(InputStream cin, String boundary) {
    Map<String, Part> map = new TreeMap<>();
    try {
        byte[] all = FileUtil.inputStreamToBytes(cin);
        byte[] beginBytes = (boundary + "\r\n").getBytes("ascii");
        byte[] middleBytes = "\r\n\r\n".getBytes("ascii");
        byte[] endBytes = ("\r\n" + boundary).getBytes("ascii");
        int start = indexOf(all, beginBytes, 0) + beginBytes.length;
        while (start < all.length) {
            int middle = indexOf(all, middleBytes, start);
            if (middle == -1) break;
            int end = indexOf(all, endBytes, middle + middleBytes.length);
            if (end == -1) break;
            String header = gets(all, start, middle, "utf8");
            byte[] content = Arrays.copyOfRange(all, middle + middleBytes.length, end);
            String[] headers = header.split("\r\n");
            Part part = new Part();
            for (String h : headers) {
                int sep = h.indexOf(':');
                String k = h.substring(0, sep).trim(), v = h.substring(sep + 1).trim();
                part.headers.put(k, v);
            }
            part.setData(content);
            map.put(part.getName(), part);
            start = end + endBytes.length;
            if (all[start] == '-' && all[start + 1] == '-') {
                break;
            } else if (all[start] == '\r' && all[start + 1] == '\n') {
                start += 2;
            } else {
                throw new RuntimeException("HTTP请求格式错误");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return map;
}

public static void main(String[] args) throws IOException {
    InputStream cin = new FileInputStream("C:\\Users\\weidiao\\Desktop\\post.txt");
    Map<String, Part> parts = parse(cin, "------WebKitFormBoundaryDXCJERqpXU3RVNAK");
    System.out.println(JSON.toJSONString(parts, true));
    Part p = parts.get("myfile");
    FileOutputStream cout = new FileOutputStream(p.getFileName());
    FileUtil.cpWithoutClose(p.asInputStream(), cout);
    cout.close();
    logger.info(parts.get("username").asString());
}
}
