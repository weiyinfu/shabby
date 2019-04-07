package utils.io;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * 工具类中禁止出现日志，禁止访问非标准库
 */
public class FileUtil {
static Random random = new Random();

//复制流中的内容，不负责关闭流
public static void cpWithoutClose(InputStream cin, OutputStream cout) throws IOException {
    byte[] buffer = new byte[1024];
    while (true) {
        int data = cin.read(buffer);
        if (data <= 0) {
            break;
        }
        cout.write(buffer, 0, data);
    }
}

public static boolean isWindows() {
    String osName = System.getProperty("os.name");
    return osName != null && osName.toLowerCase().startsWith("windows");
}


public static byte[] inputStreamToBytes(InputStream cin) {
    ByteArrayOutputStream cout = new ByteArrayOutputStream();
    try {
        cpWithoutClose(cin, cout);
        cout.close();
        return cout.toByteArray();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

//输入流转字符串
public static String inputStreamToString(InputStream cin, String charset) {
    StringBuilder builder = new StringBuilder();
    try {
        InputStreamReader in = new InputStreamReader(cin, charset);
        char[] buf = new char[1000];
        while (true) {
            int sz = in.read(buf);
            if (sz <= 0) break;
            builder.append(buf, 0, sz);
        }
        return builder.toString();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

//输入流转bytearrayinputstream
public static ByteArrayInputStream inputStreamToByteArrayInputStream(InputStream cin) {
    if (cin == null) return null;
    if (cin.getClass() == ByteArrayInputStream.class) return (ByteArrayInputStream) cin;
    byte[] bytes = inputStreamToBytes(cin);
    if (bytes == null) return null;
    return new ByteArrayInputStream(bytes);
}

//获取临时文件目录
public static String getTempDir(String prefix, String suffix) {
    try {
        File path = File.createTempFile(prefix, suffix);
        if (!path.exists()) {
            boolean success = path.mkdir();
            if (!success) {
                throw new IOException("create folder error");
            }
        }
        return path.getAbsolutePath();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

//获取资源的路径
public static Path getResourceAsPath(String resourceName) {
    String p = FileUtil.class.getResource(resourceName).getPath();
    if (p.contains(":")) p = p.substring(1);
    return Paths.get(p);
}

//获取类路径下的流
public static InputStream getResource(String path) {
    return FileUtil.class.getResourceAsStream(path);
}


/**
 * 为了照顾低版本的JDK（没有Path对象），路径拼接使用此函数
 */
public static String pathJoin(String first, String second) {
    if (first.endsWith("\\") || first.endsWith("/")) {
        first = first.substring(0, first.length() - 1);
    }
    if (second.startsWith("\\") || second.startsWith("/")) {
        second = second.substring(1);
    }
    return first + File.separator + second;
}

}
