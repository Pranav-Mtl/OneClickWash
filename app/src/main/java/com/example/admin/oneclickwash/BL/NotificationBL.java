package com.example.admin.oneclickwash.BL;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 28-12-2015.
 */
public class NotificationBL {
    public String getNotificationList(String userid){

        String result=callWS(userid);
        String status=validate(result);
        return status;
    }

    private String callWS(String userid){
        String text = "";

        try {
            String URL="user_id="+userid;
            text= RestFullWS.serverRequest(URL, Constant.WS_NOTIFICATION_LIST);

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
                Constant.notificationTitle=new String[jsonArrayObject.size()];
                Constant.notificationMessage=new String[jsonArrayObject.size()];
                Constant.notificationDate=new String[jsonArrayObject.size()];

                for (int i = 0; i < jsonArrayObject.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                    Constant.notificationTitle[i]=jsonObject.get("title").toString();
                    Constant.notificationMessage[i]=jsonObject.get("message").toString();
                    Constant.notificationDate[i]=jsonObject.get("date").toString();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return status;
    }

}
