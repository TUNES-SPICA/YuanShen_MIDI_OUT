package util.DD;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DD extends Library {
    DD INSTANCE = (DD) Native.loadLibrary("DD.64", DD.class);
    int DD_mov(int x, int y);
    int DD_movR(int dx, int dy);
    int DD_btn(int btn);
    int DD_whl(int whl);
    int DD_key(int ddcode, int flag);
    int DD_str(String s);
}
