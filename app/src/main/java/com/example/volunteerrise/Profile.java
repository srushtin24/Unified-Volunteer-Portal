package com.example.volunteerrise;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.CookieHandler;
import java.net.URI;

public class Profile extends AppCompatActivity {

    EditText setname, setstatus;
    de.hdodenhof.circleimageview.CircleImageView setprofile;
    androidx.appcompat.widget.AppCompatButton donebut;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String email, password;
    Uri setImageUri;

    ImageView imglogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });








        //updating profile
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        setprofile = findViewById(R.id.profile_pfp);
        setname = findViewById(R.id.profile_name);
        setstatus = findViewById(R.id.profile_status);

        donebut = findViewById(R.id.donebut);







        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("mail").getValue().toString();
                password = snapshot.child("password").getValue().toString();
                String profile = snapshot.child("profilepic").getValue().toString();
                String name = snapshot.child("userName").getValue().toString();
                String status = snapshot.child("status").getValue().toString();



                Picasso.get().load(profile).into(setprofile);
                setname.setText(name);
                setstatus.setText(status);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        setprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("imge/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select profile picture"), 10);

            }
        });



        donebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = setname.getText().toString();
                String status = setstatus.getText().toString();

                if(setImageUri != null){
                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalimageUri = uri.toString();


                                    Users users = new Users(auth.getUid(), name, email, password,finalimageUri, status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Profile.this, "Data is save", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Profile.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });



                                }
                            });
                        }
                    });
                }else{
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri = uri.toString();
                            Users users = new Users(auth.getUid(), name, email, password, finalImageUri, status);
                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Profile.this, "Data is save", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Profile.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }



            }
        });









        //click on logout icon
        imglogout = findViewById(R.id.logout_icon);
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Profile.this, R.style.dialog);
                dialog.setContentView(R.layout.dialog_layout);
                androidx.appcompat.widget.AppCompatButton no, yes;
                yes = dialog.findViewById(R.id.yesbtn);
                no = dialog.findViewById(R.id.nobtn);

                //if clicked on yes
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Profile.this, Login2.class);
                        startActivity(intent);
                    }
                });

                //if clicked on no
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });



        //bottom bar click listeners
        ImageView home_icon = findViewById(R.id.home_icon);
        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView profile_icon = findViewById(R.id.profile_icon);
        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Profile.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data != null){
                setImageUri = data.getData();
                setprofile.setImageURI(setImageUri);
            }
        }
    }
}