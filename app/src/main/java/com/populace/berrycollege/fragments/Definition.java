package com.populace.berrycollege.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.BerrySession;
import com.populace.berrycollege.managers.ParseDataManager;

import java.util.HashMap;
import java.util.List;

public class Definition extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    ProgressDialog pd;
    WebView definition;
    BerrySession bs;
    HashMap<String, String> url_maps = new HashMap<String, String>();
    private SliderLayout mDemoSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_definition, container1, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
        definition = (WebView)v.findViewById(R.id.web_definition);
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading Page...");
        pd.setCancelable(false);
        pd.show();
        pd.setCancelable(false);
        definition.getSettings().setJavaScriptEnabled(true);
        definition.getSettings().setPluginState(PluginState.ON);
        definition.setWebViewClient(new MyWebViewClient());
        bs=new BerrySession(this.getActivity());

            mDemoSlider = (SliderLayout) v.findViewById(R.id.slider);

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CharacterImages");
            query.setLimit(1000);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> arg0, ParseException e) {
                    pd.dismiss();
                    if (e == null) {
                        // your logic here
                        for (int i = 0; i < arg0.size(); i++) {

                            url_maps.put(arg0.get(i).getString("title"), arg0.get(i).getParseFile("image").getUrl());

                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            // initialize a SliderLayout
                            textSliderView
                                    .description(arg0.get(i).getString("title"))
                                    .image(arg0.get(i).getParseFile("image").getUrl())
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(null);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", arg0.get(i).getString("title"));

                            mDemoSlider.addSlider(textSliderView);
                            TextView tempTextView = (TextView) mDemoSlider.findViewById(R.id.description);
                            ViewGroup.LayoutParams params = tempTextView.getLayoutParams();
                            params.width = ViewPagerEx.LayoutParams.MATCH_PARENT;
                            tempTextView.setLayoutParams(params);
                            tempTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        }

                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
                        mDemoSlider.getPagerIndicator().setVisibility(View.GONE);
                        // mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setDuration(4000);
                        if (arg0.size() > 0) {
                            // bs_academics.putString(TAG_ACAD_CLUB, arg0.get(0).getString(TAG_ACAD_CLUB));
                            // bs_academics.putString(TAG_ACAD_LIBRARY, arg0.get(0).getString(TAG_ACAD_LIBRARY));
                            //bs_academics.putString(TAG_ACAD_EVENT_CAL, arg0.get(0).getString(TAG_ACAD_EVENT_CAL));
                            //bs_academics.putString(TAG_ACAD_TUTORING, arg0.get(0).getString(TAG_ACAD_TUTORING));
                            //berryProgress.setTitle("academic");
                            //downloadInformationDataNutrition();
                            //  url_maps.put(arg0.get())

                        }

                    } else {
                        // handle Parse Exception here

                        //  berryProgress.dismiss();
                        //Toast.makeText(context, "Error Occured While Downloading",
                        //    Toast.LENGTH_SHORT).show();

                    }
                }
            });

        SharedPreferences settings = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), 0);
        String definitions = bs.getString(ParseDataManager.TAG_ACAD_CLUB);
        definition.loadUrl(definitions);
        }
        else
        {
            ParseDataManager.sharedDataManager(getActivity()).noInternet(getActivity());
        }
        return v;
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        // mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("Slider Demo", "Page Changed: ");
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
