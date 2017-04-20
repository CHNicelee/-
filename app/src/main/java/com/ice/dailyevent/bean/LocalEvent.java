package com.ice.dailyevent.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by asd on 4/15/2017.
 */

public class LocalEvent extends DataSupport implements IEvent{
    @Column(unique = true)
    private int id;

    private String IMEI;
    private String title;
    private String detail;
    private String type;
    private Date createdTime;
    private Date finishedTime;
    private boolean isFinished;

    public LocalEvent(){

    }



    public LocalEvent(String IMEI, String title, String detail, String type, Date createdTime) {
        this.isFinished = isFinished;
        this.IMEI = IMEI;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.createdTime = createdTime;
    }

    public LocalEvent(IEvent event) {
        this.IMEI = event.getIMEI();
        this.title = event.getTitle();
        this.detail = event.getDetail();
        this.type = event.getType();
        this.createdTime = event.getCreatedTime();
        this.isFinished = event.isFinished();
        this.finishedTime = event.getFinishTime();
        this.id = (int) event.getPrimaryKey();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof LocalEvent)){
            return false;
        }else{
           return ((LocalEvent) o).id == id;
        }
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    @Override
    public void setFinishTime(Date date) {
        finishedTime = date;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }

    @Override
    public int hashCode() {
        return id*37;
    }

    @Override
    public String getIMEI() {
        return IMEI;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Date getFinishTime() {
        return finishedTime;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public Date getCreatedTime() {
        return createdTime;
    }

    @Override
    public long getPrimaryKey() {
        return id;
    }
}
