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

import com.example.admin.oneclickwash.BE.ReferInviteBE;
import com.example.admin.oneclickwash.BL.ReferInviteBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;

public class ReferInvite extends AppCompatActivity implements View.OnClickListener {

    Button btnShare;
    String promoMessage;
    String referMesssage;
    ReferInviteBL objReferInviteBL;
    ReferInviteBE objReferInviteBE;
    String userID;
    ProgressDialog progressDialog;
    TextView tvReferMessage;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_invite);
        initialize();

        if(Util.isInternetConnection(ReferInvite.this))
        new GetReferal().execute(userID);
        else
            showDialogInternet(ReferInvite.this);
    }

    private void initialize(){

        tvReferMessage= (TextView) findViewById(R.id.refer_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnShare= (Button) findViewById(R.id.btn_share);

        objReferInviteBE=new ReferInviteBE();
        objReferInviteBL=new ReferInviteBL();
        progressDialog=new ProgressDialog(ReferInvite.this);

        btnShare.setOnClickListener(this);

        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_USER_ID);

        popupSize();

        //promoMessage="Use my promocode PRANAV123 to signup on OneClickWash. and get free ₹100 ₹100on OneClickWash wallet.";
        //promoMessage="Signup for OneClickWash Laundry Service using my Promo Code PRANAV123 and get a Discount of ₹100. Enjoy as hassle free laundry experience!!!";
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
            case R.id.btn_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, promoMessage);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
    }

    private class GetReferal extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objReferInviteBL.getReferCode(params[0],objReferInviteBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) {
                    btnShare.setText(objReferInviteBE.getReferCode());
                    promoMessage = "Signup for OneClickWash Laundry Service using my promo code " + objReferInviteBE.getReferCode() + " and get a Discount of ₹" + objReferInviteBE.getReferValue() + ". Enjoy a hassle free laundry experience!!! \n GET APP# http://tinyurl.com/oneclickwash";
                    referMesssage="They get free gift voucher worth ₹"+objReferInviteBE.getReferValue() +" for subscribing to our services, and so do you!";
                    tvReferMessage.setText(referMesssage);
                } else {
                    Toast.makeText(getApplicationContext(), "Our referal program is on hold. Please come back later.", Toast.LENGTH_SHORT).show();
                    finish();
                }
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
