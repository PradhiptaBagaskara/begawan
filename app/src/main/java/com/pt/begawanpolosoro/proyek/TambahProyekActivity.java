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
import com.pt.begawanpolosoro.proyek.api.ResultInsertProyek;
import com.pt.begawanpolosoro.util.ApiHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahProyekActivity extends AppCompatActivity {
    MaterialEditText nama,keterangan;
    Button btn;
    RelativeLayout btntglSelesai, btntglMulai;
    TextView tglSelesaiText, tglMulaiText, title;
    ProgressBar pg;
    EditTextRupiah modal;
    ImageButton back, tglbar;
    ApiService apiService;
    CurrentUser user;
    DatePickerDialog datePickerDialog;
    Calendar date = Calendar.getInstance();

    ApiHelper apiHelper = new ApiHelper();
    private static final String TAG = "TambahProyekActivity";

    public String getCtt() {
        return ctt;
    }

    public void setCtt(String ctt) {
        this.ctt = ctt;
    }
    Locale locale = new Locale("id", "ID");

    String ctt;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);
    CoordinatorLayout rootView;

    public boolean isKeyboardStatus() {
        return keyboardStatus;
    }

    public void setKeyboardStatus(boolean keyboardStatus) {
        this.keyboardStatus = keyboardStatus;
    }

    boolean keyboardStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_proyek);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));

        InitRetro initRetro = new InitRetro(getApplicationContext());
        user = new CurrentUser(getApplicationContext());
        apiService = initRetro.InitApi().create(ApiService.class);

        rootView = findViewById(R.id.rootView);

        title = findViewById(R.id.title);
        title.setText("TAMBAH PEKERJAAN");
        nama = findViewById(R.id.namaProyek);
        modal = findViewById(R.id.modalProyek);
        keterangan = findViewById(R.id.keteranganProyek);
        btn = findViewById(R.id.btnBaru);
        pg = findViewById(R.id.progresBaru);
        pg.setVisibility(View.GONE);
        btntglMulai = findViewById(R.id.tglMulai);
        btntglSelesai = findViewById(R.id.tglSelesai);
        btntglMulai.setOnClickListener(tglStart);
        tglSelesaiText = findViewById(R.id.tglSelesaiText);
        tglbar = findViewById(R.id.tglSelesaiIc);
        tglMulaiText = findViewById(R.id.tglMulaiText);

        btntglSelesai.setOnClickListener(tglEnd);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
        btn.setOnClickListener(sendProyek);

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

    private View.OnClickListener tglStart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isKeyboardStatus()){
                hideKeyboard();
            }
            configTgl(setStartTgl);

        }
    };

    private View.OnClickListener tglEnd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isKeyboardStatus()){
                hideKeyboard();
            }
            configTgl(setTglEnd);
        }
    };

    private void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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
    private Boolean errorForm(){
        if (TextUtils.isEmpty(keterangan.getText().toString())){
            setCtt("Tidak ada catatan");
        }else {
            setCtt(keterangan.getText().toString());
        }
        if (TextUtils.isEmpty(nama.getText().toString())){
            nama.setError("Nama Harus di Isi");
            return false;
        }else if (TextUtils.isEmpty(modal.getNumber())){
            modal.setError("Modal Harus di isi ");
            return false;

        }else if (TextUtils.isEmpty(apiHelper.getTglMulai())){
            tglMulaiText.setError("Tanggal Tidak Boleh Kosong");
            return false;

        }else if (TextUtils.isEmpty(apiHelper.getTglSelesai())){
            tglSelesaiText.setError("Tanggal Tidak Boleh Kosong");
            return false;

        }
//
        return true;


    }



    private View.OnClickListener sendProyek = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (errorForm()){
                btn.setVisibility(View.GONE);
                pg.setVisibility(View.VISIBLE);
                Call<ResponseInsertProyek> p = apiService.insertProyek(user.getsAuth(),"0",nama.getText().toString(), "insert",getCtt(),
                        apiHelper.getTglMulai(),apiHelper.getTglSelesai(),modal.getNumber());
                p.enqueue(new Callback<ResponseInsertProyek>() {
                    @Override
                    public void onResponse(Call<ResponseInsertProyek> call, Response<ResponseInsertProyek> response) {
                        btn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                ResultInsertProyek result = response.body().getResult();
                                Intent intent = new Intent(getApplicationContext(), ProyekActivity.class);
                                intent.putExtra("id_proyek", result.getId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseInsertProyek> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Terjadi  Kesalahan", Toast.LENGTH_SHORT).show();

                        btn.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        t.printStackTrace();
                    }
                });
            }

        }
    };
}
