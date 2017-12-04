package com.travelex.asus.realtimeproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Loginscreen extends AppCompatActivity {

    Button register, login;
    EditText email, password;
    ImageView imageView, imageView2, imageView3;
    FirebaseAuth auth;
    private DatabaseReference mDatabaseRef;
    ProgressBar progressBar;
    public static final String FB_DATABASE_PATH = "userinfo";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        email = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        imageView = (ImageView) findViewById(R.id.imageView13);
        imageView2 = (ImageView) findViewById(R.id.imageView14);
        imageView3 = (ImageView) findViewById(R.id.imageView15);

        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);


        register = (Button) findViewById(R.id.button3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loginscreen.this, RegisterAccount.class);
                startActivity(intent);
            }
        });
        if(null != auth.getCurrentUser()){
            Intent intent = new Intent(Loginscreen.this, TouristDashboard.class);
            startActivity(intent);
        }
        if(null == auth.getCurrentUser()){

            login = (Button) findViewById(R.id.button2);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email.setEnabled(false);
                    password.setEnabled(false);
                    String email1 = email.getText().toString();
                    String password1 = password.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    register.setVisibility(View.GONE);
                    imageView2.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);

                    auth.signInWithEmailAndPassword(email1, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Incorrect Email or Password", Toast.LENGTH_LONG).show();
                                login.setVisibility(View.VISIBLE);
                                register.setVisibility(View.VISIBLE);
                                imageView2.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                                imageView3.setVisibility(View.VISIBLE);
                                email.setEnabled(true);
                                password.setEnabled(true);
                            }
                            else{
                                final String id = auth.getCurrentUser().getUid();
                                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                                Query search = mDatabaseRef.child("userinfo").orderByChild("userID").startAt(id).endAt(id+"\uf8ff");
                                search.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){

                                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                                                String stat = userAccount.getUserStatus();

                                                if(stat.equals("pending")){
                                                    auth.signOut();
                                                    login.setVisibility(View.VISIBLE);
                                                    register.setVisibility(View.VISIBLE);
                                                    imageView2.setVisibility(View.VISIBLE);
                                                    imageView.setVisibility(View.VISIBLE);
                                                    imageView3.setVisibility(View.VISIBLE);
                                                    email.setEnabled(true);
                                                    password.setEnabled(true);
                                                    Toast.makeText(getApplicationContext(), "Not yet validated please try again later", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Intent intent = new Intent(Loginscreen.this, TouristDashboard.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                        else{
                                            Intent intent = new Intent(Loginscreen.this, ChooseUser.class);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    });
                }
            });
        }

    }
    public void onBackPressed() {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(a);
            onQuitPressed();

    }
    public void onQuitPressed() {

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }
}
