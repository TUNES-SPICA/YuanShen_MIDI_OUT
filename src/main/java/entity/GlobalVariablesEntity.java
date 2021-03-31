package entity;

import lombok.Data;

/**
 * 音轨变量实体
 */
@Data
public class GlobalVariablesEntity {

    /**
     * 音符间隙
     * L = 8，那就代表默认音符为8分之一音符。L默认 = 4
     */
    private Integer L;

    /**
     * 音域
     * 初始化时，音域为中央C，即 O = 4。此参数会随><o三个参数浮动
     */
    private Integer O;

    /**
     * BPM
     * 一分钟多少拍，默认为 BPM = 120
     */
    private Integer T;

    /**
     * 音量
     * 原神无此参数
     */
    private Integer V;

    public GlobalVariablesEntity() {
        L = 4;
        O = 4;
        T = 120;
    }

    public GlobalVariablesEntity(Integer l, Integer o, Integer t) {
        L = l;
        O = o;
        T = t;
    }

    public GlobalVariablesEntity(Integer l, Integer o, Integer t, Integer v) {
        L = l;
        O = o;
        T = t;
        V = v;
    }
}
