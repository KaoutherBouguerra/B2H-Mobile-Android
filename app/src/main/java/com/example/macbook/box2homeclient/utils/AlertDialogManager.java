package com.example.macbook.box2homeclient.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.macbook.box2homeclient.LoginActivity;
import com.example.macbook.box2homeclient.MainActivity;
import com.example.macbook.box2homeclient.R;
import com.example.macbook.box2homeclient.session.SessionManager;

public class AlertDialogManager {
    static SessionManager sess;
    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     *                - pass null if you don't want icon
     */
    @SuppressWarnings("deprecation")
    public static void showAlertDialog(final Context context, String title, String message,
                                       Boolean status, final int action) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        sess=new SessionManager(context);
        // Setting Dialog Title
        alertDialog.setTitle(title);


        // Setting Dialog Message
        alertDialog.setMessage(message);


        if (status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               switch (action){
                   case 1:
                       context.startActivity(new Intent(context, MainActivity.class));
                       alertDialog.dismiss();
                       break;
                   case 2:
                       context.startActivity(new Intent(context, MainActivity.class));
                       alertDialog.dismiss();
                       break;
                   case 3:
                       context.startActivity(new Intent(context, LoginActivity.class));
                       alertDialog.dismiss();
                       break;
                   case 4:
                    //   context.startActivity(new Intent(context, ServiceProviderReg.class));
                       alertDialog.dismiss();
                       break;
                   case 5:
                       context.startActivity(new Intent(context,LoginActivity.class));
                       sess.logoutUser();
                       alertDialog.dismiss();
                       break;

                   default:
                           alertDialog.dismiss();
                           break;

               }

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}