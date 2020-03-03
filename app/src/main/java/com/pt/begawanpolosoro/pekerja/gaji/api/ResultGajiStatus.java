package com.pt.begawanpolosoro.pekerja.gaji.api;

import com.google.gson.annotations.SerializedName;

public class ResultGajiStatus {

	@SerializedName("msg")
	private String msg;

	@SerializedName("is_gaji")
	private boolean isGaji;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setIsGaji(boolean isGaji){
		this.isGaji = isGaji;
	}

	public boolean isIsGaji(){
		return isGaji;
	}

	@Override
 	public String toString(){
		return 
			"Result{" + 
			"msg = '" + msg + '\'' + 
			",is_gaji = '" + isGaji + '\'' + 
			"}";
		}
}