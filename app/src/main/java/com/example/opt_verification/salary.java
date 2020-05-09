package com.example.opt_verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class salary extends AppCompatActivity {

    Button btn_accept;
    DatabaseReference dbservice;
    ListView lvdisplay;
    List<services_detail> servicelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);


        btn_accept = findViewById(R.id.btn_accept);
        dbservice = FirebaseDatabase.getInstance().getReference("services");
        lvdisplay = findViewById(R.id.s_lv);
        servicelist = new ArrayList<>();

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), worker_login.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        dbservice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                servicelist.clear();

                for (DataSnapshot servicesnap : dataSnapshot.getChildren()) {
                    services_detail s2 = servicesnap.getValue(services_detail.class);
                    servicelist.add(s2);
                }

                services_list adapter = new services_list(salary.this, servicelist);

                lvdisplay.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}