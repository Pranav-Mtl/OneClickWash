package com.example.admin.oneclickwash.WS;

import android.util.Log;


import com.example.admin.oneclickwash.Constant.Constant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Created by Pranav Mittal on 10/31/2015.
 *
 */
public class RestFullWS {

    private final static String USER_AGENT = "Mozilla/5.0";

    public static String serverRequest(String URL,String WS_Name) {
        // TODO Auto-generated method stub

        String text="";
        try {
            //String url = "http://ec2-52-74-60-197.ap-southeast-1.compute.amazonaws.com/webservices/all_route.php?";

            URI uri = new URI(Constant.WS_HTTP, Constant.WS_DOMAIN_NAME,Constant.WS_PATH+WS_Name,URL, null);
            String ll=uri.toASCIIString();
            URL obj = new URL(ll);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            Log.d("URL-->", ll);

            // optional default is GET
            con.setRequestMethod("GET");



            //add request header
           // con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type","text/html; charset=iso-8859-1");
            System.setProperty("http.keepAlive", "false");


            int responseCode = con.getResponseCode();

            Log.d("Response Code-->", responseCode + "");
            Log.d("Content Type-->",con.getContentType()+"");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            text=response.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d("Response JSON-->",text);
        return text;
    }

    private static void sendGet() throws Exception {



    }



}
