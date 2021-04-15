package util.DD;

import entity.GlobalVariablesEntity;
import entity.symbol.InoutSymbol;
import entity.symbol.YuanShenSymbol;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class DDRun implements Runnable {

    private final CountDownLatch countDownLatch;

    private Queue<InoutSymbol> inputSymbolsQueue;
    private final Queue<YuanShenSymbol> yuanShenSymbolQueue;

    public void init() {
        inputSymbolsQueue = new LinkedList<>();
        KeyBoardMapper mapper = new KeyBoardMapper();
        GlobalVariablesEntity var = new GlobalVariablesEntity();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        while (!yuanShenSymbolQueue.isEmpty()) {
            YuanShenSymbol temp = yuanShenSymbolQueue.poll();
            InoutSymbol entity = new InoutSymbol();
            sb.append("(").append(temp.type).append(",").append(temp.key).append(",").append(temp.param).append(")");
            if (temp.type == 1) {
                sb2.append(temp.key);
                sb2.append(2000 / temp.param);
                Integer key;
                if (temp.key.length() == 1) {
                    // 白键，记录
                    key = mapper.getYuanShenKey(var.getO(), temp.key);
                } else {
                    // 黑键，降调
                    key = mapper.getYuanShenKey(var.getO(), String.valueOf(temp.key.charAt(0)));
                }
                entity.keyValue = (key == null ? 309 : key);
                entity.time = temp.param;
                inputSymbolsQueue.add(entity);
            } else {
                sb2.append("o").append(temp.param);
                if ("o".equals(temp.key)) {
                    var.setO(temp.param);
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + "音轨加载完毕" + "\n" + sb.toString() + "\n" + sb2.toString() + "\n" + inputSymbolsQueue.size());
    }

    public DDRun(CountDownLatch countDownLatch, Queue<YuanShenSymbol> yuanShenSymbolQueue) {
        this.countDownLatch = countDownLatch;
        this.yuanShenSymbolQueue = yuanShenSymbolQueue;
    }

    public void Inout() throws InterruptedException {
        while (!inputSymbolsQueue.isEmpty()) {
            InoutSymbol symbol = inputSymbolsQueue.poll();
            DD.INSTANCE.DD_key(symbol.keyValue, 1);
            DD.INSTANCE.DD_key(symbol.keyValue, 2);
            java.util.concurrent.locks.LockSupport.parkNanos(symbol.time * 1000000L);
        }
    }

    @Override
    public void run() {
        try {
            init();
            System.out.println(Thread.currentThread().getName() + "启动时间是" + System.currentTimeMillis());
            countDownLatch.await();
            //注释此处关闭按键输出
            Inout();
            System.out.println(Thread.currentThread().getName() + "输出完毕" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
