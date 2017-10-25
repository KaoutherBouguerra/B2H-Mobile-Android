package com.example.macbook.box2homeclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;
import com.example.macbook.box2homeclient.application.BaseApplication;
import com.example.macbook.box2homeclient.session.Constants;
import com.example.macbook.box2homeclient.utils.AlertDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.example.macbook.box2homeclient.session.Constants.KEY_EMAIL;
import static com.example.macbook.box2homeclient.session.Constants.KEY_EMAIL_CHANGE;

public class MotDePasseOublieActivity extends AppCompatActivity {
    Button _btn_mail;
    EditText _edt_mail;
    LinearLayout _linearLayout;
    private View mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mot_de_passe_oublie);
        setTitle(getString(R.string.mot_de_passe_oublie_));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        _btn_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;
                View focusView = null;
                _edt_mail.setError(null);
                String email = _edt_mail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    _edt_mail.setError(getString(R.string.error_field_required));
                    focusView = _edt_mail;
                    cancel = true;
                } else if (!isEmailValid(email)) {
                    _edt_mail.setError(getString(R.string.error_invalid_email));
                    focusView = _edt_mail;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    sendMail(email);
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private  void init(){
        _edt_mail = (EditText) findViewById(R.id.edt_email);
        _btn_mail = (Button) findViewById(R.id.btn_change_pwd);
        _linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void sendMail(final String email ) {

        //TODO: web service send-email return HTML format => ajouter popup webservice non disponible
        showProgress(true);
        String message = KEY_EMAIL_CHANGE + "=" + email ;

        final String requestBody = message;

        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, Constants.FORGET_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String AccessToken = response;
                if(Constants.androiStudioMode.equals("debug")){
                    Toast.makeText(MotDePasseOublieActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", AccessToken);
                }
                showProgress(false);

                AlertDialogManager.showAlertDialog(MotDePasseOublieActivity.this,getResources().getString( R.string.app_name),getResources().getString(R.string.request_success),true,3);

                Log.v("Json","AccessToken AccessToken " +AccessToken);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                Log.e("/////// VOLLEY  ///// ", error.toString());

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(MotDePasseOublieActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(MotDePasseOublieActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(MotDePasseOublieActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(MotDePasseOublieActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);


                }

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
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
        BaseApplication.getInstance().addToRequestQueue(LoginFirstRequest);


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            if (show) _linearLayout.setVisibility(View.GONE);


          /*  mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            */

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
            if (show) _linearLayout.setVisibility(View.GONE);
            //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isEmailValid(String email) {
         return email.contains("@");
    }
}
