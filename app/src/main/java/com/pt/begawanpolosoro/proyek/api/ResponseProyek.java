package com.pt.begawanpolosoro.proyek.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseProyek{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private List<ResultItemProyek> result;

	@SerializedName("transaksi")
	private List<TransaksiItem> transaksi;

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

	public void setTransaksi(List<TransaksiItem> transaksi){
		this.transaksi = transaksi;
	}

	public List<TransaksiItem> getTransaksi(){
		return transaksi;
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
			",transaksi = '" + transaksi + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}