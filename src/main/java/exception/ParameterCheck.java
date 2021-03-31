package exception;

import java.util.function.Function;

/**
 * 参数检查工具类
 *
 * @param <T> 任意对象
 */
public class ParameterCheck<T> {

    private final static ParameterCheck<Object> C;

    static {
        C = new ParameterCheck<>();
    }

    /**
     * 获取一个参数检查类对象
     *
     * @return ParameterCheck<Object> C;
     */
    public static ParameterCheck<?> getC() {
        return C;
    }

    /**
     * 参数校验
     * -如果传入的结果不为空，则视为参数校验错误，此时抛出 CheckException 异常，异常信息为传入的结果
     *
     * @param fun lambda表达式 (msg -> 三目表达式 ? null : “异常信息”)
     * @throws CheckException 参数校验异常，携带输入的异常信息内容
     */
    public void check(Function<T, String> fun) throws CheckException {
        if (fun.apply(null) != null) {
            throw new CheckException().cast(fun.apply(null) + "");
        }
    }

    public static void main(String[] args) {
        try {
            getC().check(msg -> 18 * 8 > 100 ? null : "不可大于一百");
            getC().check(msg -> 8 * 8 > 100 ? null : "不可大于一百");

        } catch (CheckException e) {
            e.printMsg();
        }
    }
}
