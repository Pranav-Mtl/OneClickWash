package com.example.admin.oneclickwash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BE.HomeScreenBE;
import com.example.admin.oneclickwash.BE.SubscriptionConfirmedBE;
import com.example.admin.oneclickwash.BE.SubscriptionDetailBE;
import com.example.admin.oneclickwash.BL.HomeScreenBL;
import com.example.admin.oneclickwash.BL.SubscriptionConfirmedBL;
import com.example.admin.oneclickwash.BL.ValidateReferalBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;

import java.util.concurrent.ExecutionException;

public class SubscriptionConfirmed extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    String packageID,packagePrice,packageName;
    TextView etUserName,etAddress;
    TextView etMobile,etEmail,etPackageName,etPackagePrice;
    TextView tvApplied;
    Button btnChangeAddress;

    HomeScreenBE objHomeScreenBE;
    HomeScreenBL objHomeScreenBL;
    SubscriptionDetailBE objSubscriptionDetailBE;

    String userId;

    ProgressDialog progressDialog;

    SubscriptionConfirmedBL objSubscriptionConfirmedBL;

    ValidateReferalBL objValidateReferalBL;

    Button btnPackageNext,btnReferral;

    int xx,yy;

    String deviceID;


    SubscriptionConfirmedBE objSubscriptionConfirmedBE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_confirmed);
        initialize();

        
        if(Util.isInternetConnection(SubscriptionConfirmed.this))
        new GetUserDetails().execute(userId);
        else
            showDialogInternet(SubscriptionConfirmed.this);
    }

    private void initialize(){
        intent=getIntent();
        packageID=intent.getExtras().get("PackageId").toString();
        packagePrice=intent.getExtras().get("Price").toString();
        packageName=intent.getExtras().get("Package").toString();

        etUserName= (TextView) findViewById(R.id.package_username);
        etMobile= (TextView) findViewById(R.id.package_mobile);
        etEmail= (TextView) findViewById(R.id.package_email);
        etAddress= (TextView) findViewById(R.id.package_address);
        etPackageName= (TextView) findViewById(R.id.package_name);
        etPackagePrice= (TextView) findViewById(R.id.package_price);
        btnPackageNext= (Button) findViewById(R.id.package_next);
        btnReferral= (Button) findViewById(R.id.package_referral);
        tvApplied= (TextView) findViewById(R.id.tv_message_applied);

        btnPackageNext.setOnClickListener(this);
        btnReferral.setOnClickListener(this);

        objSubscriptionConfirmedBE=new SubscriptionConfirmedBE();
        // btnChangeAddress= (Button) findViewById(R.id.confirm_change_address);

        //btnChangeAddress.setOnClickListener(this);

        etPackageName.setText(packageName);
        etPackagePrice.setText(packagePrice);

        objHomeScreenBE=new HomeScreenBE();
        objHomeScreenBL=new HomeScreenBL();
        objSubscriptionDetailBE=new SubscriptionDetailBE();
        objValidateReferalBL=new ValidateReferalBL();

        objSubscriptionConfirmedBL=new SubscriptionConfirmedBL();
        progressDialog=new ProgressDialog(SubscriptionConfirmed.this);

        userId= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_USER_ID);
        deviceID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_DEVICE_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        popupSize();


    }

    private void popupSize(){
        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=1000 && height>=1500){
            xx=700;
            yy=650;
        }
        else if(width>=700 && height>=1000)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {


            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.package_next:
              // new SubscriptionDone().execute(userId,packageID);
                objSubscriptionConfirmedBE.setUserID(userId);
                objSubscriptionConfirmedBE.setSubscriptionID(packageID);
                showDialogPayment(SubscriptionConfirmed.this);
                break;
            case R.id.package_referral:
                showDialogReferal(SubscriptionConfirmed.this);
                break;
        }
    }

    private class GetUserDetails extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objHomeScreenBL.getRecord(params[0],objHomeScreenBE,objSubscriptionDetailBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                etUserName.setText(objHomeScreenBE.getName());
                etEmail.setText(objHomeScreenBE.getEmail());
                etMobile.setText(objHomeScreenBE.getMobile());
                etAddress.setText(objHomeScreenBE.getAddress());
            }
            catch (NullPointerException e){

            }
            catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }


        }
    }

    private class SubscriptionDone extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objSubscriptionConfirmedBL.subscriptionDone(objSubscriptionConfirmedBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    if(objSubscriptionConfirmedBE.getPaymentType().equalsIgnoreCase("Cash")){
                        Toast.makeText(getApplicationContext(),"Subscription Done. Please Place Your Order.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), PaymentReview.class).putExtra("SubscriptionID",objSubscriptionConfirmedBL.subscriptionID));
                        finish();
                    }
                }
            }catch (NullPointerException e){

            }catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
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
        btnClosePopup.setText(getResources().getString(R.string._no_internet_cancel));
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
                                           finish();

                                       }
                                   }

        );


        dialog.show();
    }


    /* popup for add referal */
    private void showDialogReferal(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvTitle;
        Button btnClosePopup,btnsave;
        final EditText tvMsg;
        final TextView popupMsg;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_referral_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (EditText) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);
        popupMsg= (TextView) dialog.findViewById(R.id.popup_msg);

        tvTitle.setText(getResources().getString(R.string._referral_title));
        //tvMsg.setText(getResources().getString(R.string._referral_message));
        btnClosePopup.setText(getResources().getString(R.string._referral_cancel));
        btnsave.setText(getResources().getString(R.string._referral_save));


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           if(tvMsg.getText().toString().length()>0) {
                                               try{
                                               String result = new ValidateReferral().execute(deviceID, tvMsg.getText().toString()).get();
                                               if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(result)) {
                                                   btnReferral.setEnabled(false);
                                                   tvApplied.setVisibility(View.VISIBLE);
                                                   objSubscriptionConfirmedBE.setReferalCode(tvMsg.getText().toString());
                                                   dialog.dismiss();
                                               }
                                                   else {
                                                   popupMsg.setVisibility(View.VISIBLE);
                                               }
                                           }
                                          catch (Exception e){

                                          }

                                           }


                                       }
                                   }

        );


        dialog.show();
    }

    /* popup for add referal */
    private void showDialogPayment(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvTitle;
        Button btnClosePopup,btnsave;
        RadioGroup rgPayment;
        final RadioButton rbCash,rbOnline;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_payment_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        rgPayment= (RadioGroup) dialog.findViewById(R.id.group_payment);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);
        rbCash= (RadioButton) dialog.findViewById(R.id.payment_cash);
        rbOnline= (RadioButton) dialog.findViewById(R.id.payment_online);

        tvTitle.setText(getResources().getString(R.string._payment_title));
        //tvMsg.setText(getResources().getString(R.string._referral_message));
        btnClosePopup.setText(getResources().getString(R.string._payment_cancel));
        btnsave.setText(getResources().getString(R.string._payment_save));


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           if(rbCash.isChecked())
                                           {
                                               objSubscriptionConfirmedBE.setPaymentType("cash");
                                               objSubscriptionConfirmedBE.setDeviceID(deviceID);
                                               new SubscriptionDone().execute();
                                               dialog.dismiss();
                                           }else if(rbOnline.isChecked()){
                                               objSubscriptionConfirmedBE.setPaymentType("online");
                                               new SubscriptionDone().execute();
                                               dialog.dismiss();
                                           }


                                       }
                                   }

        );


        dialog.show();
    }

    private class ValidateReferral extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            String status = objValidateReferalBL.getCode(params[0], params[1]);
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
