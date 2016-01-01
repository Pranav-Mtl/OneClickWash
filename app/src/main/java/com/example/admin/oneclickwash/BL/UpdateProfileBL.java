package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 15-12-2015.
 */
public class UpdateProfileBL {

    public String updateProfile(String userid,String email,String name,String address){
        String result=callWS(userid,email,name,address);
        String status=validate(result);
        return status;
    }

    private String callWS(String userid,String email,String name,String address){
        String text = null;

            String URL="user_id="+userid+"&email="+email+"&name="+name+"&address="+address;
            text= RestFullWS.serverRequest(URL, Constant.WS_UPDATE_PROFILE);


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
