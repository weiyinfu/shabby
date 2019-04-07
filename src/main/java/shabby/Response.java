package shabby;

public class Response {
String contentType;
String content;
byte[] data;

public String getContentType() {
    return contentType;
}

public void setContentType(String contentType) {
    this.contentType = contentType;
}

public String getContent() {
    return content;
}

public void setContent(String content) {
    this.content = content;
}

public byte[] getData() {
    return data;
}

public void setData(byte[] data) {
    this.data = data;
}
}
