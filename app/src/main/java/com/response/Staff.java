package com.response;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.sql.Timestamp;


public class Staff implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String staffId;//员工ID

	private String loginName;//登录ID

	private String loginPassword;//登录密码
	
	private String deptId;//部门ID
	
	private String staffName;//警员姓名
	private String staffCode;//警员身份证号
	private String sex;
	
    private byte[] photo;
	
    private String departments;//所在部门
    private String duty;//职务
    private String jingxian;
    private String jingzhong;
    
    private String original;//籍贯
    private String nation;//民族
    
	private String telephone;
	private String email;

	private String address;

	private String remark;
	
	private String sts;
	private Timestamp stsTime;

	private Timestamp createTime;
	private String createStaffId;

	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getJingxian() {
		return jingxian;
	}

	public void setJingxian(String jingxian) {
		this.jingxian = jingxian;
	}

	public String getJingzhong() {
		return jingzhong;
	}

	public void setJingzhong(String jingzhong) {
		this.jingzhong = jingzhong;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
	public String getStaffId() {
		return staffId;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	
	public String getStaffName() {
		return staffName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	
	public String getLoginPassword() {
		return loginPassword;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	public String getDeptId() {
		return deptId;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getTelephone() {
		return telephone;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public void setSts(String sts) {
		this.sts = sts;
	}
	
	public String getSts() {
		return sts;
	}

	public void setStsTime(Timestamp stsTime) {
		this.stsTime = stsTime;
	}
	
	public Timestamp getStsTime() {
		return stsTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateStaffId(String createStaffId) {
		this.createStaffId = createStaffId;
	}
	
	public String getCreateStaffId() {
		return createStaffId;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof Staff) {
			Staff another = (Staff) obj;
			equals = new EqualsBuilder().append(staffId, another.getStaffId()).isEquals();
		}
		return equals;
	}
	
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(staffId).toHashCode();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("staffId", getStaffId()).toString();
	}
}