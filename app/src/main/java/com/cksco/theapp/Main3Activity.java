package com.cksco.theapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main3Activity extends AppCompatActivity {
    private Button left1;
    private Button right1;
    private Button collect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        left1 = (Button) findViewById(R.id.left1);
        left1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain4Activity();
            }
        });
        right1 = findViewById(R.id.right1);
        right1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openMain6Activity();
            }
        });
        collect = (Button) findViewById(R.id.collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCamerasStuffs();
            }
        });
    }

    public void moveToCamerasStuffs() {
        Intent intent = new Intent(getApplicationContext(), MainActivity_.class);
        startActivity(intent);
    }

    public void openMain4Activity(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void openMain5Activity(){
        Intent intent = new Intent(this, Main5Activity.class);
        startActivity(intent);
    }
    public void openMain6Activity(){
        Intent intent = new Intent(this, Main6Activity.class);
        startActivity(intent);
    }
}
