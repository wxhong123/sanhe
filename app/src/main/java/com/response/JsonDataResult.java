package com.response;

public class JsonDataResult<T>{
	private String code = "000";//000为成功，非000则失败
	private String msg;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	private T data ;//具体泛型对象数据
	
	public boolean isSuccess(){
		return code.equals("000");
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
