package com.example.admin.oneclickwash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BE.HomeScreenBE;
import com.example.admin.oneclickwash.BE.SubscriptionDetailBE;
import com.example.admin.oneclickwash.BL.ChangeAddressBL;
import com.example.admin.oneclickwash.BL.HomeScreenBL;
import com.example.admin.oneclickwash.Configuration.RecyclerItemClickListener;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {



    Toolbar toolbar;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    DrawerAdapter drawerAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    HomeScreenBE objHomeScreenBE;
    HomeScreenBL objHomeScreenBL;
    SubscriptionDetailBE objSubscriptionDetailBE;

    ChangeAddressBL objChangeAddressBL;

    String userID;

    LinearLayout llSubscription,llNoSubscription;
    LinearLayout llSubscriptionMonthly,llDryCleaning;
    LinearLayout llBottomSubscription,llBottomNoSubscription;
    LinearLayout llMonthlyHeading,llDryCleaningHeading;
    ImageButton btnSubscriptionArrow,btnDryCleaningArrow;

    TextView tvMobile,tvAdddress,tvStartDate,tvEndDate,tvTotalPick,tvRemainPick,tvTotalCloth,tvRemainingCloth,tvFreeCloth;
    TextView tvPackageName,tvPackageAmount;

    Button btnTakeSubscription;
    Button btnPlaceSubscriptionOrder,btnPlaceOrder;

    int xx,yy;

    ProgressDialog progressDialog;

    boolean flagMonthly,flagDryCleaning;

    ImageButton btnEdit;

    View _itemColoured;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initialize();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        if (position != 0) {
                            if (_itemColoured != null) {
                                _itemColoured.setBackgroundColor(getResources().getColor(R.color.white));
                                _itemColoured.invalidate();
                            }
                            _itemColoured = view;
                            view.setBackgroundColor(getResources().getColor(R.color.gray_color));
                        }


                        if (position == 0) {
                            startActivity(new Intent(getApplicationContext(), Profile.class).putExtra("HomeScreenBE", objHomeScreenBE));
                        } else if (position == 1) {
                            Intent intent = new Intent(getApplicationContext(), PriceList.class);
                            startActivity(intent);
                        } else if (position == 2) {
                            Intent intent = new Intent(getApplicationContext(), BookingList.class);
                            startActivity(intent);
                        } else if (position == 3) {
                            Intent intent = new Intent(getApplicationContext(), Notification.class);
                            startActivity(intent);
                        } else if (position == 4) {
                            Intent intent = new Intent(getApplicationContext(), ReferInvite.class);
                            startActivity(intent);
                        } else if (position == 5) {
                            Intent intent = new Intent(getApplicationContext(), RechargeWallet.class);
                            startActivity(intent);
                        } else if (position == 6) {
                            Intent intent = new Intent(getApplicationContext(), TermAndCondition.class);
                            startActivity(intent);
                        } else if (position == 7) {
                            Util.rateUs(getApplicationContext());
                        } else if (position == 8) {
                            Intent intent = new Intent(getApplicationContext(), ContactUs.class);
                            startActivity(intent);
                        } else if (position == 9) {
                            Intent intent = new Intent(getApplicationContext(), Help.class);
                            startActivity(intent);
                        } else if (position == 10) {
                            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SP_USER_ID, null);
                            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }


                    }
                }));

        if(Util.isInternetConnection(HomeScreen.this))
        new GetUserDetails().execute(userID);
        else
            showDialogInternet(HomeScreen.this);

    }

    private void initialize(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mRecyclerView = (RecyclerView)findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        llSubscription= (LinearLayout) findViewById(R.id.ll_subscription);
        llNoSubscription= (LinearLayout) findViewById(R.id.ll_no_subscription);
        llSubscriptionMonthly= (LinearLayout) findViewById(R.id.ll_monthly_subscription);
        btnSubscriptionArrow= (ImageButton) findViewById(R.id.home_subscription_arrow);
        btnDryCleaningArrow= (ImageButton) findViewById(R.id.home_drycleaning_arrow);
        btnTakeSubscription= (Button) findViewById(R.id.btn_take_subscription);
        btnPlaceSubscriptionOrder= (Button) findViewById(R.id.btn_place_subscription_order);
        btnPlaceOrder= (Button) findViewById(R.id.btn_place_order);
        llBottomSubscription= (LinearLayout) findViewById(R.id.ll_bottom_subscription);
        llBottomNoSubscription= (LinearLayout) findViewById(R.id.ll_bottom_nosubscription);
        llMonthlyHeading= (LinearLayout) findViewById(R.id.ll_monthly_heading);
        llDryCleaningHeading= (LinearLayout) findViewById(R.id.ll_drycleaning_heading);
        llDryCleaning= (LinearLayout) findViewById(R.id.ll_drycleaning);
        btnEdit= (ImageButton) findViewById(R.id.home_edit_address);

        tvMobile= (TextView) findViewById(R.id.home_mobile);
        tvAdddress= (TextView) findViewById(R.id.home_address);
        tvStartDate= (TextView) findViewById(R.id.home_start_date);
        tvEndDate= (TextView) findViewById(R.id.home_end_date);
        tvTotalPick= (TextView) findViewById(R.id.home_tot_pickup);
        tvRemainPick= (TextView) findViewById(R.id.home_remaining_pick);
        tvTotalCloth= (TextView) findViewById(R.id.home_tot_cloth);
        tvRemainingCloth= (TextView) findViewById(R.id.home_remaining_cloth);
        tvFreeCloth= (TextView) findViewById(R.id.home_free_cloth);
        tvPackageName= (TextView) findViewById(R.id.home_package_name);
        tvPackageAmount= (TextView) findViewById(R.id.home_package_amount);

        objHomeScreenBE=new HomeScreenBE();
        objHomeScreenBL=new HomeScreenBL();
        objChangeAddressBL=new ChangeAddressBL();
        objSubscriptionDetailBE=new SubscriptionDetailBE();
        progressDialog=new ProgressDialog(HomeScreen.this);

        btnSubscriptionArrow.setOnClickListener(this);
        btnDryCleaningArrow.setOnClickListener(this);
        btnTakeSubscription.setOnClickListener(this);
        btnPlaceSubscriptionOrder.setOnClickListener(this);
        btnPlaceOrder.setOnClickListener(this);
        llMonthlyHeading.setOnClickListener(this);
        llDryCleaningHeading.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        userID= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_USER_ID);

        mRecyclerView.setHasFixedSize(true);
          // Setting the adapter to RecyclerView
        // Creating a layout Manager
        mLayoutManager = new LinearLayoutManager(HomeScreen.this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer iscom/example/admin/oneclickwash/HomeScreen.java:50
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();

        popupSize();

        try {
            Application.tracker().setScreenName("Home Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Home Screen Open")
                    .setCategory("Home")
                    .setAction("UI OPEN")
                    .build());

        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_subscription_arrow:
                if(!flagMonthly){
                    llSubscriptionMonthly.setVisibility(View.VISIBLE);
                    flagMonthly=true;
                    btnSubscriptionArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                }
                else {
                    llSubscriptionMonthly.setVisibility(View.GONE);
                    flagMonthly=false;
                    btnSubscriptionArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;
            case R.id.home_drycleaning_arrow:
                if(!flagDryCleaning){
                    llDryCleaning.setVisibility(View.VISIBLE);
                    flagDryCleaning=true;
                    btnDryCleaningArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                }
                else {
                    llDryCleaning.setVisibility(View.GONE);
                    flagDryCleaning=false;
                    btnDryCleaningArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;
            case R.id.btn_take_subscription:
                startActivity(new Intent(getApplicationContext(),SubscriptionPackage.class));
                break;
            case R.id.btn_place_order:
                Toast.makeText(getApplicationContext(),"Please take subscription first.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SubscriptionPackage.class));
                break;
            case R.id.btn_place_subscription_order:
                startActivity(new Intent(getApplicationContext(),PlaceOrder.class));
                break;
            case R.id.ll_monthly_heading:
                if(!flagMonthly){
                    llSubscriptionMonthly.setVisibility(View.VISIBLE);
                    flagMonthly=true;
                    btnSubscriptionArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                }
                else {
                    llSubscriptionMonthly.setVisibility(View.GONE);
                    flagMonthly=false;
                    btnSubscriptionArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;
            case R.id.ll_drycleaning_heading:
                if(!flagDryCleaning){
                    llDryCleaning.setVisibility(View.VISIBLE);
                    flagDryCleaning=true;
                    btnDryCleaningArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                }
                else {
                    llDryCleaning.setVisibility(View.GONE);
                    flagDryCleaning=false;
                    btnDryCleaningArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;
            case R.id.home_edit_address:
                    showDialogAddress(HomeScreen.this);
                break;
        }
    }

    private class GetUserDetails extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objHomeScreenBL.getRecord(params[0],objHomeScreenBE,objSubscriptionDetailBE);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Constant.NAME=objHomeScreenBE.getName();

                Constant.WALLET=objHomeScreenBE.getWallet();

                drawerAdapter = new DrawerAdapter(Constant.TITLES,Constant.ICONS,Constant.NAME,Constant.WALLET ,getApplicationContext());// Letting the s0ystem know that the list objects are of fixed size
                mRecyclerView.setAdapter(drawerAdapter);

                if(Constant.subscriptionActive.equalsIgnoreCase(objHomeScreenBE.getStatus())){
                    llSubscription.setVisibility(View.VISIBLE);
                    llBottomSubscription.setVisibility(View.VISIBLE);
                    setSubscriptionData();
                }
                else {
                    llSubscription.setVisibility(View.GONE);
                    llBottomSubscription.setVisibility(View.GONE);
                    llNoSubscription.setVisibility(View.VISIBLE);
                    llBottomNoSubscription.setVisibility(View.VISIBLE);
                }

                drawerAdapter.notifyDataSetChanged();
            }
            catch (NullPointerException e){

            }
            catch(Exception e){

            }
            finally {
                progressDialog.dismiss();
            }

        }
    }

    private void setSubscriptionData(){
        tvMobile.setText("Mobile: +91 "+objHomeScreenBE.getMobile());
        tvAdddress.setText("Address: "+objHomeScreenBE.getAddress());
        tvTotalPick.setText(objSubscriptionDetailBE.getTotalPickup());
        try {
            Date dateStart=dateFormat.parse(objSubscriptionDetailBE.getStartDate());
            Date dateEnd=dateFormat.parse(objSubscriptionDetailBE.getEndDate());
            String startDate = new SimpleDateFormat("dd-MM-yyyy").format(dateStart);
            String endDate= new SimpleDateFormat("dd-MM-yyyy").format(dateEnd);

            tvStartDate.setText(startDate);
            tvEndDate.setText(endDate);
        }catch (Exception e){
            e.printStackTrace();
        }

        tvRemainPick.setText(objSubscriptionDetailBE.getRemainingPickup());
        tvTotalCloth.setText(objSubscriptionDetailBE.getTotalCloth());
        tvRemainingCloth.setText(objSubscriptionDetailBE.getRemainingCloth());
        tvFreeCloth.setText(objSubscriptionDetailBE.getFreeCloth());
        tvPackageAmount.setText("₹"+objSubscriptionDetailBE.getPackageAmount());
        tvPackageName.setText(objSubscriptionDetailBE.getPackageName());
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );//***Change Here***
            startActivity(intent);

           // System.exit(0);
            return;
        }

        Drawer.closeDrawers();
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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

    /* popup for change address */
    private void showDialogAddress(Context context){
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
        dialog.setContentView(R.layout.layout_change_address);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);

        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        etClothes= (EditText) dialog.findViewById(R.id.popup_clothes);

        tvTitle.setText(getResources().getString(R.string._address_title));
        //tvMsg.setText(getResources().getString(R.string._referral_message));
        btnClosePopup.setText(getResources().getString(R.string._address_cancel));
        btnsave.setText(getResources().getString(R.string._address_save));


        etClothes.setText(objHomeScreenBE.getAddress());
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

                    if (etClothes.getText().toString().length() > 0) {
                        new ChangeAddress().execute(userID,etClothes.getText().toString());
                    }

        }
    }

        );


        dialog.show();
    }

    private class ChangeAddress extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objChangeAddressBL.changeAddress(params[0], params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    startActivity(new Intent(getApplicationContext(),HomeScreen.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        //drawerAdapter.notifyDataSetChanged();
    }
}
