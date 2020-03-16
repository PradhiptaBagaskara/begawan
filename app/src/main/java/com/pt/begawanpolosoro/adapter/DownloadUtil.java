package com.pt.begawanpolosoro.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.pdf.PdfReaderActivity;

import java.io.File;



public class DownloadUtil {
    private static final String TAG = "DownloadUtil";
    Context context;
    DownloadManager downloadManager;
    InitRetro initRetro;
    private static final String path = Environment.getExternalStorageDirectory().getPath()
            +File.separator;
    public static final  String mediaPaths = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES) + File.separator;
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

    public String downloadInit(){
        cekDir();

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)

                .build();
        cekDir();

        PRDownloader.initialize(context.getApplicationContext(), config);
        String imgPath = mediaPaths + context.getString(R.string.img_dir);
        return imgPath;
    }

    public void cekDir(){
        String dir = path+context.getString(R.string.pt_name);
        String img = mediaPaths + context.getString(R.string.img_dir);
        File yourAppDir = new File(dir);
        File imgDir = new File(img);

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

        if(!imgDir.exists() && !imgDir.isDirectory())
        {
            // create empty directory
            if (imgDir.mkdirs())
            {
                Log.i("CreateDir","Image dir created");
            }
            else
            {
                Log.w("CreateDir","Unable to create Image dir!");
            }
        }
        else
        {
            Log.i("CreateDir","Image dir already exists");
        }
    }

    public boolean imgExist(String fileName){
        File file = new File(fileName);
        if(file.exists() || file.isFile())
        {
            Log.i(TAG, "imgExist: ditemukan" + fileName);

            return true;
        }
        else
        {
            Log.i(TAG, "imgExist: Tidak ditemukan" + fileName);

            return false;
        }

    }
    public void showImg(File file){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        try {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                i.setDataAndType(contentUri, type);
            } else {
                i.setDataAndType(Uri.fromFile(file), type);
            }
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(context, "Versi Android Tidak Compatible!", Toast.LENGTH_LONG).show();
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
        String laporanPath = path+context.getString(R.string.pt_name);

        PRDownloader.initialize(context.getApplicationContext(), config);

        long d = PRDownloader.download(url, laporanPath ,fname).build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                        alertDialog.setTitle("Download Sukses!");
                        alertDialog.setCancelable(true);
                        alertDialog
                                .setMessage("Apakah anda ingin membuka file?")
                                .setIcon(R.mipmap.ic_icon_round)
                                .setCancelable(false)
                                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        Intent intent = new Intent(context, PdfReaderActivity.class);
                                        intent.putExtra("file", path+context.getString(R.string.pt_name)+"/"+fname);
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
