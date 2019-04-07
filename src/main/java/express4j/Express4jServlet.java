package express4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class Express4jServlet extends HttpServlet {
App app = App.getApp();

@Override
public void init(ServletConfig config) throws ServletException {
    String main = config.getInitParameter("main");
    try {
        Class<?> cls = this.getClass().getClassLoader().loadClass(main);
        Object inst = cls.newInstance();
        Method initMethod = cls.getMethod("init");
        initMethod.invoke(inst);
    } catch (Exception e) {
        e.printStackTrace();
    }
    super.init(config);
}

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    app.handleGet(req, resp);
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    app.handlePost(req, resp);
}
}
