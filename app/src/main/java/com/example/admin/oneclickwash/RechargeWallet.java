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
import android.widget.TextView;

import com.example.admin.oneclickwash.BL.RechargeWalletBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

public class RechargeWallet extends AppCompatActivity implements View.OnClickListener {
    Button etCredit50,etCredit100,etCredit200;
    EditText etAmount;
    Button btnAddCredit;
    TextView tvCurrentCredit;
    RechargeWalletBL objRechargeWalletBL;

    ProgressDialog progressDialog;

    String userID;

    int xx,yy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_wallet);

        initialize();
        if(Util.isInternetConnection(RechargeWallet.this))
        new GetWallet().execute(userID);
        else
            showDialogInternet(RechargeWallet.this);
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



    private void initialize(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        etCredit50= (Button) findViewById(R.id.credit_fifty);
        etCredit100= (Button) findViewById(R.id.credit_hundred);
        etCredit200= (Button) findViewById(R.id.credit_two_hundred);
        etAmount= (EditText) findViewById(R.id.credit_amount);
        tvCurrentCredit= (TextView) findViewById(R.id.credit_current);

        objRechargeWalletBL=new RechargeWalletBL();
        progressDialog=new ProgressDialog(RechargeWallet.this);

        userID= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_USER_ID);



        btnAddCredit= (Button) findViewById(R.id.btn_buy_credit);

        etCredit50.setOnClickListener(this);
        etCredit100.setOnClickListener(this);
        etCredit200.setOnClickListener(this);
        btnAddCredit.setOnClickListener(this);

        popupSize();

        try {
            Application.tracker().setScreenName("Recharge Wallet Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Recharge Wallet Screen Open")
                    .setCategory("Recharge Wallet")
                    .setAction("UI OPEN")
                    .build());

        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_buy_credit:
                if(etAmount.getText().length()==0){
                    etAmount.setError("required.");
                }
                else {
                    Log.d("AMOUNT", etAmount.getText().toString());
                    startActivity(new Intent(getApplicationContext(),RechargeWebView.class).putExtra("Amount",etAmount.getText().toString()));
                     /* call google analytics*/



                }

                break;
            case R.id.credit_fifty:
                etAmount.setText("50");
                etAmount.setSelection(etAmount.getText().length());
                break;
            case R.id.credit_hundred:
                etAmount.setText("100");
                etAmount.setSelection(etAmount.getText().length());
                break;
            case R.id.credit_two_hundred:
                etAmount.setText("200");
                etAmount.setSelection(etAmount.getText().length());
                break;
        }
        }


    private class GetWallet extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objRechargeWalletBL.getCurrentBalance(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                    tvCurrentCredit.setText("Current balance: ₹"+ Constant.WALLET);
            }catch (NullPointerException e){

            }catch (Exception e){

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
                                           finish();
                                       }
                                   }

        );


        dialog.show();
    }


}

