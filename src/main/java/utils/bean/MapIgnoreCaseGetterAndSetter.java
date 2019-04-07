package utils.bean;

import java.util.Map;
import java.util.TreeMap;

public class MapIgnoreCaseGetterAndSetter implements GetterAndSetter {
Map<String, Object> map;

public MapIgnoreCaseGetterAndSetter() {

}

public MapIgnoreCaseGetterAndSetter(Object obj) {
    init(obj);
}

public void init(Object obj) {
    Map<String, Object> temp = (Map<String, Object>) obj;
    map = new TreeMap<>();
    for (String i : temp.keySet()) {
        map.put(i.toLowerCase(), temp.get(i));
    }
}

@Override
public Object get(String attr) {
    return map.get(attr.toLowerCase());
}

@Override
public void set(String attr, Object valueObj) {
    map.put(attr.toLowerCase(), valueObj);
}

}
