package com.ice.dailyevent.ui.impl;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ice.dailyevent.Config;
import com.ice.dailyevent.R;

/**
 * Created by asd on 4/16/2017.
 */

public class SettingActivity extends BaseActivity  {

    Switch switch_unlockShow;
    private String TAG="ICE";
    private Uri uritempFile;
    private View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        rootView = f(R.id.ll_root);


        Toolbar toolbar = f(R.id.toolbar);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        initSwitch();

    }

    private void initSwitch() {
        boolean unlockShow = sp.getBoolean(Config.UNLOCK_SHOW,false);
        if(unlockShow) switch_unlockShow.setChecked(true);
        switch_unlockShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sp.edit().putBoolean(Config.UNLOCK_SHOW,true).commit();
                if(isChecked){
                    //开启
                    sp.edit().putBoolean(Config.UNLOCK_SHOW,true).commit();
                    Snackbar.make(rootView,"请开启相应的权限，否则无法显示",Snackbar.LENGTH_LONG)
                            .setAction("手动开启", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getAppDetailSettingIntent(SettingActivity.this);
                                }
                            }).show();
                }else{
                    //关闭
                    sp.edit().putBoolean(Config.UNLOCK_SHOW,false).commit();
                }
            }
        });

    }

    private void initView() {
        switch_unlockShow = f(R.id.switch_unlockShow);
    }

    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
