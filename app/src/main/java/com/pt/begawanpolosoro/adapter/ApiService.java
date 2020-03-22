package com.pt.begawanpolosoro.adapter;


import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.login.api.ResponseLogin;
import com.pt.begawanpolosoro.pdf.api.ResponsePdf;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResponseGaji;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResponseStatusGaji;
import com.pt.begawanpolosoro.proyek.api.ResponseInsertProyek;
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.update.ResponseUpdate;
import com.pt.begawanpolosoro.user.api.ResponseUser;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
                                        @Field("param") String param,
                                        @Field("keterangan") String Catatan);

    @Multipart
    @POST("api/saldo")
    Call<ResponseSaldo> updateSaldoUser (@Part("auth_key") RequestBody auth_key,
                                        @Part("id") RequestBody id,
                                        @Part("saldo") RequestBody saldo,
                                        @Part("param") RequestBody param,
                                        @Part MultipartBody.Part file);
    @GET("api/transaksi")
    Call<ResponseTx> txApi (@Query("auth_key") String auth);

    @Multipart
    @POST("api/transaksi")
    Call<ResponseTx> postTx (@Part("auth_key") RequestBody auth,
                             @Part("id_proyek") RequestBody id_proyek,
                             @Part("nama") RequestBody nama,
                             @Part("dana") RequestBody dana,
                             @Part("keterangan") RequestBody keterangan,
                             @Part("jenis") RequestBody jenis,
                             @Part MultipartBody.Part file);


    @GET("api/user")
    Call<ResponseUser> getUser(@Query("auth_key") String auth);

    @FormUrlEncoded
    @POST("api/delete")
    Call<ResponseLogin> deleteUser (@Field("auth_key") String auth,
                                    @Field("id") String id,
                                    @Field("param") String Param);

    @FormUrlEncoded
    @POST("api/hutang")
    Call<ResponseSaldo> hutang (@Field("auth_key") String auth,
                                @Field("id") String id,
                                @Field("jumlah") String jumlah,
                                @Field("param") String Param);

    @GET("api/proyek")
    Call<ResponseProyek> getProyek(@Query("auth_key") String auth);

    @GET("api/version")
    Call<ResponseUpdate> getUpdate(@Query("param") String param);

    @GET("api/proyek")
    Call<ResponseProyek> getProyekId(@Query("auth_key") String auth,
                                     @Query("id") String id);

    @FormUrlEncoded
    @POST("api/proyek")
    Call<ResponseProyek> deleteProyekId(@Field("auth_key") String auth,
                                     @Field("id") String  id,
                                        @Field("param") String param);
    @FormUrlEncoded
    @POST("api/proyek")
    Call<ResponseProyek> updateNilaiProyek(@Field("auth_key") String auth,
                                      @Field("id") String id,
                                      @Field("aksi") String aksi,
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
                                            @Field("mulai") String TglMulai,
                                            @Field("selesai") String TglSelesai,
                                            @Field("modal") String modal);
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
    @Multipart
    @POST("api/gaji")
    Call<ResponseGaji> postGaji (@Part("auth_key") RequestBody auth,
                               @Part("id") RequestBody id,
                               @Part("keterangan") RequestBody catatan,
                               @Part("gaji") RequestBody gaji,
                                 @Part("id_proyek") RequestBody idProyek,
                                 @Part MultipartBody.Part file
                                 );


}
