package com.ice.dailyevent.model;

import com.ice.dailyevent.bean.IEvent;
import com.ice.dailyevent.listener.DeleteListener;
import com.ice.dailyevent.listener.GetEventListener;
import com.ice.dailyevent.listener.SaveEventListener;

/**
 * Created by asd on 4/18/2017.
 */

public interface IEventModel<T extends IEvent> {

    void getUnfinishedEvents(GetEventListener listener);

    void getFinishedEvents(GetEventListener listener);

    void saveEvent(IEvent  event, SaveEventListener listener);

    void updateEvent(IEvent event,SaveEventListener listener);

    void saveEvent(String title, String detail, String type, SaveEventListener listener);

    void deleteEvent(IEvent event,DeleteListener listener);

    void finishEvent(IEvent event,SaveEventListener listener);

    void getAllEvents(GetEventListener listener);
}
