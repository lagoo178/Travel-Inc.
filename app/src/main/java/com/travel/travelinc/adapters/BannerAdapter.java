package com.travel.travelinc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.travel.travelinc.R;
import com.travel.travelinc.models.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private ArrayList<Banner> banners;
    private ItemClickListener itemClickListener;
    private static BannerAdapter instance = null;

    public BannerAdapter(){
    }

    private BannerAdapter(ArrayList<Banner> banners, ItemClickListener itemClickListener){
        this.banners = banners;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(banners.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public BannerAdapter setData(ArrayList<Banner> banners){
        this.banners = banners;
        notifyDataSetChanged();
        return this;
    }

    public BannerAdapter onClick(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
        return this;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivBanner)
        ImageView ivBanner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Banner banner, ItemClickListener itemClickListener) {

            if (banner != null){
                Picasso.get().load(banner.getImage()).placeholder(R.drawable.logo_cover).into(ivBanner);

                itemView.setOnClickListener(v -> {
                    itemClickListener.onClick(banner);
                });
            }
        }
    }

    public interface ItemClickListener{
        void onClick(Banner banner);
    }
}
