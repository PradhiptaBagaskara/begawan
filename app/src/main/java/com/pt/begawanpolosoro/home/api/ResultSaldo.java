package com.pt.begawanpolosoro.home.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultSaldo implements Serializable {

	@SerializedName("saldo")
	private String saldo;

	@SerializedName("total_piutang")
	private String total_piutang;

	public String getTotal_piutang() {
		return total_piutang;
	}

	public void setTotal_piutang(String total_piutang) {
		this.total_piutang = total_piutang;
	}

	public void setSaldo(String saldo){
		this.saldo = saldo;
	}

	public String getSaldo(){
		return saldo;
	}

	@Override
 	public String toString(){
		return 
			"Result{" + 
			"saldo = '" + saldo + '\'' + 
			"}";
		}
}
