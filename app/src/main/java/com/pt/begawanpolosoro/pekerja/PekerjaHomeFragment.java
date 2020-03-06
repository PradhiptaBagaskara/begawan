package com.pt.begawanpolosoro.pekerja;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.login.api.ResponseLogin;
import com.pt.begawanpolosoro.login.api.ResultLogin;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResponseGaji;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResultItemGaji;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PekerjaHomeFragment extends Fragment {
    private static final String TAG = "PekerjaHomeFragment";



    private static List<ResultItemGaji> resultItemGajiList = new ArrayList<>();
    RecyclerView recyclerView;
    private CurrentUser user;
    private TextView nama;
    private TextViewRupiah saldo;
    private ApiService apiService;
    private Dialog profilForm;
    ProgressBar pgProfil;
    EditText edtUname, edtPass,edtNama;
    Button btnSaveProfil, btnBatalProfil;
    ImageButton userSetting;
    TextView labelPass, none;
    BottomNavigationBar bottomNavigationBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pekerja_home, container, false);
        InitRetro initRetro= new InitRetro(getActivity());
        apiService = initRetro.InitApi().create(ApiService.class);
        user = new CurrentUser(getActivity());
        nama = v.findViewById(R.id.nama);
        userSetting = v.findViewById(R.id.userSetting);
        profilDialog(getActivity());
        userSetting.setOnClickListener(showEditProfil);
        none = v.findViewById(R.id.none);

        saldo = v.findViewById(R.id.saldo);
        recyclerView = v.findViewById(R.id.home_rec_pekerja);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadGaji();
        loadSaldo();
        nama.setText(user.getsNama().toUpperCase());
        bottomNavigationBar = getActivity().findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setAutoHideEnabled(true);
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

        return v;
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
        labelPass = profilForm.findViewById(R.id.labelPass);
        labelPass.setVisibility(View.GONE);
        edtPass= profilForm.findViewById(R.id.dialogPassword);
        btnBatalProfil = profilForm.findViewById(R.id.cancelDialog);
        btnSaveProfil = profilForm.findViewById(R.id.saveDialog);
        edtNama.setText(user.getsNama());
        edtUname.setVisibility(View.GONE);
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
            if (TextUtils.isEmpty(edtNama.getText().toString())){
                edtNama.setError("Nama Tidak Boleh Kosong!");
            }else {
                btnSaveProfil.setVisibility(View.GONE);
                btnBatalProfil.setVisibility(View.GONE);
                pgProfil.setVisibility(View.VISIBLE);
                Call<ResponseLogin> up = apiService.updateUser(user.getsAuth(),edtPass.getText().toString(),edtNama.getText().toString());
                up.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        btnSaveProfil.setVisibility(View.VISIBLE);
                        btnBatalProfil.setVisibility(View.VISIBLE);
                        pgProfil.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                ResultLogin re = response.body().getResult();
                                SessionManager sm = new SessionManager(getActivity());
                                sm.storeLogin(re.getRole(),re.getNama(),re.getUsername(),re.getId());
                                nama.setText(re.getNama());
                                profilForm.dismiss();

                            }
                        }
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        btnSaveProfil.setVisibility(View.VISIBLE);
                        btnBatalProfil.setVisibility(View.VISIBLE);
                        pgProfil.setVisibility(View.GONE);
                        t.printStackTrace();
                        Toast.makeText(getActivity(), "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();

                    }
                });
            }


        }
    };



    void loadSaldo(){
        Call<ResponseSaldo> sal = apiService.saldoApi(user.getsAuth());
        sal.enqueue(new Callback<ResponseSaldo>() {
            @Override
            public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        user.setsSaldo(response.body().getResult().getSaldo());
                        saldo.convertToIDR(user.getsSaldo());

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSaldo> call, Throwable t) {

            }
        });
}

    void loadGaji(){
        Call<ResponseGaji> gajiCall = apiService.allGaji(user.getsAuth(),"all","10");
        gajiCall.enqueue(new Callback<ResponseGaji>() {
            @Override
            public void onResponse(Call<ResponseGaji> call, Response<ResponseGaji> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        if (response.body().getResult() != null){
                            resultItemGajiList = response.body().getResult();
                            GajiAdapter adapter = new GajiAdapter(resultItemGajiList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            none.setVisibility(View.GONE);

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGaji> call, Throwable t) {

            }
        });
    }
    public class GajiAdapter extends RecyclerView.Adapter<PekerjaHomeFragment.GajiAdapter.MyviewHolder>{

        List<ResultItemGaji> gajiList;
        public GajiAdapter(List<ResultItemGaji> Gaji) {
            this.gajiList = Gaji;
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
            holder.tgl.setText(gajiList.get(position).getCreatedDate());
            holder.gaji.convertToIDR(gajiList.get(position).getGaji());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sPengirim = gajiList.get(position).getNamaPengirim().toUpperCase();
                    String sPenerima = gajiList.get(position).getNama().toUpperCase();
                    String sJmlh = gajiList.get(position).getGaji();
                    String sTgl = gajiList.get(position).getCreatedDate();

                    customDialog(sPenerima,sPengirim,sTgl,sJmlh);
                }
            });

        }
        public void customDialog(String penerima, String pengirim, String date, String jumlah) {

            Dialog userForm = new Dialog(getActivity());
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
            return gajiList.size();
        }

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView tgl;
            TextViewRupiah gaji;
            public MyviewHolder(@NonNull View itemView) {
                super(itemView);
                tgl = itemView.findViewById(R.id.tgl);
                gaji = itemView.findViewById(R.id.jumlahGaji);

            }
        }
    }

}
