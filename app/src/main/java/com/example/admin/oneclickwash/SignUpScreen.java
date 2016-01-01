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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.BL.SignUpScreenBL;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SignUpScreen extends AppCompatActivity {

    TextView tvTerm;
    Button allreadyaccount;
    Spinner cityName;
    EditText userPhone,userAddress,userPass,confirmPass,userEmail,userName;
    String phone,address,password,confirmPassword,email,name;
    boolean flag=true;
    Button signUp;
    int value;
    String otp;
    ProgressDialog pd;
    SignUpScreenBL signUpWebService;
    CheckBox cbTerm;

    String deviceId;
    GoogleCloudMessaging gcmObj;

    Context applicationContext;
    String gcmID;

    int xx,yy;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        applicationContext=getApplicationContext();

        // System.out.println("width" + width + "height" + height);

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

        allreadyaccount= (Button) findViewById(R.id.signIn_tv);
        signUp= (Button) findViewById(R.id.signup_button);
        cityName= (Spinner) findViewById(R.id.city_name);
        userPhone= (EditText) findViewById(R.id.signup_mobile);
        userAddress= (EditText) findViewById(R.id.edt_address);
        userPass= (EditText) findViewById(R.id.signup_password);
        confirmPass= (EditText) findViewById(R.id.confirm_password);
        userEmail= (EditText) findViewById(R.id.edt_email);
        userName= (EditText) findViewById(R.id.signup_name);
        tvTerm= (TextView) findViewById(R.id.tv_tc);
        cbTerm= (CheckBox) findViewById(R.id.check_box_term);
        pd=new ProgressDialog(SignUpScreen.this);

        try {
            Application.tracker().setScreenName("Signup Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Signup Screen Open")
                    .setCategory("Signup")
                    .setAction("UI OPEN")
                    .build());

        } catch (Exception e) {

        }


        deviceId=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_DEVICE_ID);
        signUpWebService=new SignUpScreenBL();

        gcmID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_GCM_ID);

        if(gcmID==null){
            if (checkPlayServices()) {
                registerInBackground();
            }
        }

        List<String> list=new ArrayList<>();
        list.add("City of services");
        list.add("Gurgaon");

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        cityName.setAdapter(adapter);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = userPhone.getText().toString();
                address = userAddress.getText().toString();
                email = userEmail.getText().toString();
                password = userPass.getText().toString();
                confirmPassword = confirmPass.getText().toString();
                name = userName.getText().toString();

                System.out.println("otp-------------->" + otp);


                if (checkAllfield()) {

                    if (password.equals(confirmPassword)) {

                        /*Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME,Constant.user_email,email);
                        Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.user_mobile,phone);
                        Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.otp, otp);*/
                        value = (int) ((Math.random() * 9000) + 1000);
                        otp = String.valueOf(value);
                        Constant.STR_OTP = otp;

                        Util.hideSoftKeyboard(SignUpScreen.this);

                        if (Util.isInternetConnection(SignUpScreen.this)) {
                            if(cbTerm.isChecked())
                            new SignUp().execute(email, phone, address, password, otp, name,deviceId,gcmID);
                        }
                        else
                            showDialogInternet(SignUpScreen.this);

                    } else {
                        Toast.makeText(getApplicationContext(), "password does not match", Toast.LENGTH_LONG).show();

                    }


                }

            }
        });


        allreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                    startActivity(intent);
                }

        });

        tvTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermAndCondition.class);
                startActivity(intent);
            }
        });

    }
    public boolean checkAllfield()
    {
        flag=true;

        if(name.length()==0){
            userName.setError("Mandatory field");
            flag=false;
        }

        if(phone.length()==0)
        {
            userPhone.setError("Mandatory field");
            flag=false;
        }


        if(phone.length()<10)
        {
            userPhone.setError("Enter 10 digit number");
            flag=false;
        }


        if(password.length()==0)
        {
            userPass.setError("Mandatory field");
            flag=false;
        }
        if(confirmPassword.length()==0)
        {
            confirmPass.setError("Mandatory field");
            flag=false;
        }

        if(address.length()==0)
        {
            userAddress.setError("Mandatory field");
            flag=false;
        }
       if(email.length()==0)
        {
            userEmail.setError("Mandatory field");
            flag=false;
        }
        if(!Util.isEmailValid(email))
        {
          userEmail.setError("Enter valid email id");
            flag=false;
        }

    return  flag;

    }



    private class SignUp extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {

            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            String result1=signUpWebService.signUpRecord(getApplicationContext(),params[0], params[1], params[2], params[3],params[4],params[5],params[6],params[7]);

            return result1;
        }


        @Override
        protected void onPostExecute(String s) {
          try {

                if (s.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)) {

                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), OtpScreen.class).putExtra("Mobile",phone).putExtra("Screen","Signup");
                    startActivity(intent);

                }
                else if (s.equalsIgnoreCase(Constant.WS_RESPONSE_Exist)) {
                    Snackbar snack = Snackbar.make(findViewById(R.id.login_root),
                            getResources().getString(R.string.mobile_no_exist),
                            Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.mobile_no_exist)).setActionTextColor(getResources().getColor(R.color.sky));
                    ViewGroup group = (ViewGroup) snack.getView();
                    TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    group.setBackgroundColor(getResources().getColor(R.color.sky));
                    snack.show();
                }
            }
            catch (NullPointerException e){
                    showDialogResponse(SignUpScreen.this);
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
        btnClosePopup.setText(getResources().getString(R.string._no_response_cancel));
        btnsave.setText(getResources().getString(R.string._no_internet_ok));

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
