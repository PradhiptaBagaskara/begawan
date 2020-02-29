package com.pt.begawanpolosoro.transaksi;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.congfandi.lib.TextViewRupiah;
import com.pt.begawanpolosoro.R;

public class TxDetailActivity extends AppCompatActivity {

    TextView nama, namaTx, jenisTx, tgl, jenisBayar, id,namaP;
    TextViewRupiah total;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.darkBlue));

        Bundle extra = getIntent().getExtras();

        id = findViewById(R.id.id_tx);
        nama = findViewById(R.id.nama_pekerja);
        namaTx = findViewById(R.id.judul_pembelian);
        jenisBayar = findViewById(R.id.jenis_bayar);
        tgl = findViewById(R.id.tgl);
        total = findViewById(R.id.dana);
        jenisTx = findViewById(R.id.keterangan);
        namaP = findViewById(R.id.nama_proyek);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!extra.isEmpty()){
            nama.setText(extra.getString("nama"));
            namaTx.setText(extra.getString("nama_tx"));
            id.setText(extra.getString("id"));
            jenisBayar.setText(extra.getString("jenis_bayar"));
            tgl.setText(extra.getString("waktu"));
//            total.setText(extra.getString("dana"));
            total.convertToIDR(extra.getString("dana"));
            String ket = extra.getString("keterangan");
            ket = ket.replace(", ",System.getProperty("line.separator"));
            ket = ket.replace(",",System.getProperty("line.separator"));

            jenisTx.setText(ket);
            namaP.setText(extra.getString("nama_proyek"));



        }
    }


}
