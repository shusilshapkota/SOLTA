package com.cksco.theapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main4Activity extends AppCompatActivity {
    private Button right2;
    private Button middle2;


    // hty

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        right2 = findViewById(R.id.right2);
        right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain6Activity();
            }
        });
        middle2 = findViewById(R.id.middle2);
        middle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain3Activity();
            }
        });
    }

    public void openMain3Activity(){
        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
    }
    public void openMain6Activity(){
        Intent intent = new Intent(this, Main6Activity.class);
        startActivity(intent);
    }
}