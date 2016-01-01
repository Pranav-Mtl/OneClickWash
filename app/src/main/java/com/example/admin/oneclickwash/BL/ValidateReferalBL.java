package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 17-12-2015.
 */
public class ValidateReferalBL {

    public String getCode(String userid,String code){
        String result=callWS(userid,code);
        String status=validate(result);
        return status;
    }

    private String callWS(String userid,String code){
        String text = null;

        String URL="device_id="+userid+"&code="+code;
        text= RestFullWS.serverRequest(URL, Constant.WS_VALIDATE_REFERRAL);
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
