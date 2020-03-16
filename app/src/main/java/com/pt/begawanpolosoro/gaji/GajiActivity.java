package com.pt.begawanpolosoro.gaji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.congfandi.lib.EditTextRupiah;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.DownloadUtil;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResponseGaji;
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.proyek.api.ResultItemProyek;
import com.pt.begawanpolosoro.user.api.ResponseUser;
import com.pt.begawanpolosoro.user.api.ResultItemUser;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.angmarch.views.SpinnerTextFormatter;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GajiActivity extends AppCompatActivity {

    private static final String TAG = "GajiActivity";
    ImageButton back;
    ProgressBar pg;
    InitRetro initRetro;
    CurrentUser user;
    EditTextRupiah gaji;
    LinearLayout linierBtn;
    NiceSpinner list,opsi;
    String id;
    MaterialEditText catatan;
    Button btnRiwayat, btnSend;
    AppCompatImageView img;
    FloatingActionButton uploadImg;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    ApiHelper apiHelper = new ApiHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaji);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));
        setId("");

        initRetro = new InitRetro(getApplicationContext());
        user = new CurrentUser(getApplicationContext());

        uploadImg = findViewById(R.id.fab_img);

        img = findViewById(R.id.imgCamera);
        linierBtn = findViewById(R.id.linierBtn);
        btnSend = findViewById(R.id.btnBaru);
        btnRiwayat =findViewById(R.id.btnRiwayat);
        gaji = findViewById(R.id.gajiBaru);
        pg = findViewById(R.id.progresBaru);
        pg.setVisibility(View.GONE);
        list = findViewById(R.id.karyawan);
        catatan = findViewById(R.id.catatan);
        opsi = findViewById(R.id.proyek);
        list.setText("PILIH KARYAWAN");
        loadUser();
        loadProyek();
        btnSend.setOnClickListener(sendGaji);
        btnRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RiwayatActivity.class);
                startActivity(i);
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: "+resultCode+"-"+requestCode);
        if (resultCode == Activity.RESULT_OK){
            File dt = ImagePicker.Companion.getFile(data);
            String path = ImagePicker.Companion.getFilePath(data);
            apiHelper.setImgPath(path);
            Picasso.with(this)
                    .load(dt)
                    .into(img);
            img.setScaleType(ImageView.ScaleType.FIT_XY);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                    downloadUtil.showImg(dt);
                }
            });


        }
    }


    public void pickImg(View v){
        ImagePicker.Companion.with(this)
//                .cropSquare()
                .compress(256)
                .maxResultSize(1080, 1080)
                .start();
    }

    private View.OnClickListener  sendGaji = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(apiHelper.getImgPath())){
                Snackbar.make(img, "Gambar Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();
            }else if (TextUtils.isEmpty(gaji.getText().toString())){
                gaji.setError("Gaji tidak boleh kosong!");
            }else if (TextUtils.isEmpty(getId())){
                list.setError("Pilih Karyawan");
            }else {
                kirim();
            }
        }
    };

    void kirim(){
        pg.setVisibility(View.VISIBLE);
        linierBtn.setVisibility(View.GONE);
        apiHelper.setKeterangan(catatan.getText().toString());
        apiHelper.setSaldo(gaji.getNumber());
        File file = new File(apiHelper.getImgPath());
        RequestBody auth = RequestBody.create(MediaType.parse("text/plain"), user.getsAuth());
        RequestBody idProyek = RequestBody.create(MediaType.parse("text/plain"), apiHelper.getId_proyek());
        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), getId());
        RequestBody cat = RequestBody.create(MediaType.parse("text/plain"), apiHelper.getKeterangan());
        RequestBody pGaji = RequestBody.create(MediaType.parse("text/plain"), apiHelper.getSaldo());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part poto = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Call<ResponseGaji> g = initRetro.apiRetro().postGaji(auth,idUser,cat,pGaji,idProyek,poto);
        g.enqueue(new Callback<ResponseGaji>() {
            @Override
            public void onResponse(Call<ResponseGaji> call, Response<ResponseGaji> response) {
                pg.setVisibility(View.GONE);
                linierBtn.setVisibility(View.VISIBLE);
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        Intent i = new Intent(GajiActivity.this, RiwayatActivity.class);
                        startActivity(i);

                    }
                }
                Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseGaji> call, Throwable t) {
                pg.setVisibility(View.GONE);
                linierBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Koneksi Terganggu! Cek Manual Riwayat Gaji", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(GajiActivity.this, RiwayatActivity.class);
                startActivity(i);
                t.printStackTrace();
            }
        });

    };

    void loadUser(){
        Call<ResponseUser> u = initRetro.apiRetro().getUser(user.getsAuth());
        u.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        if (response.body().getResult() != null){
                            List<ResultItemUser> resultItemUser = response.body().getResult();

                            SpinnerTextFormatter format = new SpinnerTextFormatter<ResultItemUser>() {
                                @Override
                                public Spannable format(ResultItemUser item) {
                                    return new SpannableString(item.getNama());
                                }
                            };
                            list.setSpinnerTextFormatter(format);
                            list.setSelectedTextFormatter(format);
                            list.attachDataSource(resultItemUser);
                            ResultItemUser item = (ResultItemUser) list.getSelectedItem();
                            setId(item.getId());
                            if (resultItemUser.size() == 1){
                                list.setText(item.getNama());

                            }
                            list.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                                @Override
                                public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                                    ResultItemUser res = (ResultItemUser) list.getSelectedItem();
                                    setId(res.getId());
                                }
                            });
                        }else {
                            list.setText("BELUM ADA DATA");
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan! Coba lagi nanti", Toast.LENGTH_LONG).show();

                linierBtn.setVisibility(View.VISIBLE);
                t.printStackTrace();

            }
        });
    }

    void loadProyek (){
        Call<ResponseProyek> p = initRetro.apiRetro().getProyek(user.getsAuth());
        p.enqueue(new Callback<ResponseProyek>() {
            @Override
            public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        if (response.body().getResult().size() != 0){
                            List<ResultItemProyek> resultItemProyek = response.body().getResult();

                            SpinnerTextFormatter formater = new SpinnerTextFormatter<ResultItemProyek>() {
                                @Override
                                public Spannable format(ResultItemProyek item) {
                                    return new SpannableString(item.getNamaProyek());
                                }
                            };
                            opsi.setSpinnerTextFormatter(formater);
                            opsi.setSelectedTextFormatter(formater);
                            opsi.attachDataSource(resultItemProyek);
                            ResultItemProyek item = (ResultItemProyek) opsi.getSelectedItem();
                            apiHelper.setId_proyek(item.getId());
                            if ( response.body().getResult().size() == 1){
                                opsi.setText(item.getNamaProyek());
                            }
                            opsi.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                                @Override
                                public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                                    ResultItemProyek result = (ResultItemProyek) opsi.getSelectedItem();
                                    apiHelper.setId_proyek(result.getId());
//                Toast.makeText(getApplicationContext(), result.getId()+" "+result.getNamaProyek(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }




                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {

            }
        });
    }

}
