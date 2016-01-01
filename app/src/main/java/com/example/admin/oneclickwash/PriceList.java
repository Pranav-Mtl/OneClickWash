package com.example.admin.oneclickwash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.oneclickwash.Adapter.DryCleanPriceAdapter;
import com.example.admin.oneclickwash.Adapter.MonthlyPriceAdapter;
import com.example.admin.oneclickwash.BL.PriceListBL;
import com.example.admin.oneclickwash.Configuration.Util;

public class PriceList extends AppCompatActivity implements View.OnClickListener {

  LinearLayout llMonthlyHeading,llCleaningHeading;
  LinearLayout llMonthlyExpanded,llCleaningExpanded;

    ImageView ibMonthlyArrow,ibCleaningArrow;

    boolean flagMonthly,flagCleaning;

    PriceListBL objPriceListBL;

    MonthlyPriceAdapter monthlyPriceAdapter;
    DryCleanPriceAdapter dryCleanPriceAdapter;

    RecyclerView listMonthly,listCleaning;

    ProgressDialog progressDialog;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);
        initialize();

        if(Util.isInternetConnection(PriceList.this))
        new GetPricingData().execute();
        else
            showDialogInternet(PriceList.this);

    }

    private void initialize(){
        llMonthlyHeading= (LinearLayout) findViewById(R.id.ll_monthly_heading);
        llCleaningHeading= (LinearLayout) findViewById(R.id.ll_drycleaning_heading);
        llMonthlyExpanded= (LinearLayout) findViewById(R.id.ll_pricing_monthly);
        llCleaningExpanded= (LinearLayout) findViewById(R.id.ll_pricing_drycleaning);

        listMonthly= (RecyclerView) findViewById(R.id.monthly_pricing_list);
        listCleaning= (RecyclerView) findViewById(R.id.cleaning_pricing_list);

        listMonthly.setHasFixedSize(true);
        listCleaning.setHasFixedSize(true);

        progressDialog=new ProgressDialog(PriceList.this);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listMonthly.setLayoutManager(llm);

        final LinearLayoutManager llmm = new LinearLayoutManager(this);
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        listCleaning.setLayoutManager(llmm);

        ibMonthlyArrow= (ImageView) findViewById(R.id.home_subscription_arrow);
        ibCleaningArrow= (ImageView) findViewById(R.id.home_drycleaning_arrow);

        objPriceListBL=new PriceListBL();

        llMonthlyHeading.setOnClickListener(this);
        llCleaningHeading.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_monthly_heading:
                if (!flagMonthly) {
                    llMonthlyExpanded.setVisibility(View.VISIBLE);
                    llCleaningExpanded.setVisibility(View.GONE);
                    ibMonthlyArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                    ibCleaningArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                    flagMonthly = true;
                } else {
                    llMonthlyExpanded.setVisibility(View.GONE);
                    llCleaningExpanded.setVisibility(View.GONE);
                    ibMonthlyArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                    ibCleaningArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                    flagMonthly = false;
                }
                break;
            case R.id.ll_drycleaning_heading:
                if (!flagCleaning) {
                    llMonthlyExpanded.setVisibility(View.GONE);
                    llCleaningExpanded.setVisibility(View.VISIBLE);
                    ibMonthlyArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                    ibCleaningArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                    flagCleaning = true;
                } else {
                    llMonthlyExpanded.setVisibility(View.GONE);
                    llCleaningExpanded.setVisibility(View.GONE);
                    ibMonthlyArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                    ibCleaningArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                    flagCleaning = false;
                }
                break;
        }
    }


    class GetPricingData extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objPriceListBL.getPricingList();

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                monthlyPriceAdapter = new MonthlyPriceAdapter(getApplicationContext());
                listMonthly.setAdapter(monthlyPriceAdapter);

                dryCleanPriceAdapter = new DryCleanPriceAdapter(getApplicationContext());
                listCleaning.setAdapter(dryCleanPriceAdapter);
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }

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
