package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private TextInputEditText cityEdt;
    private ImageView backIV;
    private ImageView iconIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    final private int PERMISSION_CODE = 1;
    private String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        RecyclerView weatherRV = findViewById(R.id.idRVWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIVIcon);
        ImageView searchIV = findViewById(R.id.idIVSearch);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        assert location != null;
        cityName = getCityName(location.getLatitude(), location.getLongitude());

        getWeatherInfo(cityName);

        searchIV.setOnClickListener(v -> {
            String city = Objects.requireNonNull(cityEdt.getText()).toString();
            if(city.isEmpty()) {
                Toast.makeText(MainActivity.this, "Masukkan nama kota", Toast.LENGTH_SHORT).show();
            } else {
                cityNameTV.setText(cityName);
                getWeatherInfo(city);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == PERMISSION_CODE) {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Izin diberikan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Izin ditolak. Aktifkan izin melalui Pengaturan Aplikasi.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
    }

    private String getCityName(double latitude, double longitude) {
        String cityName = "Banjarbaru"; // Default city
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            if (addresses != null && !addresses.isEmpty()) {
                for (Address adr : addresses) {
                    if (adr != null) {
                        String city = adr.getLocality();
                        if (city != null && !city.isEmpty()) {
                            cityName = city;
                            break; // Keluar dari loop jika nama kota ditemukan
                        } else {
                            Log.d("TAG", "CITY NOT FOUND");
                            Toast.makeText(this, "Kota tidak ditemukan...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Log.d("TAG", "ADDRESS LIST EMPTY");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "Geocoder exception: " + e.getMessage());
        }

        return cityName;
    }

    private void getWeatherInfo(String cityName) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=d79d65badb0341f88ee93952231712&q=" + cityName + "&days=1&aqi=no&alerts=no";

        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"}) JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            loadingPB.setVisibility(View.GONE);
            homeRL.setVisibility(View.VISIBLE);
            weatherRVModalArrayList.clear();
            try {
                String temperature = response.getJSONObject("current").getString("temp_c");
                temperatureTV.setText(temperature + "°C");
                int isDay = response.getJSONObject("current").getInt("is_day");
                String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                conditionTV.setText(condition);
                if(isDay == 1) {
                    Picasso.get().load("https://i.ibb.co/PhvXk5T/wes-hicks-XPd-Ajxs-HXo-unsplash-1.jpg").into(backIV);
                } else {
                    Picasso.get().load("https://i.ibb.co/HN0ndFc/timothee-duran-ilfs-T5p-qv-A-unsplash.jpg").into(backIV);
                }

                JSONObject forecastObj = response.getJSONObject("forecast");
                JSONObject forcast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                JSONArray hourArray = forcast0.getJSONArray("hour");

                for(int i = 0; i < hourArray.length(); i++) {
                    JSONObject hourObj = hourArray.getJSONObject(i);
                    String time = hourObj.getString("time");
                    String temper = hourObj.getString("temp_c");
                    String wind = hourObj.getString("wind_kph");
                    String img = hourObj.getJSONObject("condition").getString("icon");
                    weatherRVModalArrayList.add(new WeatherRVModal(time, temper, img, wind));
                }
                weatherRVAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> Toast.makeText(MainActivity.this, "Tidak dapat mengambil data cuaca", Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);

    }

}