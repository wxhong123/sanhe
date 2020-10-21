package com.acc.common.util;

import android.content.Context;
import android.util.Base64;

import com.acc.common.AppConfig;
import com.caah.mppclient.api.common.CommonDBUtils;
import com.caah.mppclient.api.common.CommonInfoBean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * appToken和userToken生成类
 * created by xh.w on 2020.10.21
 */
public class TokenInfo {

    /**
     * 创建userToken
     */
    public static void createUserTocken(Context mContext){
        CommonInfoBean infoBean = CommonDBUtils.getCommoninfo(mContext);
        String userToken = infoBean.getToken();//登录用户身份令牌
        SharedPreferencesUtils.setValue(mContext, AppConfig.USER_TOKEN, userToken);
    }

    /**
     * 创建appToken
     * @param mContext
     * @return
     */
    public static void createAppToken (Context mContext) {
        Map<String, Object> map = new HashMap<>();
        map.put("username",SharedPreferencesUtils.getValue(mContext,AppConfig.JING_HAO));
        map.put("appKey", AppConfig.APP_KEY);
        map.put("appVersion", AppConfig.APP_VERSION);
        map.put("appZone", AppConfig.APP_ZONE);
        map.put("appType", AppConfig.APP_TYPE);

        String key = Base64.encodeToString("APPSECRET的值".getBytes(), 0);
        Date issuedAt=new Date(System.currentTimeMillis());//生成时间
        Date expiresAt = new Date(System.currentTimeMillis()+30*24*60*1000);//失效时间
        String appToken = Jwts.builder().addClaims(map).setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS256, key).setId(UUID.randomUUID().toString()).setIssuer("hebmpp.org").setIssuedAt(issuedAt).setExpiration(expiresAt).compact();
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(appToken);
        }
        catch (ExpiredJwtException e) {
            e.printStackTrace();
        }
        SharedPreferencesUtils.setValue(mContext, AppConfig.APP_TOKEN, appToken);
    }
}
