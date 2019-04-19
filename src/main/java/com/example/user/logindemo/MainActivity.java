package com.example.user.logindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    private EditText Username;
    private EditText Password;
    private Button Login;
    private TextView Info;
private int counter = 5;
private TextView userRegistration;
private FirebaseAuth firebaseAuth;
private ProgressDialog progressDialog;
private TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Username = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etUserPassword);
        Login = (Button)findViewById(R.id.btnLogin);
        Info = (TextView)findViewById(R.id.tvInfo);
userRegistration = (TextView)findViewById(R.id.tvRegister);
forgotPassword = (TextView)findViewById(R.id.tvForgotPassword);

        Info.setText("No of Attempts Remaining: 5");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user =  firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Username.getText().toString(),Password.getText().toString() );
            }
        });
        userRegistration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PasswordActivity.class));
            }
        });
    }



    private void validate(String userName, String userPassword){

        progressDialog.setMessage("You cab subscribe to my channel until you are verified!");
        progressDialog.show();
       firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){
                // progressDialog.dismiss();
                 Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                //checkEmailVerification();
             }else{
                 Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
counter--;
Info.setText("No of Attempts Remaining:" +counter);
progressDialog.dismiss();
if(counter == 0){
    Login.setEnabled(false);
}
             }
           }
       });
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag= firebaseUser.isEmailVerified();
        if(emailflag)
        {
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }else{
            Toast.makeText(MainActivity.this, "Verify your email", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
}
}