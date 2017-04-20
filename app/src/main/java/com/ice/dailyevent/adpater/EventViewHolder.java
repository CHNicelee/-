package com.ice.dailyevent.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ice.dailyevent.R;
import com.ice.dailyevent.listener.EventListListener;

/**
 * Created by asd on 4/19/2017.
 */

public class EventViewHolder  extends RecyclerView.ViewHolder {

    public TextView tv_content;
    public TextView tv_note;
    public TextView tv_date;
    public ImageView iv_finish;
    public TextView tv_type;
    public EventViewHolder(View view, final EventListListener listener) {
        super(view);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_note = (TextView) view.findViewById(R.id.note);
        tv_date = (TextView) view.findViewById(R.id.date);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        iv_finish = (ImageView) view.findViewById(R.id.iv_finished);
        /**
         * 设置长按事件
         */
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(iv_finish.getVisibility() != View.VISIBLE)
                    listener.onLongPress(getAdapterPosition());
                return true;
            }
        });

    }
}
