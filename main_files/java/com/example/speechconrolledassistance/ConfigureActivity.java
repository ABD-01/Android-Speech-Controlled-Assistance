package com.example.speechconrolledassistance;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigureActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedConfig";
    public static final String APIKEY = "API_KEY";
    public static final String DEVICEID = "DeviceID";

    EditText apiKey;
    EditText deviceId;
    Button save;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        apiKey = findViewById(R.id.apikey);
        deviceId = findViewById(R.id.deviceid);
        save = findViewById(R.id.saveconfig);
        cancel = findViewById(R.id.cancel);

        loadData();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(apiKey.getText().toString())) {
                    apiKey.setError("API_KEY field cannot be Empty");
                    return;
                }
                if (TextUtils.isEmpty(deviceId.getText().toString())) {
                    deviceId.setError("Device ID field cannot be Empty");
                    return;
                }
                saveData();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(APIKEY, apiKey.getText().toString());
        editor.putString(DEVICEID, deviceId.getText().toString());
        editor.apply();

        Toast.makeText(this, "Configuration Updated", Toast.LENGTH_LONG).show();
//	    ref: https://gist.github.com/codinginflow/b4f4c0cb30dbc135129c89fa13c184a1
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String api = sharedPreferences.getString(APIKEY, "");
        String id = sharedPreferences.getString(DEVICEID, "");
        apiKey.setText(api);
        deviceId.setText(id);
    }
}