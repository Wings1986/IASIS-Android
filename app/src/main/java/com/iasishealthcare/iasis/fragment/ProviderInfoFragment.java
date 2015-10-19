package com.iasishealthcare.iasis.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.Models.Provider;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.activity.ProviderResultActivity;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.customcontrol.CustomFontButton;
import com.iasishealthcare.iasis.customcontrol.CustomFontTextView;
import com.iasishealthcare.iasis.customcontrol.CustomWebView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by iGold on 9/9/15.
 */


public class ProviderInfoFragment extends BaseFragment {


    public static ProviderInfoFragment newInstance(Provider provider) {
        ProviderInfoFragment fragment = new ProviderInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("provider", provider);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_provider_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {

        final Provider provider = (Provider) getArguments().getSerializable("provider");

        ImageView photo = (ImageView) view.findViewById(R.id.photo);
        CustomFontTextView lblName = (CustomFontTextView) view.findViewById(R.id.lblName);
        CustomFontTextView lblSpecialty = (CustomFontTextView) view.findViewById(R.id.lblSpecialty);
        CustomFontButton btnSchedule = (CustomFontButton) view.findViewById(R.id.btnSchedule);
        CustomFontButton btnDetails = (CustomFontButton) view.findViewById(R.id.btnDetail);
        CustomFontButton btnStar = (CustomFontButton) view.findViewById(R.id.btnStar);


        String imagePath = "http://directory.iasishealthcare.com/images/physicians/" + provider.photo;
        Picasso.with(getActivity())
                .load(imagePath)
                .into(photo);

        lblName.setText(provider.first_name + " " + provider.last_name + ", " + provider.credentials);
        lblSpecialty.setText(provider.specialty1);

        btnDetails.setVisibility(View.GONE);

        btnSchedule.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(provider.appointment_url));
                startActivity(i);
            }
        });
        if (!provider.scheduleURLString.contains("://")) {
            btnSchedule.setVisibility(View.GONE);
        }

        // star button
        btnStar.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // provider.id
                // xml parser

            }
        });
        btnStar.setVisibility(View.VISIBLE);



        // web side
        String htmlData = AppData.getInstance().loadJSONFromAsset("html/Provider.html");

        htmlData = htmlData.replaceAll("\\{\\{first_name\\}\\}",    provider.first_name);
        htmlData = htmlData.replaceAll("\\{\\{last_name\\}\\}",     provider.last_name);
        htmlData = htmlData.replaceAll("\\{\\{credentials\\}\\}",   provider.credentials);
        htmlData = htmlData.replaceAll("\\{\\{specialty\\}\\}",     provider.specialty1);
        htmlData = htmlData.replaceAll("\\{\\{bio\\}\\}",           provider.bio);
        htmlData = htmlData.replaceAll("\\{\\{location1_name\\}\\}",    provider.location1_name);
        htmlData = htmlData.replaceAll("\\{\\{location1_address1\\}\\}", provider.location1_address1);
        htmlData = htmlData.replaceAll("\\{\\{location1_city\\}\\}", provider.location1_city);
        htmlData = htmlData.replaceAll("\\{\\{location1_state\\}\\}", provider.location1_state);
        htmlData = htmlData.replaceAll("\\{\\{location1_zip\\}\\}", provider.location1_zip);
        htmlData = htmlData.replaceAll("\\{\\{location1_phone\\}\\}", provider.location1_phone);
        htmlData = htmlData.replaceAll("\\{\\{location1_url\\}\\}", provider.location1_url.contains("http://") ? provider.location1_url : "http://" + provider.location1_url );


        CustomWebView webview = (CustomWebView)view.findViewById(R.id.webView);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.loadData(htmlData, "text/html", "UTF-8");;
    }


}
