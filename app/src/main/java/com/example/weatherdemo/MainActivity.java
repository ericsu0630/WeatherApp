package com.example.weatherdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView weatherText;
    EditText cityTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherText = findViewById(R.id.weatherTextView);
    }

    public void checkWeather(View view){
        GetWeather weather = new GetWeather();
        cityTextView = findViewById(R.id.cityTextView);
        String city = cityTextView.getText().toString();
        if (city.contains(" ")) {
            city = city.replaceAll(" ", "%20"); //replaces all spaces with %20
        }
        String urlString = getString(R.string.api_url);
        urlString += city + getString(R.string.api_key);
        Log.i("URL", urlString);
        String weatherData;
        try {
            weatherData = weather.execute(urlString).get();
            Log.i("JSON", weatherData);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Get Failed", "Something went wrong!");
        }
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