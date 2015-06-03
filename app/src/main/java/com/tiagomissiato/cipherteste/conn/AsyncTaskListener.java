package com.tiagomissiato.cipherteste.conn;

/**
 * Created by trigoleto on 12/2/14.
 */
public interface AsyncTaskListener {

    void onTaskStarted(Integer taskId);

    void onTaskProgress(Integer taskId, int progress);

    void onTaskCompleted(Integer taskId, String json);

    void onTaskServerError(Integer taskId, ServerException oError);

    void onTaskFinished(Integer taskId);
}