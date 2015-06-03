package com.tiagomissiato.cipherteste;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tiagomissiato.cipherteste.adapter.ItemAdapter;
import com.tiagomissiato.cipherteste.config.Configuration;
import com.tiagomissiato.cipherteste.conn.AsyncTaskListener;
import com.tiagomissiato.cipherteste.conn.Conn;
import com.tiagomissiato.cipherteste.conn.ServerException;
import com.tiagomissiato.cipherteste.model.Item;
import com.tiagomissiato.cipherteste.model.ItemsServerResponse;

import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends ActionBarActivity implements AsyncTaskListener{

    LinearLayout genericProgress;
    LinearLayout noResult;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView itemsList;

    Conn conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsList = (RecyclerView) findViewById(R.id.items_list);
        noResult = (LinearLayout) findViewById(R.id.layout_no_result_founded);
        genericProgress = (LinearLayout) findViewById(R.id.layout_generic_progress);

        mLayoutManager = new LinearLayoutManager(this);
        itemsList.setLayoutManager(mLayoutManager);
        itemsList.setHasFixedSize(true);
        itemsList.setItemAnimator(new DefaultItemAnimator());

        loadData();
    }

    public void loadData(){

        if(conn != null)
            conn.cancel();

        conn = new Conn(this, Configuration.ASYNC_TASK_ITEMS, this, this);
        conn.get(Configuration.URL_ITEMS, null, true);

    }

    @Override
    public void onTaskStarted(Integer taskId) {
        genericProgress.setVisibility(View.VISIBLE);
        itemsList.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
    }

    @Override
    public void onTaskProgress(Integer taskId, int progress) {

    }

    @Override
    public void onTaskCompleted(Integer taskId, String json) {

        Gson gson = new Gson();
        ItemsServerResponse response = gson.fromJson(json, ItemsServerResponse.class);
        if(response != null && response.getConteudo() != null) {

            Collections.sort(response.getConteudo(), new Comparator<Item>() {
                @Override
                public int compare(Item s1, Item s2) {
                    return s1.getValor().compareTo(s2.getValor());
                }
            });

            ItemAdapter adapter = new ItemAdapter(this, response.getConteudo());
            itemsList.setAdapter(adapter);

            genericProgress.setVisibility(View.GONE);
            noResult.setVisibility(View.GONE);
            itemsList.setVisibility(View.VISIBLE);
        } else {
            genericProgress.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
            itemsList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTaskServerError(Integer taskId, ServerException oError) {
        genericProgress.setVisibility(View.GONE);
        noResult.setVisibility(View.VISIBLE);
        itemsList.setVisibility(View.GONE);
    }

    @Override
    public void onTaskFinished(Integer taskId) {

    }
}
