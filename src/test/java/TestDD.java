

import util.DD.DD;

/**
 * 驱动测试
 */
public class TestDD {

    /*
     * 原名疑似 DD XOFT
     * 采用DD 进行模拟按键输出，此方法需要DD.64.dll
     * win10 若驱动读取失败去掉系统的验证签名操作（慎重选择，系统的dll被恶意替换可能会导致出现严重问题）
     */
    public static void main(String[] args) throws InterruptedException {
        //模拟鼠标移动  并且输入qq
        Thread.sleep(200);
        System.out.println(DD.INSTANCE.DD_mov(1, 1) == 39 ? "" : "鼠标移动");
        Thread.sleep(200);
        System.out.println(DD.INSTANCE.DD_key(301, 1) == 39 ? "" : "按下键盘");
        Thread.sleep(200);
        System.out.println(DD.INSTANCE.DD_key(301, 2) == 39 ? "" : "松开键盘");
    }

}