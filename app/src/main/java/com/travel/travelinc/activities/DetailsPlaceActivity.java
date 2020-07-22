package com.travel.travelinc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.travel.travelinc.R;
import com.travel.travelinc.models.Place;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsPlaceActivity extends AppCompatActivity {

    @BindView(R.id.tvTitleDetailsPlace)
    TextView tvTitleDetailsPlace;
    @BindView(R.id.tvDescDetailsPlace)
    TextView tvDescDetailsPlace;
    @BindView(R.id.ratingBarDetailsPlace)
    RatingBar ratingBarDetailsPlace;
    @BindView(R.id.ivMenuDetailsPlace)
    ImageView ivMenuDetailsPlace;
    @BindView(R.id.tvDetails)
    TextView tvDetails;
    @BindView(R.id.ivDetailsPlace)
    ImageView ivDetailsPlace;

    public static String EXTRA_DATA_PLACE = "extra_data_place";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_place);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getDataExtra();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataExtra() {
        if (getIntent() != null){
            Place place = getIntent().getParcelableExtra(EXTRA_DATA_PLACE);
            if (place != null){
                setView(place);
            }
        }
    }

    private void setView(Place place) {
        tvTitleDetailsPlace.setText(place.getTitle());
        tvDescDetailsPlace.setText(place.getDesc());
        ratingBarDetailsPlace.setMax(5);
        ratingBarDetailsPlace.setRating(place.getRate());
        tvDetails.setText(place.getDetails());
        Picasso.get().load(place.getImageMenu()).into(ivMenuDetailsPlace);
        Picasso.get().load(place.getImage()).into(ivDetailsPlace);
    }
}