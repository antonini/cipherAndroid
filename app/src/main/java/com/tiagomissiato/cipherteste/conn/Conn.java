package com.tiagomissiato.cipherteste.conn;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.tiagomissiato.cipherteste.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by trigoleto on 12/2/14.
 */
public class Conn {

    public static final String MAIN_KEY = "{\"result\":";
    public static final int CONN_TIMEOUT = 20;
    private static final int METHOD_POST = 0;
    private static final int METHOD_GET = 1;
    private static final int METHOD_PUT = 2;
    private static final int METHOD_DELETE = 3;

    private static final String TAG = Conn.class.getSimpleName();

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static AsyncTaskListener handler;
    private Context mContext;
    private Activity mActivity;
    private String mUrl;
    private RequestParams mParams;
    private int mMethod;
    private boolean mRetry;
    private boolean showErrorMessage = true;
    private static int taskId;

    public Conn(AsyncTaskListener handler, int taskId, Context context, Activity mActivity) {
        Conn.handler = handler;
        Conn.taskId = taskId;
        this.mContext = context;
        this.mActivity = mActivity;

        setupHttpClient();
    }

    private void setupHttpClient() {
        client.setTimeout(CONN_TIMEOUT);
        client.addHeader("Accept", "*/*");
    }

    public void addHeader(String param, String value){
        client.addHeader(param, value);
    }

    public void get(String url, RequestParams params, boolean retry) {
        this.mUrl = url;
        this.mParams = params;
        this.mRetry = retry;
        this.mMethod = METHOD_GET;
        if (params != null) {
            Log.i(TAG, " Param: " + params.toString());
        }
        if(mContext != null) {
            if (Conn.isConnected(mContext))
                client.get(getAbsoluteUrl(url, ""), params, new AsyncHandler(handler, taskId));
            else
                showNoConnectivityDialog();
        }
    }

    public void cancel(){
        client.cancelRequests(mContext, true);
    }

    public static String getAbsoluteUrl(String relativeUrl, String baseUrl) {
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(relativeUrl);
        String url = matcher.matches() ? relativeUrl : baseUrl + relativeUrl;

        return url;
    }

    public void showNoConnectivityDialog(){

        ServerException eError = new ServerException();
        eError.error = eError.new Errors();
        eError.result.message = eError.getName(ServerException.ErrorCode.NO_CONNECTION);
        eError.result.status = ServerException.ErrorCode.NO_CONNECTION;
        eError.error.message = eError.getName(ServerException.ErrorCode.NO_CONNECTION);
        eError.error.name = eError.getName(ServerException.ErrorCode.NO_CONNECTION);
        eError.error.code = ServerException.ErrorCode.NO_CONNECTION;

        handler.onTaskServerError(taskId, eError);
        handler.onTaskFinished(taskId);

        if(mActivity != null) {
            Snackbar snackbar = Snackbar.with(mContext)
                    .text(mContext.getString(R.string.error_no_conection))
                    .type(SnackbarType.MULTI_LINE)
                    .duration(Snackbar.SnackbarDuration.LENGTH_LONG);

            snackbar.show(mActivity);
        }
    }

    public static boolean isConnected(Context context){

        ConnectivityManager connectivityManager  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wiFi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isConnected = false;

        if(wiFi != null)
            isConnected = wiFi.isConnected();

        if(mobile != null && !isConnected)
            isConnected = mobile.isConnected();

        return isConnected;

    }
}
