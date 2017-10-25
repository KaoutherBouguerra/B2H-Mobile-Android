package com.example.macbook.box2homeclient.fragments.nav_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macbook.box2homeclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartagerFragment extends Fragment {


    public PartagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partager, container, false);
    }

}
