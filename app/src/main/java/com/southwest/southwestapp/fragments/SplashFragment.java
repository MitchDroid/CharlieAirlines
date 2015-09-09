package com.southwest.southwestapp.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.southwest.southwestapp.AppHelper;
import com.southwest.southwestapp.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    private Timer runSplash = new Timer();

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        proceedToMainScreen();
    }


    private void proceedToMainScreen() {
        long delay = 1000;

        TimerTask showSplash = new TimerTask() {
            @Override
            public void run() {
                AppHelper.screenManager.showMainScreenFromSplash(getActivity());
            }
        };

        // Start the timer
        runSplash.schedule(showSplash, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        runSplash.cancel();
    }
}
