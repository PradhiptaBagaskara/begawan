package com.pt.begawanpolosoro.pekerja.gaji.api;

import com.google.gson.annotations.SerializedName;

public class ResponseStatusGaji{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private ResultGajiStatus result;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(ResultGajiStatus result){
		this.result = result;
	}

	public ResultGajiStatus getResult(){
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
			"ResponseStatusGaji{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}