package com.example.incidentreporting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText usernameEditText, newPasswordEditText;
    private Button resetPasswordButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://incidentreporting-5608b-default-rtdb.firebaseio.com");

        usernameEditText = findViewById(R.id.usernameEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String newPassword = newPasswordEditText.getText().toString().trim();

                if (username.length() == 0 || newPassword.length() == 0) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your username and a new password", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(username, newPassword);
                }
            }
        });
    }

    private void resetPassword(final String username, final String newPassword) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)) {
                    DatabaseReference userRef = snapshot.child(username).getRef();
                    userRef.child("password").setValue(newPassword);
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after password reset
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ForgotPasswordActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
