package com.example.aaokabhiparkkarne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignIn; //declaring
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog; //used to show the time when registering
    private FirebaseAuth firebaseAuth; // declaring firebaase auth object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance(); //initialising firebaseauth object

        if(firebaseAuth.getCurrentUser() !=null){ //this loops executes if user is already logged in
            //start profile activity here
            finish(); //finish  current activity
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));//we are in the listener hence we had to getappcontext or else if we were not in listener we could have passed 'this'
        }

        buttonSignIn=(Button) findViewById(R.id.buttonSignIn); //initialising(binding)
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextpassword);
        textViewSignUp=(TextView) findViewById(R.id.textViewSignUp);
        progressDialog=new ProgressDialog(this);


        buttonSignIn.setOnClickListener(this); //attaching onclickListener ; 'this' is passed as methos is in same class
        textViewSignUp.setOnClickListener(this);
    }

    private void loginUser(){ //called when user clicks Signin
        String email=editTextEmail.getText().toString().trim(); //get email
        String password = editTextPassword.getText().toString().trim(); //get password

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"please enter email", Toast.LENGTH_SHORT).show();
            //stop the function from executing further
            return;

        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"please enter password", Toast.LENGTH_SHORT).show();
            //stop the function from executing further
            return;
        }
        //if code comes here means email and password are entered , we have to login the user
        progressDialog.setMessage("Logging User....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)//using authobject to login user
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //we used this listener to see if logging in is completed
                        //in this if the task is successful
                        //we start the profile activity here
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            //start the profile activity
                            finish(); //finish  current activity
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));//we are in the listener hence we had to getappcontext or else if we were not in listener we could have passed 'this'
                        }
                    }
                });

    }

    @Override
    public void onClick(View view){ //this is the method of onclick
        if (view ==buttonSignIn){
            loginUser();
        }
        if (view==textViewSignUp){
            //will open register activity here
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
