package com.ice.dailyevent.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by asd on 4/16/2017.
 */

public class DateUtil {
    public static String getDate(){
        Calendar calendar = Calendar.getInstance();
        StringBuilder r = new StringBuilder("");
        r.append(calendar.get(Calendar.YEAR)).append("-");
        r.append((calendar.get(Calendar.MONTH)+1)).append("-");
        r.append(calendar.get(Calendar.DAY_OF_MONTH)).append("  ");
        r.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
        r.append(calendar.get(Calendar.MINUTE));
        return r.toString();
    }

    public static String getSimpleDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");//等价于now.toLocaleString()
        return sdf.format(date);
    }

    public static void main(String[] args) {
        System.out.println(getSimpleDate(new Date(System.currentTimeMillis())));
    }
}
