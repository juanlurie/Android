package com.example.android.effectivenavigation;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public final class SettingsFragment extends Fragment implements
		Button.OnClickListener {

	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.settings, container, false);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		String homeIp = settings.getString("prefHomeIp", "192.168.1.3");
		String homePort = settings.getString("prefHomePort", "13000");

		String arduinoIp = settings.getString("prefArduinoIp", "192.168.1.177");
		String arduinoPort = settings.getString("prefArduinoPort", "23");

		EditText homeip = (EditText) rootView.findViewById(R.id.homeIp);
		homeip.setText(homeIp);

		EditText homeport = (EditText) rootView.findViewById(R.id.homePort);

		EditText arduinoip = (EditText) rootView.findViewById(R.id.arduinoIp);

		EditText arduinoport = (EditText) rootView
				.findViewById(R.id.arduinoPort);

		homeip.setText(homeIp);
		homeport.setText(homePort);
		arduinoip.setText(arduinoIp);
		arduinoport.setText(arduinoPort);

		Button plexConnectBtn = (Button) rootView
				.findViewById(R.id.saveSettings);
		plexConnectBtn.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {

		EditText homeip = (EditText) getActivity().findViewById(R.id.homeIp);

		EditText homeport = (EditText) getActivity()
				.findViewById(R.id.homePort);

		EditText arduinoip = (EditText) getActivity().findViewById(
				R.id.arduinoIp);

		EditText arduinoport = (EditText) getActivity().findViewById(
				R.id.arduinoPort);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		Editor editor = settings.edit();
		editor.putString("prefHomeIp", homeip.getText().toString());
		editor.putString("prefHomePort", homeport.getText().toString());
		editor.putString("prefArduinoIp", arduinoip.getText().toString());
		editor.putString("prefArduinoPort", arduinoport.getText().toString());

		editor.commit();

		Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
	}

}
