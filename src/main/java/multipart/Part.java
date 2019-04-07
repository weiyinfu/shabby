package multipart;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

public class Part {
byte[] data;
Map<String, String> headers = new TreeMap<>();

public byte[] getData() {
    return data;
}

public void setData(byte[] data) {
    this.data = data;
}

public Map<String, String> getHeaders() {
    return headers;
}

public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
}


private String fetch(String headerName, String attrName) {
    String header = headers.get(headerName);
    if (header == null) return null;
    return MultipartParser.parseAttr(header, attrName);
}

public String asString() {
    try {
        return new String(data, "utf8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return null;
}

public InputStream asInputStream() {
    return new ByteArrayInputStream(data);
}

public String getName() {
    return fetch("Content-Disposition", "name");
}

public String getFileName() {
    return fetch("Content-Disposition", "filename");
}

public String getContentType() {
    return headers.get("Content-Type");
}

public int getSize() {
    return data.length;
}

@Override
public String toString() {
    return asString();
}
}
