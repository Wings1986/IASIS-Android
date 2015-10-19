package com.iasishealthcare.iasis.fragment;


import android.content.ComponentName;
import android.content.Context;
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


public class StateFragment extends BaseFragment {

    HeaderListView mListView;
    private MyCustomAdapter mAdapter;


    List<String> allState = new ArrayList<>();

    public static StateFragment newInstance() {
        StateFragment fragment = new StateFragment();
        Bundle args = new Bundle();
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

        try {
            JSONObject allData = new JSONObject(AppData.getInstance().loadJSONFromAsset("json/Hospitals.json"));

            for (Iterator<String> iter = allData.keys(); iter.hasNext(); ) {
                String key = iter.next();
                allState.add(key);
            }


            mListView = (HeaderListView) view.findViewById(R.id.listview);

            mAdapter = new MyCustomAdapter();
            mListView.setAdapter(mAdapter);

        } catch (Exception e) {e.printStackTrace();}

    }

    public class MyCustomAdapter extends SectionAdapter {


        private LayoutInflater mInflater;


        public MyCustomAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int numberOfSections() {
            return 1;
        }

        @Override
        public int getSectionHeaderViewTypeCount() {
            return 0;
        }

        @Override
        public int getSectionHeaderItemViewType(int section) {
            return 0;

        }

        @Override
        public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.list_state_name_header, null);

                holder.titleLabel = (CustomFontTextView) convertView.findViewById(R.id.tvTitle);
                holder.titleLabel.setText("Filter Hospitals by State");

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            return convertView;
        }

        @Override
        public int numberOfRows(int section) {

            return allState.size();
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

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.list_state_item, null);

                holder.titleLabel = (CustomFontTextView) convertView.findViewById(R.id.tvTitle);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            String name = allState.get(row);

            holder.titleLabel.setText(name);

            return convertView;
        }

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);

            String state = allState.get(row);

            ((HomeActivity) getActivity()).gotoHospitals(state);
        }


    }

    public class ViewHolder {
        CustomFontTextView titleLabel;
    }

}
