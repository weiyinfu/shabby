package express4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class App {
Map<String, Handler> getUrlMapping = new HashMap<>();
Map<String, Handler> postUrlMapping = new HashMap<>();
List<Middleware> middlewares = new LinkedList<>();
Logger logger = LoggerFactory.getLogger(App.class);

static App app;

public static App getApp() {
    if (app == null) app = new App();
    return app;
}

public void use(Middleware middleware) {
    this.middlewares.add(middleware);
}

public void get(String url, Handler handler) {
    this.getUrlMapping.put(url, handler);
}

public void post(String url, Handler handler) {
    this.postUrlMapping.put(url, handler);
}

private Handler getHandler(HttpServletRequest req) {
    try {
        URL url = new URL(req.getRequestURL().toString());
        String path = url.getPath().substring("/express4j".length());
        logger.info("path " + path);
        Handler handler = this.getUrlMapping.get(path);
        return handler;
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
    return null;
}

protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
    Handler handler = getHandler(req);
    if (handler == null) {
        resp.setStatus(404);
    } else {
        handler.handle(req, resp);
    }
}

protected void handlePost(HttpServletRequest req, HttpServletResponse resp) {
    Handler handler = getHandler(req);
    if (handler == null) {
        resp.setStatus(404);
    } else {
        handler.handle(req, resp);
    }
}

}
