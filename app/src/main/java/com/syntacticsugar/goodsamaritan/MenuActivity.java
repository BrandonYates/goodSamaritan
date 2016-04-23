package com.syntacticsugar.goodsamaritan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Handler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by brandonyates on 4/21/16.
 * Revised by jdestefanis on 4/22/16
 */
public class MenuActivity extends AppCompatActivity {

    private static final int REQUEST_MAIN = 1;
    private static final int REQUEST_DEED = 1;
    ListView listView;
    String userInfo;
    JSONArray deeds;
    ArrayList<String> deedTitles = new ArrayList<String>();

    private UserService userService = new UserService();
    OnJSONResponseCallback callback = new OnJSONResponseCallback();

    private class GetUser extends AsyncTask<String, Void, JSONObject> {
        TextView menuPageTitle;
        TextView userText;
        TextView myPointsText;
        Typeface font;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            menuPageTitle = (TextView) findViewById(R.id.menuPageTitle);
            userText = (TextView) findViewById(R.id.userText);
            myPointsText = (TextView) findViewById(R.id.myPointsText);
            font = Typeface.createFromAsset(getAssets(), "sam_marker.ttf");
            menuPageTitle.setTypeface(font);
            userText.setTypeface(font);
            myPointsText.setTypeface(font);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            if (userService != null && params[0] != null) {
                try {
                    return userService.findUserById(params[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (userService == null) {
                System.out.println("User Service null");
            } else if (params[0] == null) {
                System.out.println("UserInfo null");
                System.out.println(params[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject obj) {
            super.onPostExecute(obj);
            if (obj != null) {
                System.out.println("rawUser " + obj.toString());
                //callback.onJSONResponse(true, obj);
                try {
                    User userData = new User(obj);
                    userText.setText(userData.getFirstName());
                    myPointsText.setText(userData.getPoints().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class GetUserDeeds extends AsyncTask<String, Void, JSONArray> {

        TextView myDeedsText;
        Typeface font;

        @Override
        protected JSONArray doInBackground(String... params) {
            if (params[0] != null) {
                System.out.println("Getting User's Deeds");
                    RequestParams reqparam = new RequestParams();
                    reqparam.add("uid", userInfo);

                    RestUtils.syncget("findDeedByRequestingUserId", reqparam, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                            System.out.println("JSONObject!");
                            System.out.println(response.toString());

                            //save object for returning
                            callback.onJSONArrayResponse(true, response);
                        }
                    });
                    return callback.getArray();
            } else {
                System.out.println("UserInfo null");
                System.out.println(params[0]);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDeedsText = (TextView) findViewById(R.id.myDeedsText);
            font = Typeface.createFromAsset(getAssets(), "sam_marker.ttf");
            myDeedsText.setTypeface(font);
        }

        @Override
        protected void onPostExecute(JSONArray obj) {
            super.onPostExecute(obj);
            try {
                listView = (ListView) findViewById(R.id.myDeedList);
                for (int i = 0; i < obj.length(); i++) {
                        Deed d = new Deed((JSONObject) obj.get(i));
                        deedTitles.add(d.getDescription());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, deedTitles);

                listView.setAdapter(adapter);

                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // ListView Clicked item index
                        int itemPosition = position;

                        // ListView Clicked item value
                        String itemValue = (String) listView.getItemAtPosition(position);

                        // Show Alert
                        Toast.makeText(getApplicationContext(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //userInfo = getIntent().getStringExtra("userId");
        userInfo = getIntent().getStringExtra("userId");
        new GetUser().execute(userInfo);
        new GetUserDeeds().execute(userInfo);
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

    public void gotoLogout(View view) {

        System.out.println("gotoLogout Called!");
    }

    public void createDeed(View view) {
        System.out.println("create deed Called!");

        Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
        intent.putExtra("userId", userInfo);
        startActivityForResult(intent, REQUEST_DEED);
    }

    public void gotoMain(View view) {
        System.out.println("gotoMain Called!");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", userInfo);
        startActivityForResult(intent, REQUEST_MAIN);
    }

    public void logout(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_MAIN);
    }
}
