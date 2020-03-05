package com.pt.begawanpolosoro.pdf.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePdf{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private List<ResultItemPdf> result;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(List<ResultItemPdf> result){
		this.result = result;
	}

	public List<ResultItemPdf> getResult(){
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
			"ResponsePdf{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}