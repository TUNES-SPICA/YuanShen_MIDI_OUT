package entity;

import lombok.Data;

/**
 * 音轨变量实体
 */
@Data
public class GlobalVariablesEntity {

    private Integer L;

    private Integer O;

    private Integer T;

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
