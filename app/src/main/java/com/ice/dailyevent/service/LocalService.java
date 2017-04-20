package com.ice.dailyevent.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ice.dailyevent.Config;
import com.ice.dailyevent.IMyAidlInterface;
import com.ice.dailyevent.ui.impl.NewMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;

/**
 * Created by asd on 2/22/2017.
 */

public class LocalService extends Service {
    private static final String TAG = "LocalService:::";
    private MyBinder binder;
    private MyServiceConnection conn;

    private PendingIntent pendingIntent;
    private ActivityManager activityManager;
    private Timer timer;
    private final static int GRAY_SERVICE_ID = 1001;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null) {
            binder = new MyBinder();
        }
        register();
        conn = new MyServiceConnection();

        EventBus.getDefault().register(this);

    }


    boolean isExit = true;

    /**
     *注册广播接收器
     */
    public void register(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, intentFilter);
    }

    /**
     * Eventbus 接受退出服务或者开启服务
     * @param integer
     */
    @Subscribe
    public void stopService(Integer integer){
        if(integer == Config.EXIT){
            isExit = true;
            unregisterReceiver(mReceiver);
//            Log.d(TAG, "stopService: 收到退出");
            stopForeground(true);
        }else if(integer == Config.USING){
//            Log.d(TAG, "stopService: 收到USING");
            isExit = false;
            register();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //连接远程服务
        this.bindService(new Intent(this, RemoteService.class), conn, Context.BIND_IMPORTANT);

        //没有退出才需要显示通知
        if(!getSharedPreferences(Config.SP_NAME,MODE_PRIVATE).getBoolean(Config.IS_EXIT,false))
            startForeground(GRAY_SERVICE_ID, new Notification());

        return START_STICKY;
    }
    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
     class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    }

    /**
     * 屏幕监听器
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(isExit || !getSharedPreferences(Config.SP_NAME,MODE_PRIVATE).getBoolean(Config.UNLOCK_SHOW,false)){
                return;
            }
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                //wakeUpAndUnlock(LocalService.this);
            } else {
                Log.d(TAG, "onReceive:启动NewMainActivity");
                Intent it = new Intent(LocalService.this, NewMainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
            }
        }
    };


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: 不应该吧");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    class MyBinder extends IMyAidlInterface.Stub {

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
            LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
            //连接
            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), conn, BIND_IMPORTANT);

        }
    }
}
