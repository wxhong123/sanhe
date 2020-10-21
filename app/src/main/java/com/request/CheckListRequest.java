package com.request;

/**
 * Created by androider on 2018/7/26.
 * 内容：
 */
public class CheckListRequest {

    /**
     * searchWord  : N13431
     * policeIdcard  : 调用者身份证号
     * policeName  : 调用者姓名
     * startCheckTime  : 2018-07-17 14:01:33
     * endCheckTime : 2018-07-17 14:01:33
     * cardNumber  : “420”
     * checkObject : person
     * “pageSize” : 10
     * “pageNo” : 1
     */

    private String searchWord;
    private String policeIdcard;
    private String policeName;
    private String startCheckTime;
    private String endCheckTime;
    private String cardNumber;
    private String checkObject;
    private String sfzh;
    private int pageSize; // FIXME check this code
    private int pageNo; // FIXME check this code

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getPoliceIdcard() {
        return policeIdcard;
    }

    public void setPoliceIdcard(String policeIdcard) {
        this.policeIdcard = policeIdcard;
    }

    public String getPoliceName() {
        return policeName;
    }

    public void setPoliceName(String policeName) {
        this.policeName = policeName;
    }

    public String getStartCheckTime() {
        return startCheckTime;
    }

    public void setStartCheckTime(String startCheckTime) {
        this.startCheckTime = startCheckTime;
    }

    public String getEndCheckTime() {
        return endCheckTime;
    }

    public void setEndCheckTime(String endCheckTime) {
        this.endCheckTime = endCheckTime;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCheckObject() {
        return checkObject;
    }

    public void setCheckObject(String checkObject) {
        this.checkObject = checkObject;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
