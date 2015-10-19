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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;
import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.Models.Hospital;
import com.iasishealthcare.iasis.Models.Provider;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.activity.AddProviderActivity;
import com.iasishealthcare.iasis.activity.HomeActivity;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.customcontrol.CustomFontButton;
import com.iasishealthcare.iasis.customcontrol.CustomFontTextView;
import com.iasishealthcare.iasis.listener.OnXMLListener;
import com.iasishealthcare.iasis.xmlparser.GetXMLTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by iGold on 9/9/15.
 */


public class PersonalFragment extends BaseFragment {

    HeaderListView mListView;
    private MyCustomAdapter mAdapter;


    private Hospital favoriteHospital = null;
    private List<Provider> favoriteProviders = null;

    public static PersonalFragment newInstance() {
        PersonalFragment fragment = new PersonalFragment();
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

    @Override
    public void onResume() {
        super.onResume();

        String favoriteState = AppData.getInstance().getFavouriteLocation();
        int favoriteHospitalIndex = AppData.getInstance().getFavouriteHospital();

        if (favoriteState != null && favoriteState.length()>0
                && favoriteHospitalIndex != -1) {

            try {

                JSONObject jsonObject = new JSONObject(AppData.getInstance().loadJSONFromAsset("json/Hospitals.json"));
                JSONObject jsonHospital = jsonObject.getJSONArray(favoriteState).getJSONObject(favoriteHospitalIndex);
                favoriteHospital = new Hospital(jsonHospital);

                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {e.printStackTrace();}

        }

    }

    private void initView(View view) {

        mListView = (HeaderListView) view.findViewById(R.id.listview);

        mAdapter = new MyCustomAdapter();
        mListView.setAdapter(mAdapter);

    }

    public class MyCustomAdapter extends SectionAdapter {

        final int HEADER_TYPE_HOSPITAL   = 0;
        final int HEADER_TYPE_ERWAIT     = 1;
        final int HEADER_TYPE_PROVIDER   = 2;

        private LayoutInflater mInflater;


        public MyCustomAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int numberOfSections() {
            return 3;
        }

        @Override
        public int numberOfRows(int section) {

            if (section == 2) {
                return Math.max(favoriteProviders==null ? 0 : favoriteProviders.size(), 1);
            }

            return 0;
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

            ViewHolderProvider holder = null;

            if (convertView == null) {

                holder = new ViewHolderProvider();

                if (favoriteProviders == null || favoriteProviders.size() == 0) {
                    convertView = mInflater.inflate(R.layout.list_nofavourite_provider_item, null);

                    holder.btnAddProvider = (CustomFontButton) convertView.findViewById(R.id.btnAddProvider);
                }
                else {
                    convertView = mInflater.inflate(R.layout.list_provider_item, null);

                    holder.photo = (ImageView) convertView.findViewById(R.id.photo);
                    holder.lblName = (CustomFontTextView) convertView.findViewById(R.id.lblName);
                    holder.lblSpecialty = (CustomFontTextView) convertView.findViewById(R.id.lblSpecialty);
                    holder.btnSchedule = (CustomFontButton) convertView.findViewById(R.id.btnSchedule);
                    holder.btnDetails = (CustomFontButton) convertView.findViewById(R.id.btnDetail);
                }

                convertView.setTag(holder);
            } else {
                holder = (ViewHolderProvider)convertView.getTag();
            }

            try {
                if (section == 2) {
                    if (favoriteProviders == null || favoriteProviders.size() == 0) {
                        holder.btnAddProvider.setOnTouchListener(CustomButtonTouchListener.getInstance());
                        holder.btnAddProvider.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().startActivity(new Intent(getActivity(), AddProviderActivity.class));
                            }
                        });
                    } else {
                        final Provider provider = favoriteProviders.get(row);

                        Picasso.with(getActivity())
                                .load(provider.photoURLString)
                                .into(holder.photo);

                        holder.lblName.setText(provider.name);
                        holder.lblSpecialty.setText(provider.specialty);

                        holder.btnDetails.setOnTouchListener(CustomButtonTouchListener.getInstance());
                        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((HomeActivity) getActivity()).gotoDetail(provider);
                            }
                        });

                        holder.btnSchedule.setOnTouchListener(CustomButtonTouchListener.getInstance());
                        holder.btnSchedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(provider.scheduleURLString));
                                startActivity(i);
                            }
                        });
                        if (!provider.scheduleURLString.contains("://")) {
                            holder.btnSchedule.setVisibility(View.GONE);
                        }

                    }

                }

            } catch (Exception e) {e.printStackTrace();}

            return convertView;
        }

        @Override
        public int getSectionHeaderViewTypeCount() {
            return HEADER_TYPE_PROVIDER + 1;
        }

        @Override
        public int getSectionHeaderItemViewType(int section) {
            if (section == 0)
                return HEADER_TYPE_HOSPITAL;
            else if (section == 1) {
                return HEADER_TYPE_ERWAIT;
            }
            else {
                return HEADER_TYPE_PROVIDER;
            }

        }

        @Override
        public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                switch (getSectionHeaderItemViewType(section)) {
                    case HEADER_TYPE_HOSPITAL: {
                        holder = new ViewHolderHospital();

                        convertView = mInflater.inflate(R.layout.list_myhospital_item, null);

                        ((ViewHolderHospital)holder).backImage = (ImageView) convertView.findViewById(R.id.backImage);
                        ((ViewHolderHospital)holder).btnSetMyHospital = (CustomFontButton) convertView.findViewById(R.id.btnSetMyHospital);
                        ((ViewHolderHospital)holder).subLinearLayout = (LinearLayout) convertView.findViewById(R.id.subFrameLayout);
                        ((ViewHolderHospital)holder).titleLabel= (CustomFontTextView) convertView.findViewById(R.id.tvMyHospital);
                        ((ViewHolderHospital)holder).subtitleLabel = (CustomFontTextView) convertView.findViewById(R.id.tvSubTitle);
                    }
                    break;

                    case HEADER_TYPE_ERWAIT: {
                        holder = new ViewHolderErWait();

                        convertView = mInflater.inflate(R.layout.list_erwait_item, null);

                        ((ViewHolderErWait)holder).backLinearLayout = (LinearLayout) convertView.findViewById(R.id.backLinearLayout);
                        ((ViewHolderErWait)holder).lblWaitTime = (CustomFontTextView) convertView.findViewById(R.id.tvWaitTime);
                        ((ViewHolderErWait)holder).btnCheckIn = (CustomFontButton) convertView.findViewById(R.id.btnCheckIn);
                        ((ViewHolderErWait)holder).btnDirections = (CustomFontButton) convertView.findViewById(R.id.btnDirection);
                    }
                    break;

                    case HEADER_TYPE_PROVIDER: {
                        holder = new ViewHolder();

                        convertView = mInflater.inflate(R.layout.list_provider_header, null);

                    }
                    break;

                }

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            try {

                if (section == 0) {

                    try {
                        String favoriteState = AppData.getInstance().getFavouriteLocation();
                        int favoriteHospitalIndex = AppData.getInstance().getFavouriteHospital();

                        JSONObject jsonObject = new JSONObject(AppData.getInstance().loadJSONFromAsset("json/Hospitals.json"));

                        JSONObject jsonHospital = jsonObject.getJSONArray(favoriteState).getJSONObject(favoriteHospitalIndex);
                        final Hospital hospital = new Hospital(jsonHospital);

                        String imagePath = "image/" + hospital.image.toLowerCase() + ".jpg";
                        Drawable d = Drawable.createFromStream(getActivity().getAssets().open(imagePath), null);
                        ((ViewHolderHospital) holder).backImage.setBackground(d);
                        ((ViewHolderHospital) holder).backImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (hospital != null) {
                                    ((HomeActivity) getActivity()).gotoHospitalInfo(hospital);
                                }
                            }
                        });
                    } catch (Exception e) {e.printStackTrace();}

                    if (favoriteHospital != null) {
                        ((ViewHolderHospital)holder).btnSetMyHospital.setVisibility(View.GONE);
                        ((ViewHolderHospital)holder).subLinearLayout.setVisibility(View.VISIBLE);

                        ((ViewHolderHospital)holder).titleLabel.setText("My Hospital  ");
                        if (favoriteHospital.displayname != null && favoriteHospital.displayname.length() > 0) {
                            ((ViewHolderHospital)holder).subtitleLabel.setText(favoriteHospital.displayname);
                        } else {
                            ((ViewHolderHospital)holder).subtitleLabel.setText(favoriteHospital.name);
                        }
                    } else {
                        ((ViewHolderHospital)holder).btnSetMyHospital.setVisibility(View.VISIBLE);
                        ((ViewHolderHospital)holder).btnSetMyHospital.setOnTouchListener(CustomButtonTouchListener.getInstance());
                        ((ViewHolderHospital)holder).btnSetMyHospital.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((HomeActivity) getActivity()).gotoSetting();
                            }
                        });

                        ((ViewHolderHospital)holder).subLinearLayout.setVisibility(View.GONE);
                    }
                }
                else if (section == 1) {

                    if (favoriteHospital == null || favoriteHospital.er.length() <= 8) {
                        ((ViewHolderErWait)holder).backLinearLayout.setVisibility(View.GONE);
                    }
                    if (favoriteHospital != null && favoriteHospital.er.length() > 8) {
                        // xml parser
                        new GetXMLTask(getActivity(), ((ViewHolderErWait) holder).lblWaitTime, new OnXMLListener() {
                            @Override
                            public void complete() {

                            }
                        }).execute(new String[] {favoriteHospital.er} );
                    }
                    if (favoriteHospital != null && favoriteHospital.checkin.equals("")) {
                        ((ViewHolderErWait)holder).btnCheckIn.setVisibility(View.GONE);
                    }

                    ((ViewHolderErWait)holder).btnCheckIn.setOnTouchListener(CustomButtonTouchListener.getInstance());
                    ((ViewHolderErWait)holder).btnCheckIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = favoriteHospital.checkin;
                            Intent i = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(url));
                            startActivity(i);
                        }
                    });

                    ((ViewHolderErWait)holder).btnDirections.setOnTouchListener(CustomButtonTouchListener.getInstance());
                    ((ViewHolderErWait)holder).btnDirections.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = "http://maps.google.com/maps?saddr=&daddr=" + URLEncoder.encode(favoriteHospital.info);

                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(url));
                            intent.setComponent(new ComponentName("com.google.android.apps.maps",
                                    "com.google.android.maps.MapsActivity"));
                            getActivity().startActivity(intent);
                        }
                    });
                }

                else if (section == 2) {

                }

            } catch (Exception e) {e.printStackTrace();}

            return convertView;
        }

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);


        }

    }

    public class ViewHolder {

    }
    class ViewHolderHospital extends ViewHolder {

        ImageView backImage;
        CustomFontButton btnSetMyHospital;

        LinearLayout subLinearLayout;
        CustomFontTextView titleLabel;
        CustomFontTextView subtitleLabel;
    }
    class ViewHolderErWait extends ViewHolder {

        LinearLayout backLinearLayout;
        CustomFontTextView lblWaitTime;
        CustomFontButton btnCheckIn;
        CustomFontButton btnDirections;
    }
    class ViewHolderProvider extends ViewHolder {
        CustomFontTextView lblName;
        CustomFontTextView lblSpecialty;
        ImageView photo;
        CustomFontButton btnSchedule;
        CustomFontButton btnDetails;
        CustomFontButton btnAddProvider;
    }

}
