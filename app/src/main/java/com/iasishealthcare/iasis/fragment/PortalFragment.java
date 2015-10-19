package com.iasishealthcare.iasis.fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by iGold on 9/9/15.
 */


public class PortalFragment extends BaseFragment {

    HeaderListView mListView;
    private MyCustomAdapter mAdapter;


    public static PortalFragment newInstance() {
        PortalFragment fragment = new PortalFragment();
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

        mListView = (HeaderListView) view.findViewById(R.id.listview);

        mAdapter = new MyCustomAdapter();
        mListView.setAdapter(mAdapter);

    }

    public class MyCustomAdapter extends SectionAdapter {

        final int TYPE_HEADER   = 0;
        final int TYPE_STATE     = 1;
        final int TYPE_FOOTER   = 2;

        private LayoutInflater mInflater;

        String[][] portalData = {
                {"Hope, AR", "http://mywadleyathope.com/"},
                {"AZ",       "http://patientportalaz.com/"},
                {"West Monroe, LA", "http://mygrmc.com/"},
                {"UT", "http://patientportalut.com/"},
                {"Houston, TX", "http://mysjmc.com/"},
                {"Odessa, TX", "http://yourormc.com/"},
                {"Port Arthur, TX", "http://mymcst.com/"},
                {"San Antonio, TX", "http://myswgh.com/"},
                {"Texarkana, TX", "http://mywadley.com/"},

                {"Houston, TX", "http://mysjmc.com/"},
                {"Odessa, TX", "http://yourormc.com/"},
                {"Port Arthur, TX", "http://mymcst.com/"},
                {"San Antonio, TX", "http://myswgh.com/"},
                {"Texarkana, TX", "http://mywadley.com/"},
        };

        public MyCustomAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int numberOfSections() {
            return 3;
        }

        @Override
        public int numberOfRows(int section) {

            if (section == 1) {
                return portalData.length;
            }

            return 1;
        }

        @Override
        public Object getRowItem(int section, int row) {
            return null;
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return false;
        }

        @Override
        public int getRowViewTypeCount() {
            return TYPE_FOOTER + 1;
        }

        @Override
        public int getRowItemViewType(int section, int row) {
            if (section == 0) {
                return TYPE_HEADER;
            } else if (section == 1) {
                return TYPE_STATE;
            } else {
                return TYPE_FOOTER;
            }
        }

        @Override
        public View getRowView(int section, int row, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                switch (getRowItemViewType(section, row)) {
                    case TYPE_HEADER:{
                        holder = new ViewHolderHeader();

                        convertView = mInflater.inflate(R.layout.list_portal_header, null);

                    }
                    break;

                    case TYPE_STATE:{
                        holder = new ViewHolderState();

                        convertView = mInflater.inflate(R.layout.list_state_item, null);

                        ((ViewHolderState) holder).lblTitle = (CustomFontTextView) convertView.findViewById(R.id.tvTitle);
                    }
                    break;

                    case TYPE_FOOTER:{
                        holder = new ViewHolderFooter();

                        convertView = mInflater.inflate(R.layout.list_web_limit_item, null);

                        ((ViewHolderFooter) holder).webView = (CustomWebView) convertView.findViewById(R.id.webView);

                    }
                    break;

                }

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            try {
                if (section == 0) {

                }
                else if (section == 1) {
                    ((ViewHolderState)holder).lblTitle.setText("Patient Portal " + portalData[row][0]);
                }
                if (section == 2) {

                    WebSettings settings = ((ViewHolderFooter)holder).webView.getSettings();
                    settings.setJavaScriptEnabled(true);
                    ((ViewHolderFooter)holder).webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                    ((ViewHolderFooter)holder).webView.loadUrl("http://www.iasishealthcare.com/app/portal.html");

                    ((ViewHolderFooter)holder).webView.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                v.getParent().requestDisallowInterceptTouchEvent(false);

                            } else
                                v.getParent().requestDisallowInterceptTouchEvent(true);

                            return false;
                        }

                    });
                }

            } catch (Exception e) {e.printStackTrace();}

            return convertView;
        }

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);

            if (section == 1) {
                String url = portalData[row][1];
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(i);
            }
        }

    }

    public class ViewHolder {

    }
    class ViewHolderHeader extends ViewHolder {

    }
    class ViewHolderState extends ViewHolder {

        CustomFontTextView lblTitle;
    }
    class ViewHolderFooter extends ViewHolder {

        CustomWebView webView;
    }

}
