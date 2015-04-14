package com.vic.sodiumtracking;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements Entering.ActivityCommunicator, Setting.ActivityCommunicator{

    int sodAmt;
    static int dailyTargetVal, dailyaccuamt, sodAmttaken, sodAmtAvaible;
    Tracking tracking  = null;
    Setting setting = null;
    Entering entering = null;
    Button trackingbtn = null;
    Button enteringbtn = null;
    Button settingbtn  = null;
    Button confirmbtn;
    Button confirmTargetSodAmtbtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackingbtn = (Button) findViewById(R.id.trackingbtn);
        enteringbtn = (Button) findViewById(R.id.enteringbtn);
        settingbtn = (Button) findViewById(R.id.settingbtn);

        trackingbtn.setOnClickListener(btnOnClickListener);
        enteringbtn.setOnClickListener(btnOnClickListener);
        settingbtn.setOnClickListener(btnOnClickListener);

        //To test whether the system is re-creating the activity,
        //and check whether the Bundle argument passed to your activity’s onCreate() is null.
        if(savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Tracking tracking = new Tracking();
            ft.add(R.id.myFragment, tracking);
            ft.addToBackStack(null);
            tracking.setDailyTarget(1);
            tracking.setConsumedsofar(1);
            ft.commit();
        }
        else{
            if(dailyTargetVal > 0 && sodAmttaken >0) {
                Tracking obj = (Tracking)getFragmentManager().findFragmentById(R.id.trackingfrag);
                obj.setDailyTarget(dailyTargetVal);
                obj.setConsumedsofar(sodAmttaken);
            }
        }
    }

    Button.OnClickListener btnOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == trackingbtn) {
                Tracking tracking1 = new Tracking();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.myFragment, tracking1);
                ft.addToBackStack(null);

               if(dailyTargetVal > 0 && sodAmttaken >0) {
                   //Tracking obj1 = (Tracking)getFragmentManager().findFragmentById(R.id.trackingfrag);
                   tracking1.setDailyTarget(dailyTargetVal);
                   tracking1.setConsumedsofar(sodAmttaken);
                }else{
                   tracking1.setDailyTarget(1);
                   tracking1.setConsumedsofar(1);
                }
                ft.commit();

            }
            else if(v == enteringbtn){
                Fragment entering = new Entering();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.myFragment, entering);
                ft.addToBackStack(null);
                ft.commit();
            }
            else if (v == settingbtn) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                setting = new Setting();

                if(setting.isAdded()){
                    Toast.makeText(getApplication(), "showing setting fragment", Toast.LENGTH_LONG).show();
                    ft.show(setting);
                }
                else{
                    ft.replace(R.id.myFragment, setting);
                    ft.addToBackStack(null);
                }
                ft.commit();
            }
            else if(v == confirmTargetSodAmtbtn){
                Bundle bundle=new Bundle();
                bundle.putInt("dailyTargetVal", dailyTargetVal);
                tracking.setArguments(bundle);
            }
            else if(v == confirmbtn){
                Bundle bundle=new Bundle();
                bundle.putInt("sodAmttaken",sodAmttaken);
                tracking.setArguments(bundle);
            }
            else{

            }
        }
    };

    //http://stackoverflow.com/questions/26693754/fragment-addtobackstack-and-popbackstackimmediate-not-working
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void enteringFragToActivity(int sodValue) {
        sodAmt = sodValue;
        sodAmttaken = sodAmttaken + sodAmt;
    }

    @Override
    public void settingFragToActivity(int aiValue) {
        dailyTargetVal = aiValue;
    }
}
