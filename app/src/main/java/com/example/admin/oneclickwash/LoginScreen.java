package com.example.admin.oneclickwash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BL.LoginScreenBL;
import com.example.admin.oneclickwash.BL.SignUpScreenBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class LoginScreen extends AppCompatActivity {


    Button login;
    TextView signUp,forgot;
    boolean flag = true;
    EditText userphone, userpassword;
    String phone, password;
    LoginScreenBL loginScreenBL;
    ProgressDialog pd;

    String deviceId;
    GoogleCloudMessaging gcmObj;

    Context applicationContext;
    String gcmID;

    int xx,yy;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);
        applicationContext=getApplicationContext();
        if(width>=1000 && height>=1500){
            xx=700;
            yy=800;
        }
        else if(width>=700 && height>=1000)
        {
            xx=500;
            yy=500;
        }
        else
        {
            xx=400;
            yy=500;
        }

        try {
            Application.tracker().setScreenName("Login Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Login Screen Open")
                    .setCategory("Login")
                    .setAction("UI OPEN")
                    .build());

        } catch (Exception e) {

        }


        login = (Button) findViewById(R.id.signIn_button);
        signUp = (TextView) findViewById(R.id.signUp_tv);
        userphone = (EditText) findViewById(R.id.edt_mobile);
        userpassword = (EditText) findViewById(R.id.edt_password);
        forgot= (TextView) findViewById(R.id.login_forgot);
        pd=new ProgressDialog(LoginScreen.this);
        loginScreenBL =new LoginScreenBL();

        deviceId=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_DEVICE_ID);


        gcmID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_GCM_ID);

        if(gcmID==null){
            if (checkPlayServices()) {
                registerInBackground();
            }
        }


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone=userphone.getText().toString();
                password=userpassword.getText().toString();

                if(checkAllfield()) {
                    Util.hideSoftKeyboard(LoginScreen.this);
                    if(Util.isInternetConnection(LoginScreen.this))
                    new LoginPass().execute(phone,password,gcmID,deviceId);
                    else
                        showDialogInternet(LoginScreen.this);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignUpScreen.class);
                startActivity(intent);

            }
        });
    }

    private  boolean checkAllfield()
    {

        if(phone.length()==0)
        {
            userphone.setError("Mandatory field");
            flag=false;
        }
        else if(phone.length()!=10)
        {
            userphone.setError("Invalid Mobile Number");
            flag=false;
        }

        if(password.length()==0)
        {
            userpassword.setError("Mandatory field");
            flag=false;
        }

        return  flag;

    }


    private class LoginPass extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            String result= loginScreenBL.loginRecord(params[0],params[1],params[2],params[3],getApplicationContext());

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                if (s.equalsIgnoreCase(Constant.WS_RESPONSE_OTP)) {
                    Toast.makeText(getApplicationContext(),"Verify  Mobile number",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), OtpScreen.class).putExtra("Mobile",phone).putExtra("Screen","Login");
                    startActivity(intent);
                }
                else if (s.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)) {
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    startActivity(intent);
                }
                else if (s.equalsIgnoreCase(Constant.WS_RESPONSE_FAILURE)) {
                    Snackbar snack = Snackbar.make(findViewById(R.id.login_root),
                            getResources().getString(R.string.signin_failure),
                            Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.signin_failure)).setActionTextColor(getResources().getColor(R.color.sky));
                    ViewGroup group = (ViewGroup) snack.getView();
                    TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    group.setBackgroundColor(getResources().getColor(R.color.sky));
                    snack.show();
                }
            }
            catch (NullPointerException e){

            }
            catch (Exception e){

            }
            finally {
                pd.dismiss();
            }

        }
    }


    /* popup for no server response */
    private void showDialogResponse(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText(getResources().getString(R.string._no_response_title));
        tvMsg.setText(getResources().getString(R.string._no_response_message));
        btnClosePopup.setText(getResources().getString(R.string._no_response_cancel));
        btnsave.setText(getResources().getString(R.string._no_response_save));


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {


                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    /* popup for no internet */
    private void showDialogInternet(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText(getResources().getString(R.string._no_internet_title));
        tvMsg.setText(getResources().getString(R.string._no_internet_message));
        btnClosePopup.setText(getResources().getString(R.string._no_internet_cancel));
        btnsave.setText(getResources().getString(R.string._no_internet_save));

        btnClosePopup.setVisibility(View.GONE);
        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {


                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    @Override
    public void onBackPressed() {

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                finish();
            }
            return false;
        } else {

        }
        return true;
    }

    /*--------------------------GCM KEY ----------------------------------------*/
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    gcmID = gcmObj
                            .register(Constant.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + gcmID;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(gcmID)) {
                    //Toast.makeText(getApplicationContext(),"GSM"+gcmID,Toast.LENGTH_SHORT).show();
                    Util.setSharedPrefrenceValue(applicationContext,Constant.PREFS_NAME,Constant.SP_GCM_ID,gcmID);
                } else {
                    /*Toast.makeText(
                       applicationContext,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();*/
                }
            }
        }.execute(null, null, null);
    }
}

