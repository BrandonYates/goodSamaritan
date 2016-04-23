package com.syntacticsugar.goodsamaritan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sam on 4/22/16.
 */

public class CreateActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_MAIN = 1;
    String userInfo;

    @Bind(R.id.deedTitle) EditText _deedTitle;
    @Bind(R.id.deedDesc) EditText _deedDesc;
    @Bind(R.id.deedPoints) EditText _deedPoints;
    Button _createButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

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

    public void cancel(View view) {
        System.out.println("cancel Called!");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", userInfo);
        startActivityForResult(intent, REQUEST_MAIN);
    }

    public void createDeed(View view) {
    //     ^^^^ need to see if this is returning a success or not


    }


}
