package com.example.opt_verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class add_service extends AppCompatActivity {

    EditText et_name , et_sal ;

    Button btn_add ;

    ListView lvdisplay ;

    List<services_detail> servicelist ;

    DatabaseReference dbservice ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        et_name = findViewById(R.id.et_new_service) ;
        et_sal = findViewById(R.id.et_salary) ;

        btn_add = findViewById(R.id.btn_add_serv) ;

        lvdisplay = findViewById(R.id.lv_services_display) ;

        servicelist = new ArrayList<>() ;

        dbservice = FirebaseDatabase.getInstance().getReference("services") ;

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_services();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        dbservice.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                servicelist.clear() ;

                for (DataSnapshot servicesnap : dataSnapshot.getChildren()){
                    services_detail s2 = servicesnap.getValue(services_detail.class) ;
                    servicelist.add(s2) ;
                }

                services_list adapter = new services_list( add_service.this , servicelist ) ;

                lvdisplay.setAdapter(adapter) ;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void add_services(){

        String name = et_name.getText().toString().trim() ;
        String sal = et_sal.getText().toString().trim() ;

        if( name.isEmpty() || sal.isEmpty() ){

            Toast.makeText( getApplicationContext() , "Please enter details properly . " , Toast.LENGTH_SHORT).show() ;

        }
        else {

            String id = dbservice.push().getKey() ;

            services_detail s = new services_detail(name,sal) ;

            dbservice.child(id).setValue(s) ;

            Toast.makeText( getApplicationContext() , "Service added . " , Toast.LENGTH_SHORT).show() ;

        }

    }

}
