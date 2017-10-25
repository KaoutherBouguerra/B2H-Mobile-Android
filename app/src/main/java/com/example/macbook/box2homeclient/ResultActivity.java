package com.example.macbook.box2homeclient;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.macbook.box2homeclient.session.SessionManager;

public class ResultActivity extends AppCompatActivity {
    private String vehiculType,depart,arrivee,montant;
    TextView _txt_depart,_txt_arrivee,_txt_type,_txt_montant;
    LinearLayout linearLayout2;
    FloatingActionButton fabDone;
    private static final String TAG = ResultActivity.class.getSimpleName();
    SessionManager sessionman;
    private View mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle(getString(R.string.votre_course));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
        depart = getIntent().getStringExtra("DEPART");
        arrivee = getIntent().getStringExtra("ARRIVEE");
        vehiculType = getIntent().getStringExtra("TYPE_VEHICULE");
        montant = getIntent().getStringExtra("MONTANT");

        _txt_depart.setText(depart);
        _txt_arrivee.setText(arrivee);
        _txt_type.setText(vehiculType);
        _txt_montant.setText(montant+" €");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private  void init(){
        _txt_depart = (TextView) findViewById(R.id.txt_depart);
        _txt_arrivee = (TextView) findViewById(R.id.txt_arrivee);
        _txt_type = (TextView) findViewById(R.id.txt_type);
        _txt_montant = (TextView) findViewById(R.id.txt_montant);

        linearLayout2 = (LinearLayout)findViewById(R.id.linearLayout2);
        fabDone = (FloatingActionButton) findViewById(R.id.floatingActionButtonDone);
        mProgressView = findViewById(R.id.login_progress);
    }
}
