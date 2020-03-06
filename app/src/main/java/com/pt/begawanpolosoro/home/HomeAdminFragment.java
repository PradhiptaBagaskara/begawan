package com.pt.begawanpolosoro.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.congfandi.lib.EditTextRupiah;
import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.GridMenuAdapter;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.ResponseTx;
import com.pt.begawanpolosoro.adapter.ResultItemTx;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.gaji.GajiActivity;
import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.home.api.ResultSaldo;
import com.pt.begawanpolosoro.login.api.ResponseLogin;
import com.pt.begawanpolosoro.login.api.ResultLogin;
import com.pt.begawanpolosoro.pdf.PdfActivity;
import com.pt.begawanpolosoro.proyek.TambahProyekActivity;
import com.pt.begawanpolosoro.transaksi.TxDetailActivity;
import com.pt.begawanpolosoro.user.TambahUserActivity;
import com.pt.begawanpolosoro.user.api.ResponseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.bcc.gridmenuview.GridMenu;


public class HomeAdminFragment extends Fragment {
    LinearLayout userInfo,saldoBtn;
    RelativeLayout relativeLayout;
    TextView nama,username, none;
    TextViewRupiah saldo;
    String sNama;
    String amount = "0";
    String sUsername;

    public String getsAuth() {
        return sAuth;
    }

    public void setsAuth(String sAuth) {
        this.sAuth = sAuth;
    }

    String sAuth;

    public String getsNama() {
        return sNama;
    }

    public void setsNama(String sNama) {
        this.sNama = sNama;
    }

    public String getsUsername() {
        return sUsername;
    }

    public void setsUsername(String sUsername) {
        this.sUsername = sUsername;
    }

    public String getsSaldo() {
        return sSaldo;
    }

    public void setsSaldo(String sSaldo) {
        this.sSaldo = sSaldo;
    }

    String sSaldo;
    ImageButton refresh;
    SessionManager sm;
    HashMap map;
    ApiService apiService;
    RecyclerView recyclerView;
    InitRetro initRetro;
    BottomNavigationBar bt;

