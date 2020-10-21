package com.qhd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Utils.SanDaoFangXianUtils;
import com.acc.common.Constants;
import com.acc.common.URL;
import com.acc.common.base.DictBusiness;
import com.acc.common.base.MyApplication;
import com.acc.common.dao.CheckCarDao;
import com.acc.common.dao.CheckCarJsDao;
import com.acc.common.dao.CheckInfoDao;
import com.acc.common.listview.JsListViewAdapter;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.acc.common.util.AccTool;
import com.acc.common.util.BeanUtils;
import com.acc.common.util.BimoClient;
import com.acc.common.util.DateUtil;
import com.acc.common.util.JumpUtil;
import com.acc.common.util.NetUtil;
import com.acc.common.util.PermissionUtils;
import com.acc.common.util.SPHelper;
import com.acc.common.util.ServiceGenerator;
import com.acc.common.util.StringUtil;
import com.acc.common.util.ToastUtil;
import com.acc.common.widget.CustomerDialog;
import com.acc.common.widget.TitleBar;
import com.acc.jniocr.utils.ActivityUtils;
import com.acc.jniocr.utils.Config;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.anrong.sdk.CheckCarInfo;
import com.arong.swing.db.entity.KeyCar;
import com.arong.swing.db.entity.StaffUserVO;
import com.arong.swing.util.UUIDUtils;
import com.data.BaseDicValueDao;
import com.data.Car;
import com.data.CarDao;
import com.data.TVehiclesControl;
import com.data.TVehiclesControlDao;
import com.qhd.bean.CarOcrBean;
import com.qhd.bean.ReturnResultBean2;
import com.request.OnlineCheckRequest;
import com.response.BaseCar;
import com.response.CheckCarBean;
import com.response.db_beans.CheckCar;
import com.response.db_beans.CheckCarJs;
import com.response.db_beans.CheckInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hbfec.checklib.model.ControlCar;
import cn.hbfec.checklib.model.TaskCheckResult;
import cn.qqtheme.framework.AppConfig;
import guoTeng.readCard.readCard;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 核查车
 *
 * @author biehuan
 * @date 2016-11-07
 * @since 1.0
 */
