package com.example.maptest.mycartest.UI.EquipUi;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;

/**
 * Created by ${Author} on 2017/3/19.
 * Use to  车辆违章查询，连接到违章查询官网
 */

public class BkRulerActivity extends BaseActivity implements View.OnClickListener{
    private ImageView imageView_quit;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakruler);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initClick();
        loadWeb();
    }

    private void initClick() {
        imageView_quit.setOnClickListener(this);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_view);
        imageView_quit = (ImageView) findViewById(R.id.image_bkrquits);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_bkrquits:
                finish();
                break;
        }
    }

    /**
     * 嵌入webview,引导向
     */
    private void loadWeb(){
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(AppCons.QURRYURL);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null){
            webView.destroy();
            webView = null;
        }
    }
}
