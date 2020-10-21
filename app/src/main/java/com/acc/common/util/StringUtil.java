package com.acc.common.util;

import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

public class StringUtil {

    /**
     * 判断字符串是否为null或�?空字符串
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        boolean result = false;
        if (null == str || "".equals(str.trim()) || "null".equals(str.trim())) {
            result = true;
        }
        return result;
    }

    /**
     * 如果i小于10，添�?后生成string
     *
     * @param i
     * @return
     */
    public static String addZreoIfLessThanTen(int i) {

        String string = "";
        int ballNum = i + 1;
        if (ballNum < 10) {
            string = "0" + ballNum;
        } else {
            string = ballNum + "";
        }
        return string;
    }

    /**
     * @param string
     * @return
     */
    public static boolean isNotNull(String string) {
        return null != string && !"".equals(string.trim());
    }

    /**
     * 去掉�?��字符串中的所有的单个空格" "
     *
     * @param string
     */
    public static String replaceSpaceCharacter(String string) {
        if (null == string || "".equals(string)) {
            return "";
        }
        return string.replace(" ", "");
    }

    /**
     * 获取小数位为6位的经纬�?
     *
     * @param string
     * @return
     */
    public static String getStringLongitudeOrLatitude(String string) {
        if (StringUtil.isNullOrEmpty(string)) {
            return "";
        }
        if (string.contains(".")) {
            String[] splitArray = string.split("\\.");
            if (splitArray[1].length() > 6) {
                String substring = splitArray[1].substring(0, 6);
                return splitArray[0] + "." + substring;
            } else {
                return string;
            }
        } else {
            return string;
        }
    }


    public static String getStrings(EditText editText) {
        if (editText == null) {
            return "";
        }
        String s = editText.getText().toString().trim();
        if (StringUtil.isNotNull(s)) {
            return s;
        } else {
            return "";
        }

    }

    public static String getStrings(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }


    public static byte[] gbk2utf8(String chenese) {
        char c[] = chenese.toCharArray();
        byte[] fullByte = new byte[3 * c.length];
        for (int i = 0; i < c.length; i++) {
            int m = (int) c[i];
            String word = Integer.toBinaryString(m);
            // System.out.println(word);

            StringBuffer sb = new StringBuffer();
            int len = 16 - word.length();
            // 补零
            for (int j = 0; j < len; j++) {
                sb.append("0");
            }
            sb.append(word);
            sb.insert(0, "1110");
            sb.insert(8, "10");
            sb.insert(16, "10");

            // System.out.println(sb.toString());

            String s1 = sb.substring(0, 8);
            String s2 = sb.substring(8, 16);
            String s3 = sb.substring(16);

            byte b0 = Integer.valueOf(s1, 2).byteValue();
            byte b1 = Integer.valueOf(s2, 2).byteValue();
            byte b2 = Integer.valueOf(s3, 2).byteValue();
            byte[] bf = new byte[3];
            bf[0] = b0;
            fullByte[i * 3] = bf[0];
            bf[1] = b1;
            fullByte[i * 3 + 1] = bf[1];
            bf[2] = b2;
            fullByte[i * 3 + 2] = bf[2];

        }
        return fullByte;
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }


}
