package com.pt.begawanpolosoro.gaji;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.congfandi.lib.TextViewRupiah;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.DownloadUtil;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.pekerja.gaji.api.ResultItemGaji;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.File;

public class GajiDetailActivity extends AppCompatActivity {
    private static final String TAG = "GajiDetailActivity";
    ImageButton back;
    InitRetro initRetro;
    CurrentUser user;
    TextViewRupiah gaji;
    LinearLayout linierBtn;
    MaterialEditText karyawan,proyek, tgl, pengirim;
    String id;
    MaterialEditText catatan;
    AppCompatImageView img;
    FloatingActionButton uploadImg;
    ApiHelper apiHelper = new ApiHelper();
    DownloadUtil downloadUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaji_detail);
        Intent extra = getIntent();
        downloadUtil = new DownloadUtil(getApplicationContext());
        initRetro = new InitRetro(getApplicationContext());
        apiHelper.setModal("10");

        if (extra.getExtras().getSerializable("data") != null){
            Log.i(TAG, "onCreate: list" + extra.getExtras().getSerializable("data").toString());
            ResultItemGaji data = (ResultItemGaji) extra.getExtras().getSerializable("data");
            apiHelper.setNama_proyek(data.getNamaProyek());
            apiHelper.setImgPath(downloadUtil.mediaPaths+getString(R.string.img_dir)+"/" + data.getFileName());
            apiHelper.setKeterangan(data.getKeterangan());
            apiHelper.setPenerima(data.getNama());
            apiHelper.setPengirim(data.getNamaPengirim());
            apiHelper.setTglSelesai(data.getCreatedDate());
            apiHelper.setModal(data.getGaji());
            apiHelper.setFname(data.getFileName());
            apiHelper.setImgUrl(initRetro.BASE_URL+"uploads/"+data.getFileName());
        }else {
            Log.i(TAG, "onCreate: list" +extra.getExtras().getSerializable("data").toString());
        }
        if (extra.hasExtra("pekerjaan")){
            apiHelper.setNama_proyek(extra.getStringExtra("pekerjaan"));
            apiHelper.setImgPath(downloadUtil.mediaPaths+getString(R.string.img_dir)+"/" + extra.getStringExtra("file"));
            apiHelper.setKeterangan(extra.getStringExtra("catatan"));
            apiHelper.setPenerima(extra.getStringExtra("penerima"));
            apiHelper.setPengirim(extra.getStringExtra("pengirim"));
            apiHelper.setTglSelesai(extra.getStringExtra("tanggal"));
            apiHelper.setModal(extra.getStringExtra("jumlah"));
            apiHelper.setFname(extra.getStringExtra("file"));
            apiHelper.setImgUrl(initRetro.BASE_URL+"uploads/"+extra.getStringExtra("file"));
        }else {
            Log.i(TAG, "onCreate: ");
        }
        img = findViewById(R.id.imgCamera);
        linierBtn = findViewById(R.id.linierBtn);
        tgl = findViewById(R.id.tgl);
        pengirim = findViewById(R.id.pengirim);
        uploadImg = findViewById(R.id.fab_img);
        uploadImg.setImageResource(R.drawable.ic_download);

        gaji = findViewById(R.id.gajiBaru);
        karyawan = findViewById(R.id.karyawan);
        catatan = findViewById(R.id.catatan);
        proyek = findViewById(R.id.proyek);
        karyawan.setText(apiHelper.getPenerima());
        proyek.setText(apiHelper.getNama_proyek());
        String path = apiHelper.getImgPath();
        Log.d(TAG, "onCreate: "+path);
        if (downloadUtil.imgExist(path)){
            Log.d(TAG, "onCreate: Load File From Storage ");
            uploadImg.setVisibility(View.GONE);
            File f = new File(apiHelper.getImgPath());
            Uri uri = Uri.fromFile(f);
            Picasso.with(this)
                    .load(uri)
                    .into(img);
            img.setOnClickListener(imgClick);
        }else {
            Picasso.with(this).load(apiHelper.getImgUrl()).into(img);

            Log.d(TAG, "onCreate: Load Image From Uri ");

        }

//        gaji.convertToIDR(apiHelper.getModal());
        catatan.setText(apiHelper.getKeterangan());
        pengirim.setText(apiHelper.getPengirim());
        tgl.setText(apiHelper.getTglSelesai());
    }
    public void pickImg(View v){
        String imgPath = downloadUtil.downloadInit();

       PRDownloader.download(apiHelper.getImgUrl(), imgPath,apiHelper.getFname()).build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Toast.makeText(getApplicationContext(), "Gambar Berhasil di Download", Toast.LENGTH_SHORT).show();
                        Log.d("download" , "complete: Download image");
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
