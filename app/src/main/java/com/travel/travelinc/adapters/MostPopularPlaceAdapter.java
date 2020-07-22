package com.travel.travelinc.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.travel.travelinc.R;
import com.travel.travelinc.models.Place;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MostPopularPlaceAdapter extends RecyclerView.Adapter<MostPopularPlaceAdapter.ViewHolder> {

    private ArrayList<Place> places;
    private ItemClickListener itemClickListener;

    public MostPopularPlaceAdapter(ArrayList<Place> places, ItemClickListener itemClickListener){
        this.places = places;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_the_most_popular_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(places.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivMostPopularHome)
        ImageView ivMostPopularHome;
        @BindView(R.id.tvTitleTheMostPopular)
        TextView tvTitleTheMostPopular;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Place place, ItemClickListener itemClickListener) {
            Picasso.get().load(place.getImage()).placeholder(R.drawable.logo_cover).into(ivMostPopularHome);
            tvTitleTheMostPopular.setText(place.getTitle());
            itemView.setOnClickListener(v -> itemClickListener.onClick(place));
        }
    }

    public interface ItemClickListener{
        void onClick(Place place);
    }
}
