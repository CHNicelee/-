package com.ice.dailyevent.ui.impl;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.ice.dailyevent.Config;
import com.ice.dailyevent.DragItem;
import com.ice.dailyevent.R;
import com.ice.dailyevent.adpater.EventAdapter;
import com.ice.dailyevent.bean.IEvent;
import com.ice.dailyevent.listener.EventListListener;
import com.ice.dailyevent.listener.LongPressListener;
import com.ice.dailyevent.persenter.IMainPresenter;
import com.ice.dailyevent.persenter.impl.MainPresenter;
import com.ice.dailyevent.service.LocalService;
import com.ice.dailyevent.service.RemoteService;
import com.ice.dailyevent.ui.MainView;
import com.ice.dailyevent.util.ImageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asd on 4/15/2017.
 */

public class NewMainActivity extends BaseActivity implements View.OnClickListener ,MainView,LongPressListener{

    private static final CharSequence HISTORY = "光辉历程";
    private static final String TAG = "ICE";
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ImageView imageView;
    private ProgressBar progressBar;
    private List<IEvent> events = new ArrayList<>();
    private EventAdapter adapter;
    private LinearLayout ll_tip;
    private WindowManager.LayoutParams params;
    private CoordinatorLayout container;
    private Uri uritempFile;
    private boolean isNewIEvent = true;
    private IMainPresenter mainPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.new_main_activity);
        initView();

        Toolbar toolbar = f(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Log.d(TAG, "NewMainActivityon Create: ");


        initRV();

        initFab();


        EventBus.getDefault().register(this);

        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));

        mainPresenter = new MainPresenter(this,adapter);
        checkIsFirstTime();
        mainPresenter.getUnfinishedEvents();
    }
    /**
     * 初始化各个控件
     */
    private void initView() {

        CollapsingToolbarLayout c = f(R.id.toolbarWrapper);
        c.setExpandedTitleColor(Color.WHITE);
        c.setCollapsedTitleTextColor(Color.WHITE);

        progressBar = f(R.id.progressBar);
        container = f(R.id.container);

        recyclerView = f(R.id.recyclerView);
        fab = f(R.id.fab);
        ll_tip = f(R.id.ll_tip);
        //初始化图片
        imageView = f(R.id.imageView);
        ImageUtil.setImageByPath(imageView,ImageUtil.ImagePath+ImageUtil.ImageName);
        imageView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if(sp.getBoolean(Config.UNLOCK_SHOW,false))
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        sp.edit().putBoolean(Config.IS_EXIT,false).commit();
        EventBus.getDefault().post(Config.USING);
        super.onResume();
    }

    private void checkIsFirstTime() {
        if(sp.getBoolean(Config.FIRST_TIME,true)){
            sp.edit().putBoolean(Config.FIRST_TIME,false).commit();
            mainPresenter.initData();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;

            case R.id.menu_history:
                isNewIEvent = !isNewIEvent;
                if(item.getTitle().equals(HISTORY)) {
                    //点击了历史  那么去除未完成的
                    item.setTitle("新的征程");
                    mainPresenter.getFinishedEvents();
                }else{
                    item.setTitle(HISTORY);
                    mainPresenter.getUnfinishedEvents();
                }
                break;

            case R.id.menu_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.menu_exit:
                //发送给服务  停止前台的通知 还有停止广播接收器
                EventBus.getDefault().post(Config.EXIT);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                sp.edit().putBoolean(Config.IS_EXIT,true).commit();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化floatingActionbar
     */
    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.addNewEvent();
            }
        });
    }

    /**
     * 初始化recyclerView 填充数据
     */

    private void initRV() {
        adapter = new EventAdapter(events, new EventListListener() {
            @Override
            public void onDelete(IEvent event) {
                mainPresenter.deleteEvent(event);
            }

            @Override
            public void onFinished(IEvent event) {
            }

            @Override
            public void onLongPress(final int pos) {
/*                Snackbar.make(container,"确定要删除这条记录吗？",Snackbar.LENGTH_LONG)
                        .setAction("删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mainPresenter.deleteEvent(events.remove(pos));
                                adapter.notifyItemRemoved(pos);
                            }
                        }).show();*/

                mainPresenter.finishEvent(adapter.getData().get(pos));

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper helper = new ItemTouchHelper(new DragItem(adapter));
        helper.attachToRecyclerView(recyclerView);
    }




    public IMainPresenter getPresenter(){
        return mainPresenter;
    }

    @Subscribe
    public void onNewEvent(IEvent event){
        if(events.contains(event)){
            adapter.notifyDataSetChanged();
        }else{
            events.add(event);
            adapter.notifyItemInserted(events.size());
        }
        checkRest();

    }
    @Subscribe
    public void onImageChange(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
    @Subscribe
    public void onEventEmpty(Integer integer){
        if(integer == Config.CHECK_REST)
        checkRest();
    }

    public void checkRest(){
        if(events.size() == 0){
            ll_tip.setVisibility(View.VISIBLE);
        }else {
            ll_tip.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {

        if(!sp.getBoolean(Config.UNLOCK_SHOW,false)){
            //不在锁屏显示
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //如果intent不指定category，那么无论intent filter的内容是什么都应该是匹配的。
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView:
                    showPopFormBottom(v);
                break;
        }
    }


    public void showPopFormBottom(View view) {
        BottomPopView takePhotoPopWin = new BottomPopView(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更换图片
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        //有权限  可以选择照片
                        choosePic();
                    }else{
                        getReadPermission();
                    }
                }
            }
        });
//        设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(container, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha=0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha=1f;
                getWindow().setAttributes(params);
            }
        });

