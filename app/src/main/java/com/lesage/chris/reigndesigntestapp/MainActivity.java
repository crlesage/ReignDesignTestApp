package com.lesage.chris.reigndesigntestapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reginald.swiperefresh.CustomSwipeRefreshLayout;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 *
 * Test App for Reign Design. This application is a simple display app
 * that shows the user many "posts" about Android Hacker News, from
 * http://hn.algolia.com/api/v1/search_by_date?query=android. There is the ability
 * to delete unwanted or read posts, refresh the list to get the most recent
 * posts, and click on a post to get a view of the article via the web.
 *
 *  Created by Chris LeSage.
 *      Date: January 15th, 2016.
 */

public class MainActivity extends AppCompatActivity {

    /* ------------------- CONSTANTS ----------------------  */

    private static final int WEB_POST = 1;
    private static final String MY_PREFS_NAME = "saved Data";
    SwipeMenuListView swipeMenuListView;
    CustomSwipeRefreshLayout swipeRefreshLayout;
    DataAdapter hitsAdapter;
    ArrayList<Hits> hits;
    Gson gsonSave = new Gson();

    //Start the current time to be very big (5245 days ago), so it will set most current post value, the minimum of all posts.
    long mostCurrentPostTime = 1000000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Adapter and "hits"
        hitsAdapter = new DataAdapter(this);
        if(hits == null) {
            hits = new ArrayList<>();
        }

        //Initialization
        setUpRefresh();
        initializeSwipeList();
        setMenuListClickListener();
        initializeDataAdapter();

        //Grab initial data and display
        if(!isNetworkAvailable()) {
            getSavedData();
            displayData();
        } else {
            getJsonData();
            saveData();
        }
    }

    /* ------------------- INITIALIZATION CALLS ----------------------  */

    /* Set up the refresh for the pull down listview */
    public void setUpRefresh(){
        swipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(new CustomSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isNetworkAvailable()) {
                    getSavedData();
                    displayData();
                } else {
                    getJsonData();
                    saveData();
                }
            }
        });
    }

    /* Initialize the 'delete' button for each listview item */
    public void initializeSwipeList(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // Create "delete" button for listview
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                openItem.setBackground(new ColorDrawable(getResources().getColor(R.color.red)));
                openItem.setWidth(450);
                openItem.setTitle("Delete");
                openItem.setTitleSize(18);
                openItem.setTitleColor(getResources().getColor(R.color.white));

                menu.addMenuItem(openItem);
            }
        };
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.listView);
        swipeMenuListView.setMenuCreator(creator);
    }

    /* Set click listener for 'delete' button in listview */
    public void setMenuListClickListener() {
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.listView);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        hits.remove(hitsAdapter.getItem(position));
                        hitsAdapter.removeItem(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    /* ------------------- DISPLAY DATA IN LIST VIEW ----------------------  */

    /* Initialize the hits adapter for the list view and its click to webview */
    public void initializeDataAdapter(){
        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
        listView.setAdapter(hitsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //If clicking certain hit, go to hit page
                goToWebView(position);
            }
        });
    }

    /* Updates list view with current hits */
    public void displayData(){
        hitsAdapter.removeAllItems();
        if (hits.size() == 0) {
            Toast.makeText(getApplicationContext(),"No hits", Toast.LENGTH_SHORT).show();
        }
        if(hits.size() > 0 && hits != null){
            for(Hits hit : hits){
                hitsAdapter.addItem(hit);
            }
        }
        swipeRefreshLayout.refreshComplete();
    }

    /* ------------------- DATA SAVE ----------------------  */

    /* Saves current Hits into SharedPreferences */
    public void saveData(){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        gsonSave = new Gson();
        if(hits != null && hits.size() > 0) {
            String savedHits = gsonSave.toJson(hits);
            editor.putString("jsonData", savedHits);
            editor.putLong("currentTime", mostCurrentPostTime);
            editor.commit();
        }
    }

    /* Updates the current Hits to be what is in SharedPreferences */
    public void getSavedData(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Type type = new TypeToken<List<Hits>>(){}.getType();
        String tempSavedHits = prefs.getString("jsonData", "");
        List<Hits> hitsList = gsonSave.fromJson(tempSavedHits, type);
        ArrayList<Hits> savedHits = new ArrayList<>(hitsList);
        hits.clear();

        hits = savedHits;
        mostCurrentPostTime = prefs.getLong("currentTime", 1000000000);
    }

    /* ------------------- VIEW WEB CALL ----------------------  */

    /* Call for intent to web view activity */
    public void goToWebView(int position){
        // Starting Intent and going to webPost page
        Hits hit = hitsAdapter.getItem(position);
        Intent intentPreferences = new Intent(getApplicationContext(), WebPostActivity.class);
        if((hit.getUrl() == null || hit.getUrl().equals("")) && (hit.getStory_url() == null || hit.getStory_url().equals(""))){
            Toast.makeText(getApplicationContext(), "No site available for this post.", Toast.LENGTH_SHORT).show();
            return;
        } else if (hit.getUrl() == null || hit.getUrl().equals("")) {
            intentPreferences.putExtra("url", hit.getStory_url());
        } else {
            intentPreferences.putExtra("url", hit.getUrl());
        }
        intentPreferences.putExtra("currentTime", mostCurrentPostTime);
        startActivityForResult(intentPreferences, WEB_POST);
    }

    /* Return from the clicked post screen */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1: //webPost
                //mostCurrentPostTime = data.getLongExtra("currentTime", 1000000000);
//                if(!isNetworkAvailable()) {
//                    getSavedData();
//                    displayData();
//                } else{
//                    getJsonData();
//                    saveData();
//                }
                break;
        }
    }


    /* ------------------- SERVER CALLS ----------------------  */

    /* Initiate Retrofit to grab server data */
    private Retrofit initiateRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    /* Retrofit grabbing data from URL if needed */
    public void getJsonData(){
        Retrofit retrofit = initiateRetrofit();
        ApiInterface dataCall = retrofit.create(ApiInterface.class);

        String query = "android";
        Call<JsonData> call = dataCall.getData(query);

        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                JsonData results = response.body();
                if(hits.size() <= 0) {
                    hits = results.getHits();
                    initializeCurrentPostTime();
                }
                else {
                    saveNewHits(results.getHits());
                }
                Log.d("myapp", "Got data");
                displayData();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("myapp", "FAILED GRAB DATA: " + t);
            }
        });
    }

    /* Updates the newest Post time to only add new posts to the listview */
    public void initializeCurrentPostTime(){
        Log.d("myapp", "time " + getTimeAgo(mostCurrentPostTime));
        for(Hits hit : hits){
            if(hit.created_at_i > mostCurrentPostTime){
                mostCurrentPostTime = hit.getCreated_at_i();
            }
        }
        Log.d("myapp", "new current time: " + getTimeAgo(mostCurrentPostTime));
    }

    /* Only update the hits if they are "new" */
    public void saveNewHits(ArrayList<Hits> newHits){
        for(Hits hit : newHits){
            if(hit.created_at_i > mostCurrentPostTime){
                Log.d("myapp", "Added new hit");
                hits.add(0, hit);
                hitsAdapter.notifyDataSetChanged();
                mostCurrentPostTime = hit.created_at_i;
                Log.d("myapp", "new time: " + getTimeAgo(mostCurrentPostTime));
            }
        }
    }

    /* Boolean function to see if connected to internet */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "time";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1h";
        }else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "h";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + "d";
        }
    }

}
