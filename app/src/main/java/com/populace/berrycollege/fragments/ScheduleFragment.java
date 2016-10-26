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


public class ScheduleFragment extends Fragment {
    WebView schedule ;
    ProgressDialog pd;
    BerrySession bs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
        schedule = (WebView)v.findViewById(R.id.web_schedule);
        pd = new ProgressDialog(this.getActivity());
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading Page...");
        pd.setCancelable(false);
        pd.show();
        schedule.setWebViewClient(new MyWebViewClient());
        schedule.getSettings().setJavaScriptEnabled(true);
        schedule.getSettings().setPluginState(WebSettings.PluginState.ON);
        bs=new BerrySession(this.getActivity());
        SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);
        String pdf = bs.getString(ParseDataManager.TAG__PHYSICAL_INTRAMURAL_SCHEDULE);
        schedule.getSettings().setJavaScriptEnabled(true);
        schedule.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
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
