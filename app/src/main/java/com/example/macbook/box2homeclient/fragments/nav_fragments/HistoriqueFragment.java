package com.example.macbook.box2homeclient.fragments.nav_fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.macbook.box2homeclient.LoginActivity;
import com.example.macbook.box2homeclient.R;
import com.example.macbook.box2homeclient.adapters.CustomListAdapter;
import com.example.macbook.box2homeclient.application.BaseApplication;
import com.example.macbook.box2homeclient.models.Order;
import com.example.macbook.box2homeclient.session.Constants;
import com.example.macbook.box2homeclient.session.SessionManager;
import com.example.macbook.box2homeclient.utils.AlertDialogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.macbook.box2homeclient.session.Constants.KEY_ID;
import static com.example.macbook.box2homeclient.session.Constants.KEY_ID_CONNECTED;
import static com.example.macbook.box2homeclient.session.Constants.KEY_PASSWORD;
import static com.example.macbook.box2homeclient.session.Constants.KEY_USERNAME;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_Acesstoken;
import static com.example.macbook.box2homeclient.session.SessionManager.Key_UserID;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoriqueFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,ViewTreeObserver.OnScrollChangedListener  {
    // Log tag
    private static final String TAG = HistoriqueFragment.class.getSimpleName();
    SessionManager sessionman;
 //   private static String url = "";
    private ProgressDialog pDialog;
    private List<Order> myOrderModelList = new ArrayList<Order>();
    private ListView listView;
    private CustomListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;



    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_historique, container, false);
        sessionman =new SessionManager(getContext());
        listView = (ListView) v.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
        adapter = new CustomListAdapter(getActivity(), myOrderModelList,"current",fragmentTransaction);
        listView.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchOrders();
            }
        } );
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        listView.getViewTreeObserver().addOnScrollChangedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        listView.getViewTreeObserver().removeOnScrollChangedListener(this);
    }
    @Override
    public void onRefresh() {
        myOrderModelList.clear();
        adapter.clear();
        fetchOrders();
    }

    @Override
    public void onScrollChanged() {
        if (listView.getFirstVisiblePosition() == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    private void fetchOrders(){

      //  String message = KEY_ID + "=" + sessionman.getUserId().get("ID");
        Map obj = new LinkedHashMap();
        obj.put(KEY_ID_CONNECTED, sessionman.getUserDetails().get(Key_UserID));

        JSONObject jsonObject= new JSONObject(obj);
      //  final String requestBody = message;
        final String requestBody = jsonObject.toString();
        Log.e(TAG, "jsonObject HISTORY "+requestBody);
        if(Constants.androiStudioMode.equals("debug")){
            Log.v(TAG, requestBody);
        }
        swipeRefreshLayout.setRefreshing(true);
      //  url = Constants.HISTORIQUE_URL+sessionman.getUserId().get("ID");
        StringRequest hisRequest = new StringRequest(Request.Method.POST, Constants.HISTORIQUE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                        if(Constants.androiStudioMode.equals("debug")){
                            Log.d(TAG, response.toString());
                        }
                        Log.e(TAG, "List Current Order === "+response.toString());

                        // Parsing json
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            JSONObject obj = jsonArray.getJSONObject(i);
                            Order myOrderModel = new Order();

                            myOrderModel.setArrivee(obj.getString("Arrivee"));
                            myOrderModel.setDepart(obj.getString("Depart"));
                            myOrderModel.setDateCreation(obj.getString("DateCreation"));
                            myOrderModel.setHeureCourse(obj.getString("HeureCourse"));
                            myOrderModel.setMontantMax(obj.getString("MontantMax"));
                            myOrderModel.setPhotoClient(obj.getString("PhotoClient"));
                            myOrderModel.setCoursePaid(obj.getString("CoursePaid"));
                            myOrderModel.setNomClient(obj.getString("NomClient"));
                            myOrderModel.setPrenomClient(obj.getString("PrenomClient"));
                            myOrderModel.setStatut(obj.getString("Statut"));
                            myOrderModel.setTelClient(obj.getString("TelClient"));

                            myOrderModelList.add(myOrderModel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                        adapter.notifyDataSetChanged();

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("/////// VOLLEY  ///// ", error.toString());
                // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);


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
}
