package com.developer.iron_man.gpsmain.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.Activities.DriverActivity;
import com.developer.iron_man.gpsmain.Activities.MainActivity;
import com.developer.iron_man.gpsmain.Others.HistoryRecycleGrid;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import models.Driver;
import models.History;
import models.Journey;
import models.ListVehicleLocations;
import models.QRModel;
import models.Transport;
import models.UserJourneyList;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagar on 27/7/17.
 */

public class HistoryFragment extends Fragment {

    private View view;
    private RecyclerView.LayoutManager layoutManager;
    HistoryRecycleGrid adapter;
    RecyclerView recyclerView;
    List<History> historyList=new ArrayList<>();
    APIServices mApiService;
    PrefManager prefManager;
    List<List<Transport>> listOfTransportList;
    List<Transport> transportList;
    List<Journey> journeyList;
    int gps_id;
    String dest,source,da,ti;
    String lic_plate;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.history_layout,container,false);

        dialog=new ProgressDialog(getActivity());
        recyclerView=(RecyclerView)view.findViewById(R.id.history_recycle_grid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mApiService= APIUtil.getAPIService();
        prefManager=new PrefManager(getActivity());
        prefManager.setFragmentFlag(null);
        historyList.clear();
        dialog = ProgressDialog.show(getActivity(),null,"Loading...", true);
        getCards(prefManager.getUsername());

        return view;
    }

    public void getCards(String username){




        mApiService.getUserHistory(username).enqueue(new Callback<UserJourneyList>() {
            @Override
            public void onResponse(Call<UserJourneyList> call, Response<UserJourneyList> response) {

                dialog.dismiss();
                String slash[]= new String[3];
                String cord[] = new String[2];
                String time[] =  new String[2];
                String date[] = new String[3];
                if(response.isSuccessful()) {

                    listOfTransportList=response.body().getTransport();
                    journeyList=response.body().getJourney();

                    if(journeyList.size()>0&&listOfTransportList.size()>0)

                    {
                        for(int i=0;i<journeyList.size();i++)
                        {
                            Log.e(" journey get i ",journeyList.get(i).getStart());
                            slash = (journeyList.get(i).getStart()).split("-");
                            Log.e("slash ",slash[0]+"  "+slash[1]+ " "+slash[2]);
                            transportList = listOfTransportList.get(i);
                            cord = slash[0].split(":");

                            Log.e("cord",cord[0]+"  "+cord[1]);
                            source=getAddress(Double.parseDouble(cord[0]),Double.parseDouble(cord[1]));
                            time = slash[1].split(",");
                            date = slash[2].split(",");
                            ti = time[0]+":"+time[1];
                            da = date[2]+"/"+date[1]+"/"+date[0]+" ";
                            slash = (journeyList.get(i).getEnd()).split("-");
                            cord = slash[0].split(":");
                            dest=getAddress(Double.parseDouble(cord[0]),Double.parseDouble(cord[1]));
                            for(int j=0;j<transportList.size();j++)
                            {
                                Driver driver=transportList.get(j).getDriver();
                                String image=driver.getDphoto();
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                getImage(image).compress(Bitmap.CompressFormat.PNG, 100, stream);
                                historyList.add(new History(da,ti,"Auto",lic_plate,source,dest,stream,driver));
                            }
                        }
                        adapter=new HistoryRecycleGrid(getActivity(), historyList, new HistoryRecycleGrid.VenueAdapterClickCallbacks() {
                            @Override
                            public void onCardClick(Driver driver) {

                                Intent intent=new Intent(getActivity(), DriverActivity.class);
                                Gson g=new Gson();
                                String d=g.toJson(driver);
                                prefManager.setDriver(d);
                                startActivity(intent);

                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserJourneyList> call, Throwable t) {

                dialog.dismiss();
                Toast.makeText(getActivity(), "History failed", Toast.LENGTH_LONG).show();

            }
        });

    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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

    public Bitmap getImage(String encodedimage){
        byte[] decodedString = Base64.decode(encodedimage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
