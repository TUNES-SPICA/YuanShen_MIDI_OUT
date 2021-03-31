import entity.symbol.YuanShenSymbol;
import util.DD.DDRun;
import util.read.IReadFile;
import util.read.ReadFile;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static List<Queue<YuanShenSymbol>> symbolQueueList;

    public static void init() {
        System.out.println("代表我初始化了");
    }

    public static void read(String midPath) {
        ReadFile readFile = new IReadFile();
        symbolQueueList = readFile.readMML(midPath).getSymbols();
//        System.out.println("----STR----");
//        for (Queue<YuanShenSymbol> queue : symbolQueueList) {
//            while (!queue.isEmpty()) {
//                System.out.print(queue.poll().key);
//            }HODOOD
//            System.out.println();
//        }
//        System.out.println("----END----");
    }

    public static void out(List<Queue<YuanShenSymbol>> symbolQueueList) {
        CountDownLatch countDownLatch = new CountDownLatch(symbolQueueList.size());
        for (int i = 0; i < symbolQueueList.size(); i++) {
            Thread thread = new Thread(new DDRun(countDownLatch, symbolQueueList.get(i)));
            thread.setName("音轨-" + (i + 1));
            thread.start();
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) throws Exception {
        Thread.sleep(2000);
        String fileURL = "" +
                "D:\\project\\JavaWEB\\MIDI_OUT\\src\\main\\resources\\学猫叫+佛系少女+好想你+青春修炼手册 组曲.mml"
                + "";
        // 初始化qq
        init();
        // 读取实体
        read(fileURL);
        // 输出
        out(symbolQueueList);
    }
}
