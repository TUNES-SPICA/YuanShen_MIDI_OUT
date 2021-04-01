package util.DD;

import java.util.HashMap;

/**
 * 按键映射
 * 暂时只存在 o3 o4 o5 音域的按键，若以后存在其他的按键，直接扩充map即可
 * o1
 * o2
 * o3 Z X C V B N M
 * o4 A S D F G H J
 * o5 Q W E R T Y U
 * o6
 * o7
 */
public class KeyBoardMapper {

    private static final HashMap<String, Integer> o3;
    private static final HashMap<String, Integer> o4;
    private static final HashMap<String, Integer> o5;

    private static final int A = 401;
    private static final int B = 505;
    private static final int C = 503;
    private static final int D = 403;
    private static final int E = 303;
    private static final int F = 404;
    private static final int G = 405;
    private static final int H = 406;
    private static final int I = 308;
    private static final int J = 407;
    private static final int K = 408;
    private static final int L = 409;
    private static final int M = 507;
    private static final int N = 506;
    private static final int O = 309;
    private static final int P = 310;
    private static final int Q = 301;
    private static final int R = 304;
    private static final int S = 402;
    private static final int T = 305;
    private static final int U = 307;
    private static final int V = 504;
    private static final int W = 302;
    private static final int X = 502;
    private static final int Y = 306;
    private static final int Z = 501;

    static {
        o5 = new HashMap<>();
        o5.put("c", Q);
        o5.put("C", Q);
        o5.put("d", W);
        o5.put("D", W);
        o5.put("e", E);
        o5.put("E", E);
        o5.put("f", R);
        o5.put("F", R);
        o5.put("g", T);
        o5.put("G", T);
        o5.put("a", Y);
        o5.put("A", Y);
        o5.put("b", U);
        o5.put("B", U);
        o4 = new HashMap<>();
        o4.put("c", A);
        o4.put("C", A);
        o4.put("d", S);
        o4.put("D", S);
        o4.put("e", D);
        o4.put("E", D);
        o4.put("f", F);
        o4.put("F", F);
        o4.put("g", G);
        o4.put("G", G);
        o4.put("a", H);
        o4.put("A", H);
        o4.put("b", J);
        o4.put("B", J);
        o3 = new HashMap<>();
        o3.put("c", Z);
        o3.put("C", Z);
        o3.put("d", X);
        o3.put("D", X);
        o3.put("e", C);
        o3.put("E", C);
        o3.put("f", V);
        o3.put("F", V);
        o3.put("g", B);
        o3.put("G", B);
        o3.put("a", N);
        o3.put("A", N);
        o3.put("b", M);
        o3.put("B", M);
    }

    /**
     * 获取原神的按键，c d e f g h a b 转换为 a s d f g h j
     *
     * @param o   当前音阶
     * @param key 当前按键
     * @return 转换后的按键
     */
    public Integer getYuanShenKey(int o, String key) {
        if (o == 3) {
            return o3.get(key);
        } else if (o == 4) {
            return o4.get(key);
        } else if (o == 5) {
            return o5.get(key);
        } else {
            return null;
        }
    }

}
