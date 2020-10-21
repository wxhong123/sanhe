package com.qhd.bean;

/**
 * 请求 返回结果集
 * @author xiaowei
 *
 */
public class ReturnResultBean1<T> {
	private int state;
	private PersonOcrBean message;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public PersonOcrBean getMessage() {
		return message;
	}

	public void setMessage(PersonOcrBean message) {
		this.message = message;
	}

 
}
