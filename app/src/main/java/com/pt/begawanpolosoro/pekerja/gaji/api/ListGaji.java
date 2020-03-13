package com.pt.begawanpolosoro.pekerja.gaji.api;

import com.google.gson.annotations.SerializedName;

public class ListGaji {

	@SerializedName("keterangan")
	private String keterangan;

//	@SerializedName("nama_pengirim")
	private String namaPengirim;

//	@SerializedName("nama")
	private String nama;

//	@SerializedName("file_name")
	private String fileName;

//	@SerializedName("gaji")
	private String gaji;

//	@SerializedName("id")
	private String id;

//	@SerializedName("created_date")
	private String createdDate;

//	@SerializedName("nama_proyek")
	private String namaProyek;

//	@SerializedName("username")
	private String username;
	private ResultItemGaji resultItemGaji;
//
	public ListGaji(ResultItemGaji list) {
		this.keterangan = list.getKeterangan();
		this.namaPengirim = list.getNamaPengirim();
		this.nama = list.getNama();
		this.fileName = list.getFileName();
		this.gaji = list.getGaji();
		this.id = list.getId();
		this.createdDate = list.getCreatedDate();
		this.namaProyek = list.getNamaProyek();
		this.username = list.getUsername();
		this.resultItemGaji = list;

	}

	public ResultItemGaji getResultItemGaji() {
		return resultItemGaji;
	}

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

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFileName(){
		return fileName;
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

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setNamaProyek(String namaProyek){
		this.namaProyek = namaProyek;
	}

	public String getNamaProyek(){
		return namaProyek;
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
			"ListGaji{" + 
			"keterangan = '" + keterangan + '\'' + 
			",nama_pengirim = '" + namaPengirim + '\'' + 
			",nama = '" + nama + '\'' + 
			",file_name = '" + fileName + '\'' + 
			",gaji = '" + gaji + '\'' + 
			",id = '" + id + '\'' + 
			",created_date = '" + createdDate + '\'' + 
			",nama_proyek = '" + namaProyek + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}