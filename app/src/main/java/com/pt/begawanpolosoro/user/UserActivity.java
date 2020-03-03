package com.pt.begawanpolosoro.user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.congfandi.lib.EditTextRupiah;
import com.congfandi.lib.TextViewRupiah;
import com.google.android.material.snackbar.Snackbar;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.MainActivity;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.ResponseTx;
import com.pt.begawanpolosoro.adapter.ResultItemTx;
import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.transaksi.TxDetailActivity;
import com.pt.begawanpolosoro.user.api.ResponseUser;

import java.util.List;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public int getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = Integer.parseInt(role);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    final public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String passwordDefault = "123456";

    String username;
    String saldo;
    int role;
    String nama;
    String id;
    Dialog userForm;

    Button share, reset;

    TextView vUsername, vNama, vAktifitas;
    TextViewRupiah vSaldo;
    ApiService apiService;
    ImageButton back,logout, addSaldo, saveSaldo, cancel,setting;
    ProgressBar pg, pgReset;

    RecyclerView recyclerView;
    CurrentUser user;
    EditTextRupiah updateSaldo;
    CardView cardDialog;
    CoordinatorLayout activity;

    String hal;
    Intent intent = getIntent();

    public String getHal() {
        return hal;
    }

    public void setHal(String hal) {
        this.hal = hal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.darkBlue));
        activity = findViewById(R.id.userActivity);

        apiService = InitRetro.InitApi().create(ApiService.class);
        user = new CurrentUser(getApplicationContext());
        customDialog();
        if (intent.hasExtra("halaman")){
            setHal(intent.getStringExtra("halaman"));
        }else {
            setHal("0");
        }
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("halaman", getHal());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        addSaldo =findViewById(R.id.addSaldo);
        saveSaldo =findViewById(R.id.saveSaldo);
        saveSaldo.setVisibility(View.GONE);
        pg = findViewById(R.id.progresSaldo);
        updateSaldo = findViewById(R.id.updateSaldo);
        updateSaldo.setVisibility(View.GONE);
        cancel = findViewById(R.id.cancelBtn);
        cancel.setVisibility(View.GONE);
        setting =findViewById(R.id.userSetting);
        setting.setOnClickListener(showCustomDialog);



        pg.setVisibility(View.GONE);



        Bundle extra = getIntent().getExtras();
        if (!extra.isEmpty()){
            setId(extra.getString("id"));
            setNama(extra.getString("nama"));
            setSaldo(extra.getString("saldo"));
            setUsername(extra.getString("username"));
            setRole(extra.getString("role"));

        }

        vNama = findViewById(R.id.nama);
//        vUsername = findViewById(R.id.username);
        vSaldo = findViewById(R.id.saldo);
        vAktifitas = findViewById(R.id.noneAktifitas);

        vNama.setText(getNama().toUpperCase());
        addSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddSaldo();
            }
        });
        saveSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSaveSaldo();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSaldo();
            }
        });
        vSaldo.convertToIDR(getSaldo());
        recyclerView = findViewById(R.id.tx_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadSaldo();
        if (getRole() > 0){
            Log.d("ll", "onCreate: "+getRole());

        }else{
            loadTx();


        }


    }


    private View.OnClickListener showCustomDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            userForm.show();

            share.setOnClickListener(sharedUser);
            reset.setOnClickListener(resetPass);

