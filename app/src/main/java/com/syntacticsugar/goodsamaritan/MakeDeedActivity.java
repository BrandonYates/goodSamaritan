package com.syntacticsugar.goodsamaritan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * Created by brandonyates on 4/21/16.
 */
public class MakeDeedActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = "MakeDeedActivity";

    @Bind(R.id.input_description) EditText _descriptionText;
    @Bind(R.id.input_point_value) EditText _pointValueText;
    DatePicker _dateText;
    GoogleMap _map;
    Button _acceptButton;

    private LocationManager locationManager;
    private android.location.Location location;

    Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        final String description = _descriptionText.getText().toString();
//        final int pointValue = Integer.parseInt(_pointValueText.getText().toString());
//        Date expirationDate = _dateText;

//        _dateText = (DatePicker) findViewById(R.id.input_expiration_date);

        _acceptButton = (Button) findViewById(R.id.acceptButton);
        _acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeed();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000, 1, this);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Please enable location tracking to use this appliciation", Toast.LENGTH_LONG).show();
        }

    }

    public boolean validate () {

        boolean valid = true;
        String description = _descriptionText.getText().toString();
        int pointValue = Integer.parseInt(_pointValueText.getText().toString());

        if(description.isEmpty() || description.length() < 2 || description.length() > 50) {
            _descriptionText.setError("enter a valid name");
            valid = false;
        } else {
            _descriptionText.setError(null);
        }

        if(pointValue == 0 || pointValue > 14) {
            _pointValueText.setError("please enter a value between 0 - 14 to estimate the required time");
            valid = false;
        } else {
            _pointValueText.setError(null);
        }



        return valid;
    }

    public void createDeed () {
        Log.d(TAG, "CreateDeed");

        if (!validate()) {
            onCreateDeedFailed();
            return;
        }

        _acceptButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MakeDeedActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Deed...");
        progressDialog.show();

        final String description = _descriptionText.getText().toString();
        final String date = _dateText.toString();
        final int pointValue = Integer.parseInt(_pointValueText.getText().toString());

        final String userId = getIntent().getStringExtra("userId");

        // TODO: Implement signup logic here.
        boolean b = new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            RequestParams params = new RequestParams();
                            params.add("desc", description);
                            params.add("date", date);
                            params.add("uid", userId);
                            params.add("latitude", String.valueOf(marker.getPosition().latitude));
                            params.add("longitude", String.valueOf(marker.getPosition().longitude));
                            params.add("pointValue", String.valueOf(pointValue));

                            RestUtils.post("createDeed/params", params, new AsyncHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    System.out.println("##############");
                                    System.out.println("onSuccess");
                                    try {

                                        String response = new String(responseBody, "UTF-8");

                                        // JSON Object
                                        JSONObject obj = new JSONObject(response);

                                        System.out.println("statusCode: " + statusCode);
                                        System.out.println(obj.toString());

                                        if(statusCode == 200) {
                                            onCreateDeedSuccess();
                                        } else {
                                            onCreateDeedFailed();
                                        }

                                        System.out.println(obj.toString());

                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        System.out.println("EXCEPTION!");
                                        e.printStackTrace();
                                    }
                                    System.out.println("##############");
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    try {
                                        String response = new String(responseBody, "UTF-8");
//                                        System.out.println(statusCode + ": " + response);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            onCreateDeedFailed();
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    public void onCreateDeedSuccess() {
        _acceptButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "Deed Creation succeeded", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }

    public void onCreateDeedFailed() {
        Toast.makeText(getBaseContext(), "Deed Creation failed", Toast.LENGTH_LONG).show();

        _acceptButton.setEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng startPos = new LatLng(location.getLatitude(), location.getLongitude());

        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Please enable location tracking to use this appliciation", Toast.LENGTH_LONG).show();
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPos, 13));

        marker = map.addMarker(new MarkerOptions()
                .title("Current Deed")
                .position(startPos)
                .draggable(true));
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        location = location;
    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
