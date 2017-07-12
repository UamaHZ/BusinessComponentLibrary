# BusinessComponentLibrary
业务组件库
包括内容
#### ValidDateWheelPickerDialog
有效日期选择滚轮对话框
##### 可设置属性
1. 设置最大日期, 默认为当天日期
2. 设置最小日期, 默认为当天日期
3. 设置默认选中的日期, 默认为第一个日期
4. 设置日期选择后的监听
##### demo
```
Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.MONTH, 5);
        //  对话框属性设置, 初始化
        ValidDateWheelPickerDialog dialog = new ValidDateWheelPickerDialog(this)
                .setMaxDate(maxCalendar)
                .setOnSelectDateListener(this);
        //  对话框展开
        dialog.show();
```