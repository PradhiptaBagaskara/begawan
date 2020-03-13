package com.pt.begawanpolosoro.user;

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
import com.pt.begawanpolosoro.login.api.ResponseLogin;
import com.pt.begawanpolosoro.login.api.ResultLogin;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahUserActivity extends AppCompatActivity {
    private static final String TAG = "TambahUserActivity";
    String Snama;
    String SuserName;
    String Srole;
    String Ssaldo;


    public String getSnama() {
        return Snama;
    }

    public void setSnama(String snama) {
        Snama = snama;
    }

    public String getSuserName() {
        return SuserName;
    }

    public void setSuserName(String suserName) {
        SuserName = suserName;
    }

    public String getSrole() {
        return Srole;
    }

    public void setSrole(String srole) {
        Srole = srole;
    }

    public String getSsaldo() {
        return Ssaldo;
    }

    public void setSsaldo(String ssaldo) {
        Ssaldo = ssaldo;
    }

    EditTextRupiah saldo;
    MaterialEditText namaUser;
    NiceSpinner role;
    ImageButton back;
    ApiService apiService;
    CurrentUser user;
    ProgressBar pg;
    Button btnSend;
    ApiHelper apiHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tambah_user);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));
        setSrole("0");
        InitRetro initRetro = new InitRetro(getApplicationContext());
        apiService = initRetro.InitApi().create(ApiService.class);
        user = new CurrentUser(getApplicationContext());

        role = (NiceSpinner) findViewById(R.id.roleBaru);
        namaUser = findViewById(R.id.namaBaru);
        saldo = findViewById(R.id.saldoBaru);
        back = findViewById(R.id.back);
        pg = findViewById(R.id.progresBaru);
        pg.setVisibility(View.GONE);
        btnSend = findViewById(R.id.btnBaru);


        back.setOnClickListener(v -> {
                finish();
        });
        setSrole("0");


        List<String> dataset = new LinkedList<>(Arrays.asList("KARYAWAN", "PELAKSANA", "ADMINISTRATOR"));
        role.attachDataSource(dataset);
        role.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            switch (position){
                case 0:
                    setSrole("0");

                    break;
                case  1:
                    setSrole("1");
                    break;
                case 2:
                    setSrole("3");

            }
        });

        btnSend.setOnClickListener(send);






    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private  View.OnClickListener send = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (errorForm()){
                btnSend.setVisibility(View.GONE);
                pg.setVisibility(View.VISIBLE);
                Call<ResponseLogin> p = apiService.newUser(user.getsAuth(), getSnama(),getSrole(),getSsaldo());
                p.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        btnSend.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                ResultLogin result = response.body().getResult();
                                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                                intent.putExtra("username", result.getUsername());
                                intent.putExtra("role", result.getRole());
                                intent.putExtra("saldo", result.getSaldo());
                                intent.putExtra("id", result.getId());
                                intent.putExtra("nama", result.getNama());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        btnSend.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan! Coba Lagi Nanti", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        }
    };

    private Boolean errorForm(){
        if (TextUtils.isEmpty(namaUser.getText().toString())){
            namaUser.setError("Nama Harus di Isi");
            return false;
        }else if (TextUtils.isEmpty(saldo.getNumber())){
            saldo.setError("Saldo Harus di isi ");
            return false;

        }else {
            setSnama(namaUser.getText().toString());
            setSsaldo(saldo.getNumber());
            return true;

        }
    }
}
