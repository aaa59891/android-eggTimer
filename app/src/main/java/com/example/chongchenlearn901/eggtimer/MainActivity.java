package com.example.chongchenlearn901.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static boolean started = false;

    private TextView textViewTimer;
    private Button btn;
    private SeekBar seekBarTimer;
    private MediaPlayer mediaPlayer;
    private MyCountdownTimer myCountdownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = findViewById(R.id.tvTimer);
        seekBarTimer = findViewById(R.id.seekBarTimer);
        btn = findViewById(R.id.btn);

        seekBarTimer.setMax(300);
        seekBarTimer.setOnSeekBarChangeListener(new SeekBarCountdownListener());
        seekBarTimer.setProgress(60);

        btn.setOnClickListener(btnListener);
    }

    private View.OnClickListener btnListener = (v) -> {
        if (started) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            if (myCountdownTimer != null) {
                myCountdownTimer.cancel();
            }
        } else {
            myCountdownTimer = new MyCountdownTimer(seekBarTimer.getProgress() * 1000 + 100, 1000);
            myCountdownTimer.start();
        }

        ((Button) v).setText(started ? "Start" : "Stop");
        seekBarTimer.setEnabled(started);
        started = !started;
    };

    private String getCountdownString(long millisecond) {
        long second = millisecond / 1000;
        long minute = second / 60;
        second %= 60;
        return String.format("%02d:%02d", minute, second);
    }


    private class MyCountdownTimer extends CountDownTimer {
        private static final String TAG = "MyCountdownTimer";

        MyCountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            seekBarTimer.setProgress((int) (millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.airhorn);
            }
            mediaPlayer.start();

            seekBarTimer.setProgress(0);
            seekBarTimer.setEnabled(true);
            btn.setText("Start");
            started = false;
        }
    }

    private class SeekBarCountdownListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textViewTimer.setText(getCountdownString(progress * 1000));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
