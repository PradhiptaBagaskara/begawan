package com.pt.begawanpolosoro.pekerja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.congfandi.lib.EditTextRupiah;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.PekerjaControlerActivity;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.ResponseTx;
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.proyek.api.ResultItemProyek;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.angmarch.views.SpinnerTextFormatter;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PekerjaActivity extends AppCompatActivity {
    MaterialEditText namaTx, keteranganTx;
    EditTextRupiah totalDana;
    NiceSpinner opsi,jenisBayar;
    ProgressBar pg;
    int IMAGE_REQ_CODE = 101;
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
    ImageButton back;
    FloatingActionButton uploadImg;
    AppCompatImageView img;
    ApiHelper apiHelper = new ApiHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pekerja);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));
        img = findViewById(R.id.imgCamera);

        InitRetro initRetro = new InitRetro(getApplicationContext());
        apiService = initRetro.InitApi().create(ApiService.class);
        user = new CurrentUser(getApplicationContext());
        setIdProyek("");
        uploadImg = findViewById(R.id.fab_img);

        pg = findViewById(R.id.progresBaru);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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
//                Toast.makeText(getApplicationContext(), "Selected Jenis: " + item, Toast.LENGTH_SHORT).show();
            }
        });
        totalDana = findViewById(R.id.totalTx);
        opsi = findViewById(R.id.roleBaru);
        opsi.setText("BELUM ADA PROYEK");
        loadProyek();




        btn.setOnClickListener(send);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: "+resultCode+"-"+requestCode);
        if (resultCode == Activity.RESULT_OK){
            File dt = ImagePicker.Companion.getFile(data);
            String path = ImagePicker.Companion.getFilePath(data);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(this)
                    .load(dt)
                    .into(img);
            apiHelper.setImgPath(path);


        }
    }

    public void pickImg(View v){
        ImagePicker.Companion.with(this)
//                .cropSquare()
                .compress(256)
                .maxResultSize(1080, 1080)
                .start();
    }

    private View.OnClickListener send = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), getJenis(), Toast.LENGTH_SHORT).show();
            if (cekField()){
                btn.setVisibility(View.GONE);
                File file = new File(apiHelper.getImgPath());
                RequestBody auth = RequestBody.create(MediaType.parse("text/plain"), user.getsAuth());
                RequestBody idProyek = RequestBody.create(MediaType.parse("text/plain"), getIdProyek());
                RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), namaTx.getText().toString());
                RequestBody jen = RequestBody.create(MediaType.parse("text/plain"), getJenis());
                RequestBody cat = RequestBody.create(MediaType.parse("text/plain"), keteranganTx.getText().toString());
                RequestBody pGTotal = RequestBody.create(MediaType.parse("text/plain"), totalDana.getNumber());

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part poto = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                Call<ResponseTx> r = apiService.postTx(auth, idProyek,nama, pGTotal,cat,jen, poto);
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
//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }

    private boolean cekField(){
        if (TextUtils.isEmpty(namaTx.getText())){
            namaTx.setError("Nama Pembelian Harus Diisi");
            return false;
        }else if (TextUtils.isEmpty(getIdProyek())){
            opsi.setError("Catatan Pembelian Harus Diisi");
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
                        if (response.body().getResult().size() != 0){
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
                            if ( response.body().getResult().size() == 1){
                                opsi.setText(item.getNamaProyek());
                            }
                            opsi.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                                @Override
                                public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                                    ResultItemProyek result = (ResultItemProyek) opsi.getSelectedItem();
                                    setIdProyek(result.getId());
//                Toast.makeText(getApplicationContext(), result.getId()+" "+result.getNamaProyek(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }




                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {

            }
        });
    }



}
