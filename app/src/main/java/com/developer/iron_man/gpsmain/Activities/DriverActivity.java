package com.developer.iron_man.gpsmain.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.iron_man.gpsmain.Others.CircleTrasform;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import models.Driver;

/**
 * Created by sagar on 19/9/17.
 */

public class DriverActivity extends AppCompatActivity {

    PrefManager prefManager;
    Driver obj;
    Toolbar toolbar;
    ImageView photo;
    TextView name,contact,address,licence_no,aadhar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_details);
        prefManager=new PrefManager(getApplicationContext());
        toolbar=(Toolbar)findViewById(R.id.toolbar_driver);
        toolbar.setTitle("Driver Details");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefManager.setDriver(null);
                finish();
            }
        });
        name=(TextView)findViewById(R.id.name);
        contact=(TextView)findViewById(R.id.contact);
        address=(TextView)findViewById(R.id.address);
        licence_no=(TextView)findViewById(R.id.license);
        aadhar=(TextView)findViewById(R.id.aadhar);
        photo=(ImageView)findViewById(R.id.photo);

        Gson gson=new Gson();
        String d=prefManager.getDriver();
        obj = gson.fromJson(d, Driver.class);

        name.setText(""+obj.getDname());
        contact.setText("Contact: "+obj.getDcontact());
        address.setText(""+obj.getDaddress());
        aadhar.setText("Aadhar Number : "+obj.getDaadhar());
        licence_no.setText("Driving License No : "+obj.getDlicense());

        // Loading profile image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getImage(obj.getDphoto()).compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(DriverActivity.this)
                .load(stream.toByteArray())
                .asBitmap()
                .thumbnail(0.5f)
                .transform(new CircleTrasform(DriverActivity.this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo);
    }

    public Bitmap getImage(String encodedimage){
        byte[] decodedString = Base64.decode(encodedimage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
