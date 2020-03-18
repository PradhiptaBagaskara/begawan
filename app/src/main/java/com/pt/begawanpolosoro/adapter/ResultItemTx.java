package com.pt.begawanpolosoro.adapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultItemTx implements Serializable {

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("nama")
	private String nama;

	@SerializedName("jenis")
	private String jenis;


	@SerializedName("status")
	private String status;

	@SerializedName("nama_transaksi")
	private String namaTransaksi;

	@SerializedName("id")
	private String id;

	@SerializedName("nama_proyek")
	private String namaProyek;

	@SerializedName("dana")
	private String dana;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@SerializedName("file_name")
	private String fileName;

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	@SerializedName("created_date")
	private String createdDate;

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setJenis(String jenis){
		this.jenis = jenis;
	}

	public String getJenis(){
		return jenis;
	}

	public void setNamaTransaksi(String namaTransaksi){
		this.namaTransaksi = namaTransaksi;
	}

	public String getNamaTransaksi(){
		return namaTransaksi;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setNamaProyek(String namaProyek){
		this.namaProyek = namaProyek;
	}

	public String getNamaProyek(){
		return namaProyek;
	}

	public void setDana(String dana){
		this.dana = dana;
	}

	public String getDana(){
		return dana;
	}

	@Override
 	public String toString(){
		return 
			"ResultItem{" + 
			"keterangan = '" + keterangan + '\'' + 
			",nama = '" + nama + '\'' + 
			",jenis = '" + jenis + '\'' + 
			",nama_transaksi = '" + namaTransaksi + '\'' + 
			",id = '" + id + '\'' + 
			",nama_proyek = '" + namaProyek + '\'' + 
			",dana = '" + dana + '\'' +
			",created_date = '" + createdDate + '\'' +
					"}";
		}
}