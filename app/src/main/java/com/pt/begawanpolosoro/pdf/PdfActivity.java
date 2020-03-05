package com.pt.begawanpolosoro.pdf;

import android.Manifest;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.DownloadUtil;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.pdf.api.ResponsePdf;
import com.pt.begawanpolosoro.proyek.api.ResponseProyek;
import com.pt.begawanpolosoro.proyek.api.ResultItemProyek;
import com.pt.begawanpolosoro.user.api.ResponseUser;
import com.pt.begawanpolosoro.user.api.ResultItemUser;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.angmarch.views.SpinnerTextFormatter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfActivity extends AppCompatActivity {
    NiceSpinner option,list;
    ImageButton back;
    Button downloadBtn;
    DownloadUtil downloadUtil;

    public int getOpsi() {
        return opsi;
    }

    public void setOpsi(int opsi) {
        this.opsi = opsi;
    }

    int opsi;
    InitRetro initRetro;
    CurrentUser user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getUrl() {
        return initRetro.BASE_URL+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    String param;
    ProgressBar pg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));

        initRetro = new InitRetro(getApplicationContext());
        user = new CurrentUser(getApplicationContext());
        setUrl("");
        setParam("");
        setId("");
        downloadUtil = new DownloadUtil(this);

        option = findViewById(R.id.laporanBaru);

        list = findViewById(R.id.listLaporan);
        list.setText("KOSONG");
        back = findViewById(R.id.back);
        downloadBtn = findViewById(R.id.btnDownload);
        pg = findViewById(R.id.progresBaru);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        List<String> dataset = new LinkedList<>(Arrays.asList("PROYEK", "KARYAWAN"));
        option.setText("PILIH JENIS PROYEK");
        option.attachDataSource(dataset);
        option.setOnSpinnerItemSelectedListener(opsiListener);
        downloadBtn.setOnClickListener(donlod);
        pg.setVisibility(View.GONE);
        loadLaporan(0);
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {


                    }
                })
                .check();



    }


    private View.OnClickListener donlod = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (TextUtils.isEmpty(getId())){
                list.setError("Form Tidak Boleh Kosong");

            }else {
                Dexter.withActivity(PdfActivity.this)
                        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                                downloadBtn.setEnabled(false);
                                if (report.areAllPermissionsGranted()){
                                    downloadBtn.setVisibility(View.GONE);
                                    pg.setVisibility(View.VISIBLE);
                                    getPdf(getId(), getParam());
                                }else {
                                    Toast.makeText(getApplicationContext(), "Permisi diperlukan", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                downloadBtn.setEnabled(true);

                            }
                        })
                        .check();


//            Toast.makeText(getApplicationContext(), getId(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(), getParam(), Toast.LENGTH_SHORT).show();
            }



        }
    };

    private OnSpinnerItemSelectedListener opsiListener = new OnSpinnerItemSelectedListener() {
        @Override
        public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
            setOpsi(position);
            loadLaporan(position);


        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    void loadLaporan(int position) {
        switch (position) {
            case 0:
                setParam("proyek");
                loadProyek();
                break;
            case 1:
                setParam("user");
                loadUser();
                break;
        }
    }

    void loadProyek(){
        Call<ResponseProyek> proyekCall = initRetro.apiRetro().getProyek(user.getsAuth());
        proyekCall.enqueue(new Callback<ResponseProyek>() {
            @Override
            public void onResponse(Call<ResponseProyek> call, Response<ResponseProyek> response) {

                if (response.isSuccessful())
                    if (response.body().isStatus()){
                        List<ResultItemProyek> resultItemProyek = response.body().getResult();

                        SpinnerTextFormatter formater = new SpinnerTextFormatter<ResultItemProyek>() {
                            @Override
                            public Spannable format(ResultItemProyek item) {
                                return new SpannableString(item.getNamaProyek());
                            }
                        };
                        list.setSpinnerTextFormatter(formater);
                        list.setSelectedTextFormatter(formater);
                        list.attachDataSource(resultItemProyek);
                        ResultItemProyek item = (ResultItemProyek) list.getSelectedItem();
                        setId(item.getId());
                        list.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                            @Override
                            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                                ResultItemProyek res = (ResultItemProyek) list.getSelectedItem();
                                setId(res.getId());
                            }
                        });
                    }
            }

            @Override
            public void onFailure(Call<ResponseProyek> call, Throwable t) {
                downloadBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan! Coba lagi nanti", Toast.LENGTH_LONG).show();


            }
        });
    }

    void loadUser(){
        Call<ResponseUser> u = initRetro.apiRetro().getUser(user.getsAuth());
        u.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        List<ResultItemUser> resultItemUser = response.body().getResult();

                        SpinnerTextFormatter format = new SpinnerTextFormatter<ResultItemUser>() {
                            @Override
                            public Spannable format(ResultItemUser item) {
                                return new SpannableString(item.getNama());
                            }
                        };
                        list.setSpinnerTextFormatter(format);
                        list.setSelectedTextFormatter(format);
                        list.attachDataSource(resultItemUser);
                        ResultItemUser item = (ResultItemUser) list.getSelectedItem();
                        setId(item.getId());
                        list.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                            @Override
                            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                                ResultItemUser res = (ResultItemUser) list.getSelectedItem();
                                setId(res.getId());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan! Coba lagi nanti", Toast.LENGTH_LONG).show();

                downloadBtn.setVisibility(View.VISIBLE);
                t.printStackTrace();

            }
        });
    }

    void getPdf(String ids, String param){
        Call<ResponsePdf> pd = initRetro.apiRetro().pdf(user.getsAuth(), ids, param);
        pd.enqueue(new Callback<ResponsePdf>() {
            @Override
            public void onResponse(Call<ResponsePdf> call, Response<ResponsePdf> response) {
               downloadBtn.setVisibility(View.VISIBLE);
               pg.setVisibility(View.GONE);
                if (response.isSuccessful()){

                    if (response.body().isStatus()){
                        setUrl(response.body().getResult().get(0).getFileName());
                        String filename = response.body().getResult().get(0).getFileName();
                        filename = filename.replace("uploads/", "");
//                        downloadUtil.startDownload(getUrl(), filename);
                        downloadUtil.startPrDOwnloader(getUrl(), filename);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponsePdf> call, Throwable t) {
                downloadBtn.setVisibility(View.VISIBLE);
                pg.setVisibility(View.GONE);

                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan! Coba lagi nanti", Toast.LENGTH_LONG).show();


            }
        });
    }
}
