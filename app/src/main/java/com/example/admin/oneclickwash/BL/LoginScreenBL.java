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
public class LoginScreenBL {

    String finalvalue;
    String phone,password;
    String status;
    Context context;

    public String loginRecord(String phone,String password,String gcmId,String deviceId,Context context)
    {

        this.context=context;
        this.phone=phone;
        this.password=password;


        try {
            String result = fetRecord(phone,password,gcmId,deviceId);
            finalvalue = validate(result);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return finalvalue;


    }

    private String fetRecord(String phone,String password,String gcmID,String deviceID) {
        String text = null;

        try
        {
            //http://oneclickwash.com/webservices/login.php?mobile=&password=&gcm_id=
            String URL="mobile="+phone+"&password="+password+"&gcm_id="+gcmID+"&device_id="+deviceID;
            text= RestFullWS.serverRequest(URL, "login.php");

        }
        catch (Exception e)
        {
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

            if(status.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS))
            {
                String user_id=jsonObject.get("user_id").toString();
                Util.setSharedPrefrenceValue(context, Constant.PREFS_NAME, Constant.SP_USER_ID, user_id);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }

}



