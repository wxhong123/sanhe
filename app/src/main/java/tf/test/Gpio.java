package tf.test;

import android.util.Log;

public class Gpio
{
    static public native int GPIO_open(String sPort);
    static public native int GPIO_close(int fd);
    static public native int GPIO_pullHigh(int fd, int gpio);
    static public native int GPIO_pullLow(int fd, int gpio);

    static {
        try {
            System.loadLibrary("tfgpio");
            //System.loadLibrary("gpiojni");
            Log.d("zzz", "gpiojni found!");
        } catch (UnsatisfiedLinkError e) {
            Log.d("zzz", "gpiojni not found!");
        }
    }
}
