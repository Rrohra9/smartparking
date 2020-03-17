package com.example.aaokabhiparkkarne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//we added onclicklistener to implement on click functionality
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister; //declaring
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog; //used to show the time when registering
    private FirebaseAuth firebaseAuth; //firebase authentication object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        buttonRegister=(Button) findViewById(R.id.buttonRegister); //initialising(binding)
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextpassword);
        textViewSignin=(TextView) findViewById(R.id.textViewSignin);
        progressDialog=new ProgressDialog(this);

        buttonRegister.setOnClickListener(this); //attaching onclickListener ; 'this' is passed as methos is in same class
        textViewSignin.setOnClickListener(this);

    }


    private void registerUser(){ //is called when user clicks on register button
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
        //if code comes here means email and password are entered , e=we have to register the user
        progressDialog.setMessage("Registering User....");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password) //using authobject to register user
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if (task.isSuccessful()){
                            //we used this listener to see if registration is completed
                            //in this if the task is successful
                            //we start the profile activity here
                            Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Failed to Register.Try again", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
        });
    }


    @Override
    public void onClick(View view){ //this is the method of onclick
        if (view ==buttonRegister){
            registerUser();
        }
        if (view==textViewSignin){
            //will open login activity here
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
