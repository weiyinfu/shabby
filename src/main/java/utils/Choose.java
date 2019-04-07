package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Choose {
static final Random random = new Random();

//从数组a中随机选取一个
public static <T> T choose(List<T> a) {
    return a.get(random.nextInt(a.size()));
}

public static <T> T choose(T[] a) {
    return a[random.nextInt(a.length)];
}

//获取从from到to的随机数
public static int rand(int from, int to) {
    return random.nextInt(to - from) + from;
}

//从列表a中随机选取cnt个
public static <T> List<T> choose(List<T> a, int cnt) {
    if (cnt > a.size()) {
        //System.err.println("choose too many elements!");
        System.exit(-1);
    }
    List<T> temp = new ArrayList<T>(a.size());
    for (int i = 0; i < a.size(); i++) temp.add(a.get(i));
    for (int i = 0; i < cnt; i++) {
        int id = rand(i, temp.size());
        T t = temp.get(id);
        temp.set(id, temp.get(i));
        temp.set(i, t);
    }
    return temp.subList(0, cnt);
}

//轮盘赌算法抽取，从a中抽取count个，每个元素的权重对应scores数组
public static <T> List<T> choose(List<T> a, int count, double[] scores) {
    List<T> ans = new ArrayList<T>();//最终的答案
    double sum = 0;//权重的总和
    for (double i : scores)
        sum += i;
    while (ans.size() < count) {
        //产生一个随机数
        double r = Math.random() * sum;
        double s = 0;
        int i = 0;
        //下面可以用树状数组优化为logN的效率
        while (i < scores.length) {
            s += scores[i];
            if (s > r)
                break;
            i++;
        }
        ans.add(a.get(i));
        sum -= scores[i];//总权重减去这个值
        scores[i] = 0.0;//删掉这个元素
    }
    return ans;
}

}
