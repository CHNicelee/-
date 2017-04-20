package com.ice.dailyevent.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ice.dailyevent.R;
import com.ice.dailyevent.listener.TypeOnClickListener;

/**
 * Created by asd on 4/19/2017.
 */

public class TypeViewHolder extends RecyclerView.ViewHolder {
    TextView tv_type;
    public TypeViewHolder(View itemView, final TypeOnClickListener listener) {
        super(itemView);
        tv_type= (TextView) itemView.findViewById(R.id.tv_type);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(getAdapterPosition());
            }
        });
    }
}
