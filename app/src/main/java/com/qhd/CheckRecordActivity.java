package com.qhd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.dao.CheckCarDao;
import com.acc.common.dao.CheckPersonDao;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.acc.common.util.AccTool;
import com.acc.common.util.BimoClient;
import com.acc.common.util.CheckDateUtil;
import com.acc.common.util.DateUtil;
import com.acc.common.util.SPHelper;
import com.acc.common.util.ServiceGenerator;
import com.acc.common.util.ViewUtil;
import com.acc.common.widget.CustomerDialog;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.arong.swing.db.entity.StaffUserVO;
import com.data.BaseDicValueDao;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.request.CheckListRequest;
import com.response.CheckListResonse;
import com.service.check.CheckInfoBusiness;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * 核查记录，用于统计范围时间内警员的核查记录。
 *
 * @author biehuan
 * @date 2016-11-07
 * @since 1.0
 */
public class CheckRecordActivity extends Activity implements OnScrollListener, OnClickListener
        , AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private int onlineorlocal;
    private int pageNumberonline = 1;
    private int pageNumberlocal = 1;
    // ListView的Adapter
    private CheckRecordAdapter checkRecordAdapter;
    //    private ListView lv;
    private Button bt;
    private Button query;
    private TextView t1;
    private TextView t2;
    private EditText etSearch;

//    int sYear, sMonth, sDay;
//    int eYear, eMonth, eDay;

    LinearLayout ll_starttime, ll_endtime;
    //    private String startTime;
//    private String endTime;
    private String checkObject;
    private LinearLayout lly_person;
    private EditText et_sfzh;
    private CheckBox ckbperson, ckbcar;

    private ProgressBar pg;
    private ArrayList<Map<String, String>> list;
    private ArrayList<Map<String, String>> onlinelist;
    private ArrayList<Map<String, String>> locallist;

    // ListView底部View
    private View moreView;
    private Handler handler;
    // 设置一个最大的数据条数，超过即不再加载
    private long MaxDateNum;
    // 最后可见条目的索引
    private int lastVisibleIndex;

    private CheckPersonDao checkPersonDao;
    private CheckCarDao checkCarDao;
    private BaseDicValueDao baseDicValueDao;

    private TextView title1;
    private TextView title2;
    private TextView total;
    TextView tv_highcheck;
    TextView clear;
    LinearLayout ll_highcheck;
    CheckInfoBusiness business;
    StaffUserVO userVO;
    private TitleBar titleBar;
    PullToRefreshListView pull;
    TextView emptyView;
    int sumonline, sumlocal;
    private CustomerDialog progressDialog;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record);

        clear = findViewById(R.id.clear);
        pull = findViewById(R.id.pull_refresh_list);
        emptyView = findViewById(R.id.empty_view);
        tv_highcheck = findViewById(R.id.tv_highcheck);
        tv_highcheck.setOnClickListener(this);
        clear.setOnClickListener(this);
        ll_highcheck = findViewById(R.id.ll_highcheck);
        userVO = SPHelper.getHelper(this).getInfo();
        business = new CheckInfoBusiness(this);
        list = new ArrayList<>();
        onlinelist = new ArrayList<>();
        locallist = new ArrayList<>();

        progressDialog = new CustomerDialog(CheckRecordActivity.this, R.style.MyDialog);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);

        lly_person = (LinearLayout) findViewById(R.id.lly_person);
        et_sfzh = (EditText) findViewById(R.id.et_sfzh);
        title1 = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title2.setOnClickListener(this);
        title1.setOnClickListener(this);
        total = (TextView) findViewById(R.id.total);
        etSearch = findViewById(R.id.et_searchWord);
        titleBar = findViewById(R.id.titlebar);
        titleBar.setBack(true);
        titleBar.setTitle("检查列表");

        checkPersonDao = new CheckPersonDao(this);
        checkCarDao = new CheckCarDao(this);
        baseDicValueDao = new BaseDicValueDao(this);

        deleteCheckRecord();

