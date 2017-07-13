package com.work.tiantianyu.businesscomponentlibrary;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.work.tiantianyu.businesscomponent.widget.ValidDateWheelPickerDialog;

import java.util.Calendar;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ValidDateWheelPickerDialog.OnSelectDateListener, ValidDateWheelPickerDialog.CustomDialogListener {
    private ValidDateWheelPickerDialog dialog;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.findById(this, R.id.btn_test).setOnClickListener(this);
        mContext = this;
    }

    @Override
    public void onClick(View v) {
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.MONTH, 5);
        //  对话框属性设置, 初始化
        ValidDateWheelPickerDialog dialog = new ValidDateWheelPickerDialog(this)
                .setMaxDate(maxCalendar)
                .setOnSelectDateListener(this)
                .setContentDateFormat("yyyy-MM-dd")
                .setCustomDialogListener(this)
                .build();
        //  对话框展开
        dialog.show();
    }

    @Override
    public void onSelectDate(Calendar date) {
        Toast.makeText(this, date.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void customValidDialog(ValidDateWheelPickerDialog.ViewHolder viewHolder) {
        viewHolder.getCancelView().setText("取消再说");
        viewHolder.setAfocalColor(ValidDateWheelPickerDialog.ViewHolder.WHEEL_CONTENT, ContextCompat.getColor(this, R.color.colorAccent));
        viewHolder.getSubmitView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "测试有没有被覆盖", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
