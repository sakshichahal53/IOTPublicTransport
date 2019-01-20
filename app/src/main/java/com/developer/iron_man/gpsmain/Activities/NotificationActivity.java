package com.developer.iron_man.gpsmain.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.Others.GPSTracker;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by sagar on 12/9/17.
 */

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    Button sendLocation,notSendLocation;
    PrefManager prefManager;
    public static int p=0;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_notification_layout);
        sendLocation=(Button)findViewById(R.id.send_location);
        gpsTracker=new GPSTracker(getApplicationContext());
        notSendLocation=(Button)findViewById(R.id.not_send_location);
        prefManager=new PrefManager(getApplicationContext());
        sendLocation.setOnClickListener(this);
        notSendLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.send_location:
                p=1;
                sendSMS();
                startActivity(new Intent(NotificationActivity.this,MainActivity.class));
                finish();
            case R.id.not_send_location:
                prefManager.setLocationF(null);
                finish();
        }
    }

    public void sendSMS() {
        try {
            String num[]=prefManager.getEmegencyContact().split(";");
            String location=getAddress(gpsTracker.getLocation().getLatitude(),gpsTracker.getLocation().getLongitude());
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+91"+num[0], null, "Hii,I am at "+location, null, null);
            smsManager.sendTextMessage("+91"+num[1], null, "Hii,I am at "+location, null, null);
            smsManager.sendTextMessage("+91"+num[2], null, "Hii,I am at "+location, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String add="";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = " ";
            add = add + obj.getAddressLine(0);
            //add = add + "\n" + obj.getCountryName();
            // add = add + "\n" + obj.getCountryCode();

            // add = add + "\n" + obj.getPostalCode();
            // add = add + "\n" + obj.getSubAdminArea();
            add = add + ", " + obj.getLocality();
            add = add + ", " + obj.getAdminArea();
            // add = add + "\n" + obj.getSubThoroughfare();



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        return add;
    }
}
