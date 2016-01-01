package com.example.admin.oneclickwash.BL;

import android.content.Context;

import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Admin on 12/1/2015.
 */
public class SignUpScreenBL {

    String finalvalue;
    String status;
    String email,phone,address,password,otp,name;
    String user_id,deviceId;
    Context context;


    public String signUpRecord(Context context,String email,String phone,String address,String password,String otp,String name,String deviceId,String gcm) {
        this.context=context;
        this.email=email;
        this.phone=phone;
        this.address=address;
        this.password=password;
        this.name=name;
        this.otp=otp;
        this.deviceId=deviceId;


        try {
            String result = fetRecord(email,phone,address,password,otp,name,deviceId,gcm);
            finalvalue = validate(result);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return finalvalue;

    }

    private String fetRecord(String email,String phone,String address,String password,String otp,String name,String deviceId,String gcm) {
        String text = null;

        try
        {
            ////http://oneclickwash.com/webservices/signup.php?mobile=&email=&city=&address=&password=&gcm_id=&otp=
            String URL="mobile="+phone+"&email="+email+"&city="+"&address="+address+"&password="+password+"&gcm_id="+gcm+"&otp="+otp+"&name="+name+"&device_id="+deviceId;
            text=RestFullWS.serverRequest(URL,Constant.WS_SIGNUP);

        }
        catch (Exception e)
        {
            System.out.println("in web services catch block");
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }

    public String validate(String strValue)    {
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();
            String message=jsonObject.get("message").toString();


            System.out.println("status----------->"+status);
            System.out.println("message--------->"+message);
            System.out.println("signup time user id----------->"+user_id);


        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }

}
