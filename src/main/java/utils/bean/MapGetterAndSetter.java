package utils.bean;

import java.util.Map;

//Map属性获取设置接口
public class MapGetterAndSetter implements GetterAndSetter {
Map<String, Object> map;

public MapGetterAndSetter() {

}

public MapGetterAndSetter(Object obj) {
    init(obj);
}

public void init(Object obj) {
    this.map = (Map<String, Object>) obj;
}

@Override
public Object get(String attr) {
    return map.get(attr);
}

@Override
public void set(String attr, Object valueObj) {
    map.put(attr, valueObj);
}

}