package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity{
    TextView cityName;
    Button search;
    TextView show;
    String url;

    // AsyncTask to fetch weather data
    class GetWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject main = jsonObject.getJSONObject("main");
                    String temperature = main.getString("temp"); // Extract temperature
                    String humidity = main.getString("humidity"); // Extract humidity

                    String weatherInfo = "Temperature: " + temperature + "\nHumidity: " + humidity;
                    show.setText(weatherInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                    show.setText("Error parsing weather data");
                }
            } else {
                show.setText("Error fetching weather data");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        show = findViewById(R.id.weather);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityName.getText().toString().trim();
                if (!city.isEmpty()) {
                    // Replace with your actual API key
                    url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=c7d3606162c3307c228753ef7d405cec&units=metric";
                    new GetWeather().execute(url);
                } else {
                    Toast.makeText(MainActivity.this, "Enter a city name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
