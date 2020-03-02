package com.pt.begawanpolosoro.proyek.api;

import com.google.gson.annotations.SerializedName;

public class ResponseInsertProyek{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private ResultInsertProyek result;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(ResultInsertProyek result){
		this.result = result;
	}

	public ResultInsertProyek getResult(){
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
			"ResponseInsertProyek{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}