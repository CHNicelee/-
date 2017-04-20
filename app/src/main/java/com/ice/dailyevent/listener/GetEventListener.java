package com.ice.dailyevent.listener;


import com.ice.dailyevent.bean.IEvent;

import java.util.List;

/**
 * Created by asd on 4/18/2017.
 */

public interface GetEventListener {

    void onSuccess(List<? extends IEvent> eventList);

}
