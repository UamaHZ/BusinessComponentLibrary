package com.work.tiantianyu.businesscomponent.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Tina on 2017/7/3.
 * Description: 同步子View的点击事件
 */

public class SynchronizationLinearLayout extends LinearLayout {
    public SynchronizationLinearLayout(Context context) {
        super(context);
    }

    public SynchronizationLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            try{
                if(null != child)
                    child.dispatchTouchEvent(event);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }
}
