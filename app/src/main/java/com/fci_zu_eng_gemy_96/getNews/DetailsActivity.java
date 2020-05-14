package com.fci_zu_eng_gemy_96.getNews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dmax.dialog.SpotsDialog;

public class DetailsActivity extends AppCompatActivity {

    WebView webView ;
    AlertDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dialog = new SpotsDialog.Builder().setContext(this).build();
        dialog.show();

        webView = findViewById(R.id.weView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
            }
        });

        if (!getIntent().getStringExtra("webUrl").isEmpty()){
            webView.loadUrl(getIntent().getStringExtra("webUrl"));
        }

    }
}
