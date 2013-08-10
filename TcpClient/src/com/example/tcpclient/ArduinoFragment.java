package com.example.tcpclient;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArduinoFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";

    public ArduinoFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.arduino, container, false);
        int i = getArguments().getInt(ARG_PLANET_NUMBER);
        String title = getResources().getStringArray(R.array.environment_array)[i];
    
        
        getActivity().setTitle("New Activity");
        return rootView;
    }
}