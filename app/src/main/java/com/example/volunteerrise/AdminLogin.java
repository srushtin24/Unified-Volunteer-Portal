package com.example.volunteerrise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {

    private EditText adminEmail, adminPassword;
    private androidx.appcompat.widget.AppCompatButton adminLoginButton;
    private TextView signupRedirectText;

    private DatabaseReference adminsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminEmail = findViewById(R.id.admin_email);
        adminPassword = findViewById(R.id.admin_password);
        adminLoginButton = findViewById(R.id.admin_login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        adminsRef = FirebaseDatabase.getInstance().getReference("admins");

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAdmin();
            }
        });

        signupRedirectText.setOnClickListener(v -> {
            // Optional: Redirect to User SignUp Activity
            Intent intent = new Intent(AdminLogin.this, Signup2.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginAdmin() {
        String email = adminEmail.getText().toString().trim();
        String password = adminPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        adminLoginButton.setEnabled(false); // disable button while checking
        adminLoginButton.setText("Logging in...");

        adminsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean loginSuccess = false;

                for (DataSnapshot adminSnapshot : snapshot.getChildren()) {
                    String dbEmail = adminSnapshot.child("email").getValue(String.class);
                    String dbPassword = adminSnapshot.child("password").getValue(String.class);

                    if (email.equals(dbEmail) && password.equals(dbPassword)) {
                        loginSuccess = true;
                        break;
                    }
                }

                if (loginSuccess) {
                    Toast.makeText(AdminLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLogin.this, AdminMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }

                adminLoginButton.setEnabled(true);
                adminLoginButton.setText("Log In");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                adminLoginButton.setEnabled(true);
                adminLoginButton.setText("Log In");
                Toast.makeText(AdminLogin.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
