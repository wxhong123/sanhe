package com.qhd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acc.common.URL;
import com.acc.common.dao.CheckInfoDao;
import com.acc.common.listview.JsListViewAdapter;
import com.acc.common.util.AccBase64;
import com.acc.common.util.BeanUtils;
import com.acc.common.util.BimoClient;
import com.acc.common.util.DateUtil;
import com.acc.common.util.ServiceGenerator;
import com.acc.common.util.StringUtil;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.arong.swing.db.entity.KeyPerson;
import com.data.BaseDicValueDao;
import com.response.BasePerson;
import com.response.CheckDetailResponse;
import com.response.db_beans.CheckInfo;
import com.response.db_beans.CheckPerson;
import com.response.db_beans.CheckPersonJs;
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
public class PersonDetailActivity extends Activity {

    private TitleBar titleBar;
    private String id;
    private ListView jsListView;
    private RelativeLayout rl_listbg;
    JsListViewAdapter jsListAdapter;
    List<KeyPerson> keyPersons;
    private CheckInfo main;
    private int onlineorlocal;
    CheckInfoBusiness business;
    private BaseDicValueDao baseDicValueDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        baseDicValueDao = new BaseDicValueDao(this);
        business = new CheckInfoBusiness(this);

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
        rl_listbg = findViewById(R.id.rl_listbg);

