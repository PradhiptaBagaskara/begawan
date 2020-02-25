package com.pt.begawanpolosoro.user.api;

import com.google.gson.annotations.SerializedName;

public class ResultItemUser {

	@SerializedName("role")
	private String role;

	@SerializedName("is_active")
	private String isActive;

	@SerializedName("nama")
	private String nama;

	@SerializedName("device_token")
	private Object deviceToken;

	@SerializedName("id")
	private String id;

	@SerializedName("saldo")
	private String saldo;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("username")
	private String username;

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
	}

	public void setIsActive(String isActive){
		this.isActive = isActive;
	}

	public String getIsActive(){
		return isActive;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setDeviceToken(Object deviceToken){
		this.deviceToken = deviceToken;
	}

	public Object getDeviceToken(){
		return deviceToken;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSaldo(String saldo){
		this.saldo = saldo;
	}

	public String getSaldo(){
		return saldo;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"ResultItem{" + 
			"role = '" + role + '\'' + 
			",is_active = '" + isActive + '\'' + 
			",nama = '" + nama + '\'' + 
			",device_token = '" + deviceToken + '\'' + 
			",id = '" + id + '\'' + 
			",saldo = '" + saldo + '\'' + 
			",created_date = '" + createdDate + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}