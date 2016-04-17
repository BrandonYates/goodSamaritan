package com.syntacticsugar.goodsamaritan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    }
}
