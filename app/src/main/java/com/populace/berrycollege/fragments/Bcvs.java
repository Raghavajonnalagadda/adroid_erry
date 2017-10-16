package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.BerrySession;
import com.populace.berrycollege.managers.ParseDataManager;


public class Bcvs extends Fragment {
    ProgressDialog berryProgress;
    ProgressDialog pd;
    WebView bcvs;
    BerrySession bs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bcvs, container1, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
            bcvs = (WebView) v.findViewById(R.id.web_bcvs);
            pd = new ProgressDialog(this.getActivity());
            pd.setMessage("Loading Page...");
            pd.setCancelable(false);
            pd.show();
            bcvs.getSettings().setJavaScriptEnabled(true);
            bcvs.getSettings().setPluginState(PluginState.ON);
            bcvs.setWebViewClient(new MyWebViewClient());
            bs = new BerrySession(this.getActivity());
            SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);
            //  String bcv = bs.getString(ParseDataManager.TAG_CHAR_BCV);
            String bcv = "http://www.berry.edu/service/";
            bcvs.loadUrl(bcv);
        }
        else{
            ParseDataManager.sharedDataManager(getActivity()).noInternet(getActivity());
        }
        return v;
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
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
