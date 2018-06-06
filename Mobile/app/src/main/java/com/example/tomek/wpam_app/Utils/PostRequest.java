package com.example.tomek.wpam_app.Utils;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by Tomek on 20.04.2018.
 */

public class PostRequest extends JsonObjectRequest{
    public PostRequest(JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, String url){
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }
}
