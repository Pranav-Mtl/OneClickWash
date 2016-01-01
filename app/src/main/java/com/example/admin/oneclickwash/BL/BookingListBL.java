package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 16-12-2015.
 */
public class BookingListBL {

    public String getBookingList(String userid){

        String result=callWS(userid);
        String status=validate(result);
        return status;
    }

    private String callWS(String userid){
        String text = "";

        try {
            String URL="user_id="+userid;
            text= RestFullWS.serverRequest(URL, Constant.WS_BOOKING_LIST);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }

    private String validate(String result){
        String status="";
        if(result.trim().equals("[]")){
            status="failure";
        }
        else {
            status = "success";

            JSONParser jsonP = new JSONParser();
            try {
                Object obj = jsonP.parse(result);
                JSONArray jsonArrayObject = (JSONArray) obj;
                Constant.bookingID=new String[jsonArrayObject.size()];
                Constant.bookingdate=new String[jsonArrayObject.size()];
                Constant.bookingPickdate=new String[jsonArrayObject.size()];
                Constant.bookingPickTime=new String[jsonArrayObject.size()];
                Constant.bookingStatus=new String[jsonArrayObject.size()];
                Constant.bookingType=new String[jsonArrayObject.size()];
                Constant.bookingWindow=new int[jsonArrayObject.size()];
                Constant.bookingDryclean=new String[jsonArrayObject.size()];

                Constant.bookingDropdate=new String[jsonArrayObject.size()];
                Constant.bookingDropTime=new String[jsonArrayObject.size()];
                Constant.bookingDropWindow=new int[jsonArrayObject.size()];

                for (int i = 0; i < jsonArrayObject.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                    Constant.bookingID[i]=jsonObject.get("user_booking_id").toString();
                    Constant.bookingdate[i]=jsonObject.get("booking_date").toString();
                    Constant.bookingPickdate[i]=jsonObject.get("pickup_date").toString();
                    Constant.bookingPickTime[i]=jsonObject.get("pickup_time").toString();
                    Constant.bookingStatus[i]=jsonObject.get("status").toString();
                    Constant.bookingType[i]=jsonObject.get("booking_type").toString();
                    Constant.bookingWindow[i]=Integer.valueOf(jsonObject.get("window").toString());
                    Constant.bookingDryclean[i]=jsonObject.get("drycleaning").toString();
                    Constant.bookingDropdate[i]=jsonObject.get("drop_date").toString();
                    Constant.bookingDropTime[i]=jsonObject.get("drop_time").toString();
                    Constant.bookingDropWindow[i]=Integer.valueOf(jsonObject.get("drop_window").toString());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /*------------------------------------------------------*/


    public String cancelBooking(String bookingid,String dryclean){

        String result=callWSCancel(bookingid,dryclean);
        String status=validateCancel(result);
        return status;
    }

    private String callWSCancel(String bookingid,String dryclean){
        String text = "";

        try {
            String URL="booking_id="+bookingid+"&dryclean="+dryclean;
            text= RestFullWS.serverRequest(URL, Constant.WS_CANCEL_PICK);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }

    private String validateCancel(String result)
    {
        String text="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;


                JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
               text=jsonObject.get("result").toString();





        } catch (Exception e) {

            e.printStackTrace();
        }
        return text;
    }


}
