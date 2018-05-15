package com.cksco.theapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main6Activity extends AppCompatActivity {
    private Button left3;
    private Button middle3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        left3 = findViewById(R.id.left3);
        left3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain4Activity();
            }
        });
        middle3 = findViewById(R.id.middle3);
        middle3.setOnClickListener(new View.OnClickListener() {
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
    public void openMain4Activity(){
        Intent intent = new Intent(this, Main4Activity.class);
        startActivity(intent);
    }
}