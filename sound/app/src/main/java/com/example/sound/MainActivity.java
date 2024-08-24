package com.example.sound;

import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.android.AndroidAudioInputStream;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.util.PitchConverter;

public class MainActivity extends AppCompatActivity {
    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 2048;
    private static final float THRESHOLD = 50.0f;
    private static final int TIMEOUT = 10000; // 10秒超时
    private AudioRecord audioRecord;
    private Handler handler;
    private boolean isDetecting = true;
    private long lastAboveThresholdTime = 0;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        Button stopButton = findViewById(R.id.stopButton);
        Button anotherActivityButton = findViewById(R.id.anotherActivityButton);

        handler = new Handler();

        // 设置按钮监听器
        stopButton.setOnClickListener(v -> stopDetectionAndSound());
        anotherActivityButton.setOnClickListener(v -> openAnotherActivity());

        // 初始化音频处理器
        startAudioProcessing();
    }

    private void startAudioProcessing() {
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                android.media.AudioFormat.CHANNEL_IN_MONO,
                android.media.AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);

        AndroidAudioInputStream audioStream = new AndroidAudioInputStream(audioRecord);
        audioRecord.startRecording();

        be.tarsos.dsp.AudioDispatcher dispatcher = new be.tarsos.dsp.AudioDispatcher(audioStream, BUFFER_SIZE, 0);

        AudioProcessor pitchShifter = new PitchShifter(1.0f, SAMPLE_RATE, BUFFER_SIZE);
        dispatcher.addAudioProcessor(pitchShifter);

        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public void processingFinished() { }

            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] buffer = audioEvent.getFloatBuffer();
                float amplitude = calculateAmplitude(buffer);

                if (amplitude > THRESHOLD) {
                    lastAboveThresholdTime = System.currentTimeMillis();
                    playVideo();
                } else if (System.currentTimeMillis() - lastAboveThresholdTime > TIMEOUT) {
                    changePitchAndPlaySound();
                }

                return isDetecting;
            }
        });

        new Thread(dispatcher).start();
    }

    private float calculateAmplitude(float[] buffer) {
        float sum = 0;
        for (float sample : buffer) {
            sum += sample * sample;
        }
        return (float) Math.sqrt(sum / buffer.length);
    }

    private void playVideo() {
        runOnUiThread(() -> {
            if (!videoView.isPlaying()) {
                videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.sample_video);
                videoView.start();
            }
        });
    }

    private void changePitchAndPlaySound() {
        float randomPitch = PitchConverter.semitonesToFactor((float) (Math.random() * 12 - 6));  // 随机变调
        PitchShifter pitchShifter = new PitchShifter(randomPitch, SAMPLE_RATE, BUFFER_SIZE);
        pitchShifter.process(new AudioEvent(new float[BUFFER_SIZE]));

        // 播放变调声音
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_audio);
        mediaPlayer.setOnCompletionListener(mp -> startAudioProcessing());
        mediaPlayer.start();
    }

    private void stopDetectionAndSound() {
        isDetecting = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        finish();
    }

    private void openAnotherActivity() {
        Intent intent = new Intent(this, AnotherActivity.class);
        startActivity(intent);
    }
}
