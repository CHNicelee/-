package com.ice.dailyevent.ui.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ice.dailyevent.R;
import com.ice.dailyevent.adpater.TypeAdapter;
import com.ice.dailyevent.bean.IEvent;
import com.ice.dailyevent.listener.GetTypeListener;
import com.ice.dailyevent.listener.TypeOnClickListener;
import com.ice.dailyevent.persenter.IEditPresenter;
import com.ice.dailyevent.persenter.impl.EditPresenter;
import com.ice.dailyevent.ui.EditView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by asd on 4/16/2017.
 */

public class EditActivity extends BaseActivity implements EditView, View.OnClickListener {

    FloatingActionButton fab;
    EditText et_title;
    EditText et_datail;
    RecyclerView recyclerView;
    EditText et_type;
    ProgressBar progressBar;
    IEditPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        Toolbar toolbar = f(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        presenter = new EditPresenter(this);
        fab.setOnClickListener(this);

        initRecyclerView();

    }


    private void initView() {
        fab = f(R.id.fab);
        et_title = f(R.id.et_title);
        et_datail = f(R.id.et_detail);
        recyclerView = f(R.id.recyclerView);
        et_type = f(R.id.et_type);
        progressBar = f(R.id.progressBar);
    }

    private void initRecyclerView() {
        presenter.getTypes(new GetTypeListener() {
            @Override
            public void onSuccess(final List<String> typeList) {
                TypeAdapter adpater = new TypeAdapter(typeList, new TypeOnClickListener(){
                    @Override
                    public void onClick(int pos) {
                        et_type.setText(typeList.get(pos));
                    }
                });
                recyclerView.setAdapter(adpater);
                recyclerView.setLayoutManager(new GridLayoutManager(EditActivity.this,4));
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }
    @Override
    public void showSaving() {
        fab.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSaving() {
        fab.setClickable(true);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public String getEventTitle() {
        return et_title.getText().toString();
    }

    @Override
    public String getDetail() {
        return et_datail.getText().toString();
    }

    @Override
    public String getType() {
        return et_type.getText().toString();
    }

    @Override
    public void toSuccessView(IEvent event) {
        EventBus.getDefault().post(event);
        finish();
    }

    @Override
    public void showError(String msg) {
        Snackbar.make(f(R.id.ll_wrap),msg,Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                    presenter.saveEvents();
                break;
        }
    }



}
