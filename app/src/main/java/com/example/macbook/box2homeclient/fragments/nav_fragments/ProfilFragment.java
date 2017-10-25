package com.example.macbook.box2homeclient.fragments.nav_fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.macbook.box2homeclient.R;
import com.example.macbook.box2homeclient.application.BaseApplication;
import com.example.macbook.box2homeclient.circularimageview.CircularImageView;
import com.example.macbook.box2homeclient.models.User;
import com.example.macbook.box2homeclient.session.Constants;
import com.example.macbook.box2homeclient.utils.AlertDialogManager;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


import static com.example.macbook.box2homeclient.session.Constants.KEY_FIRST_NAME;
import static com.example.macbook.box2homeclient.session.Constants.KEY_LAST_NAME;
import static com.example.macbook.box2homeclient.session.Constants.KEY_PASSWORD;
import static com.example.macbook.box2homeclient.session.Constants.KEY_PHONE;
import static com.example.macbook.box2homeclient.session.Constants.KEY_USERNAME;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_ADRESSE;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_Acesstoken;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_COUNTRY;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_EMAIL;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_NAME;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_PHOTO;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_SOCIETE;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_STATE;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_ZIP_CODE;
import static com.example.macbook.box2homeclient.session.SessionManager.Key_UserID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    CircularImageView _imview;
    FloatingActionButton f_ab_done;
    private View mProgressView;
    private View mSignUpFormView;

    View v;
    EditText _edt_name,_edt_groupe,_edt_mail,_edt_tel,_edt_pwd,_edt_confirm_pwd;
    private static final String TAG = ProfilFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_profil, container, false);
        inisializer();
        User user = BaseApplication.session.getLoggedUser();

        Log.e(TAG,"User **** "+user);
        _edt_name.setText(BaseApplication.session.getUserDetails().get(KEY_USERNAME));
        _edt_groupe.setText(BaseApplication.session.getUserDetails().get(KEY_SOCIETE));
        _edt_mail.setText(BaseApplication.session.getUserDetails().get(KEY_EMAIL));
        _edt_tel.setText(BaseApplication.session.getUserDetails().get(KEY_PHONE));
        String img_url = Constants.image_baseUrl+BaseApplication.session.getUserDetails().get(KEY_EMAIL)+"/"
                +BaseApplication.session.getUserDetails().get(KEY_PHOTO);

        Picasso.with(getActivity()).load(img_url)
                .placeholder(R.drawable.user_profile_image_background)
                .into(_imview);


        f_ab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptUpdate();

            }
        });
        return v;
    }

    private void inisializer(){

        _edt_name = (EditText)v.findViewById(R.id.edt_nom);
        _edt_groupe = (EditText)v.findViewById(R.id.edt_groupe);
        _edt_mail = (EditText)v.findViewById(R.id.edt_mail);
        _edt_tel = (EditText)v.findViewById(R.id.edt_tel);
        _edt_pwd = (EditText)v.findViewById(R.id.edt_pwd);
        _edt_confirm_pwd = (EditText)v.findViewById(R.id.edt_confirm_pwd);
        _imview = (CircularImageView) v.findViewById(R.id.imUser);
        f_ab_done = (FloatingActionButton) v.findViewById(R.id.fab_done);
        mSignUpFormView = v.findViewById(R.id.line1);
        mProgressView = v.findViewById(R.id.login_progress);

    }
    private void attemptUpdate() {
        // Reset errors.
        _edt_name.setError(null);
        _edt_groupe.setError(null);
        _edt_tel.setError(null);
        _edt_mail.setError(null);
        _edt_pwd.setError(null);
        _edt_confirm_pwd.setError(null);

        // Store values at the time of the login attempt.
        String nom = _edt_name.getText().toString();
        String groupe = _edt_groupe.getText().toString();
        String tel = _edt_tel.getText().toString();
        String email = _edt_mail.getText().toString();
        String password = _edt_pwd.getText().toString();
        String confirm_pwd = _edt_confirm_pwd.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid phone, if the User entered one.
        if (!TextUtils.isEmpty(nom) && isTextValid(nom)) {
            _edt_name.setError(getString(R.string.error_invalid_nom));
            focusView = _edt_name;
            cancel = true;
        }
        // Check for a valid phone, if the User entered one.
        if (!TextUtils.isEmpty(groupe) && isTextValid(groupe)) {
            _edt_groupe.setError(getString(R.string.error_invalid_prenom));
            focusView = _edt_groupe;
            cancel = true;
        }
        // Check for a valid phone, if the User entered one.
        if (!TextUtils.isEmpty(tel) && !isPhoneValid(tel)) {
            _edt_tel.setError(getString(R.string.error_invalid_tel));
            focusView = _edt_tel;
            cancel = true;
        }
        /*
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
            _edt_confirm_pwd.setError(getString(R.string.error_field_required));
            focusView = _edt_confirm_pwd;
            cancel = true;
        }else  if (!password.equals(confirm_pwd)) {
            _edt_confirm_pwd.setError(getString(R.string.error_invalid_same_pwd));
            focusView = _edt_confirm_pwd;
            cancel = true;
        }
*/
        if (!TextUtils.isEmpty(password)){
            if (TextUtils.isEmpty(confirm_pwd)) {
                _edt_confirm_pwd.setError(getString(R.string.error_field_required));
                focusView = _edt_confirm_pwd;
                cancel = true;
            }else if (!password.equals(confirm_pwd)) {
                _edt_confirm_pwd.setError(getString(R.string.error_invalid_same_pwd));
                focusView = _edt_confirm_pwd;
                cancel = true;
            }

        }
        if (!TextUtils.isEmpty(confirm_pwd)){
            if (TextUtils.isEmpty(password)) {
                _edt_pwd.setError(getString(R.string.error_field_required));
                focusView = _edt_pwd;
                cancel = true;
            }else if (!password.equals(confirm_pwd)) {
                _edt_pwd.setError(getString(R.string.error_invalid_same_pwd));
                focusView = _edt_pwd;
                cancel = true;
            }

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
            update(nom,groupe,tel,email,password);
        }
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


            }else{
                mSignUpFormView.setVisibility(View.VISIBLE);
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

            }else{
                mSignUpFormView.setVisibility(View.VISIBLE);
            }
            //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void update(final String nom, final String groupe, final String tel, final String email, final String pwd ) {



        //TODO: ajouter les paramétres pour ce ws

        String message = "id" + "=" + BaseApplication.session.getUserDetails().get(Key_UserID)
                + "&" + "firstName" + "=" + nom
                + "&" + "lastName" + "=" + groupe
                + "&" + "phone" + "=" + tel
                + "&" + "phone" + "=" + tel
                + "&" + "adress" + "=" + BaseApplication.session.getUserDetails().get(KEY_ADRESSE)
                + "&" + "state" + "=" + BaseApplication.session.getUserDetails().get(KEY_STATE)
                + "&" + "country" + "=" + BaseApplication.session.getUserDetails().get(KEY_COUNTRY)
                + "&" + "zipCode" + "=" + BaseApplication.session.getUserDetails().get(KEY_ZIP_CODE);


        // ayman.json.JSONObject jsonBody = new ayman.json.JSONObject(obj);

        if(Constants.androiStudioMode.equals("debug")){
            Log.v(TAG, message);
        }
        final String requestBody = message;

        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_USER_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String AccessToken = response;
                if(Constants.androiStudioMode.equals("debug")){
                    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", AccessToken);
                }
                showProgress(false);
                Log.v("Json", "____ response update ____ "+AccessToken);
                AlertDialogManager.showAlertDialog(getActivity(),getResources().getString( R.string.app_name),getResources().getString(R.string.request_success),true,0);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                Log.e("/////// VOLLEY  ///// ", error.toString());
                //  AlertDialogManager.showAlertDialog(InscriptionActivity.this,getResources().getString( R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,0);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,0);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,0);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,0);


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
