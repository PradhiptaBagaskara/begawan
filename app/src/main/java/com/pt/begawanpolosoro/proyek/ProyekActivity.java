package com.pt.begawanpolosoro.proyek;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.proyek.api.ResultItemProyek;
import com.pt.begawanpolosoro.proyek.api.TransaksiItem;
import com.pt.begawanpolosoro.transaksi.TxDetailActivity;
import com.pt.begawanpolosoro.util.ApiHelper;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProyekActivity extends AppCompatActivity {
    ApiService apiService;
    public static final String TAG = "ProyekActivity";
    public CurrentUser user;
    public TextView namaProyek, catatan, noneAktifitas;
    public TextViewRupiah modal, total, sisa,eNamaProyek;
    public ImageButton back,edit,simpan,cancel;
    public LinearLayout detail, delProyek;
    public LinearLayout showEdit, hideEdit;
    public ProgressBar progres;

    public String getsNamaProyek() {
        return sNamaProyek;
    }

    public void setsNamaProyek(String sNamaProyek) {
        this.sNamaProyek = sNamaProyek;
    }

    public final String getsModal() {
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

    public String sNamaProyek, sModal, sCatatan;

    public EditTextRupiah eModal;
    public RecyclerView recyclerView;
    public InitRetro initRetro;
    public NiceSpinner aksi;
    public LinearLayout menuBtn;

    public View editContent,showContent;
    public EditText edtCatatan;

    public String id;
    EditModal editModal;
    public ApiHelper apiHelper = new ApiHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyek);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.darkBlue));

        Bundle extra = getIntent().getExtras();
        if (!extra.isEmpty()){
            id = extra.getString("id_proyek");
        }

        getView(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        this.initRetro = new InitRetro(ProyekActivity.this);
        this.apiService = initRetro.InitApi().create(ApiService.class);
        this.user= new CurrentUser(ProyekActivity.this);
        editModal = new EditModal(getApplicationContext());

        edit.setOnClickListener(v -> {
            showContent.setVisibility(View.GONE);
            editContent.setVisibility(View.VISIBLE);
            eModal.setText("0");
        });
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
        int role = user.getRole();
        loadProyek();

        if (user.getRole() < 2){
        }





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("halaman", "1");
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
//        finish();
//
    }

    public void getView(Activity activity){
        editContent = activity.findViewById(R.id.editContent);
        editContent.setVisibility(View.GONE);
        showContent = activity.findViewById(R.id.showContent);
        recyclerView = activity.findViewById(R.id.proyek_rec);
        showEdit = activity.findViewById(R.id.showEdit);
        hideEdit = activity.findViewById(R.id.show);
        progres = activity.findViewById(R.id.progres);
        eModal = activity.findViewById(R.id.editmodal);
        edtCatatan = activity.findViewById(R.id.editCatatan);
        eNamaProyek = activity.findViewById(R.id.editnamaProyek);
        menuBtn = activity.findViewById(R.id.menuBtn);
        cancel = activity.findViewById(R.id.cancelBtn);
        noneAktifitas = activity.findViewById(R.id.noneAktifitas);
        detail = activity.findViewById(R.id.detailBtn);
        namaProyek = activity.findViewById(R.id.namaProyek);
        modal = activity.findViewById(R.id.modal);
        total = activity.findViewById(R.id.totalPengeluaran);
        sisa = activity.findViewById(R.id.sisaModal);
        catatan = activity.findViewById(R.id.catatan);
        back = activity.findViewById(R.id.back);
        edit = activity.findViewById(R.id.editBtn);
        simpan = activity.findViewById(R.id.simpanBtn);
        aksi = activity.findViewById(R.id.aksi);
        delProyek = activity.findViewById(R.id.deleteBtn);


    }
    public void deleteProyek(View v){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProyekActivity.this);

        alertDialog.setTitle(" \rPERINGATAN!");
        alertDialog
                .setMessage("Jika anda menghapus proyek ini maka semua DATA TRANSAKSI yang berhubungan dengan proyek akan ikut terhapus! \n\nApakah Anda ingin melanjutkan?")
                .setIcon(R.drawable.ic_delete)
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


    void setDelete(){
//        menuBtn.setVisibility(View.GONE);
        progres.setVisibility(View.VISIBLE);
        Call<ResponseProyek> p = initRetro.apiRetro().deleteProyekId(user.getsAuth(), id, "delete");
        p.enqueue(new Callback<ResponseProyek>() {
            @Override
            public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
                progres.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        onBackPressed();
                    }
                }
                Toast.makeText(getApplicationContext(),response.body().getMsg(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {
                t.printStackTrace();
//                menuBtn.setVisibility(View.VISIBLE);
                progres.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Terjadi kesalahan! coba lagi Nanti", Toast.LENGTH_LONG).show();


            }
        });
    }



    public void loadProyek(){
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
                        apiHelper.setTglMulai(proyek.get(0).getTglMulai());
                        apiHelper.setId_(proyek.get(0).getId());
                        apiHelper.setTglSelesai(proyek.get(0).getTglSelesai());
                        eNamaProyek.convertToIDR(getsModal());
                        namaProyek.setText(proyek.get(0).getNamaProyek());
                        modal.convertToIDR(proyek.get(0).getModal());
                        total.convertToIDR(proyek.get(0).getTotalDana());
                        sisa.convertToIDR(proyek.get(0).getSisaModal());
                        edtCatatan.setText(proyek.get(0).getKeterangan());
                        detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), ProyekDetailActivity.class);
                                intent.putExtra("id", apiHelper.getId_());
                                intent.putExtra("nama", getsNamaProyek());
                                intent.putExtra("nilai", getsModal());
                                intent.putExtra("catatan", getsCatatan());
                                intent.putExtra("tglstart", apiHelper.getTglMulai());
                                intent.putExtra("tglend", apiHelper.getTglSelesai());
