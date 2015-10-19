package com.iasishealthcare.iasis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.Models.Hospital;
import com.iasishealthcare.iasis.Models.Provider;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.fragment.BaseFragment;
import com.iasishealthcare.iasis.fragment.FindProviderFragment;
import com.iasishealthcare.iasis.fragment.HospitalsFragment;
import com.iasishealthcare.iasis.fragment.LegalFragment;
import com.iasishealthcare.iasis.fragment.MenuFragment;
import com.iasishealthcare.iasis.fragment.PersonalFragment;
import com.iasishealthcare.iasis.fragment.PortalFragment;
import com.iasishealthcare.iasis.fragment.StateFragment;
import com.iasishealthcare.iasis.listener.OnClickMenuItemListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;


/**
 * Created by iGold on 6/3/15.
 */
public class HomeActivity extends SlidingFragmentActivity {

    SlidingMenu mSlidingMenu = null;

    MenuFragment mLeftFrag;
    BaseFragment mRightFrag;

    int mIndexSelect = 1;


    TextView titleView;
    ImageView actionBtn;

    private static final int REQUEST_CHOOSE_INTEREST = 1235;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setBehindContentView(R.layout.frame_menu);

        mLeftFrag = MenuFragment.newInstance();
        mLeftFrag.setOnClickMenuItemListener(new OnClickMenuItemListener() {
            @Override
            public void onClickMenuItem(int position) {
                if (mIndexSelect != position) {
                    String title = "";

                    switch (position) {
                        case 0:
                            mRightFrag = PersonalFragment.newInstance();
                            title = "IASIS Healthcare";
                            break;
                        case 1: {

                            if (AppData.getInstance().getFavouriteLocation().length() < 1) {
                                mRightFrag = StateFragment.newInstance();
                            } else {
                                mRightFrag = HospitalsFragment.newInstance(AppData.getInstance().getFavouriteLocation());
                            }

                            title = "Find a Hospital";
                        }
                            break;
                        case 2:
                            mRightFrag = FindProviderFragment.newInstance();
                            title = "Find a Provider";
                            break;
                        case 3:
                            mRightFrag = PortalFragment.newInstance();
                            title = "Patient Portal";
                            break;
                        case 4:
                            mRightFrag = LegalFragment.newInstance();
                            title = "Legal Notices";
                            break;
                    }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, mRightFrag)
                            .commit();

                    setTitle(title);

                    mSlidingMenu.toggle();
                }

                mIndexSelect = position;
            }
        });

        mIndexSelect = 0;
        mRightFrag = PersonalFragment.newInstance();


        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, mLeftFrag).commit();

        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//        mSlidingMenu.setShadowDrawable(R.drawable.reader_shadow);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlidingMenu.setFadeDegree(0.35f);
        // mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        getSlidingMenu().setMode(SlidingMenu.LEFT);

        setContentView(R.layout.frame_content);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mRightFrag)
                .commit();


        // navigationg bar
        ImageView menuBtn = (ImageView) findViewById(R.id.nav_menu);
        menuBtn.setOnTouchListener(CustomButtonTouchListener.getInstance());
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingMenu.toggle();
            }
        });

        titleView = (TextView) findViewById(R.id.nav_title);
        setTitle("IASIS Healthcare");

        actionBtn = (ImageView) findViewById(R.id.nav_action);
        actionBtn.setOnTouchListener(CustomButtonTouchListener.getInstance());
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoSetting();

            }
        });

    }

    public void setTitle(String title) {
        titleView.setText(title);
    }





    public void gotoSetting() {

        startActivity(new Intent(this, SettingActivity.class));

    }

    public void gotoDetail(Provider provider) {

    }

    public void gotoHospitals(String state) {

        Intent intent = new Intent(this, HospitalsActivity.class);

        intent.putExtra("state", state);

        startActivity(intent);
    }

    public void gotoHospitalInfo(Hospital hospital) {

        Intent intent = new Intent(this, HospitalInfoActivity.class);

        intent.putExtra("hospital", hospital);

        startActivity(intent);
    }

    public void gotoProviderResult(ArrayList<Provider> arryProviders) {

        Intent intent = new Intent(this, ProviderResultActivity.class);

        intent.putExtra("providers", arryProviders);

        startActivity(intent);
    }

}

