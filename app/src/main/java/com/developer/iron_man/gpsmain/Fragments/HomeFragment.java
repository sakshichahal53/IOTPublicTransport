package com.developer.iron_man.gpsmain.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagar on 29/7/17.
 */

public class HomeFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView tabOne;
    TextView tabTwo;
    TextView tabFour;
    ViewPagerAdapter adapter;
    PrefManager pref;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_main_layout,container,false);
        pref=new PrefManager(getActivity());
        viewPager = (ViewPager)view.findViewById(R.id.view_pager);
        createViewPager(viewPager);
        tabLayout = (TabLayout)view.findViewById(R.id.tab_host);
        tabLayout.setupWithViewPager(viewPager);
        setUpText();

        return view;
    }

    public void setUpText() {

        //setting up the custom text for the tabs
        tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Map");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_location_on_grey_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Scan");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_phone_android_green_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabFour = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabFour.setText("Profile");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person_grey_24dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabFour);
    }

    private void createViewPager(ViewPager viewPager) {

        //filling the adapter with fragments
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new MapFragment(), "Map");
        adapter.addFrag(new QRScanFragment(), "QR Scanner");
        adapter.addFrag(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
    }

    public void callMapFragment(){
        MapFragment fragment=(MapFragment) adapter.getItem(0);
        fragment.drawAllMarker();
    }
}
