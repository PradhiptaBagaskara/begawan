package com.pt.begawanpolosoro.login.api;

import com.google.gson.annotations.SerializedName;

public class ResultLogin{

	@SerializedName("password")
	private String password;

	@SerializedName("role")
	private String role;

	@SerializedName("is_active")
	private String isActive;

	@SerializedName("foto")
	private String foto;

	@SerializedName("nama")
	private String nama;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("id")
	private String id;

	@SerializedName("nomer")
	private Object nomer;

	@SerializedName("saldo")
	private String saldo;

	@SerializedName("username")
	private String username;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

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

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setDeviceToken(String deviceToken){
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setNomer(Object nomer){
		this.nomer = nomer;
	}

	public Object getNomer(){
		return nomer;
	}

	public void setSaldo(String saldo){
		this.saldo = saldo;
	}

	public String getSaldo(){
		return saldo;
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
			"Result{" + 
			"password = '" + password + '\'' + 
			",role = '" + role + '\'' + 
			",is_active = '" + isActive + '\'' + 
			",foto = '" + foto + '\'' + 
			",nama = '" + nama + '\'' + 
			",device_token = '" + deviceToken + '\'' + 
			",id = '" + id + '\'' + 
			",nomer = '" + nomer + '\'' + 
			",saldo = '" + saldo + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}