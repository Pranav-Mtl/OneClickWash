package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.BE.BookingDetailBE;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 16-12-2015.
 */
public class BookingDetailBL {

    BookingDetailBE objBookingDetailBE;
    public String getBookingDetail(String bookingID,BookingDetailBE bookingDetailBE){

        objBookingDetailBE=bookingDetailBE;
        String result=callWS(bookingID);
         validate(result);
        return "";
    }

    private String callWS(String bookingID){
        String text = "";

        try {
            String URL="booking_id="+bookingID;
            text= RestFullWS.serverRequest(URL, Constant.WS_BOOKING_DETAIL);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }
    private void validate(String result){
        String userDetail;
        String bookingDetail;
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
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
                userDetail=jsonObject.get("user_details").toString();
                bookingDetail=jsonObject.get("booking_details").toString();
                parseUser(userDetail);
                parseBooking(bookingDetail);
            }
            catch (Exception e){
            }
        }
    }

    private void parseUser(String result){
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            objBookingDetailBE.setName(jsonObject.get("customer_name").toString());
            objBookingDetailBE.setAddress(jsonObject.get("address").toString());
            objBookingDetailBE.setMobile(jsonObject.get("phone").toString());
        }
        catch (Exception e){
        }
    }

    private void parseBooking(String result){
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            objBookingDetailBE.setService(jsonObject.get("order_type").toString());
            objBookingDetailBE.setStatus(jsonObject.get("booking_status").toString());
            objBookingDetailBE.setPickDate(jsonObject.get("pickup_date").toString());
            objBookingDetailBE.setPickTime(jsonObject.get("pickup_time").toString());
            objBookingDetailBE.setDropDate(jsonObject.get("dropoff_date").toString());
            objBookingDetailBE.setDropTime(jsonObject.get("dropoff_time").toString());
            objBookingDetailBE.setPaid(jsonObject.get("no_of_cloths").toString());
            objBookingDetailBE.setFree(jsonObject.get("no_of_free_cloths").toString());
        }
        catch (Exception e){
        }
    }

}
