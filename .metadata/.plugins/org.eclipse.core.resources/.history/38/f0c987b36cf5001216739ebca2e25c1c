package com.example.tcpclient;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class AppPreferences extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		SharedPreferences sp = getPreferenceScreen().getSharedPreferences();

		SetDefaultSummary(sp);
	}

	private void SetDefaultSummary(SharedPreferences sp) {

		String[] names = new String[] { "prefHomeIp", "prefHomePort",
				"prefArduinoIp", "prefArduinoPort" };

		for (String name : names) {
			EditTextPreference editTextPref = (EditTextPreference) findPreference(name);
			editTextPref.setSummary(editTextPref.getText());
		}

	}

	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference pref = findPreference(key);
		if (pref instanceof EditTextPreference) {
			EditTextPreference etp = (EditTextPreference) pref;
			pref.setSummary(etp.getText());
		}
	}

}