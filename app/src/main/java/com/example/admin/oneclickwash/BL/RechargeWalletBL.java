package com.example.admin.oneclickwash.BL;

import android.util.Log;

import com.example.admin.oneclickwash.BE.RechargeWalletBE;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 18-12-2015.
 */
public class RechargeWalletBL {

    RechargeWalletBE objRechargeWalletBE;

    public String getURL(String amount,String userID,RechargeWalletBE rechargeWalletBE){

        objRechargeWalletBE=rechargeWalletBE;
        String result=callWsUrl(amount,userID);  // call webservice
        String status=validate(result);           // parse json
        return status;
    }


    private String callWsUrl(String amount,String userID){

        String URL="base_price="+amount+"&user_id="+userID;
        String txtJson= RestFullWS.serverRequest(URL, Constant.WS_BUT_CREDIT);
        return txtJson;
    }

    private String validate(String result){

        boolean status;
        String url="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=(boolean)jsonObject.get("success");

            Log.d("STATUS URL-->", status + "");

            if(status)
            {

                /*String link=jsonObject.get("link").toString();
                Log.d("link URL-->",link);*/

                objRechargeWalletBE.setUserName(jsonObject.get("user_fullname").toString());
                objRechargeWalletBE.setMobileNo(jsonObject.get("user_mobile").toString());
                objRechargeWalletBE.setEmailID(jsonObject.get("user_email").toString());

                jsonObject=(JSONObject)jsonP.parse(jsonObject.get("link").toString());
                url=jsonObject.get("url").toString();

                Log.d("WEB URL-->",url);

            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return url;
    }
    /*---------------------------------*/

    public String getCurrentBalance(String userid){
        String result=callWsUrlBal(userid);
        String status=validateBal(result);
        return status;
    }

    private String callWsUrlBal(String userID){

        String URL="user_id="+userID;
        String txtJson= RestFullWS.serverRequest(URL, Constant.WS_WALLET_AMOUNT);
        return txtJson;
    }

    private String validateBal(String result){
        String status="";

        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("result").toString();
            if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(status))
            Constant.WALLET=jsonObject.get("wallet").toString();




        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }



}
