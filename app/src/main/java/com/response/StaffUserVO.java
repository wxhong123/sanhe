package com.response;


public class StaffUserVO  extends Staff{
	public final static String TYPE_STAFF = "1";
	public final static String TYPE_STAFFUSER = "2";
	
	public boolean isFormalPolice(){
		if(userType.equals(TYPE_STAFF)){
			return true;
		}else{
			return false;
		}
	}
	
	private String userType;//1为staff正式警员2为staffUser辅警

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String toString() {
		return this.getStaffName()+"/"+this.getStaffCode();
	}
}