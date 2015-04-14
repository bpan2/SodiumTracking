package com.vic.sodiumtracking;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends Fragment {
    Bundle state=null;
    int agebk = 0;
    int aibk = 0;
    int ulbk = 0;

    SharedPreferences settingLastState = null;

    String str=null;
    static int age =0;
    static int ai=0;
    static int ul=0;

    EditText ageField=null;
    TextView aiField=null;
    TextView ulField=null;
    Button confirmTargetSodAmtbtn, getSodAmtbtn;

    ActivityCommunicator mCallback;

    public interface ActivityCommunicator {
        public void settingFragToActivity(int someValue);
    }

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

    // Required empty public constructor
    public Setting() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_setting, container, false);
        ageField = (EditText) v.findViewById(R.id.ageField);
        aiField  = (TextView) v.findViewById(R.id.aiField);
        ulField  = (TextView) v.findViewById(R.id.ulField);
        confirmTargetSodAmtbtn = (Button) v.findViewById(R.id.confirmTargetSodAmtbtn);
        getSodAmtbtn = (Button) v.findViewById(R.id.getSodAmtbtn);

        confirmTargetSodAmtbtn.setOnClickListener(btnOnClickListener);
        getSodAmtbtn.setOnClickListener(btnOnClickListener);

        if(settingLastState != null) {
            Toast.makeText(getActivity(), "Last state is NOT empty", Toast.LENGTH_LONG).show();
        }

        loadLastState();
        return v;
    }

    private void saveLastState(){
        settingLastState = this.getActivity().getSharedPreferences("laststate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settingLastState.edit();

        String s1 = ageField.getText().toString();
        agebk = Integer.parseInt(s1);
        aibk = Integer.parseInt(aiField.getText().toString());
        ulbk = Integer.parseInt(ulField.getText().toString());
        editor.putInt("agebk", agebk);
        editor.putInt("aibk", aibk);
        editor.putInt("ulbk", ulbk);

        editor.commit();
    }

    private void loadLastState() {
        settingLastState = this.getActivity().getSharedPreferences("laststate", Context.MODE_PRIVATE);

        int ag = settingLastState.getInt("agebk", agebk);
        int a  = settingLastState.getInt("aibk", aibk);
        int u  = settingLastState.getInt("ulbk", ulbk);

        age = ag;
        ai = a;
        ul = u;

        ageField.setText("" + ag);
        aiField.setText("" + a);
        ulField.setText("" + u);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveLastState();
    }

    @Override
    public void onPause() {
        super.onPause();
/*
        state = new Bundle();
        String s1 = ageField.getText().toString();
        agebk = Integer.parseInt(s1);
        aibk = Integer.parseInt(aiField.getText().toString());
        ulbk = Integer.parseInt(ulField.getText().toString());
        state.putInt("agebk", agebk);
        state.putInt("aibk", aibk);
        state.putInt("ulbk", ulbk);*/
    }

   /* private Bundle saveState() { *//* called either from onDestroyView() or onSaveInstanceState() *//*
        state.putCharSequence(App.VSTUP, vstup.getText());
        return state;
    }*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

   //the following two work only when orientation is changed: vertical vs horizontal
   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("age", age);
        outState.putInt("ai", ai);
        outState.putInt("ul", ul);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            age = savedInstanceState.getInt("age", 0);
            ai = savedInstanceState.getInt("ai", 0);
            ul = savedInstanceState.getInt("ul", 0);
        }
    }*/


    Button.OnClickListener btnOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == getSodAmtbtn) {
                str = ageField.getText().toString();

                if(str.length() > 0) {
                    age = Integer.parseInt(ageField.getText().toString());
                    if (age < 0.5)                 { ai = 120;  ul = 120; }
                    if ((age > 0.5) && (age <= 1)) { ai = 370;  ul = 370; }
                    if ((age > 1) && (age <= 3))   { ai = 1000; ul = 1500;}
                    if ((age > 4) && (age <= 9))   { ai = 1200; ul = 1900;}
                    if ((age > 9) && (age <= 13))  { ai = 1500; ul = 2200;}
                    if ((age > 13) && (age <= 51)) { ai = 1500; ul = 2300;}
                    if ((age > 51) && (age <= 70)) { ai = 1300; ul = 2300;}
                    if (age > 70)                  { ai = 1200; ul = 2300;}

                    agebk = age;
                    aibk = ai;
                    ulbk = ul;
                    aiField.setText("" + ai);
                    ulField.setText("" + ul);
                }
                else{
                    Toast.makeText(getActivity(), "Please enter the age ! ", Toast.LENGTH_LONG).show();
                }

            }

            if (v == confirmTargetSodAmtbtn) {
                if (aiField == null) {
                    Toast.makeText(getActivity(), " Adequate Daily Intake is empty ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity()," The value you picked is: " + ai, Toast.LENGTH_LONG ).show();
                    mCallback.settingFragToActivity(ai);
                }
            }
        }
    };

 /*
    @Override
    public void onPause() {
        super.onPause();
        getFragmentManager().findFragmentById(R.id.settingfrag).setRetainInstance(true);
    }

   @Override
    public void onResume() {
        super.onResume();
        getFragmentManager().findFragmentById(R.id.settingfrag).getRetainInstance();
    }*/


}
