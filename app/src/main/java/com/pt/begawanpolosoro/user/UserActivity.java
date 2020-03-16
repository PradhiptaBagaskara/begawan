package com.pt.begawanpolosoro.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.pt.begawanpolosoro.login.api.ResponseLogin;
import com.pt.begawanpolosoro.pekerja.TambahSaldoActivity;
import com.pt.begawanpolosoro.transaksi.TxDetailActivity;
import com.pt.begawanpolosoro.user.api.ResponseUser;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.angmarch.views.NiceSpinner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
    LinearLayout linierBtn;
    Button share, reset, delete;

    TextView vUsername, vNama, vAktifitas;
    TextViewRupiah vSaldo;
    ApiService apiService;
    ImageButton back, addSaldo, setting;
    ProgressBar pg, pgReset;

    RecyclerView recyclerView;
    CurrentUser user;
    EditTextRupiah updateSaldo;
    CardView cardDialog;
    CoordinatorLayout activity;
    MaterialEditText catatan;
    NiceSpinner aksi;

    String hal;
    Intent intent;
    ApiHelper apiHelper = new ApiHelper();

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
        intent = getIntent();
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

//        pg = findViewById(R.id.progresSaldo);
//        updateSaldo = findViewById(R.id.updateSaldo);
//        updateSaldo.setVisibility(View.GONE);

        setting =findViewById(R.id.userSetting);
        setting.setOnClickListener(showCustomDialog);



//        pg.setVisibility(View.GONE);



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
                Intent intent = new Intent(UserActivity.this, TambahSaldoActivity.class);
                intent.putExtra("id", getId());
                startActivity(intent);
            }
        });

        vSaldo.convertToIDR(getSaldo());
        recyclerView = findViewById(R.id.tx_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadSaldo();
        if (getRole() > 1){
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
//            alertDialog.setIcon(R.drawable.ic_delete);

            alertDialog.setTitle("Reset Password!");
            alertDialog
                    .setMessage("Apakah anda yakin ingin mereset password?")
                    .setIcon(R.drawable.ic_warning_oren)
                    .setCancelable(true)
                    .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            linierBtn.setVisibility(View.GONE);
                            pgReset.setVisibility(View.VISIBLE);
                            Call<ResponseUser> pas = apiService.resetPassword(user.getsAuth(), getId(),passwordDefault);
                            pas.enqueue(new Callback<ResponseUser>() {
                                @Override
                                public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                                    linierBtn.setVisibility(View.VISIBLE);
                                    pgReset.setVisibility(View.GONE);
                                    userForm.dismiss();
                                    if (response.isSuccessful()){
                                        if (response.body().isStatus()){
                                            Snackbar.make(activity, response.body().getMsg(), Snackbar.LENGTH_LONG).show();

                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseUser> call, Throwable t) {
                                    linierBtn.setVisibility(View.VISIBLE);
                                    pgReset.setVisibility(View.GONE);
                                }
                            });



                        }
                    })
                    .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // jika tombol ini diklik, akan menutup dialog
                            // dan tidak terjadi apa2
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = alertDialog.create();
            dialog.show();


        }
    };

    public void deleteUser(View v){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

        alertDialog.setTitle("Hapus User");
        alertDialog
                .setMessage("User akan dihapus secara permanen dari sistem.\n\nApakah anda ingin melanjutkan?")
                .setIcon(R.drawable.ic_warning_red)
                .setCancelable(true)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        linierBtn.setVisibility(View.GONE);
                        pgReset.setVisibility(View.VISIBLE);
                        Call<ResponseLogin> pas = apiService.deleteUser(user.getsAuth(), getId(),"user");
                        pas.enqueue(new Callback<ResponseLogin>() {
                            @Override
                            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                                linierBtn.setVisibility(View.VISIBLE);
                                pgReset.setVisibility(View.GONE);
                                if (response.isSuccessful())
                                    if (response.body().isStatus())
                                        finish();
                                Toast.makeText(v.getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                                linierBtn.setVisibility(View.VISIBLE);
                                pgReset.setVisibility(View.GONE);
                                Snackbar.make(v, "Terjadi Kesalahan", Snackbar.LENGTH_LONG).show();

                            }
                        });


                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = alertDialog.create();
        dialog.show();

    }

    private View.OnClickListener sharedUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userForm.dismiss();

            String shareBody = "Selamat " + getNama() +"! Login anda telah di buat.\n \nUsername: " +getUsername()+"\nPassword: "+ passwordDefault
                    + "\n\nDownload Aplikasi di https://begawanpolosoro.com/app/download";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Bagikan dengan "));

        }
    };





    void loadSaldo(){

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
        aksi = userForm.findViewById(R.id.aksi);
        linierBtn = userForm.findViewById(R.id.linierBtn);
        delete = userForm.findViewById(R.id.deleteBtn);
        apiHelper.setParam("tambah");
        Log.d(TAG, "customDialog: "+apiHelper.getParam());
        cardDialog = userForm.findViewById(R.id.cardDialog);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSaldo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("halaman", getHal());
        startActivity(i);
        finishAffinity();
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
                    intent.putExtra("data", proyekFiltered.get(position));
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
