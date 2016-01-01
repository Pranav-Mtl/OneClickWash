package com.example.admin.oneclickwash.BL;

import android.content.Context;

import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 10-12-2015.
 */
public class ChangePasswordBL {

    public String changePassowrd(String id,String oldPassword,String newPassowrd)
    {



       String result = fetRecord(id,oldPassword,newPassowrd);
        String finalvalue = validate(result);




        return finalvalue;


    }

    private String fetRecord(String id,String oldPassword,String newPassowrd) {
        String text = null;

        try
        {

            //http://oneclickwash.com/webservices/password_update.php?user_id=20&old_pass=p&new_pass=p
            //http://oneclickwash.com/webservices/login.php?mobile=&password=&gcm_id=
            String URL="user_id="+id+"&old_pass="+oldPassword+"&new_pass="+newPassowrd;
            text= RestFullWS.serverRequest(URL,Constant.WS_CHANGE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }



    public String validate(String strValue)    {

        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();



        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }

}
