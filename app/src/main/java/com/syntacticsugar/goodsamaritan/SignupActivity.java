package com.syntacticsugar.goodsamaritan;

/**
 * Created by brandonyates on 4/4/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_first_name) EditText _firstNameText;
    @Bind(R.id.input_last_name) EditText _lastNameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password1) EditText _passwordText1;
    @Bind(R.id.input_password2) EditText _passwordText2;
    //@Bind(R.id.btn_signup) Button _signupButton;
    Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        userService = new UserService();

        _signupButton = (Button) findViewById(R.id.btn_signup);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String firstName = _firstNameText.getText().toString();
        final String lastName = _lastNameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password1 = _passwordText1.getText().toString();

        // TODO: Implement signup logic here.
        boolean b = new Handler().post(
                new Runnable() {
                    public void run() {
                        try {
                            RequestParams params = new RequestParams();
                            params.add("firstName", firstName);
                            params.add("lastName", lastName);
                            params.add("emailAddress", email);
                            params.add("password", password1);

                            RestUtils.post("createUser/params", params, new AsyncHttpResponseHandler() {

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
                                            onSignupSuccess();
                                        } else {
                                            onSignupFailed();
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
                            onSignupFailed();
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                });
//        progressDialog.dismiss();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "signup succeeded", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String password1 = _passwordText1.getText().toString();
        String password2 = _passwordText2.getText().toString();

        if(firstName.isEmpty() || firstName.length() < 2 || firstName.length() > 50) {
            _firstNameText.setError("enter a valid name");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        if(lastName.isEmpty() || lastName.length() < 2 || lastName.length() > 50) {
            _lastNameText.setError("enter a valid name");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password1.isEmpty() || password1.length() < 4 || password1.length() > 15) {
            _passwordText1.setError("between 4 and 15 alphanumeric characters");
            valid = false;
        } else {
            _passwordText1.setError(null);
        }

        if (password1.equals(password2)) {
            _passwordText2.setError(null);
        } else {
            _passwordText2.setError("passwords do not match");
            Log.d(TAG, "p1: " + password1 + " p2: " + password2);
            valid = false;
        }

        return valid;
    }
}