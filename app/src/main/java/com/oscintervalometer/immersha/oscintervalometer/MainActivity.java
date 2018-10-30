/*
 ******* OSCIntervalometer: MainActivity class  *********************************
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



import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private int _intervalDelay = 0;
    private int _intervalCount = 0;
    private int _snapshotCount = 0;

    Button btnSessionStart;
    Button btnSessionEnd;
    TextView txtStatus;
    EditText txtInterval;
    Handler mHandler;
    public  Timer _intervalTimer;
    private OSCHelper osclib;
    private String sessionId = "";
    private Boolean sessionStarted = false;
    private Boolean killSession = false;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _intervalTimer = new Timer();
        osclib = new OSCHelper();

        // set local reference for UI views
        txtStatus = (TextView)findViewById(R.id.statusText);
        btnSessionStart = (Button)findViewById(R.id.btnSessionEnd);
        btnSessionEnd = (Button)findViewById(R.id.btnSessionStart);
        txtInterval = (EditText)findViewById(R.id.txtIntervalSeconds);

        // PARTIAL_WAKE_LOCK will keep CPU running even if screen goes to sleep.
        // Modify as needed.
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"OSCIntervalometer::BzX1025");


        // for dealing with UI calls from work threads.
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                switch(message.what) {
                    case MessageConst.SET_CONNECTED:
                        txtStatus.setText("Connected to Gear 360");
                        btnSessionEnd.setEnabled(true);

                        break;
                    case MessageConst.SET_DISCONNECTED:
                        txtStatus.setText("Session closed: Total image count: " + Integer.toString(_snapshotCount));
                        wakeLock.release();
                        killSession = false;
                        _snapshotCount = 0;

                        break;
                    case MessageConst.SET_UPDATE_STATUS:
                        txtStatus.setText("Image count: " + Integer.toString(_snapshotCount));
                        break;
                    case MessageConst.SHOW_FAILED_CONNECT_ALERT:
                        ShowAlert("Failed to connect", "Gear 360 camera not found");

                        txtStatus.setText("");
                        break;
                }
            }
        };
    }


    // show modal dialog
    public void ShowAlert(String title, String bodyText) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(bodyText);
        alert.show();
    }


    // handle btnSessionStart OnClick
    public void btnSessionStartClick(View view) {

        if (sessionStarted == false) {
            try {
                _intervalDelay = Integer.parseInt(txtInterval.getText().toString());
                // Going through the API with camera default settings, the Gear 360 camera needs
                // about 8 seconds to process is photo. This may change with future firmware updates.
                // Also note that the cameras properties can be changed through the API. If you need
                // more granularity tha 15 second delay, look into camera settings
                if (_intervalDelay >= 15) {
                    txtStatus.setText("Attempting to connect to camera...");
                    _intervalTimer.schedule(new IntTask(), 1000);
                    wakeLock.acquire();
                } else {
                    ShowAlert("Invalid interval", "Please enter a valid numeric value greater than 15 for delay");
                }
                //  btnSessionStart.setEnabled(false);

            } catch (Exception e) {
                ShowAlert("Delay not set", "Please enter a valid numeric value greater than 15 for delay");
            }
        }    else {
            ShowAlert("Session error", "Session is active. End current session to start a new session");
        }
    }


    // handle btnSessionEnd OnClick
    public void btnSessionEndClick(View view) {
        if (sessionStarted == true) {
            txtStatus.setText("Closing session with Gear 360");
            killSession = true;
        } else {
             ShowAlert("Session error", "No session to terminate");
        }
    }


    // this will one every seconds.
    class IntTask extends TimerTask {
        public void run() {

            // test to see if user has opted to klll the session.
            if (killSession == true) {

                    sessionStarted = false;
                    osclib.EndSession(sessionId);
                    Message msg = mHandler.obtainMessage(MessageConst.SET_DISCONNECTED);
                    msg.sendToTarget();
                    _intervalCount = 0;
            }

            // otherwise handle timer tick.
            if (killSession == false) {
                // _intervalcount = 0 + sessionStarted = false means we're running this for first time
                if (_intervalCount == 0) {
                    if (sessionStarted == false) {
                        // attempt to get sessionid from OSC (Open Spherical Camera) compliant device.
                        sessionId = osclib.CreateSession();
                        if (sessionId == "Failed") {
                            Message msg = mHandler.obtainMessage(MessageConst.SHOW_FAILED_CONNECT_ALERT);
                            msg.sendToTarget();

                        } else {
                            Message msg = mHandler.obtainMessage(MessageConst.SET_CONNECTED);
                            msg.sendToTarget();


                            sessionStarted = true;
                        }
                    }
                }
                if (sessionStarted == true) {
                    _intervalCount++;
                    if (_intervalCount == _intervalDelay) {
                        osclib.TakePhoto(sessionId);
                        _snapshotCount++;
                        Message msg = mHandler.obtainMessage(MessageConst.SET_UPDATE_STATUS);
                        msg.sendToTarget();
                        _intervalCount = 0;
                    }
                    _intervalTimer.schedule(new IntTask(), 1000);
                }

            }
        }
    }
}

