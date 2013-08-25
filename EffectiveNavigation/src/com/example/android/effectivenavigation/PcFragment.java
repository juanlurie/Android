package com.example.android.effectivenavigation;

import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

public final class PcFragment extends Fragment implements
		Button.OnClickListener, OnLongClickListener {
	public static final String ARG_PLANET_NUMBER = "planet_number";
	public static final String ARG_SECTION_NUMBER = "section_number";

	private TextView mOutput;
	private RabbitConsumeHelper rabbitConsumeHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pcfragment, container, false);
		int i = getArguments().getInt(ARG_PLANET_NUMBER);
		String title = getResources().getStringArray(R.array.environment_array)[i];

		Button plexConnectBtn = (Button) rootView
				.findViewById(R.id.plexConnectButton);
		plexConnectBtn.setOnClickListener(this);
		Button plexBtn = (Button) rootView.findViewById(R.id.plexButton);
		plexBtn.setOnClickListener(this);

		Button shutdown = (Button) rootView.findViewById(R.id.shutdown);
		shutdown.setOnClickListener(this);

		shutdown.setOnLongClickListener(this);

		Button abort = (Button) rootView.findViewById(R.id.abort);
		abort.setOnClickListener(this);

		getActivity().setTitle("New Activity");
		return rootView;
	}

	private int hour;
	private int minute;

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.setTime(new Date());
			newCalendar.set(Calendar.HOUR_OF_DAY, hour);
			newCalendar.set(Calendar.MINUTE, minute);
			newCalendar.set(Calendar.SECOND, 0);

			if (newCalendar.before(calendar)) {
				newCalendar.add(Calendar.DATE, 1);

			}

			try {
				SendViaRabbit(CommandTypes.Shutdown, newCalendar.getTime()
						.toLocaleString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public void SendJson(CommandTypes CommandType, String Parameter) throws JSONException {
		RefreshUtorrentTask refreshUtorrentTask = new RefreshUtorrentTask();
		TransferData transferData = new TransferData();
		transferData.CommandType = CommandType.index();
		transferData.Parameter = Parameter;
		refreshUtorrentTask.execute(transferData.ToJson(), getActivity());
	}

	public void shutdown() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY) + 1 );
        calendar.set(Calendar.MINUTE,0);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);

		TimePickerDialog time = new TimePickerDialog(getActivity(),
				timePickerListener, hour, minute, false);

		time.show();

	}

	public void abort() throws JSONException {
		SendViaRabbit(CommandTypes.Abort, "");
	}

	public void plexConnectClick() throws JSONException {
		SendViaRabbit(CommandTypes.PlexConnect, "");
	}

	public void plexClick() throws JSONException {
		SendViaRabbit(CommandTypes.Plex, "");
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

	public void nextShutdown() throws JSONException {
		SendViaRabbit(CommandTypes.ShutdownSchedule, "");
	}

	@Override
	public void onClick(View v) {
		try {
		switch (v.getId()) {
		case R.id.plexConnectButton:
			plexConnectClick();
			break;
		case R.id.plexButton:
			plexClick();
			break;
		case R.id.shutdown:
			shutdown();
			break;
		case R.id.abort:
		
				abort();
		
			break;

		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {

		case R.id.shutdown:
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE, 1);
			Toast.makeText(getActivity(), "Force Shutdown in 60s", Toast.LENGTH_SHORT).show();

			try {
				SendViaRabbit(CommandTypes.Shutdown, calendar.getTime()
						.toLocaleString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			break;

		}

		return true;
	}
}
