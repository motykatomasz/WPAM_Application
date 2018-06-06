package com.example.tomek.wpam_app.Offers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tomek.wpam_app.Logging.LoginActivity;
import com.example.tomek.wpam_app.Logging.RegisterActivity;
import com.example.tomek.wpam_app.Logging.RegisterRequest;
import com.example.tomek.wpam_app.MainActivity;
import com.example.tomek.wpam_app.R;
import com.example.tomek.wpam_app.Utils.IpAdress;
import com.example.tomek.wpam_app.Utils.Post;
import com.example.tomek.wpam_app.Utils.PostRequest;
import com.example.tomek.wpam_app.Utils.User;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class CreateOfferActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, NavigationView.OnNavigationItemSelectedListener{

    int year, month, day, hour, minutes;
    int yearFinal, monthFinal, dayFinal, hourFinal, minutesFinal;
    Calendar dateTime;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_add_offer);

        final EditText level = (EditText) findViewById(R.id.level);
        final EditText address = (EditText) findViewById(R.id.address);
        final EditText price = (EditText) findViewById(R.id.price);
        date = (TextView) findViewById(R.id.date);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Button createButton = (Button) findViewById(R.id.createButton);

        final User user = (User) getApplicationContext();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOfferActivity.this, CreateOfferActivity.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            public final String CREATE_REQUEST_URL = "http://" + IpAdress.IP_ADRESS + "/jpa/users/" + user.getUsername() + "/posts";
            private final String TAG = CreateOfferActivity.class.getSimpleName();

            @Override
            public void onClick(View v) {

                final String lev = level.getText().toString();
                final String addr = address.getText().toString();
                final String da = dateFormat.format(dateTime.getTime());
                final String pri = price.getText().toString();
                final String contact = user.getContact();
                Map<String, String> params = new HashMap<>();

                params.put("level", lev);
                params.put("address", addr);
                params.put("date", da);
                params.put("price", pri);
                params.put("contact", contact);

                JSONObject request_json = new JSONObject(params);

                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(CreateOfferActivity.this, MainActivity.class);
                        CreateOfferActivity.this.startActivity(intent);
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error at sign in : " + error.getMessage());
                    }
                };

                PostRequest createRequest = new PostRequest(request_json, responseListener, errorListener, CREATE_REQUEST_URL);
                RequestQueue queue = Volley.newRequestQueue(CreateOfferActivity.this);
                queue.add(createRequest);
            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int y, int z){
        yearFinal = i;
        monthFinal = y +1;
        dayFinal = z;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateOfferActivity.this, CreateOfferActivity.this,
        hour, minutes, true);
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int y){
        hourFinal = i;
        minutesFinal = y;
        dateTime = new GregorianCalendar(yearFinal, monthFinal, dayFinal, hourFinal, minutesFinal);
        date.setText(dateTime.getTime().toString());

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_offers) {
            Intent intent = new Intent(CreateOfferActivity.this, AllOffersActivity.class);
            CreateOfferActivity.this.startActivity(intent);
        } else if (id == R.id.my_offers) {
            Intent intent = new Intent(CreateOfferActivity.this, MyOffersActivity.class);
            CreateOfferActivity.this.startActivity(intent);
        } else if (id == R.id.my_joined_offers) {
            Intent intent = new Intent(CreateOfferActivity.this, MyJoinedOffersActivity.class);
            CreateOfferActivity.this.startActivity(intent);
        } else if (id == R.id.add_offer) {
            Intent createIntent = new Intent(CreateOfferActivity.this, CreateOfferActivity.class);
            CreateOfferActivity.this.startActivity(createIntent);
        } else if (id == R.id.profile) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
