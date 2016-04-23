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
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
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
    String[] deedTitles;

    private UserService userService = new UserService();
    OnJSONResponseCallback callback = new OnJSONResponseCallback();

    private class GetUser extends AsyncTask<String, Void, JSONObject> {
        TextView menuPageTitle;
        TextView userText;
        TextView myPointsText;
        TextView myDeedsText;
        Typeface font;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            menuPageTitle = (TextView) findViewById(R.id.menuPageTitle);
            userText = (TextView) findViewById(R.id.userText);
            myPointsText = (TextView) findViewById(R.id.myPointsText);
            myDeedsText = (TextView) findViewById(R.id.myDeedsText);
            font = Typeface.createFromAsset(getAssets(), "sam_marker.ttf");
            menuPageTitle.setTypeface(font);
            userText.setTypeface(font);
            myPointsText.setTypeface(font);
            myDeedsText.setTypeface(font);
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
                    ArrayList<Deed> deeds = userData.getDeeds();
                    String[] deedTitles = new String[deeds.size()];
                    for (int i = 0; i < deeds.size(); ++i) {
                        Deed deed = (Deed) deeds.toArray()[i];
                        deedTitles[i] = deed.getDescription();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

        //listview
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.myDeedList);

        // Defined Array values to show in ListView
        String[] deedTitles = new String[] { "deed 1", "deed 2", "deed3"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, deedTitles);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

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
        // end of listview code
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

    public JSONArray getMyDeeds(final String myUserId){

        // get the deeds
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
//                         On complete call either onLoginSuccess or onLoginFailed

                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.add("uid", myUserId);
                        try {

                            client.get("http://10.0.2.2:8080/findDeedByRequestingUserId", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    System.out.println("##############");
                                    System.out.println("DEEDS");
                                    System.out.println(responseBody);
                                    try {
                                        if (responseBody == null) {
                                            System.out.println("# responseBody Null");
                                            throw new Exception();
                                        }
                                        String response = new String(responseBody, "UTF-8");
                                        // JSON Object
                                        deeds = new JSONArray(response);

                                        System.out.println("DEEDS are : ");
                                        System.out.println(deeds.toString());

//                                        JSONObject contextObj = obj.getJSONObject("context");
//                                        JSONObject entityObj = contextObj.getJSONObject("entity");
//
//                                        String userId = entityObj.getString("id");
//                                        System.out.println("userId is " + userId);

                                        System.out.println("##############");

                                        // deed titles into array
                                        for (int i = 0; i < deeds.length(); ++i) {
                                            JSONObject deed = deeds.getJSONObject(i);
                                            String desc = deed.getString("description");

                                            deedTitles[i] = desc;
                                        }



                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        System.out.println("EXCEPTION!");
                                        System.out.println("EXCEPTION!");
                                        System.out.println("EXCEPTION!");
                                        System.out.println("EXCEPTION!");
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    try {
                                        String response = new String(responseBody, "UTF-8");
                                        System.out.println(statusCode + ": " + response);
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
        // end of gettin' da deeds

       return deeds;
    }

    public void createDeed(View view) {
        System.out.println("create deed Called!");

        Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
        intent.putExtra("userId", userInfo);
        startActivityForResult(intent, REQUEST_DEED);
    }

    public void gotoMain(View view) {
        System.out.println("gotoMain Called!");
<<<<<<< HEAD
=======

        System.out.print(" **** go to main   " + userInfo);
>>>>>>> origin/master

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", userInfo);
        startActivityForResult(intent, REQUEST_MAIN);
    }

    public void logout(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_MAIN);
    }
}
