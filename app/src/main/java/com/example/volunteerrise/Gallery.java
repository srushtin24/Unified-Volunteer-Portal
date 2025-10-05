package com.example.volunteerrise;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Gallery extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

//        solution from video
//        VideoView videoView = findViewById(R.id.video1);
//        String vPath = "android.resource://"+getPackageName()+"/raw/video1;";
//        Uri videoURI = Uri.parse(vPath);
//        videoView.setVideoURI(videoURI);
//        videoView.start();


//        solution from gpt
//        VideoView video1 = findViewById(R.id.video1);
//        String path1 = "android.resource://" + getPackageName() + "/" + R.raw.video1;
//        video1.setVideoURI(Uri.parse(path1));
//
//        MediaController mediaController1 = new MediaController(this);
//        video1.setMediaController(mediaController1);
//        mediaController1.setAnchorView(video1);
//        video1.start();



//for multiple videos
        VideoView video1 = findViewById(R.id.video1);
        VideoView video2 = findViewById(R.id.video2);
        VideoView video3 = findViewById(R.id.video3);
        VideoView video4 = findViewById(R.id.video4);


// Setup MediaController for both videos
        MediaController mediaController1 = new MediaController(this);
        video1.setMediaController(mediaController1);
        mediaController1.setAnchorView(video1);

        MediaController mediaController2 = new MediaController(this);
        video2.setMediaController(mediaController2);
        mediaController2.setAnchorView(video2);

        MediaController mediaController3 = new MediaController(this);
        video3.setMediaController(mediaController3);
        mediaController3.setAnchorView(video3);

        MediaController mediaController4 = new MediaController(this);
        video4.setMediaController(mediaController4);
        mediaController4.setAnchorView(video4);

// Set video paths
        String path1 = "android.resource://" + getPackageName() + "/" + R.raw.video1;
        String path2 = "android.resource://" + getPackageName() + "/" + R.raw.video2;
        String path3 = "android.resource://" + getPackageName() + "/" + R.raw.video3;
        String path4 = "android.resource://" + getPackageName() + "/" + R.raw.video4;


// Set URI but do not start them
        video1.setVideoURI(Uri.parse(path1));
        video2.setVideoURI(Uri.parse(path2));
        video3.setVideoURI(Uri.parse(path3));
        video4.setVideoURI(Uri.parse(path4));


// Set click listeners
        video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // This method should match exactly
                if (!video1.isPlaying()) {
                    video1.start();
                } else {
                    video1.pause();
                }
            }
        });

        video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // This method should match exactly
                if (!video2.isPlaying()) {
                    video2.start();
                } else {
                    video2.pause();
                }
            }
        });

        video3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // This method should match exactly
                if (!video3.isPlaying()) {
                    video3.start();
                } else {
                    video3.pause();
                }
            }
        });

        video4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // This method should match exactly
                if (!video4.isPlaying()) {
                    video4.start();
                } else {
                    video4.pause();
                }
            }
        });





    }
}



