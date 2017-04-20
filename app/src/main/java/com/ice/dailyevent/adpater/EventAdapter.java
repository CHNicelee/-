package com.ice.dailyevent.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ice.dailyevent.R;
import com.ice.dailyevent.bean.IEvent;
import com.ice.dailyevent.listener.EventListListener;
import com.ice.dailyevent.util.DateUtil;

import java.util.List;

/**
 * Created by asd on 4/15/2017.
 */

public class EventAdapter  extends RecyclerView.Adapter<EventViewHolder>{

    private List<IEvent> events;
    private EventListListener listener;
    public EventAdapter(List<IEvent> events,EventListListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new EventViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        IEvent dailyEvent = events.get(position);
        holder.tv_content.setText(dailyEvent.getTitle());
        holder.tv_date.setText(DateUtil.getSimpleDate(dailyEvent.getCreatedTime()));
        holder.tv_note.setText(dailyEvent.getDetail()==null ? "" : dailyEvent.getDetail());
        holder.tv_type.setText(dailyEvent.getType()==null?"其他":dailyEvent.getType());
        holder.iv_finish.setVisibility(dailyEvent.isFinished()?View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public List<IEvent> getData() {
        return events;
    }

    /**
     * 侧滑完成
     * @param position
     */
    public void onItemDissmiss(final int position) {
        final IEvent dailyEvent = events.remove(position);
//        dailyEvent.setFinished(true);
//        listener.onFinished(dailyEvent);
//        notifyItemRemoved(position);
        listener.onDelete(dailyEvent);
        notifyItemRemoved(position);
    }

    public void onItemDissmissFailed(IEvent event){
//        events.add(event);
        event.setFinished(false);
        notifyDataSetChanged();
    }



}
