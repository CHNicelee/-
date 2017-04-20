package com.ice.dailyevent.persenter;

import com.ice.dailyevent.listener.GetTypeListener;

/**
 * Created by asd on 4/18/2017.
 */

public interface IEditPresenter {

    void saveEvents();

    void getTypes(GetTypeListener listener);
}
