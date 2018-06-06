package com.example.tomek.wpam_app.Logging;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tomek.wpam_app.Utils.IpAdress;

import org.json.JSONObject;

/**
 * Created by Tomek on 09.04.2018.
 */

public class LoginRequest extends JsonObjectRequest {
    public static final String LOGIN_REQUEST_URL = "http://" + IpAdress.IP_ADRESS + "/jpa/users/authorize";

    public LoginRequest(JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        super(LOGIN_REQUEST_URL, jsonRequest, listener, errorListener);
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }
}

