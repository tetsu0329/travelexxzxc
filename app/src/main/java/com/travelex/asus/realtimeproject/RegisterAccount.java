package com.travelex.asus.realtimeproject;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterAccount extends AppCompatActivity {

    FirebaseAuth auth;

    EditText email, password, cpassword;
    Button register, clear;
    ProgressBar progressBar;
    CheckBox checkBox;
    TextView txtView;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        auth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.editText3);
        password = (EditText) findViewById(R.id.editText5);
        cpassword = (EditText) findViewById(R.id.editText7);
        register = (Button) findViewById(R.id.button4);
        clear = (Button) findViewById(R.id.button5);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        checkBox = (CheckBox) findViewById(R.id.checkBox2);
        txtView = (TextView) findViewById(R.id.viewtxt);
        progressBar.setVisibility(View.GONE);

        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(RegisterAccount.this);
                dialog.setTitle("TRAVELEX Terms and Condition");
                dialog.setContentView(R.layout.termsandcondition);
                dialog.show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = email.getText().toString();
                String password1 = password.getText().toString();
                String cpassword1 = cpassword.getText().toString();

                if(TextUtils.isEmpty(email1)){
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(cpassword1)) {
                    Toast.makeText(getApplicationContext(), "Confirm Password", Toast.LENGTH_SHORT).show();
                    cpassword.requestFocus();
                    return;
                }
                if (password1.equals(cpassword1)){
                    if(checkBox.isChecked()){
                        progressBar.setVisibility(View.VISIBLE);
                        register.setVisibility(View.GONE);
                        clear.setVisibility(View.GONE);
                        auth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(RegisterAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(RegisterAccount.this, ChooseUser.class);
                                    startActivity(intent);
                                }
                                if(!task.isSuccessful()){

                                    Toast.makeText(getApplicationContext(), "Email/Password is invalid format", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    register.setVisibility(View.VISIBLE);
                                    clear.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please agree with the terms and condition", Toast.LENGTH_SHORT).show();
                    }

                }
                if (!password1.equals(cpassword1)){
                    Toast.makeText(getApplicationContext(), "Password doesn't Match", Toast.LENGTH_SHORT).show();
                    cpassword.requestFocus();
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setText("");
                password.setText("");
                cpassword.setText("");
            }
        });
    }
}
