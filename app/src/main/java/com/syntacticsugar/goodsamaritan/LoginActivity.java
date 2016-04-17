package com.syntacticsugar.goodsamaritan;

/**
 * Created by brandonyates on 4/4/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_MAIN = 1;
    private UserService userService;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userService = new UserService();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        userService.hello();
        Toast.makeText(getBaseContext(), "Login attempted", Toast.LENGTH_LONG).show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
//                         On complete call either onLoginSuccess or onLoginFailed

                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.add("emailAddress", email);
                        params.add("password", password);
                        try {

                            RestUtils.post("authenticate", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    System.out.println("##############");
                                    System.out.println("onSuccess");
                                    System.out.println(responseBody);
                                    try {
                                        if (responseBody == null) {
                                            System.out.println("# responseBody Null");
                                            throw new Exception();
                                        }
                                        String response = new String(responseBody, "UTF-8");
                                        // JSON Object
                                        JSONObject obj = new JSONObject(response);

                                        System.out.println(obj.toString());

                                        String userId = obj.getString("id");
                                        System.out.println("userId is " + userId);
                                        onLoginSuccess(userId);
                                        System.out.println("##############");

                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        System.out.println("EXCEPTION!");
                                        System.out.println("EXCEPTION!");
                                        System.out.println("EXCEPTION!");
                                        System.out.println("EXCEPTION!");
                                        e.printStackTrace();
                                        onLoginFailed();
                                    }
//                                onLoginSuccess();
//                                System.out.println("##############");
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
//                            userService.signIn(email, password);
//                            onLoginSuccess();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            onLoginFailed();
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                }, 3000);
        progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String userId) {
        _loginButton.setEnabled(true);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, REQUEST_MAIN);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
