package com.example.androiddeveloper.faredriver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_SIGNUP = 0;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    Context c;
    ProgressDialog progressDialog;
    String email,password;
    @Bind(R.id.name) EditText _emailText;
    @Bind(R.id.editText) EditText _passwordText;
    @Bind(R.id.btnlogin) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        editor = prefs.edit();
        if (!prefs.getString("id", "").isEmpty()) {
            Intent intent = new Intent(this, Splash.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));
        }
        c = this;
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(getApplicationContext(), Signup.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Toast.makeText(MainActivity.this, "Please Connect to internet to continue.",
                            Toast.LENGTH_SHORT).show();
                    _loginButton.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        // TODO: Implement your own authentication logic here.
        onLoginSuccess();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                this.finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        moveTaskToBack(true);
    }
    public void onLoginSuccess() {
        if(isNetworkAvailable()) {
            Login();
        }else{
            progressDialog.dismiss();

            Toast.makeText(this, "You are not connected to Internet.", Toast.LENGTH_SHORT).show();
        }
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
        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            _passwordText.setError("between 3 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void Login() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email",email);
        params.put("pass",password);


        client.get(c,
                "http://gochem.com.pk/apis/login_action.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        //Log.e("successRES", new String(responseBody));
                        try {


                            JSONObject obj1 = new JSONObject(new String(responseBody));
                            Boolean bool = obj1.getBoolean("boolean");
                            //*if (boolmn.equals("true")) {*//*
                            if(bool) {
                                log.e("message", String.valueOf(bool));

                                String user_id = obj1.getString("id");
                              /*REPONSE_JSON_OBJECT.getJSONObject("user").getJSONObject("_id");*/
                                Log.e("message******", user_id);

                                editor.putString("id", user_id);


                              /*user_id=com.example.androiddeveloper.faredriver.Handler.uid;*/
                                editor.commit();
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }else {
                                Toast.makeText(MainActivity.this, "No User Found!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        progressDialog.dismiss();
                    }
                });

    }
}
