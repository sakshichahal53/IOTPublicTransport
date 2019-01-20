package com.developer.iron_man.gpsmain.Fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.developer.iron_man.gpsmain.Activities.MainActivity;
import com.developer.iron_man.gpsmain.Others.GPSTracker;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import models.ListVehicleLocations;
import models.VehicleLocationModel;

/**
 * Created by sagar on 27/7/17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {


    private View view;
    private GoogleMap mMap;
    PrefManager pref;
    private static final String TAG = MainActivity.class.getSimpleName();
    ListVehicleLocations listVehicleLocations;
    List<VehicleLocationModel> lcoordinates;
    Marker marker;
    int flag=1;
    List<Marker> markerList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_map, container, false);
        pref=new PrefManager(getActivity());
        pref.setFragmentFlag("1");
        markerList=new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Gson gson = new Gson();
        String m = pref.getMarkers();
        if(m!=null)
        {
            listVehicleLocations = gson.fromJson(m, ListVehicleLocations.class);
        }
        lcoordinates=listVehicleLocations.getLocation();
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.28.650701, 77.233410
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mMap.setMyLocationEnabled(true);
        mMap.clear();
        drawAllMarker();
        GPSTracker gpsTracker=new GPSTracker(getActivity());
        LatLng user_loc = new LatLng(gpsTracker.getLocation().getLatitude(), gpsTracker.getLocation().getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(user_loc));
        mMap.setMinZoomPreference(15.0f);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void drawAllMarker(){
        Log.e("Working","Working");
        Gson gson = new Gson();
        String m = pref.getMarkers();
        if(m!=null)
        {
            listVehicleLocations = gson.fromJson(m, ListVehicleLocations.class);
        }
        lcoordinates=listVehicleLocations.getLocation();
        if(mMap!=null) {
            mMap.clear();
            markerList.clear();
            for (int i = 0; i < lcoordinates.size(); i++) {
                LatLng loc = new LatLng(Double.parseDouble(lcoordinates.get(i).getLatitude()), Double.parseDouble(lcoordinates.get(i).getLongitude()));
                marker = mMap.addMarker(new MarkerOptions().position(loc).icon(bitmapDescriptorFromVector(getActivity(), R.drawable.selector_auto_image)));
            }
        }
    }

    private void animateMarkerNew(final LatLng destination, final Marker marker) {

        if (marker != null) {


            final LatLng startPosition = marker.getPosition();
            Log.e("Marker",startPosition+"");
            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                       // marker.setIcon(bitmapDescriptorFromVector(getActivity(),R.drawable.selector_auto_image));
                        marker.setRotation(getBearing(startPosition, new LatLng(destination.latitude, destination.longitude)));
                        //marker.setVisible(true);
                        //marker.setZIndex(5);
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(newPosition)
                                .zoom(15.5f)
                                .build()));

                    } catch (Exception ex) {

                        Log.e("",ex.getMessage());

                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

//                     if (marker != null) {
//                     marker.remove();
//                     }
//                     marker = mMap.addMarker(new MarkerOptions().position(endPosition).icon(bitmapDescriptorFromVector(getActivity(),R.drawable.selector_auto_image)));

                }
            });
            valueAnimator.start();
        }
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }


    //Method for finding bearing between two points
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}
