package utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

/**
 * Requests，我的客户端，用来发起HTTP请求
 */
public class Requests {
static Logger logger = LoggerFactory.getLogger(Requests.class);
final static PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();

public static class Response {
    HttpResponse resp;

    Response(HttpResponse resp) {
        this.resp = resp;
    }

    public String text() {
        try {
            return EntityUtils.toString(resp.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject json() {
        try {
            return JSON.parseObject(EntityUtils.toString(resp.getEntity(), "utf8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] bytes() {
        try {
            return EntityUtils.toByteArray(resp.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int status() {
        return resp.getStatusLine().getStatusCode();
    }

}

public static HttpClient getClient() {
    return HttpClients
            .custom()
            .setConnectionManager(httpClientConnectionManager)
            .build();
}

public static Response get(URI uri) {
    try {
        HttpResponse resp = getClient().execute(new HttpGet(uri));
        return new Response(resp);
    } catch (Exception e) {
        logger.error("", e);
    }
    return null;
}

public static Response post(String url, HttpEntity entity) {
    try {
        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        HttpResponse resp = getClient().execute(post);
        return new Response(resp);
    } catch (IOException e) {
        logger.error("", e);
    }
    return null;
}
}
