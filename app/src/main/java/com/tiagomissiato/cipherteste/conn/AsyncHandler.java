package com.tiagomissiato.cipherteste.conn;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

/**
 * Created by trigoleto on 1/7/15.
 */
class AsyncHandler extends AsyncHttpResponseHandler {

    private static final String TAG = "Conn:AsyncHandler";
    private AsyncTaskListener handler;
    private Gson gson = new Gson();
    private int taskId;

    public AsyncHandler(AsyncTaskListener handler, int taskId) {
        this.handler = handler;
        this.taskId = taskId;
    }

    @Override
    public void onStart() {
        handler.onTaskStarted(taskId);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String response;
        try {
            response = new String(responseBody, "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            response = new String(responseBody);
        }

        ServerException eError;
        try {
            eError = gson.fromJson(response, ServerException.class);
            if (eError.status >= 0) {
                handler.onTaskCompleted(taskId, response);
            } else if(eError.result == null && response.length() > 0){
                handler.onTaskCompleted(taskId, response);
            } else {
                eError = gson.fromJson(response, ServerException.class);
                eError.error = eError.getErrorByCode(eError.result.status);
                eError.error.message = eError.result.message;
                handler.onTaskServerError(taskId, eError);
            }

        } catch (Exception e){
            eError = new ServerException();
            eError.error = eError.new Errors();
            eError.error.message = "null";
            eError.error.name = "JsonSyntaxException";
            eError.error.code = ServerException.ErrorCode.JsonSyntaxException;
        }
        handler.onTaskFinished(taskId);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error){
        error.getStackTrace();

        ServerException eError = new ServerException();
        eError.error = eError.new Errors();
        eError.result = eError.new GenericResult();
        eError.result.status = ServerException.ErrorCode.SERVER_GENERIC_CODE;
        eError.result.message = error.getMessage();

        eError.error.message = error.getMessage();
        eError.error.name = error.getLocalizedMessage();
        eError.error.code = statusCode;

        handler.onTaskServerError(taskId, eError);

    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        int progressPercentage = 100 * bytesWritten/totalSize;
        handler.onTaskProgress(taskId, progressPercentage);
    }

    @Override
    public void onFinish() {
        handler.onTaskFinished(taskId);
    }
}
