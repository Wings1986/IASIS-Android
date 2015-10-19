package com.iasishealthcare.iasis.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by admin on 9/21/15.
 */
public class Hospital implements Serializable{

    public String name = "";
    public String displayname = "";
    public String info = "";
    public String url = "";
    public String image = "";
    public String er = "";
    public String checkin = "";

    public Hospital(JSONObject jsonObject) {
        try {
            name = jsonObject.getString("name");
        } catch (Exception e) {e.printStackTrace();}

        try {
            displayname = jsonObject.getString("displayname");
        } catch (Exception e) {e.printStackTrace();}

        try {
            info = jsonObject.getString("info");
        } catch (Exception e) {e.printStackTrace();}

        try {
            image = jsonObject.getString("image");
        } catch (Exception e) {e.printStackTrace();}

        try {
            er = jsonObject.getString("er");
        } catch (Exception e) {e.printStackTrace();}

        try {
            checkin = jsonObject.getString("checkin");
        } catch (Exception e) {e.printStackTrace();}

        try {
            url = jsonObject.getString("url");
        } catch (Exception e) {e.printStackTrace();}


    }
}
