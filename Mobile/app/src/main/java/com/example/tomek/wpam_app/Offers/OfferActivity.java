package com.example.tomek.wpam_app.Offers;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tomek.wpam_app.MainActivity;
import com.example.tomek.wpam_app.R;
import com.example.tomek.wpam_app.Utils.DirectionsParser;
import com.example.tomek.wpam_app.Utils.IpAdress;
import com.example.tomek.wpam_app.Utils.Post;
import com.example.tomek.wpam_app.Utils.PostRequest;
import com.example.tomek.wpam_app.Utils.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gmap;
    private Post post;
    private User user;
    TextView time;
    LatLng home;
    LatLng dest;
    Polyline polyline;
    boolean userIsInteracting = false;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        TextView username = (TextView) findViewById(R.id.username);
        TextView level = (TextView) findViewById(R.id.level);
        TextView address = (TextView) findViewById(R.id.address);
        TextView price = (TextView) findViewById(R.id.price);
        TextView date = (TextView) findViewById(R.id.date);
        TextView contact = (TextView) findViewById(R.id.contact);
        time = (TextView) findViewById(R.id.time);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());


        final Button joinOfferButton = (Button) findViewById(R.id.joinButton);

        Intent i = getIntent();
        post = (Post) i.getSerializableExtra("post");
        user = (User) getApplicationContext();

        String player = "Gracz: " + post.getAuthor();
        username.setText(player);
        String lev = "Poziom: " + post.getLevel();
        level.setText(lev);
        String addr = "Adres: " + post.getAddress();
        address.setText(addr);
        String pri = "Cena: " + Double.toString(post.getPrice());
        price.setText(pri);
        String dat = "Data: " + post.getDate();
        date.setText(dat);
        contact.setText(post.getContact());

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


        joinOfferButton.setOnClickListener(new View.OnClickListener() {
            public final String CREATE_REQUEST_URL = "http://" + IpAdress.IP_ADRESS + "/jpa/posts/" + post.getId();
            private final String TAG = OfferActivity.class.getSimpleName();

            @Override
            public void onClick(View v) {

                Map<String, String> params = new HashMap<>();
                params.put("username", user.getUsername());

                JSONObject request_json = new JSONObject(params);

                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(OfferActivity.this, MainActivity.class);
                        OfferActivity.this.startActivity(intent);
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error at sign in : " + error.getMessage());
                    }
                };

                PostRequest joinRequest = new PostRequest(request_json, responseListener, errorListener, CREATE_REQUEST_URL);
                RequestQueue queue = Volley.newRequestQueue(OfferActivity.this);
                queue.add(joinRequest);

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onUserInteraction () {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gmap = googleMap;
        String addr = post.getAddress();
        this.dest = getLocationFromAddress(addr);
        this.home = getLocationFromAddress(user.getAddress());

        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.dest, 14));

        MarkerOptions marker = new MarkerOptions().position(this.dest).title(addr);
        MarkerOptions homeMarker = new MarkerOptions().position(this.home).title("Dom");
        homeMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.home));

        gmap.addMarker(marker);
        gmap.addMarker(homeMarker);

        String url = getRequestUrl(this.home, this.dest, "driving");
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            LatLng point = new LatLng(lat, lng);

            return point;

        } catch (Exception e) {
            return null;
        }
    }

    public String getRequestUrl(LatLng origin, LatLng destination, String travel_mode) {
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=" + travel_mode;
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    public String requestDirection(String reqUrl) throws Exception{
        String response = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            response = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }

        return response;
    }


    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
            TimeParser timeParser = new TimeParser();
            timeParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                polyline = gmap.addPolyline(polylineOptions);
                userIsInteracting = true;
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class TimeParser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsonObject = null;
            String time = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                time = directionsParser.parseTime(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return time;
        }

        @Override
        protected void onPostExecute(String result) {
            String t = "Czas dojazdu: " + result;
            time.setText(t);
        }
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (userIsInteracting) {
                if (polyline != null)
                    polyline.remove();
                String url = getRequestUrl(home, dest, parent.getItemAtPosition(pos).toString());
                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                taskRequestDirections.execute(url);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }

}
