package com.example.tmfappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    private EditText userEmailET;
    private EditText userPasswordET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // beirt felhasznalonev es jelszo lekerese
        userEmailET = findViewById(R.id.editText_email);
        userPasswordET = findViewById(R.id.editText_password);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        // fade in animacio hozzaadasa
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        findViewById(R.id.textView_welcome).startAnimation(animation);
        findViewById(R.id.editText_email).startAnimation(animation);
        findViewById(R.id.editText_password).startAnimation(animation);
        findViewById(R.id.button_login).startAnimation(animation);
        findViewById(R.id.button_signUp).startAnimation(animation);
    }

    // bejelentkezes, kezdooldal megnyitasa
    public void login(View view) {
        String userEmailStr = userEmailET.getText().toString();
        String userPasswordStr = userPasswordET.getText().toString();

        if (userEmailStr.equals("") || userPasswordStr.equals("")) {
            Log.e(LOG_TAG, "Email address and password are required!");
            Toast.makeText(MainActivity.this, "Email address and password are required!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(userEmailStr, userPasswordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "User logged in succesfully!");
                    Toast.makeText(MainActivity.this, "User logged in succesfully!", Toast.LENGTH_LONG).show();
                    openHomePage();
                } else {
                    Log.d(LOG_TAG, "User login failed!");
                    Toast.makeText(MainActivity.this, "User login failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // regisztracios oldal megnyitasa
    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    // kezdooldal megnyitasa
    public void openHomePage(/* felhasznalo adatai */) {
        Intent intent = new Intent(this, AppointmentListActivity.class);
        startActivity(intent);
    }

    // felhasznalonev es jelszo kimentese az activity elhagyasakor
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userEmail", userEmailET.getText().toString());
        editor.putString("userPassword", userPasswordET.getText().toString());
        editor.apply();
    }
}