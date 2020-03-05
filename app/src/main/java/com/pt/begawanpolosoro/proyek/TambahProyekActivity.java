package com.pt.begawanpolosoro.proyek;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.congfandi.lib.EditTextRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.proyek.api.ResponseInsertProyek;
import com.pt.begawanpolosoro.proyek.api.ResultInsertProyek;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahProyekActivity extends AppCompatActivity {
    MaterialEditText nama,keterangan;
    Button btn;
    ProgressBar pg;
    EditTextRupiah modal;
    ImageButton back;
    ApiService apiService;
    CurrentUser user;

    public String getCtt() {
        return ctt;
    }

    public void setCtt(String ctt) {
        this.ctt = ctt;
    }

    String ctt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_proyek);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));

        InitRetro initRetro = new InitRetro(getApplicationContext());
        user = new CurrentUser(getApplicationContext());
        apiService = initRetro.InitApi().create(ApiService.class);

        nama = findViewById(R.id.namaProyek);
        modal = findViewById(R.id.modalProyek);
        keterangan = findViewById(R.id.keteranganProyek);
        btn = findViewById(R.id.btnBaru);
        pg = findViewById(R.id.progresBaru);
        pg.setVisibility(View.GONE);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(sendProyek);


    }
    private Boolean errorForm(){
        if (TextUtils.isEmpty(keterangan.getText().toString())){
            setCtt("Tidak ada catatan");
        }else {
            setCtt(keterangan.getText().toString());
        }
        if (TextUtils.isEmpty(nama.getText().toString())){
            nama.setError("Nama Harus di Isi");
            return false;
        }else if (TextUtils.isEmpty(modal.getNumber())){
            modal.setError("Modal Harus di isi ");
            return false;

        }
//
        return true;


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private View.OnClickListener sendProyek = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (errorForm()){
                btn.setVisibility(View.GONE);
                pg.setVisibility(View.VISIBLE);
                Call<ResponseInsertProyek> p = apiService.insertProyek(user.getsAuth(),"0",nama.getText().toString(), "insert",getCtt(),modal.getNumber());
                p.enqueue(new Callback<ResponseInsertProyek>() {
                    @Override
                    public void onResponse(Call<ResponseInsertProyek> call, Response<ResponseInsertProyek> response) {
                        btn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                ResultInsertProyek result = response.body().getResult();
                                Intent intent = new Intent(getApplicationContext(), ProyekActivity.class);
                                intent.putExtra("id_proyek", result.getId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseInsertProyek> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Terjadi  Kesalahan", Toast.LENGTH_SHORT).show();

                        btn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        t.printStackTrace();
                    }
                });
            }

        }
    };
}
