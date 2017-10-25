package com.example.macbook.box2homeclient.session;

/**
 * Created by macbook on 17/10/2017.
 */

public class Constants {

    private static final String baseUrl="http://api.box2home.xyz/api";
    public static final String graphBaseUrl="http://graph.facebook.com/";
    public static final String image_baseUrl="http://bl.box2home.xyz/web/Clients/";
    public static final String LOGIN_URL_FIRST = baseUrl+"/login_check";
    public static final String MOBILE_CALCUL_MONTANT_COURSE_URl = baseUrl+"/MobileCalculMontantCourse";
    public static final String FACEBOOK_LOGIN_URL_FIRST = baseUrl+"/LoginFacebookMobile";
    public static final String HISTORIQUE_URL = baseUrl+"/ClientMobileCourses";
    public static final String SIGNUP_URL = baseUrl+"/InscriptionClient";
    public static final String LOGIN_URL=baseUrl+"/LoginMobile";
    public static final String FORGET_PASSWORD_URL=baseUrl+"_/TestExistEmail";
    public static final String UPDATE_USER_PROFILE=baseUrl+"/UpdateClientMobileProfile";
    public static final String DECONNECT_URL=baseUrl+"_/DeconnectWeb";
    public static final String PAGE_FACEBOOK="https://www.facebook.com/Box2HomeFR/";


    //debug, release
    public static String androiStudioMode="release";

    public static final String KEY_EMAIL = "mail";
    public static final String KEY_EMAIL_CHANGE = "email";
    public static final String KEY_USERNAME= "username";
    public static final String KEY_ID= "id";
    public static final String KEY_ID_CONNECTED= "idConnected";
    public static final String KEY_DISTANCE= "distance";
    public static final String KEY_DRIVER_TYPE= "driverType";
    public static final String KEY_VEHICULE= "vehicule";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONFIRM_PASSWORD = "conf_password";
    public static final String KEY_FIRST_NAME = "firstname";
    public static final String KEY_LAST_NAME = "lastname";
    public static final String KEY_PHONE = "phone";
}
