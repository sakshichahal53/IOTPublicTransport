package com.developer.iron_man.gpsmain.Others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.iron_man.gpsmain.R;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.ByteArrayOutputStream;
import java.util.List;

import models.Driver;
import models.History;

/**
 * Created by sagar on 2/8/17.
 */

public class HistoryRecycleGrid extends RecyclerView.Adapter<HistoryRecycleGrid.MyHolder>{

    public RecyclerView re;
    private List<History> dataSet ;
    public Context context=null;
    VenueAdapterClickCallbacks venueAdapterClickCallbacks;


    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView weekday;
        TextView date;
        TextView time;
        TextView vehicle;
        TextView veh_num;
        TextView source;
        TextView dest;
        ImageView image;

        public MyHolder(View itemView)
        {
            super(itemView);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.time=(TextView) itemView.findViewById(R.id.time);
            this.vehicle=(TextView)itemView.findViewById(R.id.vehicle_type);
            this.veh_num=(TextView)itemView.findViewById(R.id.vehicle_num);
            this.source=(TextView)itemView.findViewById(R.id.source);
            this.dest=(TextView)itemView.findViewById(R.id.dest);
            this.image=(ImageView)itemView.findViewById(R.id.driver_image);
        }
    }

    public HistoryRecycleGrid(Context c, List<History> data,VenueAdapterClickCallbacks venueAdapterClickCallback)
    {

        this.dataSet = data;
        this.venueAdapterClickCallbacks = venueAdapterClickCallback;
        context=c;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_card_layout, parent, false);
        MyHolder myHolder=new MyHolder(view);
        re = (RecyclerView) parent.findViewById(R.id.history_recycle_grid);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        TextView date=holder.date;
        TextView time=holder.time;
        TextView vehicle=holder.vehicle;
        TextView veh_num=holder.veh_num;
        TextView source=holder.source;
        TextView dest=holder.dest;
        ImageView driver_image=holder.image;

        //weekday.setText(dataSet.get(position).getWeekday());
        date.setText(dataSet.get(position).getDate());
        time.setText(dataSet.get(position).getTime());
        vehicle.setText(dataSet.get(position).getVehicle_type());
        veh_num.setText(dataSet.get(position).getVehicle_num());
        source.setText(dataSet.get(position).getSource());
        dest.setText(dataSet.get(position).getDest());

        Glide.with(context)
                .load(dataSet.get(position).getImage().toByteArray())
                .asBitmap()
                .thumbnail(0.5f)
                .transform(new CircleTrasform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(driver_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                venueAdapterClickCallbacks.onCardClick(dataSet.get(position).getDriver());

            }
        });



        }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface VenueAdapterClickCallbacks {
        void onCardClick(Driver driver);}



}

