package com.example.opt_verification;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class customer_pending_appointment extends AppCompatActivity {

    private Spinner sp ;
    private ListView lv ;
    private List<pending_appointment> pal ;
    private DatabaseReference dbpa ;
    private Date c_date = new Date() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_pending_appointment);

        sp = findViewById(R.id.cpa_spinner) ;
        lv = findViewById(R.id.cpa_lv) ;

        pal = new ArrayList<>() ;

        dbpa = FirebaseDatabase.getInstance().getReference("pappointment") ;

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                final String choice = adapterView.getItemAtPosition(i).toString() ;

                dbpa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        pal.clear() ;

                        for ( DataSnapshot ds : dataSnapshot.getChildren() ){

                            pending_appointment pa = ds.getValue(pending_appointment.class) ;
                            String n = pa.getCid() ;

                            if( FirebaseAuth.getInstance().getCurrentUser().getUid().equals(n) )
                            {
                                if (choice.equals("Upcoming"))
                                {
                                    if( pa.getD().after(c_date) )
                                    {
                                        pal.add(pa) ;
                                    }
                                }
                                else if (choice.equals("Expired"))
                                {
                                    if( pa.getD().before(c_date) )
                                    {
                                        pal.add(pa) ;
                                    }
                                }
                            }
                        }

                        customer_pending_for_customer adapter = new customer_pending_for_customer( customer_pending_appointment.this , pal ) ;

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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final pending_appointment p = pal.get(i) ;

                final AlertDialog.Builder bld = new AlertDialog.Builder( customer_pending_appointment.this ) ;

                bld.setMessage("Are you sure you want to cancel " + p.getWork() + " at " + p.getD() + " ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes cancel!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbpa.child(p.getId()).removeValue() ;
                            }
                        })
                        .setNegativeButton("NO!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return ;
                            }
                        });

                AlertDialog a = bld.create() ;

                if (p.getD().after(c_date)){
                    a.show() ;
                }
            }
        });
    }
}
