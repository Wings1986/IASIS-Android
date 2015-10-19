package com.iasishealthcare.iasis.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.fragment.SettingFragment;


public class SettingActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_none);

		boolean first = getIntent().getBooleanExtra("first", false);

		SettingFragment newFragment = SettingFragment.newInstance(first);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, newFragment)
				.commit();


	}

}
