package com.pt.begawanpolosoro.pdf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.pt.begawanpolosoro.R;

import java.io.File;

public class PdfReaderActivity extends AppCompatActivity {

    ImageButton back;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));
        back = findViewById(R.id.back);
        pdfView = findViewById(R.id.pdfView);
        Intent intent = getIntent();
        if (intent.hasExtra("file")){
            String fileloc = intent.getStringExtra("file");
            Log.d("file loc", "onCreate: "+fileloc);
            File file = new File(intent.getStringExtra("file"));
            pdfView.fromFile(file)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .pageFling(true)
            .load();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
