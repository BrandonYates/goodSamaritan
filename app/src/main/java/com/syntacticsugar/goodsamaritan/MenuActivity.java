package com.syntacticsugar.goodsamaritan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brandonyates on 4/21/16.
 */
public class MenuActivity extends AppCompatActivity {

    private static final int REQUEST_MAIN = 1;
    ListView listView;
    String userInfo;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        userInfo = getIntent().getStringExtra("userId");
        System.out.println("user info is " + userInfo);

        //listview
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.myDeedList);

        try {
            JSONObject user = userService.findUserById(userInfo);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

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
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        // end of listview code
    }

    public void gotoMain(View view) {
        System.out.println("gotoMenu Called!");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", userInfo);
        startActivityForResult(intent, REQUEST_MAIN);
    }

    public void logout(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_MAIN);
    }
}
