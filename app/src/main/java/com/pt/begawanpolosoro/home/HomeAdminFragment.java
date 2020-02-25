package com.pt.begawanpolosoro.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.bcc.gridmenuview.GridMenu;


public class HomeAdminFragment extends Fragment {
    LinearLayout userInfo;
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

    List sementara = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.home_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userInfo = view.findViewById(R.id.userInfo);
        nama = view.findViewById(R.id.nama);
        username = view.findViewById(R.id.username);
        saldo = view.findViewById(R.id.saldo);
        refresh = view.findViewById(R.id.refreshSaldo);
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
        apiService = InitRetro.InitApi().create(ApiService.class);
        setsNama(map.get(sm.SES_NAMA).toString());
        setsUsername(map.get(sm.SES_USERNAME).toString());
        setsAuth(map.get(sm.SES_TOKEN).toString());

        loadSaldo();

        loadTx();








    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadTx();
        nama.setText(getsNama());
        username.setText(getsUsername());
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSaldo();
            }
        });
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
                        Log.d("saldo", response.body().toString() + "token: " +getsAuth());
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
                            Log.d("tagger: ", res.getResult().toString());

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
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_tx_item, parent, false);
            MyViewHolder mViewHolder = new MyViewHolder(mView);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final TxAdapter.MyViewHolder holder, int position) {
            holder.mDana.convertToIDR(txItem.get(position).getDana());
            holder.mNama.setText(txItem.get(position).getNamaTransaksi());
            holder.mTx.setText(txItem.get(position).getNama());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return txItem.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public  TextView mTx, mNama;
            TextViewRupiah mDana;


            public MyViewHolder(View itemView) {
                super(itemView);
                mDana = itemView.findViewById(R.id.txDana);
                mTx = itemView.findViewById(R.id.txName);
                mNama = itemView.findViewById(R.id.txNameUser);
            }
        }
    }
}
