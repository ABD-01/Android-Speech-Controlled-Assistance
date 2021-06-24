package com.abd.speechcontrolledassistance;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.abd.speechcontrolledassistance.MainActivity.textToSpeech;

@SuppressWarnings("ALL")
public class RequestActions implements Runnable {

    public String API_KEY;
    public String DeviceId;
    public String command;
    public MainActivity context;

    public String reply;

    public RequestActions(MainActivity context, String api, String id, String command)
    {
        this.context = context;
        this.API_KEY = api;
        this.DeviceId = id;
        this.command = command.toLowerCase();
        Log.d(MainActivity.LOG_TAG, "Created new instatnce of RequestAction class with command: "+command);
    }

    @Override
    public void run() {
        if(this.API_KEY.isEmpty() || this.DeviceId.isEmpty()) {
            reply = "Please configure your Device.";
            toastAndSpeak(reply);
//            Toast.makeText(this.context, reply,Toast.LENGTH_LONG).show(); "This Lines fucks up the whole process."
            return;
        }
        main(command);
    }

    private void main(String command) {
        Log.d(MainActivity.LOG_TAG, "Started Main Function");
        String[] commandON = new String[] {"on", "turn on", "switch on", "open", "start"};
        String[] commandOFF = new String[] {"off", "turn off", "switch off", "close", "stop"};
        // ref: https://stackoverflow.com/questions/18885043/better-way-to-detect-if-a-string-contains-multiple-words/18885081
        for (String c : commandON)
        {
            if (command.contains(c))
            {
                performAction(command, true);
                return;
            }
        }

        for (String c : commandOFF)
        {
            if (command.contains(c))
            {
                performAction(command, false);
                return;
            }
        }

        // Invalid Command
        reply = "Invalid Command! Try Again.";
        toastAndSpeak(reply);
    }

    private void performAction(String command, boolean state) {
        Log.d(MainActivity.LOG_TAG, "Started performAction Function with state " + Boolean.toString(state));
        String[] Pins = new String[] {"light", "fan", "door", "projector", "curtains"};

        for (int i = 0; i < Pins.length; i++) {
            if (command.contains(Pins[i])) {
                sendRequest(Pins[i], i, state);
                return;
            }
        }
        // Invalid Command
        reply = "Unknown Appliance! Try Again.";
        toastAndSpeak(reply);
    }

    private void parseResponse(String name, boolean state, int success, String value) {
        Log.d(MainActivity.LOG_TAG, "Parsing Respone");
        if (success == 0) {
            reply = "Operation failed. " + value;
            toastAndSpeak(reply);
        }
        else if (success == 1) {
            reply = ((name.equals("light") || name.equals("fan")) ? "Switching " + (state ? "On": "Off") : (state ? "Opening": "Closing") ) + " the " + name;
            toastAndSpeak(reply);
        }
    }

    private void sendRequest(final String device, int pin, final boolean state) {
        String url = "https://cloud.boltiot.com/remote/" + API_KEY + "/digitalWrite?" + "deviceName=" + DeviceId;
        url += ("&pin=" + pin + "&state=" + (state ? "HIGH": "LOW"));
        Log.d(MainActivity.LOG_TAG, "Sending request to " +  url);

        RequestQueue queue = Volley.newRequestQueue(this.context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(MainActivity.LOG_TAG, "Respone successfull");
                            int success = response.getInt("success");
                            String value = response.getString("value");
                            parseResponse(device, state, success, value);
                        } catch (JSONException e) {
                            Log.d(MainActivity.LOG_TAG, "OnErrorResponse: "+e.toString());
                            reply = "Error occurred while parsing Json. Try Again!!";
                            toastAndSpeak(reply);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(MainActivity.LOG_TAG, "OnErrorResponse: "+error.toString());
                        reply = "Error occurred while sending request. Try Again!!";
                        toastAndSpeak(reply);
                    }
                });
        queue.add(request);
    // ref: https://www.youtube.com/watch?v=y2xtLqP8dSQ
    }

    private void toastAndSpeak(final String reply) {
        final MainActivity ctx = this.context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                textToSpeech.speak(reply, TextToSpeech.QUEUE_ADD, null);
                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ctx, reply,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

}