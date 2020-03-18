package com.pt.begawanpolosoro.util;

public class ApiHelper {

    private  String saldo;
    private  String modal;
    private  String role;
    private  String nama_proyek;
    private  String id_proyek;
    private  String id_tx;
    private  String id_;
    private String tglMulai;
    private String tglSeleseai;
    private String fname;
    private String param;
    private String status;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    String imgPath;
    String imgUrl;
    String penerima;
    String pengirim;
    String nama;

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    ApiHelper instance = null;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    private String keterangan;

    public ApiHelper() {
    }

    public String getTglMulai() {
        return tglMulai;
    }

    public void setTglMulai(String tglMulai) {
        this.tglMulai = tglMulai;
    }

    public String getTglSelesai() {
        return tglSeleseai;
    }

    public void setTglSelesai(String tglSeleseai) {
        this.tglSeleseai = tglSeleseai;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNama_proyek() {
        return nama_proyek;
    }

    public void setNama_proyek(String nama_proyek) {
        this.nama_proyek = nama_proyek;
    }

    public String getId_proyek() {
        return id_proyek;
    }

    public void setId_proyek(String id_proyek) {
        this.id_proyek = id_proyek;
    }

    public String getId_tx() {
        return id_tx;
    }

    public void setId_tx(String id_tx) {
        this.id_tx = id_tx;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public ApiHelper getInstance() {
        return instance;
    }

    public void setInstance(ApiHelper instance) {
        this.instance = instance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
