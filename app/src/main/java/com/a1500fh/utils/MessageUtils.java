package com.a1500fh.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Andre on 31/05/2017.
 */

public class MessageUtils {

    public static void Toast(Context context,String message, int duration)
    {
        final Toast aToast =  Toast.makeText(context, message, Toast.LENGTH_LONG);

        aToast.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                aToast.cancel();
            }
        }, duration);
    }
}
