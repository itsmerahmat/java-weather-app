package com.example.weatherapp;
// Import Library yang diperlukan
import android.annotation.SuppressLint; // Untuk memberikan informasi tambahan tentang anotasi
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    // Deklarasi Variabel yang diperlukan
    private final Context context; // Untuk mengakses resource
    // Membuat ArrayList untuk menampung data
    private final ArrayList<WeatherRVModal> weatherRVModalArrayList;

    // Membuat Constructor
    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull // Untuk menandakan bahwa nilai yang dikembalikan tidak boleh null
    // Membuat ViewHolder untuk menampung data
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder((ViewGroup) view);
    }

    // Menghubungkan data dengan ViewHolder
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        WeatherRVModal modal = weatherRVModalArrayList.get(position);
        holder.temperatureTV.setText(modal.getTemperature() + "°C");
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.conditionIV);
        holder.windTV.setText(modal.getWindSpeed() + " Km/H");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("hh:mm");

        try {
            Date t = input.parse(modal.getTime());
            assert t != null; // Untuk mengecek apakah t bernilai null
            holder.timeTV.setText(output.format(t));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Mengembalikan jumlah item yang ada
    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView timeTV;
        private final TextView temperatureTV;
        private final TextView windTV;
        private final ImageView conditionIV;
        public ViewHolder(@NonNull ViewGroup parent) {
            super(parent);
            timeTV = parent.findViewById(R.id.idTVTime);
            temperatureTV = parent.findViewById(R.id.idTVTemperature);
            windTV = parent.findViewById(R.id.idTVWindSpeed);
            conditionIV = parent.findViewById(R.id.idIVCondition);

        }
    }
}
