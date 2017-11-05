package com.lzg.player.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 *  * Created by 智光 on 2017/9/12 17:18
 *  
 */
public class StartUtils {
    public static void startActivity(Context context, Class<?> class1) {
        Intent intent = new Intent();
        intent.setClass(context, class1);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> class1, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(context, class1);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }
}
