package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.BerrySession;
import com.populace.berrycollege.managers.ParseDataManager;


public class OnBikingFragment extends Fragment  {
    WebView onbiking;
    BerrySession bs;
    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_on_biking, container, false);
        onbiking = (WebView)v.findViewById(R.id.web_onbiking);
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading Page...");
        pd.setCancelable(false);
        pd.show();
        pd.setCancelable(false);

        onbiking.setWebViewClient(new MyWebViewClient());
        bs=new BerrySession(this.getActivity());
        SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);
        String intramurals = bs.getString(ParseDataManager.TAG__PHYSICAL_INTRAMURAL_ON_BIKING);
        onbiking.loadUrl(intramurals);
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
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }

}