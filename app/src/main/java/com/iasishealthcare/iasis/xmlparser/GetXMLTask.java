package com.iasishealthcare.iasis.xmlparser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.iasishealthcare.iasis.customcontrol.CustomFontTextView;
import com.iasishealthcare.iasis.listener.OnXMLListener;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by admin on 9/24/15.
 */
public class GetXMLTask extends AsyncTask<String, Void, ArrayList<String>> {

    private Activity context;

    CustomFontTextView textView;
    OnXMLListener mListener;

    public GetXMLTask(Activity context, CustomFontTextView textView, OnXMLListener listener) {
        this.context = context;

        this.textView = textView;
        this.mListener = listener;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (result == null || result.size() == 0)
            return;

        String title = result.get(0);


        String waitTimeString;
        int waitTime = Integer.parseInt(title);

        if(waitTime < 0) {
            waitTimeString = "0 minutes";
        } else if (waitTime == 1) {
            waitTimeString = "1 minute";
        } else if (waitTime > 59) {
            waitTimeString = "Unavailable";
        } else {
            waitTimeString = waitTime + " minutes";
        }

        final String str = waitTimeString;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(str);
            }
        });


        if (mListener != null) {
            mListener.complete();
        }
    }

    @Override
    protected ArrayList<String> doInBackground(String... urlString) {

        Log.d("GetXMLTask", "url = " + urlString[0]);

        try {
            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();

            URL url = new URL(urlString[0]); // URL of the XML

            /**
             * Create the Handler to handle each of the XML tags.
             **/
            XMLHandler myXMLHandler = new XMLHandler();
            xmlR.setContentHandler(myXMLHandler);
            xmlR.parse(new InputSource(url.openStream()));

            return XMLHandler.getXMLData().getTitle();

        } catch (Exception e) {
            System.out.println(e);
        }

        // stream.close();
        return null;
    }


}