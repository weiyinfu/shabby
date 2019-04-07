自己实现一个web框架，从Servlet写起。厌倦了SpringBoot的臃肿，可以试一下此web程序。现在我开始用vert.x，此库的风格和express极为接近。

在web.xml中添加shabby路由器和express风格的主要APP。
```xml
<!--shabby框架的核心servlet-->
<servlet>
    <servlet-name>shabby</servlet-name>
    <servlet-class>shabby.RouterHttpServlet</servlet-class>
    <init-param>
        <!--设置shabby框架可以检测的包-->
        <param-name>package</param-name>
        <param-value>controller.shabby</param-value>
    </init-param>
</servlet>
<!--express4j框架的核心servlet-->
<servlet>
    <servlet-name>express4j</servlet-name>
    <servlet-class>express4j.Express4jServlet</servlet-class>
    <init-param>
        <param-name>main</param-name>
        <param-value>controller.express4j.Main</param-value>
    </init-param>
</servlet>
```
配置servlet映射
```xml
<!--shabby框架-->
<servlet-mapping>
    <servlet-name>shabby</servlet-name>
    <url-pattern>/shabby/*</url-pattern>
</servlet-mapping>
<!--express4j框架-->
<servlet-mapping>
    <servlet-name>express4j</servlet-name>
    <url-pattern>/express4j/*</url-pattern>
</servlet-mapping>

```
使用shabby之后，可以像写普通函数那样书写接口，测试特别简单。
```java
package controller.shabby;

import com.alibaba.fastjson.JSONArray;
import poemqa.TestReport;
import shabby.Param;
import shabby.Response; 
import synthesizer.PinyinSynthesizer;
import utils.io.FileUtil;

public class Haha {
RobotSingleton singleton = new poemqa.RobotSingleton();
JSONArray report;
PinyinSynthesizer synthesizer = new PinyinSynthesizer();

public String haha(@Param("q") String query) {
    return query+" is received";
}

public static void main(String[] args) {
    Haha haha = new Haha(); 
    System.out.println(x.haha('hello'));
}
}

```
使用express4j后，只需要在init函数中写明一切路由即可。
```java
package controller.express4j;

import express4j.App;

import java.io.IOException;

public class Main {
public void init() {
    App app = App.getApp();
    app.get("/hello", (req, resp) -> {
        try {
            resp.getWriter().print(req.getQueryString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
}
}

```