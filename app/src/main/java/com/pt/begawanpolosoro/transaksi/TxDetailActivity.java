package com.pt.begawanpolosoro.transaksi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.DownloadUtil;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.ResultItemTx;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.File;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class TxDetailActivity extends AppCompatActivity {

    private static final String TAG = "TxDetailActivity";
    MaterialEditText nama, namaTx, jenisTx, tgl, jenisBayar, id,namaP;
    AppCompatTextView titleImg;
    TextViewRupiah total;
    ImageButton back;
    ApiHelper apiHelper = new ApiHelper();
    InitRetro initRetro;
    DownloadUtil downloadUtil;
    AppCompatImageView img;
    FloatingActionButton uploadImg;
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
        downloadUtil = new DownloadUtil(getApplicationContext());

//        id = findViewById(R.id.id_tx);
        nama = findViewById(R.id.nama_pekerja);
        namaTx = findViewById(R.id.judul_pembelian);
        jenisBayar = findViewById(R.id.jenis_bayar);
        tgl = findViewById(R.id.tgl);
        titleImg = findViewById(R.id.txtCamera);
        titleImg.setText("BUKTI TRANSAKSI");
        total = findViewById(R.id.dana);
        jenisTx = findViewById(R.id.keterangan);
        namaP = findViewById(R.id.namaProyek);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (extra.hasExtra("data")){
            ResultItemTx dt = (ResultItemTx) extra.getSerializableExtra("data");
            nama.setText(dt.getNama());
            namaTx.setText(dt.getNamaTransaksi());
            jenisBayar.setText(dt.getJenis());
            tgl.setText(dt.getCreatedDate());
            total.convertToIDR(dt.getDana());
            String ket = dt.getKeterangan();

            jenisTx.setText(ket);
            namaP.setText(dt.getNamaProyek());
            apiHelper.setImgPath(downloadUtil.mediaPaths+getString(R.string.img_dir)+"/" + dt.getFileName());

            apiHelper.setImgUrl(initRetro.BASE_URL+"uploads/transaksi/"+dt.getFileName());
            apiHelper.setFname(dt.getFileName());




        }else {

            finish();
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
