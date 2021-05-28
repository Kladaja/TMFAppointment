package com.example.tmfappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AppointmentListActivity extends AppCompatActivity {
    private static final String LOG_TAG = AppointmentListActivity.class.getName();
    private static final String PREF_KEY = AppointmentListActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    private RecyclerView mRecyclerView;
    private ArrayList<Appointment> mAppointmentList;
    private AppointmentAdapter mAppointmentAdapter;

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;
    private CollectionReference mAppointments;

    private SharedPreferences preferences;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView_appointments);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mAppointmentList = new ArrayList<>();

        mAppointmentAdapter = new AppointmentAdapter(this, mAppointmentList);
        mRecyclerView.setAdapter(mAppointmentAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mAppointments = mFirestore.collection("TMFAppointment");

        queryData();
    }

    private void queryData() {
        mAppointmentList.clear();

        mAppointments.orderBy("externalId").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Appointment appointment = document.toObject(Appointment.class);
                mAppointmentList.add(appointment);
            }

            if (mAppointmentList.size() == 0) {
                initializeData();
                queryData();
            }

            mAppointmentAdapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        TypedArray bannerList = getResources().obtainTypedArray(R.array.example_banner);
        String[] titleList = getResources().getStringArray(R.array.example_title);
        String[] descriptionList = getResources().getStringArray(R.array.example_description);
        String[] noteList = getResources().getStringArray(R.array.example_note);
        String[] placeList = getResources().getStringArray(R.array.example_place);
        String[] validForList = getResources().getStringArray(R.array.example_validFor);
        String[] contactPersonList = getResources().getStringArray(R.array.example_contactPerson);
        String[] contactEntityList = getResources().getStringArray(R.array.example_contactEntity);
        String[] contactMediumList = getResources().getStringArray(R.array.example_contactMedium);
        String[] categoryList = getResources().getStringArray(R.array.example_category);

        for (int i = 0; i < titleList.length; i++) {
            mAppointments.add(new Appointment(
                    bannerList.getResourceId(i, 0),
                    categoryList[i],
                    contactMediumList[i],
                    descriptionList[i],
                    titleList[i],
                    "",
                    "",
                    noteList[i],
                    contactEntityList[i],
                    contactPersonList[i],
                    placeList[i],
                    validForList[i]));
        }

        bannerList.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.appointment_list_menu, menu);

        // menu keresesi elemenek kezelese
        MenuItem menuItem = menu.findItem(R.id.search_appointment);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAppointmentAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // kivalasztott menuelemek kezelese
        switch (menuItem.getItemId()) {
            case R.id.add_appointment:
                Log.d(LOG_TAG,"Go to add appointment page!");
                openHomePage();
                return true;
            case R.id.refresh_appointments:
                Log.d(LOG_TAG,"Refresh appointments list!");
                queryData();
                return true;
//            case R.id.deleted_appointments:
//                Log.d(LOG_TAG,"Go to deleted appointments!");
//                return true;
            case R.id.log_out:
                Log.d(LOG_TAG,"User logged out!");
                Toast.makeText(AppointmentListActivity.this, "User logged out!", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    // szerkesztesi oldal megnyitasa
    public void openHomePage(/* felhasznalo adatai */) {
        Intent intent = new Intent(this, EditAppointmentActivity.class);
        startActivity(intent);
    }

    // torles
    public void deleteAppointment(String title) {
        Query appointmentQuery = mAppointments.whereEqualTo("externalId", title);
        appointmentQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    mAppointments.document(documentSnapshot.getId()).delete();
                }
                Log.d(LOG_TAG, "Appointment deleted succesfully!");
                Toast.makeText(AppointmentListActivity.this, "Appointment deleted succesfully!", Toast.LENGTH_LONG).show();
                mAppointmentAdapter.notifyDataSetChanged();
            }
        });
    }
}