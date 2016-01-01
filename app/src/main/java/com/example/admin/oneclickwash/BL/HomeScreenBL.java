package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.BE.HomeScreenBE;
import com.example.admin.oneclickwash.BE.SubscriptionDetailBE;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 09-12-2015.
 */
public class HomeScreenBL {

    HomeScreenBE objHomeScreenBE;
    SubscriptionDetailBE objSubscriptionDetailBE;

    public String getRecord(String id,HomeScreenBE homeScreenBE,SubscriptionDetailBE subscriptionDetailBE)
    {
            objSubscriptionDetailBE=subscriptionDetailBE;
            objHomeScreenBE=homeScreenBE;
            String result = fetRecord(id);
            String status = validate(result);

        return status;
    }

    private String fetRecord(String id) {
        String text = null;

        try
        {
            //http://oneclickwash.com/webservices/login.php?mobile=&password=&gcm_id=
            String URL="user_id="+id;
            text= RestFullWS.serverRequest(URL, Constant.WS_USER_DETAILS);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }



    private String validate(String strValue)    {


        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            objHomeScreenBE.setName(jsonObject.get("user_name").toString());
            objHomeScreenBE.setStatus(jsonObject.get("status").toString());
            objHomeScreenBE.setEmail(jsonObject.get("user_email").toString());
            objHomeScreenBE.setMobile(jsonObject.get("phone").toString());
            objHomeScreenBE.setAddress(jsonObject.get("address").toString());
            objHomeScreenBE.setWallet(jsonObject.get("wallet").toString());

            if(objHomeScreenBE.getStatus().equalsIgnoreCase(Constant.subscriptionActive)){
                String sub=jsonObject.get("sub_detail").toString();
                parseSubscription(sub);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return "";
    }

    private void parseSubscription(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            objSubscriptionDetailBE.setStartDate(jsonObject.get("start_date").toString());
            objSubscriptionDetailBE.setEndDate(jsonObject.get("end_date").toString());
            objSubscriptionDetailBE.setTotalPickup(jsonObject.get("pickup").toString());
            objSubscriptionDetailBE.setRemainingPickup(jsonObject.get("rem_pickup").toString());
            objSubscriptionDetailBE.setTotalCloth(jsonObject.get("total_cloth").toString());
            objSubscriptionDetailBE.setRemainingCloth(jsonObject.get("rem_cloth").toString());
            objSubscriptionDetailBE.setFreeCloth(jsonObject.get("free_cloth").toString());
            objSubscriptionDetailBE.setPackageAmount(jsonObject.get("price").toString());
            objSubscriptionDetailBE.setPackageName(jsonObject.get("package_name").toString());


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
