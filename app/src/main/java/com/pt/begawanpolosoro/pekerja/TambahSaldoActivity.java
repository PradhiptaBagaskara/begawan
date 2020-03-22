package com.pt.begawanpolosoro.pekerja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSaldoActivity extends AppCompatActivity {
    private static final String TAG = "TambahSaldoActivity";
    ImageButton back;
    ProgressBar pg;
    InitRetro initRetro;
    CurrentUser user;
    EditTextRupiah saldo;
    LinearLayout linierBtn;
    Button btnSend;
    AppCompatImageView img;
    FloatingActionButton uploadImg;

    ApiHelper apiHelper = new ApiHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_saldo);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));
        initRetro = new InitRetro(getApplicationContext());
        user = new CurrentUser(getApplicationContext());
        Intent extra = getIntent();
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
        apiHelper.setImgPath("");
        if (extra.hasExtra("id")){
            apiHelper.setId_(extra.getStringExtra("id"));

        }else {
            finish();
        }


        img = findViewById(R.id.imgCamera);
        linierBtn = findViewById(R.id.linierBtn);
        btnSend = findViewById(R.id.btnBaru);
        saldo = findViewById(R.id.updateSaldo);
        pg = findViewById(R.id.progresBaru);
        pg.setVisibility(View.GONE);
        btnSend.setOnClickListener(update);
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

    private View.OnClickListener update = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(apiHelper.getImgPath())){
                Snackbar.make(v, "Gambar Tidak Boleh kosong", Snackbar.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(saldo.getText().toString())){
                saldo.setError("Form tidak boleh kosong!");
            }else {
                updateSald();
            }
        }
    };

    void updateSald(){
        linierBtn.setVisibility(View.GONE);
        pg.setVisibility(View.VISIBLE);
        File file = new File(apiHelper.getImgPath());
        RequestBody auth = RequestBody.create(MediaType.parse("text/plain"), user.getsAuth());
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), apiHelper.getId_());
        RequestBody saldoR = RequestBody.create(MediaType.parse("text/plain"), saldo.getNumber());
        RequestBody param = RequestBody.create(MediaType.parse("text/plain"), "tambah");
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part poto = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        Call<ResponseSaldo> saldoCall = initRetro.apiRetro().updateSaldoUser(auth, id, saldoR, param,poto);
        saldoCall.enqueue(new Callback<ResponseSaldo>() {
            @Override
            public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {
                pg.setVisibility(View.GONE);
                linierBtn.setVisibility(View.VISIBLE);


                if (response.isSuccessful()){

                    if (response.body().isStatus()){
                        finish();

                    }
                }
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();



            }

            @Override
            public void onFailure(Call<ResponseSaldo> call, Throwable t) {
                pg.setVisibility(View.GONE);
                linierBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.kesalahan), Toast.LENGTH_LONG).show();

            }
        });
    }

}
