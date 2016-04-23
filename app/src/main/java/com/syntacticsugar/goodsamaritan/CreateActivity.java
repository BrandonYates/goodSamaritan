package com.syntacticsugar.goodsamaritan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by sam on 4/22/16.
 */

public class CreateActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener {

    private static final int REQUEST_MENU = 1;
    String userInfo;

    @Bind(R.id.deedTitle)
    EditText _deedTitle;
    @Bind(R.id.deedDesc)
    EditText _deedDesc;
    @Bind(R.id.deedPoints)
    NumberPicker _deedPoints;
    int deedPointsValue;
    Button _createButton;


    private static GoogleMap map;
    double deedLat = 30.1894032;
    double deedLong = -85.7231643;
    boolean markerCreated = false;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);


        // map stuff
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //numberpicker stuff
        _deedPoints.setMinValue(1);
        _deedPoints.setMaxValue(14);
        _deedPoints.setWrapSelectorWheel(false);

        _deedPoints.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub

                deedPointsValue = newVal;
            }
        });

        userInfo = getIntent().getStringExtra("userId");
        System.out.println("********* ****** ***** *************** *********** user info is " + userInfo);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap mapObject) {

        map = mapObject;

        LatLng startPos = new LatLng(30.1894032, -85.7231643);

        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Please enable location tracking to use this appliciation", Toast.LENGTH_LONG).show();
        }

        CameraPosition INIT =
                new CameraPosition.Builder()
                        .target(startPos)
                        .zoom(17.5F)
                        .bearing(300F) // orientation
                        .tilt( 50F) // viewing angle
                        .build();

        // use map to move camera into position
        map.moveCamera( CameraUpdateFactory.newCameraPosition(INIT) );

        //create initial marker
        map.addMarker( new MarkerOptions()
                .position( startPos )
                .title("You are here!")).showInfoWindow();

//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPos, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Good Deeds")
//                .position(startPos));

        map.setOnMarkerDragListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMapClickListener(this);
    }

    public void cancel(View view) {
        System.out.println("cancel Called!");

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra("userId", userInfo);
        startActivityForResult(intent, REQUEST_MENU);
    }

    public void createDeed(View view) {
        //     ^^^^ need to see if this is returning a success or not

        if(validateDeed()) {
            // save deed
            //create a deed object
            System.out.println(" **** valid deed");
            System.out.println(" **** create mydeed");
            Deed myDeed = new Deed();
            // create the description

            final String myDescription = _deedTitle.getText().toString() + " - " + _deedDesc.getText().toString();
            System.out.println(" **** create desc " + myDescription);

            // Create the points
            int myPoints = _deedPoints.getValue();
            System.out.println(" **** points for deed " + myPoints);

            //create the location
            Location myLocation = new Location();
            myLocation.setLatitude(deedLat);
            myLocation.setLongitude(deedLong);
            System.out.println(" **** location  lat " + myLocation.getLatitude() + " long " + myLocation.getLongitude());

            // get the user id
            String myId = userInfo;
            System.out.println(" ****  user info " + myId);

            myDeed.setDescription(myDescription);
            myDeed.setPointValue(myPoints);
            myDeed.setLocation(myLocation);
            myDeed.setRequestingUserId(myId);

            final Long tsLong = System.currentTimeMillis()/1000;

            boolean b = new Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            try {
                                RequestParams params = new RequestParams();
                                params.add("desc", myDescription);
                                params.add("date", tsLong.toString());
                                params.add("uid", userInfo);
                                params.add("latitude", Double.toString(deedLat));
                                params.add("longitude", Double.toString(deedLong));


                                RestUtils.post("createDeed/params", params, new AsyncHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        System.out.println("***** create deed in the back onSuccess");
                                        try {

                                            String response = new String(responseBody, "UTF-8");

                                            // JSON Object
                                            JSONObject obj = new JSONObject(response);

                                            System.out.println("statusCode: " + statusCode);
                                            System.out.println(obj.toString());

                                            if(statusCode == 200) {
                                               // status okay
                                            } else {
                                              // status failed
                                            }

                                            System.out.println(obj.toString());

                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            System.out.println(" ******* EXCEPTION!");
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        try {
                                            String response = new String(responseBody, "UTF-8");
                                            //System.out.println(statusCode + ": " + response);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }, 3000);
            
            System.out.println(" **** end create ");
        }
    }

    public boolean validateDeed() {

        System.out.println(" **** called validate ");

        boolean valid = true;

        String validDT = _deedTitle.getText().toString();
        String validDD = _deedDesc.getText().toString();

        if(validDT.isEmpty() || validDT.length() < 2 || validDT.length() > 50) {
            _deedTitle.setError("enter a valid title");
            valid = false;
        } else {
            _deedTitle.setError(null);
        }

        if(validDD.isEmpty() || validDD.length() < 2 || validDD.length() > 200) {
            _deedDesc.setError("enter a valid description less than 200 characters");
            valid = false;
        } else {
            _deedDesc.setError(null);
        }
        if(markerCreated == false) {
            valid = false;
            Toast.makeText(getBaseContext(), "Create a marker for your deed by long clicking on the map!", Toast.LENGTH_LONG).show();
        }

        return valid;
    }


    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        LatLng dragPosition = arg0.getPosition();
        double dragLat = deedLat = dragPosition.latitude;
        double dragLong = deedLong = dragPosition.longitude;
        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
        Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        map.animateCamera(CameraUpdateFactory.newLatLng(arg0));
    }


    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub

        if(_deedTitle.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Create A Deed Title First!", Toast.LENGTH_LONG).show();
        } else {
            if (markerCreated == false) {
                //create new marker when user long clicks
                map.addMarker(new MarkerOptions()
                        .position(arg0)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.heart))
                        .draggable(true)
                        .title(_deedTitle.getText().toString())).showInfoWindow();
                markerCreated = true;
                deedLat = arg0.latitude;
                deedLong = arg0.longitude;

                System.out.println("***** lat long is " + deedLat + " " + deedLong + " " + arg0.latitude);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Create Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.syntacticsugar.goodsamaritan/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Create Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.syntacticsugar.goodsamaritan/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
