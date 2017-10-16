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

public class Diets extends Fragment {
    WebView diets;
    BerrySession bs;
    ProgressDialog berryProgress;
    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diets, container1, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
        diets = (WebView)v.findViewById(R.id.web_diets);
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading Page...");
//	        pd.setCancelable(false);
        pd.show();
        diets.getSettings().setJavaScriptEnabled(true);
        diets.getSettings().setPluginState(PluginState.ON);
        diets.setWebViewClient(new MyWebViewClient());
        bs=new BerrySession(this.getActivity());
        SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);
        String diet = bs.getString(ParseDataManager.TAG_NUTRITION_DIETS);
            diets.loadUrl(diet);
            // diets.loadData(diet, "text/html", "UTF-8");
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
