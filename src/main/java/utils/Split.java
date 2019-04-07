package utils;

import java.util.Arrays;

//拆分工具类
public class Split {

/**
 * 将字符串s在pos处拆分，使得尽量合理：如在换行处折开，或者在，。处折开
 */
public static int goodSplit(String s, int pos) {
    if (pos >= s.length()) return s.length();
    int sep = -1;
    for (int i = pos; i >= 0; i--) {
        if (s.charAt(i) == '\n') {
            sep = i;
            break;
        }
    }
    if (sep < pos / 2) {
        sep = -1;
    }
    if (sep != -1) {
        return sep;
    }
    for (int i = pos; i >= 0; i--) {
        if ("。!?.！？".indexOf(s.charAt(i)) != -1) {
            sep = i;
            break;
        }
    }
    if (sep < pos / 2) {
        sep = -1;
    }
    if (sep != -1) {
        return sep;
    }
    return pos;
}
/**
 * 利用fisher准则对一个double数组进行划分
 * 例如：1.5,1.4,0.3,0.2分成1.5,1.4和0.3,0.2两部分，返回值为1.4，表示大于1.4的归为一类
 */
private static class NumberSplit {
    static double mean(double[] score, int beg, int end) {
        double s = 0;
        for (int i = beg; i < end; i++) {
            s += score[i];
        }
        if (end - beg == 0) return 0;
        return s / (end - beg);
    }

    static double squareRoot(double[] score, int beg, int end, double mean) {
        double s = 0;
        for (int i = beg; i < end; i++) {
            s += (score[i] - mean) * (score[i] - mean);
        }
        return Math.sqrt(s);
    }

    static double fisher(double[] score, int pos) {
        double m1 = mean(score, 0, pos), m2 = mean(score, pos, score.length);
        double s1 = squareRoot(score, 0, pos, m1), s2 = squareRoot(score, pos, score.length, m2);
        double z = (s1 + s2) / ((m1 - m2) * (m1 - m2));
        return z;
    }

    static boolean allsame(double[] score) {
        for (int i = 1; i < score.length; i++) {
            if (score[i] != score[i - 1]) {
                return false;
            }
        }
        return true;
    }

    static double split(double[] scores) {
        if (scores == null || scores.length == 0) return Double.MIN_VALUE;
        if (allsame(scores)) return scores[0];
        Arrays.sort(scores);
        double bestZ = Double.MAX_VALUE;
        double pos = scores[0];
        for (int i = 0; i < scores.length; i++) {
            double z = fisher(scores, i);
            if (z < bestZ) {
                pos = scores[i];
                bestZ = z;
            }
        }
        return pos;
    }

    /**
     * split函数的优化版O(n*lg(n))+O(n)
     */
    static double splitFast(double[] scores) {
        if (scores == null || scores.length == 0) return Double.MIN_VALUE;
        Arrays.sort(scores);
        if (scores[0] == scores[scores.length - 1]) return scores[0];
        double sum = 0;
        double sumSquare = 0;
        for (int i = 0; i < scores.length; i++) {
            sum += scores[i];
            sumSquare += scores[i] * scores[i];
        }
        double bestZ = Double.MAX_VALUE;
        double pos = scores[0];
        double s = 0, sSquare = 0;
        for (int i = 0; i < scores.length - 1; i++) {
            s += scores[i];
            sSquare += scores[i] * scores[i];
            double m1 = s / (i + 1), m2 = (sum - s) / (scores.length - i - 1);
            double s1 = sSquare / (i + 1) - m1 * m1, s2 = (sumSquare - sSquare) / (scores.length - 1 - i) - m2 * m2;
            double z = (s1 + s2) / ((m1 - m2) * (m1 - m2));
            if (bestZ > z) {
                bestZ = z;
                pos = scores[i + 1];
            }
        }
        return pos;
    }
}

public static double goodSplit(double[] scores) {
    return NumberSplit.splitFast(scores);
}

}
