package com.travel.travelinc.post;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.travel.travelinc.MapView;
import com.travel.travelinc.R;

import androidx.appcompat.app.AppCompatDialogFragment;

public class SearchDialog extends AppCompatDialogFragment {
    private EditText SearchLocation,SearchRadius;
    private RadioGroup radioGroup;

    static String qloaction;
    static String qradius;
    static String qtype;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.layout_search_dialog,null);

        builder.setView(view)
                .setTitle("Search Location")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                qloaction = SearchLocation.getText().toString().trim();
                qradius = SearchRadius.getText().toString().trim();
                int selected = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = view.findViewById(selected);
                qtype =radioButton.getText().toString().trim();
                // arrayList.clear();///call

            }
        });

        SearchLocation = view.findViewById(R.id.searchLoc_searchdlg_edittext);
        radioGroup = view.findViewById(R.id.radio_search_rGrp);

        return builder.create();
    }

    public static String getQtype(){return qtype;}
    public static String getQloaction(){return qloaction;}
    public static String getQradius(){ return qradius;}
}

