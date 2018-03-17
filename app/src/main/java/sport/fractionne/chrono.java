package sport.fractionne;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.text.Editable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.text.TextWatcher;
import android.content.Intent;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.SoundPool.Builder;
import android.media.AudioManager;
import android.os.CountDownTimer;
import java.lang.Object;
import java.util.Locale;
import java.io.IOException;
import android.os.Vibrator;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class chrono extends AppCompatActivity {

    String DEMANDE = "sport.fractionne.mademande";
    //private static final long START_TIME_IN_MILLIS = 600000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonFinish;
    private SoundPool ourSounds;
    private CountDownTimer mCountDownTimer;
    private int sound;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long  mTimeLeftInit;
    private long accel,repos,nbCycle;
    private String mode;
    private boolean partie_accelere=false;
    private MediaPlayer media;
    private long lg;
    private long temps_total_sec=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);

        Intent chrono = getIntent();
        String[] demande = new String[4];
        demande = chrono.getStringArrayExtra(DEMANDE);
        media = MediaPlayer.create(this, R.raw.beep);
        media.setVolume(1,1);
        long lg;
        accel = Long.valueOf(demande[0]);
        repos = Long.valueOf(demande[1]);
        nbCycle = Long.valueOf(demande[2]);
        mode = String.valueOf(demande[3]);



        lg = nbCycle*(accel+repos);
        mTimeLeftInit=lg*1000;
        mTimeLeftInMillis=lg*1000;



        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mButtonFinish = findViewById(R.id.button_finish);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer(mTimeLeftInit);
            }
        });

        mButtonFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chrono.this.finish();
                if(mTimerRunning==true){
                     pauseTimer();
                }

            }
        });
        initializeSoundPool();
        updateCountDownText();
    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer(long time) {
        mTimeLeftInMillis = time;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void initializeSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        ourSounds = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();
        sound = ourSounds.load(this, R.raw.beep, 1);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000)/60;
        int seconds = (int) (mTimeLeftInMillis / 1000)%60;
        Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            if(mTimerRunning==true) {
                if (seconds % repos == 0 && partie_accelere == false) {
                    Toast.makeText(getApplicationContext(), "Accélèèèèèère!!!!", Toast.LENGTH_SHORT).show();
                    if("bip".equalsIgnoreCase(mode)) {
                        ourSounds.play(sound, 0.9f, 0.9f, 1, 0, 1);
                    }else {
                        v.vibrate(800);
                    }
                    partie_accelere = true;


                }
                if (seconds % (repos + accel) == 0 && partie_accelere == true) {
                    Toast.makeText(getApplicationContext(), "repos", Toast.LENGTH_SHORT).show();
                    if ("bip".equalsIgnoreCase(mode)) {
                        ourSounds.play(sound, 0.9f, 0.9f, 1, 0, 1);
                    }else{
                        v.vibrate(800);
                    }
                    partie_accelere = false;
                    //v.vibrate(800);
                }
            }
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
        if(media!=null) {
            media.reset();
            media.release();
            media = null;
        }
    }

}
