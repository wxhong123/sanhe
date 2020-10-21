package com.service.check;

import android.content.Context;

import com.acc.common.dao.CheckCarDao;
import com.acc.common.dao.CheckCarJsDao;
import com.acc.common.dao.CheckInfoDao;
import com.acc.common.dao.CheckPersonDao;
import com.acc.common.dao.CheckPersonJsDao;
import com.acc.common.util.BeanUtils;
import com.data.DynamicDao;
import com.request.CheckListRequest;
import com.response.CheckListResonse;
import com.response.db_beans.CheckInfo;
import com.response.db_beans.OffLineCheckVo;

import java.util.List;

public class CheckInfoBusiness {
    private Context context;

    public CheckInfoBusiness(Context _context) {
        this.context = _context;
    }

    String AppCheckVoColumns = "a.id AS \"id\",\n" +
            "\t\ta.check_time AS \"checkTime\",\n" +
            "\t\ta.check_object AS \"checkObject\",\n" +
            "\t\ta.dept_id AS \"deptId\",\n" +
            "\t\ta.police_idcard AS\t\"policeIdcard\",\n" +
            "\t\ta.police_name AS \"policeName\",\n" +
            "\t\ta.check_idcard_mode AS \"checkIdcardMode\",\n" +
            "\t\ta.check_network_status AS \"checkNetworkStatus\"";
    String AppCheckVoCarColumns = "car.hpzl AS \"hpzl\",\n" +
            "\t\tcar.cphm AS \"cphm\",\n" +
            "\t\tcar.clys AS \"clys\",\n" +
            "\t\tcar.clpp AS \"clpp\",\n" +
            "\t\tcar.cldjdz AS \"cldjdz\",\n" +
            "\t\tcar.czsfzh AS \"czsfzh\",\n" +
            "\t\tcar.czxm AS \"czxm\",\n" +
            "\t\tcar.czlxfs AS \"czlxfs\",\n" +
            "\t\tcar.czxxdz AS \"czxxdz\",\n" +
            "\t\tcar.fdjh AS \"fdjh\"";
    String AppCheckVoCarNullColumns = "'' AS \"hpzl\",\n" +
            "\t\t'' AS \"cphm\",\n" +
            "\t\t'' AS \"clys\",\n" +
            "\t\t'' AS \"clpp\",\n" +
            "\t\t'' AS\n" +
            "\t\t\"cldjdz\",\n" +
            "\t\t'' AS \"czsfzh\",\n" +
            "\t\t'' AS \"czxm\",\n" +
            "\t\t'' AS \"czlxfs\",\n" +
            "\t\t'' AS \"czxxdz\",\n" +
            "\t\t''\n" +
            "\t\tAS \"fdjh\"";
    String AppCheckVoPersonColumns = "\tperson.xm AS \"xm\",\n" +
            "\t\tperson.xb AS \"xb\",\n" +
            "\t\tperson.mz AS \"mz\",\n" +
            "\t\tperson.csrq AS \"csrq\",\n" +
            "\t\tperson.sfzh AS \"sfzh\",\n" +
            "\t\tperson.hjdz AS \"hjdz\",\n" +
            "\t\tperson.fzpcs AS \"fzpcs\",\n" +
            "\t\tperson.zp AS \"zp\",\n" +
            "\t\tperson.yxq AS \"yxq\",\n" +
            "\t\tperson.rylb AS \"rylb\",\n" +
            "\t\tperson.sjly AS \"sjly\"";
    String AppCheckVoPersonNullColumns = "\t'' AS \"xm\",\n" +
            "\t\t'' AS \"xb\",\n" +
            "\t\t'' AS \"mz\",\n" +
            "\t\t'' AS \"csrq\",\n" +
            "\t\t'' AS \"sfzh\",\n" +
            "\t\t'' AS \"hjdz\",\n" +
            "\t\t'' AS \"fzpcs\",\n" +
            "\t\t'' AS \"zp\",\n" +
            "\t\t'' AS \"yxq\",\n" +
            "\t\t'' AS \"rylb\",\n" +
            "\t\t'' AS \"sjly\"";

