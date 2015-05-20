package com.example.requiem.bdukssstudy;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Requiem on 2/4/2015.
 */
public class QuestionScreen extends Activity{

    private final static String STORETEXT = "storetext.txt";
    private final static String STOREID = "storeid.txt";
    private Button  sendButton;
    private MediaPlayer stateQuestion;
    private TextView idt, idn;
    private EditText userResponse;
    private CountDownTimer timer;
    private final  int REQ_CODE_SPEECH_INPUT = 100;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_screen);

        Button endButton = (Button) this.findViewById(R.id.end_shift);
        sendButton = (Button) this.findViewById(R.id.QSSubmit);
        idt = (TextView) this.findViewById(R.id.idnumber);
        idn = (TextView) this.findViewById(R.id.numberid);
        userResponse = (EditText) this.findViewById(R.id.user_response);
        stateQuestion = MediaPlayer.create(this, R.raw.get);
        context = getApplicationContext();

            Log.e("BDUKSSS","Setting Audio Stream");
        stateQuestion.setAudioStreamType(AudioManager.STREAM_MUSIC);
        stateQuestion.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("BDUKSSS", "I crashed here Q.Q");
                return false;
            }
        });
            Log.e("BDUKSSS", "Crash check #1");
        callSpeech();

        try {
            InputStream inp = openFileInput(STOREID);

            if (inp != null) {
                InputStreamReader temp = new InputStreamReader(inp);

                BufferedReader read = new BufferedReader(temp);

                String str;

                StringBuilder buf = new StringBuilder();

                while ((str = read.readLine()) != null) {

                    buf.append(str+"\n");

                }
                inp.close();

                idn.setText(buf.toString());
            }
        } catch (IOException t) {
            Toast.makeText( this, "Exception: " + context.toString(), Toast.LENGTH_LONG).show();
        }

        endButton.setOnClickListener(onClickListener);
        sendButton.setOnClickListener(onClickListener);
    }

    //Start speech input call
    private void callSpeech() {
        stateQuestion.start();

        stateQuestion.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("BDUKSSS", "Playback completed");
                stateQuestion.stop();

                promptSpeechInput();
            }
        });
    }

    void callTimer () {
        timer = new CountDownTimer( 20000, 1000) {
            int sec = 20000;
            int countdown = sec;

            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("BDUKSSS", "Time left: " + countdown + " seconds");
                countdown-= 1000;
            }

            @Override
            public void onFinish() {
                promptSpeechInput();
            }  }.start();
    }

    // Show google speech input dialog
    private void promptSpeechInput() {
            Log.e("BDUKSSS", "Ask for input");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }

        callTimer();
    }

    // Receive speech input
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    userResponse.setText(result.get(0));
                }
                String response = userResponse.getText().toString();

                Log.e("BDUKSSS", response);

                isNumber (response);
                sendButton.performClick();

                break;
            }
        }
    }


    private boolean isNumber (String word){
        boolean isNumber = false;
        try {
            Integer.parseInt(word);
            isNumber = true;
        } catch (NumberFormatException e) {
            isNumber = false;
        }
        return isNumber;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.end_shift:
                    // When end shift button is pressed
                    Log.e("BDUKSSS", "Return to Main");

                    String IDText = String.valueOf(idt.getText());
                    String IDNumber = String.valueOf(idn.getText());
                    String popup = "Goodbye " + IDText + " " + IDNumber;
                    Log.e("BDUKSSS", "Return to Main2");
                    Toast toast = Toast.makeText(context, popup, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();
                    Log.e("BDUKSSS", "Return to Main3");

                    timer.cancel();
                    stateQuestion.release();
                    Log.e("BDUKSSS", "Return to Main4");
                    Intent goBack = new Intent(QuestionScreen.this, MainActivity.class);
                    startActivity(goBack);
                    Log.e("BDUKSSS", "Return to Main5");
                    finish();
                    Log.e("BDUKSSS", "Return to Main6");


                   break;

                case R.id.QSSubmit:
                    // When Question Screen Submit button is pressed
                    Log.e("BDUKSSS", "Submit Result");

                    String Date = DateFormat.getDateTimeInstance().format(new Date());

                    EditText editText = (EditText) findViewById(R.id.user_response);
                    String message = String.valueOf(editText.getText()) + "      " + Date + "\n";

                    Toast paste = Toast.makeText(context, message + " sent", Toast.LENGTH_LONG);
                    paste.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    paste.show();

                    try {
                        OutputStreamWriter out =
                                new OutputStreamWriter(openFileOutput(STORETEXT, MODE_APPEND));

                        out.write(message);

                        out.close();
                    } catch (Throwable t) {
                        Toast.makeText( QuestionScreen.this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
                    }

                    break;
            }

        }
    };
}
