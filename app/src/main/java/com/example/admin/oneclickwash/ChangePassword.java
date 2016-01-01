package com.example.admin.oneclickwash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.example.admin.oneclickwash.BL.ChangePasswordBL;
import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    EditText etOldPassword,etNewPassword,etConfirmPassword;
    Button btnDone;

    String strOld,strNew,strConfirm;

    String userId;

    ChangePasswordBL objChangePasswordBL;
    ProgressDialog progressDialog;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initialize();
    }

    private void initialize(){
        etOldPassword= (EditText) findViewById(R.id.change_old);
        etNewPassword= (EditText) findViewById(R.id.change_new);
        etConfirmPassword= (EditText) findViewById(R.id.change_confirm);
        btnDone= (Button) findViewById(R.id.change_done);
        btnDone.setOnClickListener(this);

        userId= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_USER_ID);
        objChangePasswordBL=new ChangePasswordBL();
        progressDialog=new ProgressDialog(ChangePassword.this);

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
            case R.id.change_done:
                if(validate()){
                    if(Util.isInternetConnection(ChangePassword.this))
                    new ChangedPassword().execute(userId,strOld,strNew);
                    else
                        showDialogInternet(ChangePassword.this);
                }
                break;
        }
    }

    protected boolean validate(){
        boolean flag=true;
        strOld=etOldPassword.getText().toString();
        strNew=etNewPassword.getText().toString();
        strConfirm=etConfirmPassword.getText().toString();

        if(strOld.trim().length()==0){
            flag=false;
            etOldPassword.setError("Required");
        }

        if(strNew.trim().length()==0){
            flag=false;
            etNewPassword.setError("Required");
        }

        if(strConfirm.trim().length()==0){
            flag=false;
            etConfirmPassword.setError("Password Mismatch");
        }
        else if(!strNew.trim().equals(strConfirm.trim())){
            flag=false;
            etConfirmPassword.setError("Password Mismatch");
        }
        if (strOld.trim().equalsIgnoreCase(strNew.trim())){
            flag=false;
            etNewPassword.setError("Old password and New password must be different.");
        }

        return flag;
    }

    private class ChangedPassword extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objChangePasswordBL.changePassowrd(params[0],params[1],params[2]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            catch (NullPointerException e){

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
