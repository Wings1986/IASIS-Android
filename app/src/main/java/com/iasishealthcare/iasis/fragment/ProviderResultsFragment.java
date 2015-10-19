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
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;
import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.Models.Hospital;
import com.iasishealthcare.iasis.Models.Provider;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.activity.HomeActivity;
import com.iasishealthcare.iasis.activity.ProviderResultActivity;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.customcontrol.CustomFontButton;
import com.iasishealthcare.iasis.customcontrol.CustomFontTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iGold on 9/9/15.
 */


public class ProviderResultsFragment extends BaseFragment {

    ListView mListView;
    private MyCustomAdapter mAdapter;


    private ArrayList<Provider> providers = null;

    public static ProviderResultsFragment newInstance(ArrayList<Provider> arrayProviders) {
        ProviderResultsFragment fragment = new ProviderResultsFragment();
        Bundle args = new Bundle();
        args.putSerializable("providers", arrayProviders);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_provider_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {

        providers = (ArrayList<Provider>) getArguments().getSerializable("providers");

        mListView = (ListView) view.findViewById(R.id.listview);

        mAdapter = new MyCustomAdapter();
        mListView.setAdapter(mAdapter);

    }

    public class MyCustomAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyCustomAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return providers.size();
        }

        @Override
        public Object getItem(int position) {
            try {
                return providers.get(position);
            } catch (Exception e) {e.printStackTrace();}
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.list_provider_item, null);

                holder.photo = (ImageView) convertView.findViewById(R.id.photo);
                holder.lblName = (CustomFontTextView) convertView.findViewById(R.id.lblName);
                holder.lblSpecialty = (CustomFontTextView) convertView.findViewById(R.id.lblSpecialty);
                holder.btnSchedule = (CustomFontButton) convertView.findViewById(R.id.btnSchedule);
                holder.btnDetails = (CustomFontButton) convertView.findViewById(R.id.btnDetail);
                holder.btnStar = (CustomFontButton) convertView.findViewById(R.id.btnStar);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            try {
                final Provider provider = providers.get(position);

                String imagePath = "http://directory.iasishealthcare.com/images/physicians/" + provider.photo;
                Picasso.with(getActivity())
                        .load(imagePath)
                        .into(holder.photo);

                holder.lblName.setText(provider.first_name + " " + provider.last_name + ", " + provider.credentials);
                holder.lblSpecialty.setText(provider.specialty1);

                holder.btnDetails.setOnTouchListener(CustomButtonTouchListener.getInstance());
                holder.btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ((ProviderResultActivity) getActivity()).gotoProvider(provider);

                    }
                });

                holder.btnSchedule.setOnTouchListener(CustomButtonTouchListener.getInstance());
                holder.btnSchedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(provider.appointment_url));
                        startActivity(i);
                    }
                });
                if (!provider.scheduleURLString.contains("://")) {
                    holder.btnSchedule.setVisibility(View.GONE);
                }


                // star button
                holder.btnStar.setOnTouchListener(CustomButtonTouchListener.getInstance());
                holder.btnStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // provider.id
                        // xml parser
                    }
                });
                holder.btnStar.setVisibility(View.VISIBLE);


            } catch (Exception e) {e.printStackTrace();}

            return convertView;
        }

    }

    public class ViewHolder {
        CustomFontTextView lblName;
        CustomFontTextView lblSpecialty;
        ImageView photo;
        CustomFontButton btnSchedule;
        CustomFontButton btnDetails;
        CustomFontButton btnStar;
    }


}
