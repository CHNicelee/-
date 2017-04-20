package com.ice.dailyevent.persenter.impl;

import com.ice.dailyevent.bean.IEvent;
import com.ice.dailyevent.bean.LocalEvent;
import com.ice.dailyevent.listener.GetEventListener;
import com.ice.dailyevent.listener.GetTypeListener;
import com.ice.dailyevent.listener.SaveEventListener;
import com.ice.dailyevent.model.IEventModel;
import com.ice.dailyevent.model.impl.LocalEventModel;
import com.ice.dailyevent.persenter.IEditPresenter;
import com.ice.dailyevent.ui.EditView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by asd on 4/18/2017.
 */

public class EditPresenter implements IEditPresenter {

    IEventModel<LocalEvent> eventModel;
    EditView editView;
    String[] typeArray = {"学习","运动","阅读","开会","约会"};

    public EditPresenter(EditView editView){
        this.eventModel = new LocalEventModel();
        this.editView = editView;
    }

    @Override
    public void saveEvents() {
        editView.showSaving();
        eventModel.saveEvent(editView.getEventTitle(), editView.getDetail(), editView.getType(),
                new SaveEventListener() {
            @Override
            public void onSuccess(IEvent event) {
                editView.toSuccessView(event);
                editView.hideSaving();
            }

            @Override
            public void onFailed(String msg) {
                editView.showError(msg);
                editView.hideSaving();
            }
        });

    }


    @Override
    public void getTypes(final GetTypeListener listener) {
        eventModel.getAllEvents(new GetEventListener() {
            @Override
            public void onSuccess(List<? extends IEvent> eventList) {
                List<String> typeList = new ArrayList<String>();
                for (IEvent iEvent : eventList) {
                    if(iEvent.getType()!=null && !typeList.contains(iEvent.getType()))
                        typeList.add(iEvent.getType());
                }
                typeList.removeAll(Arrays.asList(typeArray));
                typeList.addAll(Arrays.asList(typeArray));
                listener.onSuccess(typeList);
            }
        });
    }
}
