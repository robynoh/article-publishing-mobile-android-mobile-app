package com.test.dessertationone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class pay_online extends AppCompatActivity {
    WebView browser;
    String amount;
    String credit;
    String buyer;
    LinearLayout linNtWkOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_online);

        Toolbar toolbar=findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        Intent intent = getIntent();
        amount = getIntent().getStringExtra("amount");
        credit = getIntent().getStringExtra("credit");
        buyer = getIntent().getStringExtra("buyer");


         browser = (WebView) findViewById(R.id.webview);
        linNtWkOff =  findViewById(R.id.linNtWkOff);


         browser.loadUrl("https://dissertation1.com/paynow/"+buyer+"/"+amount+"/"+credit);
    }

    protected void onResume() {
        super.onResume();
        if (checkInternetConnection()){
            setNetWorkON();
        }else {
            setNetWorkOFF();
        }
        browser.setWebViewClient(new MyBrowser());
        WebSettings webSettings =  browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        browser.loadUrl("https://dissertation1.com/paynow/"+buyer+"/"+amount+"/"+credit);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



    private boolean checkInternetConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();
        boolean isConnected;
        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            isConnected =true;
        } else {
            isConnected= false;
        }
        return isConnected;
    }

    public void setNetWorkON(){
        linNtWkOff.setVisibility(View.GONE);
        browser.setVisibility(View.VISIBLE);
    }
    public void setNetWorkOFF(){
        linNtWkOff.setVisibility(View.VISIBLE);
        browser.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
        }
    }
}