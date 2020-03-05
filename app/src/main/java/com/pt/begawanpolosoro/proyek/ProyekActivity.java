package com.pt.begawanpolosoro.proyek;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.congfandi.lib.EditTextRupiah;
import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.MainActivity;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.proyek.api.ResultItemProyek;
import com.pt.begawanpolosoro.proyek.api.TransaksiItem;
import com.pt.begawanpolosoro.transaksi.TxDetailActivity;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProyekActivity extends AppCompatActivity {
    ApiService apiService;
    CurrentUser user;
    TextView namaProyek, catatan, noneAktifitas;
    TextViewRupiah modal, total, sisa;
    ImageButton back,edit,simpan,cancel,delete;
    LinearLayout showEdit, hideEdit, menuBtn;
    ProgressBar progres;

    public String getsNamaProyek() {
        return sNamaProyek;
    }

    public void setsNamaProyek(String sNamaProyek) {
        this.sNamaProyek = sNamaProyek;
    }

    public String getsModal() {
        return sModal;
    }

    public void setsModal(String sModal) {
        this.sModal = sModal;
    }

    public String getsCatatan() {
        return sCatatan;
    }

    public void setsCatatan(String sCatatan) {
        this.sCatatan = sCatatan;
    }

    String sNamaProyek, sModal, sCatatan;

    EditTextRupiah eModal;
    EditText eCatatan, eNamaProyek;
    RecyclerView recyclerView;
    InitRetro initRetro;


    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyek);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.darkBlue));

        Bundle extra = getIntent().getExtras();
//        id = "2";
        if (!extra.isEmpty()){
            id = extra.getString("id_proyek");
        }
        delete = findViewById(R.id.deleteBtn);
        recyclerView = findViewById(R.id.proyek_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        showEdit = findViewById(R.id.showEdit);
        hideEdit = findViewById(R.id.show);
        progres = findViewById(R.id.progres);
        eModal = findViewById(R.id.editmodal);
        eCatatan = findViewById(R.id.editcatatan);
        eNamaProyek = findViewById(R.id.editnamaProyek);
        menuBtn = findViewById(R.id.linierBtn);
        cancel = findViewById(R.id.cancelBtn);
        noneAktifitas = findViewById(R.id.noneAktifitas);

        initRetro = new InitRetro(getApplicationContext());
        apiService = initRetro.InitApi().create(ApiService.class);
        user= new CurrentUser(getApplicationContext());
        setHideEdit();


        namaProyek = findViewById(R.id.namaProyek);
        modal = findViewById(R.id.modal);
        total = findViewById(R.id.totalPengeluaran);
        sisa = findViewById(R.id.sisaModal);
        catatan = findViewById(R.id.catatan);
        back = findViewById(R.id.back);
        edit = findViewById(R.id.editBtn);
        simpan = findViewById(R.id.simpanBtn);
        delete.setOnClickListener(del);

        setEdit();
        setSimpan();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("halaman", "1");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        loadProyek();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("halaman", "1");
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finishAffinity();

    }

    private View.OnClickListener del = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProyekActivity.this);
            alertDialog.setIcon(R.drawable.ic_delete);

            alertDialog.setTitle("PERINGATAN!");
            alertDialog
                    .setMessage("Jika anda menghapus proyek ini maka semua DATA TRANSAKSI yang berhubungan dengan proyek akan ikut terhapus! \nklik YA untuk melanjutkan klik TIDAK untuk membatalkan")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // jika tombol diklik, maka akan menutup activity ini
//                            MainActivity.this.finish();
                            setDelete();
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
            alertDialog.show();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    void setDelete(){
//        Toast.makeText(getApplicationContext(), "dialog alert", Toast.LENGTH_SHORT).show();
        RequestBody auth = RequestBody.create(MediaType.parse("text/plain"), user.getsAuth());
        RequestBody pId = RequestBody.create(MediaType.parse("text/plain"), id);
        Call<ResponseProyek> p = initRetro.apiRetro().deleteProyekId(user.getsAuth(), id);
        p.enqueue(new Callback<ResponseProyek>() {
            @Override
            public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        Toast.makeText(getApplicationContext(),response.body().getMsg(), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {

            }
        });
    }

    void setHideEdit(){
        progres.setVisibility(View.GONE);
        showEdit.setVisibility(View.GONE);
        hideEdit.setVisibility(View.VISIBLE);
        loadProyek();

    }
    void setShowEdit(){
        showEdit.setVisibility(View.VISIBLE);
        hideEdit.setVisibility(View.GONE);
        eNamaProyek.setText(getsNamaProyek());
        eNamaProyek.setTextSize(14);
        eCatatan.setText(getsCatatan());
        eCatatan.setTextSize(14);
        eModal.setText(getsModal());
        eModal.setTextSize(14);
        progres.setVisibility(View.GONE);

    }
    void setEdit(){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowEdit();
            }
        });
    }
    void setSimpan(){

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setHideEdit();
                menuBtn.setVisibility(View.GONE);
                progres.setVisibility(View.VISIBLE);
                Call<ResponseProyek> update = apiService.updateProyek(user.getsAuth(),id, eNamaProyek.getText().toString(),
                        "update",eCatatan.getText().toString(),eModal.getNumber());
                update.enqueue(new Callback<ResponseProyek>() {
                    @Override
                    public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
                        if (response.isSuccessful()) {
                            if (response.body().isStatus()) {
                                setHideEdit();
                            }
                        }
                        menuBtn.setVisibility(View.VISIBLE);
                        setHideEdit();
                    }

                    @Override
                    public void onFailure(Call<ResponseProyek> call, Throwable t) {
                        t.printStackTrace();
                        progres.setVisibility(View.GONE);
                        menuBtn.setVisibility(View.VISIBLE);
                    }
                });
//                Toast.makeText(getApplicationContext(), eModal.getNumber().toString(), Toast.LENGTH_LONG).show();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHideEdit();
            }
        });

    }

    void loadProyek(){
        final Call<ResponseProyek> res = apiService.getProyekId(user.getsAuth(), id);
        res.enqueue(new Callback<ResponseProyek>() {
            @Override
            public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
//                Log.d("res", "onResponse: "+ response.body().getMsg());
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        List<ResultItemProyek> proyek = response.body().getResult();
                        setsNamaProyek(proyek.get(0).getNamaProyek());
                        setsModal(proyek.get(0).getModal());
                        setsCatatan(proyek.get(0).getKeterangan());
                        namaProyek.setText(proyek.get(0).getNamaProyek());
                        modal.convertToIDR(proyek.get(0).getModal());
                        total.convertToIDR(proyek.get(0).getTotalDana());
                        sisa.convertToIDR(proyek.get(0).getSisaModal());
                        catatan.setText(proyek.get(0).getKeterangan());
                        if (response.body().getTransaksi() != null){
                            List<TransaksiItem> tx = response.body().getTransaksi();
                            UserAdapter adapter = new UserAdapter(tx);
                            recyclerView.setAdapter(adapter);
                            noneAktifitas.setVisibility(View.GONE);

                        }else {
                            noneAktifitas.setVisibility(View.VISIBLE);

                        }
                    }

                }



            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {

                setHideEdit();
//                Snackbar.make(getApplicationContext(), "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

        List<TransaksiItem> proyeItem, proyekFiltered;

        public UserAdapter(List<TransaksiItem> resultItemProyeks){
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
            holder.mTx.setText(proyekFiltered.get(position).getNama());
            holder.mTgl.setText(proyekFiltered.get(position).getCreatedDate());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ProyekActivity.this, TxDetailActivity.class);
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
