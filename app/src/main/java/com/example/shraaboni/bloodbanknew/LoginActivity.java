package com.example.shraaboni.bloodbanknew;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private EditText emailET;
    private EditText passwordET;

    FirebaseAuth.AuthStateListener authStateListener;
    ArrayList<String> infoList;
    DatabaseReference databaseRef;
    String userId;

    String name;
    String bloodgrp;
    String gender;
    String email;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        email = emailET.getText().toString();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("user");
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override


            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this, "auth", Toast.LENGTH_SHORT).show();
                if (firebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "already logged in", Toast.LENGTH_SHORT).show();
                }
            }
        };
/*
        DatabaseReference testDT=FirebaseDatabase.getInstance().getReference("user");
        Query query=testDT.orderByChild("email").equalTo(emailET.getText().toString());
        Log.d("chck>>","emIL: "+emailET.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(LoginActivity.this, "called", Toast.LENGTH_SHORT).show();
                Log.d("chck>>","emIL: "+dataSnapshot);
                if (dataSnapshot.exists()){
                    for (DataSnapshot single: dataSnapshot.getChildren()){
                        Person person=single.getValue(Person.class);
                        Log.d("chck>>","emIL: "+person.getEmail());
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }
    public void login(View view) {
        final String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              //  Toast.makeText(LoginActivity.this, "" + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "" + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseReference testDT=FirebaseDatabase.getInstance().getReference("user");
        Query query=testDT.orderByChild("email").equalTo(emailET.getText().toString());
        Log.d("chck>>","emIL: "+emailET.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(LoginActivity.this, "called", Toast.LENGTH_SHORT).show();
                //Log.d("chck>>","emIL: "+dataSnapshot);
                if (dataSnapshot.exists()){
                    for (DataSnapshot single: dataSnapshot.getChildren()){
                        Person person=single.getValue(Person.class);
                        name=person.getName();
                        String id=dataSnapshot.getKey();
                        Log.d("id>>","key: "+id);
                        bloodgrp=person.getBloodtype();
                        gender=person.getGender();
                        key=person.getUserKey();
                        Log.d("tst>>","name: "+name+" blood: "+bloodgrp+" gender: "+gender);
                       /* startActivity(new Intent(LoginActivity.this, AccountActivity.class)
                                .putExtra("name",name)
                                .putExtra("bloodgrp",bloodgrp)
                                .putExtra("gender",gender)
                                .putExtra("key",userId));*/

                    }
                    Intent intent=new Intent(LoginActivity.this,AccountActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("bloodgrp",bloodgrp);
                    intent.putExtra("gender",gender);
                    intent.putExtra("email",email);
                    intent.putExtra("key",key);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void moveToSignUp(View view) {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

    }

}
