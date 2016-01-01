package com.example.admin.oneclickwash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.oneclickwash.BE.PlaceOrderBE;
import com.example.admin.oneclickwash.BL.GetTimeSlotBL;
import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlaceOrder extends AppCompatActivity implements View.OnClickListener {

    Spinner spnTimeSlot;
    Button btnDate,btnTime,btnNext;
    CheckBox cbDryClean;

    private TimePicker timePicker1;
    private String format = "";


    private TextView datePickerShowDialogButton;

    private int hour;

    private int minute;

    static final int TIME_DIALOG_ID = 999;

    String strDate,strTime,strSlotID="";
    String strServiceType="Monthly Service (Wash + Dry + Iron)";

    int xx,yy;

    List timeSlotList=new ArrayList<>();

    GetTimeSlotBL objGetTimeSlotBL;

    PlaceOrderBE objPlaceOrderBE;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        initialize();
    }

    private void initialize(){

        btnDate= (Button) findViewById(R.id.order_date);
        spnTimeSlot= (Spinner) findViewById(R.id.order_time);
        btnNext= (Button) findViewById(R.id.order_next);
        cbDryClean= (CheckBox) findViewById(R.id.order_drycleaning);

        objGetTimeSlotBL=new GetTimeSlotBL();
        objPlaceOrderBE=new PlaceOrderBE();

        btnDate.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog=new ProgressDialog(PlaceOrder.this);

        btnNext.setEnabled(false);

        popupSize();
        //setTimePicker();

        try {
            Application.tracker().setScreenName("Place Order Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Place Order Screen Open")
                    .setCategory("Place Order")
                    .setAction("UI OPEN")
                    .build());

        } catch (Exception e) {

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
        dialogBuilder.setNegativeButton(
                "Go to today",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnDate.setText(
                                formatter.format(now.getTime())
                        );
                        dialog.dismiss();
                    }
                }
        );

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_date:
                showDatePicker();
                break;
            case R.id.order_next:
                if(validateDetails()){
                    showDialogPayment(PlaceOrder.this);

                    /*if(cbDryClean.isChecked()){
                        showDialogPayment(PlaceOrder.this);
                    }
                    else
                    {
                        objPlaceOrderBE.setDate(strDate);
                        objPlaceOrderBE.setTimeSlot(strTime);
                        objPlaceOrderBE.setDryClean(false);
                        objPlaceOrderBE.setDryCleanCloth("");
                        startActivity(new Intent(getApplicationContext(), OrderDetails.class).putExtra("PlaceOrderBE", objPlaceOrderBE));
                    }
*/
                }
                break;

        }
    }


    private void setTimePicker(){


         ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,objGetTimeSlotBL.list);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spnTimeSlot.setAdapter(adapter);
    }

    private  boolean validateDetails(){
        boolean flag=true;


        strDate=btnDate.getText().toString();
        strTime=spnTimeSlot.getSelectedItem().toString();
        if(spnTimeSlot.getSelectedItemPosition()!=0)
        strSlotID=objGetTimeSlotBL.listSLotID.get(spnTimeSlot.getSelectedItemPosition()-1).toString();

        if(strDate.equalsIgnoreCase("Select Date")){
            flag=false;
            Toast.makeText(getApplicationContext(),"Please select pickup date",Toast.LENGTH_SHORT).show();
        }else if(strTime.equalsIgnoreCase("Select Time slot")){

            flag=false;
            Toast.makeText(getApplicationContext(),"Please select time slot",Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    /* popup for add referal */
    private void showDialogPayment(Context context){
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

        tvTitle.setText(getResources().getString(R.string._dryclean_title));
        //tvMsg.setText(getResources().getString(R.string._referral_message));
        btnClosePopup.setText(getResources().getString(R.string._dryclean_cancel));
        btnsave.setText(getResources().getString(R.string._dryclean_save));
        rbYes.setText(getResources().getString(R.string._dryclean_yes));
        rbNo.setText(getResources().getString(R.string._dryclean_no));

        rbYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rv = (RadioButton) v;
                if (rv.isChecked()) {
                    etClothes.setVisibility(View.VISIBLE);
                } else {
                    etClothes.setVisibility(View.GONE);
                }
            }
        });

        rbNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rv = (RadioButton) v;
                if (rv.isChecked()) {
                    etClothes.setVisibility(View.GONE);
                } else {
                    etClothes.setVisibility(View.GONE);
                }
            }
        });

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
                                                if(etClothes.getText().toString().length()>0 && !(etClothes.getText().toString().equalsIgnoreCase("0"))){
                                                    objPlaceOrderBE.setDryClean(true);
                                                    objPlaceOrderBE.setDryCleanCloth(etClothes.getText().toString());
                                                    objPlaceOrderBE.setDate(strDate);
                                                    objPlaceOrderBE.setTimeSlot(strTime);
                                                    objPlaceOrderBE.setSlotID(strSlotID);
                                                    startActivity(new Intent(getApplicationContext(), OrderDetails.class).putExtra("PlaceOrderBE", objPlaceOrderBE));
                                                    dialog.dismiss();
                                                }
                                                else
                                                    etClothes.setError("Please Enter Number of Clothes ");
                                            }
                                           else if(rbNo.isChecked()){
                                                objPlaceOrderBE.setDate(strDate);
                                                objPlaceOrderBE.setTimeSlot(strTime);
                                                objPlaceOrderBE.setDryClean(false);
                                                objPlaceOrderBE.setDryCleanCloth("");
                                                objPlaceOrderBE.setSlotID(strSlotID);
                                                startActivity(new Intent(getApplicationContext(), OrderDetails.class).putExtra("PlaceOrderBE", objPlaceOrderBE));
                                                dialog.dismiss();
                                            }


                                       }
                                   }

        );


        dialog.show();
    }

    private class GetTimeSlots extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Getting slots");
            progressDialog.setCancelable(false);
            progressDialog.setIcon(R.drawable.ic_home_logo);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objGetTimeSlotBL.getTimeSlots(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    setTimePicker();
                    btnNext.setEnabled(true);
                }
                else {

                    Toast.makeText(getApplicationContext(),"Time slot not available for selected date. Please select another date",Toast.LENGTH_LONG).show();
                    objGetTimeSlotBL.list.add("Select Time Slot");
                    setTimePicker();
                }
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

                                       }
                                   }

        );


        dialog.show();
    }


}
