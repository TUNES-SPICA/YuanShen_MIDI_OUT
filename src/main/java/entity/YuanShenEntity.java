package entity;

import entity.symbol.YuanShenSymbol;
import lombok.Data;

import java.util.List;
import java.util.Queue;

/**
 * 原神乐谱实体
 */
@Data
public class YuanShenEntity {

    // 内容 单位为一个操作单元
    private List<Queue<YuanShenSymbol>> symbols;

    public void print() {
        System.out.println("(" + "轨道数: " + symbols.size() + ")");
        for (int i = 0; i < symbols.size(); i++) {
            System.out.print("(" + "path" + i + ":" + ")");
            System.out.println();
        }
    }

}
