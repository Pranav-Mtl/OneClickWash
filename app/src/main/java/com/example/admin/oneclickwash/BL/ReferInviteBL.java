package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.BE.ReferInviteBE;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 15-12-2015.
 */
public class ReferInviteBL {

    ReferInviteBE objReferInviteBE;

    public String getReferCode(String userid,ReferInviteBE referInviteBE){
        objReferInviteBE=referInviteBE;
        String result=callWS(userid);
        String status=validate(result);
        return status;
    }

    private String callWS(String userid){
        String URL="user_id="+userid;
        String text= RestFullWS.serverRequest(URL, Constant.WS_GET_REFER);
        return text;
    }

    private String validate(String result)
    {

        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();
            if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(status)) {
                objReferInviteBE.setReferCode(jsonObject.get("referral_code").toString());
                objReferInviteBE.setReferValue(jsonObject.get("referral_value").toString());
            }




        } catch (Exception e) {

            e.printStackTrace();
        }

        return status;
    }

}
