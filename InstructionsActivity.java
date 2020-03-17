package com.example.aaokabhiparkkarne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class InstructionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView welcomeuser;
    private Button buttonshowinstruct;
    private TextView instruct;
    private Button buttonLogout;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference; // with this reference we can store data to firebase database
    ValueEventListener listener;//all the below are urdu video to add data to spinner
    ValueEventListener listener2;
    FirebaseUser user; //used to reteive slot no.
    String uid;
    //DatabaseReference databaseReference2;
    //public static String slot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        welcomeuser = (TextView) findViewById(R.id.welcomeuser);
        buttonshowinstruct = (Button) findViewById(R.id.buttonshowinstruct);
        instruct = (TextView) findViewById(R.id.instruct);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        else {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        user= FirebaseAuth.getInstance().getCurrentUser();
        uid= user.getUid();
        System.out.print(uid);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        buttonLogout.setOnClickListener(this);
        buttonshowinstruct.setOnClickListener(this);


    }
    public void showinstructions(){
        listener=databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot item:dataSnapshot.getChildren()){
                    String slot= item.child("users").child(uid).child("bookedslot").getValue().toString();
                    String instructions= item.child("instructions").child(slot).child("instruct").toString();
                    instruct.setText(instructions);

                }
                //adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            }
        });

    }
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (view==buttonshowinstruct){
            showinstructions();
        }

    }
}
