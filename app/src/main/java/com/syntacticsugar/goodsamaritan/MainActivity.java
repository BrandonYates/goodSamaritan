package com.syntacticsugar.goodsamaritan;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_MAIN = 1;
    //ListView listView;
    String userInfo;
    private UserService userService = new UserService();
    OnJSONResponseCallback callback = new OnJSONResponseCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set custom font
        TextView myPointsTitle = (TextView) findViewById(R.id.myPointsTitle);
        TextView myPoints = (TextView) findViewById(R.id.myPoints);
        Typeface font = Typeface.createFromAsset(getAssets(), "sam_marker.ttf");
        myPointsTitle.setTypeface(font);
        myPoints.setTypeface(font);
        //end set custom font

        userInfo = getIntent().getStringExtra("userId");

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(userService != null && userInfo != null) {
                            try {
                                JSONObject rawUser = userService.findUserById(userInfo);

                                if(rawUser != null) {
                                    callback.onJSONResponse(true, rawUser);
                                    User user = new User(callback.getObject());

                                    System.out.println("User: " + user.toString());
                                    Collection<Deed> deeds = user.getDeeds();
                                    String[] deedTitles = new String[deeds.size()];

                                    for(int i = 0; i < deeds.size(); ++i) {
                                        Deed deed = (Deed)deeds.toArray()[i];
                                        deedTitles[i] = deed.getDescription();
                                    }
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(userService == null) {
                            System.out.println("User Service null");
                        }
                        else if (userInfo == null) {
                            System.out.println("UserInfo null");
                        }
                    }
                }, 3000);


        //listview
        // Get ListView object from xml
        //listView = (ListView) findViewById(R.id.deedList);

        // Defined Array values to show in ListView
        //String[] deedTitles = new String[] { "deed 1", "deed 2", "deed3"};

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Fourth - the Array of data

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
         //       android.R.layout.simple_list_item_1, android.R.id.text1, deedTitles);

        // Assign adapter to ListView
        //listView.setAdapter(adapter);

        // ListView Item Click Listener
/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

            }

        });
        // end of listview code */



        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng startPos = new LatLng(30.1894032, -85.7231643);

        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Please enable location tracking to use this appliciation", Toast.LENGTH_LONG).show();
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPos, 13));

        map.addMarker(new MarkerOptions()
                .title("Good Deeds")
                .position(startPos));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void gotoMenu(View view) {
        System.out.println("gotoMenu Called!");

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra("userId", userInfo);
        startActivityForResult(intent, REQUEST_MAIN);
    }
}
