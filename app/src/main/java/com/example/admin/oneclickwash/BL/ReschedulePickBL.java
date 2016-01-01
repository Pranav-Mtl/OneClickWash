package com.example.admin.oneclickwash.BL;


import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 19-12-2015.
 */
public class ReschedulePickBL {

    public String rescheduleOrder(String bookingID,String date,String time,String type,String id){

        String result=callWS(bookingID,date,time,type,id);
        String status=validate(result);
        return status;
    }

    private String callWS(String bookingID,String date,String time,String type,String id){
        String text = "";

        try {
            String URL="booking_id="+bookingID+"&pickup_date="+date+"&pickup_time="+time+"&type="+type+"&slot_id="+id;
            text= RestFullWS.serverRequest(URL, Constant.WS_RESCHEDULE_PICK);

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
