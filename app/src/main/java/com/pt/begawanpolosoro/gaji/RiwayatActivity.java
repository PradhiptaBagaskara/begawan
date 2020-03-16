package com.pt.begawanpolosoro.gaji;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResponseGaji;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResultItemGaji;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CurrentUser user;
    InitRetro initRetro;
    SearchView searchView;
    ImageButton back;
    TextView none;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));
        initRetro = new InitRetro(getApplicationContext());
        user = new CurrentUser(getApplicationContext());
        none =findViewById(R.id.none);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView = findViewById(R.id.cari);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        loadGaji();
    }
    void loadGaji(){
        Call<ResponseGaji> g = initRetro.apiRetro().allGaji(user.getsAuth(), "","");
        g.enqueue(new Callback<ResponseGaji>() {
            @Override
            public void onResponse(Call<ResponseGaji> call, Response<ResponseGaji> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        List<ResultItemGaji> item = response.body().getResult();
                        if (item.size() > 0){
                            none.setVisibility(View.GONE);

                        }
                        GajiAdapter adapter = new GajiAdapter(item);
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
                }
            }

            @Override
            public void onFailure(Call<ResponseGaji> call, Throwable t) {

            }
        });
    }

    public class GajiAdapter extends RecyclerView.Adapter<RiwayatActivity.GajiAdapter.MyviewHolder> implements Filterable {

        List<ResultItemGaji> gajiList;
        List<ResultItemGaji> gajiFiltered;


        public GajiAdapter(List<ResultItemGaji> Gaji) {
            this.gajiList = Gaji;
            this.gajiFiltered = Gaji;
        }

        @NonNull
        @Override
        public GajiAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gaji, parent, false);
            MyviewHolder myviewHolder = new MyviewHolder(mView);
            return myviewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GajiAdapter.MyviewHolder holder, int position) {
            holder.tgl.setText(gajiFiltered.get(position).getCreatedDate());
            holder.gaji.convertToIDR(gajiFiltered.get(position).getGaji());
            holder.nama.setText(gajiFiltered.get(position).getNama());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sPengirim = gajiFiltered.get(position).getNamaPengirim().toUpperCase();
                    String sPenerima = gajiFiltered.get(position).getNama().toUpperCase();
                    String sJmlh = gajiFiltered.get(position).getGaji();
                    String sTgl = gajiFiltered.get(position).getCreatedDate();
                    Intent i = new Intent(RiwayatActivity.this, GajiDetailActivity.class);
                    i.putExtra("data", gajiFiltered.get(position));
//                    i.putExtra("penerima", sPenerima);
//                    i.putExtra("penngirim", sPengirim);
//                    i.putExtra("jumlah", sJmlh);
//                    i.putExtra("tanggal", sTgl);
//                    i.putExtra("pekerjaan", gajiFiltered.get(position).getNamaProyek());
//                    i.putExtra("catatan", gajiFiltered.get(position).getKeterangan());
                    startActivity(i);
//                    customDialog(sPenerima,sPengirim,sTgl,sJmlh);
                }
            });

        }

        public void customDialog(String penerima, String pengirim, String date, String jumlah) {

            Dialog userForm = new Dialog(RiwayatActivity.this);
            userForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            userForm.setContentView(R.layout.dialog_gaji);
            userForm.setCancelable(true);
            userForm.getWindow().setBackgroundDrawableResource(R.color.transparant);
            userForm.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            TextViewRupiah gajiDialog = userForm.findViewById(R.id.gaji);
            TextView namaPengirim = userForm.findViewById(R.id.namaPengirim);
            TextView namaPenerima = userForm.findViewById(R.id.namaPenerima);
            TextView tanggal = userForm.findViewById(R.id.tglDialog);
            Button back = userForm.findViewById(R.id.backDialog);

            gajiDialog.convertToIDR(jumlah);
            namaPenerima.setText(penerima);
            namaPengirim.setText(pengirim);
            tanggal.setText(date);
            userForm.show();
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userForm.dismiss();
                }
            });


        }

        @Override
        public int getItemCount() {
            return gajiFiltered.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charseq = constraint.toString();

                    if (charseq.isEmpty()) {
                        gajiFiltered = gajiList;

                    } else {
                        List<ResultItemGaji> itemUserList = new ArrayList<>();

                        for (ResultItemGaji row : gajiList) {
                            if (row.getNama().toLowerCase().contains(charseq.toLowerCase()) || row.getGaji().contains(charseq) || row.getUsername().toLowerCase().contains(charseq.toLowerCase())) {
                                itemUserList.add(row);
                            }
                        }
                        gajiFiltered = itemUserList;

                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = gajiFiltered;
//                    Log.d("filter", "performFiltering: "+filterResults.toString());
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    gajiFiltered = (ArrayList<ResultItemGaji>) results.values;
//                    Log.d("filter", "performFiltering: "+filteredUser.toString());

                    notifyDataSetChanged();
                }
            };

        }

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView tgl;
            TextViewRupiah gaji;
            TextView nama;
//            TextViewRupiah gajiDialog;
//            Dialog userForm;
//            TextView namaPengirim, namaPenerima, tanggal;

            public MyviewHolder(@NonNull View itemView) {
                super(itemView);
                tgl = itemView.findViewById(R.id.tgl);
                gaji = itemView.findViewById(R.id.jumlahGaji);
                nama = itemView.findViewById(R.id.nama_user);
//                customDialog();


            }


        }
    }

}
