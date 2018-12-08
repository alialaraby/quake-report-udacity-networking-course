package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuakesCustomAdapter extends ArrayAdapter<Quakes> {


    public QuakesCustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Quakes> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Quakes quakes = getItem(position);

        String secondaryLoationValue = "";

        View view = convertView;

        if (view == null){

            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        TextView magnitude = (TextView) view.findViewById(R.id.magnitude_list_item);
        magnitude.setText(String.valueOf(quakes.getMagnitude()));

        GradientDrawable magCircleBackground = (GradientDrawable) magnitude.getBackground();
        int magColor = getMagnitudeColor(quakes.getMagnitude());
        magCircleBackground.setColor(magColor);

        TextView primaryLocation = (TextView) view.findViewById(R.id.primary_location_list_item);
        primaryLocation.setText(quakes.getPrimaryLocation());

        TextView locationOffset = (TextView) view.findViewById(R.id.location_offset_list_item);
        secondaryLoationValue = quakes.getLocationOffset();
        //if the location offset doesn`t exist we make the text view disappear
        if (secondaryLoationValue.isEmpty()){
            locationOffset.setVisibility(View.GONE);
        }else {
            locationOffset.setText(secondaryLoationValue);
        }

        TextView date = (TextView) view.findViewById(R.id.date_list_item);
        date.setText(String.valueOf(quakes.getDate()));

        TextView time = (TextView) view.findViewById(R.id.time_list_item);
        time.setText(String.valueOf(quakes.getTime()));

        return view;
    }

    public int getMagnitudeColor(double magnitude){

        int colorResourceId = 0;
        int colorAsInt = (int) Math.floor(magnitude);

        switch (colorAsInt){
            case (1) :
                colorResourceId = R.color.magnitude1;
                break;
            case 2 :
                colorResourceId = R.color.magnitude2;
                break;
            case 3 :
                colorResourceId = R.color.magnitude3;
                break;
            case 4 :
                colorResourceId = R.color.magnitude4;
                break;
            case 5 :
                colorResourceId = R.color.magnitude5;
                break;
            case 6 :
                colorResourceId = R.color.magnitude6;
                break;
            case 7 :
                colorResourceId = R.color.magnitude7;
                break;
            case 8 :
                colorResourceId = R.color.magnitude8;
                break;
            case 9 :
                colorResourceId = R.color.magnitude9;
                break;
            case 10 :
                colorResourceId = R.color.magnitude10plus;
                break;
            default:
                colorResourceId = R.color.magnitude1;
                break;
        }
        return ContextCompat.getColor(getContext(), colorResourceId);
    }
}
