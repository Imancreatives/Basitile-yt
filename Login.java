package com.example.incidentreporting;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, createAccountButton;
    private DatabaseReference databaseReference;
    TextView password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://incidentreporting-5608b-default-rtdb.firebaseio.com");

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.batana);
        password=findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(Login.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(username)) {
                                final String getPassword = snapshot.child(username).child("password").getValue(String.class);
                                if (getPassword.equals(password)) {
                                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this,AdminPanel.class));
                                } else {
                                    Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "User does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,ForgotPasswordActivity.class));
            }
        });
    }
}
