package com.example.tmfappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class EditAppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = EditAppointmentActivity.class.getName();
    private static final String PREF_KEY = EditAppointmentActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    private EditText appointmentExternalIDET;
    private EditText appointmentDescriptionET;
    private EditText appointmentNoteET;
    private CalendarView appointmentDateCV;
    private String appointmentDate;
    private EditText appointmentPlaceET;
    private EditText appointmentContactPersonET;
    private EditText appointmentContactEntityET;
    private EditText appointmentContactMediumET;
    private Spinner appoinmentCategoryS;

    private SharedPreferences preferences;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        // beirt adatok lekerese
        appointmentExternalIDET = findViewById(R.id.editText_title);
        appointmentDescriptionET = findViewById(R.id.editText_description);
        appointmentNoteET = findViewById(R.id.editText_note);

        appointmentDate = "2030-01-01 00:00:00";
        appointmentDateCV = findViewById(R.id.calendarView_date);
        appointmentDateCV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                appointmentDate = (year + "-" + month + "-" + dayOfMonth + "00:00:00");
            }
        });

        appointmentPlaceET = findViewById(R.id.editText_place);
        appointmentContactPersonET = findViewById(R.id.editText_contactPerson);
        appointmentContactEntityET = findViewById(R.id.editText_contactEntity);
        appointmentContactMediumET = findViewById(R.id.editText_contactMedium);
        appoinmentCategoryS = findViewById(R.id.spinner_category);

        appoinmentCategoryS.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.appointmen_categories, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appoinmentCategoryS.setAdapter(arrayAdapter);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        // firebaseauth
        mAuth = FirebaseAuth.getInstance();

        // firestore
        mFirestore = FirebaseFirestore.getInstance();
        mAppointments = mFirestore.collection("TMFAppointment");
    }

    public void finish(View view) {
        Resources resources = this.getResources();
        final int resourceID = resources.getIdentifier("basic_bg", "drawable", this.getPackageName());

        String appointmentExternalIDStr = appointmentExternalIDET.getText().toString();
        String appointmentDescriptionStr =  appointmentDescriptionET.getText().toString();
        String appointmentNoteStr =  appointmentNoteET.getText().toString();
        String appointmentDateStr =  "2030-01-01 00:00:00";
        String appointmentPlaceStr =  appointmentPlaceET.getText().toString();
        String appointmentContactPersonStr =  appointmentContactPersonET.getText().toString();
        String appointmentContactEntityStr =  appointmentContactEntityET.getText().toString();
        String appointmentContactMediumStr =  appointmentContactMediumET.getText().toString();
        String appoinmentCategoryStr =  appoinmentCategoryS.getSelectedItem().toString();

        mAppointments.add(new Appointment(
                resourceID,
                appoinmentCategoryStr,
                appointmentContactMediumStr,
                appointmentDescriptionStr,
                appointmentExternalIDStr,
                "",
                "",
                appointmentNoteStr,
                appointmentContactEntityStr,
                appointmentContactPersonStr,
                appointmentPlaceStr,
                appointmentDateStr));

        finish();
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}