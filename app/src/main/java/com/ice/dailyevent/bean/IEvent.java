package com.ice.dailyevent.bean;

import java.util.Date;

/**
 * Created by asd on 4/18/2017.
 */

public interface IEvent {

    /**
     * 得到手机的IMEI号  用来唯一标识
     * @return
     */
    String getIMEI();

    String getTitle();

    String getDetail();


    String getType();

    Date getFinishTime();

    boolean isFinished();

    Date getCreatedTime();

    long getPrimaryKey();

    void setFinished(boolean b);

    void setFinishTime(Date date);

}
