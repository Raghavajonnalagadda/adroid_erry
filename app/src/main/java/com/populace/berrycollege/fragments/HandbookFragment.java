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


public class HandbookFragment extends Fragment {
    WebView handbook;
    BerrySession bs;
    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_handbook, container, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
        handbook = (WebView)v.findViewById(R.id.web_handbook);
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading Page...");
        pd.setCancelable(false);
        pd.show();
        pd.setCancelable(false);
        handbook.setWebViewClient(new MyWebViewClient());
        handbook.getSettings().setJavaScriptEnabled(true);
        handbook.getSettings().setPluginState(PluginState.ON);
        bs=new BerrySession(this.getActivity());
        handbook = (WebView)v.findViewById(R.id.web_handbook);
        SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);
        //String resource = bs.getString(ParseDataManager.TAG__EMOTION_APPOINTMENTS);
            String filePath = "http://www.berry.edu/uploadedFiles/Website/Student_Affairs/_Assets/Documents/VikingCodeHandbook.pdf";
            handbook.loadUrl("https://docs.google.com/gview?embedded=true&url=" + filePath);
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
