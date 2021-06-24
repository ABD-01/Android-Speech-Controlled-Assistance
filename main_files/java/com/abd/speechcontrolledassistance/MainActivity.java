package com.abd.speechcontrolledassistance;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

import static com.abd.speechcontrolledassistance.ConfigureActivity.APIKEY;
import static com.abd.speechcontrolledassistance.ConfigureActivity.DEVICEID;
import static com.abd.speechcontrolledassistance.ConfigureActivity.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final Integer RecordAudioRequestCode = 1;
    public static TextToSpeech textToSpeech;

    private TextView recoText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Created MainActivity");
        About.show(this);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
            }
        });
//        ref: https://programmerworld.co/android/how-to-create-a-personal-voice-assistant-android-app-to-create-a-text-file-complete-source-code/

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
//        ref: https://github.com/abhinav0612/SpeechToText/blob/master/app/src/main/java/com/example/texttospeech/MainActivity.java#L37

        recoText = findViewById(R.id.recoText);
        button = findViewById((R.id.speak));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "Button clicked!");
                System.out.println("Button is Clicked");
                textToSpeech.speak("What is your Command", TextToSpeech.QUEUE_ADD, null);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                startActivityForResult(intent, 10);
//                ref: https://github.com/AnasAlmasri/VoiceRecognitionCalculator/blob/master/app/src/main/java/com/anas/voicerecognitioncalculator/MainActivity.java
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOG_TAG, "Activity Result out");
        System.out.println("Activity Result out");
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 10) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                assert result != null;
                recoText.setText(result.get(0));
                startRequestThread(result.get(0));
            }
        }
    }

    private void startRequestThread(String command) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String api = sharedPreferences.getString(APIKEY, "");
        String id = sharedPreferences.getString(DEVICEID, "");
        RequestActions requestAction = new RequestActions(this, api, id, command);
        new Thread(requestAction).start();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
//    ref: https://www.youtube.com/watch?v=oh4YOj9VkVE
    }

    public void configure(MenuItem item) {
        Log.d(LOG_TAG, "Configuring");
        Intent intent = new Intent(this, ConfigureActivity.class);
        startActivity(intent);
    }

    public void showAbout(MenuItem item) {
        About.show(this);
    }
}