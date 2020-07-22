package com.travel.travelinc.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travel.travelinc.R;
import com.travel.travelinc.adapters.RecyclePostAdapter;
import com.travel.travelinc.post.PostingSupport;

import java.util.List;

import static com.travel.travelinc.post.UserFeedActivity.arrayList;

public class PostView extends Fragment {

    RecyclerView recyclerView;
    RecyclePostAdapter recyclePostAdapter;
    ProgressDialog progressDialog;
    List<PostingSupport> supportList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_post_view,container,false);

        recyclerView = view.findViewById(R.id.showpostview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadData();

        return view;
    }

    private void loadData() {
        //arrayList.clear();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("lodading ..");
        progressDialog.show();
        try
        {
            //supportList = arrayList;
            Log.d("Array Size ",Integer.toString(arrayList.size()));

            recyclePostAdapter = new RecyclePostAdapter(arrayList,getActivity());
            progressDialog.dismiss();
            recyclePostAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(recyclePostAdapter);

        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}