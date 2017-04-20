package com.ice.dailyevent.persenter;

import com.ice.dailyevent.bean.IEvent;

/**
 * Created by asd on 4/18/2017.
 */

public interface IMainPresenter {
    void getUnfinishedEvents();
    void getFinishedEvents();
    void deleteEvent(IEvent event);
    void updateEvent(IEvent event);
    void initData();
    void addNewEvent();
    void finishEvent(IEvent event);
    void saveEvent(IEvent event);
}
