package com.nybblemouse.fandos;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chrismckirgan on 06/05/15.
 */
public class ListenerService extends WearableListenerService {
    private static final String WEARABLE_DATA_PATH = "/FANDOS_DATA";
    public final static String EXTRA_MESSAGE = "com.nybblemouse.fandos.MESSAGE";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.v("fandos", "WATCH: DATA RECEIVED");
        DataMap dataMap;
        for (DataEvent event : dataEvents) {

            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEARABLE_DATA_PATH)) {}
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                Log.v("fandos", "DataMap received on watch: " + dataMap);

                // ## write to file
                String FILENAME = "chillis.txt";
                String string = dataMap.getString("chillis");
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                assert fos != null;
                try {
                    fos.write(string.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // ## end write to file

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(EXTRA_MESSAGE, dataMap.getString("chillis"));
                startActivity(intent);
            }
        }
    }
}

