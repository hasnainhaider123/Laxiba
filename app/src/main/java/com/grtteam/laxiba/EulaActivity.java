package com.grtteam.laxiba;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;


import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.InputStream;




/**
 * Created by oleh on 19.07.16.
 */
public class EulaActivity extends BaseActivity {

    //@BindView(R.id.pdfView)
    PDFView pdfView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);
        pdfView = findViewById(R.id.pdfView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InputStream stream = getResources().openRawResource(R.raw.eula);
        pdfView.fromStream(stream)
                .load();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
