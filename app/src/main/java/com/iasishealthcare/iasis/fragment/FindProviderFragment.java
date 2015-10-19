package com.iasishealthcare.iasis.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.Models.Provider;
import com.iasishealthcare.iasis.R;
import com.iasishealthcare.iasis.activity.HomeActivity;
import com.iasishealthcare.iasis.activity.ProviderResultActivity;
import com.iasishealthcare.iasis.customcontrol.CustomButtonTouchListener;
import com.iasishealthcare.iasis.customcontrol.CustomFontButton;
import com.iasishealthcare.iasis.customcontrol.CustomFontEdittext;
import com.iasishealthcare.iasis.customcontrol.DialogHelper;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by iGold on 9/9/15.
 */


public class FindProviderFragment extends BaseFragment {

    CustomFontButton btnState, btnCity, btnSpecialty;
    CustomFontEdittext etLastName;

    JSONObject providerLocations;
    String[] choiceList;

    int m_indexState = 0;
    int m_indexCity = 0;
    int m_indexSpecialty = 0;

    JSONObject rawResponseData;

    public static FindProviderFragment newInstance() {
        FindProviderFragment fragment = new FindProviderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_find_provider, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {

        btnState = (CustomFontButton) view.findViewById(R.id.btnState);
        btnState.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStateDlg();
            }
        });

        btnCity = (CustomFontButton) view.findViewById(R.id.btnCity);
        btnCity.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = btnState.getText().toString();
                if (state.length()<1 || state.equalsIgnoreCase("state"))
                    return;

                openCityDlg(state);
            }
        });

        btnSpecialty = (CustomFontButton) view.findViewById(R.id.btnSpecialty);
        btnSpecialty.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnSpecialty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = btnState.getText().toString();
                String city = btnCity.getText().toString();

                if (state.equalsIgnoreCase("state")) {
                    state = "";
                }
                if (city.equalsIgnoreCase("city")) {
                    city = "";
                }

                openSpecialtyDlg(state, city);
            }
        });

        etLastName = (CustomFontEdittext) view.findViewById(R.id.etLastName);

        CustomFontButton btnSearch = (CustomFontButton) view.findViewById(R.id.btnSearch);
        btnSearch.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch(btnState.getText().toString(), btnCity.getText().toString(), btnSpecialty.getText().toString(), etLastName.getText().toString());
            }
        });

        CustomFontButton btnClear = (CustomFontButton) view.findViewById(R.id.btnClear);
        btnClear.setOnTouchListener(CustomButtonTouchListener.getInstance());
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnState.setText("State");
                btnCity.setText("City");
                btnSpecialty.setText("Specialty");
                etLastName.setText("");
            }
        });

        try {
            providerLocations = new JSONObject(AppData.getInstance().loadJSONFromAsset("json/ProviderLocations.json"));
        } catch (Exception e) {e.printStackTrace();}
    }

    private void openStateDlg() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        List<String> keys = new ArrayList<>();
        for(Iterator<String> iter = providerLocations.keys();iter.hasNext();) {
            String key = iter.next();
            keys.add(key);
        }

        choiceList = new String[keys.size()];
        choiceList = keys.toArray(choiceList);


        builder.setSingleChoiceItems(
                choiceList,
                m_indexState,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        //set to buffKey instead of selected
                        //(when cancel not save to selected)
                        m_indexState = which;
                    }
                })
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                btnState.setText(choiceList[m_indexState]);
                            }
                        }
                )
                .setNegativeButton("Cancel", null);

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void openSpecialtyDlg(String state, String city) {

        if (state.equalsIgnoreCase("arizona")) {
            state = "az";
        }
        if (state.equalsIgnoreCase("utah")) {
            state = "ut";
        }
        if (state.equalsIgnoreCase("texas")) {
            state = "tx";
        }
        if (state.equalsIgnoreCase("colorado")) {
            state = "co";
        }
        if (state.equalsIgnoreCase("arkansas")) {
            state = "ar";
        }
        if (state.equalsIgnoreCase("louisiana")) {
            state = "la";
        }

        AndroidHttpClient httpClient = new AndroidHttpClient(AppData.kServerURL);
        httpClient.setMaxRetries(3);
        ParameterMap params = httpClient.newParams()
                .add("state", state)
                .add("city", city)
                ;


        httpClient.get("/api/specialties.php", params, new AsyncCallback() {

            @Override
            public void onComplete(HttpResponse httpResponse) {

                try {
                    rawResponseData = new JSONObject(httpResponse.getBodyAsString());

                    List<String> specialties = new ArrayList<>();
                    for(Iterator<String> iter = rawResponseData.keys();iter.hasNext();) {
                        String key = iter.next();
                        if (key.length() > 0) {
                            specialties.add(key);
                        }

                        try {
                            for (Iterator<String> sub = rawResponseData.getJSONObject(key).keys(); iter.hasNext(); ) {
                                String subKey = sub.next();
                                if (subKey.length() > 0) {
                                    specialties.add(subKey);
                                }
                            }
                        } catch (Exception e) {e.printStackTrace();}
                    }


                    try {

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getActivity());


                        choiceList = specialties.toArray(choiceList);

                        builder.setSingleChoiceItems(
                                choiceList,
                                m_indexSpecialty,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        //set to buffKey instead of selected
                                        //(when cancel not save to selected)
                                        m_indexSpecialty = which;
                                    }
                                })
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {

                                                btnSpecialty.setText(choiceList[m_indexSpecialty]);

                                            }
                                        }
                                )
                                .setNegativeButton("Cancel", null);

                        AlertDialog alert = builder.create();
                        alert.show();
                    } catch (Exception e) {e.printStackTrace();}

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });




    }

    private void openCityDlg(String state) {

        try {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());


            JSONArray jsonArray = providerLocations.getJSONArray(state);


            choiceList = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                choiceList[i] = jsonArray.getString(i);
            }


            builder.setSingleChoiceItems(
                    choiceList,
                    m_indexCity,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(
                                DialogInterface dialog,
                                int which) {
                            //set to buffKey instead of selected
                            //(when cancel not save to selected)
                            m_indexCity = which;
                        }
                    })
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    btnCity.setText(choiceList[m_indexCity]);

                                }
                            }
                    )
                    .setNegativeButton("Cancel", null);

            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {e.printStackTrace();}

    }

    // search
    void onSearch(String state, String city, String specialty, String lastname) {

        String parentSpecialty = "", subSpecialty = "";

        if (state.equalsIgnoreCase("State")) {
            state = "";
        }

        if (state.equalsIgnoreCase("arizona")) {
            state = "az";
        }
        if (state.equalsIgnoreCase("utah")) {
            state = "ut";
        }
        if (state.equalsIgnoreCase("texas")) {
            state = "tx";
        }
        if (state.equalsIgnoreCase("colorado")) {
            state = "co";
        }
        if (state.equalsIgnoreCase("arkansas")) {
            state = "ar";
        }
        if (state.equalsIgnoreCase("louisiana")) {
            state = "la";
        }

        if (city.equalsIgnoreCase("city")) {
            city = "";
        }
        if (!specialty.equalsIgnoreCase("specialty")) {

            try {
                for (Iterator<String> iter = rawResponseData.keys(); iter.hasNext(); ) {
                    String key = iter.next();

                    if (parentSpecialty.length() > 0 && subSpecialty.length() > 0) {
                        break;
                    }

                    parentSpecialty = key;
                    subSpecialty = "";

                    if (key.equalsIgnoreCase(specialty)) {
                        break;
                    }

                    try {
                        for (Iterator<String> sub = rawResponseData.getJSONObject(key).keys(); iter.hasNext(); ) {
                            String subKey = sub.next();

                            if (subKey.equalsIgnoreCase(specialty)) {
                                subSpecialty = subKey;

                                break;
                            }
                        }
                    } catch (Exception e) {e.printStackTrace();}
                }
            } catch (Exception e) {e.printStackTrace();}
        }

        final Dialog waitDialog = DialogHelper.getProgressDialog(getActivity());
        waitDialog.show();

        AndroidHttpClient httpClient = new AndroidHttpClient(AppData.kServerURL);
        httpClient.setMaxRetries(3);
        ParameterMap params = httpClient.newParams()
                .add("state", state)
                .add("city", city)
                .add("specialty", parentSpecialty)
                .add("subspecialty", subSpecialty)
                .add("last_name", lastname)
                ;


        httpClient.get("/api/", params, new AsyncCallback() {

            @Override
            public void onComplete(HttpResponse httpResponse) {

                waitDialog.dismiss();

                try {
                    JSONObject result = new JSONObject(httpResponse.getBodyAsString());

                    final JSONArray providers = result.getJSONArray("providers");

                    if (providers == null || providers.length() == 0) {
                        DialogHelper.getDialog(getActivity(), "No Result Found", "No providers matching your search criteria were found.", "OK", null, null).show();
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            ArrayList<Provider> arrayProviders = new ArrayList<Provider>();
                            try {
                                for (int i = 0; i < Math.min(300, providers.length()) ; i++) {
                                    arrayProviders.add(new Provider(providers.getJSONObject(i)));
                                }
                            } catch (Exception e) {e.printStackTrace();}


                            Intent intent = new Intent(getActivity(), ProviderResultActivity.class);
                            intent.putExtra("providers", arrayProviders);
                            startActivity(intent);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();

                    DialogHelper.showToast(getActivity(), "An error occurred while fetching your search results. Please try again.");
                }

            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                waitDialog.dismiss();
                DialogHelper.showToast(getActivity(), "An error occurred while fetching your search results. Please try again.");
            }
        });
    }
}
