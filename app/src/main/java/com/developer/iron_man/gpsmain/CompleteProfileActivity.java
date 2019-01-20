package com.developer.iron_man.gpsmain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.Activities.SignUpActivity;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import models.User;

import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Iron_Man on 25/06/17.
 */

public class CompleteProfileActivity extends AppCompatActivity {

    EditText firstname, lastName, address, contact, aadhar;
    TextView photo;
    Button save;
    private Bitmap bitmap;
    ImageView image;
    User u;
    APIServices mAPIService;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        mAPIService= APIUtil.getAPIService();
        firstname = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        address = (EditText) findViewById(R.id.address);
        contact = (EditText) findViewById(R.id.contact);
        aadhar = (EditText) findViewById(R.id.aadhar);
        photo = (TextView) findViewById(R.id.imageViewPhoto);
        image=(ImageView)findViewById(R.id.image);
        save = (Button) findViewById(R.id.register);
        dialog=new ProgressDialog(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        u = new User();
        u.setUsername(bundle.getString("username"));
        u.setEmail(bundle.getString("email"));
        u.setPassword(bundle.getString("password"));

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u.setFirstName(firstname.getText().toString());
                u.setLastName(lastName.getText().toString());
                u.setAddress(address.getText().toString());
                u.setContact(contact.getText().toString());
                u.setAadhar(Long.parseLong(aadhar.getText().toString()));
                u.setPhoto(getStringImage(bitmap));

                // Send user details
                Gson g = new Gson();
                //Log.e("Json data : ", g.toJson(u));
                dialog = ProgressDialog.show(CompleteProfileActivity.this,null,"Signing up...", true);
                createUser(u);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photo.setText(getPath(filePath)+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    void createUser(User user){

        mAPIService.createNewUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()) {

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
                    finish();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_LONG).show();
            }
        });
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap getImage(String encodedimage){
        byte[] decodedString = Base64.decode(encodedimage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }



}