package utils.bean;

//属性获取、设置接口
public interface GetterAndSetter {
void init(Object obj);

Object get(String attr);

void set(String attr, Object valueObj);
}