package com.ice.dailyevent.listener;

import com.ice.dailyevent.bean.IEvent;

/**
 * Created by asd on 4/18/2017.
 */

public interface SaveEventListener {
    void onSuccess(IEvent event);
    void onFailed(String msg);
}
