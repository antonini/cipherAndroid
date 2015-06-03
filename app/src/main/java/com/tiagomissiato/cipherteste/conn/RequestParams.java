package com.tiagomissiato.cipherteste.conn;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;

/**
 * Created by trigoleto on 12/2/14.
 */
public class RequestParams extends com.loopj.android.http.RequestParams{

    private static final String TAG = RequestParams.class.getName();

    public static final int METHOD_POST = 0;
    public static final int METHOD_GET = 1;

    Context mContext;

    public RequestParams() {
        setContentEncoding("ISO-8859-1");
    }

    public RequestParams(Context mContext) {
        this.mContext = mContext;

        put("device_id", RequestParams.getDeviceId(mContext));

        try {
            put("appVersion", String.valueOf(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode));
        }catch (Exception e) {
            put("appVersion", "NO_VERSION");
        }

        setContentEncoding("ISO-8859-1");
    }

    public static String getDeviceId(Context mContext){
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getAppVersion(Context mContext){

        String version = null;
        try {
            version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "0.0.0";
        }

        return version;
    }

}
