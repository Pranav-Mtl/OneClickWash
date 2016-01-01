package com.example.admin.oneclickwash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.Adapter.PackageAdapter;
import com.example.admin.oneclickwash.Adapter.SubscriptionPackageAdapter;
import com.example.admin.oneclickwash.BL.SubscriptionPackageBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubscriptionPackage extends AppCompatActivity implements View.OnClickListener {

    ExpandableListView expListView;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    SubscriptionPackageBL objSubscriptionPackageBL;

    PackageAdapter objPackageAdapter;

    int selectedGroupPosition=-1;
    private boolean[] mGroupStates;

    LinearLayout llBottom;
    Button btnNext;
    TextView tvSelectedPackage;

    ProgressDialog progressDialog;

    String title,price;

    String packageId;

    RecyclerView listMonthly;

    SubscriptionPackageAdapter subscriptionPackageAdapter;

    int xx,yy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_package);
        initialize();

        if(Util.isInternetConnection(SubscriptionPackage.this))
        new GetAllPackages().execute();
        else
            showDialogInternet(SubscriptionPackage.this);
    }

    private void initialize(){



        listMonthly= (RecyclerView) findViewById(R.id.monthly_pricing_list);
        btnNext= (Button) findViewById(R.id.subscription_package_next);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listMonthly.setLayoutManager(llm);

        btnNext.setOnClickListener(this);



        objSubscriptionPackageBL=new SubscriptionPackageBL();
        progressDialog=new ProgressDialog(SubscriptionPackage.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        popupSize();

        try {
            Application.tracker().setScreenName("Subscription Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Subscription Screen Open")
                    .setCategory("Subscription")
                    .setAction("UI OPEN")
                    .build());

        } catch (Exception e) {

        }

        //expListView.setOnGroupClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.subscription_package_next:
                if(subscriptionPackageAdapter.mSelectedItem!=-1){
                    int pos=subscriptionPackageAdapter.mSelectedItem;

                   
                    String packageID=Constant.pricingID[pos];
                    String packagePrice=Constant.pricingPackage[pos];
                    String packageName=Constant.pricingUser[pos];

                    startActivity(new Intent(getApplicationContext(),SubscriptionConfirmed.class).putExtra("PackageId",packageID).putExtra("Price",packagePrice).putExtra("Package",packageName));
                }
                else {
                    Snackbar snack = Snackbar
                            .make(findViewById(R.id.package_root_raw),
                                    getResources().getString(R.string.no_package_selected),
                                    Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.no_package_selected));
                    ViewGroup group = (ViewGroup) snack.getView();
                    TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    group.setBackgroundColor(getResources().getColor(R.color.sky));
                    snack.show();

                }
                break;
        }
    }

   /* @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        switch (parent.getId()) {
            case R.id.expandable_list:

                selectedGroupPosition=groupPosition;

                llBottom.setVisibility(View.VISIBLE);

                mGroupStates[groupPosition] = !mGroupStates[groupPosition];

                if (mGroupStates[groupPosition]) {
                } else {
                    // group is being collapsed
                    llBottom.setVisibility(View.GONE);

                }
                break;
        }
        return false;
    }
*/
    private class GetAllPackages extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objSubscriptionPackageBL.getAllPackages();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
               subscriptionPackageAdapter=new SubscriptionPackageAdapter(getApplicationContext());
                listMonthly.setAdapter(subscriptionPackageAdapter);
            }
            catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically hpopupSizeandle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {


            onBackPressed();
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
