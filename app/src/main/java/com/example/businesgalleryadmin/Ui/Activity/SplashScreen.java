package com.example.businesgalleryadmin.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.R;

import static maes.tech.intentanim.CustomIntent.customType;

public class SplashScreen extends AppCompatActivity {

    ImageView imagesplash;
    Runnable runnable = ()-> {
        LocalSession localSession = new LocalSession(getApplicationContext());
        Boolean IsSessionCreated = localSession.getIsSessionCreated();;

        if((IsSessionCreated))
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            customType(SplashScreen.this,"fadein-to-fadeout");
            finish();
        }

        else {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            customType(SplashScreen.this,"fadein-to-fadeout");
            finish();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imagesplash = findViewById(R.id.logo);
        imagesplash.postDelayed(runnable,4000);
    }
}