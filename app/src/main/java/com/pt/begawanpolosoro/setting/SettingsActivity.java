package com.pt.begawanpolosoro.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.pt.begawanpolosoro.BuildConfig;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.DownloadUtil;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.login.api.ResponseLogin;
import com.pt.begawanpolosoro.login.api.ResultLogin;
import com.pt.begawanpolosoro.update.ResponseUpdate;
import com.pt.begawanpolosoro.user.api.ResponseUser;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "settingsActivityTitle";
    private static final int INSTALL_PACKAGES_REQUESTCODE = 102;
    private static final int GET_UNKNOWN_APP_SOURCES = 100;
    LinearLayout editProfile, gantiPassword, logout, reset, linierBtn, linierNama, linierUname, linierPass;
    ProgressBar pg;
    MaterialEditText vNama, vPassword, vUname, vKodeVerif;
    ImageButton back;
    Dialog dialog;
    ApiHelper apiHelper = new ApiHelper();
    InitRetro initRetro;
    CurrentUser user;
    Button cancel, submit;
    TextView labelNama, labelPass, labelUname, tKodeVerif;
    SessionManager sm;
    ProgressDialog pd;
    AlertDialog dialogAlert;

    public int getKodeVerif() {
        return kodeVerif;
    }

    public void setKodeVerif(int kodeVerif) {
        this.kodeVerif = kodeVerif;
    }

    int kodeVerif;
    DownloadUtil downloadUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.my_statusbar_color));

        initRetro = new InitRetro(getApplicationContext());
        user = new CurrentUser(getApplicationContext());
        sm = new SessionManager(getApplicationContext());
        downloadUtil = new DownloadUtil(getApplicationContext());
        reset = findViewById(R.id.resetData);
        if (user.getRole() != 2)
            reset.setVisibility(View.GONE);


        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });
    }

    private void customDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_home);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparant);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pg = dialog.findViewById(R.id.progresProfil);
        pg.setVisibility(View.GONE);
        vNama = dialog.findViewById(R.id.dialogNama);
        vPassword = dialog.findViewById(R.id.dialogPassword);
        vUname = dialog.findViewById(R.id.dialogUname);
        linierNama = dialog.findViewById(R.id.linierDnama);
        linierPass = dialog.findViewById(R.id.linierDpass);
        linierUname = dialog.findViewById(R.id.linierDuname);
        linierBtn = dialog.findViewById(R.id.linierBtn);
        cancel = dialog.findViewById(R.id.cancelDialog);
        submit = dialog.findViewById(R.id.saveDialog);
        vNama.setText(user.getsNama());
        vUname.setText(user.getsUsername());
        dialog.show();
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }


    public void actionProfil(View view) {
        customDialog(view.getContext());
        linierPass.setVisibility(View.GONE);
        if (user.getRole() != 2) {
            linierUname.setVisibility(View.GONE);
        }
        submit.setOnClickListener(v -> updateProfil());
    }

    private void dialogReset(Context context) {
        dialog = new Dialog(context);
        dialog.setTitle("Reset Data");
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reset);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparant);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tKodeVerif = dialog.findViewById(R.id.kodeVerifOutput);
        vKodeVerif = dialog.findViewById(R.id.kodeVerif);
        linierBtn = dialog.findViewById(R.id.linierBtn);
        submit = dialog.findViewById(R.id.saveDialog);
        pg = dialog.findViewById(R.id.progresProfil);
        pg.setVisibility(View.GONE);
        cancel = dialog.findViewById(R.id.cancelDialog);
        setKodeVerif(new Random().nextInt(9999 - 1111) + 1111);

        tKodeVerif.setText(String.valueOf(getKodeVerif()));
        dialog.show();

        cancel.setOnClickListener(v -> dialog.dismiss());

    }


    public void actionPassword(View view) {
        customDialog(view.getContext());
        linierNama.setVisibility(View.GONE);
        linierUname.setVisibility(View.GONE);
        vPassword.getText().clear();
        submit.setOnClickListener(v -> updatePass());


    }

    private void updatePass() {
        linierBtn.setVisibility(View.GONE);
        pg.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(vPassword.getText().toString())) {
            vPassword.setError("Form Tidak Boleh Kosong!");
        } else {
            if (user.getRole() == 2) {
                Call<ResponseUser> u = initRetro.apiRetro().resetPassword(user.getsAuth(), user.getsAuth(), vPassword.getText().toString());
                u.enqueue(new Callback<ResponseUser>() {
                    @Override
                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                        linierBtn.setVisibility(View.GONE);
                        pg.setVisibility(View.VISIBLE);
                        if (response.isSuccessful()) {
                            if (response.body().isStatus()) {
                                dialog.dismiss();
                            }
                        }
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseUser> call, Throwable t) {
                        linierBtn.setVisibility(View.GONE);
                        pg.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), getString(R.string.kesalahan), Toast.LENGTH_LONG).show();
                        t.printStackTrace();

                    }
                });
            } else {
                Call<ResponseLogin> up = initRetro.apiRetro().updateUser(user.getsAuth(), vPassword.getText().toString(), user.getsNama());
                up.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        linierBtn.setVisibility(View.GONE);
                        pg.setVisibility(View.VISIBLE);
                        if (response.isSuccessful())
                            if (response.body().isStatus())
                                dialog.dismiss();
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        linierBtn.setVisibility(View.GONE);
                        pg.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), getString(R.string.kesalahan), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });

            }
        }

    }

    private void updateProfil() {
        linierBtn.setVisibility(View.GONE);
        pg.setVisibility(View.VISIBLE);
        Call<ResponseLogin> up;
        if (user.getRole() == 2) {
            up = initRetro.apiRetro().updateAdmin(user.getsAuth(), vUname.getText().toString(), vNama.getText().toString());
        } else {
            up = initRetro.apiRetro().updateUser(user.getsAuth(), "", vNama.getText().toString());
        }
        up.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                pg.setVisibility(View.GONE);
                linierBtn.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        ResultLogin dt = response.body().getResult();
                        sm.storeLogin(dt.getRole(), dt.getNama(), dt.getUsername(), dt.getId());
                        dialog.dismiss();
                    }
                }
                Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                pg.setVisibility(View.GONE);
                linierBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan! Coba lagi nanti", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }

    public void actionLogout(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());

        alertDialog.setTitle("Logout Aplikasi");
        alertDialog
                .setIcon(R.drawable.ic_warning_oren)
                .setMessage("Apakah Anda yakin ingin logout?")
                .setCancelable(true)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        user.logout();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void resetData() {
        if (TextUtils.isEmpty(vKodeVerif.getText())) {
            vKodeVerif.setError("Kode Harus Diisi");
        } else {
            int k = Integer.parseInt(vKodeVerif.getText().toString());
            if (k == getKodeVerif()) {
                linierBtn.setVisibility(View.GONE);
                pg.setVisibility(View.VISIBLE);
                Call<ResponseLogin> p = initRetro.apiRetro().deleteUser(user.getsAuth(), "", "reset");
                p.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        linierBtn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        if (response.isSuccessful())
                            if (response.body().isStatus())
                                dialog.dismiss();
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        linierBtn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), getString(R.string.kesalahan), Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                vKodeVerif.setError("Kode Tidak Sama");

            }
        }
    }

    public void actionReset(View view) {
        dialogReset(view.getContext());
        submit.setOnClickListener(v -> resetData());


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void progres() {
        dialog = new Dialog(SettingsActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progres);
        dialog.setCancelable(true);
//        dialog.getWindow()
//        dialog.getWindow().setBackgroundDrawableResource(R.color.blur);
//        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean result = getPackageManager().canRequestPackageInstalls();
            if (result) {
                doUpdate();
            } else {
                // request the permission
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                alertDialog.setTitle("Pengaturan Diperlukan!");
                alertDialog
                        .setMessage("Izinkan " + getString(R.string.app_name) + " untuk menginstall update dari sumber yang tidak dikenal?")
                        .setIcon(R.drawable.ic_notifications)
                        .setCancelable(true)
                        .setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);


                            }

                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogAlert.dismiss();
                    }
                });
                dialogAlert = alertDialog.create();
                dialogAlert.show();
                Log.d(TAG, "checkIsAndroidO: permission denied");
