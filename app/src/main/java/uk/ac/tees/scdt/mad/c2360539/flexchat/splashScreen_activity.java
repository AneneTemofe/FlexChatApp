package uk.ac.tees.scdt.mad.c2360539.flexchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashScreen_activity extends AppCompatActivity {

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity( new Intent(splashScreen_activity.this, MainActivity.class));
                finish();
            }
        } ,2000);
    }
}