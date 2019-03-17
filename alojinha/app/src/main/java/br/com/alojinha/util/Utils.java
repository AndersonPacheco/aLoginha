package br.com.alojinha.util;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class Utils {

    public static Locale getCurrentLocale(Context c){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return c.getResources().getConfiguration().getLocales().get(0);
        } else {
            return c.getResources().getConfiguration().locale;
        }
    }
}
