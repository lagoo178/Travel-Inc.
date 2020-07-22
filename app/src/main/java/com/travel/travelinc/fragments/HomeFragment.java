package com.travel.travelinc.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.travel.travelinc.R;
import com.travel.travelinc.activities.DetailsPlaceActivity;
import com.travel.travelinc.adapters.BannerAdapter;
import com.travel.travelinc.adapters.MostPopularPlaceAdapter;
import com.travel.travelinc.repository.BannerRepository;
import com.travel.travelinc.repository.MostPopularRepository;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator2;


public class HomeFragment extends Fragment {

    @BindView(R.id.rvBannerHome)
    RecyclerView rvBannerHome;
    @BindView(R.id.ciBanner)
    CircleIndicator2 ciBanner;
    @BindView(R.id.rvTheMostPopularPlace)
    DiscreteScrollView rvTheMostPopularPlace;

    private SnapHelper snapHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        snapHelper = new PagerSnapHelper();
        setupAdapterBanner();
        setupMostPopularPlaceAdapter();
    }

    private void setupMostPopularPlaceAdapter() {
        InfiniteScrollAdapter wrapper;
        MostPopularPlaceAdapter mostPopularPlaceAdapter = new MostPopularPlaceAdapter(MostPopularRepository.getDataMostPopularPlaces(Objects.requireNonNull(getContext())), place -> {
            Intent intent = new Intent(getContext(), DetailsPlaceActivity.class);
            intent.putExtra(DetailsPlaceActivity.EXTRA_DATA_PLACE, place);
            startActivity(intent);
        });
        wrapper = InfiniteScrollAdapter.wrap(mostPopularPlaceAdapter);
        rvTheMostPopularPlace.setOrientation(DSVOrientation.HORIZONTAL);
        rvTheMostPopularPlace.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .build());
        rvTheMostPopularPlace.setAdapter(wrapper);
    }

    private void setupAdapterBanner() {
        BannerAdapter bannerAdapter = new BannerAdapter()
                .setData(BannerRepository.getDataBanner(Objects.requireNonNull(getContext())))
                .onClick(banner -> Toast.makeText(getContext(), "" + banner.getId(), Toast.LENGTH_SHORT).show());

        rvBannerHome.setAdapter(bannerAdapter);
        snapHelper.attachToRecyclerView(rvBannerHome);
        ciBanner.attachToRecyclerView(rvBannerHome, snapHelper);
        bannerAdapter.registerAdapterDataObserver(ciBanner.getAdapterDataObserver());
    }
}