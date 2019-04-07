package utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

//字符串分隔（默认使用空白如空格、换行符等分隔），变成一个字符串列表
public static List<String> splitWithEmpty(String s) {
    Scanner cin = new Scanner(s);
    List<String> ans = new ArrayList<String>();
    while (cin.hasNext()) {
        ans.add(cin.next());
    }
    cin.close();
    return ans;
}

//去掉‘-’的UUID字符串
public static String uuid() {
    return UUID.randomUUID().toString().replace("-", "");
}


public static String escapeSolrQuery(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        // These characters are part of the query syntax and must be escaped
        if ("\\+-!():^[]\"{}~*?|&;/ ".indexOf(c) != -1) {
            sb.append('\\');
        }
        sb.append(c);
    }
    return sb.toString();
}

static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD-HH-mm");

public static String simpleDateFormat() {
    return format.format(new Date());
}

public static String simpleDateFormat(long time) {
    return format.format(new Date(time));
}

public static boolean isEmpty(String s) {
    if (s == null) return true;
    return s.trim().length() == 0;
}
}
