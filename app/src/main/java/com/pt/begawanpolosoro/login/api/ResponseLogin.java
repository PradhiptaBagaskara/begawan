package com.pt.begawanpolosoro.login.api;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private ResultLogin result;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(ResultLogin result){
		this.result = result;
	}

	public ResultLogin getResult(){
		return result;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseLogin{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}