package utils.properties;

import utils.bean.Gs;
import utils.bean.MapIgnoreCaseGetterAndSetter;
import utils.bean.StaticFieldIgnoreCaseGetterAndSetter;
import utils.io.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 高级版的property配置文件，可以使用环境变量预定义，如${user.home}
 * 其中需要检测环路（构建拓扑图）
 */
public class PropertiesLoader {
class Node {
    String key;
    Set<Node> sons = new HashSet<Node>();
    String value;
    String originalValue;
    int fatherSize = 0;

    Node(String key, String originalValue) {
        this.key = key;
        this.originalValue = originalValue;
    }
}

Map<String, String> properties = new HashMap<>();
Map<String, Node> propertyNodes = new HashMap<>();
Pattern p = Pattern.compile("\\$\\{(.+?)\\}");

private PropertiesLoader(InputStream cin, String encoding) {
    Properties p = new Properties();
    try {
        p.load(new InputStreamReader(cin, encoding));
        filt(p);
        for (Object k : p.keySet()) {
            propertyNodes.put(k.toString(), new Node(k.toString(), p.getProperty(k.toString())));

        }
        buildGraph();
        solveGraph();
        for (Map.Entry<String, Node> i : propertyNodes.entrySet()) {
            properties.put(i.getKey(), i.getValue().value);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void filt(Properties p) {
    for (Object k : p.keySet()) {
        String key = k.toString();
        String value = p.getProperty(key);
        if (value.startsWith("classpath:")) {
            p.put(key, FileUtil.getResourceAsPath(value.substring(value.indexOf(':') + 1)).toAbsolutePath().toString());
        }
    }
}

String get(String key) {
    String ans = System.getProperty(key);
    if (propertyNodes.containsKey(key)) ans = propertyNodes.get(key).value;
    return ans;
}

void getValue(Node node) {
    Matcher m = p.matcher(node.originalValue);
    int start = 0;
    StringBuilder builder = new StringBuilder(node.originalValue);
    while (m.find(start)) {
        String f = m.group(1);
        builder.replace(m.start(), m.end(), get(f));
        start = m.end();
    }
    node.value = builder.toString();
}

private void solveGraph() {
    Queue<Node> q = new LinkedList<Node>();
    for (Node i : propertyNodes.values()) {
        if (i.fatherSize == 0) {
            q.add(i);
        }
    }
    Set<Node> visited = new HashSet<Node>();
    while (!q.isEmpty()) {
        Node node = q.poll();
        getValue(node);
        visited.add(node);
        for (Node i : node.sons) {
            i.fatherSize--;
            if (i.fatherSize == 0) {
                q.add(i);
            }
        }
    }
    if (visited.size() != propertyNodes.size()) {
        StringBuilder builder = new StringBuilder();
        for (Node i : propertyNodes.values()) {
            if (!visited.contains(i)) {
                builder.append(i.key).append(" ");
            }
        }
        throw new RuntimeException("confict exists : " + builder.toString());
    }
}

private void buildGraph() {
    for (String i : propertyNodes.keySet()) {
        Node node = propertyNodes.get(i);
        String value = node.originalValue;
        Matcher m = p.matcher(value);
        int start = 0;
        Set<String> fathers = new HashSet<String>();
        while (m.find(start)) {
            String f = m.group(1);
            if (propertyNodes.get(f) == null) {
                if (System.getProperty(f) == null) {
                    throw new RuntimeException("发现未定义变量：" + f);
                }
            } else {
                propertyNodes.get(f).sons.add(node);
                fathers.add(f);
            }
            start = m.end();
        }
        node.fatherSize = fathers.size();
    }
}

public static Map<String, String> load(InputStream cin, String encoding) {
    PropertiesLoader loader = new PropertiesLoader(cin, encoding);
    return loader.properties;
}

/**
 * 把properties中的内容直接注入到静态类的对应变量名中
 * */
public static void load(InputStream cin, String encoding, Class<?> cls) {
    PropertiesLoader loader = new PropertiesLoader(cin, encoding);
    Map<String, String> newmap = new TreeMap<>();
    //去掉.
    for (String i : loader.properties.keySet()) {
        newmap.put(i.replace(".", ""), loader.properties.get(i));
    }
    MapIgnoreCaseGetterAndSetter mapGetterAndSetter = new MapIgnoreCaseGetterAndSetter(newmap);
    StaticFieldIgnoreCaseGetterAndSetter staticFieldGetterAndSetter = new StaticFieldIgnoreCaseGetterAndSetter(cls);
    Gs.assign(staticFieldGetterAndSetter, mapGetterAndSetter, staticFieldGetterAndSetter.staticFields());
}
}
