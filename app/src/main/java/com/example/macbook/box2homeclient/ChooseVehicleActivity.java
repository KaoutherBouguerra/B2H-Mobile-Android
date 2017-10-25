package com.example.macbook.box2homeclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.macbook.box2homeclient.application.BaseApplication;
import com.example.macbook.box2homeclient.fragments.nav_fragments.HistoriqueFragment;
import com.example.macbook.box2homeclient.models.Order;
import com.example.macbook.box2homeclient.session.Constants;
import com.example.macbook.box2homeclient.session.SessionManager;
import com.example.macbook.box2homeclient.utils.AlertDialogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.macbook.box2homeclient.session.Constants.KEY_DISTANCE;
import static com.example.macbook.box2homeclient.session.Constants.KEY_DRIVER_TYPE;
import static com.example.macbook.box2homeclient.session.Constants.KEY_ID_CONNECTED;
import static com.example.macbook.box2homeclient.session.Constants.KEY_VEHICULE;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_Acesstoken;
import static com.example.macbook.box2homeclient.session.SessionManager.Key_UserID;

public class ChooseVehicleActivity extends AppCompatActivity {

    TextView _txt_depart,_txt_arrivee;
    private static String txt_s ="(3 à 6m3)",txt_m="(7 à 10m3)",txt_l="(11 à 15m3)",txt_xl = "(16m3 et plus)";
    ImageView _img_s,_img_m,_img_l,_img_xl;
    LinearLayout _linearLayoutS,_linearLayoutM,_linearLayoutL,_linearLayoutXL,linearLayout2;
    FloatingActionButton fabDone;
    private static final String TAG = ChooseVehicleActivity.class.getSimpleName();
    SessionManager sessionman;
    private View mProgressView;
    private String vehiculType="S",depart,arrivee,textVehiculType="S"+"(3 à 6m3)";
    Double dLat,dLng,aLat,aLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vehicle);
        sessionman =new SessionManager(ChooseVehicleActivity.this);
        setTitle(getString(R.string.votre_course));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();

          depart = getIntent().getStringExtra("DEPART");
          arrivee = getIntent().getStringExtra("ARRIVEE");

          dLat = getIntent().getDoubleExtra("dLat",0);
          dLng = getIntent().getDoubleExtra("dLng",0);
          aLat = getIntent().getDoubleExtra("aLat",0);
          aLng = getIntent().getDoubleExtra("aLng",0);

        _txt_depart.setText(depart);
        _txt_arrivee.setText(arrivee);

        _linearLayoutS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _img_s.setImageResource(R.mipmap.truck_clicked);
                _img_m.setImageResource(R.mipmap.truck);
                _img_l.setImageResource(R.mipmap.truck);
                _img_xl.setImageResource(R.mipmap.truck);
                textVehiculType="S "+txt_s;
            }
        });

        _linearLayoutM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _img_m.setImageResource(R.mipmap.truck_clicked);
                vehiculType="M";
                textVehiculType="M "+txt_m;
                _img_s.setImageResource(R.mipmap.truck);
                _img_l.setImageResource(R.mipmap.truck);
                _img_xl.setImageResource(R.mipmap.truck);
            }
        });

        _linearLayoutL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _img_l.setImageResource(R.mipmap.truck_clicked);
                vehiculType="L";
                textVehiculType="L "+txt_l;
                _img_s.setImageResource(R.mipmap.truck);
                _img_m.setImageResource(R.mipmap.truck);
                _img_xl.setImageResource(R.mipmap.truck);
            }
        });

        _linearLayoutXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _img_xl.setImageResource(R.mipmap.truck_clicked);
                vehiculType="XL";
                textVehiculType="L "+txt_xl;
                _img_s.setImageResource(R.mipmap.truck);
                _img_m.setImageResource(R.mipmap.truck);
                _img_l.setImageResource(R.mipmap.truck);

            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ERROR"," dLat ____ "+dLat);
                Log.e("ERROR"," dLng ____ "+dLng);
                Log.e("ERROR"," aLat ____ "+aLat);
                Log.e("ERROR"," aLng ____ "+aLng);

                getDistance(dLat,dLng,aLat,aLng);
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private  void init(){
        _txt_depart = (TextView) findViewById(R.id.txt_depart);
        _txt_arrivee = (TextView) findViewById(R.id.txt_arrivee);
        _img_s = (ImageView) findViewById(R.id.img_s);
        _img_m = (ImageView) findViewById(R.id.img_m);
        _img_l = (ImageView) findViewById(R.id.img_l);
        _img_xl = (ImageView) findViewById(R.id.img_xl);
        _linearLayoutS = (LinearLayout)findViewById(R.id.linearS);
        _linearLayoutM = (LinearLayout)findViewById(R.id.linearM);
        _linearLayoutL = (LinearLayout)findViewById(R.id.linearL);
        _linearLayoutXL = (LinearLayout)findViewById(R.id.linearXL);
        linearLayout2 = (LinearLayout)findViewById(R.id.linearLayout2);
         fabDone = (FloatingActionButton) findViewById(R.id.floatingActionButtonDone);
        mProgressView = findViewById(R.id.login_progress);
    }

    private String getDistance(Double dLat,Double dLng,Double myLat,Double myLng){
        String dist="";
        try {
        JSONObject locationJsonObject = new JSONObject();
        locationJsonObject.put("origin", dLat+","+dLng);
        locationJsonObject.put("destination", myLat+","+myLng);

            dist=  LatlngCalc(locationJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dist;
    }
    private void calculMontant(String dis){

        //  String message = KEY_ID + "=" + sessionman.getUserId().get("ID");
        showProgress(true);
        Map obj = new LinkedHashMap();
        obj.put(KEY_ID_CONNECTED, sessionman.getUserDetails().get(Key_UserID));
        obj.put(KEY_DISTANCE, dis);
        obj.put(KEY_DRIVER_TYPE, "easy");
        obj.put(KEY_VEHICULE, vehiculType);

        JSONObject jsonObject= new JSONObject(obj);

        final String requestBody = jsonObject.toString();
        Log.e(TAG, "jsonObject Monatant calcul "+requestBody);
        if(Constants.androiStudioMode.equals("debug")){
            Log.v(TAG, requestBody);
        }

        //  url = Constants.HISTORIQUE_URL+sessionman.getUserId().get("ID");
        StringRequest hisRequest = new StringRequest(Request.Method.POST, Constants.MOBILE_CALCUL_MONTANT_COURSE_URl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(Constants.androiStudioMode.equals("debug")){
                    Log.d(TAG, response.toString());
                }
                showProgress(false);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String montant = jsonObject1.getString("Montant")   ;
                    String max = jsonObject1.getString("max")   ;
                    String firstCourse = jsonObject1.getString("FirstCourse")   ;
                    String nouvMontant = jsonObject1.getString("NouvMontant")   ;

                    Log.e(TAG, "Calulate montant Order === "+response.toString());
                    Intent intent = new Intent(ChooseVehicleActivity.this,ResultActivity.class);

                    intent.putExtra("DEPART",depart);
                    intent.putExtra("ARRIVEE",arrivee);
                    intent.putExtra("TYPE_VEHICULE",textVehiculType);
                    intent.putExtra("MONTANT",nouvMontant);
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }









                // Parsing json


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("/////// VOLLEY  ///// ", error.toString());
                // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(ChooseVehicleActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(ChooseVehicleActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(ChooseVehicleActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(ChooseVehicleActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);


                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                String accessId = BaseApplication.session.getUserDetails().get(KEY_Acesstoken);
                if(Constants.androiStudioMode.equals("debug")){
                    Log.v("accessid", accessId);}
                headers.put("Authorization", "Bearer " + accessId);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {

                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        // Adding request to request queue
        BaseApplication.getInstance().addToRequestQueue(hisRequest);


    }

    private String LatlngCalc(JSONObject locationJsonObject) throws JSONException {
        final String[] dist = {""};
        RequestQueue queue = Volley.newRequestQueue(ChooseVehicleActivity.this);
        String url = "http://maps.googleapis.com/maps/api/distancematrix/" +
                "json?origins=" + locationJsonObject.getString("origin") + "&destinations=" + locationJsonObject.getString("destination") + "&mode=driving&" +
                "language=fr&sensor=false";
        Log.e("ERROR"," URL ____ "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("rows");
                            JSONObject routes = array.getJSONObject(0);
                            JSONArray legs = routes.getJSONArray("elements");
                            JSONObject steps = legs.getJSONObject(0);
                            JSONObject distance = steps.getJSONObject("distance");
                              dist[0] = distance.getString("text");
                            String[] separated = dist[0].split(" ");

                            String distanceKm= separated[0];

                            calculMontant(distanceKm);
                        } catch (JSONException e) {
                            Log.e("ERROR"," JSONException ____ "+e.getMessage());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

        return dist[0];
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            if (show) {
                linearLayout2.setVisibility(View.INVISIBLE);


            }else linearLayout2.setVisibility(View.VISIBLE);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            if (show) {
                linearLayout2.setVisibility(View.INVISIBLE);

            }   else linearLayout2.setVisibility(View.VISIBLE);
            //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
