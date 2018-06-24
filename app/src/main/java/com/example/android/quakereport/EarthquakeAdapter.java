package com.example.android.quakereport;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
public class EarthquakeAdapter extends ArrayAdapter<Earthquake>{
    static ArrayList<Earthquake> mList;
    private Earthquake currentQuakeEvent;
    public EarthquakeAdapter(Context context, ArrayList<Earthquake> list) {
        super(context, 0, list);
        mList = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
        }

        currentQuakeEvent = mList.get(position);
        TextView magnitudeText = (TextView) convertView.findViewById(R.id.magnitude);
        TextView distanceText = (TextView) convertView.findViewById(R.id.locationDistance);
        TextView regionText = (TextView) convertView.findViewById(R.id.locationRegion);
        TextView dateText = (TextView) convertView.findViewById(R.id.date);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeText.getBackground();
        magnitudeCircle.setColor(setMagnitudeColor(currentQuakeEvent.getmMagnitude()));
        magnitudeText.setText(String.valueOf(currentQuakeEvent.getmMagnitude()));
        magnitudeText.getBackground();

        if (currentQuakeEvent.hasDistance()) {
            distanceText.setText(currentQuakeEvent.getLocationDistance());
        } else {
            distanceText.setVisibility(View.GONE);
        }
        regionText.setText(currentQuakeEvent.getLocationRegion());
        dateText.setText((currentQuakeEvent.getmDateToString()));
        return convertView;
    }
    private int setMagnitudeColor(double mag) {
        int magnitude = (int) Math.floor(mag);
        int color = -1;
        switch (magnitude) {
            case 0:
            case 1:
                color = getContext().getResources().getColor(R.color.magnitude1);
                break;
            case 2:
                color = getContext().getResources().getColor(R.color.magnitude2);
                break;
            case 3:
                color = getContext().getResources().getColor(R.color.magnitude3);
                break;
            case 4:
                color = getContext().getResources().getColor(R.color.magnitude4);
                break;
            case 5:
                color = getContext().getResources().getColor(R.color.magnitude5);
                break;
            case 6:
                color = getContext().getResources().getColor(R.color.magnitude6);
                break;
            case 7:
                color = getContext().getResources().getColor(R.color.magnitude7);
                break;
            case 8:
                color = getContext().getResources().getColor(R.color.magnitude8);
                break;
            case 9:
                color = getContext().getResources().getColor(R.color.magnitude9);
                break;
            default:
                color = getContext().getResources().getColor(R.color.magnitude10plus);
                break;
        }
        return color;
    }
}