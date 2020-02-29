package com.pt.begawanpolosoro.proyek.api;

import com.google.gson.annotations.SerializedName;

public class TransaksiItem{

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("role")
	private String role;

	@SerializedName("nama")
	private String nama;

	@SerializedName("jenis")
	private String jenis;

	@SerializedName("nama_transaksi")
	private String namaTransaksi;

	@SerializedName("saldo")
	private String saldo;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("id")
	private String id;

	@SerializedName("nama_proyek")
	private String namaProyek;

	@SerializedName("dana")
	private String dana;

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
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
			"TransaksiItem{" + 
			"keterangan = '" + keterangan + '\'' + 
			",role = '" + role + '\'' + 
			",nama = '" + nama + '\'' + 
			",jenis = '" + jenis + '\'' + 
			",nama_transaksi = '" + namaTransaksi + '\'' + 
			",saldo = '" + saldo + '\'' + 
			",created_date = '" + createdDate + '\'' + 
			",id = '" + id + '\'' + 
			",nama_proyek = '" + namaProyek + '\'' + 
			",dana = '" + dana + '\'' + 
			"}";
		}
}