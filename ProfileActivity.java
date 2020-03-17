package com.example.aaokabhiparkkarne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfileActivity extends AppCompatActivity  implements View.OnClickListener {

    private FirebaseAuth firebaseAuth; //declaring
    private TextView textViewUserEmail;
    private Button buttonLogout;

    private DatabaseReference databaseReference;// with this reference we can store data to firebase database
    private EditText editTextPlateno; // below two are used to get data to be stored
    private Spinner mySpinner;
    private Button buttonAddVehicle;

    ValueEventListener listener; //all the below are urdu video to add data to spinner
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail); //initialsation
        buttonLogout = (Button) findViewById(R.id.buttonLogout);


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference= FirebaseDatabase.getInstance().getReference("users"); //initialising the object
        editTextPlateno=(EditText) findViewById(R.id.editTextPlateno);
        mySpinner=(Spinner) findViewById(R.id.slotlist);
        buttonAddVehicle=(Button)findViewById(R.id.buttonAddVehicle);


        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail.setText("Welcome baby " + user.getEmail());
        buttonLogout.setOnClickListener(this);
        buttonAddVehicle.setOnClickListener(this);

        spinnerDataList =new ArrayList<>();
        spinnerDataList.add("slot1");
        spinnerDataList.add("slot2");
        spinnerDataList.add("slot3");
        spinnerDataList.add("slot4");
        adapter =new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, spinnerDataList);
        mySpinner.setAdapter(adapter);
        retrieveData();

    }


    public void retrieveData(){
        listener=databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot item:dataSnapshot.getChildren()){
                    spinnerDataList.remove(item.child("bookedslot").getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            }
        });

    }
    private void saveUserInformation(){
        String plateno=editTextPlateno.getText().toString().trim();
        String slot= mySpinner.getSelectedItem().toString();
        System.out.println(slot);
        if(!TextUtils.isEmpty(plateno)){
            //String id =databaseReference.push().getKey(); //unique id
            String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
            userprofile user=new userprofile(id, plateno, slot); //the new class we created
            databaseReference.child(id).setValue(user);
            Toast.makeText(this, "Parking booked", Toast.LENGTH_LONG).show();
            finish();
            //startActivity(new Intent(this, ProfileActivity.class));
            startActivity(new Intent(this,InstructionsActivity.class));
        }
        else{
            Toast.makeText(this, "Please enter the plateno.", Toast.LENGTH_LONG).show();
        }

    }


    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (view==buttonAddVehicle){
            saveUserInformation();
        }
    }
}