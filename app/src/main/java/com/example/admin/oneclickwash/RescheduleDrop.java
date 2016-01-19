package com.example.admin.oneclickwash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BL.GetTimeSlotBL;
import com.example.admin.oneclickwash.BL.ReschedulePickBL;
import com.example.admin.oneclickwash.BL.ScheduleDropBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RescheduleDrop extends AppCompatActivity implements View.OnClickListener {

    TextView tvCurrentDate,tvCurrentTime;
    Button btnDate,btnDone;
    Spinner spnTime;

    private TextView datePickerShowDialogButton;

    GetTimeSlotBL objGetTimeSlots;
    ProgressDialog progressDialog;

    String bookingID;

    String strDate,strTime,strType,strTileSlot;

    String currentDate,currentTime;

    ScheduleDropBL objScheduleDropBL;


    int xx,yy;

    String slotID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_drop);
        initialize();
    }

    private void initialize(){
        tvCurrentDate= (TextView) findViewById(R.id.reschedule_current_date);
        tvCurrentTime= (TextView) findViewById(R.id.reschedule_current_time);
        btnDate= (Button) findViewById(R.id.reschedule_date);
        btnDone= (Button) findViewById(R.id.reschedule_done);
        spnTime= (Spinner) findViewById(R.id.reschedule_time);

        btnDone.setOnClickListener(this);
        btnDate.setOnClickListener(this);

        btnDone.setEnabled(false);

        bookingID=getIntent().getStringExtra("ID");
        currentDate=getIntent().getStringExtra("Date");
        currentTime=getIntent().getStringExtra("Time");
        strType=getIntent().getStringExtra("Type");

        tvCurrentTime.setText("Current Drop Time: "+currentTime);
        tvCurrentDate.setText("Current Drop Date: "+currentDate);

        objGetTimeSlots=new GetTimeSlotBL();
        objScheduleDropBL=new ScheduleDropBL();

        progressDialog=new ProgressDialog(RescheduleDrop.this);

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
            case R.id.reschedule_date:
                showDatePicker();
                break;
            case R.id.reschedule_done:
                if(validateDetails()){
                    if(Util.isInternetConnection(RescheduleDrop.this))
                    new Reschedule().execute(bookingID,strDate,strTime,strType,slotID);
                    else
                        showDialogInternet(RescheduleDrop.this);
                }
                break;
        }
    }
    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(this);
        View customView = inflater.inflate(R.layout.custom_datepicker, null);
        dialogBuilder.setView(customView);
        final Calendar now = Calendar.getInstance();
        final DatePicker datePicker =
                (DatePicker) customView.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView =
                (TextView) customView.findViewById(R.id.dialog_dateview);
        final SimpleDateFormat dateViewFormatter =
                new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("12-12-2015"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.setMinDate(minDate.getTimeInMillis());
        // View settings
        dialogBuilder.setTitle("Choose a date");
        dialogBuilder.setIcon(R.drawable.ic_home_logo);
        Calendar choosenDate = Calendar.getInstance();
        int year = choosenDate.get(Calendar.YEAR);
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        try {
            Date choosenDateFromUI = formatter.parse(
                    datePickerShowDialogButton.getText().toString()
            );
            choosenDate.setTime(choosenDateFromUI);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(
                dateViewFormatter.format(dateToDisplay.getTime())
        );
        // Buttons


        dialogBuilder.setPositiveButton(
                "Choose",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar choosen = Calendar.getInstance();
                        choosen.set(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()
                        );
                        btnDate.setText(
                                dateViewFormatter.format(choosen.getTime())
                        );
                        dialog.dismiss();
                        new GetTimeSlots().execute(btnDate.getText().toString().trim());
                    }
                }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        datePicker.init(
                year,
                month,
                day,
                new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        Calendar choosenDate = Calendar.getInstance();
                        choosenDate.set(year, monthOfYear, dayOfMonth);
                        dateTextView.setText(
                                dateViewFormatter.format(choosenDate.getTime())
                        );
                        if (choosenDate.get(Calendar.DAY_OF_WEEK) ==
                                Calendar.SUNDAY ||
                                now.compareTo(choosenDate) < 0) {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        } else {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        }
                    }
                }
        );
        // Finish
        dialog.show();
    }

    private class GetTimeSlots extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Getting slots");
            progressDialog.setCancelable(false);
            progressDialog.setIcon(R.drawable.ic_home_logo);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objGetTimeSlots.getTimeSlotsDrop(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    setTimePicker();
                    btnDone.setEnabled(true);
                }
                else {

                    Toast.makeText(getApplicationContext(), "Time slot not available for selected date. Please select another date", Toast.LENGTH_LONG).show();
                    objGetTimeSlots.dropList.add("Select Time Slot");
                    setTimePicker();
                }
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

    private void setTimePicker(){


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,objGetTimeSlots.dropList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spnTime.setAdapter(adapter);
    }


    private  boolean validateDetails(){
        boolean flag=true;


        strDate=btnDate.getText().toString();
        strTime=spnTime.getSelectedItem().toString();
        if(spnTime.getSelectedItemPosition()!=0)
        slotID=objGetTimeSlots.dropListSLotID.get(spnTime.getSelectedItemPosition()-1).toString();

        if(strDate.equalsIgnoreCase("Select Date")){
            flag=false;
            Toast.makeText(getApplicationContext(),"Please select pickup date",Toast.LENGTH_SHORT).show();
        }else if(strTime.equalsIgnoreCase("Select Time slot")){

            flag=false;
            Toast.makeText(getApplicationContext(),"Please select time slot",Toast.LENGTH_SHORT).show();
        }
        return flag;
    }


    private class Reschedule extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objScheduleDropBL.setReDrop(params[0], params[1], params[2], params[3],params[4]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(),"Drop-off Rescheduled",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), BookingList.class));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again.",Toast.LENGTH_SHORT).show();
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

                                       }
                                   }

        );


        dialog.show();
    }

}
