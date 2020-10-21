package com.qhd;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.acc.sanheapp.R;
import com.anrong.sdk.CheckCarInfo;
import com.anrong.sdk.CheckPersonInfo;
import com.anrong.sdk.SDKService;
import com.anrong.sdk.callback.InitCallBack;
import com.anrong.sdk.callback.ZipCallBack;
import com.arong.swing.db.entity.JsonDataResult;
import com.arong.swing.db.entity.KeyCar;
import com.arong.swing.db.entity.KeyPerson;
import com.arong.swing.db.entity.StaffUser;
import com.arong.swing.db.entity.StaffUserVO;
import com.arong.swing.util.UUIDUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class MainActivity extends Activity implements OnClickListener{
private final static String TAG = "MainActivity";

	private Button initBtn;

	private Button loginBtn;

	private Button currBtn;

	private Button personBtn;

	private Button carBtn;

	private List<StaffUserVO> list;

	public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
            case 1:
            	init();
                break;
            default:
                break;
            }
            super.handleMessage(msg);
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initBtn = (Button) findViewById(R.id.getAll);
		initBtn.setOnClickListener(this);

		loginBtn = (Button) findViewById(R.id.login);
		loginBtn.setOnClickListener(this);

		currBtn = (Button) findViewById(R.id.getCurr);
		currBtn.setOnClickListener(this);

		personBtn = (Button) findViewById(R.id.checkPerson);
		personBtn.setOnClickListener(this);

		carBtn = (Button) findViewById(R.id.checkCar);
		carBtn.setOnClickListener(this);


		 Thread thread=new Thread(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                Message message=new Message();
	                message.what=1;
	                mHandler.sendMessage(message);
	            }
	        });
	        thread.start();


	}


	private void init(){
		String appRootPath = Environment.getExternalStorageDirectory().toString();
		//sd����Ŀ¼
		SDKService.init(getApplication(), appRootPath, "357952007458566", "anrong", new InitCallBack() {

			@Override
			public void onSuccess(int arg0) {
				Log.i(TAG, "onSuccess");
			}

			@Override
			public void onNeedExplode(int arg0) {
				Log.i(TAG, "onNeedExplode");
				SDKService.explode(MainActivity.this, new ZipCallBack() {

					@Override
					public void onSuccess() {
						System.out.println("1");
					}

					@Override
					public void onReckonSizeFinished(long arg0) {
						System.out.println("2");
					}

					@Override
					public void onProgress(int arg0, long arg1, long arg2, String arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onPrepare() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
			}

			@Override
			public void onError(int arg0, String arg1) {
				Log.i(TAG, "onError");
			}
		});

	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.getAll:
			try {
				list = SDKService.getStaffUsrs();
				Toast.makeText(getApplicationContext(), list.toString(), Toast.LENGTH_SHORT).show();
				System.out.println(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.login:
			try {
				StaffUserVO staffUserVO = list.get(4);
				JsonDataResult<String> result= SDKService.login(staffUserVO, "111111");
				Toast.makeText(getApplicationContext(), result.getCode(), Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.getCurr:
			try {
				StaffUser staffUser = SDKService.getCurStaffUser();
				Toast.makeText(getApplicationContext(), staffUser.toString(), Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.checkPerson:
			try {
				CheckPersonInfo cp = new CheckPersonInfo();
				cp.sfhm = "230221198801101613";
				cp.xm="���ӱ�";
				//"{\"sfhm\": \"230221198801101613\", \"xm\": \"���ӱ�\"}"
				List<KeyPerson> keyPerson = SDKService.checkPerson(UUIDUtils.uuid32(), UUIDUtils.uuid32(),cp );
				Toast.makeText(getApplicationContext(), keyPerson.toString(), Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.checkCar:
			try {
				CheckCarInfo cc = new CheckCarInfo();
				cc.cphm ="ԥJ66166";
				cc.hpzl="02";
				List<KeyCar> keyCar = SDKService.checkCar(UUIDUtils.uuid32(), UUIDUtils.uuid32(), cc);
				Toast.makeText(getApplicationContext(), keyCar.toString(), Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
}