package com.pt.begawanpolosoro.adapter;


import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.login.api.ResponseLogin;
import com.pt.begawanpolosoro.pdf.api.ResponsePdf;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResponseGaji;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResponseStatusGaji;
import com.pt.begawanpolosoro.proyek.api.ResponseInsertProyek;
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.user.api.ResponseUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("api/login")
    Call<ResponseLogin> authLogin (@Field("username") String username,
                                   @Field("password") String Password);

    @GET("api/saldo")
    Call<ResponseSaldo> saldoApi (@Query("auth_key") String auth_key);

    @FormUrlEncoded
    @POST("api/saldo")
    Call<ResponseSaldo> updateSaldoApi (@Field("auth_key") String auth_key,
                                        @Field("id") String id,
                                        @Field("saldo") String saldo,
                                        @Field("param") String param);

    @GET("api/transaksi")
    Call<ResponseTx> txApi (@Query("auth_key") String auth);

    @FormUrlEncoded
    @POST("api/transaksi")
    Call<ResponseTx> postTx (@Field("auth_key") String auth,
                             @Field("id_proyek") String id_proyek,
                             @Field("nama") String nama,
                             @Field("dana") String dana,
                             @Field("keterangan") String keterangan,
                             @Field("jenis") String jenis);


    @GET("api/user")
    Call<ResponseUser> getUser(@Query("auth_key") String auth);

    @GET("api/proyek")
    Call<ResponseProyek> getProyek(@Query("auth_key") String auth);

    @GET("api/proyek")
    Call<ResponseProyek> getProyekId(@Query("auth_key") String auth,
                                     @Query("id") String id);

    @FormUrlEncoded
    @POST("api/proyek")
    Call<ResponseProyek> deleteProyekId(@Field("auth_key") String auth,
                                     @Field("id") String  id,
                                        @Field("param") String param);
    @FormUrlEncoded
    @POST("api/getpdf")
    Call<ResponsePdf> pdf(@Field("auth_key") String auth,
                          @Field("id") String  id,
                          @Field("param") String param);
    @GET("api/getpdf")
    Call<ResponseUser> getUserPdf(@Query("auth_key") String auth);

    @FormUrlEncoded
    @POST("api/password")
    Call<ResponseUser> resetPassword(@Field("auth_key") String auth_key,
                                     @Field("id") String id,
                                     @Field("password") String password);

    @FormUrlEncoded
    @POST("api/fcmtoken")
    Call<ResponseSaldo> updateToken(@Field("auth_key") String auth_key,
                                     @Field("token") String id);

    @FormUrlEncoded
    @POST("api/proyek")
    Call<ResponseProyek> updateProyek(@Field("auth_key") String auth,
                                     @Field("id") String id,
                                      @Field("nama_proyek") String namaProyek,
                                      @Field("param") String param,
                                      @Field("keterangan") String catatan,
                                      @Field("modal") String modal);
    @FormUrlEncoded
    @POST("api/proyek")
    Call<ResponseInsertProyek> insertProyek(@Field("auth_key") String auth,
                                            @Field("id") String id,
                                            @Field("nama_proyek") String namaProyek,
                                            @Field("param") String param,
                                            @Field("keterangan") String catatan,
                                            @Field("modal") String modal);
    @FormUrlEncoded
    @POST("api/editpemilik")
    Call<ResponseLogin> updateAdmin(@Field("auth_key") String auth,
                                      @Field("username") String username,
                                      @Field("nama") String nama
                                      );

    @FormUrlEncoded
    @POST("api/user")
    Call<ResponseLogin> updateUser(@Field("auth_key") String auth,
                                    @Field("password") String password,
                                    @Field("nama") String nama);
    @FormUrlEncoded
    @POST("api/user")
    Call<ResponseLogin> newUser(@Field("auth_key") String auth,
                                @Field("nama") String nama,
                                @Field("role") String role,
                                    @Field("saldo") String  Saldo);


    @GET("api/gaji")
    Call<ResponseStatusGaji> statusGaji(@Query("auth_key") String auth,
                                        @Query("param") String param);
    @GET("api/gaji")
    Call<ResponseGaji> allGaji(@Query("auth_key") String auth,
                                  @Query("param") String param,
                               @Query("limit")  String limit);
    @FormUrlEncoded
    @POST("api/gaji")
    Call<ResponseGaji> postGaji (@Field("auth_key") String auth,
                               @Field("id") String id,
                               @Field("keterangan") String catatan,
                               @Field("gaji") String gaji);


}
