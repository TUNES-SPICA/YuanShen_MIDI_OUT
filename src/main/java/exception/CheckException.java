package exception;

/**
 * 校验异常抛出
 */
public class CheckException extends NullPointerException {


    public CheckException() {
    }

    private CheckException(String message) {
        super(message);
    }


    /**
     * 抛出异常接口
     * 统一头部输出信息 headerMessage
     *
     * @param message 异常信息
     * @return FileException 异常对象
     */
    public CheckException cast(String message) {
        //异常信息头部内容
        String headerMessage = "校验异常: ";
        return new CheckException(headerMessage + message);
    }

    /**
     * 返回自义定异常内容
     *
     * @return 自义定异常信息
     */
    protected String reMsg() {
        return this.getMessage();
    }

    /**
     * 打印异常信息
     */
    protected void printMsg() {
        System.out.println(this.getMessage());
    }

}
