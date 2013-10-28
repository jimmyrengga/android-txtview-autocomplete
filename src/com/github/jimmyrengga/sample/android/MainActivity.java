package com.github.jimmyrengga.sample.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    

    public void getdata(View v) {
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            if(jsonArray.length() < 0) {
                Toast.makeText(getApplicationContext(), "nol besar", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "jumlah data : " + jsonArray.length(), Toast.LENGTH_LONG).show();

                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject jso = jsonArray.getJSONObject(i);

                    Log.i(MainActivity.class.getName(), jso.getString("username"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("users.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}
