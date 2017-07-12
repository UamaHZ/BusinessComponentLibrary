package com.work.tiantianyu.businesscomponent.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.work.tiantianyu.businesscomponent.R;
import com.work.tiantianyu.businesscomponent.bean.CalendarValueBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;


/**
 * Created by Tina on 2017/7/3.
 * Description: 有效期选择对话框(带滚轮效果)
 */

public class ValidDateWheelPickerDialog extends Dialog implements  View.OnClickListener {
    private Context mContext;
    private Calendar minDate;   //  初始日期
    private Calendar maxDate;   //  最大有效期
    private CalendarValueBean selectDate;    //  选中的日期;
    private OnSelectDateListener onSelectDateListener;
    private List<CalendarValueBean> calendarList;    //  有效日期列表
    private List<String> validDayList;      //  有效日期截止天数列表;
    private WheelView wheelCalendar;        //  日期选择的滚轮
    private WheelView wheelDay;             //  有几天的滚轮
    public ValidDateWheelPickerDialog(@NonNull Context context) {
        super(context, R.style.BaseDialogStyle);
        this.mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_valid_date_picker, null);
        setContentView(view);
        customLayout(view);
        setCanceledOnTouchOutside(false);
        minDate = Calendar.getInstance();
        maxDate = Calendar.getInstance();
        minDate.setTimeInMillis(getNow().getTimeInMillis());
        maxDate.setTimeInMillis(getNow().getTimeInMillis());
    }
    /**
     * 获取当天日期
     * @return 获取当天日期
     */
    private Calendar getNow(){
        return new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 设置对话框布局
     * @param v
     */
    public void customLayout(View v) {
        ButterKnife.findById(v, R.id.tv_cancel).setOnClickListener(this);
        ButterKnife.findById(v, R.id.tv_ok).setOnClickListener(this);
        wheelCalendar = ButterKnife.findById(v, R.id.options1);
        wheelDay = ButterKnife.findById(v, R.id.options2);
        wheelCalendar.setCyclic(false);
        wheelDay.setCyclic(false);
    }

    /**
     * 对话框的取消和点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            this.dismiss();
        } else if (i == R.id.tv_ok) {
            if (null != onSelectDateListener) {
                int index = wheelCalendar.getCurrentItem();
                onSelectDateListener.onSelectDate(calendarList.get(index).getCalendar());
            }
            this.dismiss();
        }
    }


    /**
     * 日期选择后回调监听
     */
    public interface OnSelectDateListener{
        void onSelectDate(Calendar date);
    }

    /**
     * 获取两个日期间的日期集合
     * @return 返回日期集合
     */
    private List<CalendarValueBean> getDatesBetweenTwoDate(Calendar beginDate, Calendar endDate){
        if(null == endDate || null == beginDate) return null;
        Calendar startDate = Calendar.getInstance();
        Calendar finishDate = Calendar.getInstance();
        startDate.setTimeInMillis(beginDate.getTimeInMillis());
        finishDate.setTimeInMillis(endDate.getTimeInMillis());
        List<CalendarValueBean> lDate = new ArrayList<>();
        while (true) {
            Calendar newDate = Calendar.getInstance();
            newDate.setTime(startDate.getTime());
            if(startDate.after(finishDate)){
                break;
            }else{
                lDate.add(new CalendarValueBean(newDate, formatDate(newDate)));
            }
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return lDate;
    }

    /**
     * 日期格式化,转为MM - dd
     * @param calendar
     * @return
     */
    private String formatDate(Calendar calendar){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取列表的日期距当前日期相差天数
     * @param calendarList
     * @return
     */
    private List<String> getDaysDifferenceList(List<CalendarValueBean> calendarList){
        List<String> dayList = new ArrayList<>();
        Calendar nowDate = Calendar.getInstance();
        nowDate.setTimeInMillis(getNow().getTimeInMillis());
        if(null != calendarList && calendarList.size() > 0){
            for (int i = 0; i < calendarList.size(); i++) {
                Calendar calendar = calendarList.get(i).getCalendar();
                long difDay = (calendar.getTimeInMillis() - nowDate.getTimeInMillis() ) / (1000 * 3600 * 24);
                dayList.add(String.format("%s天", String.valueOf(difDay + 1)));
            }
        }
        return dayList;
    }

    /**
     * 设置最小日期
     * @param minDate
     */
    public ValidDateWheelPickerDialog setMinDate(Calendar minDate) {
        this.minDate = minDate;
        return this;
    }

    /**
     * 设置最大日期
     * @param maxDate
     */
    public ValidDateWheelPickerDialog setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    /**
     * 设置选中的日期
     * @param selectDate
     */
    public ValidDateWheelPickerDialog setSelectDate(Calendar selectDate) {
        this.selectDate = new CalendarValueBean(selectDate, formatDate(selectDate));
        return this;
    }

    /**
     * 设置日期选择后监听
     * @param onSelectDateListener 日期选择后监听
     */
    public ValidDateWheelPickerDialog setOnSelectDateListener(OnSelectDateListener onSelectDateListener) {
        this.onSelectDateListener = onSelectDateListener;
        return this;
    }

    /**
     * 构建方法
     */
    private void build(){
        calendarList = getDatesBetweenTwoDate(this.minDate, this.maxDate);
        validDayList = getDaysDifferenceList(calendarList);
        int selectIndex = 0;
        if(null != selectDate){
            if(null != calendarList && 0 != calendarList.size()){
                //  当默认选中的日期大于可选范围, 则默认为最大日期
                boolean isSelectBiggerMax = 2 == compareCalendar(calendarList.get(calendarList.size() - 1).getCalendar(), selectDate.getCalendar());
                if(isSelectBiggerMax){
                    selectIndex = calendarList.size() - 1;
                }else{
                    for (CalendarValueBean item: calendarList
                            ) {
                        if(0 == compareCalendar(item.getCalendar(), selectDate.getCalendar())){
                            selectIndex = calendarList.indexOf(item);
                            break;
                        }
                    }
                }
            }

        }
        List<String> darList = new ArrayList<>();
        for (CalendarValueBean item: calendarList
             ) {
            darList.add(item.getDateValue());
        }
        wheelCalendar.setAdapter(new ArrayWheelAdapter(darList, 5));
        wheelCalendar.setCurrentItem(selectIndex);
        wheelDay.setAdapter(new ArrayWheelAdapter(validDayList, 5));
        wheelDay.setCurrentItem(selectIndex);
    }

    @Override
    public void show() {
        build();
        super.show();
    }

    /**
     * 年月日比较
     * 如果直接根据时间戳比较,可能有时分秒上面的干扰
     * @return dar1比dar2大,则返回1; dar1比dar2小,则返回2;    dar1和dar2同日,返回0
     */
    private static int compareCalendar(Calendar dar1, Calendar dar2){
        //  比较年
        if(dar1.get(Calendar.YEAR) > dar2.get(Calendar.YEAR)) return 1;
        if(dar1.get(Calendar.YEAR) < dar2.get(Calendar.YEAR)) return 2;
        //  比较月
        if(dar1.get(Calendar.MONTH) > dar2.get(Calendar.MONTH)) return 1;
        if(dar1.get(Calendar.MONTH) < dar2.get(Calendar.MONTH)) return 2;
        //  比较日
        if(dar1.get(Calendar.DAY_OF_MONTH) > dar2.get(Calendar.DAY_OF_MONTH)) return 1;
        if(dar1.get(Calendar.DAY_OF_MONTH) < dar2.get(Calendar.DAY_OF_MONTH)) return 2;
        return 0;
    }
}
