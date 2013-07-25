package com.example.tcpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String PREFS_NAME = "MySettings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

	public void shutdownClick(View view) {
		TcpHelper tcpHelper = new TcpHelper();

		EditText timerValueTextBox = (EditText) findViewById(R.id.timerValue);
		String timerValue = timerValueTextBox.getText().toString();
		if (timerValue.length() == 0)
			timerValue = "3600";
		tcpHelper.execute("1 " + timerValue);
	}

	public void abortClick(View view) {
		TcpHelper tcpHelper = new TcpHelper();
		tcpHelper.execute("2");
	}

	public void plexConnectClick(View view) {
		TcpHelper tcpHelper = new TcpHelper();
		tcpHelper.execute("3");
	}

	public void plexClick(View view) {
		TcpHelper tcpHelper = new TcpHelper();
		tcpHelper.execute("4");
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
