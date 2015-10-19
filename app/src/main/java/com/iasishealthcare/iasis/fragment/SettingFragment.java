package com.iasishealthcare.iasis.fragment;


import android.app.AlertDialog;
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
import com.iasishealthcare.iasis.customcontrol.DialogHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by iGold on 9/9/15.
 */


public class SettingFragment extends BaseFragment {

    CustomFontButton btnMyLocation, btnMyHospital;

    JSONObject allData;
    String strLocation = "";

    int m_indexLocation = 0;
    int m_indexHospital = 0;

    String[] choiceList;

    public static SettingFragment newInstance(boolean first) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putBoolean("first", first);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {

        btnMyLocation = (CustomFontButton) view.findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allData == null)
                    return;

                openLocationDlg();
            }
        });

        btnMyHospital = (CustomFontButton) view.findViewById(R.id.btnMyHospital);
        btnMyHospital.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnMyHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (strLocation.length() < 1) {
                    DialogHelper.showToast(getActivity(), "Please choose your location first");
                    return;
                }

                openHospitalDlg(strLocation);

            }
        });

        final boolean first = getArguments().getBoolean("first");

        CustomFontButton btnClose = (CustomFontButton) view.findViewById(R.id.btnClose);
        btnClose.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first) {
                    getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
                }
                getActivity().finish();
            }
        });
        if (first) {
            btnClose.setText("Skip Initial Setup");
        } else {
            btnClose.setText("Close");
        }

        try {
            allData = new JSONObject(AppData.getInstance().loadJSONFromAsset("json/Hospitals.json"));
        } catch (Exception e) {e.printStackTrace();}
    }

    private void openLocationDlg() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        List<String> keys = new ArrayList<>();
        for(Iterator<String> iter = allData.keys();iter.hasNext();) {
            String key = iter.next();
            keys.add(key);
        }

        choiceList = new String[keys.size()];
        choiceList = keys.toArray(choiceList);


        builder.setSingleChoiceItems(
                choiceList,
                m_indexLocation,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        //set to buffKey instead of selected
                        //(when cancel not save to selected)
                        m_indexLocation = which;
                    }
                })
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                strLocation = choiceList[m_indexLocation];
                                btnMyLocation.setText(strLocation);

                                AppData.getInstance().setFavoriteLocation(strLocation);
                            }
                        }
                )
                .setNegativeButton("Cancel", null);

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void openHospitalDlg(String location) {

        try {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());


            JSONArray jsonArray = allData.getJSONArray(location);


            choiceList = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                choiceList[i] = jsonArray.getJSONObject(i).getString("name");
            }


            builder.setSingleChoiceItems(
                    choiceList,
                    m_indexHospital,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(
                                DialogInterface dialog,
                                int which) {
                            //set to buffKey instead of selected
                            //(when cancel not save to selected)
                            m_indexHospital = which;
                        }
                    })
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    btnMyHospital.setText(choiceList[m_indexHospital]);

                                    AppData.getInstance().setFavoriteHospital(m_indexHospital);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null);

            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {e.printStackTrace();}

    }
}