    /**
     * 查询离线记录车的总数
     *
     * @param paramter
     * @return
     */
    public long queryCarCount(CheckListRequest paramter) {
        String sql = "   select count(*) from check_info a ,check_car car   where a.id=car.check_id";
        sql += this.getWhereSql("car", paramter);
        DynamicDao dynamicDao = new DynamicDao(context);
        return dynamicDao.queryCountBySql(sql);
    }

    /**
     * 查询离线记录人的总数
     *
     * @param paramter
     * @return
     */
    public long queryPersonCount(CheckListRequest paramter) {
        String sql = " select count(*) from check_info a ,check_person person where  a.id=person.check_id";
        sql += this.getWhereSql("person", paramter);
        DynamicDao dynamicDao = new DynamicDao(context);
        return dynamicDao.queryCountBySql(sql);
    }

    /**
     * 通过参数，查询列表集合
     *
     * @param paramter
     * @return
     */
    public List<CheckListResonse.DataBeanX.DataBean> queryRecode(CheckListRequest paramter) {
        String sql = " select * from ( select " + AppCheckVoColumns + "," + AppCheckVoPersonNullColumns + "," + AppCheckVoCarColumns + " from check_info a ,check_car car where a.id=car.check_id";
        if ("person".equals(paramter.getCheckObject()))
            sql += " and 1 =2 ";
        sql += this.getWhereSql("car", paramter);
        sql += " union ";
        sql += "   select " + AppCheckVoColumns + "," + AppCheckVoPersonColumns + "," + AppCheckVoCarNullColumns + "   from check_info a ,check_person person where  a.id=person.check_id";
        if ("car".equals(paramter.getCheckObject()))
            sql += " and 1 =2 ";
        sql += this.getWhereSql("person", paramter);
//        sql += ") t order by t.\"checkTime\" desc  limit " + String.valueOf(paramter.getPageSize()) + " offset " + String.valueOf(paramter.getPageSize() * (paramter.getPageNo() - 1));
        sql += ") t order by t.\"checkTime\" desc  limit " + 5 + " offset " + String.valueOf(5 * (paramter.getPageNo() - 1));
        DynamicDao dynamicDao = new DynamicDao(context);
        List<CheckListResonse.DataBeanX.DataBean> dataBeanList = dynamicDao.queryForList(sql, CheckListResonse.DataBeanX.DataBean.class);
        BeanUtils.translateListBeanDict(dataBeanList, context);
//        if (dataBeanList != null) {
//            for (CheckListResonse.DataBeanX.DataBean bean : dataBeanList) {
//                OffLineCheckVo offLineCheckVo = queryRecodeDetail(bean.getId());
//                if (offLineCheckVo != null) {
//                    if ("person".equals(paramter.getCheckObject()) && offLineCheckVo.getZbryList() != null) {
//                        bean.setZbxxNumber("" + offLineCheckVo.getZbryList().size());
//                    } else if ("car".equals(paramter.getCheckObject()) && offLineCheckVo.getZbclList() != null) {
//                        bean.setZbxxNumber("" + offLineCheckVo.getZbclList().size());
//                    }
//                }
//            }
//        }

        return dataBeanList;
    }

    /**
     * 通过核查记录信息
     *
     * @param checkId
     * @return
     */
    public OffLineCheckVo queryRecodeDetail(String checkId) {
        CheckInfoDao checkInfoDao = new CheckInfoDao(context);
        CheckInfo checkInfo = checkInfoDao.queryCheckInfoById(checkId);
        OffLineCheckVo offLineCheckVo = this.queryRecodeDetail(checkInfo);
        BeanUtils.translateBeanDict(offLineCheckVo.getClxx(), context);
        BeanUtils.translateBeanDict(offLineCheckVo.getClxx(), context);
        return offLineCheckVo;
    }

