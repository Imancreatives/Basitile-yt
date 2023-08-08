package com.example.incidentreporting;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText editTextUsername, editTextPhoneNumber, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the DatabaseReference with the specified URL
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://incidentreporting-5608b-default-rtdb.firebaseio.com");

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^\\+255[0-9]{9}$";
        return phoneNumber.matches(phonePattern);
    }

    private boolean isStrongPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!username.contains("TRAF-PLC")) {
            Toast.makeText(this, "Username must include 'TRAF-PLC'", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isStrongPassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one special character", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a HashMap to hold the user data
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("phoneNumber", phoneNumber);
        userMap.put("email", email);
        userMap.put("password", password);

        // Save the user data to the database using the username as the key
        databaseReference.child("users").child(username).setValue(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        // Perform any additional actions after the registration is successful
                        // For example, navigate to a different activity
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
                        // Handle any errors that occurred while saving the user data
                    }
                });
    }
}
