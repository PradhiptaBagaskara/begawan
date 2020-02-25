package com.pt.begawanpolosoro.proyek.api;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseProyek{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private List<ResultItemProyek> result;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(List<ResultItemProyek> result){
		this.result = result;
	}

	public List<ResultItemProyek> getResult(){
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
			"ResponseProyek{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}