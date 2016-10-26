package com.populace.berrycollege.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.BerrySession;
import com.populace.berrycollege.managers.ParseDataManager;

public class CaloryCounter extends Fragment {
    WebView cal_count;
    ProgressDialog pd;
    BerrySession bs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calory_counter, container1, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
            cal_count = (WebView) v.findViewById(R.id.web_cal_count);
            pd = new ProgressDialog(this.getActivity());
            pd.setMessage("Loading Page...");
            pd.setCancelable(false);
            pd.show();
            cal_count.getSettings().setJavaScriptEnabled(true);
            cal_count.getSettings().setPluginState(WebSettings.PluginState.ON);
            cal_count.setWebViewClient(new MyWebViewClient());
            bs = new BerrySession(this.getActivity());
            SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);
            String calory_count = bs.getString(ParseDataManager.TAG_NUTRITION_CAL_COUNT);
            cal_count.loadUrl(calory_count);
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
        System.out.println("on finish");
        if (pd.isShowing()) {
            pd.dismiss();
        }

    }
}
}