//                                intent.putExtra("data", proyek.get(0));

                                startActivity(intent);
                            }
                        });
                        if (response.body().getTransaksi() != null){
                            List<TransaksiItem> tx = response.body().getTransaksi();
                            UserAdapter adapter = new UserAdapter(tx);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            noneAktifitas.setVisibility(View.GONE);

                        }else {
                            noneAktifitas.setVisibility(View.VISIBLE);

                        }
                    }

                }



            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {

              Toast.makeText(getApplicationContext(), "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProyek();

    }

    public class EditModal extends ProyekActivity {
        ProyekActivity ac;
        Context context;

        public int getAksion() {
            return aksion;
        }

        public void setAksion(int aksion) {
            this.aksion = aksion;
        }

        private int aksion;

        public EditModal(Context c) {
            ac = ProyekActivity.this;
            user = new CurrentUser(ac);
            this.context = c;

            ac.getView(ProyekActivity.this);
            ac.cancel.setOnClickListener(hideEdit);
            ac.progres.setVisibility(View.GONE);
            List<String> dataset = new LinkedList<>(Arrays.asList("TAMBAHKAN", "KURANGI"));
            ac.aksi.attachDataSource(dataset);
            ac.aksi.setSelectedIndex(0);
            Log.d(ac.TAG, "EditModal: " +ac.aksi.getSelectedIndex());
            ac.aksi.setOnSpinnerItemSelectedListener(action);
            ac.simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAksion() == 0){
                        tambahModal();
                    }else if(getAksion() == 1){
                        kurangModal();
                    }
                }
            });




        }
        boolean cekField(){
            int sementara= Integer.parseInt(ac.eModal.getNumber());
            if (sementara < 1){
                ac.eModal.setError("Nominal Tidak Boleh Kosong!");
                return false;
            }
            return true;
        }


        private void tambahModal(){
                if (cekField()){

                ac.menuBtn.setVisibility(View.GONE);
                ac.progres.setVisibility(View.VISIBLE);
                Call<ResponseProyek> p = ac.apiService.updateNilaiProyek(ac.user.getsAuth(), ac.id, "tambah","nilai",ac.edtCatatan.getText().toString(),  ac.eModal.getNumber());

                p.enqueue(new Callback<ResponseProyek>() {
                    @Override
                    public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
                        ac.menuBtn.setVisibility(View.VISIBLE);
                        ac.progres.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                if (response.body().getResult().size() < 1){
                                    ac.loadProyek();
                                    ac.editContent.setVisibility(View.GONE);
                                    ac.showContent.setVisibility(View.VISIBLE);

                                }

                            }
                        }
                        Snackbar.make(ac.menuBtn, response.body().getMsg(), Snackbar.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseProyek> call, Throwable t) {
                        ac.menuBtn.setVisibility(View.VISIBLE);
                        ac.progres.setVisibility(View.GONE);
                        Snackbar.make(ac.menuBtn, "Terjadi Kesalahan!", Snackbar.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                });
                }

            }

        private void kurangModal(){

                if (cekField()){

                ac.menuBtn.setVisibility(View.GONE);
                ac.progres.setVisibility(View.VISIBLE);
                Call<ResponseProyek> p = ac.apiService.updateNilaiProyek(ac.user.getsAuth(), ac.id, "kurang","nilai",ac.getsCatatan(),  ac.eModal.getNumber());

                p.enqueue(new Callback<ResponseProyek>() {
                    @Override
                    public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
                        ac.menuBtn.setVisibility(View.VISIBLE);
                        ac.progres.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
//                                if (response.body().getResult().size() < 1){
                                    ac.loadProyek();
                                    ac.editContent.setVisibility(View.GONE);
                                    ac.showContent.setVisibility(View.VISIBLE);

//                                }

                            }
                        }
                        Snackbar.make(ac.menuBtn, response.body().getMsg(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseProyek> call, Throwable t) {
                        ac.menuBtn.setVisibility(View.VISIBLE);
                        ac.progres.setVisibility(View.GONE);
                        Snackbar.make(ac.menuBtn, "Terjadi Kesalahan!", Snackbar.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                });
                }

            }

        private OnSpinnerItemSelectedListener action = new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setAksion(0);
                        break;
                    case 1:
                        setAksion(1);
                        break;
                }
            }
        };
        public View.OnClickListener hideEdit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.showContent.setVisibility(View.VISIBLE);
                ac.editContent.setVisibility(View.GONE);
            }
        };
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
            if (proyekFiltered.get(position).getStatus().equals("belum lunas") && proyekFiltered.get(position).getJenis().equals("utang"))
                holder.mDana.setTextColor(getResources().getColor(R.color.red));
            else if (proyekFiltered.get(position).getStatus().equals("lunas") && proyekFiltered.get(position).getJenis().equals("utang") )
                holder.mDana.setTextColor(getResources().getColor(R.color.colorPrimary));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ProyekActivity.this, TxDetailActivity.class);

                    intent.putExtra("tx", proyekFiltered.get(position));
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
