package com.acc.common.util;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.util.ConvertUtils;

public class CheckDateUtil {


    /**
     * 起始、结束时间选择控件（自动对比设置起始、结束时间）
     *
     * @param activity
     * @param edt_startDate 开始时间Editetext
     * @param edt_endDate   结束时间Editetext
     * @param type          (1代表选择开始日期 2代表选择结束日期)
     */
    public static void checkDateTime(Activity activity, EditText edt_startDate, EditText edt_endDate, String type) {
        Calendar c = Calendar.getInstance();
        //取得系统日期:
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        int hour = 0;
        int minuts = 0;
        int hourCheck = c.get(Calendar.HOUR_OF_DAY);
        int minutsCheck = c.get(Calendar.MINUTE);
        if (type.equals("1")) {
            String enddate = edt_startDate.getText().toString();
            if (!enddate.equals("")) {
                int endYear = Integer.parseInt(enddate.substring(0, 4));
                int endMonth = Integer.parseInt(enddate.substring(5, 7));
                int endDay = Integer.parseInt(enddate.substring(8, 10));
               /* editText.getText().substring(0, 4)  editText.getText().substring(5, 7) editText.getText().substring(8, 10) editText.getText().substring(11, 13)
                editText.getText().substring(14, 16)  editText.getText().substring(17, 19)*/
                hour = 0;
                minuts = 0;
                hourCheck = Integer.parseInt(enddate.substring(11, 13));
                minutsCheck = Integer.parseInt(enddate.substring(14, 16));


                //DateUtil.formatFullDate(enddate);
                //AccTool.onYearMonthDayTimePicker();
                onYearMonthDayTimePicker(activity, (year), 1, 1, month, day, endDay, hour, minuts, 23, 59, hourCheck, minutsCheck, edt_startDate);

            } else {

                onYearMonthDayTimePicker(activity, (year), 1, 1, (year - 1), month, day, hour, minuts, 23, 59, hourCheck, minutsCheck, edt_startDate);
            }
        } else if (type.equals("2")) {
            String startdate = edt_startDate.getText().toString();
            if (!startdate.equals("")) {

                int startYear = Integer.parseInt(startdate.substring(0, 4));
                int startMonth = Integer.parseInt(startdate.substring(5, 7));
                int startDay = Integer.parseInt(startdate.substring(8, 10));
                hour = 0;
                minuts = 0;
                hourCheck = Integer.parseInt(startdate.substring(11, 13));
                minutsCheck = Integer.parseInt(startdate.substring(14, 16));
                onYearMonthDayTimePicker(activity, (year), startMonth, startDay, startYear - 1, month, day, hour, minuts, 23, 59, hourCheck, minutsCheck, edt_endDate);
            } else {
                onYearMonthDayTimePicker(activity, (year), 1, day, (year - 1), month, day, hour, minuts, 23, 59, hourCheck, minutsCheck, edt_endDate);
            }
        }
    }


    /**
     * 起始、结束时间选择控件（自动对比设置起始、结束时间）
     *
     * @param activity
     * @param tv_startDate 开始时间Editetext
     * @param tv_endDate   结束时间Editetext
     * @param type         (1代表选择开始日期 2代表选择结束日期)
     */
    public static void checkDate(Activity activity, TextView tv_startDate, TextView tv_endDate, String type) {
        Calendar c = Calendar.getInstance();
        //取得系统日期:
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (type.equals("1")) {
            String enddate = tv_endDate.getText().toString();
            if (!enddate.equals("")) {
                int endYear = Integer.parseInt(enddate.substring(0, 4));
                int endMonth = Integer.parseInt(enddate.substring(5, 7));
                int endDay = Integer.parseInt(enddate.substring(8, 10));
                onYearMonthDayPicker(activity, (year - 3), month, day, endYear, endMonth, endDay, endYear, endMonth, endDay, tv_startDate, 1);
            } else {
                onYearMonthDayPicker(activity, (year - 3), month, day, year, month, day, year, month, day, tv_startDate, 1);
            }
        } else if (type.equals("2")) {
            String startdate = tv_startDate.getText().toString();
            if (!startdate.equals("")) {
                int startYear = Integer.parseInt(startdate.substring(0, 4));
                int startMonth = Integer.parseInt(startdate.substring(5, 7));
                int startDay = Integer.parseInt(startdate.substring(8, 10));
                onYearMonthDayPicker(activity, startYear, startMonth, startDay, year, month, day, startYear, startMonth, startDay, tv_endDate, 2);
            } else {
                onYearMonthDayPicker(activity, (year - 3), month, day, year, month, day, (year - 3), month, day, tv_endDate, 2);
            }
        }
    }


    public static void onYearMonthDayPicker(Activity activity, int startYear, int startMonth, int startDay, int endYear,
                                            int endMonth, int endDay, int selectedYear, int selectedMonth,
                                            int selectedDay, final TextView tv_date, int type) {
        final DatePicker picker = new DatePicker(activity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(activity, 10));
        picker.setRangeEnd(endYear, endMonth, endDay);
        picker.setRangeStart(startYear, startMonth, startDay);
        picker.setSelectedItem(selectedYear, selectedMonth, selectedDay);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
//                showToast(year + "-" + month + "-" + day);

            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);

            }
        });
        picker.show();

        picker.getCancelButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.dismiss();
            }
        });

        picker.getSubmitButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shifenxiao = "";
                switch (type) {
                    case 1:
                        shifenxiao = " 00:00:00";
                        break;
                    case 2:
                        shifenxiao = " 23:59:59";
                        break;
                }
                tv_date.setText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay() + shifenxiao);
                picker.dismiss();
            }
        });
    }


    /**
     * 日期、时间选择器
     *
     * @param activity
     * @param startYear   日期选择器的开始年份
     * @param startMonth  日期选择器的开始月份
     * @param startDay    日期选择器的开始日期
     * @param endYear     日期选择器的结束年份
     * @param endMonth    日期选择器的结束月份
     * @param endDay      日期选择器的结束日期
     * @param startHour   日期选择器的开始时间小时
     * @param startMinute 日期选择器的开始时间分
     * @param endHour     日期选择器的结束时间小时
     * @param endMinute   日期选择器的结束时间分
     * @param endHour     日期选择器初始选中的时间小时
     * @param endMinute   日期选择器初始选中的时间分
     * @param editText
     */
    public static void onYearMonthDayTimePicker(Activity activity, int startYear, int startMonth, int startDay, int endYear,
                                                int endMonth, int endDay, int startHour, int startMinute,
                                                int endHour, int endMinute, int checkHour, int checkMinute, final EditText editText) {
        DateTimePicker picker = new DateTimePicker(activity, DateTimePicker.HOUR_24);
        picker.setDateRangeStart(startYear, startMonth, startDay);
        picker.setDateRangeEnd(endYear, endMonth, endDay);
        picker.setTimeRangeStart(startHour, startMinute);
        picker.setTimeRangeEnd(endHour, endMinute);
        picker.setSelectedItem(startYear, startMonth, startDay, checkHour, checkMinute);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                editText.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00");
            }
        });
        picker.show();
    }


}
