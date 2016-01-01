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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BE.BookingDetailBE;
import com.example.admin.oneclickwash.BL.BookingDetailBL;
import com.example.admin.oneclickwash.BL.BookingListBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BookingDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tvOrderId,tvName,tvMobile,tvAddress,tvService,tvStatus;
    TextView tvPickDate,tvPickTime,tvDropTime,tvDropDate,tvPaid,tvFree;

    Button btnRate,btnScheduleDrop,btnReScheduleDrop;

    BookingDetailBE objBookingDetailBE;
    BookingDetailBL objBookingDetailBL;

    Intent intent;

    String bookingId,userId,window;
    int windowTime,windowDrop;
    String orderType;

    ProgressDialog progressDialog;

    BookingListBL objBookingListBL;

    LinearLayout llReschedule,btnRechedule,btnCancel,llScheduleDrop,llReScheduleDrop;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormatTime = new SimpleDateFormat("K:mm a");
    Calendar cal;

    int xx,yy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        initialize();

        if(Util.isInternetConnection(BookingDetail.this))
            new GetBookingDetail().execute(bookingId);
        else
            showDialogInternet(BookingDetail.this);
    }

    private void initialize(){
        tvOrderId= (TextView) findViewById(R.id.detail_book_id);
        tvName= (TextView) findViewById(R.id.detail_book_name);
        tvMobile= (TextView) findViewById(R.id.detail_book_mobile);
        tvAddress= (TextView) findViewById(R.id.detail_book_address);
        tvService= (TextView) findViewById(R.id.detail_book_type);
        tvStatus= (TextView) findViewById(R.id.detail_book_status);
        tvPickDate= (TextView) findViewById(R.id.detail_book_pick_date);
        tvPickTime= (TextView) findViewById(R.id.detail_book_pick_time);
        tvDropTime= (TextView) findViewById(R.id.detail_book_drop_time);
        tvDropDate= (TextView) findViewById(R.id.detail_book_drop_date);
        tvPaid= (TextView) findViewById(R.id.detail_book_paid_clothes);
        tvFree= (TextView) findViewById(R.id.detail_book_free_clothes);
        btnRate= (Button) findViewById(R.id.detail_rate);
        btnScheduleDrop= (Button) findViewById(R.id.detail_btn_schedule_drop);
        btnReScheduleDrop= (Button) findViewById(R.id.detail_btn_reschedule_drop);

        llReschedule= (LinearLayout) findViewById(R.id.detail_ll_reschedule);
        btnRechedule= (LinearLayout) findViewById(R.id.detail_btn_reschedule);
        btnCancel= (LinearLayout) findViewById(R.id.detail_btn_cancel);
        llScheduleDrop= (LinearLayout) findViewById(R.id.detail_layout_dropoff);
        llReScheduleDrop= (LinearLayout) findViewById(R.id.detail_layout_redropoff);


        objBookingDetailBE=new BookingDetailBE();
        objBookingDetailBL=new BookingDetailBL();
        objBookingListBL=new BookingListBL();
        progressDialog=new ProgressDialog(BookingDetail.this);

        btnRate.setOnClickListener(this);
        btnRechedule.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnScheduleDrop.setOnClickListener(this);
        btnReScheduleDrop.setOnClickListener(this);

        intent=getIntent();
        bookingId=intent.getStringExtra("BookingID");
        windowTime=(int) intent.getExtras().get("Window");
        orderType=intent.getStringExtra("Type");
        windowDrop=(int) intent.getExtras().get("WindowDrop");
        //windowTime=Integer.valueOf(window);
        userId= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_USER_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        popupSize();
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
            case R.id.detail_rate:
                startActivity(new Intent(getApplicationContext(),BookingRate.class).putExtra("Id",bookingId).putExtra("Service",objBookingDetailBE.getService()));
                break;
            case R.id.detail_btn_reschedule:
                Intent intentReschedue=new Intent(getApplicationContext(), ReschedulePick.class);
                intentReschedue.putExtra("ID", bookingId);
                intentReschedue.putExtra("Date",objBookingDetailBE.getPickDate());
                intentReschedue.putExtra("Time",objBookingDetailBE.getPickTime());
                intentReschedue.putExtra("Type",objBookingDetailBE.getService());

                startActivity(intentReschedue);

                break;
            case R.id.detail_btn_cancel:
                showDialogCancel(BookingDetail.this);

                break;
            case R.id.detail_btn_schedule_drop:
                Intent intentDrop=new Intent(getApplicationContext(), ScheduleDrop.class);
                intentDrop.putExtra("ID", bookingId);
                intentDrop.putExtra("Type",orderType);
                startActivity(intentDrop);

                break;
            case R.id.detail_btn_reschedule_drop:
                Intent intentReschedueDrop=new Intent(getApplicationContext(), RescheduleDrop.class);
                intentReschedueDrop.putExtra("ID", bookingId);
                intentReschedueDrop.putExtra("Date",objBookingDetailBE.getPickDate());
                intentReschedueDrop.putExtra("Time",objBookingDetailBE.getPickTime());
                intentReschedueDrop.putExtra("Type", objBookingDetailBE.getService());

                startActivity(intentReschedueDrop);

                break;
        }
    }

    private class GetBookingDetail extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objBookingDetailBL.getBookingDetail(params[0],objBookingDetailBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                    tvOrderId.setText("Booking ID: OCW"+bookingId);
                    tvName.setText(objBookingDetailBE.getName());
                    tvMobile.setText("+91 "+objBookingDetailBE.getMobile());
                    tvAddress.setText("Address: "+objBookingDetailBE.getAddress());
                    if(orderType.equalsIgnoreCase("N")){
                        objBookingDetailBE.setService(Constant.ServiceNormal);
                        tvPaid.setText("Number of Paid Clothes: "+objBookingDetailBE.getPaid());
                        tvFree.setText("Number of Free Clothes: " + objBookingDetailBE.getFree());
                    }
                    else {
                        objBookingDetailBE.setService(Constant.ServiceSpecial);
                        tvPaid.setText("Number of Clothes: "+objBookingDetailBE.getPaid());
                        tvFree.setText("");
                    }

                    tvService.setText("Service Type: "+objBookingDetailBE.getService());
                    tvStatus.setText("Status: "+objBookingDetailBE.getStatus());
                    tvPickDate.setText(objBookingDetailBE.getPickDate());
                    tvPickTime.setText(objBookingDetailBE.getPickTime());
                    tvDropDate.setText(objBookingDetailBE.getDropDate());
                    tvDropTime.setText(objBookingDetailBE.getDropTime());

                initializeStatus();


            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

    private void initializeStatus(){
        if(Constant.statusPickup.equalsIgnoreCase(objBookingDetailBE.getStatus())){
            btnRate.setVisibility(View.GONE);
            try {
                cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                String date = dateFormat.format(cal.getTime());
                Date d1 = dateFormat.parse(date);
                Date d2 = dateFormat.parse(objBookingDetailBE.getPickDate());
                if(d2.compareTo(d1)==0){
                    //Log.d("Ubder True condition", Constant.bookingPickdate[position]);
                    String time = dateFormatTime.format(cal.getTime());
                    Log.d("CURRENT TIME",time);
                    Date timed1 = dateFormatTime.parse(time);
                    String currentTime[]=objBookingDetailBE.getPickTime().split("-");
                    Date timed2 = dateFormatTime.parse(currentTime[0]);

                    long diffMs = timed2.getTime()- timed1.getTime() ;
                    long diffSec = diffMs / 1000;
                    long min = diffSec / 60;
                    long sec = diffSec % 60;
                    Log.d("Minutes",min+"");
                    //Log.d("Window",Constant.bookingWindow[position]*60+"");

                    if(min>(windowTime*60)){
                        llReschedule.setVisibility(View.VISIBLE);
                        if(!orderType.equalsIgnoreCase("N")){
                            btnRechedule.setVisibility(View.GONE);
                            llReschedule.setGravity(Gravity.CENTER);
                        }
                    }
                }
                else {
                   // Log.d("Ubder False condition", Constant.bookingPickdate[position]);
                    llReschedule.setVisibility(View.VISIBLE);
                    if(!orderType.equalsIgnoreCase("N")){
                        btnRechedule.setVisibility(View.GONE);
                        llReschedule.setGravity(Gravity.CENTER);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(Constant.statusReady.equalsIgnoreCase(objBookingDetailBE.getStatus())){
            btnRate.setVisibility(View.GONE);
            llScheduleDrop.setVisibility(View.VISIBLE);


        }
        else if(Constant.statusDelivery.equalsIgnoreCase(objBookingDetailBE.getStatus())){
            btnRate.setVisibility(View.GONE);
            try {
                cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                String date = dateFormat.format(cal.getTime());
                Date d1 = dateFormat.parse(date);
                Date d2 = dateFormat.parse(objBookingDetailBE.getDropDate());
                if(d2.compareTo(d1)==0){
                    //Log.d("Ubder True condition", Constant.bookingPickdate[position]);
                    String time = dateFormatTime.format(cal.getTime());
                    Log.d("CURRENT TIME",time);
                    Date timed1 = dateFormatTime.parse(time);
                    String currentTime[]=objBookingDetailBE.getDropTime().split("-");
                    Date timed2 = dateFormatTime.parse(currentTime[0]);

                    long diffMs = timed2.getTime()- timed1.getTime() ;
                    long diffSec = diffMs / 1000;
                    long min = diffSec / 60;
                    long sec = diffSec % 60;
                    Log.d("Minutes",min+"");
                    //Log.d("Window",Constant.bookingWindow[position]*60+"");

                    if(min>(windowDrop*60)){
                        llReScheduleDrop.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    // Log.d("Ubder False condition", Constant.bookingPickdate[position]);
                    llReScheduleDrop.setVisibility(View.VISIBLE);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /*---------------cancel-------------------------*/

    private class CancelPickup extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objBookingListBL.cancelBooking(params[0], params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) ;
                {
                    Toast.makeText(getApplicationContext(), "Pickup Cancelled", Toast.LENGTH_LONG).show();
                    Intent intentCancel = new Intent(getApplicationContext(), BookingList.class);
                    startActivity(intentCancel);
                    finish();
                }
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
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


    private void showDialogCancel(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvTitle;
        Button btnClosePopup,btnsave;
        RadioGroup rgPayment;
        final RadioButton rbYes,rbNo;
        final EditText etClothes;

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
        rbYes= (RadioButton) dialog.findViewById(R.id.payment_cash);
        rbNo= (RadioButton) dialog.findViewById(R.id.payment_online);
        etClothes= (EditText) dialog.findViewById(R.id.popup_clothes);

        tvTitle.setText(getResources().getString(R.string._cancel_title));
        //tvMsg.setText(getResources().getString(R.string._referral_message));
        btnClosePopup.setText(getResources().getString(R.string._cancel_cancel));
        btnsave.setText(getResources().getString(R.string._cancel_save));
        rbYes.setText(getResources().getString(R.string._cancel_yes));
        rbNo.setText(getResources().getString(R.string._cancel_no));

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
                                           if(rbYes.isChecked()){
                                               new CancelPickup().execute(bookingId,orderType);
                                                   dialog.dismiss();
                                           }
                                           else if(rbNo.isChecked()){
                                               dialog.dismiss();
                                           }


                                       }
                                   }

        );


        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
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
