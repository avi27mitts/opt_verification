package com.example.opt_verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class worker_confirm extends AppCompatActivity {

    ListView lv ;
    List<confirm_appointment> li ;
    DatabaseReference dbca ;
    String na ;
    Spinner s ;
    Date curent_date = new Date( ) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_confirm);

        lv = findViewById(R.id.wc_lv) ;
        s = findViewById(R.id.wc_spinner) ;
        li = new ArrayList<>() ;

        na = getIntent().getStringExtra( worker_options.na ) ;
        dbca = FirebaseDatabase.getInstance().getReference("confirmed") ;

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                final String choice = adapterView.getItemAtPosition(i).toString() ;

                dbca.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        li.clear() ;

                        for ( DataSnapshot ds : dataSnapshot.getChildren() ){

                            confirm_appointment ca = ds.getValue( confirm_appointment.class ) ;
                            if( ca.getWid().equals(na) ){
                                if( choice.equals("Upcoming") ){
                                    if( ca.getD().after(curent_date) ){
                                        li.add( ca ) ;
                                    }
                                }

                                else if ( choice.equals("Completed") ){
                                    if(ca.getD().before(curent_date)){
                                        li.add( ca ) ;
                                    }
                                }
                            }
                        }

                        w_confirm adapter = new w_confirm( worker_confirm.this , li ) ;

                        lv.setAdapter(adapter) ;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
