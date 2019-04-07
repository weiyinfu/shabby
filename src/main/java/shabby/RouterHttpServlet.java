package shabby;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import multipart.MultipartParser;
import multipart.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.io.FileUtil;
import utils.template.Freemarker;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

public class RouterHttpServlet extends HttpServlet {
Freemarker freemarker;
Logger logger = LoggerFactory.getLogger(RouterHttpServlet.class);

@Override
public void init(ServletConfig config) throws ServletException {
    super.init(config);
    Config.packageName = config.getInitParameter("package");
    Config.templatePath = config.getInitParameter("templatePath");
    if (Config.templatePath != null) {
        freemarker = new Freemarker(Paths.get(Config.templatePath));
    }
}

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    logger.info(RouterHttpServlet.class + " do get ");
    logger.info(req.getRequestURL().toString());
    Map<String, Object> map = new TreeMap<>();
    resp.setCharacterEncoding("utf8");
    req.setCharacterEncoding("utf8");
    Enumeration<String> it = req.getParameterNames();
    while (it.hasMoreElements()) {
        String key = it.nextElement();
        map.put(key, req.getParameter(key));
    }
    handle(map, req, resp);
}

void handle(Map<String, ?> map, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    Object responseObject = Router.route(map, req.getRequestURL().toString());
    if (responseObject == null) return;
    if (responseObject.getClass() == String.class) {
        String retStr = responseObject.toString();
        //请求重定向
        if (retStr.startsWith("redirect:")) {
            String target = retStr.substring("redirect:".length());
            resp.sendRedirect(target);
            return;
        } else if (retStr.startsWith("forward:")) {//请求继续处理
            String target = retStr.substring("forward:".length());
            req.getRequestDispatcher(target).forward(req, resp);
            return;
        } else if (retStr.startsWith("template:")) {//模板渲染
            String target = retStr.substring("template:".length());
            responseObject = new ModelAndView(null, target);
        }
    }
    if (responseObject.getClass() == String.class ||
            responseObject.getClass() == int.class || responseObject.getClass() == Integer.class
            || responseObject.getClass() == double.class || responseObject.getClass() == Double.class
            || responseObject.getClass() == float.class || responseObject.getClass() == Float.class
            || responseObject.getClass() == char.class || responseObject.getClass() == Character.class
            || responseObject.getClass() == byte.class || responseObject.getClass() == Byte.class
            || responseObject.getClass() == long.class || responseObject.getClass() == Long.class) {
        resp.setContentType("text/plain");
        resp.getWriter().print(responseObject);
    } else if (ModelAndView.class.isAssignableFrom(responseObject.getClass())) {
        ModelAndView mv = (ModelAndView) responseObject;
        freemarker.render(mv.getModel(), mv.getView(), resp.getWriter());
    } else if (Response.class.isAssignableFrom(responseObject.getClass())) {
        Response respObj = (Response) responseObject;
        if (respObj.getContentType() != null) {
            resp.setContentType(respObj.getContentType());
        }
        if (respObj.content != null) {
            resp.getWriter().print(respObj.content);
        } else {
            resp.getOutputStream().write(respObj.data);
            resp.setContentLength(respObj.data.length);
        }
    } else {
        resp.setContentType("text/json");
        resp.getWriter().print(JSON.toJSONString(responseObject));
    }
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getContentType() == null) {
        logger.info("没有ContentType的POST");
        handle(null, req, resp);
    } else {
        logger.info(req.getContentType());
        if (req.getContentType().toLowerCase().startsWith("multipart/form-data")) {
            String boundary = MultipartParser.parseAttr(req.getHeader("Content-Type"), "boundary");
            logger.info("发现post请求:" + boundary);
            Map<String, Part> map = MultipartParser.parse(req.getInputStream(), "--" + boundary);
            handle(map, req, resp);
        } else if (req.getContentType().toLowerCase().startsWith("application/x-www-form-urlencoded")) {
            doGet(req, resp);
        } else if (req.getContentType().toLowerCase().startsWith("application/json")) {
            JSONObject json = JSON.parseObject(FileUtil.inputStreamToString(req.getInputStream(), "utf8"));
            handle(json, req, resp);
        }
    }
}
}
