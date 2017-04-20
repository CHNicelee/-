package com.ice.dailyevent.ui;

import com.ice.dailyevent.bean.IEvent;

import java.util.List;

/**
 * Created by asd on 4/18/2017.
 */

public interface MainView {

    void showEvents(List<IEvent> events);

    void showTip();

    void hideTip();

    void showSnackBar(String msg);

    void showLoading();

    void hideLoading();

    void showAddNewEvent();


    void showUndoDelete(IEvent event);

    void showDeleteError(IEvent event);

    void showFinish(IEvent event);
}
