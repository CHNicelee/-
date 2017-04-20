package com.ice.dailyevent.listener;

import java.util.List;

/**
 * Created by asd on 4/19/2017.
 */

public interface GetTypeListener {
    void onSuccess(List<String> type);
    void onFailed(String msg);
}
