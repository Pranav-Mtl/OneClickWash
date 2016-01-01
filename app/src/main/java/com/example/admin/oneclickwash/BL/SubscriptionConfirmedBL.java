package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.BE.SubscriptionConfirmedBE;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 10-12-2015.
 */
public class SubscriptionConfirmedBL {

    SubscriptionConfirmedBE objSubscriptionConfirmedBE;
    public String  subscriptionID="";
    public String subscriptionDone(SubscriptionConfirmedBE subscriptionConfirmedBE)
    {
        objSubscriptionConfirmedBE=subscriptionConfirmedBE;
        String result=callWS();
        String status=validate(result);
        return status;
    }
    private String callWS()
    {
        if(objSubscriptionConfirmedBE.getReferalCode()==null){
            objSubscriptionConfirmedBE.setReferalCode("");
        }
        //oneclickwash.com/webservices/user_subscribe.php?user_id=&sub_id=&type=&code
        String URL="user_id="+objSubscriptionConfirmedBE.getUserID()+"&sub_id="+objSubscriptionConfirmedBE.getSubscriptionID()+"&type="+objSubscriptionConfirmedBE.getPaymentType()+"&code="+objSubscriptionConfirmedBE.getReferalCode()+"&device_id="+objSubscriptionConfirmedBE.getDeviceID();
        String txtJson= RestFullWS.serverRequest(URL, Constant.WS_SUBSCRIPTION_DONE);
        return txtJson;
    }

    private String validate(String result){

        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();

            if(status.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)){
                subscriptionID=jsonObject.get("user_subscription_id").toString();
            }
        }catch (Exception e){

        }

        return status;
    }

}
