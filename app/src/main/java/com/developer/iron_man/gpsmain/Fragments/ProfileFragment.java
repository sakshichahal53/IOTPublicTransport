package com.developer.iron_man.gpsmain.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.iron_man.gpsmain.Others.CircleTrasform;
import com.developer.iron_man.gpsmain.Others.MyTextView;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import models.ContactCreateModel;
import models.UpdateContactsModel;
import models.UserModel;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagar on 27/7/17.
 */

public class ProfileFragment extends Fragment {

    private View view;
    PrefManager pref;
    UserModel obj;
    ImageView profileImage;
    EditText number1,number2,number3;
    Button saveNumbers,addNumbers;
    LinearLayout profileDetail,addNumberLayout;
    APIServices apiServices;
    ContactCreateModel contactCreateModel;
    EditText address,phone,aadhar,email;
    TextView name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.profile_layout,container,false);
        pref=new PrefManager(getActivity());
        apiServices = APIUtil.getAPIService();
        pref.setFragmentFlag(null);

        name=(TextView)view.findViewById(R.id.profile_name);
        address=(EditText)view.findViewById(R.id.profile_address);
        email=(EditText)view.findViewById(R.id.email);
        phone=(EditText)view.findViewById(R.id.profile_contact);
        aadhar=(EditText)view.findViewById(R.id.profile_aadhar);
        profileImage=(ImageView)view.findViewById(R.id.profile_image);


        number1 = (EditText)view.findViewById(R.id.contact1);
        number2 = (EditText)view.findViewById(R.id.contact2);
        number3 = (EditText)view.findViewById(R.id.contact3);

        if(pref.getEmegencyContact()!=null){
            String[] s = pref.getEmegencyContact().split(";");
            number1.setText(s[0]);
            number2.setText(s[1]);
            number3.setText(s[2]);
        }

        saveNumbers = (Button) view.findViewById(R.id.save_numbers);
        addNumbers = (Button) view.findViewById(R.id.add_phones);
        profileDetail = (LinearLayout)view.findViewById(R.id.profile_layout);
        addNumberLayout = (LinearLayout)view.findViewById(R.id.number_layout);
        contactCreateModel = new ContactCreateModel();
        contactCreateModel.setUsername(pref.getUsername());




        //Getting user details
        Gson gson = new Gson();
        String user = pref.getUser();
        obj = gson.fromJson(user, UserModel.class);

        name.setText(obj.getName());
        address.setText(obj.getAddress());
        email.setText(obj.getEmail());
        aadhar.setText(obj.getAadhar());
        phone.setText(obj.getContact());
        loadProfileImage();

        addNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNumberLayout.setVisibility(View.VISIBLE);
                profileDetail.setVisibility(View.GONE);
                addNumbers.setVisibility(View.GONE);
                saveNumbers.setVisibility(View.VISIBLE);

            }
        });

        saveNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nums = "";
                nums += number1.getText().toString()+";";
                nums += number2.getText().toString()+";";
                nums += number3.getText().toString();
                contactCreateModel.setEmergencyContact(nums);
                Log.e("Numbers to save : ",nums);
                if(pref.getEmegencyContact()==null){
                    sendEmergencyNumbers(contactCreateModel);
                }else{
                    UpdateContactsModel updateContactsModel = new UpdateContactsModel();
                    updateContactsModel.setEmergencyContact(nums);
                    updateCurrentContacts(updateContactsModel);
                }

                addNumberLayout.setVisibility(View.GONE);
                profileDetail.setVisibility(View.VISIBLE);
                addNumbers.setVisibility(View.VISIBLE);
                saveNumbers.setVisibility(View.GONE);
            }
        });

        return view;
    }

    public Bitmap getImage(String encodedimage){
        byte[] decodedString = Base64.decode(encodedimage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    void loadProfileImage(){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getImage(obj.getPhoto().trim()).compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(this)
                .load(stream.toByteArray())
                .asBitmap()
                .thumbnail(0.5f)
                .transform(new CircleTrasform(getActivity()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);

    }

    void sendEmergencyNumbers(final ContactCreateModel contactCreateModel){
        apiServices.contactCreate(contactCreateModel).enqueue(new Callback<ContactCreateModel>() {
            @Override
            public void onResponse(Call<ContactCreateModel> call, Response<ContactCreateModel> response) {
                pref.setEmegencyContact(contactCreateModel.getEmergencyContact());
                Log.e("Emergency c response : ",response.body().toString());
            }

            @Override
            public void onFailure(Call<ContactCreateModel> call, Throwable t) {
                Log.e("Failed : ",t.getMessage());
            }
        });
    }

    void updateCurrentContacts(final UpdateContactsModel updateContactsModel){
        apiServices.contactUpdate(pref.getUsername(),updateContactsModel).enqueue(new Callback<UpdateContactsModel>() {
            @Override
            public void onResponse(Call<UpdateContactsModel> call, Response<UpdateContactsModel> response) {
                pref.setEmegencyContact(updateContactsModel.getEmergencyContact());
                Log.e("Emergency u response : ",response.body().toString());
            }

            @Override
            public void onFailure(Call<UpdateContactsModel> call, Throwable t) {
                Log.e("Failed : ",t.getMessage());
            }
        });
    }
}
