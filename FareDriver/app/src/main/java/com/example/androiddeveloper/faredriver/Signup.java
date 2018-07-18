package com.example.androiddeveloper.faredriver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class Signup extends AppCompatActivity {

    String fname,lname,pass1,pass2,mail,address,phonenumber,carnumber;
    @Bind(R.id.fname)
    EditText _fnameText;
    @Bind(R.id.carnum) EditText _carnumber;
    @Bind(R.id.phone) EditText _phonenumber;
    @Bind(R.id.lname) EditText _lnameText;
    @Bind(R.id.email) EditText _emailText;
    @Bind(R.id.password) EditText _passText;
    @Bind(R.id.reEnterPassword) EditText _repassText;
    @Bind(R.id.address2) EditText _addressText;
    @Bind(R.id.cont)
    Button btncont;
    @Bind(R.id.link_login)
    TextView _loginLink;
    Context ctx;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ctx = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));
        }
        ButterKnife.bind(this);
        btncont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                if(isNetworkAvailable()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }else{
                    Toast.makeText(Signup.this, "Please Connect to internet to continue.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        btncont.setEnabled(false);
        progressDialog = new ProgressDialog(Signup.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        fname = _fnameText.getText().toString();
        lname = _lnameText.getText().toString();
        phonenumber= _phonenumber.getText().toString();
        carnumber= _carnumber.getText().toString();
        mail = _emailText.getText().toString();
        address = _addressText.getText().toString();
        pass1 = _passText.getText().toString();
        pass2 = _repassText.getText().toString();

        // TODO: Implement your own signup logic here.
        onSignupSuccess();
    }
    public void onSignupSuccess() {
        if(isNetworkAvailable()) {
            SignUp1();
        }else{
            progressDialog.dismiss();
            Toast.makeText(this, "You are not connected to Internet.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign Up failed", Toast.LENGTH_LONG).show();
        btncont.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;
        fname = _fnameText.getText().toString();
        lname = _lnameText.getText().toString();
        phonenumber= _phonenumber.getText().toString();
        carnumber= _carnumber.getText().toString();
        mail = _emailText.getText().toString();
        pass1 = _passText.getText().toString();
        pass2 = _repassText.getText().toString();
        address = _addressText.getText().toString();
        if (fname.isEmpty() || fname.length() < 3) {
            _fnameText.setError("at least 3 characters");
            valid = false;
        } else {
            _fnameText.setError(null);
        }

        if (phonenumber.isEmpty() || phonenumber.length() < 11) {
            _phonenumber.setError("at least 11 Digits");
            valid = false;
        } else {
            _phonenumber.setError(null);
        }
        if (carnumber.isEmpty() || carnumber.length() < 4) {
            _phonenumber.setError("at least 4 Digits");
            valid = false;
        } else {
            _carnumber.setError(null);
        }

        if (lname.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }
        if (mail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);

        }if(address.isEmpty()){
            _addressText.setError("Address cannot be empty");
            valid = false;
        }else{
            _addressText.setError(null);
        }
        if (pass1.isEmpty() || pass1.length() < 4 || pass1.length() > 10) {
            _passText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passText.setError(null);
        }
        if (pass2.isEmpty() || pass2.length() < 4 || pass2.length() > 10 || !(pass2.equals(pass1))) {
            _repassText.setError("Password Do not match");
            valid = false;
        } else {
            _repassText.setError(null);
        }
        return valid;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void SignUp1() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("firstname",fname);
        params.put("lastname",lname);
        params.put("email",mail);
        params.put("city",carnumber);
        params.put("mobile",phonenumber);
        params.put("address",address);
        params.put("password",pass1);
        client.get(ctx,
                "http://gochem.com.pk/apis/user_signup_action1.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        progressDialog.dismiss();
                        Toast.makeText(Signup.this, "Sorry something went wrong",
                                Toast.LENGTH_SHORT).show();
                        //Log.e("Failure", new String(responseBody));
                    }
                });
    }
    public void onBackPressed() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        moveTaskToBack(true);
    }
}
