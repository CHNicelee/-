package com.ice.dailyevent.ui.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.ice.dailyevent.R;
import com.ice.dailyevent.util.ImageUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by asd on 4/16/2017.
 */

public class BottomPopView extends PopupWindow {

        private Context mContext;

        private View view;

        private Button btn_cancel,btn_changeImage,btn_default;


        public BottomPopView(final Context mContext, final View.OnClickListener itemsOnClick) {

            this.view = LayoutInflater.from(mContext).inflate(R.layout.bottom_pop, null);
            this.mContext = mContext;
            btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
            btn_changeImage = (Button) view.findViewById(R.id.btn_changeImage);
            btn_default = (Button) view.findViewById(R.id.btn_default);
            // 取消按钮
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 销毁弹出框
                    dismiss();
                }
            });

            //默认按钮
            btn_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.jay_bg);
                    try {
                        ImageUtil.saveFile(bitmap, ImageUtil.ImageName, (Activity) mContext);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    EventBus.getDefault().post(bitmap);
                    dismiss();
                }
            });

            //更换图片
            btn_changeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemsOnClick.onClick(v);
                    dismiss();
                }
            });

            // 设置外部可点击
            this.setOutsideTouchable(true);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            this.view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = view.findViewById(R.id.pop_layout).getTop();

                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });


    /* 设置弹出窗口特征 */
            // 设置视图
            this.setContentView(this.view);
            // 设置弹出窗体的宽和高
            this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
            this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

            // 设置弹出窗体可点击
            this.setFocusable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置弹出窗体的背景
            this.setBackgroundDrawable(dw);

            // 设置弹出窗体显示时的动画，从底部向上弹出
            this.setAnimationStyle(R.style.take_photo_anim);

        }





}
