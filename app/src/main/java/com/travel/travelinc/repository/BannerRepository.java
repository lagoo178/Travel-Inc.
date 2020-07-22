package com.travel.travelinc.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;

import com.travel.travelinc.R;
import com.travel.travelinc.models.Banner;

import java.util.ArrayList;

public class BannerRepository {

    public static ArrayList<Banner> getDataBanner(Context context){
        @SuppressLint("Recycle") TypedArray dataBanner = context.getResources().obtainTypedArray(R.array.image_banner);

        ArrayList<Banner> banners = new ArrayList<>();
        for (int i = 0; i < dataBanner.length(); i++){
            int banner = dataBanner.getResourceId(i, 0);
            banners.add(new Banner(i, banner));
        }

        return banners;
    }

}
