package com.qhd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acc.common.URL;
import com.acc.common.listview.JsListViewAdapter;
import com.acc.common.util.BeanUtils;
import com.acc.common.util.BimoClient;
import com.acc.common.util.DateUtil;
import com.acc.common.util.ServiceGenerator;
import com.acc.common.util.StringUtil;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.arong.swing.db.entity.KeyCar;
import com.data.BaseDicValueDao;
import com.response.BaseCar;
import com.response.CheckDetailResponse;
import com.response.db_beans.CheckCar;
import com.response.db_beans.CheckCarJs;
import com.response.db_beans.CheckInfo;
import com.response.db_beans.OffLineCheckVo;
import com.service.check.CheckInfoBusiness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * 核查人员详细
 *
 * @author biehuan
 * @date 2017-03-03
 * @since 1.0
 */
public class CarDetailActivity extends Activity {

    RelativeLayout rl_listbg;
    private TitleBar titleBar;
    private String id;
    private int onlineorlocal;
    private ListView jsListView;
    JsListViewAdapter jsListAdapter;
    List<KeyCar> keyCars;
    private CheckInfo main;
    CheckInfoBusiness business;
    private BaseDicValueDao baseDicValueDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        baseDicValueDao = new BaseDicValueDao(this);
        business = new CheckInfoBusiness(this);
        rl_listbg = findViewById(R.id.rl_listbg);
        titleBar = findViewById(R.id.titlebar);
        titleBar.setBack(true);
        titleBar.setTitle("核查记录详细信息");
        id = this.getIntent().getStringExtra("id");
        onlineorlocal = getIntent().getIntExtra("onlineorlocal", 0);

