package com.iasishealthcare.iasis.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by admin on 9/21/15.
 */
public class Provider implements Serializable {

    public String guid = "";
    public String name = "";
    public String specialty  = "";
    public String photoURLString = "";
    public String scheduleURLString = "";

    // web data
    public String id = "";
    public String photo = "";
    public String first_name = "";
    public String last_name = "";
    public String credentials = "";
    public String specialty1 = "";
    public String appointment_url = "";
    public String bio = "";
    public String location1_name = "";
    public String location1_address1 = "";
    public String location1_city = "";
    public String location1_state = "";
    public String location1_zip = "";
    public String location1_phone = "";
    public String location1_url = "";


    public Provider(JSONObject jsonObject) {

        try {
            id = jsonObject.getString("id");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            photo = jsonObject.getString("photo");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            first_name = jsonObject.getString("first_name");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            last_name = jsonObject.getString("last_name");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            credentials = jsonObject.getString("credentials");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            specialty1 = jsonObject.getString("specialty1");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            appointment_url = jsonObject.getString("appointment_url");
        } catch (Exception e) { e.printStackTrace(); }


        try {
            bio = jsonObject.getString("bio");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            location1_name = jsonObject.getString("location1_name");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            location1_address1 = jsonObject.getString("location1_address1");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            location1_city = jsonObject.getString("location1_city");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            location1_state = jsonObject.getString("location1_state");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            location1_zip = jsonObject.getString("location1_zip");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            location1_phone = jsonObject.getString("location1_phone");
        } catch (Exception e) { e.printStackTrace(); }

        try {
            location1_url = jsonObject.getString("location1_url");
        } catch (Exception e) { e.printStackTrace(); }
    }
}