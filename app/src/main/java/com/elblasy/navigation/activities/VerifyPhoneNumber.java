package com.elblasy.navigation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.elblasy.navigation.LocaleUtils;
import com.elblasy.navigation.R;
import com.elblasy.navigation.SharedPref;
import com.elblasy.navigation.models.User;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumber extends AppCompatActivity {

    private String mVerificationId;
    private Pinview codeInputView;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private String mobile, name, city;
    SharedPref sharedPref;
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                Log.e("Code", code);
                codeInputView.setValue(code);
                //verifying the code
                verifyVerificationCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    public VerifyPhoneNumber() {
        LocaleUtils.updateConfig(this);
    }

    //the method is sending verification code
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+2" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        Log.e("Mobile", mobile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference("users");
        codeInputView = findViewById(R.id.code);

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        name = intent.getStringExtra("user");
        city = intent.getStringExtra("city");

        sharedPref = new SharedPref(this);


        final TextView mobileText = findViewById(R.id.mobile);
        mobileText.setText(mobile);

        sendVerificationCode(mobile);

        Button sign = findViewById(R.id.sign);

        sign.setOnClickListener(v -> {
            String code = codeInputView.getValue();

            if (code.isEmpty() || code.length() < 6) {
                Toast.makeText(VerifyPhoneNumber.this, "Enter valid code", Toast.LENGTH_LONG).show();
                return;
            }
            //verifying the code entered manually
            verifyVerificationCode(code);

        });
    }

    private void verifyVerificationCode(String code) {
        try {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

            //signing the user
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNumber.this, task -> {

                    if (task.isSuccessful()) {


                        User users = new User(name, mobile, city);
                        mdatabase.child("Clients").child(city).child(mobile).setValue(users);

                        //verification successful we will start the profile activity
                        Intent intent = new Intent(VerifyPhoneNumber.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        sharedPref.setPreferName(name);
                        sharedPref.setPrefPhoneNumber("+2" + mobile);
                        sharedPref.setPrefCity(city);
                        startActivity(intent);
                        finish();

                    } else {

                        //verification unsuccessful.. display an error message
                        String message = "Somthing is wrong, we will fix it soon...";

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered...";
                        }

                        Toast.makeText(VerifyPhoneNumber.this, message, Toast.LENGTH_LONG).show();

                    }
                });
    }

}
