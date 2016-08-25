package com.tcms.activity;

import com.tcms.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends  BaseActivity{
	private WebView webview; 
	private String url;
    @SuppressLint("SetJavaScriptEnabled")  
    @Override  
    protected void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.webview);  
        webview = (WebView) findViewById(R.id.webView1);  
        initivReabck(this);
        setTitle(this, R.string.web_view);
        Intent intent = getIntent();
       url= intent.getStringExtra("url");
        
        WebSettings webSettings = webview.getSettings();  
        //����WebView���ԣ��ܹ�ִ��Javascript�ű�    
        webSettings.setJavaScriptEnabled(true);    
        //���ÿ��Է����ļ�  
        webSettings.setAllowFileAccess(true);  
         //����֧������  
        webSettings.setBuiltInZoomControls(true);  
        //������Ҫ��ʾ����ҳ    
        webview.loadUrl(url);    
        //����Web��ͼ    
        webview.setWebViewClient(new webViewClient ());    
          
    }  
       
  
    @Override  
    public boolean onCreateOptionsMenu(Menu menu)  
    {  
        // Inflate the menu; this adds items to the action bar if it is present.  
        getMenuInflater().inflate(R.menu.main, menu);  
        return true;  
    }  
      
    @Override   
    //���û���    
    //����Activity���onKeyDown(int keyCoder,KeyEvent event)����    
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {    
            webview.goBack(); //goBack()��ʾ����WebView����һҳ��    
            return true;    
        }    
        finish();//�����˳�����  
        return false;    
    }    
        
    //Web��ͼ    
    private class webViewClient extends WebViewClient {    
        public boolean shouldOverrideUrlLoading(WebView view, String url) {    
            view.loadUrl(url);    
            return true;    
        }    
    }    
}
