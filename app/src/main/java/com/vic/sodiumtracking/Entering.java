package com.vic.sodiumtracking;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.layout.simple_dropdown_item_1line;


public class Entering extends Fragment {

    DBAdapter db = null;
    Cursor actvCursor;
    AutoCompleteTextView actv;
    ArrayAdapter<String> actvAdapter = null;
    EditText foodNameField, sodAmtField;
    Button confirmbtn;
    String str=null;
    static String str2=null;
    ArrayList<String> sodValdata = new ArrayList<>();
    ArrayList<String> foodName = new ArrayList<String>();
    String[] soddata=null;
    String[] foodname=null;
    View v;


    ActivityCommunicator mCallback;

    public interface ActivityCommunicator {
        public void enteringFragToActivity(int sodValue);
    }

    // Required empty public constructor
    public Entering() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_entering, container, false);
        Context c = getActivity().getApplicationContext();

        db = new DBAdapter(c);
        db.open();
        actvCursor = db.getAllItems();

        try {
            if (actvCursor.moveToFirst()) {
                while (!actvCursor.isAfterLast()) {
                    foodName.add(actvCursor.getString(actvCursor.getColumnIndex("name")));
                    sodValdata.add(actvCursor.getString(actvCursor.getColumnIndex("value")));
                    actvCursor.moveToNext();
                }
            }
        }finally {//http://stackoverflow.com/questions/12801602/android-sqlite-leaked
            if(actvCursor != null && !actvCursor.isClosed()){
                actvCursor.close();
            }
        }

        soddata = new String[sodValdata.size()];
        soddata =  sodValdata.toArray(soddata);
        foodname = new String[foodName.size()];
        foodname = foodName.toArray(foodname);

        actv = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);
        actvAdapter = new ArrayAdapter<>(c, simple_dropdown_item_1line, foodname);
        actv.setDropDownBackgroundResource(R.color.actvbkcolor);
        actv.setAdapter(actvAdapter);
        actv.setThreshold(1);
        actv.setTextColor(Color.RED);

        final String[] finalSoddata = soddata;
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodNameField = (EditText) v.findViewById(R.id.foodNameField) ;
                sodAmtField = (EditText) v.findViewById(R.id.sodAmtField);
                str = parent.getItemAtPosition(position).toString();
                str2 = finalSoddata[position];

                foodNameField.setText(str);
                sodAmtField.setText(str2);
            }
        });

        confirmbtn = (Button) v.findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(btnOnClickListener);

        return v;
    }


    Button.OnClickListener btnOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == confirmbtn){
                if(str != null && str2 != null) {
                    int sodValue = Integer.parseInt(str2);
                    Toast.makeText(getActivity(),"The value you picked is: " + str2, Toast.LENGTH_LONG ).show();
                    mCallback.enteringFragToActivity(sodValue);

                }
                else{
                    Toast.makeText(getActivity(), "Please pick a food", Toast.LENGTH_LONG).show();
                }
            }
        }
    };


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try {
            mCallback = (ActivityCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityCommunicator");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


/*
        int activityValue =((MainActivity)context).someIntValue;
        if(activityValue == 0){
            //show one view
        }else{
            // show other view
        }*/


    }
