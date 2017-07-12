package com.work.tiantianyu.businesscomponent.bean;

import java.util.Calendar;

/**
 * Created by Tina on 2017/7/12.
 * Description: 日期对象
 */

public class CalendarValueBean {
    private Calendar calendar;  //  日期
    private String dateValue;   //  日期格式化后的字符串

    public CalendarValueBean(Calendar calendar, String dateValue) {
        this.calendar = calendar;
        this.dateValue = dateValue;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }
}
