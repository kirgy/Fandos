package com.nybblemouse.fandos;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    GoogleCloudMessaging gcm;

    String regid;
    String PROJECT_NUMBER = "955148347938"; // Replace with your GCM Project Number
    String[] mRegDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById( R.id.login );
        loginButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRegId();
            }
        });

    }

    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                TextView info = (TextView) findViewById(R.id.info);
                info.setText("Registering device with server...");

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    info.setText("Device successfully registered with server.");
                    Log.i("GCM", "Device registered, registration ID=" + regid);

                    final EditText usernameEditText = (EditText) findViewById(R.id.username);
                    final EditText passwordEditText = (EditText) findViewById(R.id.password);

                    mRegDetails = new String[3];
                    mRegDetails[0] = usernameEditText.getText().toString();
                    mRegDetails[1] = passwordEditText.getText().toString();
                    mRegDetails[2] = regid;
                    registerDevice();
                } catch (IOException ex) {
                    Log.i("GCM", "Error :" + ex.getMessage());
                    info.setText("Device failed to register with GCM server. Are you connected to the internet?");
                }
                return regid;
            }

            @Override
            protected void onPostExecute(String msg) {
                //etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }

    public Boolean registerDevice() {
        TextView info = (TextView) findViewById(R.id.info);
        info.setText("Registering device with Fandos server...");

        Boolean status = false;
        //@TODO: post these values to the server

        return status;
    }



    //@Override
    public void onClick(View v) {
        getRegId();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
