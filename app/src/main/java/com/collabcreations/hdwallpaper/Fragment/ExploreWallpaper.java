package com.collabcreations.hdwallpaper.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.collabcreations.hdwallpaper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreWallpaper extends Fragment {

    public ExploreWallpaper() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_wallpaper, container, false);
    }
}
