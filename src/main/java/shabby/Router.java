package shabby;


import com.alibaba.fastjson.JSON;
import multipart.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.bean.Gs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Router {
static Logger logger = LoggerFactory.getLogger(Router.class);

/**
 * 像Servlet一样，Router里面的对象都是单例模式
 */
static final Map<String, Object> ma = new HashMap<>();

static Object loadInstance(String className) {
    Object obj = ma.get(className);
    if (obj != null) return obj;
    synchronized (ma) {
        try {
            Class<?> cls = Router.class.getClassLoader().loadClass(Config.packageName + "." + className);
            obj = cls.newInstance();
            ma.put(className, obj);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return null;
}

static Object route(Map<String, ?> params, String url) {
    logger.info(url + " is visited");
    int end = url.indexOf('?');
    if (end == -1) end = url.length();
    String shabby = Config.servletPath.substring(0, Config.servletPath.length() - 1);
    int beg = url.indexOf(shabby) + shabby.length();
    String path = url.substring(beg, end);
    String pathItems[] = path.split("/");
    String className = pathItems[0];
    String method = pathItems[1];
    logger.info(className + " 类名");
    logger.info(method + " 方法名");
    logger.info("参数:" + JSON.toJSONString(params, true));
    Object obj = loadInstance(className);
    return handle(method, params, obj);
}

static Object handle(String method, Map<String, ?> params, Object obj) {
    try {
        Class<?> cls = obj.getClass();
        Method[] methods = cls.getDeclaredMethods();
        Method m = null;
        for (Method me : methods) {
            if (me.getName().equals(method)) {
                m = me;
                break;
            }
        }
        if (m == null) {
            logger.error(obj.getClass().getName() + "没有名为" + method + "的函数");
            return null;
        }
        if (params == null || params.size() == 0) {
            Object ans = m.invoke(obj);
            return ans;
        }
        Annotation[][] ano = m.getParameterAnnotations();
        Object[] objects = new Object[ano.length];
        Class<?>[] types = m.getParameterTypes();
        for (int ind = 0; ind < ano.length; ind++) {
            Annotation[] i = ano[ind];
            for (Annotation j : i) {
                if (j.annotationType() == Param.class) {
                    String key = j.annotationType().getDeclaredMethod("value").invoke(j).toString();
                    Object value = params.get(key);
                    Object v = Gs.seven(value, types[ind]);
                    if (v != null) {
                        objects[ind] = v;
                    } else if (types[ind] == Map.class) {
                        objects[ind] = params;
                    } else if (types[ind] == Part.class) {
                        if (value.getClass() == Part.class) {
                            objects[ind] = value;
                        } else {
                            throw new RuntimeException("目标函数想要接受Part类型参数，而传入参数类型为" + value.getClass());
                        }
                    } else {
                        objects[ind] = Gs.mapToBean(params, types[ind]);
                    }
                }
            }
        }
        logger.info("解析得到的参数：" + JSON.toJSONString(objects, true));
        Object json = null;
        switch (objects.length) {
            case 0:
                json = m.invoke(obj);
                break;
            case 1:
                json = m.invoke(obj, objects[0]);
                break;
            case 2:
                json = m.invoke(obj, objects[0], objects[1]);
                break;
            case 3:
                json = m.invoke(obj, objects[0], objects[1], objects[2]);
                break;
            case 4:
                json = m.invoke(obj, objects[0], objects[1], objects[2], objects[3]);
                break;
            case 5:
                json = m.invoke(obj, objects[0], objects[1], objects[2], objects[3], objects[4]);
                break;
            case 6:
                json = m.invoke(obj, objects[0], objects[1], objects[2], objects[3], objects[4], objects[5]);
                break;
            default:
                throw new RuntimeException("最多6个参数");
        }
        return json;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

}
