package com.example.tomek.wpam_app.Offers;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tomek.wpam_app.MainActivity;
import com.example.tomek.wpam_app.R;
import com.example.tomek.wpam_app.Utils.IpAdress;
import com.example.tomek.wpam_app.Utils.Post;
import com.example.tomek.wpam_app.Utils.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyOffersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    List<Post> myPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_my_offers);
        final ListView offers = (ListView) findViewById(R.id.myOffersListView);
        final MyOffersActivity.CustomAdapter customAdapter = new MyOffersActivity.CustomAdapter();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        User user = (User) getApplicationContext();

        myPosts = new ArrayList<>();

        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parsePostJson(response);
                offers.setAdapter(customAdapter);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("abc", "Error at sign in : " + error.getMessage());
            }
        };

        String url = "http://" + IpAdress.IP_ADRESS + "/jpa/users/" + user.getUsername() + "/myposts";
        MyOffersRequest myOffersRequest = new MyOffersRequest(responseListener, errorListener, url);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(myOffersRequest);


        offers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Post p = (Post) parent.getAdapter().getItem(position);

                if(p.getOther() == null) {
                    Intent intent = new Intent(MyOffersActivity.this, MyOfferActivity.class);
                    intent.putExtra("post", (Serializable) p);
                    MyOffersActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(MyOffersActivity.this, MyJoinedOfferActivity.class);
                    intent.putExtra("post", (Serializable) p);
                    MyOffersActivity.this.startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    private void parsePostJson(JSONArray response){

        myPosts.clear();

        for (int i=0; i < response.length(); i++) {
            try {
                JSONObject pt = response.getJSONObject(i);

                String author = pt.getJSONObject("author").getString("username");
                String other = (!pt.isNull("other")) ? pt.getJSONObject("other").getString("username") : "Brak";
                String other_contact = (!pt.isNull("other")) ? pt.getJSONObject("other").getString("contact") : "Brak";

                Post post = new Post(pt.getInt("id"), pt.getString("level"), pt.getString("address"),
                        pt.getString("date"), pt.getDouble("price"), pt.getString("contact"),
                        author, other, other_contact);
                myPosts.add(post);
            } catch (JSONException e){

            }
        }
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myPosts.size();
        }

        @Override
        public Object getItem(int position) {
            return myPosts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.list_position,null);
            TextView address = (TextView) convertView.findViewById(R.id.post_addres);
            TextView price = (TextView) convertView.findViewById(R.id.post_price);
            TextView level = (TextView) convertView.findViewById(R.id.post_level);

            Post p = myPosts.get(position);
            String adr = "Adres: " + p.getAddress();
            address.setText(adr);
            String pri = "Cena: " + Double.toString(p.getPrice());
            price.setText(pri);
            String lev = "Poziom: " + p.getLevel();
            level.setText(lev);

            return convertView;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_offers) {
            Intent intent = new Intent(MyOffersActivity.this, AllOffersActivity.class);
            MyOffersActivity.this.startActivity(intent);
        } else if (id == R.id.my_offers) {
            Intent intent = new Intent(MyOffersActivity.this, MyOffersActivity.class);
            MyOffersActivity.this.startActivity(intent);
        } else if (id == R.id.my_joined_offers) {
            Intent intent = new Intent(MyOffersActivity.this, MyJoinedOffersActivity.class);
            MyOffersActivity.this.startActivity(intent);
        } else if (id == R.id.add_offer) {
            Intent createIntent = new Intent(MyOffersActivity.this, CreateOfferActivity.class);
            MyOffersActivity.this.startActivity(createIntent);
        } else if (id == R.id.profile) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
