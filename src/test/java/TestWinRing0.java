import wr0.WinRing0Util;

/**
 * 驱动测试
 */
public class TestWinRing0 {

    private static void init() {
        WinRing0Util.InitializeOls();
    }


    public static void main(String[] args) {
        init();
        /*
         * 未知原因导致winRing0驱动读取正确但是无效
         * 排查至WinRing0 C源码部分：{
         *
         *  port
         *  [in] I/O port address
         *  value
         *  [in] a BYTE value to write to the port
         *
         *  VOID WriteIoPortByte(
         *  WORD port,
         *  BYTE value
         *  );
         *
VOID WINAPI WriteIoPortByte(WORD port, BYTE value)
{
	if(gHandle == INVALID_HANDLE_VALUE){return ;}

	DWORD	returnedLength = 0;
	BOOL	result = FALSE;
	DWORD   length = 0;
    OLS_WRITE_IO_PORT_INPUT inBuf;

	inBuf.CharData = value;
	inBuf.PortNumber = port;
	length = offsetof(OLS_WRITE_IO_PORT_INPUT, CharData) +
						 sizeof(inBuf.CharData);

	result = DeviceIoControl(
						gHandle,
						IOCTL_OLS_WRITE_IO_PORT_BYTE,
						&inBuf,
						length,
						NULL,
						0,
						&returnedLength,
						NULL
						);
}
         *
         * 此接口port参数java中传值为固定，可能是不同系统port存在差异的问题？（未知）
         * }
         */
        int dllStatus = WinRing0Util.GetDllStatus();
        System.err.println("WinRing0 dllStatus:" + dllStatus);
        if (dllStatus == 0) {
            String keys = "qqq";
            int length = keys.length();
            try {
                for (int i = 0; i < length; i++) {
                    Thread.sleep(500);
                    char key = keys.charAt(i);
                    if (Character.isUpperCase(key)) {
                        WinRing0Util.keyPressWidthShift(key + "");
                        continue;
                    }
                    WinRing0Util.keyPress(key + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("1");
        }
        //销毁WinRing0
        WinRing0Util.DeinitializeOls();
    }
}
