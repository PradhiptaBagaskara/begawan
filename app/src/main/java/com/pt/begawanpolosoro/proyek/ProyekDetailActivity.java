package com.pt.begawanpolosoro.proyek;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.congfandi.lib.EditTextRupiah;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.proyek.api.ResponseInsertProyek;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProyekDetailActivity extends AppCompatActivity {

    MaterialEditText nama,keterangan;
    Button btn;
    RelativeLayout btntglSelesai, btntglMulai;
    TextView tglSelesaiText, tglMulaiText, title, labelModal;
    ProgressBar pg;
    EditTextRupiah modal;
    ImageButton back, tglbar,editBatal,edit;
    ApiService apiService;
    CurrentUser user;
    DatePickerDialog datePickerDialog;
    Calendar date = Calendar.getInstance();

    ApiHelper apiHelper = new ApiHelper();
    private static final String TAG = "TambahProyekActivity";

    public int getPageMode() {
        return pageMode;
    }

    public void setPageMode(int pageMode) {
        this.pageMode = pageMode;
    }

    int pageMode;
    CoordinatorLayout rootView;

    public boolean isKeyboardStatus() {
        return keyboardStatus;
    }

    public void setKeyboardStatus(boolean keyboardStatus) {
        this.keyboardStatus = keyboardStatus;
    }

    boolean keyboardStatus;

    public void setCtt(String ctt) {
        this.ctt = ctt;
    }
    Locale locale = new Locale("id", "ID");
    InputMethodManager inputMethodManager;
    String ctt;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyek_detail);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));

        InitRetro initRetro = new InitRetro(getApplicationContext());
        user = new CurrentUser(getApplicationContext());
        apiService = initRetro.InitApi().create(ApiService.class);
        apiHelper = new ApiHelper();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setPageMode(0);
        setKeyboardStatus(false);

        Intent extra= getIntent();
        if (extra.getExtras() != null){
            apiHelper.setId_(extra.getStringExtra("id"));
            apiHelper.setKeterangan(extra.getStringExtra("catatan"));
            apiHelper.setModal(extra.getStringExtra("nilai"));
            apiHelper.setTglMulai(extra.getStringExtra("tglstart"));
            apiHelper.setNama_proyek(extra.getStringExtra("nama"));
            apiHelper.setTglSelesai(extra.getStringExtra("tglend"));
        }
        rootView = findViewById(R.id.rootView);
//        labelModal = findViewById(R.id.labelModal);
//        labelModal.setVisibility(View.GONE);
//        customTgl();

        title = findViewById(R.id.title);
        title.setText("DETAIL PEKERJAAN");
        nama = findViewById(R.id.namaProyek);
//        nama.setEnabled(false);
        modal = findViewById(R.id.modalProyek);
//        modal.setVisibility(View.GONE);
        keterangan = findViewById(R.id.keteranganProyek);
//        keterangan.setEnabled(false);
        btn = findViewById(R.id.btnBaru);
        btn.setVisibility(View.GONE);
        pg = findViewById(R.id.progresBaru);
        pg.setVisibility(View.GONE);
        btntglMulai = findViewById(R.id.tglMulai);
        btntglSelesai = findViewById(R.id.tglSelesai);
//        btntglMulai.setOnClickListener(tglStart);
        tglSelesaiText = findViewById(R.id.tglSelesaiText);
        tglbar = findViewById(R.id.tglSelesaiIc);
        edit = findViewById(R.id.edit);
        tglMulaiText = findViewById(R.id.tglMulaiText);
        editBatal = findViewById(R.id.editBatal);
        editBatal.setVisibility(View.GONE);
