package com.iasishealthcare.iasis.xmlparser;

import java.util.ArrayList;

/**
 * Created by admin on 9/24/15.
 */
public class XMLGettersSetters {

    private ArrayList<String> title = new ArrayList<String>();

    public ArrayList<String> getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title.add(title);
    }
}
