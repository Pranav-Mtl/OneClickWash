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
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BE.HomeScreenBE;
import com.example.admin.oneclickwash.BL.UpdateProfileBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    EditText etName,etLocation,etEmail,etMobile;

    Button btnChangePassword,btnEditProfile,btnSave;

    String userId;

    HomeScreenBE objHomeScreenBE;

    UpdateProfileBL objUpdateProfileBL;

    ProgressDialog progressDialog;

    TextView editMessage;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();
    }

    private void initialize(){

        etName= (EditText) findViewById(R.id.edt_name);
        etEmail= (EditText) findViewById(R.id.edt_email);
        etMobile= (EditText) findViewById(R.id.edt_mobile);
        etLocation= (EditText) findViewById(R.id.edt_address);
        editMessage= (TextView) findViewById(R.id.edit_message);

        btnChangePassword= (Button) findViewById(R.id.profile_password);
        btnEditProfile= (Button) findViewById(R.id.profile_edit);
        btnSave= (Button) findViewById(R.id.profile_save);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        objUpdateProfileBL=new UpdateProfileBL();

        progressDialog=new ProgressDialog(Profile.this);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnSave.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);

        userId= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_USER_ID);

        objHomeScreenBE=(HomeScreenBE) getIntent().getSerializableExtra("HomeScreenBE");

        etName.setText(objHomeScreenBE.getName());
        etEmail.setText(objHomeScreenBE.getEmail());
        etMobile.setText(objHomeScreenBE.getMobile());
        etLocation.setText(objHomeScreenBE.getAddress());

        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etMobile.setEnabled(false);
        etLocation.setEnabled(false);

        popupSize();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_edit:
                etName.setEnabled(true);
                etEmail.setEnabled(true);
                etLocation.setEnabled(true);
                btnSave.setVisibility(View.VISIBLE);
                editMessage.setVisibility(View.VISIBLE);
                break;
            case R.id.profile_password:
                startActivity(new Intent(getApplicationContext(),ChangePassword.class));
                break;
            case R.id.profile_save:
                String name=etName.getText().toString();
                String email=etEmail.getText().toString();
                String location=etLocation.getText().toString();
                boolean flag=false;
                if(name.length()==0){
                    etName.setError("Required");
                    flag=true;
                }

                if(email.length()==0){
                    etEmail.setError("Required");
                    flag=true;
                }

                if(location.length()==0){
                    etLocation.setError("Required");
                    flag=true;
                }
                if(!flag)
                    if(Util.isInternetConnection(Profile.this))
                        new UpdateProfile().execute(userId,email,name,location);
                else
                    showDialogInternet(Profile.this);
                break;
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

    private class UpdateProfile extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objUpdateProfileBL.updateProfile(params[0],params[1],params[2],params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(),"Profile updated successfully.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomeScreen.class));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Something went wrong. Try again?",Toast.LENGTH_SHORT).show();
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
                                           //finish();
                                       }
                                   }

        );


        dialog.show();
    }


}