    /**
     * 通过核录主表，查询子表信息
     *
     * @param checkInfo
     * @return
     */
    public OffLineCheckVo queryRecodeDetail(CheckInfo checkInfo) {
        OffLineCheckVo offLineCheckVo = new OffLineCheckVo();
        offLineCheckVo.setMain(checkInfo);
        if (checkInfo != null && "person".equals(checkInfo.getCheckObject())) {//查询人员基本信息与警示信息
            CheckPersonDao checkPersonDao = new CheckPersonDao(context);
            CheckPersonJsDao checkPersonJsDao = new CheckPersonJsDao(context);
            offLineCheckVo.setRyxx(checkPersonDao.queryCheckPersonByCheckId(checkInfo.getId()));
            offLineCheckVo.setZbryList(checkPersonJsDao.queryCheckCarJsListByCheckId(checkInfo.getId()));

        } else if (checkInfo != null && "car".equals(checkInfo.getCheckObject())) {
            ///查询车辆基本信息与车辆重点信息
            CheckCarDao checkCarDao = new CheckCarDao(context);
            CheckCarJsDao checkCarJsDao = new CheckCarJsDao(context);
            offLineCheckVo.setClxx(checkCarDao.queryCheckCarByCheckId(checkInfo.getId()));
            offLineCheckVo.setZbclList(checkCarJsDao.queryCheckCarJsListByCheckId(checkInfo.getId()));
        }
        return offLineCheckVo;
    }

    /**
     * 通过核查主表ID，删除所有与核查单有关的数据
     *
     * @param checkInfoid
     */
    public void deleteByCheckInfo(String checkInfoid) {
        CheckInfoDao checkInfoDao = new CheckInfoDao(context);
        CheckPersonDao checkPersonDao = new CheckPersonDao(context);
        CheckPersonJsDao checkPersonJsDao = new CheckPersonJsDao(context);
        CheckCarDao checkCarDao = new CheckCarDao(context);
        CheckCarJsDao checkCarJsDao = new CheckCarJsDao(context);
        checkInfoDao.deleteById(checkInfoid);
        checkCarDao.deleteByCheckId(checkInfoid);
        checkPersonDao.deleteByCheckId(checkInfoid);
        checkCarJsDao.deleteByCheckId(checkInfoid);
        checkPersonJsDao.deleteByCheckId(checkInfoid);
    }

    /**
     * 通过参数拼装SQL语句
     *
     * @param checkObject
     * @param paramter
     * @return
     */
    public String getWhereSql(String checkObject, CheckListRequest paramter) {
        String sql = "";
        if ("person".equals(checkObject)) {
            if (paramter.getStartCheckTime() != null && paramter.getStartCheckTime().length() > 0)
                sql += "  and a.check_time >= '" + paramter.getStartCheckTime() + "'";
            if (paramter.getEndCheckTime() != null && paramter.getEndCheckTime().length() > 0)
                sql += "  and a.check_time <= '" + paramter.getEndCheckTime() + "'";
            if (paramter.getSearchWord() != null && paramter.getSearchWord().length() > 0)
                sql += "   and (person.xm like '%" + paramter.getSearchWord() + "%' or person.sfzh like  '%" + paramter.getSearchWord() + "%'  )";
            if (paramter.getCardNumber() != null && paramter.getCardNumber().length() > 0)
                sql += "  and person.sfzh='" + paramter.getCardNumber() + "'";
            if (paramter.getPoliceIdcard() != null && paramter.getPoliceIdcard().length() > 0)
                sql += "   and a.police_idcard='" + paramter.getPoliceIdcard() + "'";
        } else if ("car".equals(checkObject)) {
            if (paramter.getStartCheckTime() != null && paramter.getStartCheckTime().length() > 0)
                sql += "  and a.check_time >= '" + paramter.getStartCheckTime() + "'";
            if (paramter.getEndCheckTime() != null && paramter.getEndCheckTime().length() > 0)
                sql += "  and a.check_time <= '" + paramter.getEndCheckTime() + "'";
            if (paramter.getSearchWord() != null && paramter.getSearchWord().length() > 0)
                sql += "   and (car.cphm like like '%" + paramter.getSearchWord() + "%' or car.czsfzh like  '%" + paramter.getSearchWord() + "%' or  car.czxm like  '%" + paramter.getSearchWord() + "%'  )";
            if (paramter.getCardNumber() != null && paramter.getCardNumber().length() > 0)
                sql += "  car.cphm='" + paramter.getCardNumber() + "'";
            if (paramter.getPoliceIdcard() != null && paramter.getPoliceIdcard().length() > 0)
                sql += "   and a.police_idcard='" + paramter.getPoliceIdcard() + "'";
        }
        return sql;
    }
}
