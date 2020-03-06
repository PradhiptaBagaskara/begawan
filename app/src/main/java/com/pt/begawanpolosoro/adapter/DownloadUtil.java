package com.pt.begawanpolosoro.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.pdf.PdfReaderActivity;

import java.io.File;


public class DownloadUtil {
    Context context;
    DownloadManager downloadManager;
    InitRetro initRetro;
    private static final String path = Environment.getExternalStorageDirectory().getPath()
            +File.separator+"PT. BEGAWAN POLOSORO";
    public DownloadUtil(Context c) {
        this.context = c;
        this.initRetro = new InitRetro(context);

    }

    private DownloadManager dm(){
        if (downloadManager == null){
            downloadManager = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return downloadManager;
    }

    public long startDownload(String uri, String fname){
        cekDir();
        if (downloadManager == null){
            downloadManager = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        DownloadManager.Request req =  new DownloadManager.Request(Uri.parse(uri));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        req.setTitle(fname);
        req.setDescription(fname+" Berhasil didownload");
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        req.setDestinationInExternalFilesDir(context, path,fname);
        req.setDestinationInExternalPublicDir(path, fname);
        req.setMimeType("*/*");
//        req.setDestinationUri()
        Log.d("downloaded", "startDownload: " + fname);


        long id = downloadManager.enqueue(req);

        return id;
    }

    private void cekDir(){
        File yourAppDir = new File(path);

        if(!yourAppDir.exists() && !yourAppDir.isDirectory())
        {
            // create empty directory
            if (yourAppDir.mkdirs())
            {
                Log.i("CreateDir","App dir created");
            }
            else
            {
                Log.w("CreateDir","Unable to create app dir!");
            }
        }
        else
        {
            Log.i("CreateDir","App dir already exists");
        }
    }

    public long startPrDOwnloader(String url, String fname){
        // Enabling database for resume support even after the application is killed:

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)

                .build();
        cekDir();

        PRDownloader.initialize(context.getApplicationContext(), config);

        long d = PRDownloader.download(url, path ,fname).build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setIcon(R.drawable.ic_delete);

                        alertDialog.setTitle("Download Sukses!");
                        alertDialog.setCancelable(true);
                        alertDialog
                                .setMessage("Apakah anda ingin membuka file?")
                                .setIcon(R.mipmap.ic_icon_round)
                                .setCancelable(false)
                                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        Intent intent = new Intent(context, PdfReaderActivity.class);
                                        intent.putExtra("file", path+"/"+fname);
                                        context.startActivity(intent);


                                    }
                                })
                                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // jika tombol ini diklik, akan menutup dialog
                                        // dan tidak terjadi apa2
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = alertDialog.create();
                        alertDialog.show();
                        Log.d("download" , "complete: "+fname);
                    }

                    @Override
                    public void onError(Error error) {
                        Log.d("erorr", "onError: error");
                        Toast.makeText(context, "Terjadi kesalahan! coba lagi nanti", Toast.LENGTH_SHORT).show();
                    }
                });
        return d;
    }
}
