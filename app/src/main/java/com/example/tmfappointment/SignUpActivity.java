package com.example.tmfappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignUpActivity.class.getName();
    private static final String PREF_KEY = SignUpActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    private EditText userNameET;
    private EditText userEmailET;
    private EditText userPasswordET;
    private EditText userPasswordAgainET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key != SECRET_KEY) { finish(); }

        // beirt adatok lekerese
        userNameET = findViewById(R.id.editText_name);
        userEmailET = findViewById(R.id.editText_email);
        userPasswordET = findViewById(R.id.editText_password);
        userPasswordAgainET = findViewById(R.id.editText_passwordAgain);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        // a bejelentkezesi oldalon kimentett nev es jelszo felhasznalasa
        String userEmail = preferences.getString("userEmail", "");
        String userPassword = preferences.getString("userPassword", "");

        userEmailET.setText(userEmail);
        userPasswordET.setText(userPassword);
        userPasswordAgainET.setText(userPassword);

        // firebaseauth
        mAuth = FirebaseAuth.getInstance();
    }

    // regisztracio, kezdooldal megnyitasa
    public void signUp(View view) {
        String userNameStr = userNameET.getText().toString();
        String userEmailStr = userEmailET.getText().toString();
        String userPasswordStr = userPasswordET.getText().toString();
        String userPasswordAgainStr = userPasswordAgainET.getText().toString();

        // beirt adatok ellenorzese
        if (userNameStr.equals("") || userEmailStr.equals("") || userPasswordStr.equals("")) {
            Log.e(LOG_TAG, "Username, email address and password are required!");
            Toast.makeText(SignUpActivity.this, "Username, email address and password are required!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!userPasswordStr.equals(userPasswordAgainStr)) {
            Log.e(LOG_TAG, "Password is not confirmed!");
            Toast.makeText(SignUpActivity.this, "Password is not confirmed!", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(LOG_TAG, "Username: " + userNameStr + ", email: " + userEmailStr + ", password: " + userPasswordStr);

        // felhasznalo letrehozasa
        mAuth.createUserWithEmailAndPassword(userEmailStr, userPasswordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created succesfully!");
                    Toast.makeText(SignUpActivity.this, "User created succesfully!", Toast.LENGTH_LONG).show();
                    openHomePage();
                } else {
                    Log.d(LOG_TAG, "User was not created succesfully!");
                    Toast.makeText(SignUpActivity.this, "User was not created succesfully!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // visszateres a bejelentkezesi oldalra
    public void login(View view) {
        finish();
    }

    // kezdooldal megnyitasa
    public void openHomePage() {
        Intent intent = new Intent(this, AppointmentListActivity.class);
        startActivity(intent);
    }
}