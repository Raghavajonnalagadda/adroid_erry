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


public class GeneralInfoFragment extends Fragment {
    WebView generalInfo;
    BerrySession bs;
    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_general_info, container, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
        generalInfo = (WebView)v.findViewById(R.id.web_generalInfo);
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading Page...");
        pd.setCancelable(false);
        pd.show();
        pd.setCancelable(false);
        generalInfo.setWebViewClient(new MyWebViewClient());
        bs=new BerrySession(this.getActivity());
        SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);
        String resource = bs.getString(ParseDataManager.TAG__EMOTION_GENERAL_INFO);
            //generalInfo.loadUrl("https://www.berry.edu/stulife/counseling/");
            generalInfo.loadUrl("https://www.berry.edu/stulife/counseling/staff/");


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
