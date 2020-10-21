package com.acc.common.util;

import android.content.Context;
import android.view.View;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by androider on 2017/10/30.
 * 内容：
 */

public class ViewUtil {

    public static void initPull(PullToRefreshListView pullRefreshList, View empty) {
        pullRefreshList.getRefreshableView().setEmptyView(empty);
        ILoadingLayout startLabels = pullRefreshList
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("松开刷新");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pullRefreshList.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉刷新");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("松开刷新");// 下来达到一定距离时，显示的提示
    }

//    public static void initTimePicker(Activity activity, TimePickerView pvTime, String stime, final TextView tv_time) {
//        Calendar selectedDate = Calendar.getInstance();
//        Calendar startDate = Calendar.getInstance();
//        //startDate.set(2013,1,1);
//        Calendar endDate = Calendar.getInstance();
//        //endDate.set(2020,1,1);
//        //正确设置方式 原因：注意事项有说明
//        startDate.set(2013, 0, 1);
//        endDate.set(2025, 12, 31);
//
//        pvTime = new TimePickerView.Builder(activity, new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {//选中事件回调
//                stime = DateUtil.getyyyy_MM(date);
//                tv_time.setText(stime);
//            }
//        })
//                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
//                .setCancelText("取消")//取消按钮文字
//                .setSubmitText("确定")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
//                .setTitleSize(20)//标题文字大小
//                .setTitleText("日期")//标题文字
//                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
//                .isCyclic(true)//是否循环滚动
//                .setTitleColor(Color.WHITE)//标题文字颜色
//                .setSubmitColor(activity.getResources().getColor(R.color.title_color))//确定按钮文字颜色
//                .setCancelColor(activity.getResources().getColor(R.color.title_color))//取消按钮文字颜色
//                .setTitleBgColor(activity.getResources().getColor(R.color.white))//标题背景颜色 Night mode
//                .setBgColor(activity.getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
//                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//                .setRangDate(startDate, endDate)//起始终止年月日设定
//                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
//                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isDialog(false)//是否显示为对话框样式
//                .build();
//    }


    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density; //当前屏幕密度因子
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
