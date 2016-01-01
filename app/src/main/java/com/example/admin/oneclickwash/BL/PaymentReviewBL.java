package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.BE.PaymentReviewBE;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 18-12-2015.
 */
public class PaymentReviewBL {

    PaymentReviewBE objPaymentReviewBE;
    public String getSubscriptiondata(String id,String userID,PaymentReviewBE paymentReviewBE)
    {
        objPaymentReviewBE=paymentReviewBE;
        String result = callWS(id,userID);
        String finalvalue = validate(result);
        return finalvalue;


    }

    private String callWS(String id,String userID) {
        String text = null;

        try
        {

            String URL="user_subscription_id="+id+"&user_id="+userID;
            text= RestFullWS.serverRequest(URL, Constant.WS_SUBSCRIPTION_DETAIL);

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
            objPaymentReviewBE.setPackageName(jsonObject.get("package_name").toString());
            objPaymentReviewBE.setAmount(jsonObject.get("discount_price").toString());
            objPaymentReviewBE.setCloth(jsonObject.get("no_of_cloth").toString());
            objPaymentReviewBE.setPickup(jsonObject.get("no_of_pickup").toString());
            objPaymentReviewBE.setWallet(jsonObject.get("wallet").toString());
            objPaymentReviewBE.setPackageID(jsonObject.get("subs_id").toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
        return status;
    }

    /*------------------CANCEL SUBSCRIPTION ------------------------*/

    public String cancelSubscription(String subscription){
        String result=callWSCancel(subscription);
        String status=validateCancel(result);
        return status;
    }

    private String callWSCancel(String id) {
        String text = null;
        try
        {
            String URL="user_subscription_id="+id;
            text= RestFullWS.serverRequest(URL, Constant.WS_SUBSCRIPTION_CANCEL);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }
    public String validateCancel(String strValue)    {

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

    /* ----------------------- CALL WEBSERVICE WHEN NO NEED TO CALL PAYMENT GATEWAY -------------------------*/

    public String subscriptionSuccess(String subscriptionID,String userID){
        String result=callWSSuccess(subscriptionID, userID);
        String status=validateSuccess(result);
        return status;
    }

    private String callWSSuccess(String subscriptionID,String userID) {
        String text = null;
        try
        {
            String URL="subscription_id="+subscriptionID+"&user_id="+userID;
            text= RestFullWS.serverRequest(URL, Constant.WS_SUBSCRIPTION_OK);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }
    public String validateSuccess(String strValue)    {

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
