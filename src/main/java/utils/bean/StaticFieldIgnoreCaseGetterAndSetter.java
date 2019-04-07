package utils.bean;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * 将Class的静态字段映射为Map
 */
public class StaticFieldIgnoreCaseGetterAndSetter implements GetterAndSetter {
Class<?> cls;
Map<String, Field> map = new TreeMap<>();

StaticFieldIgnoreCaseGetterAndSetter() {

}

public StaticFieldIgnoreCaseGetterAndSetter(Class<?> cls) {
    init(cls);
}

@Override
public void init(Object obj) {
    this.cls = (Class<?>) obj;
    for (Field i : cls.getDeclaredFields()) {
        if (i.getModifiers() == 9) {
            map.put(i.getName().toLowerCase(), i);
        }
    }
}

@Override
public Object get(String attr) {
    try {
        Field field = map.get(attr.toLowerCase());
        if (field == null) return null;
        if (field.getModifiers() == 9) {
            return field.get(cls);
        } else {
            return null;
        }
    } catch (Exception e) {
    }
    return null;
}

@Override
public void set(String attr, Object valueObj) {
    try {
        Field field = map.get(attr.toLowerCase());
        if (field == null) return;
        if (field.getModifiers() == 9) {
            field.set(cls, valueObj);
        }
    } catch (Exception e) {
    }
}

public Collection<String> staticFields() {
    return map.keySet();
}
}
