package com.example.tomek.wpam_app.Offers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

/**
 * Created by Tomek on 20.04.2018.
 */

public class MyOffersRequest extends JsonArrayRequest {

    public MyOffersRequest(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener, String url){
        super(Request.Method.GET, url,null, listener, errorListener);
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }
}
