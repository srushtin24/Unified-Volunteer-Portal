package com.example.volunteerrise;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup2 extends AppCompatActivity {

    TextView loginbut, adminloginbut;
    EditText rg_username, rg_email, rg_password, rg_repassword;
    androidx.appcompat.widget.AppCompatButton rg_signup;

    CircleImageView rg_profileImg;
//    ImageView rg_profileImg;


    FirebaseAuth auth;
    Uri imageURI;
    String imageUri;

    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();


        rg_username = findViewById(R.id.signup_username);
        rg_email = findViewById(R.id.signup_email);
        rg_password = findViewById(R.id.signup_password);
        rg_repassword = findViewById(R.id.signup_confirm);
        rg_profileImg = findViewById(R.id.signup_profileimg);
        rg_signup = findViewById(R.id.signup_button);


        loginbut = findViewById(R.id.loginRedirectText);
        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup2.this, Login2.class);
                startActivity(intent);
                finish();
            }
        });

        adminloginbut = findViewById(R.id.adminloginRedirectText);
        adminloginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup2.this, AdminLogin.class);
                startActivity(intent);
                finish();
            }
        });





        rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = rg_username.getText().toString();
                String email = rg_email.getText().toString();
                String Password = rg_password.getText().toString();
                String cPassword = rg_repassword.getText().toString();
                String status = "hey! i am using this app!";


                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(Password) ||TextUtils.isEmpty(cPassword)){
                    Toast.makeText(Signup2.this, "please enter valid info", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    rg_email.setError("type a valid email");
                }else if(Password.length()<6){
                    rg_password.setError("password must be 6 characters or more");
                } else if (!Password.equals(cPassword)) {
                    rg_password.setError("the password doesn't match!");
                }else{
                    auth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);


                                if(imageURI != null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageUri = uri.toString();
                                                        Users users = new Users(id, name, email, Password, cPassword, imageUri, status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Intent intent = new Intent(Signup2.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(Signup2.this, "error in creating user", Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else{
                                    String status = "hey im using this app";
                                    imageUri = "https://firebasestorage.googleapis.com/v0/b/volunteerrise-b04f5.firebasestorage.app/o/profile.jpg?alt=media&token=43ef9ab1-6f1b-4504-b933-51e3a1c42fb0";
                                    Users users = new Users(id,name,email,Password,imageUri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(Signup2.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(Signup2.this, "error in creating user", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                }
                            }else{
                                Toast.makeText(Signup2.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });






        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data != null){
                imageURI = data.getData();
                rg_profileImg.setImageURI(imageURI);
            }
        }
    }
}