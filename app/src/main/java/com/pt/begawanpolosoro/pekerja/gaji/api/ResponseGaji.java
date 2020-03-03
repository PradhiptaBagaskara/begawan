package com.pt.begawanpolosoro.pekerja.gaji.api;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseGaji{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private List<ResultItemGaji> result;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(List<ResultItemGaji> result){
		this.result = result;
	}

	public List<ResultItemGaji> getResult(){
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
			"ResponseGaji{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}