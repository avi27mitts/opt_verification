package com.example.opt_verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class customer_edit_details extends AppCompatActivity {

    private DatabaseReference dbc ;
    private TextView tv_name , tv_email ;
    private EditText et_username , et_address , et_phone ;
    private Button btn_password , btn_update ;
    private String cname = "" , cusername = "" , cemail = "" , caddress = "" , cphone = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit_details);

        dbc = FirebaseDatabase.getInstance().getReference("customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        tv_name = findViewById(R.id.ced_tv_name) ;
        tv_email = findViewById(R.id.ced_tv_email) ;
        et_username = findViewById(R.id.ced_et_username) ;
        et_address = findViewById(R.id.ced_et_address) ;
        et_phone = findViewById(R.id.ced_et_phone) ;
        btn_password = findViewById(R.id.ced_btn_password) ;
        btn_update = findViewById(R.id.ced_btn_update) ;

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update_details() ;

            }
        });

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent( getApplicationContext() , customer_update_password.class ) ;
                //startActivity( intent ) ;
                Toast.makeText( getApplicationContext() , "not done yet." , Toast.LENGTH_SHORT ).show() ;

            }
        });

    }

    private void update_details(){

        String phone = et_phone.getText().toString().trim() ;
        String address = et_address.getText().toString().trim() ;
        String username = et_username.getText().toString().trim() ;

        if ( phone.length() != 10 ){
            et_phone.setError( "Please enter valid phone number." );
            et_phone.requestFocus() ;
            return;
        }
        else if ( address.isEmpty() ){
            et_address.setError( "Please enter valid address." );
            et_address.requestFocus() ;
            return;
        }
        else if ( username.isEmpty() ){
            et_username.setError( "Please enter valid username." );
            et_username.requestFocus() ;
            return;
        }
        else{

            dbc.child( "caddress" ).setValue( (String) address ) ;
            dbc.child( "cphone" ).setValue( (String) phone ) ;
            dbc.child( "cusername" ).setValue( (String) username ) ;

            Toast.makeText( getApplicationContext() , "Details updated Successfully." , Toast.LENGTH_SHORT ).show() ;

            Intent intent = new Intent( getApplicationContext() , customer_options.class ) ;
            startActivity( intent );

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cname = dataSnapshot.child("cname").getValue().toString() ;
                cusername = dataSnapshot.child("cusername").getValue().toString() ;
                cphone = dataSnapshot.child("cphone").getValue().toString() ;
                cemail = dataSnapshot.child("cemail").getValue().toString() ;

                if ( dataSnapshot.child("caddress").exists() ){
                    caddress = dataSnapshot.child("caddress").getValue().toString() ;
                }

                tv_name.setText( cname );
                tv_email.setText( cemail );
                et_phone.setText( cphone );
                et_address.setText( caddress );
                et_username.setText( cusername );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
