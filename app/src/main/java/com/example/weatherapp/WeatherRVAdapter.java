package com.example.weatherapp;

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
    private Context context;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        WeatherRVModal modal = weatherRVModalArrayList.get(position);
        holder.temperatureTV.setText(modal.getTemperature() + "°C");
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.conditionIV);
        holder.windTV.setText(modal.getWindSpeed() + " Km/Jam");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm");

        try {
            Date t = input.parse(modal.getTime());
            holder.timeTV.setText(output.format(t));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timeTV, temperatureTV, windTV;
        private ImageView conditionIV;
        public ViewHolder(@NonNull ViewGroup parent) {
            super(parent);
            timeTV = parent.findViewById(R.id.idTVTime);
            temperatureTV = parent.findViewById(R.id.idTVTemperature);
            windTV = parent.findViewById(R.id.idTVWindSpeed);
            conditionIV = parent.findViewById(R.id.idIVCondition);

        }
    }
}
