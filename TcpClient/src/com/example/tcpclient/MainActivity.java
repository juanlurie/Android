package com.example.tcpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void shutdownClick(View view) {
		TcpHelper tcpHelper = new TcpHelper();

		EditText timerValueTextBox = (EditText) findViewById(R.id.timerValue);
		String timerValue = timerValueTextBox.getText().toString();
		if(timerValue.length() == 0)
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

	public class TcpHelper extends AsyncTask<Object, Object, String> {

		private String runCommand(Object command) {

			try {
				String sentence;
				String modifiedSentence;

				Socket clientSocket = new Socket("192.168.1.3", 13000);
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

				Socket clientSocket = new Socket("192.168.1.177", 23);
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
