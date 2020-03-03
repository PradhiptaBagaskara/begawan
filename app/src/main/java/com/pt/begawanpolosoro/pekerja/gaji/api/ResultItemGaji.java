package com.pt.begawanpolosoro.pekerja.gaji.api;

import com.google.gson.annotations.SerializedName;

public class ResultItemGaji {

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("nama_pengirim")
	private String namaPengirim;

	@SerializedName("nama")
	private String nama;

	@SerializedName("id_pemilik")
	private String idPemilik;

	@SerializedName("gaji")
	private String gaji;

	@SerializedName("id")
	private String id;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("bulan")
	private String bulan;

	@SerializedName("username")
	private String username;

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setNamaPengirim(String namaPengirim){
		this.namaPengirim = namaPengirim;
	}

	public String getNamaPengirim(){
		return namaPengirim;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setIdPemilik(String idPemilik){
		this.idPemilik = idPemilik;
	}

	public String getIdPemilik(){
		return idPemilik;
	}

	public void setGaji(String gaji){
		this.gaji = gaji;
	}

	public String getGaji(){
		return gaji;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setBulan(String bulan){
		this.bulan = bulan;
	}

	public String getBulan(){
		return bulan;
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
			"keterangan = '" + keterangan + '\'' + 
			",nama_pengirim = '" + namaPengirim + '\'' + 
			",nama = '" + nama + '\'' + 
			",id_pemilik = '" + idPemilik + '\'' + 
			",gaji = '" + gaji + '\'' + 
			",id = '" + id + '\'' + 
			",id_user = '" + idUser + '\'' + 
			",created_date = '" + createdDate + '\'' + 
			",bulan = '" + bulan + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}