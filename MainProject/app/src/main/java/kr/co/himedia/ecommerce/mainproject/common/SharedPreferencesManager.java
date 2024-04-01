package kr.co.himedia.ecommerce.mainproject.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREFERENCES_NAME = "my_preferences";

    public static SharedPreferences getPreferences(Context mContext){
        return mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setCustomer(Context context, int seq_cst, String cst_nm){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("seq_cst", seq_cst);
        editor.putString("cst_nm", cst_nm);

        editor.apply();
    }

    public static int getSeq_cst(Context context){
        SharedPreferences prefs = getPreferences(context);
        int seq_cst = prefs.getInt("seq_cst", 0);

        return seq_cst;
    }

    public static String getCst_nm(Context context){
        SharedPreferences prefs = getPreferences(context);
        String cst_nm = prefs.getString("cst_nm", "");

        return cst_nm;
    }

    public static void clearPreferences(Context context){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
