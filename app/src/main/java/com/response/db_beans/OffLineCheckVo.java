package com.response.db_beans;

import java.util.List;

public class OffLineCheckVo {

	CheckInfo main;

	CheckPerson ryxx;

	List<CheckPersonJs> zbryList;

	CheckCar clxx;

	List<CheckCarJs> zbclList;

	public CheckInfo getMain() {
		return main;
	}

	public void setMain(CheckInfo main) {
		this.main = main;
	}

	public CheckPerson getRyxx() {
		return ryxx;
	}

	public void setRyxx(CheckPerson ryxx) {
		this.ryxx = ryxx;
	}

	public List<CheckPersonJs> getZbryList() {
		return zbryList;
	}

	public void setZbryList(List<CheckPersonJs> zbryList) {
		this.zbryList = zbryList;
	}

	public CheckCar getClxx() {
		return clxx;
	}

	public void setClxx(CheckCar clxx) {
		this.clxx = clxx;
	}

	public List<CheckCarJs> getZbclList() {
		return zbclList;
	}

	public void setZbclList(List<CheckCarJs> zbclList) {
		this.zbclList = zbclList;
	}

}
