package com.iasishealthcare.iasis.customcontrol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.Models.Provider;
import com.iasishealthcare.iasis.activity.ProviderResultActivity;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 10/2/15.
 */
public class CustomWebView extends WebView{

    Context mContext;

    public CustomWebView(Context context) {
        super(context);
        if(!isInEditMode()){
            init(context);
        }
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()){
            init(context);
        }
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode()){
            init(context);
        }
    }

    private void init(final Context context) {

        mContext = context;

        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                System.out.println("url " + url);

                if (url.startsWith("iasis://")) { //
                    gotoDeepLink(url);
                }
                else {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    context.startActivity(i);
                }
//                return super.shouldOverrideUrlLoading(view, url);
                return true;
            }

        });
    }


    private void gotoDeepLink(String url) {

        String[] components = url.split("=");

        final Dialog waitDialog = DialogHelper.getProgressDialog(mContext);
        waitDialog.show();

        AndroidHttpClient httpClient = new AndroidHttpClient(AppData.kServerURL);
        httpClient.setMaxRetries(3);
        ParameterMap params = httpClient.newParams()
                .add("dataset", components[1])
                ;


        httpClient.get("/api", params, new AsyncCallback() {

            @Override
            public void onComplete(HttpResponse httpResponse) {

                waitDialog.dismiss();

                try {
                    JSONObject result = new JSONObject(httpResponse.getBodyAsString());

                    final JSONArray providers = result.getJSONArray("providers");

                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            ArrayList<Provider> arrayProviders = new ArrayList<Provider>();
                            try {
                                for (int i = 0; i < providers.length(); i++) {
                                    arrayProviders.add(new Provider(providers.getJSONObject(i)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Intent intent = new Intent(mContext, ProviderResultActivity.class);
                            intent.putExtra("providers", arrayProviders);
                            mContext.startActivity(intent);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();

                    DialogHelper.showToast(mContext, "An error occurred while fetching your search results. Please try again.");
                }

            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                waitDialog.dismiss();
                DialogHelper.showToast(mContext, "An error occurred while fetching your search results. Please try again.");
            }
        });
    }

}
