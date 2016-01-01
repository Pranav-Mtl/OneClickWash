package com.example.admin.oneclickwash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
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
import com.example.admin.oneclickwash.BL.OtpVerificationBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;

public class OtpScreen extends AppCompatActivity {


    TextView otpText;
    EditText otpEdittext;
    String otpValue;
    Button otpVerifybtn;
    String userId;
    ProgressDialog pd;
    OtpVerificationBL otpVerificationBL;
    String mobile;
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    String message="We are trying to auto verify your mobile number";
    Button btnResendOTP;
    String screenName;

    int xx,yy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        otpEdittext= (EditText) findViewById(R.id.otp_edit);

        otpVerifybtn= (Button) findViewById(R.id.otp_button);

        btnResendOTP= (Button) findViewById(R.id.btn_resend_otp);
        otpText= (TextView) findViewById(R.id.otp_text);
        otpVerificationBL=new OtpVerificationBL();
        pd=new ProgressDialog(OtpScreen.this);

        popupSize();

        mobile=getIntent().getStringExtra("Mobile");
        screenName=getIntent().getStringExtra("Screen");
        userId=Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_USER_ID);

        otpText.setText(message + " " + mobile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        registerReceiver(receiver, filter);

        if(screenName.equalsIgnoreCase("Login")){
            int value = (int) ((Math.random() * 9000) + 1000);
            String otp = String.valueOf(value);
            Constant.STR_OTP = otp;

            new ResendOTP().execute(mobile,otp);
        }


        otpVerifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otpValue = otpEdittext.getText().toString();

                Util.hideSoftKeyboard(OtpScreen.this);
                if(otpValue.length()!=0) {
                    if (Constant.STR_OTP.equals(otpValue)) {
                        if (Util.isInternetConnection(OtpScreen.this))
                        new OtpVerify().execute(mobile);
                        else
                            showDialogInternet(OtpScreen.this);
                    } else {
                        Snackbar snack = Snackbar.make(findViewById(R.id.otp_root),
                                getResources().getString(R.string.otp_incorrect),
                                Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.otp_incorrect)).setActionTextColor(getResources().getColor(R.color.sky));
                        ViewGroup group = (ViewGroup) snack.getView();
                        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        group.setBackgroundColor(getResources().getColor(R.color.sky));
                        snack.show();
                    }
                }else {
                    otpEdittext.setError("Required");
                }

            }
        });



        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = (int) ((Math.random() * 9000) + 1000);
                String otp = String.valueOf(value);
                Constant.STR_OTP = otp;

                new ResendOTP().execute(mobile,otp);

            }
        });

    }

    private void popupSize(){
        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=1000){
            xx=700;
            yy=650;
        }
        else if(width>=700)
        {
            xx=550;
            yy=500;
        }
        else
        {
            xx=450;
            yy=400;
        }

    }

    private class OtpVerify extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {

            pd.show();
            pd.setMessage("Loading");
            pd.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params)
        {
            String result=otpVerificationBL.verifyOtp(params[0],getApplicationContext());

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                if (s.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.otp_success),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            catch (NullPointerException e){

            }
            catch(Exception e){

            }finally {
                pd.dismiss();
            }

        }

    }

    @Override
    public void onBackPressed() {

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        final SmsManager sms = SmsManager.getDefault();

        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        message=message.substring(0,4);


                       /* String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                        String msg[]=message.split(".",1);
                        int length=msg[0].length();
                        Log.d("Length",length+"");
                        Log.d("Start",msg[0].charAt(length-5)+"");
                        Log.d("End",msg[0].charAt(length-1)+"");

                        message=msg[0].substring(length-5, length-1);*/


                        //etOTP.setText(message.trim());



                        // Show Alert
                        int duration = Toast.LENGTH_LONG;

                       /* etOTP.setText(message);
                        etOTP.setSelection(etOTP.getText().length());*/

                        if(message.equals(Constant.STR_OTP)){
                            new OtpVerify().execute(mobile);
                        }




                       /* Toast toast = Toast.makeText(context,
                                "senderNum: " + senderNum + ", message: " + message, duration);
                        toast.show();*/

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }
        }
    };


    @Override
    protected void onResume() {

        registerReceiver(receiver,filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    private class ResendOTP extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String result=otpVerificationBL.getResend(params[0],params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(),"OTP sent successfully",Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                pd.dismiss();
            }
        }
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

}
