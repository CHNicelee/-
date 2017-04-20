package com.ice.dailyevent.ui;

import com.ice.dailyevent.bean.IEvent;

/**
 * Created by asd on 4/18/2017.
 */

public interface EditView {

    void showSaving();

    void hideSaving();

    String getEventTitle();

    String getDetail();

    String getType();

    void toSuccessView(IEvent event);

    void showError(String msg);



}
