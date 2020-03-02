package com.pt.begawanpolosoro.proyek.api;

import com.google.gson.annotations.SerializedName;

public class ResultInsertProyek {

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("id")
	private String id;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("nama_proyek")
	private String namaProyek;

	@SerializedName("modal")
	private String modal;

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
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

	public void setModal(String modal){
		this.modal = modal;
	}

	public String getModal(){
		return modal;
	}

	@Override
 	public String toString(){
		return 
			"Result{" + 
			"keterangan = '" + keterangan + '\'' + 
			",id = '" + id + '\'' + 
			",created_date = '" + createdDate + '\'' + 
			",nama_proyek = '" + namaProyek + '\'' + 
			",modal = '" + modal + '\'' + 
			"}";
		}
}