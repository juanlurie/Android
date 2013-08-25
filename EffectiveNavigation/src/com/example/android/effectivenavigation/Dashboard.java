package com.example.android.effectivenavigation;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Dashboard extends Fragment {
	
	public void nextShutdown() throws JSONException {
		SendViaRabbit(CommandTypes.ShutdownSchedule, "");
	}

	public void SendViaRabbit(CommandTypes CommandType, String Parameter) throws JSONException {
		RabbitHelper rabbitHelper = new RabbitHelper();
		TransferData transferData = new TransferData();
		transferData.CommandType = CommandType.index();
		transferData.Parameter = Parameter;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			rabbitHelper.execute(AsyncTask.THREAD_POOL_EXECUTOR,
					transferData.ToJson(), getActivity());
		} else {
			rabbitHelper.execute(transferData.ToJson(), getActivity());
		}

	}


	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.dashboard, container, false);
		
		rootView.findViewById(R.id.refreshDashboard).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						switch (view.getId()) {
						case R.id.refreshDashboard:
							try {
								nextShutdown();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}
				});
		
		

		return rootView;
	}
}