package com.pt.begawanpolosoro.user;

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

import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.user.api.ResponseUser;
import com.pt.begawanpolosoro.user.api.ResultItemUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment {

    RecyclerView recyclerView;
    ApiService apiService;
    CurrentUser user;
    SearchView searchView;
    UserAdapter adapter;
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
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        searchView = view.findViewById(R.id.cari_user);
        searchView.clearFocus();
        none = view.findViewById(R.id.none);
        TextView title = getActivity().findViewById(R.id.title);
        title.setText("PENGGUNA");



        recyclerView = view.findViewById(R.id.user_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadUser();





        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }

    public void loadUser(){
        Call<ResponseUser> call = apiService.getUser(user.getsAuth());
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.isSuccessful()){
                    ResponseUser res = response.body();
                    if (res.isStatus()){
                        if (res.getResult() != null){
                            none.setVisibility(View.GONE);
                            List<ResultItemUser> userList = response.body().getResult();
                            adapter = new UserAdapter(userList);
                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();
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
                        Log.d("tagger: ", res.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });

    }

    public class UserAdapter extends RecyclerView.Adapter<UserFragment.UserAdapter.MyViewHolder> implements Filterable {

        List<ResultItemUser> txItem, filteredUser;
        public UserAdapter (List<ResultItemUser> MtxItem){
            txItem = MtxItem;
            filteredUser = MtxItem;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
            MyViewHolder mViewHolder = new MyViewHolder(mView);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.mDana.convertToIDR(filteredUser.get(position).getSaldo());
            holder.mNama.setText(filteredUser.get(position).getNama());
            holder.mTx.setText(filteredUser.get(position).getUsername());
            int role = Integer.parseInt(filteredUser.get(position).getRole());
            if (role == 0){
                holder.mRole.setText("karyawan");
            }else if (role == 1){
                holder.mRole.setText("pelaksana");
                holder.mRole.setTextColor(getResources().getColor(R.color.colorPrimary));
            }else {
                holder.mRole.setText("administrator");
                holder.mRole.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    intent.putExtra("username", filteredUser.get(position).getUsername());
                    intent.putExtra("role", filteredUser.get(position).getRole());
                    intent.putExtra("saldo", filteredUser.get(position).getSaldo());
                    intent.putExtra("id", filteredUser.get(position).getId());
                    intent.putExtra("nama", filteredUser.get(position).getNama());
                    intent.putExtra("halaman", "2");


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
                        List<ResultItemUser> itemUserList = new ArrayList<>();

                        for (ResultItemUser row : txItem){
                            if (row.getNama().toLowerCase().contains(charseq.toLowerCase()) || row.getUsername().toLowerCase().contains(charseq.toLowerCase())){
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
                    filteredUser = (ArrayList<ResultItemUser>) results.values;
//                    Log.d("filter", "performFiltering: "+filteredUser.toString());

                    notifyDataSetChanged();
                }
            };
        }


        public class MyViewHolder extends RecyclerView.ViewHolder  {
            public TextView mTx, mNama, mRole;
            TextViewRupiah mDana;



            public MyViewHolder(View itemView) {
                super(itemView);
                mDana = itemView.findViewById(R.id.txDana);
                mTx = itemView.findViewById(R.id.txName);
                mNama = itemView.findViewById(R.id.txNameUser);
                mRole = itemView.findViewById(R.id.role);
            }
        }
    }

}
