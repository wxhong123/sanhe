package com.cnnk.IdentifyCard;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.acc.common.myokhttp.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;


@SuppressLint("NewApi")
public class Cvr100bUtil {

    private static final Cvr100bUtil instance = new Cvr100bUtil();

    public static Cvr100bUtil getInstance() {
        return instance;
    }

    private Cvr100bUtil() {
    }

    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothServerSocket mBThServer = null;
    BluetoothSocket mBTHSocket = null;
    InputStream mmInStream = null;
    OutputStream mmOutStream = null;
    int Readflage = -99;

    byte[] cmd_SAM = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96,
            0x69, 0x00, 0x03, 0x12, (byte) 0xFF, (byte) 0xEE};
    byte[] cmd_find = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96,
            0x69, 0x00, 0x03, 0x20, 0x01, 0x22};
    byte[] cmd_selt = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96,
            0x69, 0x00, 0x03, 0x20, 0x02, 0x21};
    byte[] cmd_read = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96,
            0x69, 0x00, 0x03, 0x30, 0x01, 0x32};
    byte[] cmd_sleep = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96,
            0x69, 0x00, 0x02, 0x00, 0x02};
    byte[] cmd_weak = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96,
            0x69, 0x00, 0x02, 0x01, 0x03};
    byte[] recData = new byte[1500];

    String DEVICE_NAME1 = "CVR-100B";
    String DEVICE_NAME2 = "IDCReader";
    String DEVICE_NAME3 = "COM2";
    String DEVICE_NAME4 = "BOLUTEK";

    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String[] decodeInfo = new String[11];

    String base64Pic = "";

    public String getBase64PicString() {
        return base64Pic;
    }

    public void setBase64Pic(String base64Pic) {
        this.base64Pic = base64Pic;
    }

    public String getSfzh() {
        return decodeInfo[5];
    }

    public String[] getDecodeInfo() {
        return decodeInfo;
    }

    public void clearDecodeInfo() {
        decodeInfo = new String[11];
    }

    public void conn() {
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (Iterator<BluetoothDevice> iterator = pairedDevices.iterator(); iterator.hasNext(); ) {
                BluetoothDevice device = (BluetoothDevice) iterator.next();
                if (DEVICE_NAME1.equals(device.getName())
                        || DEVICE_NAME2.equals(device.getName())
                        || DEVICE_NAME3.equals(device.getName())
                        || DEVICE_NAME4.equals(device.getName())) {
                    try {
                        myBluetoothAdapter.enable();
                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);// 使得蓝牙处于可发现模式，持续时间150s
                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 150);
                        mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        int sdk = Integer.parseInt(Build.VERSION.SDK);
                        if (sdk >= 10) {
                            mBTHSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                        } else {
                            mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        }
                        mBThServer = myBluetoothAdapter.listenUsingRfcommWithServiceRecord("myServerSocket", MY_UUID);
                        mBTHSocket.connect();
                        mmInStream = mBTHSocket.getInputStream();
                        mmOutStream = mBTHSocket.getOutputStream();
                    } catch (IOException e) {
                        Log.v("Cvr100bUtil", Constants.Cvr100.DEVICE_CONN_EXCEPTION);
                        RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.DEVICE_CONN_EXCEPTION);
                    }
                    if ((mmInStream != null) && (mmInStream != null)) {
                        Log.v("Cvr100bUtil", Constants.Cvr100.DEVICE_CONN_SUCCESS);
                        RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.DEVICE_CONN_SUCCESS);
                    } else {
                        Log.v("Cvr100bUtil", Constants.Cvr100.DEVICE_CONN_FAIL);
                        RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.DEVICE_CONN_FAIL);
                    }
                    break;
                }
            }
        } else {
            Log.v("Cvr100bUtil", Constants.Cvr100.BLUETOOTH_NOT_OPEN);
            RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.BLUETOOTH_NOT_OPEN);
        }
    }

    public void read() {
        int readcount = 15;
        try {
            while (readcount > 1) {
                ReadCard();
                readcount = readcount - 1;
                if (Readflage > 0) {
                    readcount = 0;
                    String readInfo = "姓名：" + decodeInfo[0] + "\n" + "性别：" + decodeInfo[1] + "\n" + "民族：" + decodeInfo[2] + "\n"
                            + "出生日期：" + decodeInfo[3] + "\n" + "地址：" + decodeInfo[4] + "\n" + "身份号码：" + decodeInfo[5] + "\n"
                            + "签发机关：" + decodeInfo[6] + "\n" + "有效期限：" + decodeInfo[7] + "-" + decodeInfo[8] + "\n"
                            + decodeInfo[9] + "\n";
                    RequestParameter.getRequestParameter().putAttr("cvrInfo", readInfo);
                    if (Readflage == 1) {
                        FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/wltlib/zp.bmp");
                        Bitmap bmp = BitmapFactory.decodeStream(fis);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bmp.compress(CompressFormat.JPEG, 100, out);
                        byte[] bytes = out.toByteArray();
                        base64Pic = Base64.encodeToString(bytes, Base64.DEFAULT);
                        Log.v("base64Pic", base64Pic);
                        //Bitmap bmp = BitmapFactory.decodeStream(fis);
                        //fis.close();
                        //image.setImageBitmap(bmp);
                    } else {
                        // ett.append("照片解码失败，请检查路径"+Environment.getExternalStorageDirectory()+"/wltlib/");
                        // image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.face));
                    }
                } else {
                    // image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.face));
                    if (Readflage == -2) {
                        Log.v("Cvr100bUtil", Constants.Cvr100.BLUETOOTH_CONN_EXCEPTION);

                        RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.BLUETOOTH_CONN_EXCEPTION);
                    }
                    if (Readflage == -3) {
                        Log.v("Cvr100bUtil", Constants.Cvr100.NOCARD_OR_READ);
                        RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.NOCARD_OR_READ);
                    }
                    if (Readflage == -4) {
                        Log.v("Cvr100bUtil", Constants.Cvr100.NOCARD_OR_READ);
                        RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.NOCARD_OR_READ);
                    }
                    if (Readflage == -5) {
                        Log.v("Cvr100bUtil", Constants.Cvr100.READCARD_FAIL);
                        RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.READCARD_FAIL);
                    }
                    if (Readflage == -99) {
                        Log.v("Cvr100bUtil", Constants.Cvr100.OPERATION_EXCEPTION);
                        RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.OPERATION_EXCEPTION);
                    }
                }
                Thread.sleep(100);
            }

        } catch (IOException e) {
            Log.v("Cvr100bUtil", Constants.Cvr100.READCARD_DATA_EXCEPTION);
            RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.READCARD_DATA_EXCEPTION);
            // image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.face));
        } catch (InterruptedException e) {
            Log.v("Cvr100bUtil", Constants.Cvr100.READCARD_DATA_EXCEPTION);
            RequestParameter.getRequestParameter().putAttr("cvrInfo", Constants.Cvr100.READCARD_DATA_EXCEPTION);
            // image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.face));
        }
    }

    public void sleep() {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                Log.v("Cvr100bUtil", "设备未正常连接！");
                RequestParameter.getRequestParameter().putAttr("cvrInfo", "设备未正常连接！");
                return;
            }
            mmOutStream.write(cmd_sleep);
            Log.v("Cvr100bUtil", "睡眠模式！");
            RequestParameter.getRequestParameter().putAttr("cvrInfo", "睡眠模式！");
            // image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.face));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void weak() {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                Log.v("Cvr100bUtil", "设备未正常连接！");
                RequestParameter.getRequestParameter().putAttr("cvrInfo", "设备未正常连接！");
                return;
            }
            mmOutStream.write(cmd_weak);
            Log.v("Cvr100bUtil", "唤醒模式！");
            RequestParameter.getRequestParameter().putAttr("cvrInfo", "唤醒模式！");
            // image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.face));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                return;
            }
            mmOutStream.close();
            mmInStream.close();
            mBTHSocket.close();
            mBThServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void ReadCard() {
        decodeInfo = new String[10];
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                Readflage = -2;// 连接异常
                return;
            }
            mmOutStream.write(cmd_find);
            Thread.sleep(200);
            int datalen = mmInStream.read(recData);
            if (recData[9] == -97) {
                mmOutStream.write(cmd_selt);
                Thread.sleep(200);
                datalen = mmInStream.read(recData);
                if (recData[9] == -112) {
                    mmOutStream.write(cmd_read);
                    Thread.sleep(1000);
                    byte[] tempData = new byte[1500];
                    if (mmInStream.available() > 0) {
                        datalen = mmInStream.read(tempData);
                    } else {
                        Thread.sleep(500);
                        if (mmInStream.available() > 0) {
                            datalen = mmInStream.read(tempData);
                        }
                    }
                    int flag = 0;
                    if (datalen < 1294) {
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                        Thread.sleep(1000);
                        if (mmInStream.available() > 0) {
                            datalen = mmInStream.read(tempData);
                        } else {
                            Thread.sleep(500);
                            if (mmInStream.available() > 0) {
                                datalen = mmInStream.read(tempData);
                            }
                        }
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }

                    } else {
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                    }
                    tempData = null;
                    if (flag == 1295) {
                        if (recData[9] == -112) {

                            byte[] dataBuf = new byte[256];
                            for (int i = 0; i < 256; i++) {
                                dataBuf[i] = recData[14 + i];
                            }
                            String TmpStr = new String(dataBuf, "UTF16-LE");
                            TmpStr = new String(TmpStr.getBytes("UTF-8"));
                            LogUtils.d("str==" + TmpStr);
                            decodeInfo[0] = TmpStr.substring(0, 15);
                            decodeInfo[1] = TmpStr.substring(15, 16);
                            decodeInfo[2] = TmpStr.substring(16, 18);
                            decodeInfo[3] = TmpStr.substring(18, 26);
                            decodeInfo[4] = TmpStr.substring(26, 61);
                            decodeInfo[5] = TmpStr.substring(61, 79);
                            decodeInfo[6] = TmpStr.substring(79, 94);
                            decodeInfo[7] = TmpStr.substring(94, 102);
                            decodeInfo[8] = TmpStr.substring(102, 110);
                            decodeInfo[9] = TmpStr.substring(110, 128);
                            if (decodeInfo[1].equals("1"))
                                decodeInfo[1] = "1";
                            else
                                decodeInfo[1] = "2";
                            try {
                                int code = Integer.parseInt(decodeInfo[2].toString());
                                decodeInfo[2] = decodeNation(code);
                            } catch (Exception e) {
                                decodeInfo[2] = "";
                            }

                            // 照片解码
//							try {
//								int ret = IDCReaderSDK.Init();
//								if (ret == 0) {
//									byte[] datawlt = new byte[1384];
//									byte[] byLicData = { (byte) 0x05,
//											(byte) 0x00, (byte) 0x01,
//											(byte) 0x00, (byte) 0x5B,
//											(byte) 0x03, (byte) 0x33,
//											(byte) 0x01, (byte) 0x5A,
//											(byte) 0xB3, (byte) 0x1E,
//											(byte) 0x00 };
//									for (int i = 0; i < 1295; i++) {
//										datawlt[i] = recData[i];
//									}
//									int t = IDCReaderSDK.unpack(datawlt,
//											byLicData);
//									if (t == 1) {
//										Readflage = 1;// 读卡成功
//									} else {
//										Readflage = 6;// 照片解码异常
//									}
//								} else {
//									Readflage = 6;// 照片解码异常
//								}
//							} catch (Exception e) {
//								Readflage = 6;// 照片解码异常
//							}

                        } else {
                            Readflage = -5;// 读卡失败！
                        }
                    } else {
                        Readflage = -5;// 读卡失败
                    }
                } else {
                    Readflage = -4;// 选卡失败
                }
            } else {
                Readflage = -3;// 寻卡失败
            }

        } catch (IOException e) {
            Readflage = -99;// 读取数据异常
        } catch (InterruptedException e) {
            Readflage = -99;// 读取数据异常
        }
    }

    private String decodeNation(int code) {
        String nation;
        switch (code) {
            case 1:
                nation = "01";
                break;
            case 2:
                nation = "02";
                break;
            case 3:
                nation = "03";
                break;
            case 4:
                nation = "04";
                break;
            case 5:
                nation = "05";
                break;
            case 6:
                nation = "06";
                break;
            case 7:
                nation = "07";
                break;
            case 8:
                nation = "08";
                break;
            case 9:
                nation = "09";
                break;
            case 10:
                nation = "10";
                break;
            case 11:
                nation = "11";
                break;
            case 12:
                nation = "12";
                break;
            case 13:
                nation = "13";
                break;
            case 14:
                nation = "14";
                break;
            case 15:
                nation = "15";
                break;
            case 16:
                nation = "16";
                break;
            case 17:
                nation = "17";
                break;
            case 18:
                nation = "18";
                break;
            case 19:
                nation = "19";
                break;
            case 20:
                nation = "20";
                break;
            case 21:
                nation = "21";
                break;
            case 22:
                nation = "22";
                break;
            case 23:
                nation = "23";
                break;
            case 24:
                nation = "24";
                break;
            case 25:
                nation = "25";
                break;
            case 26:
                nation = "26";
                break;
            case 27:
                nation = "27";
                break;
            case 28:
                nation = "28";
                break;
            case 29:
                nation = "29";
                break;
            case 30:
                nation = "30";
                break;
            case 31:
                nation = "31";
                break;
            case 32:
                nation = "32";
                break;
            case 33:
                nation = "33";
                break;
            case 34:
                nation = "34";
                break;
            case 35:
                nation = "35";
                break;
            case 36:
                nation = "36";
                break;
            case 37:
                nation = "37";
                break;
            case 38:
                nation = "38";
                break;
            case 39:
                nation = "39";
                break;
            case 40:
                nation = "40";
                break;
            case 41:
                nation = "41";
                break;
            case 42:
                nation = "42";
                break;
            case 43:
                nation = "43";
                break;
            case 44:
                nation = "44";
                break;
            case 45:
                nation = "45";
                break;
            case 46:
                nation = "46";
                break;
            case 47:
                nation = "47";
                break;
            case 48:
                nation = "48";
                break;
            case 49:
                nation = "49";
                break;
            case 50:
                nation = "50";
                break;
            case 51:
                nation = "51";
                break;
            case 52:
                nation = "52";
                break;
            case 53:
                nation = "53";
                break;
            case 54:
                nation = "54";
                break;
            case 55:
                nation = "55";
                break;
            case 56:
                nation = "56";
                break;
            case 97:
                nation = "99";
                break;
            case 98:
                nation = "98";
                break;
            default:
                nation = "";
                break;
        }
        return nation;
    }

    public void tvs_300_read(String shuju) {
        decodeInfo = new String[10];
        int a = shuju.length();
        char[] temp = shuju.toCharArray();
        StringBuffer name = new StringBuffer();
        StringBuffer xinbie = new StringBuffer();
        StringBuffer minzu1 = new StringBuffer();
        String minzu = null;
        StringBuffer lian = new StringBuffer();
        StringBuffer yue = new StringBuffer();
        StringBuffer ri = new StringBuffer();
        StringBuffer zhuzhi = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer qianfa = new StringBuffer();
        StringBuffer youxiao = new StringBuffer();
        for (int i = 0; i < a; i++) {
            System.out.println(i + "--" + temp[i]);
            if (i > 0 && i < 15) {
                name.append(temp[i]);
            }
            if (i == 16) {
        		/*if(temp[i]=='1'){
        			xinbie="男";
        		}
                if(temp[i]=='2'){
                	xinbie="女";
        		}*/
                xinbie.append(temp[i]);
            }
            if (i >= 17 && i <= 18) {
                minzu1.append(temp[i]);
                if (i == 18) {
                    int code = Integer.parseInt(minzu1.toString());
                    decodeInfo[2] = decodeNation(code);
                }
            }
            if (i >= 19 && i <= 22) {
                lian.append(temp[i]);
            }
            if (i >= 23 && i <= 24) {
                yue.append(temp[i]);
            }
            if (i >= 25 && i <= 26) {
                ri.append(temp[i]);
            }
            if (i >= 27 && i <= 61) {
                zhuzhi.append(temp[i]);
            }
            if (i >= 62 && i <= 79) {
                id.append(temp[i]);
            }
            if (i >= 80 && i <= 94) {
                qianfa.append(temp[i]);
            }
            if (i >= 95 && i <= 110) {
                if (i == 98 || i == 100 || i == 106 || i == 108) {
                    youxiao.append(temp[i] + ".");
                } else if (i == 102) {
                    youxiao.append(temp[i] + "-");
                } else {
                    youxiao.append(temp[i]);
                }
            }

            decodeInfo[0] = name.toString();
            decodeInfo[1] = xinbie.toString();
            decodeInfo[4] = zhuzhi.toString();
            decodeInfo[5] = id.toString();

        }
    }

    public void inv_300_read(String sIdInfo[]) {
        decodeInfo = new String[10];
        decodeInfo[0] = sIdInfo[0];
        if ("男".equals(sIdInfo[1])) {
            decodeInfo[1] = "1";
        } else {
            decodeInfo[1] = "2";
        }
        decodeInfo[3] = sIdInfo[3];
        decodeInfo[2] = getMzCode(sIdInfo[2]);
        decodeInfo[4] = sIdInfo[4];
        decodeInfo[5] = sIdInfo[5];
        decodeInfo[6] = sIdInfo[6];
        decodeInfo[7] = sIdInfo[7];
    }


    private String getMzCode(String mzName) {
        String mzCode = "";
        if (mzName.equals("汉")) {
            mzCode = "01";
        } else if (mzName.equals("蒙古")) {
            mzCode = "02";
        } else if (mzName.equals("回")) {
            mzCode = "03";
        } else if (mzName.equals("藏")) {
            mzCode = "04";
        } else if (mzName.equals("维吾尔")) {
            mzCode = "05";
        } else if (mzName.equals("苗")) {
            mzCode = "06";
        } else if (mzName.equals("彝")) {
            mzCode = "07";
        } else if (mzName.equals("壮")) {
            mzCode = "08";
        } else if (mzName.equals("布依")) {
            mzCode = "09";
        } else if (mzName.equals("朝鲜")) {
            mzCode = "10";
        } else if (mzName.equals("满")) {
            mzCode = "11";
        } else if (mzName.equals("侗")) {
            mzCode = "12";
        } else if (mzName.equals("瑶")) {
            mzCode = "13";
        } else if (mzName.equals("白")) {
            mzCode = "14";
        } else if (mzName.equals("土家")) {
            mzCode = "15";
        } else if (mzName.equals("哈尼")) {
            mzCode = "16";
        } else if (mzName.equals("哈萨克")) {
            mzCode = "17";
        } else if (mzName.equals("傣")) {
            mzCode = "18";
        } else if (mzName.equals("黎")) {
            mzCode = "19";
        } else if (mzName.equals("僳僳")) {
            mzCode = "20";
        } else if (mzName.equals("佤")) {
            mzCode = "21";
        } else if (mzName.equals("畲")) {
            mzCode = "22";
        } else if (mzName.equals("高山")) {
            mzCode = "23";
        } else if (mzName.equals("拉祜")) {
            mzCode = "24";
        } else if (mzName.equals("水")) {
            mzCode = "25";
        } else if (mzName.equals("东乡")) {
            mzCode = "26";
        } else if (mzName.equals("纳西")) {
            mzCode = "27";
        } else if (mzName.equals("景颇")) {
            mzCode = "28";
        } else if (mzName.equals("柯尔克孜")) {
            mzCode = "29";
        } else if (mzName.equals("土")) {
            mzCode = "30";
        } else if (mzName.equals("达斡尔")) {
            mzCode = "31";
        } else if (mzName.equals("仫佬")) {
            mzCode = "32";
        } else if (mzName.equals("羌")) {
            mzCode = "33";
        } else if (mzName.equals("布朗")) {
            mzCode = "34";
        } else if (mzName.equals("撒拉")) {
            mzCode = "35";
        } else if (mzName.equals("毛南")) {
            mzCode = "36";
        } else if (mzName.equals("仡佬")) {
            mzCode = "37";
        } else if (mzName.equals("锡伯")) {
            mzCode = "38";
        } else if (mzName.equals("阿昌")) {
            mzCode = "39";
        } else if (mzName.equals("普米")) {
            mzCode = "40";
        } else if (mzName.equals("塔吉克")) {
            mzCode = "41";
        } else if (mzName.equals("怒")) {
            mzCode = "42";
        } else if (mzName.equals("乌孜别克")) {
            mzCode = "43";
        } else if (mzName.equals("俄罗斯")) {
            mzCode = "44";
        } else if (mzName.equals("鄂温克")) {
            mzCode = "45";
        } else if (mzName.equals("崩龙")) {
            mzCode = "46";
        } else if (mzName.equals("保安")) {
            mzCode = "47";
        } else if (mzName.equals("裕固")) {
            mzCode = "48";
        } else if (mzName.equals("京")) {
            mzCode = "49";
        } else if (mzName.equals("塔塔尔")) {
            mzCode = "50";
        } else if (mzName.equals("独龙")) {
            mzCode = "51";
        } else if (mzName.equals("鄂伦春")) {
            mzCode = "52";
        } else if (mzName.equals("赫哲")) {
            mzCode = "53";
        } else if (mzName.equals("门巴")) {
            mzCode = "54";
        } else if (mzName.equals("珞巴")) {
            mzCode = "55";
        } else if (mzName.equals("基诺")) {
            mzCode = "56";
        } else if (mzName.equals("其他")) {
            mzCode = "99";
        } else if (mzName.equals("外国血统")) {
            mzCode = "98";
        }
        return mzCode;
    }

    public static String getMz(String mzCode) {
        String mz = "";
        if (mzCode.equals("01")) {
            mz = "汉";
        } else if (mzCode.equals("02")) {
            mz = "蒙古";
        } else if (mzCode.equals("03")) {
            mz = "回";
        } else if (mzCode.equals("04")) {
            mz = "藏";
        } else if (mzCode.equals("05")) {
            mz = "维吾尔";
        } else if (mzCode.equals("06")) {
            mz = "苗";
        } else if (mzCode.equals("07")) {
            mz = "彝";
        } else if (mzCode.equals("08")) {
            mz = "壮";
        } else if (mzCode.equals("09")) {
            mz = "布依";
        } else if (mzCode.equals("10")) {
            mz = "朝鲜";
        } else if (mzCode.equals("11")) {
            mz = "满";
        } else if (mzCode.equals("12")) {
            mz = "侗";
        } else if (mzCode.equals("13")) {
            mz = "瑶";
        } else if (mzCode.equals("14")) {
            mz = "白";
        } else if (mzCode.equals("15")) {
            mz = "土家";
        } else if (mzCode.equals("16")) {
            mz = "哈尼";
        } else if (mzCode.equals("17")) {
            mz = "哈萨克";
        } else if (mzCode.equals("18")) {
            mz = "傣";
        } else if (mzCode.equals("19")) {
            mz = "黎";
        } else if (mzCode.equals("20")) {
            mz = "僳僳";
        } else if (mzCode.equals("21")) {
            mz = "佤";
        } else if (mzCode.equals("22")) {
            mz = "畲";
        } else if (mzCode.equals("23")) {
            mz = "高山";
        } else if (mzCode.equals("24")) {
            mz = "拉祜";
        } else if (mzCode.equals("25")) {
            mz = "水";
        } else if (mzCode.equals("26")) {
            mz = "东乡";
        } else if (mzCode.equals("27")) {
            mz = "纳西";
        } else if (mzCode.equals("28")) {
            mz = "景颇";
        } else if (mzCode.equals("29")) {
            mz = "柯尔克孜";
        } else if (mzCode.equals("30")) {
            mz = "土";
        } else if (mzCode.equals("31")) {
            mz = "达斡尔";
        } else if (mzCode.equals("32")) {
            mz = "仫佬";
        } else if (mzCode.equals("33")) {
            mz = "羌";
        } else if (mzCode.equals("34")) {
            mz = "布朗";
        } else if (mzCode.equals("35")) {
            mz = "撒拉";
        } else if (mzCode.equals("36")) {
            mz = "毛南";
        } else if (mzCode.equals("37")) {
            mz = "仡佬";
        } else if (mzCode.equals("38")) {
            mz = "锡伯";
        } else if (mzCode.equals("39")) {
            mz = "阿昌";
        } else if (mzCode.equals("40")) {
            mz = "普米";
        } else if (mzCode.equals("41")) {
            mz = "塔吉克";
        } else if (mzCode.equals("42")) {
            mz = "怒";
        } else if (mzCode.equals("43")) {
            mz = "乌孜别克";
        } else if (mzCode.equals("44")) {
            mz = "俄罗斯";
        } else if (mzCode.equals("45")) {
            mz = "鄂温克";
        } else if (mzCode.equals("46")) {
            mz = "崩龙";
        } else if (mzCode.equals("47")) {
            mz = "保安";
        } else if (mzCode.equals("48")) {
            mz = "裕固";
        } else if (mzCode.equals("49")) {
            mz = "京";
        } else if (mzCode.equals("50")) {
            mz = "塔塔尔";
        } else if (mzCode.equals("51")) {
            mz = "独龙";
        } else if (mzCode.equals("52")) {
            mz = "鄂伦春";
        } else if (mzCode.equals("53")) {
            mz = "赫哲";
        } else if (mzCode.equals("54")) {
            mz = "门巴";
        } else if (mzCode.equals("55")) {
            mz = "珞巴";
        } else if (mzCode.equals("56")) {
            mz = "基诺";
        } else if (mzCode.equals("99")) {
            mz = "其他";
        } else if (mzCode.equals("98")) {
            mz = "外国血统";
        }
        return mz;
    }
	/*public void nfc_read(IDCardInfo info){
		decodeInfo = new String[10]; 
		decodeInfo[0] = info.getName();
		if("男".equals(info.getSex())){
			decodeInfo[1] = "1";
		}else{
			decodeInfo[1] = "2";
		}
    	decodeInfo[2] = getMzCode(info.getNation());
    	decodeInfo[4] = info.getAddress();
    	decodeInfo[5] = info.getIDNumber();
    	Cvr100bUtil.getInstance().setBase64Pic(Base64.encodeToString(info.getBmp(), Base64.DEFAULT));
	}
	
	*//**
     * 字节数组转化成bmp图片
     *//*
	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}*/

}
