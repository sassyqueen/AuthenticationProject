package sg.edu.rp.webservices.authenticationproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Button btnLogin, btnRegister;
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userListRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // btnLogin = (Button)findViewById(R.id.login);
       // btnRegister = (Button)findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };






    }

    //register


    public void register(View v) {

        //Toast.makeText(getApplicationContext(), "register", Toast.LENGTH_SHORT).show();

        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        if (etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "No empty fields, please.", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: implement Firebase Authentication
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        userListRef = firebaseDatabase.getReference("/profiles/" + user.getUid());

                        userListRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String userDisplay = dataSnapshot.getValue(String.class);


                                if (userDisplay.equalsIgnoreCase("")){

                                    Toast.makeText(getBaseContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getBaseContext(), SetDisplayName.class);
                                    startActivity(i);
                                }
                                else if (!userDisplay.equalsIgnoreCase("")){

                                    Toast.makeText(getBaseContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getBaseContext(), ChatActivity.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Toast.makeText(MainActivity.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                    Toast.makeText(MainActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                            } else {
                                String reason = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, "Registration failed: " + reason, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }


    //login


    public void login(View v) {

        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        if (etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "No empty fields, please.", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: implement Firebase Authentication
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    } else {

                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.w(TAG, "isEmailVerified ? " + user.isEmailVerified());
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        userListRef = firebaseDatabase.getReference("/profiles/" + user.getUid());

                        userListRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String userDisplay = dataSnapshot.getValue(String.class);


                                if (userDisplay.equalsIgnoreCase("")){

                                    Toast.makeText(getBaseContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getBaseContext(), SetDisplayName.class);
                                    startActivity(i);
                                }
                                else if (!userDisplay.equalsIgnoreCase("")){

                                    Toast.makeText(getBaseContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getBaseContext(), ChatActivity.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        //not required for user to verify email
                        /*
                        if (user.isEmailVerified()) {
                           // Intent i = new Intent(getBaseContext(), ProfileActivity.class);
                           // startActivity(i);
                        } else {
                            Toast.makeText(MainActivity.this, "Please proceed to verify your email first.", Toast.LENGTH_SHORT).show();
                        }

                        */



                    }
                }
            });


        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mAuth.getCurrentUser()!= null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            userListRef = firebaseDatabase.getReference("/profiles/" + mAuth.getCurrentUser().getUid());

            userListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userDisplay = dataSnapshot.getValue(String.class);


                    if (userDisplay.equalsIgnoreCase("")){

                        Intent i = new Intent(getBaseContext(), SetDisplayName.class);
                        startActivity(i);
                    }
                    else if (!userDisplay.equalsIgnoreCase("")){

                        Intent i = new Intent(getBaseContext(), ChatActivity.class);
                        startActivity(i);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }



}



