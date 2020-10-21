package com.qhd;

import android.os.Environment;

import com.arong.swing.db.dao.StaffUserSDAO;
import com.arong.swing.db.entity.StaffUserVO;
import com.arong.swing.exception.AppException;
import com.arong.swing.exception.SysException;

import java.sql.SQLException;
import java.util.List;

public class MyStaffUserSDAO extends StaffUserSDAO {
    static String addr = Environment.getExternalStorageDirectory().toString() + "/anrong/infocheck/datas/" + "anrongtec.db";

    public MyStaffUserSDAO() {
        super(addr);
    }


    @Override
    public List<StaffUserVO> queryStaffUserVOALL() throws SysException, AppException {
        List res = null;
        try {
            res = this.jdbcUtils.queryList("SELECT 2 as user_type,s.DEPARTMENTS,s.STAFF_ID,s.STAFF_NAME,s.LOGIN_NAME,s.LOGIN_PASSWORD,s.DEPT_ID,s.TELEPHONE,s.EMAIL,s.STS,s.STS_TIME,s.CREATE_TIME,s.CREATE_STAFF_ID,s.STAFF_CODE,s.SEX from STAFF_USER s order by s.CREATE_TIME asc ", StaffUserVO.class, new Object[0]);
            return res;
        } catch (SQLException var3) {
            throw new SysException("queryList集合查询 error", var3);
        }
    }


}
