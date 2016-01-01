package com.example.admin.oneclickwash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin.oneclickwash.BE.PaymentBE;
import com.example.admin.oneclickwash.BE.RechargeWalletBE;
import com.example.admin.oneclickwash.BL.PaymentBL;
import com.example.admin.oneclickwash.BL.PaymentReviewBL;
import com.example.admin.oneclickwash.BL.RechargeWalletBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.R;

import java.net.URLDecoder;

public class PaymentWebview extends AppCompatActivity {

    private WebView webView;

    PaymentBL objPaymentBL;

    ProgressDialog mProgressDialog;

    String addToURL="?embed=form";

    String userID,wallet;

    String amount;
    String subscription;

    Button btnDone;

    PaymentBE objPaymentBE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_webview);

        initialize();

        if(Util.isInternetConnection(PaymentWebview.this))
        new GetURLToLoad().execute(userID,subscription,amount,wallet);
    }

    private void initialize(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mProgressDialog=new ProgressDialog(PaymentWebview.this);
        objPaymentBE=new PaymentBE();
        objPaymentBL=new PaymentBL();

        webView = (WebView) findViewById(R.id.ocw_webView);


        amount=getIntent().getExtras().get("Amount").toString();
        subscription=getIntent().getStringExtra("SubscriptionID");
        wallet=getIntent().getStringExtra("Wallet");


        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_USER_ID);
    }


    private class GetURLToLoad extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String URL=objPaymentBL.getSubscriptiondata(params[0],params[1],params[2],params[3],objPaymentBE);
            return URL;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                s=s+addToURL+"&data_name="+objPaymentBE.getUserName()+"&data_email="+objPaymentBE.getEmailID()+"&data_phone="+objPaymentBE.getMobileNo()+"&data_readonly=data_\n" +
                        "name&data_readonly=data_phone";
                startWebView(s);
            }
            catch (Exception e){

            }finally {
                mProgressDialog.dismiss();
            }

        }
    }

     /*------------- web view ------------------------*/

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                try {
                    if (progressDialog == null) {
                        // in standard case YourActivity.this
                        progressDialog = new ProgressDialog(PaymentWebview.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });


        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);


        //Load url in webview
        webView.loadUrl(url);


    }

    /*----------------------- ON BACK PRESSED --------------------------*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            try {

                //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
                Log.d("URL RETURN", webView.getUrl());

                String url = webView.getUrl();

                String ss[] = url.split("&");

                String keyValue[] = ss[0].split("=");
                String param_key = URLDecoder.decode(keyValue[0]);
// the	tracking	id	value	(i.e.	12345)	is
                // stored	in	param_value


                String param_value =
                        URLDecoder.decode(keyValue[1]);

                Log.d("Key", param_key);
                Log.d("Paymenti id", param_value);

                String status[] = ss[1].split("=");

                String statusKey = URLDecoder.decode(status[0]);
                String statusValue = URLDecoder.decode(status[1]);

                Log.d("STATUS Key", statusKey);
                Log.d("STATUS VALUE", statusValue);

                if (statusValue.equals("success")) {

                    Toast.makeText(getApplicationContext(), "OneClickWash Payment Successed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    // finish();
                } else {
                    Toast.makeText(getApplicationContext(), "OneClickWash payment failed. Please try again.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                finish();
            }
            catch (Exception e){
                e.printStackTrace();
                finish();
            }




            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
