package utils.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 将JavaBean转换为Map对象
 * 将Map对象转换为JavaBean对象
 * 数据类型必须匹配
 */
public class Gs {
/**
 * 将Map类型转换为Bean类型
 */
public static <T> T mapToBean(Map<String, ?> map, Class<T> cls) {
    try {
        T t = cls.newInstance();
        MapGetterAndSetter mapGs = new MapGetterAndSetter(map);
        BeanGetterAndSetter beanGs = new BeanGetterAndSetter(t);
        Gs.assign(beanGs, mapGs, map.keySet());
        return t;
    } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
    }
    return null;
}

/**
 * 根据类型将x转化为type类型的数据
 * 其中检查七种基本数据类型
 */
public static Object seven(Object x, Class<?> type) {
    Object value = null;
    if (type == int.class || type == Integer.class) {
        value = Integer.parseInt(x.toString());
    } else if (type == double.class || type == Double.class) {
        value = Double.parseDouble(x.toString());
    } else if (type == char.class || type == Character.class) {
        value = x.toString().charAt(0);
    } else if (type == float.class || type == Float.class) {
        value = Float.parseFloat(x.toString());
    } else if (type == long.class || type == Long.class) {
        value = Long.parseLong(x.toString());
    } else if (type == short.class || type == Short.class) {
        value = Short.parseShort(x.toString());
    } else if (type == String.class) {
        value = x.toString();
    } else {
        value = null;
    }
    return value;
}

static String lowerFirst(String s) {
    return s.substring(0, 1).toLowerCase() + s.substring(1);
}

public static Map<String, Object> beanToMap(Object obj) {
    BeanGetterAndSetter beanGs = new BeanGetterAndSetter(obj);
    Map<String, Object> map = new HashMap<>();
    MapGetterAndSetter mapGs = new MapGetterAndSetter(map);
    Gs.assign(mapGs, beanGs, beanGs.attrs());
    return map;
}

public static Map<String, Object> staticFieldToMap(Class<?> cls) {
    StaticFieldGetterAndSetter staticGs = new StaticFieldGetterAndSetter(cls);
    Map<String, Object> ans = new TreeMap<>();
    MapGetterAndSetter mapGs = new MapGetterAndSetter(ans);
    assign(mapGs, staticGs, staticGs.staticFields());
    return ans;
}

public static void mapToStaticField(Class<?> cls, Map<String, ?> map) {
    MapGetterAndSetter mapGs = new MapGetterAndSetter(map);
    StaticFieldGetterAndSetter staticGs = new StaticFieldGetterAndSetter(cls);
    assign(staticGs, mapGs, map.keySet());
}

public static void assign(GetterAndSetter gsDes, GetterAndSetter gsSrc, Collection<String> attrs) {
    for (String i : attrs) {
        Object obj = gsSrc.get(i);
        if (obj == null) continue;
        gsDes.set(i, obj);
    }
}

}
