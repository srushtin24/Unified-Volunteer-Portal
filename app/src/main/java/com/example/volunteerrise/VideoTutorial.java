package com.example.volunteerrise;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VideoTutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_tutorial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        VideoView video = findViewById(R.id.videotutorial);


// Setup MediaController for both videos
        MediaController mediaController1 = new MediaController(this);
        video.setMediaController(mediaController1);
        mediaController1.setAnchorView(video);



// Set video paths
        String path1 = "android.resource://" + getPackageName() + "/" + R.raw.videotutorial;



// Set URI but do not start them
        video.setVideoURI(Uri.parse(path1));


// Set click listeners
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // This method should match exactly
                if (!video.isPlaying()) {
                    video.start();
                } else {
                    video.pause();
                }
            }
        });






    }
}