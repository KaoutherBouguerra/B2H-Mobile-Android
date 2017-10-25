package com.example.macbook.box2homeclient.application;


import android.app.Application;
import android.os.SystemClock;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.macbook.box2homeclient.models.User;
import com.example.macbook.box2homeclient.session.SessionManager;

/**
 * Created by macbook on 16/10/2017.
 */

public class BaseApplication extends Application {
   public static SessionManager session;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    private static BaseApplication mInstance;
    public static final String TAG = BaseApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        session = new SessionManager(getApplicationContext());
      //  SystemClock.sleep(3000);
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
