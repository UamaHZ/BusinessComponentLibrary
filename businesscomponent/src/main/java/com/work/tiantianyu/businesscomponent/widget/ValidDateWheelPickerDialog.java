package com.work.tiantianyu.businesscomponent.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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

public class ValidDateWheelPickerDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private Calendar minDate;   //  初始日期
    private Calendar maxDate;   //  最大有效期
    private CalendarValueBean selectDate;    //  选中的日期;
    private OnSelectDateListener onSelectDateListener;
    private List<CalendarValueBean> calendarList;    //  有效日期列表
    private List<String> validDayList;      //  有效日期截止天数列表;
    private WheelView wheelCalendar;        //  日期选择的滚轮
    private WheelView wheelDay;             //  有几天的滚轮
    private View rootView;
    //  属性值
    private String pattern;  //  内容格式化形式
    private ViewHolder viewHolder;
    private CustomDialogListener mCustomDialogListener;
    public ValidDateWheelPickerDialog(@NonNull Context context) {
        super(context, R.style.BaseDialogStyle);
        this.mContext = context;
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_valid_date_picker, null);
        rootView = view;
        setContentView(view);
        customLayout(view);
        viewHolder = new ViewHolder(view);
        setCanceledOnTouchOutside(false);
        minDate = Calendar.getInstance();
        maxDate = Calendar.getInstance();
        minDate.setTimeInMillis(getNow().getTimeInMillis());
        maxDate.setTimeInMillis(getNow().getTimeInMillis());
        pattern = "MM-dd";
    }

    /**
     * 获取当天日期
     *
     * @return 获取当天日期
     */
    private Calendar getNow() {
        return new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 设置对话框布局
     */
    private void customLayout(View v) {
        ButterKnife.findById(v, R.id.tv_cancel).setOnClickListener(this);
        ButterKnife.findById(v, R.id.tv_ok).setOnClickListener(this);
        wheelCalendar = ButterKnife.findById(v, R.id.options1);
        wheelDay = ButterKnife.findById(v, R.id.options2);
        wheelCalendar.setCyclic(false);
        wheelDay.setCyclic(false);
    }

    /**
     * 对对话框简单的文字内容做处理
     * @param mCustomDialogListener
     * @return
     */
    public ValidDateWheelPickerDialog setCustomDialogListener(CustomDialogListener mCustomDialogListener) {
        this.mCustomDialogListener = mCustomDialogListener;
        return this;
    }

    /**
     * 对话框的取消和点击事件
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
    public interface OnSelectDateListener {
        void onSelectDate(Calendar date);
    }

    /**
     * 对话框界面UI的设置
     */
    public interface CustomDialogListener{
        void customValidDialog(ViewHolder viewHolder);
    }
    /**
     * 获取两个日期间的日期集合
     *
     * @return 返回日期集合
     */
    private List<CalendarValueBean> getDatesBetweenTwoDate(Calendar beginDate, Calendar endDate, String pattern) {
        if (null == endDate || null == beginDate) return null;
        Calendar startDate = Calendar.getInstance();
        Calendar finishDate = Calendar.getInstance();
        startDate.setTimeInMillis(beginDate.getTimeInMillis());
        finishDate.setTimeInMillis(endDate.getTimeInMillis());
        List<CalendarValueBean> lDate = new ArrayList<>();
        while (true) {
            Calendar newDate = Calendar.getInstance();
            newDate.setTime(startDate.getTime());
            if (startDate.after(finishDate)) {
                break;
            } else {
                lDate.add(new CalendarValueBean(newDate, formatDate(newDate, pattern)));
            }
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return lDate;
    }

    /**
     * 日期格式化
     */
    private String formatDate(Calendar calendar, String pattern) {
        SimpleDateFormat simpleDateFormat;
        if (TextUtils.isEmpty(pattern)) {
            simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
        } else {
            try {
                simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
            } catch (IllegalArgumentException e) {
                simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
            }
        }
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取列表的日期距当前日期相差天数
     */
    private List<String> getDaysDifferenceList(List<CalendarValueBean> calendarList) {
        List<String> dayList = new ArrayList<>();
        Calendar nowDate = Calendar.getInstance();
        nowDate.setTimeInMillis(getNow().getTimeInMillis());
        if (null != calendarList && calendarList.size() > 0) {
            for (int i = 0; i < calendarList.size(); i++) {
                Calendar calendar = calendarList.get(i).getCalendar();
                long difDay = (calendar.getTimeInMillis() - nowDate.getTimeInMillis()) / (1000 * 3600 * 24);
                dayList.add(String.format("%s天", String.valueOf(difDay + 1)));
            }
        }
        return dayList;
    }

    /**
     * 设置最小日期
     */
    public ValidDateWheelPickerDialog setMinDate(Calendar minDate) {
        this.minDate = minDate;
        return this;
    }

    /**
     * 设置最大日期
     */
    public ValidDateWheelPickerDialog setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    /**
     * 设置选中的日期
     */
    public ValidDateWheelPickerDialog setSelectDate(Calendar selectDate) {
        this.selectDate = new CalendarValueBean(selectDate, null);
        return this;
    }

    /**
     * 设置内容的格式化标准, 默认为"MM-dd"
     */
    public ValidDateWheelPickerDialog setContentDateFormat(String pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * 设置日期选择后监听
     *
     * @param onSelectDateListener 日期选择后监听
     */
    public ValidDateWheelPickerDialog setOnSelectDateListener(OnSelectDateListener onSelectDateListener) {
        this.onSelectDateListener = onSelectDateListener;
        return this;
    }



    /**
     * 构建方法
     */
    public ValidDateWheelPickerDialog build() {
        if(null != mCustomDialogListener){
            mCustomDialogListener.customValidDialog(viewHolder);
        }
        //  重新覆盖确定的点击回调监听
        ButterKnife.findById(rootView, R.id.tv_ok).setOnClickListener(this);
        calendarList = getDatesBetweenTwoDate(this.minDate, this.maxDate, this.pattern);
        validDayList = getDaysDifferenceList(calendarList);
        int selectIndex = 0;
        if (null != selectDate) {
            if (null != calendarList && 0 != calendarList.size()) {
                //  当默认选中的日期大于可选范围, 则默认为最大日期
                boolean isSelectBiggerMax = 2 == compareCalendar(calendarList.get(calendarList.size() - 1).getCalendar(), selectDate.getCalendar());
                if (isSelectBiggerMax) {
                    selectIndex = calendarList.size() - 1;
                } else {
                    for (CalendarValueBean item : calendarList
                            ) {
                        if (0 == compareCalendar(item.getCalendar(), selectDate.getCalendar())) {
                            selectIndex = calendarList.indexOf(item);
                            break;
                        }
                    }
                }
            }

        }
        List<String> darList = new ArrayList<>();
        for (CalendarValueBean item : calendarList
                ) {
            darList.add(item.getDateValue());
        }
        wheelCalendar.setAdapter(new ArrayWheelAdapter(darList, 5));
        wheelCalendar.setCurrentItem(selectIndex);
        wheelDay.setAdapter(new ArrayWheelAdapter(validDayList, 5));
        wheelDay.setCurrentItem(selectIndex);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * 年月日比较
     * 如果直接根据时间戳比较,可能有时分秒上面的干扰
     *
     * @return dar1比dar2大, 则返回1; dar1比dar2小,则返回2;    dar1和dar2同日,返回0
     */
    private static int compareCalendar(Calendar dar1, Calendar dar2) {
        //  比较年
        if (dar1.get(Calendar.YEAR) > dar2.get(Calendar.YEAR)) return 1;
        if (dar1.get(Calendar.YEAR) < dar2.get(Calendar.YEAR)) return 2;
        //  比较月
        if (dar1.get(Calendar.MONTH) > dar2.get(Calendar.MONTH)) return 1;
        if (dar1.get(Calendar.MONTH) < dar2.get(Calendar.MONTH)) return 2;
        //  比较日
        if (dar1.get(Calendar.DAY_OF_MONTH) > dar2.get(Calendar.DAY_OF_MONTH)) return 1;
        if (dar1.get(Calendar.DAY_OF_MONTH) < dar2.get(Calendar.DAY_OF_MONTH)) return 2;
        return 0;
    }

    public static class ViewHolder {
        TextView tvTitle;
        WheelView contentWheel;
        WheelView hintWheel;
        TextView tvCancel;
        TextView tvOk;
        public static final int WHEEL_CONTENT = 1;  //  内容滚轮
        public static final int WHEEL_HINT = 2;     //  提示滚轮
        ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            contentWheel = (WheelView) view.findViewById(R.id.options1);
            hintWheel = (WheelView) view.findViewById(R.id.options2);
            tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            tvOk = (TextView) view.findViewById(R.id.tv_ok);
        }

        public TextView getTitleView() {
            return tvTitle;
        }

        public TextView getCancelView() {
            return tvCancel;
        }

        public TextView getSubmitView() {
            return tvOk;
        }

        /**
         * 设置滚轮焦点文字颜色
         * @param wheelType
         * @param color
         * @return
         */
        public ViewHolder setFocusColor(int wheelType, @ColorInt int color){
            if(WHEEL_CONTENT == wheelType){
                contentWheel.setTextColorCenter(color);
            }else{
                hintWheel.setTextColorCenter(color);
            }
            return this;
        }
        /**
         * 设置滚轮非焦点文字颜色
         * @param wheelType
         * @param color
         * @return
         */
        public ViewHolder setAfocalColor(int wheelType, @ColorInt int color){
            if(WHEEL_CONTENT == wheelType){
                contentWheel.setTextColorOut(color);
            }else{
                hintWheel.setTextColorOut(color);
            }
            return this;
        }
    }
}
