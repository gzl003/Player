package com.lzg.palyer.helper;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

import com.lzg.palyer.applcation.PlayerApplication;
import com.lzg.palyer.utils.StringUtil;

/**
 * Created by gang on 16/6/2.
 */
public final class UIHelper {
    private static Toast toast;

    public static void shortToast(CharSequence text) {
        shortToast(null, text);
    }

    public static void shortToast(int resId) {
        shortToast(null, resId);
    }

    public static void shortToast(Context context, int resId) {
        toast(context, PlayerApplication.getInstance().getApplicationContext().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void shortToast(Context context, CharSequence text) {
        toast(context, text, Toast.LENGTH_SHORT);
    }

    public static void longToast(CharSequence text) {
        longToast(null, text);
    }

    public static void longToast(Context context, int resId) {
        toast(context, PlayerApplication.getInstance().getApplicationContext().getString(resId), Toast.LENGTH_LONG);
    }

    public static void longToast(Context context, int resId, Object... formatArgs) {
        toast(context, PlayerApplication.getInstance().getApplicationContext().getString(resId, formatArgs), Toast.LENGTH_LONG);
    }

    public static void longToast(Context context, CharSequence text) {
        toast(context, text, Toast.LENGTH_LONG);
    }

    /**
     * Make a toast.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #} or
     *                 {@link #}
     */
    public static void toast(Context context, CharSequence text, int duration) {
        if (context == null) {
            context = PlayerApplication.getInstance().getApplicationContext();
        }
//        Toast toast = Toast.makeText(context, text, duration);
        toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 50);
        toast.show();

//		View v = context.getLayoutInflater().inflate(R.layout.toast, null);
//		((TextView) v.findViewById(android.R.id.message)).setText(text);
//
//		Toast toast = new Toast(context);
//		toast.setGravity(Gravity.CENTER, 0, 50);
//		toast.setDuration(duration);
//		toast.setView(v);
//		toast.show();
    }

    public static void cancleToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static Uri toUri(String url) {
        if (StringUtil.isEmptyOrWhitespace(url))
            return null;
        return Uri.parse(url);
    }
}
