package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by appslure on 15-12-2015.
 */
public class GetTimeSlotBL
{
    public List list,listSLotID;
    public List dropList,dropListSLotID;



    public String getTimeSlots(String date)
    {
        list=new ArrayList();
        listSLotID=new ArrayList();

        String result = callWS(date);
        String finalvalue = validate(result);
        return finalvalue;
    }

    private String callWS(String date) {
        String text = "";

        try {
            String URL="pick_date="+date;
            text= RestFullWS.serverRequest(URL, Constant.WS_TIME_SLOT);

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
            status=jsonObject.get("status").toString();
            if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(status)){
                String timeslot=jsonObject.get("timeslot").toString();
                parseSlot(timeslot);
            }



        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }

    private void parseSlot(String result){

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;
            list.add("Select Time Slot");
            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                String valid=jsonObject.get("show").toString();
                if(valid.equalsIgnoreCase("y"))
                 list.add(jsonObject.get("timeslot").toString());
                 listSLotID.add(jsonObject.get("slot_id").toString());
            }




        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /*-------------------------------------------------------------------------------------------------*/


    public String getTimeSlotsDrop(String date)
    {
        dropList=new ArrayList();
        dropListSLotID=new ArrayList();

        String result = callWSDrop(date);
        String finalvalue = validateDrop(result);
        return finalvalue;
    }

    private String callWSDrop(String date) {
        String text = "";

        try {
            String URL="drop_date="+date;
            text= RestFullWS.serverRequest(URL, Constant.WS_DROP_SLOT);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }



    private String validateDrop(String strValue)    {

        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("status").toString();
            if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(status)){

                String timeslot=jsonObject.get("timeslot").toString();
                parseSlotDrop(timeslot);
            }



        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }

    private void parseSlotDrop(String result){

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;
            dropList.add("Select Time Slot");
            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(i).toString());
                String valid=jsonObject.get("show").toString();
                if(valid.equalsIgnoreCase("y")) {
                    dropList.add(jsonObject.get("timeslot").toString());
                    dropListSLotID.add(jsonObject.get("slot_id").toString());
                }
            }




        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}
