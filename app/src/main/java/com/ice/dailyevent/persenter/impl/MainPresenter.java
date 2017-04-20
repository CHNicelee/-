package com.ice.dailyevent.persenter.impl;

import com.ice.dailyevent.adpater.EventAdapter;
import com.ice.dailyevent.bean.IEvent;
import com.ice.dailyevent.bean.LocalEvent;
import com.ice.dailyevent.listener.DeleteListener;
import com.ice.dailyevent.listener.GetEventListener;
import com.ice.dailyevent.listener.SaveEventListener;
import com.ice.dailyevent.model.IEventModel;
import com.ice.dailyevent.model.impl.LocalEventModel;
import com.ice.dailyevent.persenter.IMainPresenter;
import com.ice.dailyevent.ui.MainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asd on 4/18/2017.
 */

public class MainPresenter implements IMainPresenter {

    private IEventModel<LocalEvent> eventModel;
    private MainView mainView;
    private EventAdapter adapter;

    public MainPresenter(MainView mainView,EventAdapter adapter){
        eventModel = new LocalEventModel();
        this.mainView = mainView;
        this.adapter =adapter;
    }

    @Override
    public void getUnfinishedEvents() {
        mainView.showLoading();
        eventModel.getUnfinishedEvents(new GetEventListener() {
            @Override
            public void onSuccess(List<? extends IEvent> eventList) {
                List<IEvent> events = new ArrayList<>(eventList);
                mainView.showEvents(events);
                mainView.hideLoading();
            }
        });
    }

    @Override
    public void getFinishedEvents() {
        mainView.showLoading();
        eventModel.getFinishedEvents(new GetEventListener() {
            @Override
            public void onSuccess(List<? extends IEvent> eventList) {
                List<IEvent> events = new ArrayList<>(eventList);
                mainView.showEvents(events);
                mainView.hideLoading();
            }
        });
    }

    @Override
    public void deleteEvent(final IEvent event) {
        mainView.showLoading();
        eventModel.deleteEvent(event, new DeleteListener() {
            @Override
            public void onSuccess() {
                mainView.hideLoading();
                mainView.showUndoDelete(event);
//                mainView.showSnackBar("删除成功");
            }

            @Override
            public void onFailed(String msg) {
                mainView.hideLoading();
                mainView.showDeleteError(event);
                mainView.showSnackBar("删除失败...");
            }
        });
    }

    @Override
    public void updateEvent(final IEvent event) {
        mainView.showLoading();
        eventModel.updateEvent(event, new SaveEventListener() {
            @Override
            public void onSuccess(IEvent event) {
                mainView.hideLoading();
                mainView.showSnackBar("更新完成");
                if(adapter.getData().size() == 0) mainView.showTip();
            }

            @Override
            public void onFailed(String msg) {
                adapter.onItemDissmissFailed(event);
                mainView.showSnackBar(msg);
                mainView.hideLoading();
            }
        });
    }

    @Override
    public void initData() {
        eventModel.saveEvent("提示1", "长按事件即可完成事件哟，下次打开的时候会放在光辉往事中，可从右上角菜单进入", "其他", new SaveEventListener() {
            @Override
            public void onSuccess(IEvent event) {

            }

            @Override
            public void onFailed(String msg) {

            }
        });
        eventModel.saveEvent("提示2", "想彻底删除一个事件的时候，可以向左滑动，请慎用", "其他", new SaveEventListener() {
            @Override
            public void onSuccess(IEvent event) {

            }

            @Override
            public void onFailed(String msg) {

            }
        });
        eventModel.saveEvent("提示3", "点击右上角的菜单，进入到设置，可以开启强力提示功能哟~", "其他", new SaveEventListener() {
            @Override
            public void onSuccess(IEvent event) {

            }

            @Override
            public void onFailed(String msg) {

            }
        });
        eventModel.saveEvent("提示4", "点击图片可选择自定义的背景图片，请开启权限哟~", "其他", new SaveEventListener() {
            @Override
            public void onSuccess(IEvent event) {

            }

            @Override
            public void onFailed(String msg) {

            }
        });

        getUnfinishedEvents();

    }

    @Override
    public void addNewEvent() {
        mainView.showAddNewEvent();
    }

    @Override
    public void finishEvent(IEvent event) {
        mainView.showLoading();
        event.setFinished(true);
        eventModel.finishEvent(event, new SaveEventListener() {
            @Override
            public void onSuccess(IEvent event) {
                mainView.showSnackBar("恭喜您已经完成了该事件");
                mainView.hideLoading();
                mainView.showFinish(event);
            }

            @Override
            public void onFailed(String msg) {
                mainView.showSnackBar("对不起，网络出现了故障，请重试");
                mainView.hideLoading();
            }
        });
    }

    @Override
    public void saveEvent(final IEvent event) {
        eventModel.saveEvent(event, new SaveEventListener() {
            @Override
            public void onSuccess(IEvent event) {
                mainView.showSnackBar("撤销成功");
            }

            @Override
            public void onFailed(String msg) {
                mainView.showUndoDelete(event);
            }
        });
    }
}
