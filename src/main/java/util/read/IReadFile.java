package util.read;

import entity.GlobalVariablesEntity;
import entity.YuanShenEntity;
import entity.symbol.YuanShenSymbol;
import exception.CheckException;
import exception.ParameterCheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class IReadFile extends method implements ReadFile {

    @Override
    public YuanShenEntity readMML(String fileURL) {
        FileInputStream fis;
        char c;
        try {
            // 建立管道,FileInputStream文件输入流类用于读文件
            fis = new FileInputStream(new File(fileURL));
            c = (char) fis.read();
            {
                // 过滤头部分
                List<String> track = readTextToCount(fis, null, new ArrayList<>(), 0);
                System.out.println("----STR----");
                for (String str : track) {
                    System.out.println(str);
                }
                System.out.println("----END----");
                System.out.println();
                List<Queue<YuanShenSymbol>> symbolQueueList = countsToSymbol(track);
                YuanShenEntity yuanShenEntity = new YuanShenEntity();
                yuanShenEntity.setSymbols(symbolQueueList);
                return yuanShenEntity;
                //开始读取
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CheckException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

class method {

    /**
     * 读取文件转换为内容
     * 此处采用递归调用文件，如果内容头部为 null [Settings]则直接跳过，直到下一个内容头部开头为'['的字符。如果内容头部为[3MLE EXTENSION]则结束递归
     * 判断头文件 -> 记载由[Channel?]开头的内容，直至下一个[开头 -> 判断是否存在[3MLE EXTENSION] 跳出循环
     * 0x0000000A new char(10) 换行
     * 0x0000000D new char(13) 换行
     * 0x0000005B new char(91) '['
     *
     * @param fis   FileInputStream文件流
     * @param title 头部标签
     * @param data  返回的数据实体
     * @param count 计数器
     * @throws IOException io异常
     */
    List<String> readTextToCount(FileInputStream fis, StringBuffer title, List<String> data, int count) throws IOException, CheckException {
        ParameterCheck.getC().check(s -> count > 18 ? EnumMml.error : null);
        if (title != null && EnumMml.tail.contentEquals(title)) {
            return data;
        }
        {
            StringBuffer sb = new StringBuffer();
            char c = (char) fis.read();
            // 如果 title为'[' 则进行拼接，否则跳过
            if (title != null && String.valueOf((char) 0x0000005B).contentEquals(title)) {
                sb.append(title);
            } else {
                while (c != 0x0000005B) {
                    c = (char) fis.read();
                }
            }
            // 将以'['开头的内容拼接成完整的一行
            while (c != 0x0000000A && c != 0x0000000D) {
                sb.append(c);
                c = (char) fis.read();
            }
            // if 为 [Channel?]开头的内容 则进行内容读取
            if (!EnumMml.head.contentEquals(sb) && !EnumMml.tail.contentEquals(sb)) {
                sb = new StringBuffer();
                while (c != 0x0000005B) {
                    if (c != 0x0000000A && c != 0x0000000D) {
                        {
                            // 清楚注释
                            if (c == '/') {
                                for (int i = 0; i < 10; i++) {
                                    c = (char) fis.read();
                                }
                            }
                        }
                        {
                            // 置换连音符
                            if (c == '&') {
                                c = 'r';
                            }
                        }

                        sb.append(c);
                    }
                    c = (char) fis.read();
                }
                data.add(sb.toString());
                sb = new StringBuffer(c + "");
            }
            // 继续调用方法
            return readTextToCount(fis, sb, data, count + 1);
        }
    }


    /**
     * 将内容转化为操作单元
     * 对String的内容进行读取，拆分每一个操作进入队列
     * for(counts)(
     * <p>
     * pointer++
     * ↓
     * str = "l1.rrrrrrrrrrrrrrrrrrrrr2.l1g+&g+&g+&g+&g+&g+4&g+16.&g+64>d+&d+&d+&d+d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+&d+4&d+16.&d+64e4&e32";
     * <p>
     * char c = String.charAt(pointer);
     * <p>
     * }
     *
     * @param counts list
     * @return 操作队列List
     */
    List<Queue<YuanShenSymbol>> countsToSymbol(List<String> counts) throws CheckException {
        List<Queue<YuanShenSymbol>> list = new ArrayList<>();
        for (String str : counts) {
            // 创建操作单元队列
            Queue<YuanShenSymbol> queue = new LinkedList<>();
            // L（音符长度）默认为四分之一音符，T（BPM）节奏默认为120，O（音域）默认为中央C,V（音量）原神没有音量暂时不考虑此参数
            GlobalVariablesEntity var = new GlobalVariablesEntity(4, 4, 120);
            // 指针,指向读取的位置
            int pointer = 0;

            // 遍历字符串 *开*冲*!
            while (pointer < str.length()) {
                char c = str.charAt(pointer);
                YuanShenSymbol symbol = new YuanShenSymbol();
                // 1按键类型 (c,d,e,f,g,a,b) || (n || r) 2系统参数变更 (l,v,o)
                symbol.type = (c >= 97 && c <= 103) || (c == 110 || (c == 114)) ? 1 : 2;
                symbol.key = c + "";
                if (symbol.type == 1) {
                    {
                        // 此处用于处理黑键，例如 e+ f-
                        if (++pointer < str.length() && (str.charAt(pointer) == '+' || str.charAt(pointer) == '-')) {
                            symbol.key = symbol.key + str.charAt(pointer);
                        } else {
                            pointer--;
                        }
                    }
                    // d4 -> note = 4 即音符d，为四分之一音符
                    // c8 -> note = 8 即音符c，为八分之一音符
                    // e16. -> note = 16+（16/2） 即按下音符e的时长为十六分之一音符 + （十六分之一音符/2）
                    // . 代表五线谱中的休止符号
                    int note = 0;
//                    ==================================================================================================================
                    // 压缩音符-暂不考虑，有 亿 点小问题
//                    if (c == 'n') {
//                        int n = 0;
//                        n = Character.getNumericValue(str.charAt(++pointer));
//                        n = n * 10 + Character.getNumericValue(str.charAt(++pointer));
//                        symbol.key = nToKey(n);
//                    } else {
//                    ==================================================================================================================
                    // 遍历，知道出现字母为止，读取其中的数字或者小数点，计算按键市场
                    while (++pointer < str.length() && str.charAt(pointer) < 97 && str.charAt(pointer) != '<' && str.charAt(pointer) != '>') {
                        c = str.charAt(pointer);
                        if (c < 58 && c > 47) {
                            note = note * 10 + Character.getNumericValue(c);
                        } else if (c == '.') {
                            if (note == 0) {
                                note = var.getL();
                            }
                            symbol.param = noteToMillisecond(var.getT(), note * 2);
                        }
                    }
//                    }
                    // 如果音符后面没有数字，则默认为全局变量 L 的时长
                    if (note == 0) {
                        note = var.getL();
                    }
                    symbol.param = symbol.param + noteToMillisecond(var.getT(), note);
                    queue.add(symbol);
                } else {
                    if (c == 'o') {
                        // o 代表音域
                        // o4 = 代表钢琴中，第四组 哆喏咪发嗦啦唏
                        // o的默认参数是4，o4也称之为中央C
                        symbol.param = Character.getNumericValue(str.charAt(++pointer));
                        queue.add(symbol);
                    } else if (c == 't') {
                        // bpm调节
                        // 此处计算有 亿 点问题
                        // 问题不大，能用就行
                        int bpm = 0;
                        while (++pointer < str.length() && Character.isDigit(str.charAt(pointer))) {
                            bpm = bpm * 10 + Character.getNumericValue(str.charAt(pointer));
                        }
                        pointer--;
                        var.setT(bpm);
                    } else if (c == 'l') {
                        // 压缩音符-暂不考虑
                        // 音轨全局节奏调节
                        int l = 0;
                        while (++pointer < str.length() && Character.isDigit(str.charAt(pointer))) {
                            l = l * 10 + Character.getNumericValue(str.charAt(pointer));
                        }
                        pointer--;
                        var.setL(l);
                        pointer++;
                    } else if (c == '<') {
                        // 降低音轨全局变量o的一个音域
                        pointer++;
                        symbol.key = "o";
                        symbol.param = var.getO() - 1;
                        var.setO(symbol.param);
                        queue.add(symbol);
                    } else if (c == '>') {
                        // 提高音轨全局变量o的一个音域
                        pointer++;
                        symbol.key = "o";
                        symbol.param = var.getO() + 1;
                        var.setO(symbol.param);
                        queue.add(symbol);
                    } else {
                        while (++pointer < str.length() && Character.isDigit(str.charAt(pointer))) {
                            //过滤 *意*义*不*明* 的字符穿~~~
                            System.out.print("• .•̀ <(你在这里干嘛!");
                        }
                    }
                }
            }
            list.add(queue);
        }
        return list;
    }

    /**
     * 音符转换为时间毫秒
     *
     * @param bpm  bpm速度
     * @param note 音符
     * @return 时间（单位毫秒）
     */
    int noteToMillisecond(int bpm, int note) {
        // 分钟/vpm/音符
        return ((60 * 1000) / bpm) * 4 / note;
    }

    /**
     * 音符n 转换为按键
     *
     * @param n 音符
     * @return 时间（单位毫秒）
     */
    String nToKey(int n) throws CheckException {
        n = (n + 1) % 12;
        switch (n) {
            case 1:
                return "c";
            case 2:
                return "c+";
            case 3:
                return "d";
            case 4:
                return "d+";
            case 5:
                return "e";
            case 6:
                return "f";
            case 7:
                return "f+";
            case 8:
                return "g";
            case 9:
                return "g+";
            case 10:
                return "a";
            case 11:
                return "a+";
            case 0:
                return "b";
        }
        throw new CheckException().cast("文件读取错误");
    }
}
