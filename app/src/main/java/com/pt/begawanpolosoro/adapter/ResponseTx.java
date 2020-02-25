package com.pt.begawanpolosoro.adapter;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseTx{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private List<ResultItemTx> result;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(List<ResultItemTx> result){
		this.result = result;
	}

	public List<ResultItemTx> getResult(){
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
			"ResponseTx{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}