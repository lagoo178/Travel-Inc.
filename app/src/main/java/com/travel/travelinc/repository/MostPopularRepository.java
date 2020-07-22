package com.travel.travelinc.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import com.travel.travelinc.R;
import com.travel.travelinc.models.Place;

import java.util.ArrayList;

public class MostPopularRepository {

    public static ArrayList<Place> getDataMostPopularPlaces(Context context){
        String[] dataTitle = context.getResources().getStringArray(R.array.title_place);
        String[] dataDesc = context.getResources().getStringArray(R.array.desc_place);
        String[] dataRating = context.getResources().getStringArray(R.array.rating_place);
        String[] dataDetails = context.getResources().getStringArray(R.array.details_place);
        @SuppressLint("Recycle") TypedArray dataImagePlace = context.getResources().obtainTypedArray(R.array.image_place);
        @SuppressLint("Recycle") TypedArray dataImageMenuPlace = context.getResources().obtainTypedArray(R.array.menu_image_place);

        ArrayList<Place> places = new ArrayList<>();
        for (int i = 0; i < dataTitle.length; i++){
            String title = dataTitle[i];
            String desc = dataDesc[i];
            String rating = dataRating[i];
            String details = dataDetails[i];
            int imagePlace = dataImagePlace.getResourceId(i, 0);
            int imageMenuPlace = dataImageMenuPlace.getResourceId(i, 0);
            places.add(new Place(i, imagePlace, title, desc, Float.parseFloat(rating), imageMenuPlace, details));
        }

        return places;
    }

}
