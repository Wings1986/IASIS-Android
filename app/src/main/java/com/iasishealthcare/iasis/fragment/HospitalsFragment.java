package com.iasishealthcare.iasis.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;
import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.Models.Hospital;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.activity.HomeActivity;
import com.iasishealthcare.iasis.activity.HospitalInfoActivity;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.customcontrol.CustomFontButton;
import com.iasishealthcare.iasis.customcontrol.CustomFontTextView;
import com.iasishealthcare.iasis.listener.OnXMLListener;
import com.iasishealthcare.iasis.xmlparser.GetXMLTask;
import com.iasishealthcare.iasis.xmlparser.XMLGettersSetters;
import com.iasishealthcare.iasis.xmlparser.XMLHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by iGold on 9/9/15.
 */


public class HospitalsFragment extends BaseFragment {

    HeaderListView mListView;
    private MyCustomAdapter mAdapter;

    String mState = "";
    List<Hospital> allHospital = new ArrayList<>();

    public static HospitalsFragment newInstance(String state) {
        HospitalsFragment fragment = new HospitalsFragment();
        Bundle args = new Bundle();
        args.putString("state", state);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_personal, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {

        mState = getArguments().getString("state");

        try {
            JSONObject allData = new JSONObject(AppData.getInstance().loadJSONFromAsset("json/Hospitals.json"));

            JSONArray jsonArrayHospital = allData.getJSONArray(mState);

            for (int i = 0 ; i < jsonArrayHospital.length() ; i ++) {
                JSONObject obj = jsonArrayHospital.getJSONObject(i);

                allHospital.add(new Hospital(obj));
            }


            mListView = (HeaderListView) view.findViewById(R.id.listview);

            mAdapter = new MyCustomAdapter();
            mListView.setAdapter(mAdapter);

        } catch (Exception e) {e.printStackTrace();}

    }

    public class MyCustomAdapter extends SectionAdapter {


        private LayoutInflater mInflater;

        final int KEY_HEADER_TAG = R.id.myid;
        final int KEY_BODY_TAG = R.id.myid + 10;

        public MyCustomAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int numberOfSections() {
            return 1;
        }

        @Override
        public int getSectionHeaderViewTypeCount() {
            return 1;
        }

        @Override
        public int getSectionHeaderItemViewType(int section) {
            return 0;

        }

        @Override
        public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {

            ViewHolderHeader holder = null;

            if (convertView == null) {

                holder = new ViewHolderHeader();

                convertView = mInflater.inflate(R.layout.list_state_name_header, null);

                holder.lblTitle = (CustomFontTextView) convertView.findViewById(R.id.tvTitle);
                holder.lblTitle.setText(mState + " IASIS Hospitals");

//                convertView.setTag(KEY_HEADER_TAG + section, holder);
            } else {
                holder = (ViewHolderHeader)convertView.getTag(KEY_HEADER_TAG + section);
            }

            return convertView;
        }

        @Override
        public int numberOfRows(int section) {

            return allHospital.size();
        }

        @Override
        public Object getRowItem(int section, int row) {
            return null;
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return true;
        }

        @Override
        public View getRowView(int section, int row, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.list_hospital_item, null);

                holder.lblTitle = (CustomFontTextView) convertView.findViewById(R.id.lblName);
                holder.lblLocation = (CustomFontTextView) convertView.findViewById(R.id.lblSpecialty);
                holder.btnInfo = (CustomFontButton) convertView.findViewById(R.id.btnInfo);
                holder.btnCheckIn = (CustomFontButton) convertView.findViewById(R.id.btnCheckIn);
                holder.lblWait = (CustomFontTextView) convertView.findViewById(R.id.tvWaitTime);

                convertView.setTag(KEY_BODY_TAG + section * 100 + row, holder);
            } else {
                holder = (ViewHolder)convertView.getTag(KEY_BODY_TAG + section * 100 + row);
            }

            try {

                final Hospital hospital = allHospital.get(row);

                if (hospital.displayname != null && hospital.displayname.length() > 0) {
                    holder.lblTitle.setText(hospital.displayname);
                } else {
                    holder.lblTitle.setText(hospital.name);
                }

                String parts[] = hospital.info.split("\\n");
                holder.lblLocation.setText(parts[1]);

                holder.btnInfo.setOnTouchListener(CustomButtonTouchListener.getInstance());
                holder.btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HospitalInfoActivity.class);

                        intent.putExtra("hospital", hospital);

                        startActivity(intent);
                    }
                });

                holder.btnCheckIn.setOnTouchListener(CustomButtonTouchListener.getInstance());
                holder.btnCheckIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = hospital.checkin;
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(i);
                    }
                });
                holder.btnCheckIn.setVisibility(hospital.checkin.length() < 1 ? View.GONE : View.VISIBLE);


                if (hospital.er.length() >= 8) {
                    holder.lblWait.setVisibility(View.VISIBLE);

                    // xml parser
                    new GetXMLTask(getActivity(), holder.lblWait, new OnXMLListener() {
                        @Override
                        public void complete() {

                        }
                    }).execute(new String[] {hospital.er} );

                } else {
                    holder.lblWait.setVisibility(View.GONE);
                }

            } catch (Exception e) {e.printStackTrace();}

            return convertView;
        }

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);

        }


    }

    public class ViewHolderHeader {
        CustomFontTextView lblTitle;
    }

    public class ViewHolder {
        CustomFontTextView lblTitle;
        CustomFontTextView lblLocation;
        CustomFontButton btnInfo;
        CustomFontButton btnCheckIn;
        CustomFontTextView lblWait;
    }

}
