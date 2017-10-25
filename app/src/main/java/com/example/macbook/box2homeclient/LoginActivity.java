package com.example.macbook.box2homeclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.macbook.box2homeclient.models.User;
import com.example.macbook.box2homeclient.session.Constants;
import com.example.macbook.box2homeclient.utils.AlertDialogManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.macbook.box2homeclient.session.Constants.KEY_EMAIL;
import static com.example.macbook.box2homeclient.session.Constants.KEY_EMAIL_CHANGE;
import static com.example.macbook.box2homeclient.session.Constants.KEY_FIRST_NAME;
import static com.example.macbook.box2homeclient.session.Constants.KEY_LAST_NAME;
import static com.example.macbook.box2homeclient.session.Constants.KEY_PASSWORD;
import static com.example.macbook.box2homeclient.session.Constants.KEY_PHONE;
import static com.example.macbook.box2homeclient.session.Constants.KEY_USERNAME;
import static com.example.macbook.box2homeclient.session.Constants.graphBaseUrl;
import static com.example.macbook.box2homeclient.session.SessionManager.KEY_Acesstoken;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity   {




    private static final String TAG = LoginActivity.class.getSimpleName();
    public static String AccessTOken = null;

    private BaseApplication controller;

    /**
     * A dummy authentication store containing known User names and passwords.

     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private TextView txt_pwd;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
  //  private LoginButton loginButton;
    private CallbackManager callbackManager;
    LoginResult _loginResult;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (_loginResult!=null) {
            GraphRequest request = GraphRequest.newMeRequest(_loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                //txtState.setText("Hi, " + object.getString("name"));
                                Log.e("LoginAc", "User object: " + object.toString());
                                Log.e("LoginAc", "User name: " + object.getString("name"));
                                String name = object.getString("name");
                                String email = object.getString("email");
                                String urlImage = graphBaseUrl+ object.getString("id") + "/picture?type=normal";
                                Log.e("LoginAc", "User urlImage: " + urlImage);

                                testIfMailExist(email,name,name,"","");
                                //TODO
                                //  Log.e("LoginAc","Auth Token: " + object.getString("name"));
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();

            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        setContentView(R.layout.activity_login);

        //login connect



        // If using in a fragment
      //  loginButton.setFragment(this);
        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                _loginResult=loginResult;
                // App code
                Toast toast = Toast.makeText(LoginActivity.this, "Logged In = "+loginResult.toString(), Toast.LENGTH_SHORT);
                toast.show();
                Log.e("LoginAc","User ID: " + loginResult.getAccessToken().getUserId() );
                Log.e("LoginAc","Auth Token: " + loginResult.getAccessToken().getToken() );
               // Log.e("LoginAc","Auth Token: " + loginResult.getAccessToken().ge() );



            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

     /*
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));


//                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(
//                                    JSONObject object,
//                                    GraphResponse response) {
//                                if (object != null) {
//                                    Log.d("Me Request",object.toString());
//                                    Toast t = Toast.makeText(getActivity(), object.toString(), Toast.LENGTH_SHORT);
//                                    t.show();
//                                }
//
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,link,email");
//                request.setParameters(parameters);
//                request.executeAsync();
            }
        });
*/
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        txt_pwd = (TextView) findViewById(R.id.txt_pwd);
        controller = new BaseApplication();

        txt_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               // finish();
                Intent intent = new Intent(LoginActivity.this,MotDePasseOublieActivity.class);
                startActivity(intent);
            }
        });


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button _email_sign_up_button = (Button) findViewById(R.id.email_sign_up_button);
        _email_sign_up_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,InscriptionActivity.class);
                startActivity(intent);
            }
        });
        Button _facebook_sign_in_button = (Button) findViewById(R.id.facebook_sign_in_button);
        _facebook_sign_in_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });

    }



    /**
     * Callback received when a permissions request has been completed.
     */



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the User entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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
            LoginFirst(email,password);
        }
    }

    private boolean isEmailValid(String email) {
         return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
         return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


          //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            if (show) mLoginFormView.setVisibility(View.GONE);


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
            if (show) mLoginFormView.setVisibility(View.GONE);
          //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the User.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            LoginFirst(mEmail,mPassword);

             return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);


        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    private void LoginFirst(final String email, final String password) {


        String message;
        String url;


        if (password.isEmpty()){
            message = "username" + "=" + email
                    + "&" + "password" + "=" + password;
            url=Constants.FACEBOOK_LOGIN_URL_FIRST;

        }else{
            message = "_username" + "=" + email
                    + "&" + "_password" + "=" + password;
            url=Constants.LOGIN_URL_FIRST;
        }



        // ayman.json.JSONObject jsonBody = new ayman.json.JSONObject(obj);

        if(Constants.androiStudioMode.equals("debug")){
            Log.v(TAG, message);
        }
        final String requestBody = message;

        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String AccessToken = response;
                if(Constants.androiStudioMode.equals("debug")){
                    Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", AccessToken);
                }

                Log.v("Json","AccessToken AccessToken " +AccessToken);

                try {
                    JSONObject person = new JSONObject(AccessToken);
                    String token = person.getString("token");
                    // String id=person.getString("");

                    AccessTOken = token;
                    if(Constants.androiStudioMode.equals("debug")){
                        Log.v("jsonparser", token);
                    }
                    if (AccessTOken != null) {

                        BaseApplication.session.createLoginSession(password, email, AccessTOken);
                        BaseApplication.session.saveUserPwd(password);

                        LoginSecond(email,password);

                        if(Constants.androiStudioMode.equals("debug")) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                Log.e("/////// VOLLEY  ///// ", error.toString());

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);


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

    private void LoginSecond(final String email, final String password) {

        String message = KEY_USERNAME + "=" + email
                + "&" + KEY_PASSWORD + "=" + password;


        // ayman.json.JSONObject jsonBody = new ayman.json.JSONObject(obj);

        if(Constants.androiStudioMode.equals("debug")){
            Log.v(TAG, message);}
        final String requestBody = message;

        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String AccessToken = response;
                if(Constants.androiStudioMode.equals("debug")){
                    Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", AccessToken);
                }

                Log.v("Json","AccessToken AccessToken " +AccessToken);

                try {
                    JSONObject person = (new JSONObject(AccessToken));
                    boolean success = person.getBoolean("success");
                    // String id=person.getString("");
                    if (success) {

                        JSONObject tokenObject=person.getJSONObject("token");
                        JSONObject usrObject = tokenObject.getJSONObject("user");


                        BaseApplication.session.createLoginSession(password, email, "");
                        BaseApplication.session.saveUserPwd(password);

                        User user = new User() ;
                        user.setId(usrObject.getString("id"));
                        user.setAddress(usrObject.getString("address"));
                        user.setUsername(usrObject.getString("username"));
                        user.setUsername_canonical(usrObject.getString("username_canonical"));
                        user.setEmail(usrObject.getString("email"));
                        user.setEmail_canonical(usrObject.getString("email_canonical"));
                        user.setFirst_name(usrObject.getString("first_name"));
                        user.setLast_name(usrObject.getString("last_name"));
                        user.setNom_societe(usrObject.getString("nom_societe"));
                        user.setPhone(usrObject.getString("phone"));
                        user.setPhoto(usrObject.getString("photo"));

                        user.setState(usrObject.getString("state"));
                        user.setCountry(usrObject.getString("country"));
                        user.setZip_code(usrObject.getString("zip_code"));
                        user.setType(usrObject.getString("type"));
                        user.setStatut(usrObject.getString("statut"));
                        user.setDate_creation(usrObject.getString("date_creation"));
                        BaseApplication.session.setLoggedUser(user);
                        Log.e(TAG,"getUser getLoggedUser ***** "+BaseApplication.session.getLoggedUser().getFirst_name());
                        BaseApplication.session.createLoginSession(password, email, AccessTOken);
                        BaseApplication.session.saveUserPwd(password);
                        BaseApplication.session.setUserDetails(user.getId(),user.getAddress(),user.getUsername(),
                                user.getUsername_canonical(),user.getEmail(),user.getEmail_canonical(),user.getFirst_name(),
                                user.getLast_name(),user.getNom_societe(),user.getPhone(),user.getPhoto(),
                                user.getState(),user.getCountry(),user.getZip_code(),user.getType(),user.getStatut(),user.getDate_creation());

                        if(Constants.androiStudioMode.equals("debug")) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }

                    showProgress(false);

                } catch (JSONException e) {
                    showProgress(false);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                Log.e("/////// VOLLEY  ///// ", error.toString());
               // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);


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

    void getKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash********* :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void testIfMailExist(final String mail,final String name,final String lastName,final String tel,final String pwd){
        {

            //TODO: web service send-email return HTML format => ajouter popup webservice non disponible
            showProgress(true);
            String message = KEY_EMAIL_CHANGE + "=" + mail ;

            final String requestBody = message;

            StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, Constants.FORGET_PASSWORD_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String AccessToken = response;
                    if(Constants.androiStudioMode.equals("debug")){
                        Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        Log.v("Json", AccessToken);
                    }
                    showProgress(false);

                    if (response.equals("true")){
                        //TODO call facebook login ws

                        LoginFirst(mail,"");


                    }else{
                        //TODO call inscription ws
                        signUp(name,lastName,tel,mail,pwd);


                    }

                   // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString( R.string.app_name),getResources().getString(R.string.request_success),true,3);

                    Log.v("Json","AccessToken AccessToken " +AccessToken);




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showProgress(false);
                    Log.e("/////// VOLLEY  ///// ", error.toString());

                    if (error instanceof AuthFailureError) {
                        AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                    } else if (error instanceof ServerError) {
                        AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                    } else if (error instanceof NetworkError) {
                        AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                        AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);


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
                    Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.v("Json", AccessToken);
                }
                showProgress(false);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
               // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString( R.string.app_name),getResources().getString(R.string.success_signup),true,3);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                Log.e("/////// VOLLEY  ///// ", error.toString());
                //  AlertDialogManager.showAlertDialog(InscriptionActivity.this,getResources().getString( R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);


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

