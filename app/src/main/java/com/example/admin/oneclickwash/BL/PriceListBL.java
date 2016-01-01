package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 14-12-2015.
 */
public class PriceListBL {

    public String getPricingList(){
        String result=callWS();
        validate(result);
        return "";
    }

    private String callWS(){

        String URL="";
        String text= RestFullWS.serverRequest(URL, Constant.WS_PRICE_LIST);
        return text;
    }

    private void validate(String result)
    {
        String monthlyJson;
        String cleaningJson;
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            monthlyJson=jsonObject.get("subscription_model").toString();
            cleaningJson=jsonObject.get("drycleaning").toString();

            parseMonthlyJson(monthlyJson);
            parsePriceJson(cleaningJson);


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void parseMonthlyJson(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;

            Constant.pricingID=new String[jsonArrayObject.size()];
            Constant.pricingCloth=new String[jsonArrayObject.size()];
            Constant.pricingUser=new String[jsonArrayObject.size()];
            Constant.pricingOriginal=new String[jsonArrayObject.size()];
            Constant.pricingPackage=new String[jsonArrayObject.size()];
            Constant.pricingPickup=new String[jsonArrayObject.size()];
            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.pricingID[i]=jsonObject.get("model_id").toString();
                Constant.pricingCloth[i]=jsonObject.get("no_of_cloths").toString();
                Constant.pricingPickup[i]=jsonObject.get("no_of_pickups").toString();
                Constant.pricingOriginal[i]=jsonObject.get("original_price").toString();
                Constant.pricingPackage[i]=jsonObject.get("discount_price").toString();
                Constant.pricingUser[i]=jsonObject.get("subscription_name").toString();
            }





        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private void parsePriceJson(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray)obj;

            Constant.dryCleanID=new String[jsonArrayObject.size()];
            Constant.dryCleanItem=new String[jsonArrayObject.size()];
            Constant.dryCleanOriginal=new String[jsonArrayObject.size()];
            Constant.dryCleanPrice=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.dryCleanID[i]=jsonObject.get("drycleaning_id").toString();
                Constant.dryCleanItem[i]=jsonObject.get("item_name").toString();
                Constant.dryCleanOriginal[i]=jsonObject.get("original_rates").toString();
                Constant.dryCleanPrice[i]=jsonObject.get("discount_price").toString();

            }





        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
