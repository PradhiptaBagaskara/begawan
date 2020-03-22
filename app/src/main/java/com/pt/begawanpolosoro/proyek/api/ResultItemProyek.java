package com.pt.begawanpolosoro.proyek.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultItemProyek implements Serializable {

	@SerializedName("sisa_modal")
	private String sisaModal;

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("total_dana")
	private String totalDana;

	@SerializedName("tgl_mulai")
	private String tglMulai;

	@SerializedName("tgl_selesai")
	private String tglSelesai;

	@SerializedName("id")
	private String id;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("nama_proyek")
	private String namaProyek;

	@SerializedName("modal")
	private String modal;

	public void setSisaModal(String sisaModal){
		this.sisaModal = sisaModal;
	}

	public String getSisaModal(){
		return sisaModal;
	}

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setTotalDana(String totalDana){
		this.totalDana = totalDana;
	}

	public String getTotalDana(){
		return totalDana;
	}

	public void setTglMulai(String tglMulai){
		this.tglMulai = tglMulai;
	}

	public String getTglMulai(){
		return tglMulai;
	}

	public void setTglSelesai(String tglSelesai){
		this.tglSelesai = tglSelesai;
	}

	public String getTglSelesai(){
		return tglSelesai;
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
			"ResultItem{" + 
			"sisa_modal = '" + sisaModal + '\'' + 
			",keterangan = '" + keterangan + '\'' + 
			",total_dana = '" + totalDana + '\'' + 
			",tgl_mulai = '" + tglMulai + '\'' + 
			",tgl_selesai = '" + tglSelesai + '\'' + 
			",id = '" + id + '\'' + 
			",created_date = '" + createdDate + '\'' + 
			",nama_proyek = '" + namaProyek + '\'' + 
			",modal = '" + modal + '\'' + 
			"}";
		}
}