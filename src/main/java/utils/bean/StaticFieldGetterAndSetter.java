package utils.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 将Class的静态字段映射为Map
 */
public class StaticFieldGetterAndSetter implements GetterAndSetter {
Class<?> cls;

StaticFieldGetterAndSetter() {

}

public StaticFieldGetterAndSetter(Class<?> cls) {
    init(cls);
}

@Override
public void init(Object obj) {
    this.cls = (Class<?>) obj;
}

@Override
public Object get(String attr) {
    try {
        Field field = cls.getDeclaredField(attr);
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
        Field field = cls.getDeclaredField(attr);
        if (field.getModifiers() == 9) {
            field.set(cls, valueObj);
        }
    } catch (Exception e) {
    }
}

public Collection<String> staticFields() {
    List<String> a = new ArrayList<>();
    for (Field i : cls.getDeclaredFields()) {
        if (i.getModifiers() == 9) {
            a.add(i.getName());
        }
    }
    return a;
}
}
