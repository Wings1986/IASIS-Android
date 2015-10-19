package com.iasishealthcare.iasis.customcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * Created by iGold on 6/2/15.
 */

public class DialogHelper {

    public static Dialog getDialog(Context context, String title, String content,
                                               String firstText, String secondText,
                                               final DialogCallBack callback) {
        if (context != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(content);
            builder.setCancelable(true);
            builder.setNegativeButton(firstText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();

                    if (callback != null) {
                        callback.onClick(0);
                    }
                }
            });

            if (secondText != null) {
                builder.setPositiveButton(secondText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (callback != null) {
                            callback.onClick(1);
                        }
                    }
                });
            }

            AlertDialog alertDialog = builder.create();
            alertDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
            return alertDialog;
        }
        return null;
    }

    public static void showToast (Context context, String title) {
        if (context != null) {
            Toast.makeText(context, title, Toast.LENGTH_LONG).show();
        }

    }


    public static ProgressDialog getProgressDialog(Context context){

        ProgressDialog waitDialog = new ProgressDialog(context);
        waitDialog.setMessage("Loading...");
        waitDialog.setCancelable(false);
        return waitDialog;
    }

}
