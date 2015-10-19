package com.iasishealthcare.iasis.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;
import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.Models.Hospital;
import com.iasishealthcare.iasis.Models.Provider;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.activity.HomeActivity;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.customcontrol.CustomFontButton;
import com.iasishealthcare.iasis.customcontrol.CustomFontTextView;
import com.iasishealthcare.iasis.customcontrol.CustomWebView;
import com.iasishealthcare.iasis.listener.OnXMLListener;
import com.iasishealthcare.iasis.xmlparser.GetXMLTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by iGold on 9/9/15.
 */


public class HospitalInfoFragment extends BaseFragment {

    private Hospital hospital = null;

    public static HospitalInfoFragment newInstance(Hospital hospital) {
        HospitalInfoFragment fragment = new HospitalInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("hospital", hospital);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_hospital_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

    }


    private void initView(View view) {

        hospital = (Hospital) getArguments().getSerializable("hospital");

        /*
         my hospital
         */

        CustomFontTextView lblName = (CustomFontTextView) view.findViewById(R.id.lblName);
        CustomFontTextView lblAddress1 = (CustomFontTextView) view.findViewById(R.id.lblAddress1);
        CustomFontTextView lblAddress2 = (CustomFontTextView) view.findViewById(R.id.lblAddress2);
        CustomFontTextView lblPhone = (CustomFontTextView) view.findViewById(R.id.lblPhone);


        if (hospital.displayname != null && hospital.displayname.length() > 0) {
            lblName.setText(hospital.displayname);
        } else {
            lblName.setText(hospital.name);
        }

        String parts[] = hospital.info.split("\\n");
        lblAddress1.setText(parts[0]);
        lblAddress2.setText(parts[1]);
        lblPhone.setText(parts[2]);

        /*
         er wait
         */

        LinearLayout backLinearLayout = (LinearLayout) view.findViewById(R.id.backLinearLayout);
        CustomFontTextView lblWaitTime = (CustomFontTextView) view.findViewById(R.id.tvWaitTime);
        CustomFontButton btnCheckIn = (CustomFontButton) view.findViewById(R.id.btnCheckIn);
        CustomFontButton btnDirections = (CustomFontButton) view.findViewById(R.id.btnDirection);

        if (hospital == null || hospital.er.length() <= 8) {
            backLinearLayout.setVisibility(View.GONE);
        }
        if (hospital != null && hospital.er.length() > 8) {
            // parse xml
            new GetXMLTask(getActivity(), lblWaitTime, new OnXMLListener() {
                @Override
                public void complete() {

                }
            }).execute(new String[] {hospital.er} );
        }
        if (hospital != null && hospital.checkin.equals("")) {
            btnCheckIn.setVisibility(View.GONE);
        }

        btnCheckIn.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = hospital.checkin;
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(i);
            }
        });

        btnDirections.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?saddr=&daddr=" + URLEncoder.encode(hospital.info);

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                intent.setComponent(new ComponentName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity"));
                getActivity().startActivity(intent);
            }
        });

        /*
         er wait
        */
        CustomWebView webview = (CustomWebView)view.findViewById(R.id.webView);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.loadUrl(hospital.url);
    }


}
