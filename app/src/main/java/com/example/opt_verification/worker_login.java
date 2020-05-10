package com.example.opt_verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class worker_login extends AppCompatActivity {

    public static final String NUMBER = "wnum" ;
    public static final String numb = "num" ;

    Button btnverify , btnotp ;

    EditText etnumber , etotp;

    String sent_code ;

    FirebaseAuth fbauth ;// = FirebaseAuth.getInstance() ;

    DatabaseReference dbworker ;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);

        btnotp = findViewById(R.id.btnget_otp) ;
        btnverify = findViewById(R.id.btnverify) ;

        etnumber = findViewById(R.id.etnumber) ;
        etotp = findViewById(R.id.etotp) ;

        fbauth = FirebaseAuth.getInstance() ;

        String sent_code ;

        dbworker = FirebaseDatabase.getInstance().getReference("workers") ;

        fbauth = FirebaseAuth.getInstance() ;

        FirebaseUser user = fbauth.getCurrentUser() ;

        progressDialog = new ProgressDialog( worker_login.this ) ;

        if ( user != null){
            finish() ;
            Intent intent = new Intent( getApplicationContext() , worker_options.class) ;
            startActivity( intent ) ;
        }

        btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendotp();
            }
            
        });

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifycode();
            }
        });

    }

    private void sendotp() {
        String num = "+91" + etnumber.getText().toString() ;

        if ( num.isEmpty() ){
            etnumber.setError( " Entering phone number is mandatory. " );
            etnumber.requestFocus() ;
            return ;
        }

        if ( num.length() != 13 ){
            etnumber.setError( " Please enter a valid phone number. " );
            etnumber.requestFocus() ;
            return ;
        }
        progressDialog.setMessage( "Sending otp." );
        progressDialog.show() ;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressDialog.dismiss() ;
            Toast.makeText( getApplicationContext() , "Code sent successfully. " , Toast.LENGTH_LONG).show() ;

            sent_code = s ;

        }

    } ;

    private void verifycode() {
        String code = etotp.getText().toString() ;

        if (code.isEmpty()){
            etotp.setError( " Please enter a valid otp. " ) ;
            etotp.requestFocus() ;
            return ;
        }

        try {
            progressDialog.setMessage( "Verifying....Please wait." );
            progressDialog.show() ;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sent_code, code) ;

            signInWithPhoneAuthCredential(credential) ;

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext() , "Wrong code." , Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // if verifiaction is successful

                            progressDialog.dismiss() ;
                            Toast.makeText( getApplicationContext() , "Verification approved." , Toast.LENGTH_LONG).show() ;

                            dbworker.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(etnumber.getText().toString()).exists()){

                                        worker_details s = dataSnapshot.child(etnumber.getText().toString()).getValue(worker_details.class) ;

                                        Toast.makeText( getApplicationContext() , "Welcome back." , Toast.LENGTH_SHORT).show() ;

                                        finish() ;
                                        Intent intent = new Intent(getApplicationContext() , worker_options.class) ;
                                        intent.putExtra(NUMBER , etnumber.getText().toString()) ;
                                        startActivity(intent) ;

                                    }
                                    else {

                                        Toast.makeText( getApplicationContext() , " New user." , Toast.LENGTH_SHORT).show() ;

                                        Intent intent = new Intent(getApplicationContext() , worker_registration.class) ;
                                        intent.putExtra(NUMBER , etnumber.getText().toString()) ;
                                        etnumber.setText("");
                                        etotp.setText("");
                                        startActivity(intent) ;

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else { // if verification fails
                            progressDialog.dismiss() ;
                            Toast.makeText( getApplicationContext() , "Verification failed." , Toast.LENGTH_LONG).show() ;
                            Intent intent = new Intent(getApplicationContext() , worker_options.class) ;
                            intent.putExtra(NUMBER , etnumber.getText().toString()) ;
                            startActivity(intent) ;
                        }
                    }
                });
    }

}
