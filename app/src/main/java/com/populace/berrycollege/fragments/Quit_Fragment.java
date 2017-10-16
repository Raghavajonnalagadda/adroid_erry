package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.populace.berrycollege.R;

/**
 * Created by Populace PC 1 on 10/27/2016.
 */

public class Quit_Fragment extends Fragment {
    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quit, container1, false);
        WebView webView = (WebView) v.findViewById(R.id.fragment_quite_webview);
        webView.loadUrl("http://www.berry.edu/berryrecreation/tobaccocessation/");
        pd = new ProgressDialog(this.getActivity());
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.getSettings().setJavaScriptEnabled(true);
        pd.setMessage("Loading Page...");
        pd.setCancelable(false);
        pd.show();
        //   getFragmentManager().beginTransaction().add(R.id.container_fragmen_intramural,new ScheduleFragment(),null).commit();


        return v;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
                pd.setCancelable(false);
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }
}
