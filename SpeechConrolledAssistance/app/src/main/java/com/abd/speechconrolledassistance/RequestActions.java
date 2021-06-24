package com.abd.speechconrolledassistance;

import android.speech.tts.TextToSpeech;

public class RequestActions implements Runnable {

    private String API_KEY;
    private String DeviceId;
    private String command;

    public RequestActions(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        MainActivity.textToSpeech.speak("Hello, I'm speaking from different thread. I'm making this unusually long to test the working of threads. Maybe this will be enough",
                TextToSpeech.QUEUE_ADD, null);

    }

}