package com.example.user.myapplication.videoview;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.user.myapplication.R;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView videoView;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        initView();
    }


    private void initView() {
        videoView = (VideoView) findViewById(R.id.videoView);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    private void play() {
        String filePath = Environment.getExternalStorageDirectory()+"/video_test.mp4";
        videoView.setVideoPath(filePath);
        videoView.setMediaController(new MediaController(VideoViewActivity.this));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });

        MediaPlayer mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }
}
