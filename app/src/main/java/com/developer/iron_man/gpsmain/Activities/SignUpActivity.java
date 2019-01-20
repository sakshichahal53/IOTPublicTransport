package com.developer.iron_man.gpsmain.Activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.CompleteProfileActivity;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.developer.iron_man.gpsmain.Services.LocationService;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import models.User;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Iron_Man on 25/06/17.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email, password,confirm_password;
    public TextView sign_in;
    public TextView sign_up;
    public TextView sign_in_text;
    public TextView sign_up_text;
    LinearLayout layout1,layout2;
    Button submit,signInButton;
    User user;
    String userEmail;
    APIServices mAPIService;
    PrefManager prefManager;
    ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        submit = (Button) findViewById(R.id.submit);
        sign_in=(TextView)findViewById(R.id.sign_in);
        sign_up=(TextView)findViewById(R.id.sign_up);
        sign_in_text=(TextView)findViewById(R.id.sign_in_text);
        sign_up_text=(TextView)findViewById(R.id.sign_up_text);
        layout1=(LinearLayout)findViewById(R.id.layout1);
        layout2=(LinearLayout)findViewById(R.id.layout2);
        signInButton=(Button)findViewById(R.id.sign_in_button);
        mAPIService= APIUtil.getAPIService();
        prefManager=new PrefManager(getApplicationContext());
        dialog=new ProgressDialog(SignUpActivity.this);
        prefManager.setUser(null);
        // Creating a User with information provided by the user
        user = new User();

        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirm_password.getText().toString())){
                    userEmail = email.getText().toString();
                    user.setEmail(userEmail);
                    user.setPassword(password.getText().toString());
                    user.setUsername(userEmail.split("@")[0]);

                    Intent i = new Intent(SignUpActivity.this, CompleteProfileActivity.class);
                    i.putExtra("email",userEmail);
                    i.putExtra("password",user.getPassword());
                    i.putExtra("username",user.getUsername());
                    startActivity(i);
                }
                else {

                    Toast.makeText(getApplicationContext(),"Passwords not matching",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.sign_up:

                sign_up_text.setVisibility(View.VISIBLE);
                sign_in_text.setVisibility(View.GONE);
                confirm_password.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);



                break;
            case R.id.sign_in:

                //to turn it into sign in layout

                sign_up_text.setVisibility(View.GONE);
                sign_in_text.setVisibility(View.VISIBLE);
                confirm_password.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);

                break;

            case R.id.sign_in_button:

                JsonObject j=new JsonObject();
                j.addProperty("username",email.getText().toString().split("@")[0]);
                j.addProperty("password",password.getText().toString());
                getLoginToken(j);
                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                i.putExtra("username",email.getText().toString().split("@")[0]);
                startActivity(i);
                finish();

        }
    }

    void getLoginToken(JsonObject loginModel){

        mAPIService.getToken(loginModel).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if(response.isSuccessful()) {

                    String s=response.body().toString();
                    try {

                        JSONObject obj=new JSONObject(s);
                        Log.e("Token: ",obj.getString("token"));
                        String token=obj.getString("token");
                        prefManager.setToken(token);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Failed : ", "Unable to submit post to API.");
            }
        });

    }

}


