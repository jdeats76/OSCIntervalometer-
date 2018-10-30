/*
 ******* Open Spherical Camera helper library for Java Android *********************************
 * Copyright 2018 Jeremy Deata
 *
 * For more information on the OSC Street View API please see
 * https://developers.google.com/streetview/open-spherical-camera/reference/
 *
 * LICENSE:
 * This code is licensed under the GNU license (Gpl v3)
 * A complete transcript of the license can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * WARRANTY:
 * This software is provided as is with no warranty or guarantee whatsoever.
 * By using this software you agree to assume all culpability for potential data loss,
 * hardware failures, accidents resulting in use and potential legal issues resulting from
 * photography without consent. By using and continuing to use this software, you assume
 * full culpability of any other possible unintended outcome not explicitly stated here.
 *
 ***********************************************************************************************
 */

package com.oscintervalometer.immersha.oscintervalometer;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


// Utility class for OSC (Open Spherical Camera) API
// The OSC API is a standard developed by Google StreetView team that has been adopted
// by several camera vendors including Samsung.
//
// The intent of the application is to provide Samsung Gear 360 (2017) owners an intervalometer
// Functionality that is native to most 360 cameras but absent from Samsung's Gear 360 app.
//
// Samsung Gear 260 camera only supports v1 of the OSC API. The implementation of the
// wrapper library provide the basics: session start, takepicture, sessionend
// The API provides a lot more possibilities.
// For more information on the OSC API please see.
// https://developers.google.com/streetview/open-spherical-camera/
//
public class OSCHelper {
    // Get string from input stream
    private String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }


    // CreateSession
    public String CreateSession() {
        String result = "Failed";
        try {

            // NOTE: 192.168.43.1 is the Gear 360's IP Address. Each camera vendor will have
            // a unique IP address for their product. This IP address is the only part of the code
            // that must be changed in order for this code to be used by other OSC Compliant cameras.
            // set url... TO DO: Add camera's IP as a configurable option.
            URL githubEndpoint = new URL("http://192.168.43.1:80/osc/commands/execute");

            // create connection obj
            HttpURLConnection myConnection =
                    (HttpURLConnection) githubEndpoint.openConnection();

            myConnection.setRequestMethod("POST");

            // set headers
            myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            myConnection.setRequestProperty("Accept", "application/json");
            myConnection.setRequestProperty("X-XSRF-Protected", "1");

            // Create the data
            String myData = "{\"name\":\"camera.startSession\",\"parameters\":{}}";

            myConnection.setDoOutput(true);
            myConnection.getOutputStream().write(myData.getBytes());

            if (myConnection.getResponseCode() == 200) {
                InputStream responseBody = myConnection.getInputStream();
                result = getStringFromInputStream(responseBody);

                // split
                String firstPart = "{ \"name\": \"camera.startSession\", \"state\": \"done\", \"results\": { \"sessionId\": \"";
                String lastPart = "\", \"timeout\": 300 } }";

                // quick and dirty parse. No need to bring the JSON parser into this for something
                // some small.
                result = result.replace(firstPart, "");
                result = result.replace(lastPart, "");

            }

        } catch (IOException e) {
            // TO DO: Log exception
            result = "Failed";
        }
        return result;
    }

    // take photo
    public String TakePhoto(String sessionId) {
        String result = "Failed";
        try {
            // set url
            URL githubEndpoint = new URL("http://192.168.43.1:80/osc/commands/execute");

            // create connection obj
            HttpURLConnection myConnection =
                    (HttpURLConnection) githubEndpoint.openConnection();

            myConnection.setRequestMethod("POST");

            // set headers
            myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            myConnection.setRequestProperty("Accept", "application/json");
            myConnection.setRequestProperty("X-XSRF-Protected", "1");

            // Create the data
            String myData = "{\"name\":\"camera.takePicture\",\"parameters\":{\"sessionId\":\"" + sessionId + "\"}}";

            myConnection.setDoOutput(true);
            myConnection.getOutputStream().write(myData.getBytes());

            if (myConnection.getResponseCode() == 200) {
                InputStream responseBody = myConnection.getInputStream();
                result = getStringFromInputStream(responseBody);


            }

        } catch (IOException e) {
            result = "Failed";
        }
        return result;
    }

    public String EndSession(String sessionId) {
        String result = "Failed";
        try {
            // set url
            URL githubEndpoint = new URL("http://192.168.43.1:80/osc/commands/execute");

            // create connection obj
            HttpURLConnection myConnection =
                    (HttpURLConnection) githubEndpoint.openConnection();

            myConnection.setRequestMethod("POST");

            // set headers
            myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            myConnection.setRequestProperty("Accept", "application/json");
            myConnection.setRequestProperty("X-XSRF-Protected", "1");

            // Create the data
            String myData = "{\"name\":\"camera.takePicture\",\"parameters\":{\"sessionId\":\"" + sessionId + "\"}}";

            myConnection.setDoOutput(true);
            myConnection.getOutputStream().write(myData.getBytes());

            if (myConnection.getResponseCode() == 200) {
                InputStream responseBody = myConnection.getInputStream();
                result = getStringFromInputStream(responseBody);

            }

        } catch (IOException e) {
            result = "Failed";
        }
        return result;
    }

}
