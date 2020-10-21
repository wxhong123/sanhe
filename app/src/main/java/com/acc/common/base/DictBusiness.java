package com.acc.common.base;

import android.content.Context;
import android.text.TextUtils;

import com.data.BaseDicValue;
import com.data.BaseDicValueDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictBusiness {

    public static final String car_type = "car_type";

    public static final String car_cplx="car_cplx";


    private static Map<String, List<BaseDicValue>> dictMap = new HashMap<>();
    private Context context;

    public DictBusiness(Context _context) {
        this.context = _context;
    }

    public String getDictLablByTypeAndValue(String type, String value) {
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(value))
            return null;

        List<BaseDicValue> list = this.getBaseDicValueListBytype(type);
        String label = this.queryLableByListAndType(list, value);
        if (TextUtils.isEmpty(label)) {//用于更改数据字典内容，重新加载数据
            list = this.freshenDictByType(type);
            return queryLableByListAndType(list, value);
        } else
            return label;
    }

    public List<BaseDicValue> getBaseDicValueListBytype(String type) {
        if (TextUtils.isEmpty(type))
            return null;
        if (!dictMap.containsKey(type)) {
            this.freshenDictByType(type);
        }
        return dictMap.get(type);
    }

    public String getDictCodeByTypeAndLable(String type, String lable) {
        List<BaseDicValue> baseDicValueList = this.getBaseDicValueListBytype(type);
        if (baseDicValueList != null || baseDicValueList.size() < 1 || TextUtils.isEmpty(lable))
            return null;
        for (BaseDicValue baseDicValue : baseDicValueList) {
            if (lable.equals(baseDicValue.getDicValueName()))
                return baseDicValue.getDicValueCode();
        }
        return "";
    }


    public String queryLableByListAndType(List<BaseDicValue> list, String value) {
        if (list == null || list.size() < 1 || TextUtils.isEmpty(value))
            return null;
        for (BaseDicValue baseDicValue : list) {
            if (value.equals(baseDicValue.getDicValueCode()))
                return baseDicValue.getDicValueName();
        }
        return "";
    }

    /**
     * 查询数据字典集合
     *
     * @param type
     * @return
     */
    public String[] queryLablesByType(String type) {
        List<BaseDicValue> baseDicValueList = this.getBaseDicValueListBytype(type);
        if (baseDicValueList != null && baseDicValueList.size() > 0) {
            String[] labels = new String[baseDicValueList.size()];
            for (int i = 0; i < baseDicValueList.size(); i++) {
                labels[i] = baseDicValueList.get(i).getDicValueName();
            }
            return labels;
        }
        return null;
    }

    /**
     * 查询数据字典集合
     *
     * @param type
     * @return
     */
    public String[] queryCodesByType(String type) {
        List<BaseDicValue> baseDicValueList = this.getBaseDicValueListBytype(type);
        if (baseDicValueList != null && baseDicValueList.size() > 0) {
            String[] codes = new String[baseDicValueList.size()];
            for (int i = 0; i < baseDicValueList.size(); i++) {
                codes[i] = baseDicValueList.get(i).getDicValueCode();
            }
            return codes;
        }
        return null;
    }

    public List<BaseDicValue> freshenDictByType(String type) {
        BaseDicValueDao baseDicValueDao = new BaseDicValueDao(context);
        List<BaseDicValue> list = baseDicValueDao.queryDictListByType(type);
        dictMap.put(type, list);
        return list;
    }
}
