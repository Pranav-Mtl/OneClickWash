package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 16-12-2015.
 */
public class BookingRateBL {

    public String getRaingdata(String id,String bookingID,String rating,String comment)
    {



        String result = callWS( id,bookingID,rating,comment);
        String finalvalue = validate(result);




        return finalvalue;


    }

    private String callWS(String id,String bookingID,String rating,String comment) {
        String text = null;

        try
        {

            String URL="user_id="+id+"&booking_id="+bookingID+"&rating="+rating+"&comment="+comment;
            text= RestFullWS.serverRequest(URL, Constant.WS_FEEDBACK);

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