    List sementara = new ArrayList<>();
    NestedScrollView scrollView;
    Dialog saldoForm, profilForm;
    EditTextRupiah edtSaldo;
    EditText edtUname, edtPass,edtNama;
    Button edtSave,edtBatal, btnSaveProfil, btnBatalProfil;
    ProgressBar edtPg, pgProfil;
    GridView menuGrid;
    int logo[] = {R.drawable.user, R.drawable.proyek, R.drawable.gaji, R.drawable.print};
    String menuName[] = {"USER", "PROYEK", "GAJI","LAPORAN"};
    GridMenuAdapter gridMenuAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_home, container, false);
        relativeLayout = view.findViewById(R.id.rel);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_rec);
        menuGrid = view.findViewById(R.id.gridMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        scrollView = view.findViewById(R.id.scroll);
        customDialog(getActivity());
        userInfo = view.findViewById(R.id.userInfo);
        saldoBtn = view.findViewById(R.id.edtSaldoBtn);
        saldoBtn.setOnClickListener(showEditSaldo);
        none = view.findViewById(R.id.none);
        profilDialog(getActivity());
        gridMenuAdapter = new GridMenuAdapter(getActivity(),logo,menuName);
        menuGrid.setAdapter(gridMenuAdapter);
        menuGrid.setOnItemClickListener(setMenuListener);

        nama = view.findViewById(R.id.nama);
        username = view.findViewById(R.id.username);
        saldo = view.findViewById(R.id.saldo);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SessionManager(getContext());
        map = sm.getLogged();
        initRetro = new InitRetro(getContext());
        apiService = initRetro.InitApi().create(ApiService.class);
        setsNama(map.get(sm.SES_NAMA).toString());
        setsUsername(map.get(sm.SES_USERNAME).toString());
        setsAuth(map.get(sm.SES_TOKEN).toString());

        loadSaldo();

        loadTx();

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nama.setText(getsNama().toUpperCase());
        username.setText(getsUsername());

        userInfo.setOnClickListener(showEditProfil);


    }



    private AdapterView.OnItemClickListener setMenuListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i;
            switch (position){
                case 0:
                    i = new Intent(getActivity(), TambahUserActivity.class);
                    startActivity(i);
                    break;
                case 1:
                    i = new Intent(getActivity(), TambahProyekActivity.class);
                    startActivity(i);
                    break;
                case 2:
                    i = new Intent(getActivity(), GajiActivity.class);
                    startActivity(i);
                    break;
                case 3:
                    i = new Intent(getActivity(), PdfActivity.class);
                    startActivity(i);
                    break;


            }
        }
    };

    private View.OnClickListener showEditSaldo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saldoForm.show();
            edtSave.setOnClickListener(updateSaldo);
            edtBatal.setOnClickListener(closeDialog);

        }
    };

    private View.OnClickListener closeDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edtSaldo.getText().clear();
            saldoForm.dismiss();
        }
    };

    private View.OnClickListener updateSaldo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edtSave.setVisibility(View.GONE);
            edtBatal.setVisibility(View.GONE);
            edtPg.setVisibility(View.VISIBLE);
           Call<ResponseSaldo> update = apiService.updateSaldoApi(getsAuth(), getsAuth(), edtSaldo.getNumber().toString(), "tambah");
           update.enqueue(new Callback<ResponseSaldo>() {
               @Override
               public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {
                   if (response.isSuccessful()){
                       if (response.body().isStatus()){
                           edtBatal.setVisibility(View.VISIBLE);
                           edtSave.setVisibility(View.VISIBLE);
                           edtPg.setVisibility(View.GONE);
                           saldoForm.dismiss();
                           Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                           loadSaldo();

                       }
                   }
               }

               @Override
               public void onFailure(Call<ResponseSaldo> call, Throwable t) {
                   t.printStackTrace();
                   Toast.makeText(getActivity(), "Terjadi Kesalahan!", Toast.LENGTH_LONG).show();
                   edtBatal.setVisibility(View.VISIBLE);
                   edtSave.setVisibility(View.VISIBLE);
                   edtPg.setVisibility(View.GONE);
               }
           });
        }
    };

    private void customDialog(Context context) {
        saldoForm = new Dialog(context);
        saldoForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saldoForm.setContentView(R.layout.dialog_saldo_home);
        saldoForm.setCancelable(true);
        saldoForm.getWindow().setBackgroundDrawableResource(R.color.transparant);
        saldoForm.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        edtPg = saldoForm.findViewById(R.id.progresSaldo);
        edtPg.setVisibility(View.GONE);
        edtSaldo = saldoForm.findViewById(R.id.dialogSaldo);
        edtSave = saldoForm.findViewById(R.id.saveSaldo);
        edtBatal= saldoForm.findViewById(R.id.cancelDialog);
    }


    private void profilDialog(Context context) {
        profilForm = new Dialog(context);
        profilForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profilForm.setContentView(R.layout.dialog_edit_home);
        profilForm.setCancelable(true);
        profilForm.getWindow().setBackgroundDrawableResource(R.color.transparant);
        profilForm.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pgProfil = profilForm.findViewById(R.id.progresProfil);
        pgProfil.setVisibility(View.GONE);
        edtUname = profilForm.findViewById(R.id.dialogUname);
        edtNama = profilForm.findViewById(R.id.dialogNama);
        edtPass= profilForm.findViewById(R.id.dialogPassword);
        btnBatalProfil = profilForm.findViewById(R.id.cancelDialog);
        btnSaveProfil = profilForm.findViewById(R.id.saveDialog);
        edtNama.setText(getsNama());
        edtUname.setText(getsUsername());
    }

    private View.OnClickListener showEditProfil = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            profilForm.show();
            btnBatalProfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profilForm.dismiss();
                    edtPass.getText().clear();
                }
            });
            btnSaveProfil.setOnClickListener(updateProfil);

        }
    };

    private View.OnClickListener updateProfil = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            btnSaveProfil.setVisibility(View.GONE);
            btnBatalProfil.setVisibility(View.GONE);
            pgProfil.setVisibility(View.VISIBLE);
            Call<ResponseLogin> up = apiService.updateAdmin(getsAuth(),edtUname.getText().toString(), edtNama.getText().toString());
            up.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    btnSaveProfil.setVisibility(View.VISIBLE);
                    pgProfil.setVisibility(View.GONE);
                    btnBatalProfil.setVisibility(View.VISIBLE);

                    if (response.isSuccessful()){
                        if (response.body().isStatus()){
                            ResultLogin dt = response.body().getResult();
                            sm.storeLogin(dt.getRole(),dt.getNama(),dt.getUsername(),dt.getId());
                            nama.setText(dt.getNama().toUpperCase());
                            profilForm.dismiss();
                            Toast.makeText(getActivity(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getActivity(),response.body().getMsg(),Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    btnSaveProfil.setVisibility(View.VISIBLE);
                    pgProfil.setVisibility(View.GONE);
                    btnBatalProfil.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"Terjadi Kesalahan! Coba lagi nanti",Toast.LENGTH_LONG).show();

                }
            });
            if (!TextUtils.isEmpty(edtPass.getText().toString())){
                Call<ResponseUser> u = apiService.resetPassword(getsAuth(), getsAuth(), edtPass.getText().toString());
                u.enqueue(new Callback<ResponseUser>() {
                    @Override
                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                        btnSaveProfil.setVisibility(View.VISIBLE);
                        pgProfil.setVisibility(View.GONE);
                        btnBatalProfil.setVisibility(View.VISIBLE);

                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                profilForm.dismiss();
                                Toast.makeText(getActivity(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getActivity(),response.body().getMsg(),Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUser> call, Throwable t) {
                        btnSaveProfil.setVisibility(View.VISIBLE);
                        pgProfil.setVisibility(View.GONE);
                        btnBatalProfil.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Terjadi Kesalahan! Coba lagi nanti", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    };




    void loadSaldo(){
        Call<ResponseSaldo> apiSaldo = apiService.saldoApi(getsAuth());
        apiSaldo.enqueue(new Callback<ResponseSaldo>() {
            @Override
            public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        ResultSaldo data = response.body().getResult();
//                        Log.d("saldo", response.body().toString() + "token: " +getsAuth());
                        amount = data.getSaldo().toString();
                    }

                }
                setsSaldo(amount);

                saldo.convertToIDR(getsSaldo());
//                saldo.setText(model.toIdr(getsSaldo()));

            }

            @Override
            public void onFailure(Call<ResponseSaldo> call, Throwable t) {
//                Toast.makeText(getActivity(), "Terjadi Kesalahan! Coba lagi nanti", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void loadTx(){
        Call<ResponseTx> TxApi = apiService.txApi(getsAuth());
        TxApi.enqueue(new Callback<ResponseTx>() {
            @Override
            public void onResponse(Call<ResponseTx> call, Response<ResponseTx> response) {
                if (response.isSuccessful()){
                    ResponseTx res = response.body();
                    if (res.isStatus()){
                        if (res.getResult() != null){
//                            Log.d("tagger: ", res.getResult().toString());
                            none.setVisibility(View.GONE);
                            List<ResultItemTx> TxItem = res.getResult();
                            TxAdapter Tadapter = new TxAdapter(TxItem);
                            recyclerView.setAdapter(Tadapter);
                        }

                    }else {
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

    public class TxAdapter extends RecyclerView.Adapter<TxAdapter.MyViewHolder>{

        List<ResultItemTx> txItem;
        public TxAdapter (List<ResultItemTx> MtxItem){
            txItem = MtxItem;
        }

        @NonNull
        @Override
        public TxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txby_user, parent, false);
            MyViewHolder mViewHolder = new MyViewHolder(mView);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final TxAdapter.MyViewHolder holder, final int position) {
            holder.mDana.convertToIDR(txItem.get(position).getDana());
            holder.mNama.setText(txItem.get(position).getNama());
            holder.mTx.setText(txItem.get(position).getNamaTransaksi());
            holder.mTgl.setText(txItem.get(position).getCreatedDate());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TxDetailActivity.class);
                    intent.putExtra("id", txItem.get(position).getId());
                    intent.putExtra("nama", txItem.get(position).getNama());
                    intent.putExtra("nama_tx", txItem.get(position).getNamaTransaksi());
                    intent.putExtra("nama_proyek", txItem.get(position).getNamaProyek());
                    intent.putExtra("jenis_bayar", txItem.get(position).getJenis());
                    intent.putExtra("dana", txItem.get(position).getDana());
                    intent.putExtra("keterangan", txItem.get(position).getKeterangan());
                    intent.putExtra("waktu", txItem.get(position).getCreatedDate());
                    intent.putExtra("halaman", "1");

                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return txItem.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public  TextView mTx, mNama, mTgl;
            TextViewRupiah mDana;


            public MyViewHolder(View itemView) {
                super(itemView);
                mDana = itemView.findViewById(R.id.txDana);
                mTx = itemView.findViewById(R.id.pembelian);
                mNama = itemView.findViewById(R.id.nama_proyek);
                mTgl = itemView.findViewById(R.id.txTgl);
            }
        }
    }
}
