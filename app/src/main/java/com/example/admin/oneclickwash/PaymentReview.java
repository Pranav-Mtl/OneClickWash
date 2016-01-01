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
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BE.PaymentReviewBE;
import com.example.admin.oneclickwash.BL.PaymentReviewBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

public class PaymentReview extends AppCompatActivity implements View.OnClickListener {

    TextView tvSubID,tvPackageName,tvName,tvAmount,tvPickup,tvCloth,tvWallet,tvTotal;
    Button btnPayment,btnCancel;
    String subscriptionID;
    PaymentReviewBE objPaymentReviewBE;
    PaymentReviewBL objPaymentReviewBL;

    ProgressDialog progressDialog;

    String userID;

    int packageAmount ;
    int currentBalance;

    int totalPayble;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_review);
        initialize();

        if(Util.isInternetConnection(PaymentReview.this))
        new GetSubscriptionData().execute(subscriptionID,userID);
        else
            showDialogInternet(PaymentReview.this);
    }

    private void initialize(){
        tvSubID= (TextView) findViewById(R.id.review_id);
        tvPackageName= (TextView) findViewById(R.id.review_package);
        tvName= (TextView) findViewById(R.id.review_name);
        tvAmount= (TextView) findViewById(R.id.review_amount);
        tvPickup= (TextView) findViewById(R.id.review_pickup);
        tvCloth= (TextView) findViewById(R.id.review_cloth);
        tvWallet= (TextView) findViewById(R.id.review_wallet);
        tvTotal= (TextView) findViewById(R.id.review_total);

        btnPayment= (Button) findViewById(R.id.review_btn_payment);
        btnCancel= (Button) findViewById(R.id.review_btn_cancel);

        subscriptionID=getIntent().getStringExtra("SubscriptionID");
        userID= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_USER_ID);

        btnPayment.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        progressDialog=new ProgressDialog(PaymentReview.this);

        objPaymentReviewBE=new PaymentReviewBE();
        objPaymentReviewBL=new PaymentReviewBL();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        popupSize();

        try {
            Application.tracker().setScreenName("Payment Review Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Payment Review Screen Open")
                    .setCategory("Payment Review")
                    .setAction("UI OPEN")
                    .build());

        } catch (Exception e) {

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


            //onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.review_btn_payment:
                if(totalPayble>0) {
                    Intent intent = new Intent(getApplicationContext(), PaymentWebview.class);
                    intent.putExtra("Amount", totalPayble);
                    intent.putExtra("SubscriptionID", subscriptionID);
                    intent.putExtra("Wallet", objPaymentReviewBE.getWallet());
                    startActivity(intent);
                }
                else {
                    new SubscriptionSuccess().execute(objPaymentReviewBE.getPackageID(),userID);
                }
                break;
            case R.id.review_btn_cancel:
                new CancelSubscription().execute(subscriptionID);
                break;
        }
    }

    private class GetSubscriptionData extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objPaymentReviewBL.getSubscriptiondata(params[0],params[1],objPaymentReviewBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) {
                    tvSubID.setText("Subscription ID: OCW" + subscriptionID);
                    tvPackageName.setText(objPaymentReviewBE.getPackageName());
                    tvAmount.setText("₹" + objPaymentReviewBE.getAmount());
                    tvPickup.setText(objPaymentReviewBE.getPickup());
                    tvCloth.setText(objPaymentReviewBE.getCloth());
                    tvWallet.setText("₹"+objPaymentReviewBE.getWallet());

                    findBalace();

                }
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }


    private class CancelSubscription extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objPaymentReviewBL.cancelSubscription(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) {
                    Toast.makeText(getApplicationContext(), "Subscription Cancelled Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Something went wrong? Please try again.", Toast.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void findBalace(){
        try {
            packageAmount = Integer.valueOf(objPaymentReviewBE.getAmount());
             currentBalance = Integer.valueOf(objPaymentReviewBE.getWallet());

             totalPayble = packageAmount - currentBalance;

            tvTotal.setText("₹" + totalPayble);
        }catch (Exception e){

        }

    }


    private class SubscriptionSuccess extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params) {
            String result=objPaymentReviewBL.subscriptionSuccess(params[0],params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) {
                    Toast.makeText(getApplicationContext(), "Subscription Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Something went wrong? Please try again.", Toast.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
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

}
