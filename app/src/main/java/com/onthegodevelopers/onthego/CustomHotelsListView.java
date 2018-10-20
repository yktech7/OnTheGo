package com.onthegodevelopers.onthego;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomHotelsListView extends ArrayAdapter<String> {

    private String [] hotelNames;
    private String [] foodTypes;
    private Integer [] ImageIDs;
    private Activity context;

    public CustomHotelsListView(Activity context, String [] hotelNames, String [] foodTypes, Integer [] ImageIDs) {
        super(context, R.layout.hotelslistviewlayout, hotelNames);

        this.context = context;
        this.hotelNames = hotelNames;
        this.foodTypes = foodTypes;
        this.ImageIDs = ImageIDs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.hotelslistviewlayout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.hotelNameText.setText(hotelNames[position]);
        viewHolder.foodTypesText.setText(foodTypes[position]);
        viewHolder.foodImage.setImageResource(ImageIDs[position]);

        return r;
    }

    class ViewHolder
    {
        TextView hotelNameText;
        TextView foodTypesText;
        ImageView foodImage;
        ViewHolder(View v){
            hotelNameText = (TextView) v.findViewById(R.id.hotelNameID);
            foodTypesText = (TextView) v.findViewById(R.id.foodTypesID);
            foodImage = (ImageView) v.findViewById(R.id.foodImageID);
        }
    }


}
