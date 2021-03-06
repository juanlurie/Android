package com.example.android.effectivenavigation;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class RabbitConsumeHelper extends AsyncTask<Object, Object, String> {
	public Activity Context;
	private Channel channel;

	@Override
	protected String doInBackground(Object... params) {
		Context = (Activity) params[1];
		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("192.168.1.3");
			Connection connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare("helloWorld", false, false, false, null);
		
			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume("helloWorld", true, consumer);

			while (!isCancelled()) {
				try {
					QueueingConsumer.Delivery delivery = consumer
							.nextDelivery(5000);
					final String message = new String(delivery.getBody());

					final ConsumeData data = new ConsumeData(message);

					Context.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							TextView mOutput = (TextView) Context
									.findViewById(R.id.output);
							
							mOutput.setMovementMethod(new ScrollingMovementMethod());

							for (String item : data.Messages) {
								mOutput.setText(item + "\n" + mOutput.getText());
							}

						}
					});

					System.out.println(" [x] Received '" + message + "'");
				} catch (Exception ex) {

				}
			}

		} catch (Exception ex) {

		}

		return "";
	}

	@Override
	protected void onCancelled() {

		try {
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cancel(true);
		super.onCancelled();
	}

}