//        btntglSelesai.setOnClickListener(tglEnd);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
//        btn.setOnClickListener(sendProyek);

        nama.setText(apiHelper.getNama_proyek());
        keterangan.setText(apiHelper.getKeterangan());
        modal.setText(apiHelper.getModal());
        tglMulaiText.setText(apiHelper.getTglMulai());
        tglSelesaiText.setText(apiHelper.getTglSelesai());
        edit.setOnClickListener(editListener);

        editFunc();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;
                Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    if (!isKeyboardStatus()) {
                        setKeyboardStatus(true);
                    }
                }
                else {
                    // keyboard is closed
                    if (isKeyboardStatus()) {
                        setKeyboardStatus(false);
                    }
                }
            }
        });

    }

    private void configTgl(OnTimeSelectListener listener ){

        TimePickerView timePickerView = new TimePickerBuilder(this, listener)
                .setType(new boolean[]{true, true, true, false, false, false})// type of date
                .setCancelText("BATAL")
                .setSubmitText("OK")
                .setTitleSize(20)
                .setTitleText("Pilih Tanggal")
//             .setOutSideCancelable(false)// default is true
                .isCyclic(false)// default is false
                .setTitleColor(getResources().getColor(R.color.darkGrey))
                .setSubmitColor(getResources().getColor(R.color.darkBlue))
                .setCancelColor(getResources().getColor(R.color.darkBlue))

                .setDate(date)
                .setLabel("","","","hours","mins","seconds")
                .build();
        timePickerView.show();
    }
    private OnTimeSelectListener setTglEnd = new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, View v) {

            apiHelper.setTglSelesai(dateFormatter.format(date));
            tglSelesaiText.setText(apiHelper.getTglSelesai());
        }
    };

    private OnTimeSelectListener setStartTgl = new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, View v) {
            apiHelper.setTglMulai(dateFormatter.format(date));
            tglMulaiText.setText(apiHelper.getTglMulai());
        }
    };
    private Boolean errorForm(){
        if (TextUtils.isEmpty(keterangan.getText().toString())){
            setCtt("Tidak ada catatan");
        }else {
            setCtt(keterangan.getText().toString());
        }
        if (TextUtils.isEmpty(nama.getText().toString())){
            nama.setError("Nama Harus di Isi");
            return false;
        }else if (TextUtils.isEmpty(apiHelper.getTglMulai())){
            tglMulaiText.setError("Tanggal Tidak Boleh Kosong");
            return false;

        }else if (TextUtils.isEmpty(apiHelper.getTglSelesai())){
            tglSelesaiText.setError("Tanggal Tidak Boleh Kosong");
            return false;

        }
        apiHelper.setNama_proyek(nama.getText().toString());
        apiHelper.setKeterangan(keterangan.getText().toString());
//
        return true;


    }

    private View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setPageMode(1);
            nama.setFocusableInTouchMode(true);
            nama.requestFocus();
            edit.setVisibility(View.GONE);
            editBatal.setVisibility(View.VISIBLE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            modal.setFocusableInTouchMode(true);
            keterangan.setFocusableInTouchMode(true);
            btn.setVisibility(View.VISIBLE);
            btntglMulai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isKeyboardStatus()){
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }

                    configTgl(setStartTgl);
                }
            });
            btntglSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isKeyboardStatus()){
                        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    }

                    configTgl(setTglEnd);
                }
            });
            btn.setOnClickListener(senListener);

        }
    };

    private void editFunc(){
        editBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPageMode(0);
                if (isKeyboardStatus()){
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }

                btn.setVisibility(View.GONE);
                nama.setFocusableInTouchMode(false);
                nama.clearFocus();
                keterangan.clearFocus();
                modal.clearFocus();
                modal.setFocusableInTouchMode(false);
                keterangan.setFocusableInTouchMode(false);
                editBatal.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                btntglMulai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                });
                btntglSelesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                });

            }
        });



    }

    private View.OnClickListener senListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (errorForm()){
                btn.setVisibility(View.GONE);
                pg.setVisibility(View.VISIBLE);
                Call<ResponseInsertProyek> p = apiService.insertProyek(user.getsAuth(), apiHelper.getId_(),apiHelper.getNama_proyek(), "update",
                        apiHelper.getKeterangan(), apiHelper.getTglMulai(), apiHelper.getTglSelesai(), "0");
                p.enqueue(new Callback<ResponseInsertProyek>() {
                    @Override
                    public void onResponse(Call<ResponseInsertProyek> call, Response<ResponseInsertProyek> response) {
                        btn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                    Intent i = new Intent(ProyekDetailActivity.this, ProyekActivity.class);
                                    i.putExtra("id_proyek", apiHelper.getId_());
                                    startActivity(i);
                                    finish();
                            }
                        }
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseInsertProyek> call, Throwable t) {
                        btn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                });
            }
        }
    };
}
