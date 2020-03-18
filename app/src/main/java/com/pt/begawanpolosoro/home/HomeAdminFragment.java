package com.pt.begawanpolosoro.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.congfandi.lib.EditTextRupiah;
import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.CurrentUser;
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
import com.pt.begawanpolosoro.pdf.PdfActivity;
import com.pt.begawanpolosoro.proyek.TambahProyekActivity;
import com.pt.begawanpolosoro.transaksi.TxDetailActivity;
import com.pt.begawanpolosoro.user.TambahUserActivity;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

//import com.bcc.gridmenuview.GridMenu;


public class HomeAdminFragment extends Fragment {
    ImageButton saldoBtn;
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

    Dialog saldoForm, profilForm;
    NiceSpinner aksi;
    EditTextRupiah edtSaldo;
    EditText edtUname, edtPass,edtNama;
    MaterialEditText catatan;
    Button edtSave,edtBatal, btnSaveProfil, btnBatalProfil;
    ProgressBar edtPg, pgProfil;
    GridView menuGrid;
    int logo[] = {R.drawable.user, R.drawable.proyek, R.drawable.gaji, R.drawable.print};
    String menuName[] = {"PENGGUNA", "PEKERJAAN", "GAJI","LAPORAN"};
    GridMenuAdapter gridMenuAdapter;
    ApiHelper apiHelper = new ApiHelper();
    CurrentUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_home, container, false);
        relativeLayout = view.findViewById(R.id.rel);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_rec);
        menuGrid = view.findViewById(R.id.gridMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        customDialog(getActivity());
        saldoBtn = view.findViewById(R.id.editSaldo);
        saldoBtn.setOnClickListener(showEditSaldo);
        none = view.findViewById(R.id.none);
        gridMenuAdapter = new GridMenuAdapter(getActivity(),logo,menuName);
        menuGrid.setAdapter(gridMenuAdapter);
        menuGrid.setOnItemClickListener(setMenuListener);
        nama = view.findViewById(R.id.nama);
        username = view.findViewById(R.id.username);
        saldo = view.findViewById(R.id.saldo);
        TextView title = getActivity().findViewById(R.id.title);
        title.setText(getResources().getString(R.string.app_name));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SessionManager(getContext());
        map = sm.getLogged();
        initRetro = new InitRetro(getContext());
        user = new CurrentUser(getActivity());

        apiService = initRetro.InitApi().create(ApiService.class);
        setsAuth(user.getsAuth());
        setsNama(user.getsNama());
        setsUsername(user.getsUsername());


        loadSaldo();

        loadTx();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadTx();
        Log.i(TAG, "onResume: "+user.getsNama());

    }


    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: " + user.getsNama());
        nama.setText(user.getsNama().toUpperCase());
        username.setText(user.getsUsername());

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
            catatan.getText().clear();
            edtSaldo.getText().clear();
            saldoForm.dismiss();
        }
    };

    private View.OnClickListener updateSaldo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edtSave.setVisibility(View.GONE);
            edtBatal.setVisibility(View.GONE);
            apiHelper.setKeterangan(catatan.getText().toString());
            edtPg.setVisibility(View.VISIBLE);
           Call<ResponseSaldo> update = apiService.updateSaldoApi(getsAuth(), getsAuth(), edtSaldo.getNumber().toString(), apiHelper.getParam(), apiHelper.getKeterangan());
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
        aksi = saldoForm.findViewById(R.id.aksi);
        catatan = saldoForm.findViewById(R.id.catatan);

        List<String> dataset = new LinkedList<>(Arrays.asList("TAMBAHKAN", "KURANGI"));
        aksi.attachDataSource(dataset);
        apiHelper.setParam("tambah");
        aksi.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        apiHelper.setParam("tambah");
                        break;
                    case 1:
                        apiHelper.setParam("kurang");
                        break;
                }
            }
        });
        Log.d(TAG, "customDialog: "+apiHelper.getParam());
    }









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
            if (txItem.get(position).getStatus().equals("belum lunas") && txItem.get(position).getJenis().equals("utang"))
                holder.mDana.setTextColor(getResources().getColor(R.color.red));
            else if (txItem.get(position).getStatus().equals("lunas") && txItem.get(position).getJenis().equals("utang") )
                holder.mDana.setTextColor(getResources().getColor(R.color.colorPrimary));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TxDetailActivity.class);
                    intent.putExtra("halaman", "1");
                    intent.putExtra("data", txItem.get(position));

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
