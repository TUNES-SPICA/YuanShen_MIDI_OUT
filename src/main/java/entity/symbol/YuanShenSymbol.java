package entity.symbol;

/**
 * 一个操作单元
 */
public class YuanShenSymbol {

    /**
     * 1按键类型 2系统参数变更
     */
    public int type;

    /**
     * 键
     */
    public String key;

    /**
     * 如果type类型为1 参数为按键按下按键的时间，单位毫秒
     */
    public int param;

    public YuanShenSymbol() {
        this.param = 0;
    }

    public YuanShenSymbol(int type, String key, int param) {
        this.type = type;
        this.key = key;
        this.param = param;
    }
}
