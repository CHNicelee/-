package com.ice.dailyevent.ui.impl;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ice.dailyevent.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asd on 4/15/2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected <T extends View> T f(int id){
        return (T) findViewById(id);
    }

    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(Config.SP_NAME,MODE_PRIVATE);
    }

    private List<String> permissionList;
    private PermissionCallback listener;
    public void getPermission(String[] permissions,PermissionCallback listener){
        this.listener = listener;
        permissionList = new ArrayList<>();
        if (permissions.length>0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //如果api大于23
                for (int i = 0; i < permissions.length; i++) {
                    if (checkSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(permissions[i]);
                    }
                }
                requestPermissions( permissionList.toArray(new String[permissionList.size()]),1);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode!=1) return;
        List<String> deniedList = new ArrayList<>();
        if(grantResults.length>0)
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    deniedList.add(permissions[i]);
                }
            }

        listener.onDenied(deniedList.toArray(new String[deniedList.size()]));
    }


}
interface PermissionCallback{
    void onDenied(String[] permission);
}
