package com.pt.begawanpolosoro.transaksi;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.congfandi.lib.TextViewRupiah;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.DownloadUtil;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.ResultItemTx;
import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.proyek.api.TransaksiItem;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.File;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TxDetailActivity extends AppCompatActivity {

    private static final String TAG = "TxDetailActivity";
    MaterialEditText nama, namaTx, jenisTx, tgl, jenisBayar, id,namaP,pelunasan;
    AppCompatTextView titleImg;
    TextViewRupiah total;
    ImageButton back;
    ApiHelper apiHelper = new ApiHelper();
    InitRetro initRetro;
    DownloadUtil downloadUtil;
    AppCompatImageView img;
    FloatingActionButton uploadImg;
    Button lunasBtn;
    CurrentUser user ;

    TextView dialogLabelUtang;
    Button btnCloseUtang, btnSendUtang;
    LinearLayout dialogLinarUtang;
    ProgressBar pgUtang;
    TextViewRupiah jumlahUtang;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.darkBlue));
        initRetro = new InitRetro(getApplicationContext());
        Intent extra = getIntent();
        user = new CurrentUser(getApplicationContext());
        downloadUtil = new DownloadUtil(getApplicationContext());

//        id = findViewById(R.id.id_tx);
        nama = findViewById(R.id.nama_pekerja);
        namaTx = findViewById(R.id.judul_pembelian);
        jenisBayar = findViewById(R.id.jenis_bayar);
        tgl = findViewById(R.id.tgl);
        titleImg = findViewById(R.id.txtCamera);
        titleImg.setText(getString(R.string.bukti_tx));
        total = findViewById(R.id.dana);
        jenisTx = findViewById(R.id.keterangan);
        namaP = findViewById(R.id.namaProyek);
        back = findViewById(R.id.back);
        pelunasan = findViewById(R.id.statusPelunasan);
        lunasBtn = findViewById(R.id.lunasiBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (extra.hasExtra("data")){
            ResultItemTx dt = (ResultItemTx) extra.getSerializableExtra("data");
            apiHelper.setNama(dt.getNama());
            apiHelper.setModal(dt.getDana());
            apiHelper.setId_(dt.getId());
            apiHelper.setModal(dt.getDana());
            nama.setText(dt.getNama());
            namaTx.setText(dt.getNamaTransaksi());
            jenisBayar.setText(dt.getJenis());
            tgl.setText(dt.getCreatedDate());
            total.convertToIDR(dt.getDana());
            String ket = dt.getKeterangan();
            pelunasan.setText(dt.getStatus());
            apiHelper.setStatus(dt.getStatus());

            jenisTx.setText(ket);
            namaP.setText(dt.getNamaProyek());
            apiHelper.setImgPath(downloadUtil.mediaPaths+getString(R.string.img_dir)+"/" + dt.getFileName());

            apiHelper.setImgUrl(initRetro.BASE_URL+"uploads/transaksi/"+dt.getFileName());
            apiHelper.setFname(dt.getFileName());




        }else if (extra.hasExtra("tx")){
            TransaksiItem dt = (TransaksiItem) extra.getSerializableExtra("tx");
            apiHelper.setNama(dt.getNama());
            apiHelper.setModal(dt.getDana());
            apiHelper.setId_(dt.getId());
            apiHelper.setModal(dt.getDana());
            nama.setText(dt.getNama());
            namaTx.setText(dt.getNamaTransaksi());
            jenisBayar.setText(dt.getJenis());
            tgl.setText(dt.getCreatedDate());
            total.convertToIDR(dt.getDana());
            String ket = dt.getKeterangan();
            pelunasan.setText(dt.getStatus());
            apiHelper.setStatus(dt.getStatus());

            jenisTx.setText(ket);
            namaP.setText(dt.getNamaProyek());
            apiHelper.setImgPath(downloadUtil.mediaPaths+getString(R.string.img_dir)+"/" + dt.getFileName());

            apiHelper.setImgUrl(initRetro.BASE_URL+"uploads/transaksi/"+dt.getFileName());
            apiHelper.setFname(dt.getFileName());
        }else {
            finish();
        }
        if (!apiHelper.getStatus().equals("lunas") && user.getRole() > 1){
            lunasBtn.setVisibility(View.VISIBLE);
        }
        img = findViewById(R.id.imgCamera);
        uploadImg = findViewById(R.id.fab_img);
        uploadImg.setImageResource(R.drawable.ic_download);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        if (downloadUtil.imgExist(apiHelper.getImgPath())){
            Log.d(TAG, "onCreate: Load File From Storage ");
            uploadImg.setVisibility(View.GONE);
            File f = new File(apiHelper.getImgPath());
            Uri uri = Uri.fromFile(f);
            Picasso.with(this)
                    .load(uri)
                    .into(img);
            img.setOnClickListener(imgClick);
        }else {
            Picasso.with(this)
                    .load(apiHelper.getImgUrl())
                    .transform(new BlurTransformation(this))
                    .into(img);

            Log.d(TAG, "onCreate: Load Image From Uri ");

        }
    }

    private void utangDialog(){
        dialog = new Dialog(TxDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_utang);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparant);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogLinarUtang = dialog.findViewById(R.id.linierBtn);
        dialogLabelUtang = dialog.findViewById(R.id.dialoglabelNama);
        btnCloseUtang = dialog.findViewById(R.id.cancelDialog);
        btnSendUtang = dialog.findViewById(R.id.lunasiBtn);
        pgUtang = dialog.findViewById(R.id.progres);
        jumlahUtang = dialog.findViewById(R.id.jumlahUtang);
        String label = getString(R.string.label_dialog_utang);
        label = label.replace("pengguna", apiHelper.getNama());
        label = label.replace("semua", "");
        label = label.replace("Semua ", "");
        label = label.replace("hutang", "Hutang");

        dialogLabelUtang.setText(label);
        try {
            jumlahUtang.convertToIDR(apiHelper.getModal());
        }catch (Exception e){
            e.printStackTrace();
        }
        btnCloseUtang.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        btnSendUtang.setOnClickListener(utangListener);

    }

    private View.OnClickListener utangListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pgUtang.setVisibility(View.VISIBLE);
            dialogLinarUtang.setVisibility(View.GONE);
            Call<ResponseSaldo> p = initRetro.apiRetro().hutang(user.getsAuth(),apiHelper.getId_(),apiHelper.getModal(),"single");
            p.enqueue(new Callback<ResponseSaldo>() {
                @Override
                public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {
                    pgUtang.setVisibility(View.GONE);
                    dialogLinarUtang.setVisibility(View.VISIBLE);
                    if (response.isSuccessful())
                        if (response.body().isStatus())
                            dialog.dismiss();
                            finish();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseSaldo> call, Throwable t) {
                    pgUtang.setVisibility(View.GONE);
                    dialogLinarUtang.setVisibility(View.VISIBLE);
                    t.printStackTrace();

                }
            });
        }
    };


    public void lunasiUtang(View view) {
        utangDialog();
    }
    public void pickImg(View view){
        String imgPath = downloadUtil.downloadInit();

        PRDownloader.download(apiHelper.getImgUrl(), imgPath,apiHelper.getFname()).build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Toast.makeText(getApplicationContext(), "Gambar Berhasil di Download", Toast.LENGTH_SHORT).show();
                        Log.d("download" , "complete: Download image  "+apiHelper.getImgPath());
                        uploadImg.setVisibility(View.GONE);
                        File f = new File(apiHelper.getImgPath());
                        Uri uri = Uri.fromFile(f);
                        downloadUtil.showImg(f);
                        Picasso.with(getApplicationContext()).load(uri).into(img);
                        img.setOnClickListener(imgClick);
                    }

                    @Override
                    public void onError(Error error) {
                        Log.d("erorr", "onError: error");

                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan! coba lagi nanti", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File file = new File(apiHelper.getImgPath());
            downloadUtil.showImg(file);
        }
    };

}
