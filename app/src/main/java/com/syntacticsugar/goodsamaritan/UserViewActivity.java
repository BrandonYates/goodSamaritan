package com.syntacticsugar.goodsamaritan;

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
import java.util.Collection;

import cz.msebera.android.httpclient.Header;

/**
 * Created by brandonyates on 4/22/16.
 */
public class UserViewActivity extends AppCompatActivity {

    private static final int REQUEST_MAIN = 1;
    ListView listView;
    String userId;
    JSONArray deeds;
    String[] deedTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //set custom font
        TextView userViewPageTitle = (TextView) findViewById(R.id.menuPageTitle);
        TextView userViewText = (TextView) findViewById(R.id.userText);
        TextView userPointsText = (TextView) findViewById(R.id.userPointsText);
        TextView myDeedsText = (TextView) findViewById(R.id.myDeedsText);
        Typeface font = Typeface.createFromAsset(getAssets(), "sam_marker.ttf");
        userViewPageTitle.setTypeface(font);
        userViewText.setTypeface(font);
        userPointsText.setTypeface(font);
        //end set custom font


        userId = getIntent().getStringExtra("userId");
        System.out.println("user info is " + userId);

        //listview
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.myDeedList);

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

    public class backgroundOperation extends AsyncTask<String, Void, String> {

        private UserService userService = new UserService();

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            if (userService != null && userId != null) {
                try {
                    JSONObject rawUser = userService.findUserById(userId);

                    if (rawUser != null) {
                        System.out.println("rawUser " + rawUser.toString());

                        User user = new User(rawUser);
                        Collection<Deed> deeds = user.getDeeds();
                        String[] deedTitles = new String[deeds.size()];

                        for (int i = 0; i < deeds.size(); ++i) {
                            Deed deed = (Deed) deeds.toArray()[i];
                            deedTitles[i] = deed.getDescription();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (userService == null) {
                System.out.println("User Service null");
            } else if (userId == null) {
                System.out.println("UserInfo null");
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //update deed list

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, deedTitles);

            // Assign adapter to ListView
            listView.setAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
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

                            RestUtils.get("findDeedByRequestingUserId", params, new AsyncHttpResponseHandler() {
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

    public void gotoMain(View view) {
        System.out.println("gotoMenu Called!");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, REQUEST_MAIN);
    }
}
