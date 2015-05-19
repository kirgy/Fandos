package com.nybblemouse.fandos;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by chrismckirgan on 05/05/15.
 */
public class GcmMessageHandler extends IntentService {
    GoogleApiClient googleClient;
    String mes;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        showToast();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("message"));
        String rawMessage = extras.getString("message");
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
                .build();
        googleClient.connect();

        JSONObject jsonMessage = null;
        try {
            jsonMessage = new JSONObject(rawMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendData(jsonMessage);

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }
    public void sendData(JSONObject jsonMessage) {
        Log.v("fandos", "MOBILE: SENDING DATA!");
        String WEARABLE_DATA_PATH = "/FANDOS_DATA";

        String Chillis = "?";
        try {
            Chillis = jsonMessage.getString("chillis");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a DataMap object and send it to the data layer
        DataMap dataMap = new DataMap();
        dataMap.putLong("time", new Date().getTime());
        dataMap.putString("FROM", "MOBILE");
        dataMap.putString("chillis", Chillis);
        //Requires a new thread to avoid blocking the UI
        new SendToDataLayerThread(WEARABLE_DATA_PATH, dataMap).start();
    }


    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
            }
        });

    }

    // inner class @TODO: seperate out
    class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;
        }

        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleClient).await();
            for (Node node : nodes.getNodes()) {

                // Construct a DataRequest and send over the data layer
                PutDataMapRequest putDMR = PutDataMapRequest.create(path);
                putDMR.getDataMap().putAll(dataMap);
                PutDataRequest request = putDMR.asPutDataRequest();
                DataApi.DataItemResult result = Wearable.DataApi.putDataItem(googleClient,request).await();
                if (result.getStatus().isSuccess()) {
                    Log.v("fandos", "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.v("fandos", "ERROR: failed to send DataMap");
                }
            }
        }
    }
}
