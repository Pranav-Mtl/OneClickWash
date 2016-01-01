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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BL.BookingRateBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;

public class BookingRate extends AppCompatActivity implements View.OnClickListener {

    TextView tvID,tvService;
    RatingBar ratingBar;
    EditText etMessage;
    Button btnSend;
    String bookingID;
    String bookingService;
   Intent intent;

    String userId;

    BookingRateBL objBookingRateBL;

    ProgressDialog progressDialog;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_rate);
        initialize();
    }

    private void initialize(){
        tvID= (TextView) findViewById(R.id.rate_id);
        tvService= (TextView) findViewById(R.id.rate_service);
        ratingBar= (RatingBar) findViewById(R.id.feedback_rating);
        etMessage= (EditText) findViewById(R.id.rate_message);
        btnSend= (Button) findViewById(R.id.rate_send);

        progressDialog=new ProgressDialog(BookingRate.this);
        objBookingRateBL=new BookingRateBL();
        intent=getIntent();
        bookingID=intent.getStringExtra("Id");
        bookingService=intent.getStringExtra("Service");

        tvID.setText("Booking ID: OCW"+bookingID);
        tvService.setText("Service Type: "+bookingService);

        btnSend.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        userId= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_USER_ID);
        popupSize();
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
            case R.id.rate_send:
                int rate=(int)ratingBar.getRating();
                String comment=etMessage.getText().toString();
                if(Util.isInternetConnection(BookingRate.this))
                new GiveRating().execute(userId,bookingID,rate+"",comment);
                else
                showDialogInternet(BookingRate.this);
                break;
        }
    }

    private class GiveRating extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            String result=objBookingRateBL.getRaingdata(params[0], params[1], params[2], params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) {
                    Toast.makeText(getApplicationContext(), "Booking Rated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "You have already rated this booking.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            catch (NullPointerException e){}
            catch (Exception e){}
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
