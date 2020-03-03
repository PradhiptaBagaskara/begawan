package com.pt.begawanpolosoro.pekerja;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.congfandi.lib.EditTextRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.PekerjaControlerActivity;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.ResponseTx;
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.proyek.api.ResultItemProyek;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.angmarch.views.SpinnerTextFormatter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PekerjaActivity extends AppCompatActivity {
    MaterialEditText namaTx, keteranganTx;
    EditTextRupiah totalDana;
    NiceSpinner opsi,jenisBayar;
    ProgressBar pg;
    private static final String TAG = "PekerjaActivity";

    public String getIdProyek() {
        return idProyek;
    }

    public void setIdProyek(String idProyek) {
        this.idProyek = idProyek;
    }

    public String getJenis() {
        return jenis.toLowerCase();
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    String jenis;


    String idProyek;
    ApiService apiService;
    CurrentUser user;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pekerja);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));

        InitRetro initRetro = new InitRetro(getApplicationContext());
        apiService = initRetro.InitApi().create(ApiService.class);
        user = new CurrentUser(getApplicationContext());

        pg = findViewById(R.id.progresBaru);


        btn = findViewById(R.id.btnBaru);
        namaTx = findViewById(R.id.namaBaru);
        keteranganTx = findViewById(R.id.keterangan);
        jenisBayar = findViewById(R.id.jenisBayar);
        List<String> dataset = new LinkedList<>(Arrays.asList("KHAS", "BON"));
        jenisBayar.attachDataSource(dataset);
        String  jen = jenisBayar.getSelectedItem().toString();
        setJenis(jen);
        jenisBayar.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                setJenis(item);
                Toast.makeText(getApplicationContext(), "Selected Jenis: " + item, Toast.LENGTH_SHORT).show();
            }
        });
        totalDana = findViewById(R.id.totalTx);
        opsi = findViewById(R.id.roleBaru);
        loadProyek();




        btn.setOnClickListener(send);


        opsi.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                ResultItemProyek result = (ResultItemProyek) opsi.getSelectedItem();
                setIdProyek(result.getId());
                Toast.makeText(getApplicationContext(), result.getId()+" "+result.getNamaProyek(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private View.OnClickListener send = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), getJenis(), Toast.LENGTH_SHORT).show();
            if (cekField()){
                btn.setVisibility(View.GONE);
                Call<ResponseTx> r = apiService.postTx(user.getsAuth(), getIdProyek(),namaTx.getText().toString(), totalDana.getNumber(),keteranganTx.getText().toString(),getJenis());
                r.enqueue(new Callback<ResponseTx>() {
                    @Override
                    public void onResponse(Call<ResponseTx> call, Response<ResponseTx> response) {
                        btn.setVisibility(View.VISIBLE);
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                Intent intent = new Intent(PekerjaActivity.this, PekerjaControlerActivity.class);
                                intent.putExtra("halaman", "2");
                                startActivity(intent);
                                finish();
                            }
                        }
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseTx> call, Throwable t) {
                        btn.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan! Coba Lagi Nanti..", Toast.LENGTH_SHORT).show();


                    }
                });

            }
        }
    };

    private boolean cekField(){
        if (TextUtils.isEmpty(namaTx.getText())){
            namaTx.setError("Nama Pembelian Harus Diisi");
            return false;
        }else if (TextUtils.isEmpty(keteranganTx.getText())){
            keteranganTx.setError("Catatan Pembelian Harus Diisi");
            return false;

        }else if (TextUtils.isEmpty(totalDana.getNumber())){
            totalDana.setError("Total Pembelian Harus Diisi");
            return false;

        }else {
            return true;
        }
    }




    void loadProyek (){
        Call<ResponseProyek> p = apiService.getProyek(user.getsAuth());
        p.enqueue(new Callback<ResponseProyek>() {
            @Override
            public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){

                        List<ResultItemProyek> resultItemProyek = response.body().getResult();

                        SpinnerTextFormatter formater = new SpinnerTextFormatter<ResultItemProyek>() {
                            @Override
                            public Spannable format(ResultItemProyek item) {
                                return new SpannableString(item.getNamaProyek());
                            }
                        };
                        opsi.setSpinnerTextFormatter(formater);
                        opsi.setSelectedTextFormatter(formater);
                        opsi.attachDataSource(resultItemProyek);
                        ResultItemProyek item = (ResultItemProyek) opsi.getSelectedItem();
                        setIdProyek(item.getId());


                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {

            }
        });
    }



}
