package com.cksco.theapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    private Button button2;
    private EditText mMainText;
    FirebaseFirestore mFirestore;
    int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Toast.makeText(getApplicationContext(), auth.getCurrentUser().getDisplayName() + "signed in", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(getApplicationContext(), logins.class);
            startActivity(i);
        }

        button2 = (Button) findViewById(R.id.button2);
        mMainText = (EditText) findViewById(R.id.editText);
        mFirestore = FirebaseFirestore.getInstance();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = mMainText.getText().toString();

                Map<String, String> userMap = new HashMap<>();
                userMap.put("Username: ", username);
                userMap.put("School: ", "Craig Kielburger Secondary School");
                userMap.put("Score: ", "N/A");

                mFirestore.collection("Users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getApplicationContext(), "Username added to Firestore", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();
                        Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

                openMain3Activity();
            }
        });
    }



    public void openMain3Activity(){
        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
    }
}
