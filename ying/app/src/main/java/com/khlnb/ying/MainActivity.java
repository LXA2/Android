package com.khlnb.ying;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webview);

        // 启用 JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 设置 WebViewClient 以在 WebView 中打开链接
        webView.setWebViewClient(new WebViewClient());

        // 加载本地 HTML 文件
        //webView.loadUrl("file:///android_asset/sample.html");

        // 或者加载远程 HTML 页面
        webView.loadUrl("https://proton0x00.github.io/test-site/index.html");
    }
}
