package com.qhd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.acc.sanheapp.R;
import com.cnnk.IdentifyCard.Cvr100bUtil;
import com.cnnk.IdentifyCard.IC2ReadCard;
import com.cnnk.IdentifyCard.RequestParameter;
import com.data.IdCardDn;

/**
 * 身份证背甲读卡
 */
public class ReadCardActivity extends Activity {
    private IC2ReadCard ic;
    private ReadCardThread mReadCardThread;
    private boolean startRead = true;
    private int number = 0;//
    private Handler mHandler = new Handler() {
        private IdCardDn personBean;

        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            switch (paramMessage.what) {
                case 1:

                    Toast.makeText(ReadCardActivity.this, RequestParameter.getRequestParameter().getAttr("cvrInfo"), Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 2:
                    String[] arrayOfString = Cvr100bUtil.getInstance().getDecodeInfo();
                    personBean = new IdCardDn();
                    personBean.setName(arrayOfString[0]);
                    personBean.setSex(arrayOfString[1]);
                    personBean.setMz(arrayOfString[2]);
                    personBean.setAddress(arrayOfString[4]);
                    personBean.setIdcard(arrayOfString[5]);
                    personBean.setPhoto(Cvr100bUtil.getInstance().getBase64PicString());

                    Cvr100bUtil.getInstance().clearDecodeInfo();
                    break;
                case 3:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);
        ic = new IC2ReadCard();
        ic.openPower();
        RunReadCardThread();
    }

    private void RunReadCardThread() {
        if ((this.mReadCardThread != null) && (this.mReadCardThread.isAlive()))
            this.mReadCardThread.mTerminate = true;
        try {
            Thread.sleep(2000L);
            mReadCardThread = new ReadCardThread();
            this.mReadCardThread.start();
            return;
        } catch (InterruptedException localInterruptedException) {
            localInterruptedException.printStackTrace();
        }
    }


    public class ReadCardThread extends Thread {
        public boolean mTerminate = false;

        public ReadCardThread() {
        }

        public void run() {
            try {
                while (!this.mTerminate) {
                    if (startRead)
                        saveAndCheckPerson();
                    Thread.sleep(100L);
                }
            } catch (InterruptedException localInterruptedException) {
            }
        }
    }

    private void saveAndCheckPerson() {
        ic.read();

        if (!TextUtils.isEmpty(Cvr100bUtil.getInstance().getSfzh())) {
            this.startRead = false;
            this.mHandler.sendEmptyMessage(2);
        } else {
            ++number;
            if (number > 3) {
                mHandler.sendEmptyMessage(3);
            }
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (this.mReadCardThread != null)
            this.mReadCardThread.interrupt();
        if (this.ic != null)
            this.ic.closePower();
    }
}
