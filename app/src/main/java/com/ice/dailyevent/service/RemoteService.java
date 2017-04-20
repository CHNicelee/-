package com.ice.dailyevent.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.ice.dailyevent.IMyAidlInterface;

/**
 * Created by asd on 2/22/2017.
 */

public class RemoteService extends Service {

    private MyBinder binder;
    private MyServiceConnection conn;
    private PendingIntent pendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if(binder == null){
            binder = new MyBinder();
        }

        conn = new MyServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //连接本地服务
        this.bindService(new Intent(this,LocalService.class),conn, Context.BIND_IMPORTANT);
        //提高服务优先级，避免过多被杀掉
//        Notification notification = new Notification(R.mipmap.ic_launcher,"启动中", System.currentTimeMillis());
//        pendingIntent = PendingIntent.getService(this,0,intent,0);
//        notification.setLatestEventInfo(this,"哎哟可以","这是内容",pendingIntent);
//        notification.
//        startForeground(startId,notification);

        return START_STICKY; }

    class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }


    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //复活本地服务
            RemoteService.this.startService(new Intent(RemoteService.this,LocalService.class));
            //连接
            RemoteService.this.bindService(new Intent(RemoteService.this,LocalService.class),conn,BIND_IMPORTANT);

        }
    }
}
