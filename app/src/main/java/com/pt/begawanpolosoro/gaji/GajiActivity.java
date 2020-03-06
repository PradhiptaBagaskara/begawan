package com.pt.begawanpolosoro.gaji;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.congfandi.lib.EditTextRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResponseGaji;
import com.pt.begawanpolosoro.user.api.ResponseUser;
import com.pt.begawanpolosoro.user.api.ResultItemUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.angmarch.views.SpinnerTextFormatter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GajiActivity extends AppCompatActivity {

    ImageButton back;
    ProgressBar pg;
    InitRetro initRetro;
    CurrentUser user;
    EditTextRupiah gaji;
    LinearLayout linierBtn;
    NiceSpinner list;
    String id;
    MaterialEditText catatan;
    Button btnRiwayat, btnSend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

        linierBtn = findViewById(R.id.linierBtn);
        btnSend = findViewById(R.id.btnBaru);
        btnRiwayat =findViewById(R.id.btnRiwayat);
        gaji = findViewById(R.id.gajiBaru);
        pg = findViewById(R.id.progresBaru);
        pg.setVisibility(View.GONE);
        list = findViewById(R.id.karyawan);
        catatan = findViewById(R.id.catatan);
        list.setText("PILIH KARYAWAN");
        loadUser();
        btnSend.setOnClickListener(sendGaji);
        btnRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RiwayatActivity.class);
                startActivity(i);
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View.OnClickListener  sendGaji = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(gaji.getText().toString())){
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
        Call<ResponseGaji> g = initRetro.apiRetro().postGaji(user.getsAuth(),getId(), catatan.getText().toString(), gaji.getNumber());
        g.enqueue(new Callback<ResponseGaji>() {
            @Override
            public void onResponse(Call<ResponseGaji> call, Response<ResponseGaji> response) {
                pg.setVisibility(View.GONE);
                linierBtn.setVisibility(View.VISIBLE);
                if (response.isSuccessful()){
                    if (response.body().isStatus()){

                    }
                }
                Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseGaji> call, Throwable t) {
                pg.setVisibility(View.GONE);
                linierBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan! Cobalagi nanti", Toast.LENGTH_SHORT).show();
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
}
