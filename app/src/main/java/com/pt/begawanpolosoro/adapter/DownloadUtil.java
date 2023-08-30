package com.pt.begawanpolosoro.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.pdf.PdfReaderActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DownloadUtil {
    private static final String TAG = "DownloadUtil";
    Context context;
    DownloadManager downloadManager;
    InitRetro initRetro;
    private static final String path = Environment.getExternalStorageDirectory().getPath()
            +File.separator;
    public static final  String mediaPaths = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES) + File.separator;
    public static final String updatePath = Environment.getExternalStorageState()+"update";
    public DownloadUtil(Context c) {
        this.context = c;
        this.initRetro = new InitRetro(context);

    }
    public String getUpdateDir(){
        String dir = path+File.separator+context.getString(R.string.pt_name)+File.separator+"update";
        return dir;
    }

    private DownloadManager dm(){
        if (downloadManager == null){
            downloadManager = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return downloadManager;
    }
    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
        String error = dir+File.separator+"error_log";
        File yourAppDir = new File(dir);
        File imgDir = new File(img);
        File errorDir = new File(error);


        if(!errorDir.exists() && !errorDir.isDirectory())
        {
            // create empty directory
            if (errorDir.mkdirs())
            {
                Log.i("CreateDir","Error dir created");
            }
            else
            {
                Log.w("CreateDir","Unable to create Error dir!");
            }
        }
        else
        {
            Log.i("CreateDir","Error dir already exists");
        }

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
    public void errorLog(String text, String filename)
    {
        String dir = path+context.getString(R.string.pt_name);
        String error = dir+File.separator+"error_log";
        File logFile = new File(error+File.separator+filename+"_error_log.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    public void checkPermission(String permission, int requestCode, Activity activity)
    {
        if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission

            ActivityCompat.requestPermissions(activity,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Log.d(TAG, "checkPermission: GRANTED");

        }
    }
    public void installApk(File file){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        Log.d(TAG, "installApk: mime "+ type);
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

    public void openPDF(File file){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        Log.d(TAG, "openpdf: mime "+ type);
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
            Toast.makeText(context, "Versi Android Tidak Compatible! Silakan buka PDF di " + Uri.fromFile(file).toString(), Toast.LENGTH_LONG).show();
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
                        Log.d(TAG, "onDownloadComplete: download complete");
                        Toast.makeText(context, "Download Selesai", Toast.LENGTH_SHORT).show();
                        File file = new File(laporanPath+File.separator+fname);
                        openPDF(file);

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
