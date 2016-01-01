package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.BE.PlaceOrderBE;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 15-12-2015.
 */
public class PlaceOrderBL {

    PlaceOrderBE objPlaceOrderBE;
    public String placeOrder(String userID,PlaceOrderBE placeOrderBE){
        objPlaceOrderBE=placeOrderBE;
        String result=callWS(userID);
        String status=validate(result);
        return status;
    }

    private String callWS(String userid){
        String text = "";

        try {
            String URL="user_id="+userid+"&pickup_date="+objPlaceOrderBE.getDate()+"&pickup_time="+objPlaceOrderBE.getTimeSlot()+"&dry_clean="+objPlaceOrderBE.isDryClean()+"&no_of_clothes="+objPlaceOrderBE.getDryCleanCloth()+"&slot_id="+objPlaceOrderBE.getSlotID();
            text= RestFullWS.serverRequest(URL, Constant.WS_PLACE_ORDER);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }

    private String validate(String strValue)    {

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