//        final Calendar ca = Calendar.getInstance();
//        sYear = ca.get(Calendar.YEAR);
//        eYear = ca.get(Calendar.YEAR);
//        sMonth = ca.get(Calendar.MONTH);
//        eMonth = ca.get(Calendar.MONTH);
//        sDay = ca.get(Calendar.DAY_OF_MONTH);
//        eDay = ca.get(Calendar.DAY_OF_MONTH);

        ckbperson = findViewById(R.id.ckbperson);
        ckbcar = findViewById(R.id.ckbcar);
        ckbperson.setOnCheckedChangeListener(this);
        ckbcar.setOnCheckedChangeListener(this);

        // 实例化底部布局
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);
        query = (Button) findViewById(R.id.query);
        query.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                list.clear();

                moreView.setVisibility(View.VISIBLE);
                checkRecordAdapter.notifyDataSetChanged();
//                mSimpleAdapter.notifyDataSetChanged();

                if (onlineorlocal == 0) {
                    pageNumberonline = 1;
                    getData(createRequest(), true);
                } else {
                    pageNumberlocal = 1;
                    getLocalData(createRequest(), true, false);
                    getLocalDataNum(createRequest());
//                    loadMoreDate();
//                    mSimpleAdapter.notifyDataSetChanged();// 通知listView刷新数据
                }

            }
        });


        t1 = (TextView) findViewById(R.id.start);
        t1.setText(AccTool.getStringDateShort("yyyy-MM-dd") + " 00:00:00");
        t2 = (TextView) findViewById(R.id.end);
        t2.setText(AccTool.getStringDateShort("yyyy-MM-dd") + " 23:59:59");
        ll_starttime = findViewById(R.id.ll_starttime);
        ll_endtime = findViewById(R.id.ll_endtime);

        ll_starttime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDateUtil.checkDate(CheckRecordActivity.this, t1, t2, "1");
            }
        });
        ll_endtime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDateUtil.checkDate(CheckRecordActivity.this, t1, t2, "2");
            }
        });


        bt = (Button) moreView.findViewById(R.id.bt_load);
        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        handler = new Handler();
        initData();
        CheckListRequest request = createRequest();
        //todo  转圈圈
        getData(request, true);
        getLocalDataNum(request);
        getLocalData(request, true, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> map = list.get(position - 1);
        String tv_id = map.get("tv_id");
        Intent intent = new Intent();
        intent.putExtra("id", tv_id);
        intent.putExtra("onlineorlocal", onlineorlocal);
//                if ("人".equals(checkobject)) {
//                    intent.setClass(CheckRecordActivity.this, PersonDetailActivity.class);
//                } else {
//                    intent.setClass(CheckRecordActivity.this, CarDetailActivity.class);
//                }
        String checkobject = map.get("checkobject");
        if ("person".equals(checkobject)) {
            intent.setClass(CheckRecordActivity.this, PersonDetailActivity.class);
        } else {
            intent.setClass(CheckRecordActivity.this, CarDetailActivity.class);
        }
        startActivity(intent);
    }

    private void getLocalData(CheckListRequest request, boolean isRefresh, boolean first) {
        if (isRefresh) {
            locallist.clear();
        }
        List<CheckListResonse.DataBeanX.DataBean> dataBeans = business.queryRecode(request);
        if (dataBeans != null && dataBeans.size() > 0) {
            for (int i = 0; i < dataBeans.size(); i++) {
                CheckListResonse.DataBeanX.DataBean dataBean = dataBeans.get(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("zbxxnumber", dataBean.getZbxxNumber());
                if ("person".equals(dataBean.getCheckObject())) {
                    map.put("tv_id", nullToString(dataBean.getId()));
//                map.put("xh", (((pageNumber - 1) * Constants.pageRecorders) + (i + 1)) + "");
//                map.put("ItemTitle", bean.getXm());
                    map.put("xh", "姓名：" + nullToString(dataBean.getXm()));
                    map.put("ItemTitle", "民族：" + nullToString(baseDicValueDao.queryLableBydictTypeAndValue("mz", dataBean.getMz(), "")));
                    map.put("ItemText", "身份证号：" + nullToString(dataBean.getSfzh()));
                    map.put("checktime", "核查时间：" + DateUtil.getFullDateToString(DateUtil.formatDateFromFullDate(dataBean.getCheckTime())));
                    map.put("checkobject", nullToString(dataBean.getCheckObject()));
                    map.put("zp", nullToString(dataBean.getZp()));
                    map.put("type", "person");
                } else {
                    map.put("tv_id", dataBean.getId());
//                map.put("xh", (((pageNumber - 1) * Constants.pageRecorders) + (i + 1)) + "");
                    String hpzlName = baseDicValueDao.queryLableBydictTypeAndValue("car_type", dataBean.getHpzl(), "");
//                    if ("02".equals(hpzl)) {
//                        hpzlName = "小型汽车";
//                    } else if ("01".equals(hpzl)) {
//                        hpzlName = "大型汽车";
//                    }
                    map.put("xh", "品牌：" + nullToString(dataBean.getClpp()));
                    map.put("ItemTitle", "颜色：" + nullToString(baseDicValueDao.queryLableBydictTypeAndValue("car_color", dataBean.getClys(), "")));
//                    map.put("ItemText", "号牌种类：" + hpzlName + " 号码：" + nullToString(dataBean.getCphm()));
                    map.put("ItemText", nullToString(hpzlName) + " " + nullToString(dataBean.getCphm()));
                    map.put("ItemText", "号码：" + nullToString(dataBean.getCphm()));
                    map.put("checktime", "核查时间：" + DateUtil.getFullDateToString(DateUtil.formatDateFromFullDate(dataBean.getCheckTime())));
                    map.put("checkobject", nullToString(dataBean.getCheckObject()));
                    map.put("type", "car");
                }
                locallist.add(map);
            }
        }
        if (!first) {
            list.clear();
            list.addAll(locallist);
//            mSimpleAdapter.notifyDataSetChanged();
            checkRecordAdapter.notifyDataSetChanged();
        }
        pull.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull.onRefreshComplete();
            }
        }, 1000);
    }

    private String nullToString(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return "";
        }
        return str;
    }


    private void getLocalDataNum(CheckListRequest request) {
        int personCount = (int) business.queryPersonCount(request);
        int carCount = (int) business.queryCarCount(request);
        if ("person".equals(checkObject)) {
            sumlocal = personCount;
        } else if ("car".equals(checkObject)) {
            sumlocal = carCount;
        } else {
            sumlocal = personCount + carCount;
        }
        title2.setText("离线：人" + personCount + "条，车" + carCount + "条");
    }

    private void initData() {
        total.setText("共" + MaxDateNum + "条");

        checkRecordAdapter = new CheckRecordAdapter(list, this);
        pull.setAdapter(checkRecordAdapter);
        pull.setOnItemClickListener(this);
        ViewUtil.initPull(pull, emptyView);
        pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (onlineorlocal == 0) {
                    pageNumberonline = 1;
                    CheckListRequest request = createRequest();
                    getData(request, false);
                } else {
                    pageNumberlocal = 1;
                    CheckListRequest request = createRequest();
                    getLocalDataNum(request);
                    getLocalData(request, false, false);
                    // loadMoreDate();// 加载更多数据
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (onlineorlocal == 0) {
                    if (onlinelist.size() > 0 && onlinelist.size() >= sumonline) {
                        Toast.makeText(CheckRecordActivity.this, "已经加载完毕", Toast.LENGTH_SHORT).show();
                        pull.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pull.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }
                    pageNumberonline += 1;
                    CheckListRequest request = createRequest();
                    getData(request, false);
                } else {
                    if (locallist.size() > 0 && locallist.size() >= sumlocal) {
                        Toast.makeText(CheckRecordActivity.this, "已经加载完毕", Toast.LENGTH_SHORT).show();
                        pull.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pull.onRefreshComplete();
                            }
                        }, 1000);
                    }
                    pageNumberlocal += 1;
                    CheckListRequest request = createRequest();
                    getLocalDataNum(request);
                    getLocalData(request, false, false);
                    // loadMoreDate();// 加载更多数据
                }
            }
        });
    }

    private CheckListRequest createRequest() {
        CheckListRequest request = new CheckListRequest();
        request.setSearchWord(etSearch.getText().toString().trim());
        request.setCheckObject(checkObject);
        request.setPoliceIdcard(userVO.getStaffCode());
        request.setPoliceName(userVO.getStaffName());
        request.setPageSize(10);
        request.setStartCheckTime(t1.getText().toString());
        request.setEndCheckTime(t2.getText().toString());
        request.setCardNumber(et_sfzh.getText().toString().trim());
        request.setSfzh(et_sfzh.getText().toString().trim());
        request.setPageNo(onlineorlocal == 0 ? pageNumberonline : pageNumberlocal);
        return request;
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 计算最后可见条目的索引
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

        // 所有的条目已经和最大条数相等，则移除底部的View
        if (totalItemCount == MaxDateNum + 1) {
//          lv.removeFooterView(moreView);  
            moreView.setVisibility(View.GONE);
            //Toast.makeText(this, "数据全部加载完成，没有更多数据！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == checkRecordAdapter.getCount()) {
        }

    }


    //删除5天前的核查记录
    private void deleteCheckRecord() {
        Date oldDate = DateUtil.getDateAddDays(new Date(), -5);
        String checktime = DateUtil.getDateToString(oldDate) + " 23:59:59";
        checkPersonDao.delete(checktime);
//        checkCarDao.delete(checktime);
    }


    private void getData(CheckListRequest request, boolean isRefresh) {
        if (isRefresh) {
            onlinelist.clear();
        }
        String url = URL.host + URL.collectlist;
        CheckInfoBusiness checkInfoBusiness = new CheckInfoBusiness(this);
//        long carCount = checkInfoBusiness.queryCarCount(request);
//        long personCount = checkInfoBusiness.queryPersonCount(request);
//        List<CheckListResonse.DataBeanX.DataBean> list = checkInfoBusiness.queryRecode(request);
        showProgressDialog("核查中");
        emptyView.setText("正在查询");

        ServiceGenerator.createService(BimoClient.class, URL.host)
                .checkList(JSON.toJSONString(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CheckListResonse>() {
                    @Override
                    public void onNext(CheckListResonse responseBody) {
                        dismissProgressDialog();
                        if (null != responseBody) {
                            pull.onRefreshComplete();
                            emptyView.setText("没有数据");
                            dismissProgressDialog();
                            processListInfo(responseBody);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        pull.onRefreshComplete();
                        emptyView.setText("没有数据");
                        title1.setText("在线：人" + "0条，车" + "0条");
                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//
//        MyApplication.mMyOkhttp.get()
//                .url(url)
//                .addParam("jsonObj", JSON.toJSONString(request))
//                .tag(this)
//                .enqueue(new RawResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        pull.onRefreshComplete();
//                        emptyView.setText("没有数据");
//                        title1.setText("在线：人" + "0条，车" + "0条");
//                        dismissProgressDialog();
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, String response) {
//                        pull.onRefreshComplete();
//                        emptyView.setText("没有数据");
//                        dismissProgressDialog();
//                        CheckListResonse bean = JSON.parseObject(response, CheckListResonse.class);
//                        processListInfo(bean);
//                    }
//                });
    }

    private void processListInfo(CheckListResonse bean) {
        if (!"000".equals(bean.getCode()) || bean.getData() == null) {
            title1.setText("在线：人" + "0条，车" + "0条");
            return;
        }
        List<CheckListResonse.DataBeanX.DataBean> data = bean.getData().getData();
        if (data != null && data.size() > 0) {
            int checkPersonNumber = bean.getData().getCheckPersonNumber();
            int checkCarNumber = bean.getData().getCheckCarNumber();
            if ("person".equals(checkObject)) {
                sumonline = checkPersonNumber;
            } else if ("car".equals(checkObject)) {
                sumonline = checkCarNumber;
            } else {
                sumonline = checkPersonNumber + checkCarNumber;
            }
            title1.setText("在线：人" + checkPersonNumber + "条，车" + checkCarNumber + "条");
//                            if ("人".equals(checkobject)) {
//                                MaxDateNum = bean.getData().getCheckPersonNumber();
//                                putPersonData(data);
//                            } else {
//                                MaxDateNum = bean.getData().getCheckCarNumber();
//                                putCarData(data);
//                            }
            putData(data);
        } else {
            title1.setText("在线：人" + "0条，车" + "0条");
        }
    }

    private void putData(List<CheckListResonse.DataBeanX.DataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            CheckListResonse.DataBeanX.DataBean bean = data.get(i);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("zbxxnumber", bean.getZbxxNumber());
            if ("person".equals(bean.getCheckObject())) {
                map.put("tv_id", nullToString(bean.getId()));
//                map.put("xh", (((pageNumber - 1) * Constants.pageRecorders) + (i + 1)) + "");
//                map.put("ItemTitle", bean.getXm());
                map.put("xh", "姓名：" + nullToString(bean.getXm()));
                map.put("ItemTitle", "民族：" + nullToString(bean.getMz()));
                map.put("ItemText", "身份证号：" + nullToString(bean.getSfzh()));
                map.put("checktime", "核查时间：" + nullToString(bean.getCheckTime()));
                map.put("checkobject", nullToString(bean.getCheckObject()));
                map.put("zp", nullToString(bean.getZp()));
                map.put("type", "person");
            } else {
                map.put("tv_id", bean.getId());
//                map.put("xh", (((pageNumber - 1) * Constants.pageRecorders) + (i + 1)) + "");
                String hpzl = bean.getHpzl();
                String hpzlName = "";
                if ("02".equals(hpzl)) {
                    hpzlName = "小型汽车";
                } else if ("01".equals(hpzl)) {
                    hpzlName = "大型汽车";
                }
                map.put("xh", "品牌：" + nullToString(bean.getClpp()));
                map.put("ItemTitle", "颜色：" + nullToString(bean.getClys()));
//                map.put("ItemText", "号牌种类：" + hpzlName + " 号码：" + nullToString(bean.getCphm()));
                map.put("ItemText", nullToString(hpzlName) + " " + nullToString(bean.getCphm()));
                map.put("checktime", "核查时间：" + nullToString(bean.getCheckTime()));
                map.put("checkobject", nullToString(bean.getCheckObject()));
                map.put("type", "car");
            }
            onlinelist.add(map);
        }
        if (onlineorlocal == 0) {
            list.clear();
            list.addAll(onlinelist);
            checkRecordAdapter.notifyDataSetChanged();
        }
//        mSimpleAdapter.notifyDataSetChanged();// 通知listView刷新数据
        total.setText("共" + MaxDateNum + "条");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title1:
                title1.setTextColor(getResources().getColor(R.color.white));
                title1.setBackgroundResource(R.color.orange);
                title2.setTextColor(getResources().getColor(R.color.gray));
                title2.setBackgroundResource(R.color.white);

                onlineorlocal = 0;
                list.clear();
                list.addAll(onlinelist);
//                mSimpleAdapter.notifyDataSetChanged();
                checkRecordAdapter.notifyDataSetChanged();
                break;
            case R.id.title2:
                title1.setTextColor(getResources().getColor(R.color.gray));
                title1.setBackgroundResource(R.color.white);
                title2.setTextColor(getResources().getColor(R.color.white));
                title2.setBackgroundResource(R.color.orange);

                list.clear();
                list.addAll(locallist);
//                mSimpleAdapter.notifyDataSetChanged();
                checkRecordAdapter.notifyDataSetChanged();
                onlineorlocal = 1;
                break;
            case R.id.tv_highcheck:
                if (ll_highcheck.getVisibility() == View.GONE) {
                    ll_highcheck.setVisibility(View.VISIBLE);
                } else {
                    ll_highcheck.setVisibility(View.GONE);
                }
                break;
            case R.id.clear:
                t1.setText("");
                t2.setText("");
//                startTime = "";
//                endTime = "";
                et_sfzh.setText("");
                etSearch.setText("");
                checkObject = "";
                ckbperson.setChecked(false);
                ckbcar.setChecked(false);
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ckbperson:
                if (isChecked) {
                    ckbcar.setChecked(false);
                    lly_person.setVisibility(View.VISIBLE);
                    checkObject = "person";
                } else {
                    lly_person.setVisibility(View.GONE);
                    et_sfzh.setText("");
                    if (!ckbcar.isChecked()) {
                        checkObject = "";
                    }
                }
                break;
            case R.id.ckbcar:
//                lly_person.setVisibility(View.GONE);
//                et_sfzh.setText("");
                if (isChecked) {
                    ckbperson.setChecked(false);
                    checkObject = "car";
                } else {
                    if (!ckbperson.isChecked()) {
                        checkObject = "";
                    }
                }
                break;
        }
    }


    public void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.setMessage(msg);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing() == true) {
            progressDialog.dismiss();
        }
    }


}