        jsListView = (ListView) findViewById(R.id.js_list);
        jsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                showJsDetail(position);
            }
        });
        jsListView.setEmptyView(findViewById(R.id.tv_empty));
        //      jsListView.setEmptyView(View.inflate(CarDetailActivity.this, R.layout.eded, null));

        if (onlineorlocal == 0) {
            getDataonline();
        } else {
            getDatalocal();
        }
    }

    // TODO: 2019/3/29    这个地方详情没法展示，有问题必须改 ，数据后台就有
    private void showJsDetail(int position) {
        if (keyCars != null && keyCars.size() > 0) {
            String msg = "";
            msg += nullToString(keyCars.get(position).getCphm()) + "<br>"
                    + "种类：" + nullToString(keyCars.get(position).getHpzl()) + "<br>"
                    + "车主姓名：" + nullToString(keyCars.get(position).getCzxm()) + "<br>"
                    + "车主身份证号：" + nullToString(keyCars.get(position).getCzsfzh());
            new AlertDialog.Builder(CarDetailActivity.this).setTitle("警示信息")//设置对话框标题
                    .setMessage(Html.fromHtml(msg))//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                        }
                    }).show();//在按键响应事件中显示此对话框
        }
    }

    private void getDatalocal() {
        OffLineCheckVo offLineCheckVo = business.queryRecodeDetail(id);
//        CheckCar checkCar = checkCarDao.queryById(Integer.parseInt(id));
        CheckCar checkCar = offLineCheckVo.getClxx();
        main = offLineCheckVo.getMain();
        TextView v;
        v = (TextView) findViewById(R.id.textView1);
        v.setText("号码：" + nullToString(checkCar.getCphm()) + " " + "号牌种类：" + nullToString(baseDicValueDao.queryLableBydictTypeAndValue("car_type", checkCar.getHpzl(), "")) + " " + "颜色：" + nullToString(baseDicValueDao.queryLableBydictTypeAndValue("car_color", checkCar.getClys(), "")));
        v = (TextView) findViewById(R.id.textView2);
        v.setText(nullToString(checkCar.getCzxm()));
        v = (TextView) findViewById(R.id.textView3);
        v.setText(nullToString(checkCar.getCzlxfs()));
        v = (TextView) findViewById(R.id.textView4);
        v.setText(nullToString(checkCar.getCzsfzh()));
        v = (TextView) findViewById(R.id.textView5);
//        v.setText(nullToString(checkCar.getCldjdz()));
        v.setText(nullToString(checkCar.getCzxxdz()));
        v = (TextView) findViewById(R.id.tv_checktime);
        v.setText(DateUtil.getFullDateToString(checkCar.getCheckTime()));
        v = (TextView) findViewById(R.id.tv_datastatus);
        String status = main.getCheckNetworkStatus();
        if ("offline".equals(status)) {
            v.setText("离线");
        } else {
            v.setText("在线");
        }
        v = (TextView) findViewById(R.id.tv_policename);
        v.setText(nullToString(main.getPoliceName()));
        v = (TextView) findViewById(R.id.textView10);
        //    v.setText("核查时间:  " + nullToString(checkPerson.getCreateTime()));
        v = (TextView) findViewById(R.id.textView11);
        //    v.setText("人员类别:  " + nullToString(checkPerson.getRylb()));
//        if (!TextUtils.isEmpty(checkPerson.getZp())) {
//            Bitmap bm = BitmapFactory.decodeByteArray(Base64.decode(checkPerson.getZp(), 38862), 0, 38862);
//            ImageView mImageView = (ImageView) findViewById(R.id.imageView0);
//            mImageView.setImageBitmap(bm);
//        }

        List<CheckCarJs> zbclList = offLineCheckVo.getZbclList();
        if (zbclList != null && zbclList.size() > 0) {
            keyCars = new ArrayList<>();
            for (CheckCarJs js : zbclList) {
                KeyCar keycar = BeanUtils.copyBeanToBean(js, KeyCar.class);
                keycar.setPverid(js.getSjly());
                keyCars.add(keycar);
            }
            jsListAdapter = new JsListViewAdapter(CarDetailActivity.this, keyCars, 1);
            jsListView.setAdapter(jsListAdapter);
            rl_listbg.setBackgroundResource(R.drawable.orange_stroke);
        }
    }

    private String nullToString(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return "";
        }
        return str;
    }


    private void getDataonline() {
        String url = URL.host + URL.collectdetail;
        Map map = new HashMap<>();
        map.put("id", id);


        ServiceGenerator.createService(BimoClient.class, URL.host)
                .checkDetail(JSON.toJSONString(map))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CheckDetailResponse>() {
                    @Override
                    public void onNext(CheckDetailResponse responseBody) {
                        if (null != responseBody) {
                            processDetailInfo(responseBody);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });


//        MyApplication.mMyOkhttp.get()
//                .url(url)
//                .addParam("jsonObj", JSON.toJSONString(map))
//                .tag(this)
//                .enqueue(new RawResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        Log.d("mydata", "===>>" + error_msg);
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, String response) {
//                        Log.d("mydata", "===>>" + response);
//                        CheckDetailResponse bean = JSON.parseObject(response, CheckDetailResponse.class);
//                        processDetailInfo(bean);
//                    }
//                });
    }

    private void processDetailInfo(CheckDetailResponse bean) {
        if ("000".equals(bean.getCode())) {
            main = bean.getData().getMain();
            BaseCar clxx = bean.getData().getClxx();
            putData(clxx);
            List<com.response.KeyCar> zbclList = bean.getData().getZbclList();
            if (zbclList != null && zbclList.size() > 0) {
                keyCars = new ArrayList<>();
                for (com.response.KeyCar e : zbclList) {
                    KeyCar keycar = BeanUtils.copyBeanToBean(e, KeyCar.class);
                    keycar.setPverid(e.getSjly());
                    keyCars.add(keycar);
                }
            }
            if (keyCars != null && keyCars.size() > 0) {
                jsListAdapter = new JsListViewAdapter(CarDetailActivity.this, keyCars, 1);
                jsListView.setAdapter(jsListAdapter);
                rl_listbg.setBackgroundResource(R.drawable.orange_stroke);
            }
        }
    }

    private void putData(BaseCar clxx) {
        TextView v;
        v = (TextView) findViewById(R.id.textView1);
        v.setText(nullToString(clxx.getCphm()) + " " + nullToString(clxx.getHpzl()) + " " + nullToString(clxx.getClys()));
        v = (TextView) findViewById(R.id.textView2);
        v.setText(nullToString(clxx.getCzxm()));
        v = (TextView) findViewById(R.id.textView3);
        v.setText(nullToString(clxx.getCzlxfs()));
        v = (TextView) findViewById(R.id.textView4);
        v.setText(nullToString(clxx.getCzsfzh()));
        v = (TextView) findViewById(R.id.textView5);
//        v.setText(nullToString(clxx.getCldjdz()));
        v.setText(nullToString(clxx.getCzxxdz()));
        v = (TextView) findViewById(R.id.tv_checktime);
        v.setText(nullToString(clxx.getCheckTime()));
        v = (TextView) findViewById(R.id.tv_datastatus);
        String status = main.getCheckNetworkStatus();
        if ("offline".equals(status)) {
            v.setText("离线");
        } else {
            v.setText("在线");
        }
        v = (TextView) findViewById(R.id.tv_policename);
        v.setText(nullToString(main.getPoliceName()));
    }

}