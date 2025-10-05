package com.example.volunteerrise;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.inappmessaging.model.Button;

public class Login2 extends AppCompatActivity {

    androidx.appcompat.widget.AppCompatButton button;
    TextView signinbut;
    EditText email, password;
    FirebaseAuth auth;
    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.login_button);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        signinbut = findViewById(R.id.signupRedirectText);

        //direct to signin page
        signinbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login2.this, Signup2.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String Email = email.getText().toString();
                String pass = password.getText().toString();

                if ((TextUtils.isEmpty(Email))){
                    Toast.makeText(Login2.this, "Enter the Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(Login2.this, "Enter the Password", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    email.setError("Give proper email address");
                } else if (password.length()<6) {
                    password.setError("more than 6 characters");
                    Toast.makeText(Login2.this, "password need to be longer than 6 characters", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                try{
                                    Intent intent = new Intent(Login2.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }catch(Exception e){
                                    Toast.makeText(Login2.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                Toast.makeText(Login2.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }



            }
        });

    }
}