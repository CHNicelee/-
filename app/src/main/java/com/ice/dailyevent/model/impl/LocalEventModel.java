package com.ice.dailyevent.model.impl;

import com.ice.dailyevent.bean.IEvent;
import com.ice.dailyevent.bean.LocalEvent;
import com.ice.dailyevent.listener.DeleteListener;
import com.ice.dailyevent.listener.GetEventListener;
import com.ice.dailyevent.listener.SaveEventListener;
import com.ice.dailyevent.model.IEventModel;
import com.ice.dailyevent.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by asd on 4/18/2017.
 */

public class LocalEventModel implements IEventModel<LocalEvent> {


    //传入true 返回已完成的数据 否则返回为未完成的数据
    private void getFinishedEvents(boolean isFinished,GetEventListener listener){
        List<LocalEvent> localEvents = new LinkedList<>(LocalEvent.findAll(LocalEvent.class));
        List<IEvent> events = new ArrayList<>();
        for (LocalEvent localEvent : localEvents) {
            if(localEvent.isFinished() == isFinished)
                events.add(localEvent);
            else
                localEvent=null;
        }
        listener.onSuccess(events);
    }

    @Override
    public void getUnfinishedEvents(GetEventListener listener) {
        getFinishedEvents(false,listener);
    }

    @Override
    public void getFinishedEvents(GetEventListener listener) {
        getFinishedEvents(true,listener);
    }

    @Override
    public void saveEvent(IEvent event, SaveEventListener listener) {
        LocalEvent localEvent = new LocalEvent(event);
        if(localEvent.save()){
            listener.onSuccess(localEvent);
        }else{
            listener.onFailed("保存失败");
        }
    }

    @Override
    public void updateEvent(IEvent event, SaveEventListener listener) {
        LocalEvent localEvent = new LocalEvent(event.getIMEI(),event.getTitle(),event.getDetail(),
                                    event.getType(),event.getCreatedTime());
        localEvent.setFinishedTime(event.getFinishTime());
        localEvent.setFinished(event.isFinished());
        int count = localEvent.update(event.getPrimaryKey());
        if(count>0){
            listener.onSuccess(event);
        }else {
            listener.onFailed("操作失败");
        }
    }



    @Override
    public void saveEvent(String title, String detail, String type, SaveEventListener listener) {
        if(title==null || title.trim().equals("")){
            listener.onFailed("事件标题不能为空！请重新输入");
        }
        Date date = new Date(System.currentTimeMillis());
        LocalEvent event = new LocalEvent(Util.getIMEI(),title,detail,type,date);
        saveEvent(event,listener);
    }

    @Override
    public void deleteEvent(IEvent event,DeleteListener listener) {
        int count = LocalEvent.delete(LocalEvent.class,event.getPrimaryKey());
        if(count>0)
            listener.onSuccess();
        else
            listener.onFailed("删除失败");
    }

    @Override
    public void finishEvent(IEvent event, SaveEventListener listener) {
        LocalEvent localEvent = new LocalEvent(event.getIMEI(),event.getTitle(),event.getDetail(),
                event.getType(),event.getCreatedTime());
        localEvent.setFinishedTime(new Date(System.currentTimeMillis()));
        localEvent.setFinished(true);
        int count = localEvent.update(event.getPrimaryKey());
        if(count>0){
            listener.onSuccess(event);
        }else {
            listener.onFailed("操作失败");
        }
    }

    @Override
    public void getAllEvents(GetEventListener listener) {
        listener.onSuccess(LocalEvent.findAll(LocalEvent.class));
    }

}
