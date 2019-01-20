package com.developer.iron_man.gpsmain.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.developer.iron_man.gpsmain.Others.GPSTracker;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.developer.iron_man.gpsmain.Services.LocationService;
import com.google.gson.Gson;

import models.LocationModel;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagar on 7/9/17.
 */

public class SplashActivity extends AppCompatActivity {

    LocationManager locationManager;
    public static long startTime;
    public static TextView text;
    PrefManager prefManager;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private APIServices mAPIService;
    GPSTracker gpsTracker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        mAPIService= APIUtil.getAPIService();
        gpsTracker=new GPSTracker(getApplicationContext());
        startTime = System.currentTimeMillis();
        prefManager=new PrefManager(getApplicationContext());
        prefManager.setNotificationFlag(null);
        startLocationService();
    }

    //This method leads you to the alert dialog box.
    void checkGps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            showGPSDisabledAlertToUser();
        }
    }

    void startLocationService(){

        //The method below checks if Location is enabled on device or not. If not, then an alert dialog box appears with option
        //to enable gps.
        checkGps();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            return;
        }

        //Here, the Location Service gets bound and the GPS Speedometer gets Active.
       startService(new Intent(this,LocationService.class));


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(prefManager.getUsername()==null)
                    startActivity(new Intent(SplashActivity.this,SignUpActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    //This method configures the Alert Dialog box.
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void sendLocation(LocationModel locationModel){
        Gson g = new Gson();
        Log.e("In sendLocation : ", g.toJson(locationModel));
        mAPIService.savePost(locationModel).enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                Log.e("In response : ", response.toString());
                if(response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Log.e("SendLocation : ", "Unable to submit post to API.");
            }
        });
    }
}
