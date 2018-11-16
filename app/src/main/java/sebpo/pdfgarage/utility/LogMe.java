package sebpo.pdfgarage.utility;

import android.util.Log;

public class LogMe {
    static final boolean LOG = true;
    static final String TAG = "ÄPP";

    public static void i(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
    }

    public static void d(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (LOG) android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (LOG) android.util.Log.w(tag, string);
    }

    public static void LOGD(String message) {
        if (LOG) {
            Log.d(TAG, message);
        }
    }

    public static void LOGE(String message) {
        if (LOG) {
            Log.e(TAG, message);
        }
    }

}