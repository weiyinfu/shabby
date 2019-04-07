package utils.bean;

import com.alibaba.fastjson.JSON;
import utils.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

public class BeanIgnoreCaseGetterAndSetter extends BeanGetterAndSetter {
Map<String, Method> methodMap = new TreeMap<>();

public BeanIgnoreCaseGetterAndSetter() {

}

public BeanIgnoreCaseGetterAndSetter(Object obj) {
    init(obj);
}

@Override
public void init(Object obj) {
    this.obj = obj;
    for (Method m : obj.getClass().getMethods()) {
        methodMap.put(m.getName().toLowerCase(), m);
    }
}

@Override
public Object get(String attr) {
    Method m = methodMap.get("get" + attr);
    if (m == null) return null;
    try {
        return m.invoke(obj);
    } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
    }
    return null;
}

@Override
public void set(String attr, Object valueObj) {
    Method m = methodMap.get("set" + attr);
    if (m != null) {
        try {
            m.invoke(obj, valueObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

public Method getter(String attr) {
    return methodMap.get("get" + attr.toLowerCase());
}

public Method setter(String attr) {
    return methodMap.get("set" + attr.toUpperCase());
}

public static void main(String[] args) {
    User haha = new User();
    BeanIgnoreCaseGetterAndSetter getterAndSetter = new BeanIgnoreCaseGetterAndSetter();
    getterAndSetter.init(haha);
    getterAndSetter.set("name", "wyf");
    getterAndSetter.set("age", 19);
    System.out.println(JSON.toJSONString(getterAndSetter.attrs(), true));
    System.out.println(JSON.toJSONString(haha, true));
}
}
