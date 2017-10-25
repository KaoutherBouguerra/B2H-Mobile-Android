package com.example.macbook.box2homeclient;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.example.macbook.box2homeclient.application.BaseApplication;
import com.example.macbook.box2homeclient.circularimageview.CircularImageView;
import com.example.macbook.box2homeclient.custom.CustomTypefaceSpan;
import com.example.macbook.box2homeclient.fragments.nav_fragments.CourseFragment;
import com.example.macbook.box2homeclient.fragments.nav_fragments.HistoriqueFragment;
import com.example.macbook.box2homeclient.fragments.nav_fragments.PartagerFragment;
import com.example.macbook.box2homeclient.fragments.nav_fragments.ProfilFragment;
import com.example.macbook.box2homeclient.fragments.nav_fragments.QuiSommeNousFragment;
import com.example.macbook.box2homeclient.session.Constants;
import com.example.macbook.box2homeclient.session.SessionManager;
import com.squareup.picasso.Picasso;

import static com.example.macbook.box2homeclient.session.Constants.KEY_FIRST_NAME;
import static com.example.macbook.box2homeclient.session.Constants.PAGE_FACEBOOK;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_EMAIL;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_FIRSTNAME;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_LASTNAME;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_PHOTO;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BaseApplication controller;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = ((BaseApplication) getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // changeMenuFont(navigationView);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
            setHeaderInfo(navigationView);
            showFirstView();

        }
    }

    private void changeMenuFont(NavigationView navigationView){
        Menu m = navigationView .getMenu();

        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    SpannableString s = new SpannableString(subMenuItem.getTitle());
                    Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/NunitoSans-Regular.ttf");

                    s.setSpan(tf, 0, s.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    subMenuItem.setTitle(s);
                }
            }

        }
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/NunitoSans-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
    private void setHeaderInfo(NavigationView navigationView){
        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView  name = (TextView)header.findViewById(R.id.txt_username);
        CircularImageView imview = (CircularImageView) header.findViewById(R.id.imageView);

        name.setText(BaseApplication.session.getUserDetails().get(KEY_FIRSTNAME)+" "+BaseApplication.session.getUserDetails().get(KEY_LASTNAME));
        String img_url = Constants.image_baseUrl+BaseApplication.session.getUserDetails().get(KEY_EMAIL)+"/"
                +BaseApplication.session.getUserDetails().get(KEY_PHOTO);

        Picasso.with(MainActivity.this).load( img_url)
                .placeholder(R.drawable.user_profile_image_background)
                .into(imview);

    }

    private void showFirstView(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setTitle(getString(R.string.title_course));
        CourseFragment schedule = new CourseFragment();
        fragmentTransaction.replace( R.id.frame,schedule,"Course Fragment");
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            super.onBackPressed();
        }


    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean isLogout= false;
        boolean share= false;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_course) {
            setTitle(getString(R.string.title_course));
             CourseFragment schedule = new CourseFragment();
             fragmentTransaction.replace( R.id.frame,schedule,"Course Fragment");
            // Handle the camera action
        } else if (id == R.id.nav_profil) {
            setTitle(getString(R.string.title_profi));
            ProfilFragment schedule = new ProfilFragment();
            fragmentTransaction.replace( R.id.frame,schedule,"Profil Fragment");

        } else if (id == R.id.nav_historique) {
            setTitle(getString(R.string.title_historique));
            HistoriqueFragment schedule = new HistoriqueFragment();
            fragmentTransaction.replace( R.id.frame,schedule,"Order Fragment");

        } else if (id == R.id.nav_partager) {

           // PartagerFragment schedule = new PartagerFragment();
            //fragmentTransaction.replace( R.id.frame,schedule,"Partager Fragment");
            share =true;
            share();

        } else if (id == R.id.nav_about) {
            setTitle(getString(R.string.title_qui_sommes_nous));
            QuiSommeNousFragment schedule = new QuiSommeNousFragment();
            fragmentTransaction.replace( R.id.frame,schedule,"QuiSommeNous Fragment");

        } else if (id == R.id.nav_off) {
            isLogout=true;
           disconnectMessage();
        }
        if (!isLogout && !share){
            fragmentTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void share(){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, PAGE_FACEBOOK);
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void disconnectMessage(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set title
        alertDialogBuilder.setTitle(getString(R.string.app_name));

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.disconnectMsg))
                .setCancelable(false)
                .setPositiveButton("Oui",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        MainActivity.this.finish();

                        dialog.cancel();

                        BaseApplication.session.logoutUser();
                    }
                })
                .setNegativeButton("Non",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
