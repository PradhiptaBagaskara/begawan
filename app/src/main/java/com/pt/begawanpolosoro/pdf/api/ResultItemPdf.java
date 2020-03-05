package com.pt.begawanpolosoro.pdf.api;

import com.google.gson.annotations.SerializedName;

public class ResultItemPdf{

	@SerializedName("file_name")
	private String fileName;

	@SerializedName("nama_laporan")
	private String namaLaporan;

	@SerializedName("id")
	private String id;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("created_date")
	private String createdDate;

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFileName(){
		return fileName;
	}

	public void setNamaLaporan(String namaLaporan){
		this.namaLaporan = namaLaporan;
	}

	public String getNamaLaporan(){
		return namaLaporan;
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

	@Override
 	public String toString(){
		return 
			"ResultItem{" + 
			"file_name = '" + fileName + '\'' + 
			",nama_laporan = '" + namaLaporan + '\'' + 
			",id = '" + id + '\'' + 
			",id_user = '" + idUser + '\'' + 
			",created_date = '" + createdDate + '\'' + 
			"}";
		}
}