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
import com.populace.berrycollege.managers.ParseDataManager;


public class MinutesFragment extends Fragment {
    WebView minutes;
    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_minutes, container, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
        minutes = (WebView)v.findViewById(R.id.web_minutes);
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading Page...");
        pd.setCancelable(false);
        pd.show();
        pd.setCancelable(false);

        minutes.setWebViewClient(new MyWebViewClient());
        minutes.getSettings().setJavaScriptEnabled(true);
            //minutes.getSettings().setPluginState(WebSettings.PluginState.ON);
        minutes.getSettings().setBuiltInZoomControls(true);
            minutes.getSettings().setPluginState(WebSettings.PluginState.ON);
            //minutes.getSettings().setDefaultZoom(ZoomDensity.FAR);
            // minutes.getSettings().setUseWideViewPort(true);
            //  minutes.getSettings().setLoadWithOverviewMode(true);
        // minutes.loadUrl("file:///android_asset/img.html");
            // minutes.loadUrl("file:///android_asset/img.html");
            minutes.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "http://berry.edu/uploadedFiles/Website/Student_Affairs/Athletics/_Assets/Documents/BerryGroupFitness.pdf");

        }
        else
        {
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
