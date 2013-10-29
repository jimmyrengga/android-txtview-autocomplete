package com.github.jimmyrengga.sample.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private SQLiteUserAssistant sqLiteUserAssistant;
    TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        sqLiteUserAssistant = new SQLiteUserAssistant(MainActivity.this);
        sqLiteUserAssistant.openDB();

        List<User> list = getUser();
        for(User user: list) {
            insertData(user);
        }

        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        List<User> users = sqLiteUserAssistant.getAllUsers();
        for(User u: users){
            Log.i(this.toString(), u.getUsername());
        }

        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, R.layout.list_item, users);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User selected = (User) adapterView.getAdapter().getItem(i);
                tv1.setText(selected.getUsername());
                tv2.setText(selected.getFullname());
            }
        });
    }

    public List<User> getUser() {
        List<User> listUser = new ArrayList<User>();

        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            if(jsonArray.length() < 0) {
                Toast.makeText(getApplicationContext(), "nol besar", Toast.LENGTH_LONG).show();
                return new ArrayList<User>();
            } else {
                Toast.makeText(getApplicationContext(), "jumlah data : " + jsonArray.length(), Toast.LENGTH_LONG).show();

                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject jso = jsonArray.getJSONObject(i);
                    Log.i(MainActivity.class.getName(), jso.getString("username"));

                    User u = new User();
                    u.setUsername(jso.getString("username"));
                    u.setFullname(jso.getString("fullname"));
                    listUser.add(u);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listUser;
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

    private void insertData(User user) {
        sqLiteUserAssistant.insertUser(user.getUsername(), user.getFullname());
    }

}
