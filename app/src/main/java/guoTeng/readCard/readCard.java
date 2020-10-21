package guoTeng.readCard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Utils.SanDaoFangXianUtils;
import com.acc.common.Constants;
import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.dao.CheckInfoDao;
import com.acc.common.dao.CheckPersonDao;
import com.acc.common.dao.CheckPersonJsDao;
import com.acc.common.listview.JsListViewAdapter;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.AccTool;
import com.acc.common.util.BeanUtils;
import com.acc.common.util.BimoClient;
import com.acc.common.util.DateUtil;
import com.acc.common.util.NetUtil;
import com.acc.common.util.PermissionUtils;
import com.acc.common.util.SPHelper;
import com.acc.common.util.ServiceGenerator;
import com.acc.common.util.ToastUtil;
import com.acc.common.util.VerifyCheck;
import com.acc.common.widget.CustomerDialog;
import com.acc.common.widget.TitleBar;
import com.acc.jniocr.utils.ActivityUtils;
import com.acc.jniocr.utils.Config;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.anrong.sdk.CheckPersonInfo;
import com.arong.swing.db.entity.KeyPerson;
import com.arong.swing.db.entity.StaffUserVO;
import com.arong.swing.util.StringUtil;
import com.arong.swing.util.UUIDUtils;
import com.cnnk.IdentifyCard.Cvr100bUtil;
import com.cnnk.IdentifyCard.IC2ReadCard;
import com.cnnk.IdentifyCard.RequestParameter;
import com.data.BaseDicValueDao;
import com.data.Person;
import com.data.PersonDao;
import com.data.TPersonControl;
import com.data.TPersonControlDao;
import com.qhd.TitleBarUtils;
import com.qhd.WarnAdapter;
import com.qhd.bean.PersonOcrBean;
import com.qhd.bean.ReturnResultBean1;
import com.request.OnlineCheckRequest;
import com.response.BasePerson;
import com.response.CheckDetailResponse;
import com.response.db_beans.CheckCar;
import com.response.db_beans.CheckInfo;
import com.response.db_beans.CheckPerson;
import com.response.db_beans.CheckPersonJs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hbfec.checklib.model.ControlPerson;
import cn.hbfec.checklib.model.TaskCheckResult;
import cn.qqtheme.framework.AppConfig;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * 人员核查。
 */
public class readCard extends Activity implements OnClickListener {