//        takePhotoPopWin.lis
    }


    public void choosePic(){
        Intent intentSel = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//打开资源库
        startActivityForResult(intentSel, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //权限申请结果
        switch (requestCode) {
            case 3:
                if (resultCode == RESULT_OK) {
                    Uri imageURL = data.getData();
                    startPhotoZoom(imageURL,720);
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                        ImageUtil.saveFile(bitmap,ImageUtil.ImageName,this);
                        imageView.setImageBitmap(bitmap);
                        EventBus.getDefault().post(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    //获取读的权限
    public void getReadPermission() {
        getPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionCallback() {

            @Override
            public void onDenied(String[] permission) {
                if(permission.length>0) {
                    Snackbar.make(container,"需要读写存储的权限，否则无法设置图片",Snackbar.LENGTH_LONG)
                            .setAction("手动授权", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getAppDetailSettingIntent(NewMainActivity.this);
                                }
                            }).show();
                }else{
                    choosePic();
                }
            }
        });
    }
    //跳转到应用详情
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

    /**
     * 裁剪图片
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 9998);
        intent.putExtra("aspectY", 9999);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);

        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 4);
    }

    @Override
    public void showEvents(List<IEvent> events) {
        Log.d(TAG, "showEvents: events.size()"+events.size());
        this.events.clear();
        this.events.addAll(events);
        adapter.notifyDataSetChanged();
        if(this.events.size()==0) showTip();
        else hideTip();
    }

    @Override
    public void showTip() {
        ll_tip.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTip() {
        ll_tip.setVisibility(View.GONE);
    }

    @Override
    public void showSnackBar(String msg) {
        Snackbar.make(container,msg,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAddNewEvent() {
        startActivity(new Intent(NewMainActivity.this,EditActivity.class));
    }

    @Override
    public void showUndoDelete(final IEvent event) {
        Snackbar.make(container,"点击右方按键可撤销删除",Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainPresenter.saveEvent(event);
                        adapter.getData().add(event);
                        adapter.notifyItemInserted(adapter.getData().size()-1);
                    }
                }).show();
    }

    @Override
    public void showDeleteError(IEvent event) {
        showSnackBar("删除失败，请检查网络重试");
        adapter.getData().add(event);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showFinish(IEvent event) {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onLongPress(final int pos) {

    }
}
