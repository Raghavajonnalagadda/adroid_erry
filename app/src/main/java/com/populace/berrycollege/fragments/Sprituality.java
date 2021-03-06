package com.populace.berrycollege.fragments;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
public class Sprituality extends Fragment {
    WebView spirtuality;
    BerrySession bs;
    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prituality, container1, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
        spirtuality = (WebView)v.findViewById(R.id.web_spirtuality);
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading Page...");
        pd.setCancelable(false);
        pd.show();
        spirtuality.setWebViewClient(new MyWebViewClient());
        spirtuality.setWebViewClient(new MyWebViewClient());
        spirtuality.getSettings().setJavaScriptEnabled(true);
        spirtuality.getSettings().setPluginState(WebSettings.PluginState.ON);
        bs=new BerrySession(this.getActivity());
        SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);

            String spirtual = bs.getString(ParseDataManager.TAG_SPIR_SPIRTUALITY);
            spirtuality.loadUrl(spirtual);
            // spirtuality.loadUrl("file:///android_asset/spiritual.html");
            //  String spirituality = bs.getString(ParseDataManager.TAG_SPIR_SPIRTUALITY);
            // spirtuality.loadData(spirituality, "text/html", "UTF-8");
        }
        else
        {
            ParseDataManager.sharedDataManager(getActivity()).noInternet(getActivity());
        }
        return v;
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("geo:")) {
                showMap(Uri.parse(url));
                return true;
            } else {
                view.loadUrl(url);
            }
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
