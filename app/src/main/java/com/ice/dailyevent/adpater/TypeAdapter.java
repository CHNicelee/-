package com.ice.dailyevent.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ice.dailyevent.R;
import com.ice.dailyevent.listener.TypeOnClickListener;

import java.util.List;

/**
 * Created by asd on 4/19/2017.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeViewHolder>{

    List<String> typeList;
    TypeOnClickListener listener;

    public TypeAdapter(List<String> typeList, TypeOnClickListener listener) {
        this.typeList = typeList;
        this.listener = listener;
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item,parent,false);
        return new TypeViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, int position) {
        holder.tv_type.setText(typeList.get(position));
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}
