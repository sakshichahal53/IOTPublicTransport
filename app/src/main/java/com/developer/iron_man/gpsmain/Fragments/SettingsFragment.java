package com.developer.iron_man.gpsmain.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;

/**
 * Created by sagar on 29/7/17.
 */

public class SettingsFragment extends Fragment {
    PrefManager prefManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings,container,false);
        prefManager = new PrefManager(getActivity());
        prefManager.setFragmentFlag(null);
        return view;

    }
}