    private IC2ReadCard ic;
    private ReadCardThread mReadCardThread;
    private CustomerDialog progressDialog;
    private RadioGroup radioGroup0;
    private RadioButton radioButton0;
    /**
     * 当前查询的人员信息
     */
    private CheckPerson mCheckPerson;
    /**
     * 提示是否高于37.3,true是高，false是低
     */
    public ObservableBoolean mIsHighTw = new ObservableBoolean(false);
    /**
     * 手机号
     */
    public ObservableField<String> mPhone = new ObservableField<>();
    /**
     * 是否为手动点击体温
     */
    public boolean mIsDj = true;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            switch (paramMessage.what) {
                case 1:
                    Toast.makeText(readCard.this, RequestParameter.getRequestParameter().getAttr("cvrInfo"), Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 2:
                    //清空数据
                    clearInfo();

                    String[] arrayOfString = Cvr100bUtil.getInstance().getDecodeInfo();
                    displayPerson = new BasePerson();
                    displayPerson.setXm(arrayOfString[0]);
                    displayPerson.setXb(arrayOfString[1]);
                    displayPerson.setMz(arrayOfString[2]);
                    displayPerson.setCsrq(arrayOfString[3]);
                    displayPerson.setHjdz(arrayOfString[4]);
                    displayPerson.setSfzh(arrayOfString[5]);
                    displayPerson.setFzpcs(arrayOfString[6]);
                    String yxq = "";
                    if (com.acc.common.util.StringUtil.isNotNull(arrayOfString[7])) {
                        if (arrayOfString[7].length() > 17) {
                            yxq = arrayOfString[7].substring(0, 17);
                        } else {
                            yxq = arrayOfString[7];
                        }
                    }
                    displayPerson.setYxq(yxq);
                    displayPerson.setZp(Cvr100bUtil.getInstance().getBase64PicString());

                    Cvr100bUtil.getInstance().clearDecodeInfo();
                    etSfzh.setText(displayPerson.getSfzh());
                    boolean online = SPHelper.getHelper(readCard.this).isOnline();
                    showProgressDialog("核查中");
                    tvEmpty.setText("查询中");
                    tvEmpty.setTextColor(getResources().getColor(R.color.tv_gray));
                    if (online) {
                        Display(displayPerson, true);
                        getcheck(displayPerson);
                    } else {
                        LogUtils.d("yxq==" + arrayOfString[7]);
                        Display(displayPerson, true);
                        bidui(displayPerson);
                    }
                    break;
                case 3:
                    ToastUtil.showToast(readCard.this, "自己关了");
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private TextView tvEmpty;
    private TextView t0;
    private EditText etSfzh;
    private Button sghc;
    private Button button9;
    List<KeyPerson> keyPersons;
    private CheckPersonDao checkPersonDao;
    private CheckPersonJsDao checkPersonJsDao;
    private ListView jsListView;
    JsListViewAdapter jsListAdapter;

    TitleBar titlebar;
    Button onlinecheck;
    TextView tv_tongche;
    CheckBox ckb;
    RelativeLayout ll_listbg;

    String groupid;
    CheckInfo info;
    int checkStyle;
    String checkMode = "hand";
    BasePerson displayPerson;

    private BaseDicValueDao baseDicValueDao;
    private ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);

    private static final String TAG = "readCard";
    private static final int REQUEST_PZ_CODE = 1;
    String LICENSE_OCR_NAME = "66ea648a52d79cea9861f525012c99d2.lic";

    @Override
    public void onClick(View v) {
        if (R.id.button8 == v.getId()) {
//            startActivityForResult(new Intent(this, ReadCardActivity.class), 1);//背甲读卡
            sghc.setText("正在读卡");
            RunReadCardThread();
        } else if (R.id.button9 == v.getId()) {
            checkMode = "ocr";
            if (!PermissionUtils.checkPermission(this, Constants.REQUEST_PERMISSION_EXTERNAL_STORAGE, 0, "应用需要调用相机权限")) {
                return;
            }
            ActivityUtils.startCheck(readCard.this, Config.OCR_TYPE_1, LICENSE_OCR_NAME, REQUEST_PZ_CODE, null);
        }
        switch (v.getId()) {
            //手动核查
            case R.id.button_onlinecheck:
                String sfzh = etSfzh.getText().toString().trim();
                if (com.acc.common.util.StringUtil.isNotNull(sfzh)) {
                    sfzh = sfzh.toUpperCase();
                    etSfzh.setText(sfzh);
                }
                if (!VerifyCheck.isIDCardVerify(sfzh)) {
                    ToastUtil.showToast(this, "身份证号有误，请重新输入");
                    return;
                }
                //清除数据
                clearInfo();

                boolean online = SPHelper.getHelper(readCard.this).isOnline();
                displayPerson = new BasePerson();
                displayPerson.setXm("");
                displayPerson.setSfzh(sfzh);
                checkMode = "hand";
                showProgressDialog("核查中");
                tvEmpty.setText("查询中");
                tvEmpty.setTextColor(getResources().getColor(R.color.tv_gray));
                if (online) {
                    Display(displayPerson, true);
                    getcheck(null);
                } else {
                    Display(displayPerson, false);
                    bidui(displayPerson);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 清楚数据
     */
    private void clearInfo() {
        //清空警示信息
        if (null != jsListAdapter) {
            jsListAdapter.clearData();
        }
        //清空基本信息
        TextView v = findViewById(R.id.textView1);
        v.setText("");
        v = findViewById(R.id.textView2);
        v.setText("");
        v = findViewById(R.id.textView3);
        v.setText("");
        v = findViewById(R.id.textView4);
        v.setText("");
        v = findViewById(R.id.textView5);
        v.setText("");
        v = findViewById(R.id.textView6);
        v.setText("");
        //清空采集数据
        mCheckPerson = null;
        if (mIsHighTw.get()) {
            mIsDj = false;
            mIsHighTw.set(false);
        }
        mPhone.set("");
        //照片
        ImageView mImageView = findViewById(R.id.imageView1);
        mImageView.setImageBitmap(null);
    }

    /**
     * 在线查询
     */
    private void getcheck(BasePerson person) {
        final String sfzh = etSfzh.getText().toString().trim();
        String url = URL.host + URL.query;
        StaffUserVO userVO = SPHelper.getHelper(readCard.this).getInfo();
        final OnlineCheckRequest request = new OnlineCheckRequest();
        request.setUsername("anrong");
        request.setPassword("anrong");
        request.setDeviceId(NetUtil.getIMEI(readCard.this));
        request.setSfzh(sfzh);
        request.setXm(null == person ? "" : person.getXm());
        request.setPoliceIdcard(userVO.getStaffCode());
        request.setPoliceName(userVO.getStaffName());
        request.setDeptId(userVO.getDeptId());
        request.setLatitude(MyApplication.getlatitude());
        request.setLongitude(MyApplication.getLongitude());
        request.setCheckIdcardMode(checkMode);
        request.setCheckNetworkStatus("online");
        request.setCheckObject("person");
        request.setGroupId(ckb.isChecked() ? groupid : "");
        request.setCheckAddress(SPHelper.getHelper(readCard.this).getCheckAddr());
        request.setCheckTaskType(SPHelper.getHelper(readCard.this).getCheckType());

        ServiceGenerator.createService(BimoClient.class, URL.host)
                .checkPerson(JSON.toJSONString(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CheckDetailResponse>() {
                    @Override
                    public void onNext(CheckDetailResponse responseBody) {
                        dismissProgressDialog();
                        if (null != responseBody) {
                            processPersonInfo(responseBody, person, sfzh);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(readCard.this, "连接失败");
                        dismissProgressDialog();
                        tvEmpty.setText("无网络连接");
                        tvEmpty.setTextColor(Color.RED);
                        mmHandler.sendEmptyMessage(3);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        MyApplication.mMyOkhttp.get()
//                .url(url)
//                .addParam("jsonObj", JSON.toJSONString(request))
//                .tag(this)
//                .enqueue(new RawResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        ToastUtil.showToast(readCard.this, "连接失败");
//                        dismissProgressDialog();
//                        tvEmpty.setText("无网络连接");
//                        tvEmpty.setTextColor(Color.RED);
//                        mmHandler.sendEmptyMessage(3);
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, String response) {
//                        dismissProgressDialog();
//                        mmHandler.sendEmptyMessage(3);
//                        CheckDetailResponse result = JSON.parseObject(response, CheckDetailResponse.class);
//                        processPersonInfo(result, person, sfzh);
//
//                    }
//                });
    }

    private void processPersonInfo(CheckDetailResponse result, BasePerson person, String sfzh) {
        BasePerson ryxx;
        if (!"000".equals(result.getCode())) {
            tvEmpty.setText("查询完毕");
            tvEmpty.setTextColor(getResources().getColor(R.color.tv_gray));
            if (null == person) {
                return;
            } else {
                ryxx = person;
            }
        } else {
            ryxx = result.getData().getRyxx();
        }
        confirm();
        Display(ryxx, false);

        if (keyPersons == null) {
            keyPersons = new ArrayList<>();
        }
        keyPersons.clear();
        List<com.response.KeyPerson> zbryList = result.getData().getZbryList();
        if (zbryList != null && zbryList.size() > 0) {
            for (com.response.KeyPerson e : zbryList) {
                KeyPerson keyPerson = BeanUtils.copyBeanToBean(e, KeyPerson.class);
                keyPerson.setPverid(e.getSjly());
                keyPersons.add(keyPerson);
                String pverid = keyPerson.getPverid();
            }
        }

        CheckPersonInfo cp = new CheckPersonInfo();
        cp.sfhm = sfzh;
        //查询河北库
        searchHB(keyPersons, cp.sfhm);
        //查廊坊布控数据
        searchLangFang(keyPersons, cp.sfhm);

        /*******把接口的数据加入本地库，一起上传********/
        createCheckHistory("online");
        mCheckPerson = addCheckPersonDao(result.getData().getRyxx());
        boolean add = checkPersonDao.add(mCheckPerson);

        if (keyPersons != null && keyPersons.size() > 0) {
            /*******把接口的数据加入本地库，一起上传********/
            for (KeyPerson k : keyPersons) {
                if (!"010".equals(k.getPverid()) && !"020".equals(k.getPverid()) && !"099".equals(k.getPverid()) && !AccTool.nullOrEmpty(k.getPverid())) {
                    CheckPersonJs checkPersonJs = BeanUtils.copyBeanToBean(k, CheckPersonJs.class);
                    checkPersonJs.setId(UUIDUtils.uuid32());
                    checkPersonJs.setCheckId(info.getId());
                    checkPersonJs.setCheckTime(DateUtil.getFullDateToString(info.getCheckTime()));
                    checkPersonJs.setSjly(k.getPverid());
                    checkPersonJsDao.add(checkPersonJs);
                }
            }
            mmHandler.sendEmptyMessage(1);
            jsListAdapter = new JsListViewAdapter(readCard.this, keyPersons);
            jsListView.setAdapter(jsListAdapter);
            ll_listbg.setBackgroundResource(R.drawable.orange_stroke);
        } else {
            mmHandler.sendEmptyMessage(2);
            if (jsListAdapter != null) {
                jsListAdapter.clearData();
            }
            tvEmpty.setText("无异常");
            tvEmpty.setTextColor(Color.GREEN);
            ll_listbg.setBackgroundResource(R.drawable.lv_empty);
        }
    }

    private void confirm() {
        MyApplication.mMyOkhttp.get()
                .url(URL.host + URL.Confirm)
                .addParam("id", NetUtil.getIMEI(readCard.this))
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

    public void InitView() {

        progressDialog = new CustomerDialog(readCard.this, R.style.MyDialog);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);

        t0 = findViewById(R.id.textView0);
        t0.setText("");
        TextView v = findViewById(R.id.textView1);
        v.setText("");
        v = findViewById(R.id.textView2);
        v.setText("");
        v = findViewById(R.id.textView3);
        v.setText("");
        v = findViewById(R.id.textView4);
        v.setText("");
        v = findViewById(R.id.textView5);
        v.setText("");
        v = findViewById(R.id.textView6);
        v.setText("");

        onlinecheck = findViewById(R.id.button_onlinecheck);
        onlinecheck.setOnClickListener(this);
        tv_tongche = findViewById(R.id.tv_tongche);
        ckb = findViewById(R.id.ckb);
        ll_listbg = findViewById(R.id.ll_listbg);

        ckb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    if (info != null) {
//                        groupid = info.getId();
//                    }
                } else {
                    groupid = null;
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_check);
        viewDataBinding.setVariable(com.acc.sanheapp.BR.readCardVM, this);
        InitView();
        baseDicValueDao = new BaseDicValueDao(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getBoolean("fromCar", false)) {
                groupid = bundle.getString("infoid", "");
//                createCheckHistory();
                CheckCar car = (CheckCar) bundle.getSerializable("car");
                ckb.setChecked(true);
                ckb.setText("是否同车");
                if (car.isNeedTransform()) {
                    tv_tongche.setText(nullToString(car.getCphm()) + " " + nullToString(baseDicValueDao.queryLableBydictTypeAndValue("car_type", car.getHpzl(), "")) + " " + nullToString(baseDicValueDao.queryLableBydictTypeAndValue("car_color", car.getClys(), "")));
                } else {
                    tv_tongche.setText(nullToString(car.getCphm()) + " " + nullToString(car.getHpzl()) + " " + nullToString(car.getClys()));
                }

            }
        }
        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(this);
        etSfzh = findViewById(R.id.sfzh);
        etSfzh.clearFocus();
        etSfzh.setTransformationMethod(new A2bigA());
        sghc = findViewById(R.id.button8);
        sghc.setOnClickListener(this);
        radioGroup0 = findViewById(R.id.radioGroup0);
        radioGroup0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (null != mCheckPerson && null != mCheckPerson.getId() && mIsDj) {
                    mCheckPerson.setLxdh(mPhone.get());
                    mCheckPerson.setSffs(mIsHighTw.get() ? "1" : "0");
                    boolean update = checkPersonDao.update(mCheckPerson);
                    if (update) {
                        Toast.makeText(readCard.this, "信息增加成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(readCard.this, "增加失败，本地数据已上传", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mIsDj = true;
                }
            }
        });

        checkPersonDao = new CheckPersonDao(this);
        checkPersonJsDao = new CheckPersonJsDao(this);
        titlebar = findViewById(R.id.titlebar);
        TitleBarUtils.setTile(this, titlebar, "人员核查", true);
        titlebar.setBack(true);
        if (AppConfig.USE_OFFLINE_CHECK) {
            titlebar.setTitleRight("模式切换");
            titlebar.setOnRightTextclickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean online = SPHelper.getHelper(readCard.this).isOnline();
                    if (online) {
                        ToastUtil.showToast(readCard.this, "离线模式");
                    } else {
                        ToastUtil.showToast(readCard.this, "在线模式");
                    }
                    SPHelper.getHelper(readCard.this).setOnline(!online);
                    TitleBarUtils.setTile(readCard.this, titlebar, "人员核查", true);
                }
            });
        }
        //监听手机输入变化
        mPhone.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String s = mPhone.get();
                if (s != null && s.length() == 11 && null != mCheckPerson && null != mCheckPerson.getId()) {
                    mCheckPerson.setLxdh(mPhone.get());
                    mCheckPerson.setSffs(mIsHighTw.get() ? "1" : "0");
                    boolean update = checkPersonDao.update(mCheckPerson);
                    if (update) {
                        Toast.makeText(readCard.this, "信息增加成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(readCard.this, "增加失败，本地数据已上传", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        jsListView = findViewById(R.id.js_list);
        jsListView.setEmptyView(findViewById(R.id.tv_empty));
        jsListView = findViewById(R.id.js_list);
        tvEmpty = findViewById(R.id.tv_empty);
        jsListView.setEmptyView(tvEmpty);
        jsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                showJsDetail(position);
            }
        });

        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(this);

        etSfzh = findViewById(R.id.sfzh);
        etSfzh.clearFocus();
        sghc = findViewById(R.id.button8);
        sghc.setOnClickListener(this);


        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Button b1 = findViewById(R.id.button1);
        b1.setOnClickListener(this);
        Button b2 = findViewById(R.id.button2);
        b2.setOnClickListener(this);
        Button b3 = findViewById(R.id.button3);
        b3.setOnClickListener(this);
        Button b4 = findViewById(R.id.button4);
        b4.setOnClickListener(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ic = new IC2ReadCard();
        ic.openPower();
    }

    private String nullToString(String s) {
        if (com.acc.common.util.StringUtil.isNullOrEmpty(s)) {
            return "";
        }
        if (com.acc.common.util.StringUtil.isNullOrEmpty(s.trim())) {
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


    private void RunReadCardThread() {
        checkMode = "readcard";
        if ((this.mReadCardThread != null) && (this.mReadCardThread.isAlive())) {
            return;
        }
        try {
            Thread.sleep(100L);
            mReadCardThread = new ReadCardThread();
            this.mReadCardThread.start();
            return;
        } catch (InterruptedException localInterruptedException) {
            localInterruptedException.printStackTrace();
        }
    }

    public class ReadCardThread extends Thread {
        public boolean open = true;

        public ReadCardThread() {
        }

        public void run() {
            try {
                while (open) {
                    saveAndCheckPerson();
                    Thread.sleep(SPHelper.getHelper(readCard.this).getCheckInterval());
                }
            } catch (InterruptedException localInterruptedException) {
            }
        }
    }

    private void saveAndCheckPerson() {
        ic.read();
        if (!TextUtils.isEmpty(Cvr100bUtil.getInstance().getSfzh())) {
            this.mHandler.sendEmptyMessage(2);
            if (checkStyle == 1) {
                mReadCardThread.open = false;
            }
        }
    }


    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (this.mReadCardThread != null) {
            this.mReadCardThread.interrupt();
        }
        if (this.ic != null) {
            this.ic.closePower();
        }
    }

    private void Display(BasePerson person, boolean needTransform) {
        try {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView v = findViewById(R.id.textView1);
        String xm = person.getXm();
        if (null != xm && xm.length() != 0) {
            v.setText(nullToString("" + person.getXm()));
        }


        TextView xb = findViewById(R.id.textView2);
        String xbText = person.getXb();
        if (null != xbText && xbText.length() != 0) {
            xb.setText(nullToString(person.getXb()));
            if (needTransform && StringUtil.isNotBlank(person.getXb())) {
                switch (person.getXb()) {
                    case "1":
                        xb.setText("男");
                        break;
                    default:
                        xb.setText("女");
                        break;
                }
            }
        }


        TextView mz = findViewById(R.id.textView3);
        String mzText = person.getMz();
        if (mzText != null && mzText.length() != 0) {
            mz.setText(nullToString(person.getMz()));
            if (needTransform && StringUtil.isNotBlank(person.getMz())) {
                mz.setText(nullToString(Cvr100bUtil.getMz(person.getMz())));
            }

        }

        TextView csrq = findViewById(R.id.textView4);
        String csrqText = person.getCsrq();

        if (csrqText != null && csrqText.length() != 0) {
            csrq.setText(nullToString(person.getCsrq()));
        }


        TextView hjdz = findViewById(R.id.textView5);
        String hjdzText = person.getHjdz();
        if (hjdzText != null && hjdzText.length() != 0) {
            hjdz.setText(nullToString(person.getHjdz()));
        }


        TextView sfzh = findViewById(R.id.textView6);
        String sfzhText = person.getSfzh();
        if (sfzhText != null && sfzhText.length() != 0) {
            sfzh.setText(nullToString(person.getSfzh()));
        }

        tv_tongche.setText(nullToString(person.getXm()) + " " + nullToString(person.getSfzh()));
        String zp = person.getZp();
        if (null != zp && zp.length() != 0) {
            ImageView mImageView = findViewById(R.id.imageView1);
            if (zp != null) {
                byte[] ZpData = Base64.decode(zp, Base64.DEFAULT);
                if (ZpData == null) {
                    return;
                }
                Bitmap bm = BitmapFactory.decodeByteArray(ZpData, 0, ZpData.length);
                mImageView.setImageBitmap(bm);
            } else {
                mImageView.setImageBitmap(null);
            }
        }
    }

    private final Handler mmHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                sghc.setEnabled(false);
                onlinecheck.setEnabled(false);
                button9.setEnabled(false);
                t0.setText("正在查询");
            } else if (msg.what == 2) {
                t0.setText("正常人员");
            } else if (msg.what == 1) {
                //弹出提示框
                showDialog();
                t0.setText("非正常人员" + "(" + keyPersons.size() + ")");
            } else if (msg.what == 3) {
                t0.setText("查询结果");
            }
            if (msg.what != 0) {
                sghc.setEnabled(true);
                onlinecheck.setEnabled(true);
                button9.setEnabled(true);
                if (checkStyle == 1) {
                    sghc.setText("手动读卡核查");
                }
            }
        }
    };


    /**
     * 查询被核查人的信息
     */
    private void bidui(BasePerson person) {
        mmHandler.sendEmptyMessage(0);
        try {
            //查询公安部库
            CheckPersonInfo cp = new CheckPersonInfo();
            cp.xm = person.getXm();
            cp.sfhm = person.getSfzh();
            if (keyPersons == null) {
                keyPersons = new ArrayList<>();
            }
            keyPersons.clear();
            //查询河北库
            searchHB(keyPersons, cp.sfhm);
            //查廊坊布控数据
            searchLangFang(keyPersons, cp.sfhm);
            //查询三道防线数据
            searchSanDaoFangXian(keyPersons, person);

            createCheckHistory("offline");
            mCheckPerson = addCheckPersonDao(person);
            boolean add = checkPersonDao.add(mCheckPerson);
            if (keyPersons != null && keyPersons.size() > 0) {
                mmHandler.sendEmptyMessage(1);
                for (KeyPerson k : keyPersons) {
                    CheckPersonJs checkPersonJs = BeanUtils.copyBeanToBean(k, CheckPersonJs.class);
                    checkPersonJs.setId(UUIDUtils.uuid32());
                    checkPersonJs.setCheckId(info.getId());
                    checkPersonJs.setCheckTime(DateUtil.getFullDateToString(info.getCheckTime()));
                    checkPersonJs.setSjly(k.getPverid());
                    checkPersonJsDao.add(checkPersonJs);
                }
            } else {
                mmHandler.sendEmptyMessage(2);
            }
            //警示信息
            if (keyPersons != null && keyPersons.size() > 0) {
                jsListAdapter = new JsListViewAdapter(readCard.this, keyPersons);
                jsListView.setAdapter(jsListAdapter);
                ll_listbg.setBackgroundResource(R.drawable.orange_stroke);
                tvEmpty.setText("查询完毕");
                tvEmpty.setTextColor(getResources().getColor(R.color.tv_gray));
            } else {
                if (jsListAdapter != null) {
                    jsListAdapter.clearData();
                }
                tvEmpty.setText("无异常");
                tvEmpty.setTextColor(Color.GREEN);
                ll_listbg.setBackgroundResource(R.drawable.lv_empty);
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvEmpty.setText("查询完毕");
            tvEmpty.setTextColor(getResources().getColor(R.color.tv_gray));
            mmHandler.sendEmptyMessage(3);
        }
        dismissProgressDialog();
    }

    private CheckPerson addCheckPersonDao(BasePerson person) {
        CheckPerson bean = new CheckPerson();
        bean.setXm(person.getXm());
        bean.setXb(person.getXb());
        bean.setMz(person.getMz());
        bean.setSffs(mIsHighTw.get() ? "1" : "0");
        bean.setLxdh(mPhone.get());
        bean.setCsrq(person.getCsrq());
        bean.setSfzh(person.getSfzh());
        bean.setHjdz(person.getHjdz());
        bean.setFzpcs(person.getFzpcs());
        bean.setYxq(person.getYxq());
        bean.setCheckTime(DateUtil.getDateToString(new Date(), DateUtil.FULLFORMAT));
        bean.setRylb(getRylb());
        bean.setCheckId(info.getId());
        bean.setId(UUIDUtils.uuid32());
        bean.setZp(person.getZp());
        bean.setSjly(person.getSjly());
        return bean;
    }

    private void createCheckHistory(String net) {
        StaffUserVO userVO = SPHelper.getHelper(readCard.this).getInfo();
        info = new CheckInfo();
        info.setId(UUIDUtils.uuid32());
        info.setCheckIdcardMode(checkMode);
        info.setCheckNetworkStatus(net);
        info.setCheckObject("person");
        info.setCheckTime(new Date());
        info.setDeptId(userVO.getDeptId());
        info.setDeviceId(NetUtil.getIMEI(readCard.this));
        if (groupid == null) {
            groupid = info.getId();
        }
        info.setGroupId(ckb.isChecked() ? groupid : "");
        info.setLatitude(MyApplication.getlatitude());
        info.setLongitude(MyApplication.getLongitude());
        info.setPoliceIdcard(userVO.getStaffCode());
        info.setPoliceName(userVO.getStaffName());
        info.setCreateBy(userVO.getLoginName());
        info.setCreateDate(new Date());
        info.setUpdateBy(userVO.getLoginName());
        info.setUpdateDate(new Date());
        info.setCheckAddress(SPHelper.getHelper(readCard.this).getCheckAddr());
        info.setCheckTaskType(SPHelper.getHelper(readCard.this).getCheckType());
        new CheckInfoDao(this).add(info);
    }

    /**
     * 查询河北库
     *
     * @param keyPersons 加入的集合
     * @param sfzh       身份证号
     */
    private void searchHB(List<KeyPerson> keyPersons, String sfzh) {
        try {
            List<Person> personList = new PersonDao(this).loadAll(sfzh);
            for (Person person : personList) {
                KeyPerson keyPerson = new KeyPerson();
                keyPerson.setAqms(person.getAqms());
                keyPerson.setBklxfs(person.getBklxfs());
                keyPerson.setBklxr(person.getBklxr());
                keyPerson.setClfs(person.getClfs());
                keyPerson.setPfirstid("");
                keyPerson.setPverid("local");
                keyPerson.setSfzh(sfzh);
                keyPerson.setRylb(person.getRylb());
                keyPerson.setRwid("");
                keyPerson.setRwmc(person.getRwmc());
                keyPersons.add(keyPerson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询廊坊布控数据
     *
     * @param keyPersons 加入的集合
     * @param sfzh       身份证号
     */
    private void searchLangFang(List<KeyPerson> keyPersons, String sfzh) {
        //查廊坊数据库
        TPersonControlDao tPersonControlDao = new TPersonControlDao(this);
        List<TPersonControl> personJsList = tPersonControlDao.queryZdrList(sfzh);
        if (personJsList != null && personJsList.size() > 0) {
            for (TPersonControl langFangPersonJs : personJsList) {
                KeyPerson keyPerson = new KeyPerson();
                keyPerson.setAqms(langFangPersonJs.getBkms());
                keyPerson.setBklxfs(langFangPersonJs.getBklxdh());
                keyPerson.setBklxr(langFangPersonJs.getBklxr());
                keyPerson.setClfs(langFangPersonJs.getCzfs());
                keyPerson.setPfirstid("");
                keyPerson.setPverid("langfangbk");
                keyPerson.setRwmc("廊坊本地布控");
                keyPerson.setRylb(langFangPersonJs.getBkrlx());
                keyPerson.setSfzh(sfzh);
                // keyPerson.setSjly("langfangbk");
                keyPersons.add(keyPerson);
            }
        }
    }

    /**
     * 核查三道防线数据
     *
     * @param keyPersons 原来重点人集合
     * @param p          人员对象
     */
    private void searchSanDaoFangXian(List<KeyPerson> keyPersons, BasePerson p) {
        TaskCheckResult taskCheckResult = SanDaoFangXianUtils.checkPerson(this, p, false, Constants.SDFX_USERNAME, Constants.SDFX_PASSWORD);
        //请求成功
        if ("000".equals(taskCheckResult.getCode())) {
            ControlPerson person = taskCheckResult.getPerson();
            if (null != person) {
                KeyPerson keyPerson = new KeyPerson();
                keyPerson.setAqms(person.getRymx());
                keyPerson.setBklxfs(person.getGkrlxfs());
                keyPerson.setBklxr(person.getGkr());
                keyPerson.setClfs(person.getCzcs());
                keyPerson.setPfirstid("");
                keyPerson.setPverid("st-sandaofangxian");
                keyPerson.setRwmc(taskCheckResult.getRwmc());
                keyPerson.setRylb(person.getRylb());
                keyPerson.setSfzh(p.getSfzh());
                keyPersons.add(keyPerson);
            }
        } else {
            ToastUtil.showToastLong(this, taskCheckResult.getMsg());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStyle = SPHelper.getHelper(this).getCheckStyle();
        if (checkStyle == 2) {
            sghc.setText("自动读卡核查");
            sghc.setClickable(false);
            mmHandler.post(new Runnable() {
                @Override
                public void run() {
                    RunReadCardThread();
                }
            });
        } else {
            sghc.setText("手动读卡核查");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PZ_CODE) {
            if (data != null) {
                String data1 = data.getStringExtra(Config.RQUERY_OCR_DATA_KEY);
                ReturnResultBean1 resultBean = JSON.parseObject(data1, ReturnResultBean1.class);
                LogUtils.d("code==" + resultBean.getState());
                if (resultBean.getState() == 200) {
                    PersonOcrBean personOcrBean = (PersonOcrBean) resultBean.getMessage();
                    etSfzh.setText(personOcrBean.getCard());
                    displayPerson = new BasePerson();

                    displayPerson.setXm(personOcrBean.getName());
                    displayPerson.setXb(personOcrBean.getSex());
                    displayPerson.setMz(personOcrBean.getMz());
                    displayPerson.setCsrq(personOcrBean.getDateOfBirth());
                    displayPerson.setHjdz(personOcrBean.getAddress());
                    displayPerson.setSfzh(personOcrBean.getCard());
                    displayPerson.setFzpcs(personOcrBean.getJg());
                    displayPerson.setYxq(personOcrBean.getYxq());
                }
                boolean online = SPHelper.getHelper(readCard.this).isOnline();
                //清除数据
                clearInfo();

                showProgressDialog("核查中");
                tvEmpty.setText("查询中");
                tvEmpty.setTextColor(getResources().getColor(R.color.tv_gray));
                if (online) {
                    Display(displayPerson, false);
                    getcheck(displayPerson);
                } else {
                    Display(displayPerson, false);
                    bidui(displayPerson);
                }
            }
        }
        if (requestCode == 200) {
            TitleBarUtils.setTile(this, titlebar, "人员核查", true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRylb() {
        if (keyPersons != null && keyPersons.size() > 0) {
            String rylb = "";
            for (int i = 0; i < keyPersons.size(); i++) {
                rylb += keyPersons.get(i).getRylb() + "  ";
            }
            return rylb;
        }
        return "";
    }

    private void showDialog() {
        if (keyPersons != null && keyPersons.size() > 0) {
            String msg = "";
            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < keyPersons.size(); i++) {
                msg += keyPersons.get(i).getRylb() + "<br>";
                stringList.add(keyPersons.get(i).getRylb());
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(readCard.this);
            View view = View.inflate(readCard.this, R.layout.layout_warndialog, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            ListView lv = view.findViewById(R.id.listview);
            WarnAdapter adapter = new WarnAdapter(stringList, readCard.this);
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

    private void showJsDetail(int position) {
        if (keyPersons != null && keyPersons.size() > 0) {
            String msg = "";
            msg += nullToString(keyPersons.get(position).getRylb()) + "<br>"
                    + "联系人：" + nullToString(keyPersons.get(position).getBklxr()) + "<br>"
                    + "联系人方式：" + nullToString(keyPersons.get(position).getBklxfs()) + "<br>"
                    + "案情描述：" + nullToString(keyPersons.get(position).getAqms()) + "<br>"
                    + "处置方式：" + nullToString(keyPersons.get(position).getClfs());
            new AlertDialog.Builder(readCard.this).setTitle("警示信息")//设置对话框标题
                    .setMessage(Html.fromHtml(msg))//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).show();//在按键响应事件中显示此对话框
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
}
