package com.kapturecrm.nlpdashboardservice;

import net.sf.json.JSONObject;

public class DoubtTests {

    public static void main(String[] args) {
        JSONObject obj = new JSONObject(true);
        obj.put("name", "foo"); //FAILS
    }

}
