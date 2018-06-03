package com.cksco.theapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


public class Main2Activity_ extends AppCompatActivity {
    private com.cksco.theapp.MyGLCamSurf mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main2_);
        mRenderer = findViewById(R.id.renderer_view);


    }

    public void onStart() {
        super.onStart();

    }


    @Override
    public void onPause() {
        super.onPause();
        mRenderer.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        mRenderer.onResume();

    }

}
