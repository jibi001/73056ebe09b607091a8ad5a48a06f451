package com.ceemart.ceemart;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ceemart.ceemart.controllers.DeviceCotroller;
import com.ceemart.ceemart.controllers.GPSTracker;

import com.ceemart.ceemart.controllers.SessionController;
import com.ceemart.ceemart.controllers.UpdateController;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView message;

    DeviceCotroller deviceCntrlr;
    Context context = this;
    GPSTracker gps;
    String macAddress;
    String accessToken;
    String updateDateTime;


    Button updateGo;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 5;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        SessionController session = new SessionController(context);
        accessToken = session.getAccessToken();
        updateDateTime = session.getLastUpdateTime();
        updateSyncing();

    }

    /* function updateSyncing
    * get update data list from server
    *
    * @param : Null
    *
    * @retun : Null
    */
    public void updateSyncing() {
        final UpdateController updateController = new UpdateController();

        try {
            updateController.updateSyncing("123", updateDateTime, getApplicationContext(), new VolleyCallback() {
                @Override
                public boolean onSuccessResponse(JSONObject result) {
                    Log.d("result", String.valueOf(result));

                    return false;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void LocationAccess() {
        gps = new GPSTracker(context, MainActivity.this);

        // check if GPS enabled
        // Check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            message.setText("Late : " + latitude + "\n" + "Long : " + longitude);
            Log.d("Lat : ", "Lat :" + latitude + "\n" + "Long : " + longitude);


        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }

    }

    public interface VolleyCallback {
        boolean onSuccessResponse(JSONObject result);

    }


}
