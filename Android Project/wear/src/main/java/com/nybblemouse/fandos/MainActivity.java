package com.nybblemouse.fandos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.nybblemouse.fandos.MESSAGE";
    private Boolean mCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mCreated = true;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus ) {
        if(hasFocus && mCreated){
            final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);


            String message;

            // Read chilli value from storage before we check if theres an update to make
            int ch;
            StringBuffer fileContent = new StringBuffer("");
            FileInputStream fis;
            try {
                Context context = getApplicationContext();
                fis = context.openFileInput( "chillis.txt" );
                try {
                    while( (ch = fis.read()) != -1)
                        fileContent.append((char)ch);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String data = new String(fileContent);
            message = data;

            if(getIntent() != null && getIntent().getExtras() != null) {
                Intent intent = getIntent();
                message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
                Log.v("fandos", "Chillis: " +message);

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] vibrationPattern = {0, 500, 50, 300};
                //-1 - don't repeat
                final int indexInPatternToRepeat = -1;
                vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);

                // show cta screen
                final ImageView titleLogo = (ImageView) findViewById(R.id.titleLogo);
                titleLogo.setImageResource(R.drawable.title);

                final ImageView cta = (ImageView) findViewById(R.id.ctaTitle);
                cta.setImageResource(R.drawable.cta);
                Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
                pulse.setRepeatCount(Animation.INFINITE);
                cta.startAnimation(pulse);

                final String clickChilli = message;
                cta.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                        fadeout.setAnimationListener(new Animation.AnimationListener(){
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation arg0) {
                                titleLogo.setVisibility(View.GONE);
                                cta.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        titleLogo.startAnimation(fadeout);
                        cta.startAnimation(fadeout);
                        animateChillis(clickChilli, stub);
                    }
                });
            } else {
                animateChillis(message, stub);
            }
        }
    }

    public void animateChillis(String message, WatchViewStub stub) {
        final ImageView chilliWheel = (ImageView) stub.findViewById(R.id.chillis);
        final Resources res = getResources();
        final int chilliItems = Integer.parseInt(message) + 1;

        for(int i=0; i <= Integer.parseInt(message) + 1; i++) {
            String mDrawableName = "wheel" + i;
            int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
            final Drawable drawable = res.getDrawable(resID);

            final int duration = 200 * i;
            final int thisIndex = i;
            final ImageView chilliWheelLast = (ImageView) stub.findViewById(R.id.chillislast);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(duration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(chilliItems == thisIndex){
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                                ImageView title = (ImageView) findViewById(R.id.branding);
                                title.setImageResource(R.drawable.title);
                                title.startAnimation(fadeIn);
                            } else {
                                int j;
                                if (thisIndex == 0) {
                                    j = 0;
                                } else {
                                    j = thisIndex - 1;
                                }
                                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
                                int lastResID = res.getIdentifier("wheel" + j, "drawable", getPackageName());

                                Drawable lastDrawable = res.getDrawable(lastResID);

                                chilliWheel.setImageDrawable(drawable);
                                chilliWheelLast.setImageDrawable(lastDrawable);
                                chilliWheel.startAnimation(animation);
                            }
                        }
                    });
                }
            }).start();
        }

    }

}
