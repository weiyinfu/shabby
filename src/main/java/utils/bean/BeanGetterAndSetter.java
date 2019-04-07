package utils.bean;

import com.alibaba.fastjson.JSON;
import utils.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

//Java Bean属性获取、设置接口
public class BeanGetterAndSetter implements GetterAndSetter {
Object obj;
//缓存下来，避免每次都遍历
static Map<Class<?>, Set<String>> classAttrs = new HashMap<>();

public BeanGetterAndSetter() {

}

public BeanGetterAndSetter(Object obj) {
    init(obj);
}

public void init(Object obj) {
    this.obj = obj;
}

String getMethod(String attr) {
    return "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
}

String setMethod(String attr) {
    return "set" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
}

@Override
public Object get(String attr) {
    try {
        return obj.getClass().getMethod(getMethod(attr)).invoke(obj);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

@Override
public void set(String attr, Object valueObj) {
    try {
        obj.getClass().getMethod(setMethod(attr), valueObj.getClass()).invoke(obj, valueObj);
    } catch (Exception e) {
    }
}

public Set<String> attrs() {
    Set<String> a = classAttrs.get(obj.getClass());
    if (a != null) return a;
    a = new TreeSet<>();
    for (Method m : obj.getClass().getMethods()) {
        if (m.getName().equalsIgnoreCase("getclass")) continue;
        if (m.getName().startsWith("get") || m.getName().startsWith("set")) {
            a.add(Gs.lowerFirst(m.getName().substring(3)));
        }
    }
    classAttrs.put(obj.getClass(), a);
    return a;
}

public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    User haha = new User();
    BeanGetterAndSetter getterAndSetter = new BeanGetterAndSetter();
    getterAndSetter.init(haha);
    getterAndSetter.set("name", "wyf");
    getterAndSetter.set("age", 19);
    System.out.println(JSON.toJSONString(getterAndSetter.attrs()));
    System.out.println(JSON.toJSONString(haha, true));
}

}