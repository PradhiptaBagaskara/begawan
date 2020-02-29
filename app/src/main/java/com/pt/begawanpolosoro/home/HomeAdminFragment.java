package com.pt.begawanpolosoro.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.bcc.gridmenuview.GridMenu;
import com.bcc.gridmenuview.event.OnItemClickListener;
import com.bcc.gridmenuview.model.MenuItem;
import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.ResponseTx;
import com.pt.begawanpolosoro.adapter.ResultItemTx;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.home.api.ResultSaldo;
import com.pt.begawanpolosoro.transaksi.TxDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.bcc.gridmenuview.GridMenu;


public class HomeAdminFragment extends Fragment {
    LinearLayout userInfo;
    RelativeLayout relativeLayout;
    TextView nama,username;
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
    GridMenu menu;
    final ArrayList<MenuItem> list = new ArrayList<>();
    RecyclerView recyclerView;
    InitRetro initRetro;
    BottomNavigationBar bt;

    List sementara = new ArrayList<>();
    NestedScrollView scrollView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_home, container, false);
        relativeLayout = view.findViewById(R.id.rel);
        userInfo = view.findViewById(R.id.userInfo);
        bt = getActivity().findViewById(R.id.bottom_navigation_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        scrollView = view.findViewById(R.id.scroll);




        nama = view.findViewById(R.id.nama);
        username = view.findViewById(R.id.username);
        saldo = view.findViewById(R.id.saldo);
//        refresh = view.findViewById(R.id.refreshSaldo);
        menu = view.findViewById(R.id.menu_grid);





        list.add(new MenuItem("USER", getResources().getDrawable(R.drawable.user)));
        list.add(new MenuItem("PROYEK", getResources().getDrawable(R.drawable.proyek)));
        list.add(new MenuItem("GAJI", getResources().getDrawable(R.drawable.gaji)));
        list.add(new MenuItem("LAPORAN", getResources().getDrawable(R.drawable.print)));

        menu.setMenuItems(list);



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
        nama.setText(getsNama());
        username.setText(getsUsername());
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadSaldo();
//            }
//        });
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), getsSaldo(), Toast.LENGTH_LONG).show();
            }
        });

        menu.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "you selected " + list.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });



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
