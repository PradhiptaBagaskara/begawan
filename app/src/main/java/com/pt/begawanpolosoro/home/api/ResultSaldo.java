package com.pt.begawanpolosoro.home.api;

import com.google.gson.annotations.SerializedName;

public class ResultSaldo{

	@SerializedName("saldo")
	private String saldo;

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