//                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]
//                        {Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            doUpdate();
        }
    }

    public void actionCheck(View view) {
        progres();


//        Log.i(TAG, "actionCheck: "+downloadUtil.md5(versi));
        checkIsAndroidO();
    }

    private void doUpdate() {
        dialog.show();

        String versi = BuildConfig.VERSION_NAME;
        String md5 = downloadUtil.md5(versi);
        Call<ResponseUpdate> up = initRetro.apiRetro().getUpdate("cek");
        up.enqueue(new Callback<ResponseUpdate>() {
            @Override
            public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                dialog.cancel();

                if (response.isSuccessful()) {
                    String md5Server = response.body().getHash();
                    if (!md5.equals(md5Server)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                        alertDialog.setTitle("Update Tersedia");
                        alertDialog
                                .setMessage("Update Aplikasi Sekarang?")
                                .setIcon(R.drawable.ic_notifications)
                                .setCancelable(true)
                                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        downloadUtil.downloadInit();
                                        String dir = downloadUtil.getUpdateDir() + File.separator + "update.apk";
                                        pdDownload();
                                        File capk = new File(dir);
                                        if (capk.exists() && capk.isFile()) {
                                            capk.delete();
                                        }

//                                        int max = (int) response.body().getBytes();
//                                        pd.setMax(max);
                                        PRDownloader.download(response.body().getDownloadUrl(), downloadUtil.getUpdateDir(), "update.apk")
                                                .build()
                                                .setOnProgressListener(new OnProgressListener() {
                                                    @Override
                                                    public void onProgress(Progress progress) {
                                                        float total = (progress.totalBytes / 1024) / 1024;
                                                        float cur = ((progress.currentBytes / 1024) / 1024) * 100;
                                                        int tc = (int) (cur / total);
                                                        pd.setProgress(tc);


                                                    }
                                                }).start(new OnDownloadListener() {
                                            @Override
                                            public void onDownloadComplete() {
                                                pd.dismiss();
//                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                File apk = new File(downloadUtil.getUpdateDir() + File.separator + "update.apk");
//                                                Uri uri = Uri.fromFile(apk);
                                                if (apk.exists() && apk.isFile()) {
                                                    downloadUtil.installApk(apk);

                                                }
                                            }

                                            @Override
                                            public void onError(Error error) {
                                                pd.dismiss();
                                                Log.e(TAG, "onError: ", error.getConnectionException());
                                                downloadUtil.errorLog(error.getConnectionException().toString(), TAG);
                                                downloadUtil.errorLog(error.getConnectionException().getLocalizedMessage(), TAG);
                                                downloadUtil.errorLog(error.getConnectionException().getMessage(), TAG);

                                                Toast.makeText(getApplicationContext(), getString(R.string.kesalahan), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // jika tombol ini diklik, akan menutup dialog
                                        // dan tidak terjadi apa2
                                        dialogAlert.cancel();
                                    }
                                });
                        dialogAlert = alertDialog.create();
                        dialogAlert.show();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                        alertDialog.setTitle("Aplikasi Uptodate");
                        alertDialog
                                .setMessage("Tidak tersedia update untuk sekarang")
                                .setIcon(R.drawable.ic_notifications)
                                .setCancelable(true)
                                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialogAlert.dismiss();
//                                        Toast.makeText(view.getContext(), "CLOSE", Toast.LENGTH_LONG).show();

                                    }

                                });
                        dialogAlert = alertDialog.create();
                        dialogAlert.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Kesalaha Dalam Memuat Data!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Kesalaha Dalam Memuat Data!", Toast.LENGTH_LONG).show();
                t.printStackTrace();


            }
        });
    }


    private void pdDownload() {
        pd = new ProgressDialog(SettingsActivity.this);
        pd.setMessage("Sedang Mendownload File. Mohon Tunggu...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setMax(100);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_UNKNOWN_APP_SOURCES) {
            checkIsAndroidO();
        }
    }
}