//
//            addVideo.setOnClickListener(addVideoFb);
        }
    };

    private View.OnClickListener resetPass = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            reset.setVisibility(View.GONE);
            pgReset.setVisibility(View.VISIBLE);
            Call<ResponseUser> pas = apiService.resetPassword(user.getsAuth(), getId(),passwordDefault);
            pas.enqueue(new Callback<ResponseUser>() {
                @Override
                public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                    reset.setVisibility(View.VISIBLE);
                    pgReset.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body().isStatus()){
                            Snackbar.make(activity, response.body().getMsg(), Snackbar.LENGTH_LONG).show();

                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseUser> call, Throwable t) {
                    reset.setVisibility(View.VISIBLE);
                    pgReset.setVisibility(View.GONE);
                }
            });

        }
    };

    private View.OnClickListener sharedUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Share2.Builder(UserActivity.this)
                    .setContentType(ShareContentType.TEXT)
                    // 设置要分享的文本内容
                    .setTextContent("Selamat " + getNama() +"! Login anda telah di buat.\n \nUsername: " +getUsername()+"\nPassword: "+ passwordDefault
                    + "\n\nDownload Aplikasi di https://begawanpolosoro.com/app/begawan.apk")
                    .setTitle("Login Aplikasi "+ getResources().getString(R.string.app_name))
                    .build()
                    .shareBySystem();
        }
    };

    void setAddSaldo(){
        pg.setVisibility(View.GONE);
        vSaldo.setVisibility(View.GONE);
        addSaldo.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        saveSaldo.setVisibility(View.VISIBLE);
        updateSaldo.setVisibility(View.VISIBLE);
//        updateSaldo.setText(getSaldo());


    }


    void setSaveSaldo(){
        saveSaldo.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        pg.setVisibility(View.VISIBLE);
        Call<ResponseSaldo> saldoCall = apiService.updateSaldoApi(user.getsAuth(), getId(), updateSaldo.getNumber(), "tambah");
        saldoCall.enqueue(new Callback<ResponseSaldo>() {
            @Override
            public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {
                pg.setVisibility(View.GONE);
                loadSaldo();


                if (response.isSuccessful()){

                    if (response.body().isStatus()){

                    }else {
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan!", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseSaldo> call, Throwable t) {
                pg.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan!", Toast.LENGTH_LONG).show();

                loadSaldo();
            }
        });

    }
    void loadSaldo(){
        pg.setVisibility(View.GONE);
        vSaldo.setVisibility(View.VISIBLE);
        addSaldo.setVisibility(View.VISIBLE);
        updateSaldo.setVisibility(View.GONE);
        saveSaldo.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        Call<ResponseSaldo> apiSaldo = apiService.saldoApi(getId());
        apiSaldo.enqueue(new Callback<ResponseSaldo>() {
            @Override
            public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {
                if (response.isSuccessful()){
                    ResponseSaldo res = response.body();
                    if (res.isStatus()){
                        if (res.getResult() != null){
                            setSaldo(res.getResult().getSaldo());
                            vSaldo.convertToIDR(getSaldo());

                        }

                    }else {
                        Log.d("tagger: ", res.getMsg());
//                        vAktifitas
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSaldo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void loadTx(){
        Call<ResponseTx> TxApi = apiService.txApi(getId());
        TxApi.enqueue(new Callback<ResponseTx>() {
            @Override
            public void onResponse(Call<ResponseTx> call, Response<ResponseTx> response) {
                if (response.isSuccessful()){
                    ResponseTx res = response.body();
                    if (res.isStatus()){
                        if (res.getResult() != null){
//                            Log.d("tagger: ", res.getResult().toString());

                            List<ResultItemTx> TxItem = res.getResult();
                            UserAdapter Tadapter = new UserAdapter(TxItem);
                            recyclerView.setAdapter(Tadapter);
                            vAktifitas.setVisibility(View.GONE);

                        }

                    }else {
                        recyclerView.setVisibility(View.GONE);
                        Log.d("tagger: ", res.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTx> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }



    private void customDialog() {
        userForm = new Dialog(UserActivity.this);
        userForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userForm.setContentView(R.layout.dialog_user_edit);
        userForm.setCancelable(true);
        userForm.getWindow().setBackgroundDrawableResource(R.color.transparant);
        userForm.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pgReset = userForm.findViewById(R.id.progresReset);
        pgReset.setVisibility(View.GONE);
        share = userForm.findViewById(R.id.shareUser);
        reset = userForm.findViewById(R.id.resetPassword);
        cardDialog = userForm.findViewById(R.id.cardDialog);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("halaman", getHal());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

        List<ResultItemTx> proyeItem, proyekFiltered;

        public UserAdapter(List<ResultItemTx> resultItemProyeks){
            this.proyeItem = resultItemProyeks;
            this.proyekFiltered = resultItemProyeks;
        }

        public class UserViewHolder extends RecyclerView.ViewHolder{
            public  TextView mTx, mNama,mTgl;
            public TextViewRupiah mDana;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                mDana = itemView.findViewById(R.id.txDana);
                mTx = itemView.findViewById(R.id.nama_proyek);
                mNama = itemView.findViewById(R.id.pembelian);
                mTgl = itemView.findViewById(R.id.txTgl);

            }
        }



        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txby_user, parent, false);
            UserViewHolder userViewHolder = new UserViewHolder(mView);

            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {
            holder.mDana.convertToIDR(proyekFiltered.get(position).getDana());
            holder.mNama.setText(proyekFiltered.get(position).getNamaTransaksi());
            holder.mTx.setText(proyekFiltered.get(position).getNamaProyek());
            holder.mTgl.setText(proyekFiltered.get(position).getCreatedDate());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(UserActivity.this, TxDetailActivity.class);
                    intent.putExtra("id", proyekFiltered.get(position).getId());
                    intent.putExtra("nama", proyekFiltered.get(position).getNama());
                    intent.putExtra("nama_tx", proyekFiltered.get(position).getNamaTransaksi());
                    intent.putExtra("nama_proyek", proyekFiltered.get(position).getNamaProyek());
                    intent.putExtra("jenis_bayar", proyekFiltered.get(position).getJenis());
                    intent.putExtra("dana", proyekFiltered.get(position).getDana());
                    intent.putExtra("keterangan", proyekFiltered.get(position).getKeterangan());
                    intent.putExtra("waktu", proyekFiltered.get(position).getCreatedDate());
                    startActivity(intent);


                }
            });



        }

        @Override
        public int getItemCount() {
            return proyekFiltered.size();
        }
    }
}
