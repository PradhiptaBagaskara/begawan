package com.pt.begawanpolosoro.adapter;


import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.login.api.ResponseLogin;
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

    @GET("api/user")
    Call<ResponseUser> getUser(@Query("auth_key") String auth);

    @GET("api/proyek")
    Call<ResponseProyek> getProyek(@Query("auth_key") String auth);

    @GET("api/proyek")
    Call<ResponseProyek> getProyekId(@Query("auth_key") String auth,
                                     @Query("id") String id);

    @FormUrlEncoded
    @POST("api/proyek")
    Call<ResponseProyek> updateProyek(@Field("auth_key") String auth,
                                     @Field("id") String id,
                                      @Field("nama_proyek") String namaProyek,
                                      @Field("param") String param,
                                      @Field("keterangan") String catatan,
                                      @Field("modal") String modal);
}
