package com.example.weatherdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView weatherText, tempText, nameText;
    EditText cityTextView;
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherText = findViewById(R.id.weatherTextView);
        tempText = findViewById(R.id.tempTextView);
        nameText = findViewById(R.id.nameTextView);
    }

    public void checkWeather(View view){
        GetWeather weather = new GetWeather();
        cityTextView = findViewById(R.id.cityTextView);
        city = cityTextView.getText().toString();
        if (city.contains(" ")) {
            city = city.replaceAll(" ", "%20"); //replaces all spaces with %20
        }
        String urlString = getString(R.string.api_url);
        urlString += city + getString(R.string.api_key);
        Log.i("URL", urlString);
        String allData="";
        String clouds, temp;
        try {
            allData = weather.execute(urlString).get();
            JSONObject jsonObject = new JSONObject(allData);
            clouds = jsonObject.getString("weather");
            city = jsonObject.getString("name");
            nameText.setText(city);
            jsonObject = jsonObject.getJSONObject("main");
            temp = jsonObject.getString("temp");
            double tempDouble = Double.parseDouble(temp);
            tempDouble -= 273.15;
            temp = String.format("%.1f", tempDouble);
            temp += "ºC";
            Log.i("JSON", temp);
            tempText.setText(temp);
            JSONArray cloudArray = new JSONArray(clouds);
            for(int i=0;i<cloudArray.length();i++){
                JSONObject part = cloudArray.getJSONObject(i);
                String w = part.getString("description");
                Log.i("JSON", w);
                weatherText.setText(w);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Get Failed", "Something went wrong!");
        }
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(cityTextView.getWindowToken(),0);

    }

    public void updateText(String jsonArray){
        //JSONObject jo = new JSONObject(jsonArray);
    }

    public class GetWeather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data;
                do{
                    data = reader.read();
                    char c = (char) data;
                    s += c;
                }while(data!=-1);
                return s;
            }catch(Exception e){
                e.printStackTrace();
                return "connection failed";
            }
        }
    }
}