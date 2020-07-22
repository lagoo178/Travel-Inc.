package com.travel.travelinc.maps;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.travel.travelinc.R;
import com.travel.travelinc.post.PostingSupport;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.map_pop_up, null);

        /*TextView placeName = view.findViewById(R.id.catagoryName);
        TextView catagoryType = view.findViewById(R.id.catagoryType);
        ImageView img = view.findViewById(R.id.mapImage);*/

        TextView areaName = view.findViewById(R.id.PostAreaName);
        ImageView img = view.findViewById(R.id.PostImage);
        TextView postName = view.findViewById(R.id.PostName);
        TextView descriptin = view.findViewById(R.id.PostDescription);
        TextView postContributor = view.findViewById(R.id.postContributor);

        TextView type = view.findViewById(R.id.postType);


        type.setText(marker.getTitle());
        areaName.setText(marker.getSnippet());
        PostingSupport postingSupport = (PostingSupport) marker.getTag();

        postName.setText(postingSupport.getName());
        descriptin.setText(postingSupport.getDescription());
        postContributor.setText(postingSupport.getUserName());


        try {
            Picasso.get().load(postingSupport.getImageuri()).into(img);

        } catch (Exception e) {

        }
        return view;
    }
}
