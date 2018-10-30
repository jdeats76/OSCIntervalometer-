------------------------------------------------------------------------------------------
-- Open Sphere Camera Intervalometer
------------------------------------------------------------------------------------------
-- Copyright 2018 Jeremy Deats
------------------------------------------------------------------------------------------
-- 
-- LICENSE:
-- This code is licensed under the GNU license (Gpl v3)
-- A complete transcript of the license can be found here
-- https://www.gnu.org/licenses/gpl-3.0.en.html
--
------------------------------------------------------------------------------------------
--
-- WARRANTY AND LEGAL TERMS OF USE:
-- This software is provided as is with no warranty or guarantee whatsoever.
-- By using this software you agree to assume all culpability for potential data loss,
-- hardware failures, accidents resulting in use and potential legal issues resulting from
-- photography without consent. By using and continuing to use this software, you assume
-- full culpability of any other possible unintended outcome not explicitly stated here.
-- TL;DR Use at your own risk.
--
-------------------------------------------------------------------------------------------

PURPOSE:
The purpose of this app is to provide Samsung Gear 360 (2017 edition) camera owners an Introvalometer feature though a custom Android app. This app using the open OSC API standard to manipulate the Gear 360 camera and can be easily modified to work with any OSC camera that supports v1 of the OSC API (see comments in source code). This App is not limited to Samsung phones and can run on any WiFi enabled Android device running Android Nougat 7.x or above.

What is an intervalometer? One popular feature of 360 cameras is the ability to take a series of automatic shots on time delay or step count. This feature is called a Intervalometer. To capture sequential 360 photos for the purpose of a scenario like Google Street View photography, an intervalometer is a required feature. Many 360 cameras have an intervalometer feature built into the companion software or on the device itself, however the Samsung Gear 360 and a few other model 360 cameras do not offer this feature. 

Google has provided an OSC (Open Spherical Camera) API which camera vendors are free to support as a standard way to enable custom software to interact and manipulate the camera. Google's Street View app for iOS and Android utilizes the OSC API to communicate with a variety of cameras and many popular cameras support the OSC API. A partial list can be viewed here at the URL below (look for "Street View Integration" in description):
https://www.google.com/streetview/publish/

Complete details on the OSC API can be found here:
https://developers.google.com/streetview/open-spherical-camera/

-----------------------------------------------------------------------------------------

SETUP INSTRUCTIONS:

The Samsung Gear 360 camera does not ship with OSC API support built in. Prior to the OSCIntravelometer App you will need to update the firmware on the Gear 360 camera to enable OSC API support. You can do this through the Samsung Gear 360 App.  

Once you have the firmware updated, follow these steps

1. Power on the Gear 360 camera 

2. Hold down the menu button until the LED display on the camera reads
 "Connect to Street View". 

3. Hit the shutter button above the LED display to engage "Connect to Street View"

3. Go to the WiFi settings on your phone and look for a WiFi network named something similar to "Gear360(...).OSC". Proceed to connect to that WiFi network. The network is password protected. The needed password can be found on the LED screen on the camera.

4. Once you have successfully connected by WiFi to your Gear 360 camera launch the OSCIntervalometer App.

5. Set the number of seconds you want to delay between each photo.

6. Tap "Start Session" button to begin.

 