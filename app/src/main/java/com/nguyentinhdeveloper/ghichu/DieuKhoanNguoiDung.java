package com.nguyentinhdeveloper.ghichu;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class DieuKhoanNguoiDung extends AppCompatActivity {
private WebView webView;
private static final String URL="https://vuabocphet98.wixsite.com/dieukhoannguoidung/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieu_khoan_nguoi_dung);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Điều khoản người dùng");
        webView=findViewById(R.id.dieukhoan);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(URL);
    }
}
