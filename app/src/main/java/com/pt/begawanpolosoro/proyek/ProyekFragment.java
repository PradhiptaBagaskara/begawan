package com.pt.begawanpolosoro.proyek;

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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.proyek.api.ResultItemProyek;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProyekFragment extends Fragment {


    RecyclerView recyclerView;
    ApiService apiService;
    CurrentUser user;
    SearchView searchView;
    ProyekAdapter adapter;
    TextView none;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = InitRetro.InitApi().create(ApiService.class);
        user = new CurrentUser(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_proyek, container, false);
        searchView = v.findViewById(R.id.cari_user);
        searchView.clearFocus();
        none = v.findViewById(R.id.none);
        TextView title = getActivity().findViewById(R.id.title);
        title.setText("PEKERJAAN");



        recyclerView = v.findViewById(R.id.proyek_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadProyek();


        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        loadProyek();
    }

    public void loadProyek(){
        Call<ResponseProyek> data = apiService.getProyek(user.getsAuth());
        data.enqueue(new Callback<ResponseProyek>() {
            @Override
            public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
                ResponseProyek res = response.body();
                if (res.isStatus()){
                    if (res.getResult().size() != 0){
                        none.setVisibility(View.GONE);
                        List<ResultItemProyek> userList = response.body().getResult();
                        adapter = new ProyekAdapter(userList);
                        recyclerView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                adapter.getFilter().filter(query);
//                Log.d("search", "onQueryTextSubmit: "+query);
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
                    Log.d("tagger: ", res.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {
               t.printStackTrace();
            }

        });
    }


    public class ProyekAdapter extends RecyclerView.Adapter<ProyekAdapter.MyViewHolder> implements Filterable {

        List<ResultItemProyek> txItem, filteredUser;
        public ProyekAdapter (List<ResultItemProyek> MtxItem){
            txItem = MtxItem;
            filteredUser = MtxItem;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.proyek_item, parent, false);
            ProyekFragment.ProyekAdapter.MyViewHolder mViewHolder = new ProyekFragment.ProyekAdapter.MyViewHolder(mView);
            return mViewHolder;
        }



        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//            holder.mDana.convertToIDR(filteredUser.get(position).getSaldo());
                holder.mNama.setText(filteredUser.get(position).getNamaProyek());
                holder.mTgl.setText(filteredUser.get(position).getCreatedDate());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProyekActivity.class);
                    intent.putExtra("id_proyek", filteredUser.get(position).getId());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return filteredUser.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charseq = constraint.toString();

                    if (charseq.isEmpty()){
                        filteredUser = txItem;

                    }else {
                        List<ResultItemProyek> itemUserList = new ArrayList<>();

                        for (ResultItemProyek row : txItem){
                            if (row.getNamaProyek().toLowerCase().contains(charseq.toLowerCase())){
                                itemUserList.add(row);
                            }
                        }
                        filteredUser = itemUserList;

                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredUser;
//                    Log.d("filter", "performFiltering: "+filterResults.toString());
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredUser = (ArrayList<ResultItemProyek>) results.values;
                    Log.d("filter", "performFiltering: "+filteredUser.toString());

                    notifyDataSetChanged();
                }
            };
        }


        public class MyViewHolder extends RecyclerView.ViewHolder  {
            public TextView mNama, mTgl;



            public MyViewHolder(View itemView) {
                super(itemView);

                mNama = itemView.findViewById(R.id.nama_proyek);
                mTgl = itemView.findViewById(R.id.tgl);
            }
        }
    }


}
