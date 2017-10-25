package com.example.macbook.box2homeclient.fragments.nav_fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.macbook.box2homeclient.ChooseVehicleActivity;
import com.example.macbook.box2homeclient.R;
import com.example.macbook.box2homeclient.session.Constants;
import com.example.macbook.box2homeclient.utils.GPSTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.macbook.box2homeclient.R.id.map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final long INTERVAL = 0;
    private static final long FASTEST_INTERVAL = 0;
    private static final String TAG = "CourseFragment";
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private boolean isDepartClicked=false;
    private boolean isArriveeClicked=false;
    public static final int REQUEST_CODE_CAPTURE = 100;
    View v;
    View mapView;
    GoogleMap m_map;
    GPSTracker gps;
    public static double latitude = 0, longitude = 0;
    Marker currentLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static Location mLocation;
    private boolean first= true;
    Double depLat,depLng,arrLat,arrLng;

    private EditText _edt_depart,_edt_arrivee;
    private LinearLayout _linearLayout;
    private LinearLayout _linearLayoutArr;
    private LinearLayout _linearLayoutDep;
    private ProgressBar _progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        v= inflater.inflate(R.layout.fragment_course, container, false);
//        Snackbar.make(v, getString(R.string.snackbarText), Snackbar.LENGTH_LONG)
  //              .setAction("Action", null).show();
        initFields();
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCurrentLocation();
            }
        });

        _linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isDepartClicked){
                    isArriveeClicked=false;
                    isDepartClicked=true;
                    _linearLayoutDep.setBackgroundResource(0);
                    _linearLayoutArr.setBackgroundResource(R.drawable.rectangle_shadow_top);
                    _linearLayout.setBackgroundResource(R.drawable.marker_active_arr);

                    depLat = latitude;
                    depLng=longitude;
                    displayCurrentAdresse(latitude,longitude,isDepartClicked);

                    _edt_depart.setTextColor(ContextCompat.getColor(getActivity(), R.color._color_light_grey));
                    _edt_arrivee.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorblack));
                    changeMargin(_linearLayoutDep,_edt_depart);
                    _edt_arrivee.requestFocus();
                }else {
                    isArriveeClicked=true;
                    isDepartClicked=false;

                   // _linearLayoutDep.setBackgroundResource(R.drawable.rectangle_shadow_top);
                    //_linearLayoutArr.setBackgroundResource(0);
                    //_linearLayout.setBackgroundResource(R.drawable.marker_active);
                    arrLat = latitude;
                    arrLng=longitude;
                    displayCurrentAdresse(latitude,longitude,isDepartClicked);
                    //_edt_arrivee.setTextColor(ContextCompat.getColor(getActivity(), R.color._color_light_grey));
                    //_edt_depart.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorblack));

                    //_edt_depart.requestFocus();
                    //changeMargin(_linearLayoutArr,_edt_arrivee);

                }


            }
        });



        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        return v;
    }

    private void initFields(){

        _linearLayout=(LinearLayout)v.findViewById(R.id.linearLayout);
        _linearLayoutArr=(LinearLayout)v.findViewById(R.id.line2);
        _linearLayoutDep=(LinearLayout)v.findViewById(R.id.line1);
        _edt_depart=(EditText)v.findViewById(R.id.edt_depart);
        _edt_arrivee=(EditText)v.findViewById(R.id.edt_arrivee);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    protected void startLocationUpdates() {
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
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        Log.d(TAG, "Location update started ..............: ");
    }
    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;

        if (first){
            displayCurrentLocation();
            displayCurrentAdresse(mLocation.getLatitude(),mLocation.getLongitude(),isDepartClicked);
            first = false;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        m_map = googleMap;
        if(m_map != null) {
            gps = new GPSTracker(getContext());
            ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mg3 = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            // check if GPS enabled
            //test to loacte current location

            if( !gps.canGetLocation() && ( mWifi.isConnected() ||  mg3.isConnected())){
                showSettingsAlert();

                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
            }
            else if( gps.canGetLocation()&& (!mWifi.isConnected() && !mg3.isConnected()) ){

                showsettingwifi();
            }else if(  gps.canGetLocation() && ( mWifi.isConnected() ||  mg3.isConnected())){

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    return;
                }else{

                    m_map.setMyLocationEnabled(true);

                }
            }
            m_map.getUiSettings().setMapToolbarEnabled(false);
            m_map.getUiSettings().setAllGesturesEnabled(true);
            m_map.getUiSettings().setMyLocationButtonEnabled(false);


          /*  m_map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    if (!first)
                      _progressBar.setVisibility(View.VISIBLE);
                  //  displayCurrentAdresse(cameraPosition.target.latitude,cameraPosition.target.longitude);
                    latitude = cameraPosition.target.latitude;
                    longitude = cameraPosition.target.longitude;


                }


            });

            */
            m_map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                   LatLng mapCenterLatLng = m_map.getCameraPosition().target;


                    _progressBar.setVisibility(View.INVISIBLE);
                    latitude = mapCenterLatLng.latitude;
                    longitude = mapCenterLatLng.longitude;

                    if (!first)
                     displayCurrentAdresse(latitude,longitude,isDepartClicked);
                }
            });
            m_map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int reason) {
                    if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {

                        _progressBar.setVisibility(View.VISIBLE);
                    } else if (reason == GoogleMap.OnCameraMoveStartedListener
                            .REASON_API_ANIMATION) {

                    } else if (reason == GoogleMap.OnCameraMoveStartedListener
                            .REASON_DEVELOPER_ANIMATION) {

                    }
                }
            });
            m_map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {

                }
            });
            m_map.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
                @Override
                public void onCameraMoveCanceled() {

                }
            });

          //  mapCenterLatLng = mGoogleMap.getCameraPosition().target;// it should be done on MapLoaded.

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }

            m_map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {

                }
            });


        }

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        isArriveeClicked = false;
        isDepartClicked =false;
        _linearLayoutDep.setBackgroundResource(R.drawable.rectangle_shadow_top);
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }
    @Override
    public void onPause() {
        super.onPause();
      //  stopLocationUpdates();
    }

    public void showSettingsAlert() {
      /*  if(isNetworkEnabled==false){
            Toast.makeText(getApplicationContext(),"enable wifi or mobile data",Toast.LENGTH_LONG).show();

        }*/
//        startActivity(new Intent("android.settings.WIFI_SETTINGS"));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,REQUEST_CODE_CAPTURE);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void showsettingwifi() {
        // startActivity(new Intent("android.settings.WIFI_SETTINGS"));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("WIFI is settings");

        // Setting Dialog Message
        alertDialog.setMessage("WIFI is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("WIFI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(intent,REQUEST_CODE_CAPTURE);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    public void displayCurrentLocation() {


        if (mLocation!= null){
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();


              //  currentLocationMarker.remove();

            // m_map.clear();
          //  Marker currentLocationMarker = m_map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Current Location") ) ;
        //    currentLocationMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.mylocationpin));

            m_map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));


        }

    }

    public void displayCurrentAdresse(Double latitude, Double longitude,boolean isDepartClicked) {

            if (latitude != null && longitude != null) {
                this.latitude = latitude;
                this.longitude = longitude;
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    if (Constants.androiStudioMode.equals("debug")) {
                        Log.i("addresses", addresses.toString());
                    }
                    Log.i("addresses", addresses.toString());
                    String street = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String address = addresses.get(0).getAddressLine(1);
                    String city = addresses.get(0).getAddressLine(2);
                    String state = addresses.get(0).getAddressLine(3);
                    String Country = addresses.get(0).getAddressLine(4);

                 // String   completeAdress = "," + Country + "," + state + "," + city + "," + address + "," + street + "  ";
                    //String adr = completeAdress.replaceAll("null,", "");
                 //   String adr = getCompleteAddressString(latitude,longitude);
                   // Toast.makeText(getContext(), "" + adr, Toast.LENGTH_LONG).show();

                    if (!isArriveeClicked) {
                        if (!isDepartClicked)
                            _edt_depart.setText(street);
                        else _edt_arrivee.setText(street);

                        _progressBar.setVisibility(View.INVISIBLE);


                    }else {
                        Intent intent = new Intent(getActivity(), ChooseVehicleActivity.class);
                        intent.putExtra("DEPART",_edt_depart.getText().toString());
                        intent.putExtra("ARRIVEE",_edt_arrivee.getText().toString());

                        intent.putExtra("dLat",depLat);
                        intent.putExtra("dLng",depLng);
                        intent.putExtra("aLat",arrLat);
                        intent.putExtra("aLng",arrLng);
                        startActivity(intent);
                        //TODO go to choosevehiculActivity
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

    }

    private void changeMargin(LinearLayout _linearLayoutDep,EditText _edt_depart){

        RelativeLayout.LayoutParams parameterArr =  (RelativeLayout.LayoutParams) _linearLayoutDep.getLayoutParams();
        LinearLayout.LayoutParams parameterDep =  (LinearLayout.LayoutParams) _edt_depart.getLayoutParams();
        parameterArr.setMargins(parameterArr.leftMargin, parameterArr.topMargin, parameterArr.rightMargin, 0); // left, top, right, bottom
        if (isArriveeClicked){
            parameterDep.setMargins(parameterDep.leftMargin, 0, parameterDep.rightMargin, parameterDep.bottomMargin); // left, top, right, bottom

        }else
            parameterDep.setMargins(parameterDep.leftMargin, parameterDep.topMargin, parameterDep.rightMargin, 0); // left, top, right, bottom

        _linearLayoutDep.setLayoutParams(parameterArr);
        _edt_depart.setLayoutParams(parameterDep);
       // _linearLayoutDep.invalidate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                // permission was granted, yay! Do the
                // location-related task you need to do.


                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                } else {
                    m_map.setMyLocationEnabled(true);

                    //postCityId(longitude,latitude);

                    displayCurrentLocation();
                    displayCurrentAdresse(mLocation.getLatitude(),mLocation.getLongitude(),isDepartClicked);

                }
            }


            return;


            // other 'case' lines to check for other
            // permissions this app might request
        }


    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("My Current address", strReturnedAddress.toString());
            } else {
                Log.e("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }
        return strAdd;
    }
}
