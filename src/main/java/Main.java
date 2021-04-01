import entity.symbol.YuanShenSymbol;
import util.DD.DDRun;
import util.read.IReadFile;
import util.read.ReadFile;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static List<Queue<YuanShenSymbol>> symbolQueueList;

    private static final String url = "" +
            "D:\\project\\other\\MIDI_OUT\\src\\main\\resources\\学猫叫+佛系少女+好想你+青春修炼手册 组曲.mml"
            + "";

    /**
     * ******************************************************************************
     * * 流程概述: 初始化 -> 读乐谱 -> 转化音符 -> 创建多线程 -> 同时启动线程输出
     * ******************************************************************************
     * * 初始化 无操作
     * * 读乐谱 将mml文件转化为乐谱实体，拆分乐谱实体，转化为按键队列
     * * 输出   创建线程，加载按键队列。加载完毕后同时启动所有多线程，随后依次输出按键队列
     * ******************************************************************************
     */
    public static void main(String[] args) {
        // 初始化
        init();
        // 读乐谱
        read(url);
        // 输出
        out(symbolQueueList);
    }

    /**
     * 初始化操作
     */
    public static void init() {
        try {
            Thread.sleep(2000);
            System.out.println("代表我初始化了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件，获得乐谱实体
     *
     * @param midPath 文件路径
     */
    public static void read(String midPath) {
        ReadFile readFile = new IReadFile();
        symbolQueueList = readFile.readMML(midPath).getSymbols();
    }

    /**
     * 输出按键
     * 注释掉DDRan.run() 中的 Inout() 方法可以关闭输出
     *
     * @param symbolQueueList 乐谱音轨队列
     */
    public static void out(List<Queue<YuanShenSymbol>> symbolQueueList) {
        CountDownLatch countDownLatch = new CountDownLatch(symbolQueueList.size());
        for (int i = 0; i < symbolQueueList.size(); i++) {
            Thread thread = new Thread(new DDRun(countDownLatch, symbolQueueList.get(i)));
            thread.setName("音轨-" + (i + 1));
            thread.start();
            countDownLatch.countDown();
        }
    }
}
