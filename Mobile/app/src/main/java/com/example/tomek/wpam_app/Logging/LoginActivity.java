package com.example.tomek.wpam_app.Logging;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tomek.wpam_app.MainActivity;
import com.example.tomek.wpam_app.R;
import com.example.tomek.wpam_app.Utils.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final Button signInButton = (Button) findViewById(R.id.signIn);
        final TextView registerLink = (TextView) findViewById(R.id.register);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            private final String TAG = LoginActivity.class.getSimpleName();
            @Override
            public void onClick(View view) {
                final String uname = username.getText().toString().trim();
                final String pass = password.getText().toString().trim();
                Map<String, String> params = new HashMap<>();
                params.put("username", uname);
                params.put("password", pass);

                JSONObject request_json = new JSONObject(params);

                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = Boolean.valueOf(response.getString("success"));

                            if (success) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //intent.putExtra("username", uname);
                                User user = (User) getApplicationContext();
                                user.setUsername(uname);
                                user.setPassword(pass);
                                user.setAddress(response.getString("address"));
                                user.setContact(response.getString("contact"));
                                LoginActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error at sign in : " + error.getMessage());
                    }
                };

                LoginRequest loginRequest = new LoginRequest(request_json, responseListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

    }
}
