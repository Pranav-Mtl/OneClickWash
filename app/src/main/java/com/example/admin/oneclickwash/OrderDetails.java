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
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BE.HomeScreenBE;
import com.example.admin.oneclickwash.BE.PlaceOrderBE;
import com.example.admin.oneclickwash.BE.SubscriptionDetailBE;
import com.example.admin.oneclickwash.BL.HomeScreenBL;
import com.example.admin.oneclickwash.BL.PlaceOrderBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener{

    TextView etMobile,etEmail,etName,etAddress,etServiceType,etDate,etTime;

    Button btnNext;

    HomeScreenBE objHomeScreenBE;
    HomeScreenBL objHomeScreenBL;
    SubscriptionDetailBE objSubscriptionDetailBE;

    ProgressDialog progressDialog;

    String userID;

    Intent intent;

    String strServiceType,strDate,strTime;

    PlaceOrderBL objPlaceOrderBL;

    PlaceOrderBE placeOrderBE;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        initialize();

        if(Util.isInternetConnection(OrderDetails.this))
        new GetUserDetails().execute(userID);
        else
            showDialogInternet(OrderDetails.this);
    }

    private void initialize(){

        etName= (TextView) findViewById(R.id.order_detail_name);
        etMobile= (TextView) findViewById(R.id.order_detail_mobile);
        etEmail= (TextView) findViewById(R.id.order_detail_email);
        etAddress= (TextView) findViewById(R.id.order_detail_address);
        etServiceType= (TextView) findViewById(R.id.order_detail_service);
        etDate= (TextView) findViewById(R.id.order_detail_date);
        etTime= (TextView) findViewById(R.id.order_detail_time);

        btnNext= (Button) findViewById(R.id.order_detail_next);
        btnNext.setOnClickListener(this);

        objPlaceOrderBL=new PlaceOrderBL();

        objHomeScreenBE=new HomeScreenBE();
        objHomeScreenBL=new HomeScreenBL();
        objSubscriptionDetailBE=new SubscriptionDetailBE();
        progressDialog=new ProgressDialog(OrderDetails.this);

        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_USER_ID);

        intent=getIntent();
        placeOrderBE= (PlaceOrderBE) intent.getSerializableExtra("PlaceOrderBE");


        etServiceType.setText("Monthly service");
        etDate.setText(placeOrderBE.getDate());
        etTime.setText(placeOrderBE.getTimeSlot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        popupSize();

        try {
            Application.tracker().setScreenName("Order Confirmation Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Order Confirmation Screen Open")
                    .setCategory("Order Confirmation")
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


            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_detail_next:
                new PlacedOrder().execute(userID);
                break;
        }
    }

    private class GetUserDetails extends AsyncTask<String,String,String> {

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
                etName.setText(objHomeScreenBE.getName());
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

    private class PlacedOrder extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objPlaceOrderBL.placeOrder(params[0],placeOrderBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) {
                    Toast.makeText(getApplicationContext(), "Order Successfully placed.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    finish();
                } else if(Constant.WS_RESPONSE_SCHEDULED.equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(), "Sorry, You can schedule only one pickup at a time.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    finish();

                }
                else
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_LONG).show();

            }catch (NullPointerException e){

            }
            catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }
        }
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
                                           finish();
                                       }
                                   }

        );


        dialog.show();
    }
}
