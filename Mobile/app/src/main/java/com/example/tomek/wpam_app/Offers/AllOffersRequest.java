package com.example.tomek.wpam_app.Offers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.tomek.wpam_app.Utils.IpAdress;

import org.json.JSONArray;


/**
 * Created by Tomek on 10.04.2018.
 */

public class AllOffersRequest extends JsonArrayRequest {
    public static final String All_OFFERS_REQUEST_URL = "http://" + IpAdress.IP_ADRESS + "/jpa/posts";

    public AllOffersRequest(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        super(Request.Method.GET, All_OFFERS_REQUEST_URL,null, listener, errorListener);
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }
}
