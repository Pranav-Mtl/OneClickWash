package com.example.admin.oneclickwash.BL;

import android.util.Log;

import com.example.admin.oneclickwash.BE.PaymentBE;
import com.example.admin.oneclickwash.BE.PaymentReviewBE;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 18-12-2015.
 */
public class PaymentBL {

    PaymentBE objPaymentReviewBE;
    public String getSubscriptiondata(String id,String sub_id,String price,String wallet,PaymentBE paymentBE)
    {
        objPaymentReviewBE=paymentBE;
        String result = callWS(id,sub_id,price,wallet);
        String finalvalue = validate(result);
        return finalvalue;


    }

    private String callWS(String id,String sub_id,String price,String wallet) {
        String text = null;

        try
        {

            String URL="base_price="+price+"&user_id="+id+"&user_subscription_id="+sub_id+"&wallet="+wallet;
            text= RestFullWS.serverRequest(URL, Constant.WS_SUBSCRIPTION_PAYMENT);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }



    public String validate(String strValue)    {

        boolean status;
        String url="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=(boolean)jsonObject.get("success");

            Log.d("STATUS URL-->", status + "");

            if(status)
            {

                /*String link=jsonObject.get("link").toString();
                Log.d("link URL-->",link);*/

                objPaymentReviewBE.setUserName(jsonObject.get("user_fullname").toString());
                objPaymentReviewBE.setMobileNo(jsonObject.get("user_mobile").toString());
                objPaymentReviewBE.setEmailID(jsonObject.get("user_email").toString());

                jsonObject=(JSONObject)jsonP.parse(jsonObject.get("link").toString());
                url=jsonObject.get("url").toString();

                Log.d("WEB URL-->",url);

            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return url;
    }
}
