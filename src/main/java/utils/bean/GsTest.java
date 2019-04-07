package utils.bean;

import com.alibaba.fastjson.JSON;
import utils.User;

import java.util.Map;

import static utils.bean.Gs.beanToMap;
import static utils.bean.Gs.mapToBean;

public class GsTest {

static void test1() {
    User haha = new User();
    haha.setAge(20);
    haha.setName("weidiao");
    Map<String, Object> res = beanToMap(haha);
    System.out.println(JSON.toJSONString(res));
    User ha = mapToBean(res, User.class);
    System.out.println(JSON.toJSONString(ha));
}

public static void main(String[] args) {
    test1();
}
}
