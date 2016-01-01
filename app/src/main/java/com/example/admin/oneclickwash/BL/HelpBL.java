package com.example.admin.oneclickwash.BL;

import android.content.Context;

import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 14-12-2015.
 */
public class HelpBL {

    public String sendMessage(String id,String message)
    {


            String result = fetRecord(id, message);
            String finalvalue = validate(result);


        return finalvalue;


    }

    private String fetRecord(String id,String message) {
        String text = null;

        try
        {
            //http://oneclickwash.com/webservices/login.php?mobile=&password=&gcm_id=
            String URL="user_id="+id+"&comment="+message;
            text= RestFullWS.serverRequest(URL,Constant.WS_HELP);

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
