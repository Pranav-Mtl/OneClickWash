package com.example.admin.oneclickwash.BL;

import android.content.Context;

import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Admin on 12/2/2015.
 */
public class OtpVerificationBL {

    String finalvalue;
    String status;
    Context mContext;

    public String verifyOtp(String user_id,Context context)
    {
        try {
            mContext=context;
            String result = fetRecord(user_id);
            finalvalue = validate(result);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return finalvalue;

    }

    private String fetRecord(String user_id) {

        String text = null;

        try
        {
           // http://oneclickwash.com/webservices/otp_verify.php?user_id=
            String URL="mobile="+user_id;
            text= RestFullWS.serverRequest(URL,Constant.WS_OTP_VERIFY);

        }
        catch (Exception e)
        {
            System.out.println("in web services catch block");
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;

    }

    public String validate(String strValue)

    {

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();
            if(status.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)){
                String userid=jsonObject.get("user_id").toString();
                Util.setSharedPrefrenceValue(mContext,Constant.PREFS_NAME,Constant.SP_USER_ID,userid);
            }

            System.out.println("otp result--------->"+status);


        } catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
        return status;
    }

    /*----------------------------------------------------------------------*/

    public String getResend(String mobile,String otp){
        String result=callWSResend(mobile,otp);
        String status=validateResend(result);
        return status;
    }

    private String callWSResend(String mobile,String otp){
        String URL="mobile="+mobile+"&otp="+otp;
        String text= RestFullWS.serverRequest(URL,Constant.WS_RESEND_OTP);
        return text;
    }

    private String validateResend(String result){
        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();




        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }



}
