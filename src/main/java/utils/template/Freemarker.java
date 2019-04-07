package utils.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Freemarker {
Configuration conf;

public  Freemarker(Path templatePath) {
    conf = new Configuration(new Version(2, 3, 23));
    conf.setDefaultEncoding("utf8");
    try {
        conf.setDirectoryForTemplateLoading(templatePath.toFile());
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void render(Object obj, String tem, PrintWriter out) {
    try {
        Template template = conf.getTemplate(tem);
        template.process(obj, out);
        out.flush();
    } catch (IOException | TemplateException e) {
        e.printStackTrace();
    }
}

public  String render(Object obj, String templatePath) {
    StringWriter cout = new StringWriter();
    PrintWriter writer = new PrintWriter(cout);
    render(obj, templatePath, writer);
    writer.close();
    return cout.toString();
}

public static void main(String[] args) throws IOException, TemplateException {
    Map<String, Integer> ma = new HashMap<>();
    ma.put("one", 1);
    ma.put("two", 2);
    ma.put("three", 3);
    String ans = new Freemarker(Paths.get(".")).render(ma, "haha.ftl");
    System.out.println(ans);
}
}
