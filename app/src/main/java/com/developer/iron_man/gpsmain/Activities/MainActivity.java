package com.developer.iron_man.gpsmain.Activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.iron_man.gpsmain.Fragments.HistoryFragment;
import com.developer.iron_man.gpsmain.Fragments.SettingsFragment;
import com.developer.iron_man.gpsmain.Fragments.HomeFragment;
import com.developer.iron_man.gpsmain.Others.CircleTrasform;
import com.developer.iron_man.gpsmain.Others.GPSTracker;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import models.ListVehicleLocations;
import models.LocationModel;
import models.UserModel;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagar on 27/7/17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite,logout;
    private Toolbar toolbar;


    // urls to load navigation header background image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    PrefManager pref;
    UserModel obj;
    APIServices mApiService;
    ProgressDialog dialog;
    public static TextView t,sp;
    GPSTracker gpsTracker;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiService= APIUtil.getAPIService();
        gpsTracker=new GPSTracker(getApplicationContext());
        pref=new PrefManager(getApplicationContext());
        dialog=new ProgressDialog(MainActivity.this);
        Bundle bundle = getIntent().getExtras();
        dialog = ProgressDialog.show(MainActivity.this,null,"Loading...", true);

        if(pref.getUsername()==null)
        {
            getUserInfo(bundle.getString("username"),savedInstanceState);
        }

        else
        {
            getUserInfo(pref.getUsername(),savedInstanceState);

        }



    }

    public void init(Bundle savedInstanceState){


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        logout=(TextView)toolbar.findViewById(R.id.logout);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSMS();
            }
        });

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        Gson gson = new Gson();
        String u = pref.getUser();

        if(u!=null)
        {
            obj = gson.fromJson(u, UserModel.class);
            logout.setVisibility(View.VISIBLE);
            pref.setUsername(obj.getUsername());
            pref.setUserId(obj.getUserId());

            sendLocation(new LocationModel(gpsTracker.getLocation().getLatitude()+"",gpsTracker.getLocation().getLongitude()+"",0.0+"","user",pref.getUserId()));
        }

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        final Handler UI_HANDLER = new Handler();

        Runnable UI_UPDATE_RUNNABLE = new Runnable() {
            @Override
            public void run() {

                getLatestCoordinates(obj.getUsername());
                UI_HANDLER.postDelayed(this, 1500);
            }
        };

        UI_HANDLER.postDelayed(UI_UPDATE_RUNNABLE, 1500);

        logout.setOnClickListener(this);
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Hi");
        txtWebsite.setText(obj.getName());

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getImage(obj.getPhoto()).compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(this)
                .load(stream.toByteArray())
                .asBitmap()
                .thumbnail(0.5f)
                .transform(new CircleTrasform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // show or hide the fab button
            toggleFab();
            return;

        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                //history
                HistoryFragment historyFragment = new HistoryFragment();
                return historyFragment;
            case 3:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        if(navItemIndex!=2)
            getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

                    case R.id.nav_history:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_HISTORY;
                        break;

                    case R.id.nav_rateUs:
                        navItemIndex = 2;
                        //code for playStore

                        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backStack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                        }

                        break;

                    case R.id.nav_settings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

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

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.logout:

                pref.setUser(null);
                pref.setUserId(null);
                pref.setUsername(null);
                pref.setEmegencyContact(null);
                logout.setVisibility(View.GONE);
                pref.setFragmentFlag(null);
                startActivity(new Intent(this,SignUpActivity.class));
                finish();
        }
    }

    public Bitmap getImage(String encodedimage){
        byte[] decodedString = Base64.decode(encodedimage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    void getUserInfo(final String username, final Bundle bundle){

        mApiService.getUserDetails(username).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {



                if(response.isSuccessful()) {

                    Gson g=new Gson();
                    String user=g.toJson(response.body());
                    pref.setUser(user);
                    getCoordinates(username,bundle);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });


    }

    public void getCoordinates(String username, final Bundle saveBundle){

        mApiService.getNearByCoordinates(username).enqueue(new Callback<ListVehicleLocations>() {
            @Override
            public void onResponse(Call<ListVehicleLocations> call, Response<ListVehicleLocations> response) {

                if(response.isSuccessful()) {

                    Gson g=new Gson();
                    String locations=g.toJson(response.body());
                    pref.setMarkers(locations);
                    dialog.dismiss();
                    init(saveBundle);
                }
            }

            @Override
            public void onFailure(Call<ListVehicleLocations> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Nearby vehicles coordinates fetch failed", Toast.LENGTH_LONG).show();

            }
        });


    }

    void getLatestCoordinates(String username){

        mApiService.getNearByCoordinates(username).enqueue(new Callback<ListVehicleLocations>() {
            @Override
            public void onResponse(Call<ListVehicleLocations> call, Response<ListVehicleLocations> response) {

                if(response.isSuccessful()) {

                    Gson g=new Gson();
                    String locations=g.toJson(response.body());
                    pref.setMarkers(locations);
                    if(pref.getFragmentFlag()!=null){
                        HomeFragment fragment=(HomeFragment)getSupportFragmentManager().findFragmentByTag(TAG_HOME);
                        if(fragment!=null)
                            fragment.callMapFragment();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListVehicleLocations> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Nearby vehicles coordinates failed", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pref.setNotificationFlag(null);
    }

    public void sendLocation(LocationModel locationModel){
        Gson g = new Gson();

        mApiService.savePost(locationModel).enqueue(new Callback<LocationModel>() {
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

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    public void sendSMS() {
        try {
            String num[]=pref.getEmegencyContact().split(";");
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

