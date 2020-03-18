package com.pt.begawanpolosoro.pekerja;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.ResponseTx;
import com.pt.begawanpolosoro.adapter.ResultItemTx;
import com.pt.begawanpolosoro.transaksi.TxDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiUserFragment extends Fragment {


    RecyclerView recyclerView;
    SearchView searchView;
    CurrentUser user;
    ApiService apiService;
    TextView vAktifitas;
    BottomNavigationBar bottomNavigationBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaksi_user, container, false);
        recyclerView = v.findViewById(R.id.user_tx_rec);
        searchView = v.findViewById(R.id.cari_tx);
        user = new CurrentUser(getActivity());
        InitRetro initRetro = new InitRetro(getActivity());
        apiService = initRetro.InitApi().create(ApiService.class);

        bottomNavigationBar = getActivity().findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setAutoHideEnabled(true);
        vAktifitas = v.findViewById(R.id.none);
        TextView title = getActivity().findViewById(R.id.title);
        title.setText("TRANSAKSI");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx < dy){
                    bottomNavigationBar.hide(true);
                }else {
                    bottomNavigationBar.show(true);
                }

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadTx();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTx();
    }

    public void loadTx(){
        Call<ResponseTx> TxApi = apiService.txApi(user.getsAuth());
        TxApi.enqueue(new Callback<ResponseTx>() {
            @Override
            public void onResponse(Call<ResponseTx> call, Response<ResponseTx> response) {
                if (response.isSuccessful()){
                    ResponseTx res = response.body();
                    if (res.isStatus()){
                        if (res.getResult() != null){
//                            Log.d("tagger: ", res.getResult().toString());
                            vAktifitas.setVisibility(View.GONE);

                            List<ResultItemTx> TxItem = res.getResult();
                            TransaksiAdapter adapter = new TransaksiAdapter(TxItem);
                            recyclerView.setAdapter(adapter);
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    adapter.getFilter().filter(query);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    adapter.getFilter().filter(newText);

                                    return false;
                                }
                            });

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


    public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.MyViewHolder> implements Filterable{

        List<ResultItemTx> resultItemTxes;
        List<ResultItemTx> filteredItem;
        public  TransaksiAdapter(List<ResultItemTx> res){
            this.resultItemTxes = res;
            this.filteredItem = res;

        }
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charseq = constraint.toString();

                    if (charseq.isEmpty()){
                        filteredItem = resultItemTxes;

                    }else {
                        List<ResultItemTx> itemUserList = new ArrayList<>();

                        for (ResultItemTx row : resultItemTxes){
                            if (row.getNama().toLowerCase().contains(charseq.toLowerCase()) || row.getCreatedDate().toLowerCase().contains(charseq.toLowerCase())){
                                itemUserList.add(row);
                            }
                        }
                        filteredItem = itemUserList;

                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItem;
//                    Log.d("filter", "performFiltering: "+filterResults.toString());
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredItem = (ArrayList<ResultItemTx>) results.values;
//                    Log.d("filter", "performFiltering: "+filteredUser.toString());

                    notifyDataSetChanged();
                }
            };

        }

        @NonNull
        @Override
        public TransaksiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txby_user, parent, false);
            MyViewHolder mViewHolder = new MyViewHolder(mView);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TransaksiAdapter.MyViewHolder holder, int position) {
            holder.mDana.convertToIDR(filteredItem.get(position).getDana());
            holder.mNama.setText(filteredItem.get(position).getNamaTransaksi());
            holder.mTx.setText(filteredItem.get(position).getNamaProyek());
            holder.mTgl.setText(filteredItem.get(position).getCreatedDate());
            if (filteredItem.get(position).getStatus().equals("belum lunas") && filteredItem.get(position).getJenis().equals("utang"))
                holder.mDana.setTextColor(getResources().getColor(R.color.red));
            else if (filteredItem.get(position).getStatus().equals("lunas") && filteredItem.get(position).getJenis().equals("utang") )
                holder.mDana.setTextColor(getResources().getColor(R.color.colorPrimary));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), TxDetailActivity.class);
                    intent.putExtra("halaman", "1");
                    intent.putExtra("data", filteredItem.get(position));
                    startActivity(intent);


                }
            });

        }

        @Override
        public int getItemCount() {
            return filteredItem.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView mTx, mNama,mTgl;
            public TextViewRupiah mDana;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                mDana = itemView.findViewById(R.id.txDana);
                mTx = itemView.findViewById(R.id.nama_proyek);
                mNama = itemView.findViewById(R.id.pembelian);
                mTgl = itemView.findViewById(R.id.txTgl);
            }
        }
    }

}
