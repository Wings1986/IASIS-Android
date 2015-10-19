package com.iasishealthcare.iasis.activity;


import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iasishealthcare.iasis.Models.Hospital;
import com.iasishealthcare.iasis.Models.Provider;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.fragment.HospitalInfoFragment;
import com.iasishealthcare.iasis.fragment.ProviderInfoFragment;
import com.iasishealthcare.iasis.fragment.ProviderResultsFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ProviderResultActivity extends FragmentActivity {

	TextView tvTitle;

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

					setTitle("Provider Search Result");

				} else {
					onBackPressed();
				}
			}
		});

		tvTitle = (TextView) findViewById(R.id.nav_title);
		setTitle("Provider Search Result");

		ArrayList<Provider> providers = (ArrayList<Provider>) getIntent().getExtras().getSerializable("providers");

		ProviderResultsFragment newFragment = ProviderResultsFragment.newInstance(providers);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, newFragment)
				.commit();

	}
	public void setTitle(String title) {
		tvTitle.setText(title);
	}


	public void gotoProvider(Provider provider) {

		setTitle(provider.first_name);

		ProviderInfoFragment newFragment = ProviderInfoFragment.newInstance(provider);

		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.content_frame, newFragment)
				.addToBackStack(null)
				.commit();
	}
}
