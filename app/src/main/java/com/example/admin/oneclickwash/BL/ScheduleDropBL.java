package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 19-12-2015.
 */
public class ScheduleDropBL {


    public String setDrop(String bookingID,String date,String time,String type,String slotID){
        String result=callWS(bookingID,date,time,type,slotID);
        String status=validate(result);
        return status;
    }

    private String callWS(String bookingID,String date,String time,String type,String slotID){
        //booking_id=&drop_date=&drop_time=


        String URL="booking_id="+bookingID+"&drop_date="+date+"&drop_time="+time+"&type="+type+"&slot_id="+slotID;
        String text= RestFullWS.serverRequest(URL, Constant.WS_SCHEDULE_DROP);
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


        } catch (Exception e) {

            e.printStackTrace();
        }

        return status;
    }

    public String setReDrop(String bookingID,String date,String time,String type,String slotID){
        String result=callReWS(bookingID,date,time,type,slotID);
        String status=Revalidate(result);
        return status;
    }

    private String callReWS(String bookingID,String date,String time,String type,String slotID){
        //booking_id=&drop_date=&drop_time=


        String URL="booking_id="+bookingID+"&drop_date="+date+"&drop_time="+time+"&type="+type+"&slot_id="+slotID;
        String text= RestFullWS.serverRequest(URL, Constant.WS_RESCHEDULE_DROP);
        return text;
    }
    private String Revalidate(String result)
    {

        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();


        } catch (Exception e) {

            e.printStackTrace();
        }

        return status;
    }

}
