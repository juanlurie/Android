package com.example.android.effectivenavigation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitHelper extends AsyncTask<Object, Object, String> {
	public Activity Context;

	@Override
	protected String doInBackground(Object... params) {
		Context = (Activity) params[2];
		String command = params[1].toString();

		try {

			String sentence;
			String modifiedSentence = "";

			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(Context);

			String ip = settings.getString("prefHomeIp", "192.168.1.3");

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ip);
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare("commands", false, false, false, null);
			String message = command;
			channel.basicPublish("", "commands", null, message.getBytes());

			channel.close();
			connection.close();

		} catch (Exception ex) {

		}
		return "";
	}

}