package shabby;

import utils.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RouterTest {
/**
 * 一个参数的测试
 */
void one() {
    System.out.println("天下大势为我所控");
}

/**
 * 两个参数的测试
 */
void two(@Param("one") int x, @Param("two") int y) {
    int z = x + y;
    System.out.println(z);
}

/**
 * 三个参数的测试
 */


void three(@Param("one") String one, @Param("two") String two, @Param("three") String three) {
    System.out.println(one + two + three);
}

void paramIsObject(@Param("haha") User haha) {
    System.out.println(haha.getName() + " " + haha.getAge());
}

/**
 * 测试路由转发
 */
static void test1() {
    Map<String, String> ma = new HashMap<>();
    ma.put("one", "1");
    ma.put("two", "2");
    ma.put("three", "hello");
    Object x = Router.handle("one", ma, new Router());
    Router.handle("two", ma, new Router());
    Router.handle("three", ma, new Router());
}

/**
 * 测试URL分析
 */
static void test2() {
    String url = "http://localhost:8081/shabby/Tuning/add?one=1&two=2";
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("one", "1");
    map.put("two", "2");
    System.out.println(Router.route(map, url));
}

static void test3() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", "1");
    map.put("age", "2");
    Router.handle("paramIsObject", map, new Router());
}

public int add(int one, int two) {
    return one + two;
}

static void test4() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    RouterTest test = new RouterTest();
    Method method = test.getClass().getMethod("add", int.class, int.class);
    Object ans = method.invoke(test, 1, 2);
    System.out.println(ans);
    ans = method.invoke(test, new int[]{1, 2});
    System.out.println(ans);
}

public static void main(String[] args) throws Exception {
//    test3();
    test4();
}
}
