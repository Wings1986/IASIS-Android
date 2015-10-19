package com.iasishealthcare.iasis.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iasishealthcare.iasis.Models.Hospital;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.fragment.HospitalInfoFragment;
import com.iasishealthcare.iasis.fragment.HospitalsFragment;


public class HospitalsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_back);

		ImageView btnBack = (ImageView) findViewById(R.id.nav_back);
		btnBack.setOnTouchListener(CustomButtonTouchListener.getInstance());
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
					getSupportFragmentManager().popBackStack();
				} else {
					onBackPressed();
				}
			}
		});

		TextView tvTitle = (TextView) findViewById(R.id.nav_title);
		tvTitle.setText("Find a Hospital");


		String state = (String) getIntent().getExtras().getString("state");

		HospitalsFragment newFragment = HospitalsFragment.newInstance(state);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, newFragment)
				.commit();



	}

}