public class CheckCarActivity extends Activity implements OnClickListener
        /* , SurfaceHolder.Callback, Camera.PreviewCallback */ {
    String LICENSE_OCR_NAME = "66ea648a52d79cea9861f525012c99d2.lic";
    TextView tv_empty;
    private TitleBar titlebar;
    private Button button;
    private Button buttonPz;
    private TextView rs;
    private Spinner sp_hpzl;
    private Spinner sp_cplx;
    String[] hpzlArray;
    String[] hpzlCodeArray;
    private EditText et_hphm;
    List<KeyCar> keyCars;
    private RelativeLayout rl_listbg;
    DictBusiness dictBusiness = null;
    private boolean openocr;
    String hpzl;
    String cplx;

    CheckInfo info;
    String groupid;
    CheckCar bindbean;
    String checkmode = "hand";

    private CustomerDialog progressDialog;
    private CheckCarDao checkCarDao;
    private static final int REQUEST_PZ_CODE = 1;
    private ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);

    private Button olcheck;
    private JsListViewAdapter jsListAdapter;
    private ListView jsListView;
    private BaseDicValueDao baseDicValueDao;

    private CheckCarJsDao checkCarJsDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_check_car);
        baseDicValueDao = new BaseDicValueDao(this);
        progressDialog = new CustomerDialog(CheckCarActivity.this, R.style.MyDialog);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        checkCarJsDao = new CheckCarJsDao(this);
        titlebar = findViewById(R.id.titlebar);
        titlebar.setBack(true);
        TitleBarUtils.setTile(this, titlebar, "车辆核查", true);
        if (AppConfig.USE_OFFLINE_CHECK) {
            titlebar.setTitleRight("模式切换");
            titlebar.setOnRightTextclickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean online = SPHelper.getHelper(CheckCarActivity.this).isOnline();
                    if (online) {
                        ToastUtil.showToast(CheckCarActivity.this, "离线模式");
                    } else {
                        ToastUtil.showToast(CheckCarActivity.this, "在线模式");
                    }
                    SPHelper.getHelper(CheckCarActivity.this).setOnline(!online);
                    TitleBarUtils.setTile(CheckCarActivity.this, titlebar, "车辆核查", true);
                }
            });
        }

        button = findViewById(R.id.check_car);
        button.setOnClickListener(this);
        buttonPz = findViewById(R.id.check_car_pz);
        buttonPz.setOnClickListener(this);
        rs = (TextView) findViewById(R.id.result_car);
        tv_empty = findViewById(R.id.tv_empty);
        et_hphm = findViewById(R.id.hphm);
        et_hphm.setTransformationMethod(new A2bigA());
        sp_hpzl = findViewById(R.id.spinner_hpzl);
        dictBusiness = new DictBusiness(this);
        hpzlArray = dictBusiness.queryLablesByType(DictBusiness.car_type);
        hpzlCodeArray = dictBusiness.queryCodesByType(DictBusiness.car_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hpzlArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_hpzl.setAdapter(adapter);//将adapter 添加到spinner中
        sp_hpzl.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //hpzl = (String) sp_hpzl.getSelectedItem();
                hpzl = hpzlCodeArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_cplx = findViewById(R.id.spinner_cplx);
        sp_cplx.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cplx = (String) sp_cplx.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        checkCarDao = new CheckCarDao(this);

        olcheck = findViewById(R.id.check_car_onlinecheck);
        olcheck.setOnClickListener(this);
        jsListView = findViewById(R.id.js_list);
        jsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                showJsDetail(position);
            }
        });
        jsListView.setEmptyView(tv_empty);
        rl_listbg = findViewById(R.id.rl_listbg);
        /*  initCameraView();*///todo 这个地方要取消注释，如果需要本页面ocr的话
    }

    private void showJsDetail(int position) {
        if (keyCars != null && keyCars.size() > 0) {
            String msg = "";
            msg += noNullString(keyCars.get(position).getBklxfs()) + "<br>"
                    + "联系人：" + noNullString(keyCars.get(position).getBklxr()) + "<br>"
                    + "联系人方式：" + noNullString(keyCars.get(position).getBklxfs()) + "<br>"
                    + "案情描述：" + noNullString(keyCars.get(position).getAqms()) + "<br>"
                    + "处置方式：" + noNullString(keyCars.get(position).getClfs());
            new AlertDialog.Builder(CheckCarActivity.this).setTitle("警示信息")//设置对话框标题
                    .setMessage(Html.fromHtml(msg))//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).show();//在按键响应事件中显示此对话框
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_car:
                if (TextUtils.isEmpty(et_hphm.getText())) {
                    Toast.makeText(this, "请输入车辆牌照", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkmode = "hand";
                showProgressDialog("核查中");
                tv_empty.setText("查询中");
                tvemptygray();
                if (SPHelper.getHelper(this).isOnline()) {
                    getcheck();
                } else {
                    Display(hpzl, cplx + et_hphm.getText().toString().toUpperCase());
                    bidui(hpzl, cplx + et_hphm.getText().toString().toUpperCase());
                }
                break;
            case R.id.check_car_pz:
                checkmode = "ocr";
                //todo 这个地方要取消注释，如果需要本页面ocr的话
/*                if (openocr) {
                    openocr = false;
                    buttonPz.setText("拍照");
                    re_c.setVisibility(View.GONE);
                } else {
                    openocr = true;
                    buttonPz.setText("关闭");
                    re_c.setVisibility(View.VISIBLE);
                }*/
//                Intent intent = new Intent();
//                intent.setClass(CheckCarActivity.this, PlateActivity.class);
//                startActivityForResult(intent, REQUEST_PZ_CODE);
                if (!PermissionUtils.checkPermission(this, Constants.REQUEST_PERMISSION_EXTERNAL_STORAGE, 0, "应用需要调用相机权限"))
                    return;
                String s = ActivityUtils.startCheck(CheckCarActivity.this, Config.OCR_TYPE_2, LICENSE_OCR_NAME, REQUEST_PZ_CODE, null);
                break;
            case R.id.check_car_onlinecheck:
                if (bindbean == null) {
                    ToastUtil.showToast(this, "请检查车辆");
                    return;
                }
                String clys = bindbean.getClys();
                bindbean.setClys(baseDicValueDao.queryLableBydictTypeAndValue("car_color", clys, ""));
                Bundle bundle = new Bundle();
                bundle.putString("infoid", groupid);
                bundle.putSerializable("car", bindbean);
                bundle.putBoolean("fromCar", true);
                JumpUtil.jumptoact(this, readCard.class, bundle);
            default:
                break;
        }
    }

    private void tvemptygray() {
        tv_empty.setTextColor(getResources().getColor(R.color.tv_gray));
    }

    /**
     * 在线查询
     */
    private void getcheck() {
        String url = URL.host + URL.query;

        StaffUserVO info = SPHelper.getHelper(this).getInfo();
        OnlineCheckRequest request = new OnlineCheckRequest();
        request.setUsername("anrong");
        request.setPassword("anrong");
        request.setDeviceId(NetUtil.getIMEI(this));
        request.setHpzl(hpzl);
        request.setCphm(cplx + et_hphm.getText().toString().toUpperCase());
        request.setPoliceIdcard(info.getStaffCode());
        request.setDeptId(info.getDeptId());
        request.setPoliceName(info.getStaffName());
        request.setLatitude(MyApplication.getlatitude());
        request.setLongitude(MyApplication.getLongitude());
//        request.setGroupId(info.getDeptId()); //不知道为什么这么传
        request.setGroupId(UUIDUtils.uuid32());
        request.setCheckIdcardMode(checkmode);
        request.setCheckNetworkStatus("online");
        request.setCheckObject("car");
        request.setCheckAddress(SPHelper.getHelper(CheckCarActivity.this).getCheckAddr());
        request.setCheckTaskType(SPHelper.getHelper(CheckCarActivity.this).getCheckType());

        mmHandler.sendEmptyMessage(0);


        ServiceGenerator.createService(BimoClient.class, URL.host)
                .checkCar(JSON.toJSONString(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CheckCarBean>() {
                    @Override
                    public void onNext(CheckCarBean responseBody) {
                        dismissProgressDialog();
                        if (null != responseBody) {
                            processCarInfo(responseBody);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(CheckCarActivity.this, "连接失败");
                        dismissProgressDialog();
                        tv_empty.setText("无网络连接");
                        tv_empty.setTextColor(Color.RED);
                        mmHandler.sendEmptyMessage(3);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        MyApplication.mMyOkhttp.post()
                .url(url)
                .addParam("jsonObj", JSON.toJSONString(request))
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtil.showToast(CheckCarActivity.this, "连接失败");
                        dismissProgressDialog();
                        tv_empty.setText("无网络连接");
                        tv_empty.setTextColor(Color.RED);
                        mmHandler.sendEmptyMessage(3);
                    }

                    @Override
                    public void onSuccess(int statusCode, String response) {
                        dismissProgressDialog();
                        mmHandler.sendEmptyMessage(3);
                        CheckCarBean result = JSON.parseObject(response, CheckCarBean.class);
                        processCarInfo(result);
                    }
                });
    }

    private void processCarInfo(CheckCarBean result) {
        if (result == null || result.getData() == null) {
            ToastUtil.showToast(CheckCarActivity.this, result.getMsg());
            tv_empty.setText("查询完毕");
            tvemptygray();
            return;
        }
        confirm();
        BaseCar baseCar = result.getData().getClxx();
        groupid = result.getData().getId();


        /*******把接口的数据加入本地库，一起上传********/
        createCheckHistory("online");

        bindbean = new CheckCar();
        bindbean.setId(UUIDUtils.uuid32());
        bindbean.setCheckId(CheckCarActivity.this.info.getId());
        bindbean.setCphm(cplx + et_hphm.getText().toString().toUpperCase());
        bindbean.setHpzl(hpzl);
        bindbean.setCheckTime(new Date());
        bindbean.setClys(baseDicValueDao.queryCodeBydictTypeAndValue("car_color", baseCar.getClys(), ""));
        bindbean.setNeedTransform(false);
        bindbean.setClpp(baseCar.getClpp());
        bindbean.setCzsfzh(baseCar.getCzsfzh());
        bindbean.setCzxm(baseCar.getCzxm());
        bindbean.setCzlxfs(baseCar.getCzlxfs());
        bindbean.setCzxxdz(baseCar.getCzxxdz());
        bindbean.setCldjdz(baseCar.getCldjdz());
        bindbean.setCzxm(baseCar.getCzxm());
        bindbean.setCzlxfs(baseCar.getCzlxfs());
        bindbean.setFdjh(baseCar.getFdjh());
        bindbean.setCheckTime(new Date());

        checkCarDao.add(bindbean);

        if (keyCars == null) {
            keyCars = new ArrayList<>();
        }
        keyCars.clear();
        List<com.response.KeyCar> zbclList = result.getData().getZbclList();
        if (zbclList != null && zbclList.size() > 0) {
            for (com.response.KeyCar e : zbclList) {
                KeyCar keyCar = BeanUtils.copyBeanToBean(e, KeyCar.class);
                keyCar.setPverid(e.getSjly());
                keyCars.add(keyCar);
            }
        }
        /*************************************************************/

        //查询公安部库
        CheckCarInfo cc = new CheckCarInfo();
        cc.hpzl = hpzl;
        cc.cphm = cplx + et_hphm.getText().toString().toUpperCase();
//                        try {
//                            List<KeyCar> dbkeycars = SDKService.checkCar(UUIDUtils.uuid32(), UUIDUtils.uuid32(), cc);
//                            if (dbkeycars != null && dbkeycars.size() > 0) {
//                                for (KeyCar e : dbkeycars) {
//                                    e.setPverid("gonganbu");
//                                    keyCars.add(e);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
        //查询河北库
        List<Car> cars = new CarDao(CheckCarActivity.this).loadAll(cc.cphm, hpzl);//  AppDataBase.getInstance(getApplicationContext()).carDao().loadAll(Md5Util.sfzhofMd5(cphm), hpzl);
        for (Car car : cars) {
            KeyCar keyCar = new KeyCar();
            keyCar.setAqms(car.getAqms());
            keyCar.setBklxfs(car.getBklxfs());
            keyCar.setBklxr(car.getBklxr());
            keyCar.setClfs(car.getClfs());
            keyCar.setCllb(car.getCllb());
            keyCar.setClsbdm(car.getClsdbm());
            keyCar.setClys("");
            keyCar.setCphm(car.getCphm());
            keyCar.setCzsfzh("");
            keyCar.setCzxm("");
            keyCar.setFdjh(car.getFdjh());
            keyCar.setHpzl(car.getHpzl());
            keyCar.setPfirstid("");
            keyCar.setPverid("anrongjk");
            keyCar.setRwid("");
            keyCar.setRwmc(car.getRwmc());
            keyCars.add(keyCar);
        }
        //查廊坊数据库
        TVehiclesControlDao tVehiclesControlDao = new TVehiclesControlDao(CheckCarActivity.this);
        List<TVehiclesControl> vehiclesControList = tVehiclesControlDao.queryZdclList(cc.cphm, hpzl);
        if (vehiclesControList != null && vehiclesControList.size() > 0) {
            for (TVehiclesControl vehicles : vehiclesControList) {
                KeyCar keyCar = new KeyCar();
                keyCar.setAqms(vehicles.getBksy());
                keyCar.setBklxfs(vehicles.getLrmjlxdh());
                keyCar.setBklxr(vehicles.getLrmj());
                keyCar.setClfs(vehicles.getCzfs());
                keyCar.setPfirstid("");
                keyCar.setPverid("langfangbk");
                keyCar.setRwmc("廊坊本地布控");
                keyCar.setCllb("廊坊市车辆布控");
                keyCar.setCzsfzh(vehicles.getCzsfz());
                keyCar.setCzxm(vehicles.getCzxm());
                //keyCar.setSjly("langfangbk");
                keyCars.add(keyCar);
            }
        }
        /***********************************************************/
        Display(baseCar);
        if (keyCars != null && keyCars.size() > 0) {
            /*******把接口的数据加入本地库，一起上传********/
            for (KeyCar k : keyCars) {
                if (!"010".equals(k.getPverid()) && !"020".equals(k.getPverid()) && !"099".equals(k.getPverid()) && !AccTool.nullOrEmpty(k.getPverid())) {
                    CheckCarJs checkCarJs = BeanUtils.copyBeanToBean(k, CheckCarJs.class);
                    checkCarJs.setId(UUIDUtils.uuid32());
                    checkCarJs.setCheckId(CheckCarActivity.this.info.getId());
                    checkCarJs.setCheckTime(DateUtil.getFullDateToString((CheckCarActivity.this.info.getCheckTime())));
                    checkCarJs.setSjly(k.getPverid());
                    checkCarJs.setCzxm(baseCar.getCzxm());
                    checkCarJs.setCzsfzh(baseCar.getCzsfzh());
                    checkCarJsDao.add(checkCarJs);
                }
            }

            mmHandler.sendEmptyMessage(1);
            jsListAdapter = new JsListViewAdapter(CheckCarActivity.this, keyCars, 1);
            jsListView.setAdapter(jsListAdapter);
            rl_listbg.setBackgroundResource(R.drawable.orange_stroke);
        } else {
            if (jsListAdapter != null) {
                jsListAdapter.clearData();
            }
            mmHandler.sendEmptyMessage(2);
            tv_empty.setText("无异常");
            tv_empty.setTextColor(Color.GREEN);
            rl_listbg.setBackgroundResource(R.drawable.lv_empty);
        }

    }

    /**
     * 查询三道防线数据库
     *
     * @param keyCars 重点车辆集合
     * @param cphm    车牌号
     * @param carType 车辆类别
     */
    private void searchSandaoFangXian(List<KeyCar> keyCars, String cphm, String carType) {
        TaskCheckResult taskCheckResult = SanDaoFangXianUtils.checkCar(this, cphm, carType, Constants.SDFX_USERNAME, Constants.SDFX_PASSWORD);
        //请求成功
        if ("000".equals(taskCheckResult.getCode())) {
            ControlCar car = taskCheckResult.getCar();
            if (null != car) {
                KeyCar keyCar = new KeyCar();
                keyCar.setAqms(car.getLkly());
                keyCar.setBklxfs(car.getBklxfs());
                keyCar.setBklxr(car.getBklxr());
                keyCar.setClfs(car.getClfs());
                keyCar.setPfirstid("");
                keyCar.setPverid("st-sandaofangxian");
                keyCar.setRwmc("三道防线布控");
                keyCar.setCllb("三道防线车辆布控");
                keyCar.setCzsfzh(car.getCzsfzh());
                keyCar.setCzxm(car.getCzxm());
                keyCars.add(keyCar);
            }
        } else {
            ToastUtil.showToastLong(this, taskCheckResult.getMsg());
        }
    }

    private void Display(String hpzl, String hm) {
        String zl = null;
        if (hpzl != null) {
//            switch (hpzl) {
//                case "01":
//                    zl = "大型汽车";
//                    break;
//                case "02":
//                    zl = "小型汽车";
//                    break;
            zl = dictBusiness.getDictLablByTypeAndValue(DictBusiness.car_type, hpzl);
//            }
        }
        TextView v;
        v = findViewById(R.id.textView1);
        v.setText(noNullString(zl) + "  " + noNullString(hm));
    }

    private String noNullString(String s) {
        if (StringUtil.isNullOrEmpty(s)) {
            return "";
        }
        if (StringUtil.isNullOrEmpty(s.trim())) {
            return "";
        }
        if ("null".equals(s)) {
            return "";
        }
        if (com.acc.common.util.StringUtil.isNotNull(s)) {
            return s;
        }
        return "";
    }

    private void Display(BaseCar baseCar) {
        if (baseCar == null) {
            return;
        }
        try {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hpzl = baseCar.getHpzl();
        ImageView mImageView = findViewById(R.id.imageView1);
        if (hpzl != null) {
            switch (hpzl) {
                case "大型汽车":
                    mImageView.setImageResource(R.mipmap.ic_bigcar);
                    break;
                case "小型汽车":
                    mImageView.setImageResource(R.mipmap.ic_smallcar);
                    break;
            }
        }
        TextView v;
        v = findViewById(R.id.textView1);
        v.setText(noNullString(hpzl) + " " + noNullString(baseCar.getCphm()) + " " + noNullString(baseCar.getClpp()) + " " + noNullString(baseCar.getClys()));
        v = findViewById(R.id.textView2);
        v.setText(noNullString(baseCar.getCzsfzh()));
        v = findViewById(R.id.textView3);
        v.setText(noNullString(baseCar.getCzxm()) + " " + noNullString(baseCar.getCzlxfs()));
        v = findViewById(R.id.textView4);
        v.setText(noNullString("登记地址:") + noNullString(baseCar.getCzxxdz()));
    }

    private final Handler mmHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                button.setEnabled(false);
                buttonPz.setEnabled(false);
//                rs.setTextColor(Color.parseColor("#000000"));
                rs.setText("正在查询");
            } else if (msg.what == 2) {
//                rs.setTextColor(Color.parseColor("#00cc00"));
                rs.setText("正常车辆");
                button.setEnabled(true);
                buttonPz.setEnabled(true);
            } else if (msg.what == 1) {
//                rs.setTextColor(Color.parseColor("#ff0000"));
                showDialog();
                rs.setText("非正常车辆" + "(" + keyCars.size() + ")");
                button.setEnabled(true);
                buttonPz.setEnabled(true);
            } else if (msg.what == 3) {
//                rs.setTextColor(Color.parseColor("#BEBEBE"));
                rs.setText("警示信息");
                button.setEnabled(true);
                buttonPz.setEnabled(true);
            }
        }
    };

    private void showDialog() {
        if (keyCars != null && keyCars.size() > 0) {
            String msg = "";
            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < keyCars.size(); i++) {
                msg += keyCars.get(i).getCllb() + "<br>";
                stringList.add(keyCars.get(i).getCllb());
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckCarActivity.this);
            View view = View.inflate(CheckCarActivity.this, R.layout.layout_warndialog, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            ListView lv = view.findViewById(R.id.listview);
            WarnAdapter adapter = new WarnAdapter(stringList, CheckCarActivity.this);
            lv.setAdapter(adapter);
            dialog.show();
            Button button = view.findViewById(R.id.tvok);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    /**
     * 核查车辆信息
     *
     * @param hpzl 号牌种类
     * @param cphm 车牌号码
     */
    private void bidui(String hpzl, String cphm) {
        mmHandler.sendEmptyMessage(0);
        try {
            //查询公安部库
            CheckCarInfo cc = new CheckCarInfo();
            cc.hpzl = hpzl;
            cc.cphm = cphm;
            if (keyCars == null) {
                keyCars = new ArrayList<>();
            }
            keyCars.clear();
//            List<KeyCar> keygonganbu = SDKService.checkCar(UUIDUtils.uuid32(), UUIDUtils.uuid32(), cc);
//            if (keygonganbu != null && keygonganbu.size() > 0) {
//                for (KeyCar e : keygonganbu) {
//                    e.setPverid("gonganbu");
//                    keyCars.add(e);
//                }
//            }
            //查询河北库
            List<Car> cars = new CarDao(this).loadAll(cphm, hpzl);//  AppDataBase.getInstance(getApplicationContext()).carDao().loadAll(Md5Util.sfzhofMd5(cphm), hpzl);
            for (Car car : cars) {
                KeyCar keyCar = new KeyCar();
                keyCar.setAqms(car.getAqms());
                keyCar.setBklxfs(car.getBklxfs());
                keyCar.setBklxr(car.getBklxr());
                keyCar.setClfs(car.getClfs());
                keyCar.setCllb(car.getCllb());
                keyCar.setClsbdm(car.getClsdbm());
                keyCar.setClys("");
                keyCar.setCphm(car.getCphm());
                keyCar.setCzsfzh("");
                keyCar.setCzxm("");
                keyCar.setFdjh(car.getFdjh());
                keyCar.setHpzl(car.getHpzl());
                keyCar.setPfirstid("");
                keyCar.setPverid("local");
                keyCar.setRwid("");
                keyCar.setRwmc(car.getRwmc());
                keyCars.add(keyCar);
            }
            //查廊坊数据库
            TVehiclesControlDao tVehiclesControlDao = new TVehiclesControlDao(this);
            List<TVehiclesControl> vehiclesControList = tVehiclesControlDao.queryZdclList(cphm, hpzl);
            if (vehiclesControList != null && vehiclesControList.size() > 0) {
                for (TVehiclesControl vehicles : vehiclesControList) {
                    KeyCar keyCar = new KeyCar();
                    keyCar.setAqms(vehicles.getBksy());
                    keyCar.setBklxfs(vehicles.getLrmjlxdh());
                    keyCar.setBklxr(vehicles.getLrmj());
                    keyCar.setClfs(vehicles.getCzfs());
                    keyCar.setPfirstid("");
                    keyCar.setPverid("langfangbk");
                    keyCar.setRwmc("廊坊本地布控");
                    keyCar.setCllb("廊坊市车辆布控");
                    keyCar.setCzsfzh(vehicles.getCzsfz());
                    keyCar.setCzxm(vehicles.getCzxm());
                    //keyCar.setSjly("langfangbk");
                    keyCars.add(keyCar);
                }
            }
            //查询三道防线数据库
            searchSandaoFangXian(keyCars, cc.cphm, hpzl);

            createCheckHistory("offline");
            CheckCar bean = createCheckCarBean(hpzl, cphm);
            bindbean = bean;
            checkCarDao.add(bean);
            if (keyCars != null && keyCars.size() > 0) {
                mmHandler.sendEmptyMessage(1);
                for (KeyCar k : keyCars) {
                    CheckCarJs checkCarJs = BeanUtils.copyBeanToBean(k, CheckCarJs.class);
                    checkCarJs.setId(UUIDUtils.uuid32());
                    checkCarJs.setCheckId(info.getId());
                    checkCarJs.setCheckTime(DateUtil.getFullDateToString(info.getCheckTime()));
                    checkCarJs.setSjly(k.getPverid());
                    checkCarJsDao.add(checkCarJs);
                }
                jsListAdapter = new JsListViewAdapter(CheckCarActivity.this, keyCars, 1);
                jsListView.setAdapter(jsListAdapter);
                rl_listbg.setBackgroundResource(R.drawable.orange_stroke);
            } else {
                mmHandler.sendEmptyMessage(2);
                if (jsListAdapter != null) {
                    jsListAdapter.clearData();
                }
                tv_empty.setText("无异常");
                tv_empty.setTextColor(Color.GREEN);
                rl_listbg.setBackgroundResource(R.drawable.lv_empty);
            }
        } catch (Exception e) {
            e.printStackTrace();
            tv_empty.setText("查询完毕");
            tvemptygray();
            mmHandler.sendEmptyMessage(3);
        }
        dismissProgressDialog();
    }

    private CheckCar createCheckCarBean(String hpzl, String cphm) {
        CheckCar bean = new CheckCar();
        bean.setId(UUIDUtils.uuid32());
        bean.setCheckId(info.getId());
        bean.setHpzl(hpzl);
        bean.setCphm(cphm);
        bean.setCheckTime(new Date());
        bean.setNeedTransform(true);
        return bean;
    }

    private void createCheckHistory(String net) {
        StaffUserVO userVO = SPHelper.getHelper(CheckCarActivity.this).getInfo();
        info = new CheckInfo();
        info.setId(UUIDUtils.uuid32());
        info.setCheckIdcardMode(checkmode);
        info.setCheckNetworkStatus(net);
        info.setCheckObject("car");
        info.setCheckTime(new Date());
        info.setDeptId(userVO.getDeptId());
        info.setDeviceId(NetUtil.getIMEI(CheckCarActivity.this));
        groupid = info.getId();
        info.setGroupId(groupid);
        info.setLatitude(MyApplication.getlatitude());
        info.setLongitude(MyApplication.getLongitude());
        info.setPoliceIdcard(userVO.getStaffCode());
        info.setPoliceName(userVO.getStaffName());
        info.setCreateBy(userVO.getLoginName());
        info.setCreateDate(new Date());
        info.setUpdateBy(userVO.getLoginName());
        info.setUpdateDate(new Date());
        info.setCheckAddress(SPHelper.getHelper(CheckCarActivity.this).getCheckAddr());
        info.setCheckTaskType(SPHelper.getHelper(CheckCarActivity.this).getCheckType());
        new CheckInfoDao(this).add(info);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PZ_CODE) {
            if (data != null) {
                String data1 = data.getStringExtra(Config.RQUERY_OCR_DATA_KEY);
                ReturnResultBean2 resultBean = JSON.parseObject(data1, ReturnResultBean2.class);
                if (resultBean.getState() == 200) {
                    CarOcrBean ocrBean = resultBean.getMessage();
                    if ("蓝".equals(ocrBean.getCarColor())) {
                        hpzl = "02";
                    } else {
                        hpzl = "01";
                    }
                    for (int i = 0; i < sp_hpzl.getCount(); i++) {
                        if (hpzl.equals(getResources().getStringArray(R.array.hpzl_code)[i])) {
                            hpzl = getResources().getStringArray(R.array.hpzl_code)[i];
                            sp_hpzl.setSelection(i);
                            break;
                        }
                    }
                    cplx = ocrBean.getCarNO().substring(0, 1);
                    for (int i = 0; i < sp_cplx.getCount(); i++) {
                        if (cplx.equals(sp_cplx.getItemAtPosition(i).toString())) {
                            sp_cplx.setSelection(i);
                            break;
                        }
                    }
                    et_hphm.setText(ocrBean.getCarNO().substring(1));
                    showProgressDialog("核查中");
                    tv_empty.setText("查询中");
                    tvemptygray();
                    if (SPHelper.getHelper(this).isOnline()) {
                        getcheck();
                    } else {
                        if (TextUtils.isEmpty(et_hphm.getText())) {
                            Toast.makeText(this, "请输入车辆牌照", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Display(hpzl, cplx + et_hphm.getText().toString().toUpperCase());
                        bidui(hpzl, cplx + et_hphm.getText().toString().toUpperCase());
                    }
                }

            }
        }
        if (requestCode == 200) {
            TitleBarUtils.setTile(this, titlebar, "人员核查", true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class A2bigA extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }

    }

/*    //private Button photo_button = null;
    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/alpha/Plate/";
    private static final String UserID = "5DDDC395381FC1F48F49";
    private Camera camera;
    private SurfaceView surfaceView;
    private RelativeLayout re_c;
    private SurfaceHolder surfaceHolder;
    private PlateAPI api = null;
    private Bitmap bitmap;
    private int preWidth = 0;
    private int preHeight = 0;
    private int width;
    private int height;
    private TimerTask timer;
    private Timer timer2;
    private Vibrator mVibrator;
    private ToneGenerator tone;
    private byte[] tackData;
    private PLViewfinderView myView;
    private long recogTime;
    private boolean isFatty = false;
    private boolean bInitKernal = false;
    AlertDialog alertDialog = null;
    private int[] m_ROI = {0, 0, 0, 0};
    private boolean isROI = false;
    private int rotation = -1;
    Toast toast;
    String FilePath = "";
    private ImageButton back;
    private ImageButton flash;
    private boolean baddView = false;*/

 /*   private void initCameraView() {
//        try {
        copyDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        File file = new File(PATH);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        int uiRot = getWindowManager().getDefaultDisplay().getRotation();// 获取屏幕旋转的角度
        System.out.println("旋转角度——————" + uiRot);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int metricwidth = metric.widthPixels; // 屏幕宽度（像素）
        int metricheight = metric.heightPixels; // 屏幕高度（像素）
        System.out.println("屏幕宽度：" + metricwidth + "   屏幕高度" + metricheight);

        if (metricwidth >= metricheight) {
            //设备横屏状态时 屏幕旋转角度为1或者3时执行以下代码
            if (uiRot == 1 || uiRot == 3) {
                switch (uiRot) {
                    case 1:
                        rotation = 0;

                        break;
                    case 3:
                        rotation = 180;

                        break;
                }
            } else {
                ////设备横屏状态时 屏幕旋转角度为2或者0时执行以下代码
                switch (uiRot) {
                    case 0:
                        rotation = 0;

                        break;

                    case 2:
                        rotation = 180;

                        break;

                }
            }
        } else if (metricheight >= metricwidth) {
            //设备竖屏状态时 屏幕旋转角度为0或者2时执行以下代码
            if (uiRot == 0 || uiRot == 2) {

                switch (uiRot) {
                    case 0:
                        rotation = 90;

                        break;

                    case 2:
                        rotation = 270;

                        break;

                }
            } else {
                //设备竖屏状态时 屏幕旋转角度为1或者3时执行以下代码
                switch (uiRot) {
                    case 1:
                        rotation = 270;

                        break;
                    case 3:
                        rotation = 90;

                        break;
                }
            }
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
        Configuration cf = this.getResources().getConfiguration(); //获取设置的配置信息
        int noriention = cf.orientation;
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
        }
        if (noriention == Configuration.ORIENTATION_PORTRAIT) {
            if (api == null) {
                api = new PlateAPI();
                //FilePath =Environment.getExternalStorageDirectory().toString()+"/"+UserID+".lic";
                String cacheDir = (this.getCacheDir()).getPath();
                FilePath = cacheDir + "/" + UserID + ".lic";
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                int nRet = api.ETInitPlateKernal("", FilePath, UserID, 0x06, 0x01, telephonyManager, this);
                if (nRet != 0) {
                    Toast.makeText(getApplicationContext(), "激活失败", Toast.LENGTH_SHORT).show();
                    System.out.print("nRet=" + nRet);
                    bInitKernal = false;
                } else {
                    System.out.print("nRet=" + nRet);
                    bInitKernal = true;
                }
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // // 屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findView();
    }*/

/*    public void copyDataBase() {
        //  Common common = new Common();
        String cacheDir = (this.getCacheDir()).getPath();
        // String dst = Environment.getExternalStorageDirectory().toString() + "/"+UserID+".lic";
        String dst = cacheDir + "/" + UserID + ".lic";
        File file = new File(dst);
        if (!file.exists()) {
            // file.createNewFile();
        } else {
            file.delete();
        }

        try {
            InputStream myInput = getAssets().open(UserID + ".lic");
            OutputStream myOutput = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            System.out.println(UserID + ".lic" + "is not found");
        }
    }*/

    /*   protected void onRestart() {
           if (bitmap != null) {
               bitmap.recycle();
               bitmap = null;
           }
           super.onRestart();
       }
   */
    @Override
    protected void onResume() {
        super.onResume();
    }

 /*   private void findView() {
        surfaceView = findViewById(R.id.surfaceViwe);
        re_c = findViewById(R.id.re_c);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        height = metric.heightPixels; // 屏幕高度（像素）
        if (width * 3 == height * 4) {
            isFatty = true;
        }
        back = findViewById(R.id.back);
        flash = findViewById(R.id.flash);

//        int back_w = (int) (height * 0.066796875);
//        int back_h = (int) (back_w * 1);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        layoutParams.leftMargin = (int) ((width * 0.10));
//        layoutParams.topMargin = (int) (height * 0.0486111111111111111111111111111);
//        back.setLayoutParams(layoutParams);

//        int flash_w = (int) (height * 0.066796875);
//        int flash_h = (int) (flash_w * 1);
//        layoutParams = new RelativeLayout.LayoutParams(flash_w, flash_h);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        layoutParams.leftMargin = (int) (((width - width * 0.150)) - flash_w);
//        layoutParams.topMargin = (int) (height * 0.0486111111111111111111111111111);
//        flash.setLayoutParams(layoutParams);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    if (camera != null) {
//                        camera.setPreviewCallback(null);
//                        camera.stopPreview();
//                        camera.release();
//                        camera = null;
//                    }
//                } catch (Exception e) {
//                }
//                if (toast != null) {
//                    toast.cancel();
//                    toast = null;
//                }
//                if (timer2 != null) {
//                    timer2.cancel();
//                    timer2 = null;
//                }
//                if (alertDialog != null) {
//                    alertDialog.dismiss();
//                    alertDialog.cancel();
//                    alertDialog = null;
//                }
//                if (api != null) {
//                    api.ETUnInitPlateKernal();
//                    api = null;
//                }
                re_c.setVisibility(View.GONE);
            }
        });
        flash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    String mess = "当前设备不支持闪光灯";
                    Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
                } else {
                    if (camera != null) {
                        Camera.Parameters parameters = camera.getParameters();
                        String flashMode = parameters.getFlashMode();
                        if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            parameters.setExposureCompensation(0);
                        } else {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 闪光灯常亮
                            parameters.setExposureCompensation(-1);
                        }
                        try {
                            camera.setParameters(parameters);
                        } catch (Exception e) {
                            String mess = "当前设备不支持闪光灯";
                            Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
                        }
                        camera.startPreview();
                    }
                }
            }
        });

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(CheckCarActivity.this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }*/


 /*   private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            try {
                if (tone == null) {
                    tone = new ToneGenerator(1, ToneGenerator.MIN_VOLUME);
                }
                tone.startTone(ToneGenerator.TONE_PROP_BEEP);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
*/

/*    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (api == null) {
            api = new PlateAPI();
            //FilePath =Environment.getExternalStorageDirectory().toString()+"/7332DBAFD2FD18301EF6.lic";
            String cacheDir = (this.getCacheDir()).getPath();
            FilePath = cacheDir + "/" + UserID + ".lic";
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int nRet = api.ETInitPlateKernal("", FilePath, "7332DBAFD2FD18301EF6", 0x06, 0x01, telephonyManager, this);
            if (nRet != 0) {
                Toast.makeText(getApplicationContext(), "激活失败", Toast.LENGTH_SHORT).show();
                System.out.print("nRet=" + nRet);
                bInitKernal = false;
            } else {
                System.out.print("nRet=" + nRet);
                bInitKernal = true;
            }
        }
        if (camera == null) {
            try {
                camera = Camera.open();
            } catch (Exception e) {
                e.printStackTrace();
                String mess = getResources().getString(R.string.toast_camera);
                Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_LONG).show();
                return;
            }
        }
        try {
            */

    /**
     * 禁止打开相机时在此崩溃,TODO
     *//*
            camera.setPreviewDisplay(holder);
            initCamera(holder);
            Timer time = new Timer();
            if (timer == null) {
                timer = new TimerTask() {
                    public void run() {
                        //isSuccess=false;
                        if (camera != null) {
                            try {
                                camera.autoFocus(new Camera.AutoFocusCallback() {
                                    public void onAutoFocus(boolean success, Camera camera) {
                                        // isSuccess=success;
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                };
            }
            time.schedule(timer, 500, 2500);

        } catch (IOException e) {
            e.printStackTrace();

        }

        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
        }
    }


    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        synchronized (camera) {
                            new Thread() {
                                public void run() {
                                    initCamera(holder);
                                    super.run();
                                }
                            }.start();
                        }
                        // camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
                    }
                }
            });
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
        }
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog.cancel();
            alertDialog = null;
        }
        if (api != null) {
            api.ETUnInitPlateKernal();
            api = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (camera != null) {
                    camera.setPreviewCallback(null);
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (api != null) {
                api.ETUnInitPlateKernal();
                bInitKernal = false;
                api = null;
            }
            finish();
            if (toast != null) {
                toast.cancel();
                toast = null;
            }
            if (timer2 != null) {
                timer2.cancel();
                timer2 = null;
            }
            if (alertDialog != null) {
                alertDialog.cancel();
                alertDialog = null;
            }
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }*/

/*    @TargetApi(14)
    private void initCamera(SurfaceHolder holder) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        Camera.Size size;
        int length = list.size();
        Camera.Size tmpsize = list.get(0);
        if (rotation == 90) {
            tmpsize = getOptimalPreviewSize(list, height, width);
        } else {
            tmpsize = getOptimalPreviewSize(list, width, height);
        }

        int previewWidth = list.get(0).width;
        int previewheight = list.get(0).height;
        previewWidth = tmpsize.width;
        previewheight = tmpsize.height;
        int second_previewWidth = 0;
        int second_previewheight = 0;
//			int nlast = -1;
//			int nThird =-1;
//			int Third_previewWidth = 0;
//			int Third_previewheight = 0;
//			if (length == 1) {
//				size = list.get(0);
//				previewWidth = size.width;
//				previewheight = size.height;
//			} else {
//
//				for (int i = 0; i < length; i++) {
//					size = list.get(i);
//					second_previewWidth = size.width;
//					second_previewheight = size.height;
//					if(rotation==90)
//					{
//						if((size.height==width||size.width==height) && nThird==-1)
//						{
//							if(size.height ==width &&size.width >=height)
//							{
//								Third_previewWidth  = size.width;
//								Third_previewheight = size.height;
//								nThird =i;
//							}
//							else if(size.width ==height &&size.height >=width)
//							{
//								Third_previewWidth  = size.width;
//								Third_previewheight = size.height;
//								nThird =i;
//							}
//						}
//							if (second_previewWidth * width == second_previewheight * height) {
        //
//								if(second_previewWidth >800){
//				                     if(second_previewWidth== width && nlast == -1){
//				                    	 previewWidth = second_previewWidth;
//										 previewheight = second_previewheight;
//										 nlast = i;
//				                     }
//				                     else if(second_previewWidth< width){
//				                    	 previewWidth = second_previewWidth;
//										 previewheight = second_previewheight;
//										 nlast = i;
//				                     }
//
//								}
//							}
//					}
//					else{
//						if((size.height==height||size.width==width) && nThird==-1)
//						{
//							Third_previewWidth  = size.width;
//							Third_previewheight = size.height;
//							nThird =i;
//						}
//						if (second_previewWidth * height == second_previewheight * width) {
        //
//								if(second_previewWidth >800){
//				                     if(second_previewWidth== width && nlast == -1){
//				                    	 previewWidth = second_previewWidth;
//										 previewheight = second_previewheight;
//										 nlast = i;
//				                     }
//				                     else if(second_previewWidth< width){
//				                    	 previewWidth = second_previewWidth;
//										 previewheight = second_previewheight;
//										 nlast = i;
//				                     }
//
//								}
//							}
//					}
//
////					}
//				}
//			if(nlast==-1 && nThird!=-1){
//				preWidth = Third_previewWidth;
//			      preHeight = Third_previewheight;
//			}
//			else{
//			   preWidth = previewWidth;
//		       preHeight = previewheight;
//			 }
//			}
        if (length == 1) {
            preWidth = previewWidth;
            preHeight = previewheight;
        } else {
            second_previewWidth = previewWidth;
            second_previewheight = previewheight;
            for (int i = 0; i < length; i++) {
                size = list.get(i);
                if (size.height > 700) {
                    if (size.width * previewheight == size.height * previewWidth && size.height < second_previewheight) {
                        second_previewWidth = size.width;
                        second_previewheight = size.height;
                    }
                }
            }
            preWidth = second_previewWidth;
            preHeight = second_previewheight;
        }
        if (!isROI) {
            int t;
            int b;
            int l;
            int r;
            l = height / 5;
            r = height * 3 / 5;
            t = 4;
            b = width - 4;
            double proportion = (double) height / (double) preWidth;
            double hproportion = (double) width / (double) preHeight;
            l = (int) (l / proportion);
            t = (int) (t / hproportion);
            r = (int) (r / proportion);
            b = (int) (b / hproportion);
            int borders[] = {l, t, r, b};
            m_ROI[0] = l;
            m_ROI[1] = t;
            m_ROI[2] = r;
            m_ROI[3] = b;
            api.ETSetPlateROI(borders, preWidth, preHeight);
            isROI = true;
        }
        if (!baddView) {
            if (isFatty)
                myView = new PLViewfinderView(this, width, height, isFatty, 1);
            else
                myView = new PLViewfinderView(this, width, height, 1);
            re_c.addView(myView);
            baddView = true;
        }

        parameters.setPictureFormat(PixelFormat.JPEG);

        parameters.setPreviewSize(preWidth, preHeight);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        if (parameters.isZoomSupported()) {
            parameters.setZoom(2);
        }

        camera.setPreviewCallback(this);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        //camera.cancelAutoFocus();
    }*/

 /*   public String savePicture(Bitmap bitmap, String tag) {
        String strCaptureFilePath = PATH + tag + "_Plate_" + pictureName() + ".jpg";
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(strCaptureFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "图片存储失败,请检查SD卡", Toast.LENGTH_SHORT).show();
        }
        return strCaptureFilePath;
    }*/

/*    public String pictureName() {
        String str = "";
        Time t = new Time();
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        if (month < 10)
            str = String.valueOf(year) + "0" + String.valueOf(month);
        else {
            str = String.valueOf(year) + String.valueOf(month);
        }
        if (date < 10)
            str = str + "0" + String.valueOf(date + "_");
        else {
            str = str + String.valueOf(date + "_");
        }
        if (hour < 10)
            str = str + "0" + String.valueOf(hour);
        else {
            str = str + String.valueOf(hour);
        }
        if (minute < 10)
            str = str + "0" + String.valueOf(minute);
        else {
            str = str + String.valueOf(minute);
        }
        if (second < 10)
            str = str + "0" + String.valueOf(second);
        else {
            str = str + String.valueOf(second);
        }
        return str;
    }
    private int counter = 0;
    private int counterCut = 0;*/


/*
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        tackData = data;
        Camera.Parameters parameters = camera.getParameters();
        int buffl = 256;
        char recogval[] = new char[buffl];
        Date dt = new Date();
        Long timeStart = System.currentTimeMillis();
//        if (!progressDialog.isShowing()) {
        if (!alertDialog.isShowing()) {
            int pLineWarp[] = new int[800 * 45];
            int nv21Width = parameters.getPreviewSize().width;
            int nv21Height = parameters.getPreviewSize().height;
            int r = api.RecognizePlateNV21(data, 1, nv21Width, nv21Height, recogval, buffl, pLineWarp);
            Long timeEnd = System.currentTimeMillis();
            if (r == 0) {
                //camera.stopPreview();
                //api.WTUnInitCardKernal();

                // 震动
                recogTime = (timeEnd - timeStart);
                String plateNo = api.GetRecogResult(0);
                String plateColor = api.GetRecogResult(1);
                mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                mVibrator.vibrate(50);
                // 删除正常识别保存图片功能
                int[] datas = convertYUV420_NV21toARGB8888(tackData, parameters.getPreviewSize().width,
                        parameters.getPreviewSize().height);


                String str = plateNo;//String.valueOf(recogval);
                str += "\r\n车牌颜色:";
                str += plateColor;
                str += "\r\n识别时间:" + recogTime;
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setMessage(str);
                Window window = alertDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                // 设置透明度为0.3
                lp.alpha = 0.8f;
                lp.width = width * 2 / 3;
                //lp.flags= 0x00000020;
                window.setAttributes(lp);
                window.setGravity(Gravity.CENTER);
                alertDialog.show();
                showProgressDialog("核查中");
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inInputShareable = true;
                opts.inPurgeable = true;
                bitmap = Bitmap.createBitmap(datas, parameters.getPreviewSize().width,
                        parameters.getPreviewSize().height, android.graphics.Bitmap.Config.ARGB_8888);
                Bitmap tmpbitmap = Bitmap.createBitmap(bitmap, m_ROI[0], m_ROI[1], m_ROI[2] - m_ROI[0], m_ROI[3] - m_ROI[1]);
                System.out.println("m_ROI:" + m_ROI[0] + " " + m_ROI[1] + " " + m_ROI[2] + " " + m_ROI[3]);
                //savePicture(tmpbitmap, "P");
                String strFilePath = PATH + "Plate_" + pictureName() + ".jpg";
                api.SavePlateImg(strFilePath, 0);
                String strFileRROIPath = PATH + "Plate_ROI_" + pictureName() + ".jpg";
                api.SavePlateImg(strFileRROIPath, 1);
                //savePicture(bitmap,"FP");
//                Intent intent = new Intent();
//                intent.putExtra("plateNo", plateNo);
//                intent.putExtra("plateColor", plateColor);
//                setResult(RESULT_OK, intent);
//                finish();
                if ("蓝".equals(plateColor)) {
                    hpzl = "02";
                } else {
                    hpzl = "01";
                }
                for (int i = 0; i < sp_hpzl.getCount(); i++) {
                    if (hpzl.equals(getResources().getStringArray(R.array.hpzl_code)[i])) {
                        hpzl = getResources().getStringArray(R.array.hpzl_code)[i];
                        sp_hpzl.setSelection(i);
                        break;
                    }
                }
                cplx = plateNo.substring(0, 1);
                for (int i = 0; i < sp_cplx.getCount(); i++) {
                    if (cplx.equals(sp_cplx.getItemAtPosition(i).toString())) {
                        sp_cplx.setSelection(i);
                        break;
                    }
                }
                et_hphm.setText(plateNo.substring(1));
//                button.performClick();
                if (SPHelper.getHelper(this).isOnline()) {
                    getcheck();
                } else {
                    if (TextUtils.isEmpty(et_hphm.getText())) {
                        Toast.makeText(this, "请输入车辆牌照", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Display(hpzl, cplx + et_hphm.getText().toString().toUpperCase());
                    bidui(hpzl, cplx + et_hphm.getText().toString().toUpperCase());
                }
            }
        }
    }*/

/*    @Override
    protected void onStop() {

        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        if (alertDialog != null) {
            alertDialog.cancel();
            alertDialog.dismiss();
        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
        }
        if (api != null) {
            api.ETUnInitPlateKernal();
            api = null;
        }
    }*/

 /*   @TargetApi(14)
    private void NewApis(Camera.Parameters parameters) {
        if (Build.VERSION.SDK_INT >= 14) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
    }

    public int[] convertYUV420_NV21toARGB8888(byte[] data, int width, int height) {
        int size = width * height;
        int offset = size;
        int[] pixels = new int[size];
        int u, v, y1, y2, y3, y4;

        // i along Y and the final pixels
        // k along pixels U and V
        for (int i = 0, k = 0; i < size; i += 2, k += 2) {
            y1 = data[i] & 0xff;
            y2 = data[i + 1] & 0xff;
            y3 = data[width + i] & 0xff;
            y4 = data[width + i + 1] & 0xff;

            u = data[offset + k] & 0xff;
            v = data[offset + k + 1] & 0xff;
            u = u - 128;
            v = v - 128;

            pixels[i] = convertYUVtoARGB(y1, u, v);
            pixels[i + 1] = convertYUVtoARGB(y2, u, v);
            pixels[width + i] = convertYUVtoARGB(y3, u, v);
            pixels[width + i + 1] = convertYUVtoARGB(y4, u, v);

            if (i != 0 && (i + 2) % width == 0)
                i += width;
        }

        return pixels;
    }*/

/*
    private int convertYUVtoARGB(int y, int u, int v) {
        int r, g, b;

        r = y + (int) 1.402f * u;
        g = y - (int) (0.344f * v + 0.714f * u);
        b = y + (int) 1.772f * v;
        r = r > 255 ? 255 : r < 0 ? 0 : r;
        g = g > 255 ? 255 : g < 0 ? 0 : g;
        b = b > 255 ? 255 : b < 0 ? 0 : b;
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (size.height < 700) continue;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (size.height < 700) continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                } else if (Math.abs(size.height - targetHeight) == minDiff && size.width > optimalSize.width) {
                    optimalSize = size;
                }
            }
        }
        return optimalSize;
    }
*/
    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new CustomerDialog(CheckCarActivity.this, R.style.MyDialog);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }
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

    private void confirm() {
        MyApplication.mMyOkhttp.get()
                .url(URL.host + URL.Confirm)
                .addParam("id", NetUtil.getIMEI(CheckCarActivity.this))
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }

                    @Override
                    public void onSuccess(int statusCode, String response) {
                    }
                });
    }
}
