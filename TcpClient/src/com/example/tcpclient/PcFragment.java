package com.example.tcpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PcFragment extends Fragment implements
		CompoundButton.OnCheckedChangeListener {
	public static final String ARG_PLANET_NUMBER = "planet_number";

	public PcFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_planet, container,
				false);
		int i = getArguments().getInt(ARG_PLANET_NUMBER);
		String title = getResources().getStringArray(R.array.environment_array)[i];

		ToggleButton s = (ToggleButton) rootView.findViewById(R.id.switch1);
		if (s != null) {
			s.setOnCheckedChangeListener(this);
		}

		getActivity().setTitle("New Activity");
		return rootView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked)
			shutdown();
		else
			abort();

	}

	private int hour;
	private int minute;

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			TcpHelper tcpHelper = new TcpHelper();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.setTime(new Date());
			newCalendar.set(Calendar.HOUR_OF_DAY, hour);
			newCalendar.set(Calendar.MINUTE, minute);

			if (newCalendar.before(calendar)) {
				newCalendar.add(Calendar.DATE, 1);

			}

			SendJson("2", newCalendar.getTime().toLocaleString());
		}
	};

	public void shutdown() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);

		TimePickerDialog time = new TimePickerDialog(getActivity(),
				timePickerListener, hour, minute, false);

		time.show();

	}

	public void abort() {
		SendJson("3", "");
	}

	public void plexConnectClick(View view) {
		SendJson("4", "");
	}

	public void plexClick(View view) {
		SendJson("1", "");
	}

	public void SendJson(String CommandType, String Parameter) {
		TcpHelper tcpHelper = new TcpHelper();
		String myString = new JSONObject().put("CommandType", CommandType)
				.put("Parameter", Parameter).toString();
		tcpHelper.execute(myString);
	}

	public void nextShutdown() {
		SendJson("5", "");
	}

	public class TcpHelper extends AsyncTask<Object, Object, String> {

		private String runCommand(Object command) {

			try {
				String sentence;
				String modifiedSentence = "";

				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(getActivity());

				String arduinoIp = settings.getString("prefHomeIp",
						"192.168.1.177");
				String arduinoPort = settings.getString("prefHomePort", "23");

				Socket clientSocket = new Socket(arduinoIp,
						Integer.parseInt(arduinoPort));
				DataOutputStream outToServer = new DataOutputStream(
						clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				sentence = command.toString();
				outToServer.writeBytes(sentence);
				modifiedSentence = inFromServer.readLine();

				clientSocket.close();
				return modifiedSentence;
			} catch (Exception ex) {
				return "Error";
			}

		}

		protected void onPostExecute(String result) {
			Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		}

		protected String doInBackground(Object... params) {
			return runCommand(params[0]);

		}
	}

}