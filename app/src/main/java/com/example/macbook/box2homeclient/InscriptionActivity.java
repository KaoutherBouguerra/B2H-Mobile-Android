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


import java.io.UnsupportedEncodingException;

import static com.example.macbook.box2homeclient.session.Constants.*;

public class InscriptionActivity extends AppCompatActivity {



    private EditText _edt_nom,_edt_prenom,_edt_tel,_edt_mail,_edt_pwd,_edt_confirm_edt;
    private Button _btn_sign_up;
    private View mProgressView;
    private View mSignUpFormView;


    private static final String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        setTitle(getString(R.string.action_sign_up));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        _btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });


    }
    private void attemptSignUp() {


        // Reset errors.
        _edt_nom.setError(null);
        _edt_prenom.setError(null);
        _edt_tel.setError(null);
        _edt_mail.setError(null);
        _edt_pwd.setError(null);
        _edt_confirm_edt.setError(null);

        // Store values at the time of the login attempt.
        String nom = _edt_nom.getText().toString();
        String prenom = _edt_prenom.getText().toString();
        String tel = _edt_tel.getText().toString();
        String email = _edt_mail.getText().toString();
        String password = _edt_pwd.getText().toString();
        String confirm_pwd = _edt_confirm_edt.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid phone, if the User entered one.
        if (TextUtils.isEmpty(nom)) {
            _edt_nom.setError(getString(R.string.error_field_required));
            focusView = _edt_nom;
            cancel = true;
        }else if (isTextValid(nom)){
            _edt_nom.setError(getString(R.string.error_invalid_nom));
            focusView = _edt_nom;
            cancel = true;

        }

        if (TextUtils.isEmpty(prenom)) {
            _edt_prenom.setError(getString(R.string.error_field_required));
            focusView = _edt_prenom;
            cancel = true;
        } else  if (!TextUtils.isEmpty(prenom) && isTextValid(prenom)) {
            _edt_prenom.setError(getString(R.string.error_invalid_prenom));
            focusView = _edt_prenom;
            cancel = true;
        }

        if (TextUtils.isEmpty(tel)){
            _edt_tel.setError(getString(R.string.error_field_required));
            focusView = _edt_tel;
            cancel = true;
        }else if (!isPhoneValid(tel)) {
            _edt_tel.setError(getString(R.string.error_invalid_tel));
            focusView = _edt_tel;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            _edt_pwd.setError(getString(R.string.error_field_required));
            focusView = _edt_pwd;
            cancel = true;
        }
        // Check for a valid password, if the User entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            _edt_pwd.setError(getString(R.string.error_invalid_password));
            focusView = _edt_pwd;
            cancel = true;
        }
        // Check for a valid password, if the User entered one.
        if (TextUtils.isEmpty(confirm_pwd)) {
            _edt_confirm_edt.setError(getString(R.string.error_field_required));
            focusView = _edt_confirm_edt;
            cancel = true;
        }else  if (!password.equals(confirm_pwd)) {
            _edt_confirm_edt.setError(getString(R.string.error_invalid_same_pwd));
            focusView = _edt_confirm_edt;
            cancel = true;
        }

        // Check for a valid email address.

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
            // Show a progress spinner, and kick off a background task to
            // perform the User login attempt.
            showProgress(true);
            // mAuthTask = new UserLoginTask(email, password);
            // mAuthTask.execute((Void) null);
            signUp(nom,prenom,tel,email,password);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void init(){

        _edt_nom = (EditText)findViewById(R.id.edt_nom);
        _edt_prenom = (EditText)findViewById(R.id.edt_prenom);
        _edt_tel = (EditText)findViewById(R.id.edt_tel);
        _edt_mail = (EditText)findViewById(R.id.edt_mail);
        _edt_pwd = (EditText)findViewById(R.id.edt_pwd);
        _edt_confirm_edt = (EditText)findViewById(R.id.edt_confirm_pwd);
        _btn_sign_up = (Button) findViewById(R.id.email_sign_up_button);

        mSignUpFormView = findViewById(R.id.line1);
        mProgressView = findViewById(R.id.login_progress);

    }

    private boolean isEmailValid(String email) {
         return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
         return password.length() > 4;
    }

    private boolean isPhoneValid(String tel) {
         return tel.length() > 7;
    }

    private boolean isTextValid(String text) {
        return Character.isDigit(text.charAt(0));
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
                mSignUpFormView.setVisibility(View.INVISIBLE);
                _btn_sign_up.setVisibility(View.INVISIBLE);

            }


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
            if (show) {
                mSignUpFormView.setVisibility(View.INVISIBLE);
                _btn_sign_up.setVisibility(View.INVISIBLE);
            }
            //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void signUp(final String nom, final String prenom, final String tel, final String email, final String pwd ) {

        //TODO: ajouter webservice TestExistMail avant l(inscription pour vérifier si l'email existe déja ou non



        String message = KEY_FIRST_NAME + "=" + nom
                + "&" + KEY_LAST_NAME + "=" + prenom
                + "&" + KEY_PHONE + "=" + tel
                + "&" + KEY_EMAIL + "=" + email
                + "&" + KEY_PASSWORD + "=" + pwd;


        // ayman.json.JSONObject jsonBody = new ayman.json.JSONObject(obj);

        if(Constants.androiStudioMode.equals("debug")){
            Log.v(TAG, message);}
        final String requestBody = message;

        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, Constants.SIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String AccessToken = response;
                if(Constants.androiStudioMode.equals("debug")){
                    Toast.makeText(InscriptionActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", AccessToken);
                }
                showProgress(false);

                AlertDialogManager.showAlertDialog(InscriptionActivity.this,getResources().getString( R.string.app_name),getResources().getString(R.string.success_signup),true,3);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                Log.e("/////// VOLLEY  ///// ", error.toString());
              //  AlertDialogManager.showAlertDialog(InscriptionActivity.this,getResources().getString( R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(InscriptionActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(InscriptionActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(InscriptionActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(InscriptionActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);


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

}
