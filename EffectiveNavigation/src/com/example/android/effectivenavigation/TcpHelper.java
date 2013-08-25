package com.example.android.effectivenavigation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

public class TcpHelper extends AsyncTask<Object, Object, String> {

	public Activity Context;

	private TransferData runCommand(Object... commands) {
		String command = commands[0].toString();

		try {
			String sentence;
			String modifiedSentence = "";

			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(Context);

			String arduinoIp = settings
					.getString("prefHomeIp", "192.168.1.3");
			String arduinoPort = settings.getString("prefHomePort", "13000");

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
			return deserializeReceived(modifiedSentence);
		} catch (Exception ex) {
			TransferData transferData = new TransferData();
			transferData.Message = ex.getMessage();
			return transferData;
		}

	}

	protected TransferData deserializeReceived(String data) throws JSONException {
		return new TransferData(data);
	}

	protected void onPostExecute(String result) {
		Toast.makeText((Activity) Context, result, Toast.LENGTH_SHORT).show();
	}

	public void Run(String command, Activity context) {
		execute(command, context);
	}

	@Override
	protected String doInBackground(Object... params) {
		Context = (Activity) params[1];
		final TransferData transferData = runCommand(params);

		switch (transferData.CommandType) {
		case 1:
			break;
		case 4:
			break;
		case 3:
			break;
		case 2:
			break;
		
		default:
			break;
		}

		return transferData.Message;
	}

}