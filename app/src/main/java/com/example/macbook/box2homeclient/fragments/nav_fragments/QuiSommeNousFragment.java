package com.example.macbook.box2homeclient.fragments.nav_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.macbook.box2homeclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuiSommeNousFragment extends Fragment {


    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_qui_somme_nous, container, false);

        TextView _txt_info = (TextView)v.findViewById(R.id.txt_info);
        Spanned sp = Html.fromHtml( getString(R.string.txt_qui_somme_nous));
        _txt_info.setText(Html.fromHtml(getString(R.string.txt_qui_somme_nous)));
       // _txt_info.setText(getString(R.string.txt_qui_somme_nous));

        return  v;
    }

}
