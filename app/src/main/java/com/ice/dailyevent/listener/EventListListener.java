package com.ice.dailyevent.listener;

import com.ice.dailyevent.bean.IEvent;

/**
 * Created by asd on 4/19/2017.
 */

public interface EventListListener {

    void onDelete(IEvent event);

    void onFinished(IEvent event);

    void onLongPress(int pos);
}
