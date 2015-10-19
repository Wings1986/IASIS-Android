package com.iasishealthcare.iasis.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.customcontrol.CustomFontTextView;
import com.iasishealthcare.iasis.listener.OnClickMenuItemListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by iGold on 9/9/15.
 */


public class MenuFragment extends BaseFragment {

    ListView mListView;
    private MyCustomAdapter mAdapter;

    private OnClickMenuItemListener mOnClickMenuitemListener;

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {

        mListView = (ListView) view.findViewById(R.id.listview);


        mAdapter = new MyCustomAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void setOnClickMenuItemListener(OnClickMenuItemListener listener) {
        mOnClickMenuitemListener = listener;
    }


    public class MyCustomAdapter extends BaseAdapter {

        private static final int TYPE_PROFILE = 0;
        private static final int TYPE_ADS = 1;
        private static final int TYPE_ITEM = 2;
        private static final int TYPE_MAX_COUNT = TYPE_ITEM + 1;


        private List<Item> items = new ArrayList<>();
        private LayoutInflater mInflater;


        public MyCustomAdapter() {
            mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            items.add(new Item("My IASIS", R.drawable.menu_star));
            items.add(new Item("Hospitals", R.drawable.menu_hospitals));
            items.add(new Item("Providers", R.drawable.menu_stethoscope));
            items.add(new Item("Patient Portal", R.drawable.menu_key));
            items.add(new Item("Legal", R.drawable.menu_legal));
        }

        @Override
        public int getViewTypeCount() {
            // Get the number of items in the enum
            return TYPE_MAX_COUNT;

        }

        @Override
        public int getItemViewType(int position) {
            // Use getViewType from the Item interface

            return TYPE_ITEM;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return items.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            int type = getItemViewType(position);

            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {

                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.list_item_menu_item, null);

                        holder.imageView = (ImageView) convertView.findViewById(R.id.ivAvatar);
                        holder.textView = (CustomFontTextView)convertView.findViewById(R.id.tvTitle);

                        holder.backView = convertView;

                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final int index = position;
            holder.imageView.setImageResource(items.get(index).getResID());
            holder.textView.setText(items.get(index).getTitle());

            holder.backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickMenuitemListener != null) {
                        mOnClickMenuitemListener.onClickMenuItem(index);
                    }
                }
            });

            return convertView;
        }

    }

    public static class ViewHolder {
        public View backView;
        public CustomFontTextView textView;
        public ImageView imageView;
    }

    public class Item {
        String title;
        int resID;

        public Item(String _title, int _resID) {
            this.title = _title;
            this.resID = _resID;
        }
        public String getTitle() { return this.title; }
        public int getResID() { return this.resID; }
    }



}