        if (onlineorlocal == 0) {
            getDataonline();
        } else {
            getDatalocal();
        }
        titleBar = findViewById(R.id.titlebar);
        titleBar.setTitle("核查记录详细信息");
        titleBar.setBack(true);

    }

    private void showJsDetail(int position) {
        if (keyPersons != null && keyPersons.size() > 0) {
            String msg = "";
            msg += nullToString(keyPersons.get(position).getRylb()) + "<br>"
                    + "联系人：" + nullToString(keyPersons.get(position).getBklxr()) + "<br>"
                    + "联系人方式：" + nullToString(keyPersons.get(position).getBklxfs()) + "<br>"
                    + "处置方式：" + nullToString(keyPersons.get(position).getClfs());
            new AlertDialog.Builder(PersonDetailActivity.this).setTitle("警示信息")//设置对话框标题
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
        CheckPerson checkPerson = offLineCheckVo.getRyxx();
//        CheckPerson checkPerson = checkPersonDao.queryById(Integer.parseInt(id));
        CheckInfoDao checkInfoDao = new CheckInfoDao(this);
        main = checkInfoDao.queryCheckInfoById(id);
        if (checkPerson == null || main == null) {
            return;
        }
        TextView vv = findViewById(R.id.textView1);
        vv.setText("体温：" + ("0".equals(checkPerson.getSffs()) ? getString(R.string.dw) : getString(R.string.gw)));
        TextView vvv = findViewById(R.id.textView0);
        vvv.setText("电话：" + nullToString(checkPerson.getLxdh()));
        TextView v;
        v = (TextView) findViewById(R.id.tv_name);
        v.setText(nullToString(checkPerson.getXm()));
        v = (TextView) findViewById(R.id.tv_sex);
        v.setText(nullToString(baseDicValueDao.queryLableBydictTypeAndValue("xb", checkPerson.getXb(), "")));
        v = (TextView) findViewById(R.id.tv_minzu);
        v.setText(nullToString(baseDicValueDao.queryLableBydictTypeAndValue("mz", checkPerson.getMz(), "")));
        v = (TextView) findViewById(R.id.tv_sfzh);
        v.setText(nullToString(checkPerson.getSfzh()));
        v = (TextView) findViewById(R.id.tv_addr);
        v.setText(nullToString(checkPerson.getHjdz()));
        v = (TextView) findViewById(R.id.tv_fzjg);
        v.setText(nullToString(checkPerson.getFzpcs()));
        v = (TextView) findViewById(R.id.tv_yxqx);
        v.setText(nullToString(checkPerson.getYxq()));
        v = (TextView) findViewById(R.id.tv_checktime);
        v.setText(nullToString(checkPerson.getCheckTime()));
        v = (TextView) findViewById(R.id.tv_datastatus);
        String status = main.getCheckNetworkStatus();
        if ("offline".equals(status)) {
            v.setText("离线");
        } else {
            v.setText("在线");
        }
        v = (TextView) findViewById(R.id.tv_policename);
        v.setText(nullToString(main.getPoliceName()));

        ImageView mImageView = (ImageView) findViewById(R.id.imageView0);
        try {
            String zp = checkPerson.getZp();
            if (StringUtil.isNotNull(zp)) {
                byte[] ZpData = Base64.decode(zp, Base64.DEFAULT);
                if (ZpData == null)
                    return;
                Bitmap bm = BitmapFactory.decodeByteArray(ZpData, 0, ZpData.length);
                mImageView.setImageBitmap(bm);
            }
        } catch (Exception e) {

        }

        List<CheckPersonJs> checkPersonJsList = offLineCheckVo.getZbryList();
        if (checkPersonJsList != null && checkPersonJsList.size() > 0) {
            keyPersons = new ArrayList<>();
            for (CheckPersonJs js : checkPersonJsList) {
                KeyPerson keyPerson = BeanUtils.copyBeanToBean(js, KeyPerson.class);
                keyPerson.setPverid(js.getSjly());
                keyPersons.add(keyPerson);
            }
            jsListAdapter = new JsListViewAdapter(PersonDetailActivity.this, keyPersons);
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
        if ("000".equals(bean.getCode()) && bean.getData() != null) {
            main = bean.getData().getMain();
            BasePerson ryxx = bean.getData().getRyxx();
            putData(ryxx);
            List<com.response.KeyPerson> zbryList = bean.getData().getZbryList();
            if (zbryList != null && zbryList.size() > 0) {
                keyPersons = new ArrayList<>();
                for (com.response.KeyPerson e : zbryList) {
                    KeyPerson keyPerson = BeanUtils.copyBeanToBean(e, KeyPerson.class);
                    keyPerson.setPverid(e.getSjly());
                    keyPersons.add(keyPerson);
                }
            }
            if (keyPersons != null && keyPersons.size() > 0) {
                jsListAdapter = new JsListViewAdapter(PersonDetailActivity.this, keyPersons);
                jsListView.setAdapter(jsListAdapter);
                rl_listbg.setBackgroundResource(R.drawable.orange_stroke);
            }
        }
    }

    private void putData(BasePerson ryxx) {
        TextView v;
        v = findViewById(R.id.textView1);
        v.setText("实测体温：" + ("0".equals(ryxx.getSffs()) ? getString(R.string.dw) : getString(R.string.gw)));
        v = findViewById(R.id.textView0);
        v.setText("联系电话：" + nullToString(ryxx.getLxdh()));
        v = (TextView) findViewById(R.id.tv_name);
        v.setText(nullToString(ryxx.getXm()));
        v = (TextView) findViewById(R.id.tv_sex);
        v.setText(nullToString(ryxx.getXb()));
        v = (TextView) findViewById(R.id.tv_minzu);
        v.setText(nullToString(ryxx.getMz()));
        v = (TextView) findViewById(R.id.tv_sfzh);
        v.setText(nullToString(ryxx.getSfzh()));
        v = (TextView) findViewById(R.id.tv_addr);
        v.setText(nullToString(ryxx.getHjdz()));
        v = (TextView) findViewById(R.id.tv_fzjg);
        v.setText(nullToString(ryxx.getFzpcs()));
        v = (TextView) findViewById(R.id.tv_yxqx);
        v.setText(nullToString(ryxx.getYxq()));
        v = (TextView) findViewById(R.id.tv_checktime);
        v.setText(DateUtil.getFullDateToString(main.getCheckTime()));
        v = (TextView) findViewById(R.id.tv_datastatus);
        String status = main.getCheckNetworkStatus();
        if ("offline".equals(status)) {
            v.setText("离线");
        } else {
            v.setText("在线");
        }
        v = (TextView) findViewById(R.id.tv_policename);
        v.setText(nullToString(main.getPoliceName()));

        ImageView mImageView = (ImageView) findViewById(R.id.imageView0);
        try {
            if (!TextUtils.isEmpty(ryxx.getZp())) {
                byte[] decode = AccBase64.decode(ryxx.getZp());
                Bitmap bm = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                mImageView.setImageBitmap(bm);
            }
        } catch (Exception e) {

        }

    }

}