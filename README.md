# BusinessComponentLibrary
业务组件库
包括内容
#### ValidDateWheelPickerDialog
有效日期选择滚轮对话框
##### 可设置属性
|开放方法名                         |用处                                     |
|--------------------------------|-----------------------------------|
|setMaxDate(Calendar maxDate)|设置最大日期, 默认为当天日期|
|setMinDate(Calendar minDate)|设置最小日期,默认当天日期    |
|setSelectDate(Calendar selectDate)|设置默认选中的日期, 默认为第一个日期, 当默认选中的日期比最大日期大,则默认选择最后一个日期|
|setOnSelectDateListener(OnSelectDateListener listener)|设置确定的点击事件, 该事件不可被覆盖|
|setContentDateFormat(String pattern)|设置内容的日期格式化|
|setCustomDialogListener(CustomDialogListener listener)|针对对话框的控件进行个性化设置|
##### demo
```
Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.MONTH, 5);
        //  对话框属性设置, 初始化
        ValidDateWheelPickerDialog dialog = new ValidDateWheelPickerDialog(this)
                .setMaxDate(maxCalendar)
                .setOnSelectDateListener(this)
                .build();
        //  对话框展开
        dialog.show();
```