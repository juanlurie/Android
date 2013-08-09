package com.example.tcpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements
		CompoundButton.OnCheckedChangeListener {
	public static final String PREFS_NAME = "MySettings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ToggleButton s = (ToggleButton) findViewById(R.id.switch1);
		if (s != null) {
			s.setOnCheckedChangeListener(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, "Show current settings");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = new Intent(this, AppPreferences.class);
		startActivityForResult(i, 0);

		return (true);
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

	static final int TIME_DIALOG_ID = 999;

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);

		switch (id) {

		case TIME_DIALOG_ID:

			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);

		}
		return null;
	}

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
		showDialog(TIME_DIALOG_ID);
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

	public void arduinoClick(View view) {
		ArduinoTcpHelper tcpHelper = new ArduinoTcpHelper();
		tcpHelper.execute("0");

	}

	public void ShowToast(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.show();
	}

	public class TcpHelper extends AsyncTask<Object, Object, String> {

		private String runCommand(Object command) {

			try {
				String sentence;
				String modifiedSentence;

				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);

				String homeIp = settings.getString("prefHomeIp", "192.168.1.4");
				String homePort = settings.getString("prefHomePort", "13001");

				Socket clientSocket = new Socket(homeIp,
						Integer.parseInt(homePort));
				DataOutputStream outToServer = new DataOutputStream(
						clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				sentence = command.toString();
				outToServer.writeBytes(sentence + '\n');
				modifiedSentence = inFromServer.readLine();

				clientSocket.close();
				return modifiedSentence;
			} catch (Exception ex) {
				return "Error";
			}

		}

		protected void onPostExecute(String result) {
			Toast toast = Toast.makeText(MainActivity.this, result,
					Toast.LENGTH_LONG);
			toast.show();

			if (result.toLowerCase().contains("shutdown")) {
				TextView t = (TextView) findViewById(R.id.textView1);
				t.setText(result);
			}
		}

		protected String doInBackground(Object... params) {
			return runCommand(params[0]);

		}
	}

	public class ArduinoTcpHelper extends AsyncTask<Object, Object, String> {

		private String runCommand(Object command) {

			try {
				String sentence;
				String modifiedSentence;

				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);

				String arduinoIp = settings.getString("prefArduinoIp",
						"192.168.1.177");
				String arduinoPort = settings
						.getString("prefArduinoPort", "23");

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
			Toast toast = Toast.makeText(MainActivity.this, result,
					Toast.LENGTH_LONG);
			toast.show();
		}

		protected String doInBackground(Object... params) {
			return runCommand(params[0]);

		}
	}

}
