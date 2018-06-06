package com.example.tomek.wpam_app;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.tomek.wpam_app.Offers.AllOffersActivity;
import com.example.tomek.wpam_app.Offers.CreateOfferActivity;
import com.example.tomek.wpam_app.Offers.MyOffersActivity;
import com.example.tomek.wpam_app.Offers.MyJoinedOffersActivity;
import com.example.tomek.wpam_app.Utils.Post;
import com.example.tomek.wpam_app.Utils.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        //final TextView tvWelcomeMsg = (TextView) findViewById(R.id.welcomeText);
        final Button allOffersButton = (Button) findViewById(R.id.allOffers);
        final Button myOffersButton = (Button) findViewById(R.id.myOffers);
        final Button myJoinedOffersButton = (Button) findViewById(R.id.myJoinedOffers);
        final Button profileButton = (Button) findViewById(R.id.profile);
        final Button createButton = (Button) findViewById(R.id.createButton);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        User user = (User) getApplicationContext();
        String message = user.getUsername() + " welcome to your user area";
        //tvWelcomeMsg.setText(message);

        final String TAG = MainActivity.class.getSimpleName();
        allOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AllOffersActivity.class);
                MainActivity.this.startActivity(intent);

                Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //List<Post> posts = parsePostJson(response);
//                        Intent intent = new Intent(MainActivity.this, AllOffersActivity.class);
//                        intent.putExtra("posts", (Serializable) posts);
//                        MainActivity.this.startActivity(intent);
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error at sign in : " + error.getMessage());
                    }
                };



            }
        });

        myOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOffersActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        myJoinedOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyJoinedOffersActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(MainActivity.this, CreateOfferActivity.class);
                MainActivity.this.startActivity(createIntent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To do
            }
        });

    }


    private List<Post> parsePostJson(JSONArray response){

        List<Post> allPosts = new ArrayList<>();

        for (int i=0; i < response.length(); i++) {
            try {
                JSONObject pt = response.getJSONObject(i);
                Post post = new Post(pt.getInt("id"), pt.getString("level"), pt.getString("address"),
                        pt.getDouble("price"), pt.getString("contact"));
                allPosts.add(post);
            } catch (JSONException e){

            }
        }
        return allPosts;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_offers) {
            Intent intent = new Intent(MainActivity.this, AllOffersActivity.class);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.my_offers) {
            Intent intent = new Intent(MainActivity.this, MyOffersActivity.class);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.my_joined_offers) {
            Intent intent = new Intent(MainActivity.this, MyJoinedOffersActivity.class);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.add_offer) {
            Intent createIntent = new Intent(MainActivity.this, CreateOfferActivity.class);
            MainActivity.this.startActivity(createIntent);
        } else if (id == R.id.profile) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
