package com.example.tcpclient;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class PcFragment extends Fragment implements
		CompoundButton.OnCheckedChangeListener, Button.OnClickListener {
	public static final String ARG_PLANET_NUMBER = "planet_number";

	public PcFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pcfragment, container, false);
		int i = getArguments().getInt(ARG_PLANET_NUMBER);
		String title = getResources().getStringArray(R.array.environment_array)[i];

		ToggleButton s = (ToggleButton) rootView.findViewById(R.id.switch1);
		if (s != null) {
			s.setOnCheckedChangeListener(this);
		}

		Button plexConnectBtn = (Button) rootView
				.findViewById(R.id.plexConnectButton);
		plexConnectBtn.setOnClickListener(this);
		Button plexBtn = (Button) rootView.findViewById(R.id.plexButton);
		plexBtn.setOnClickListener(this);

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

			SendJson(CommandTypes.Shutdown, newCalendar.getTime().toLocaleString());
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
		SendJson(CommandTypes.Abort, "");
	}

	public void plexConnectClick() {
		SendJson(CommandTypes.PlexConnect, "");
	}

	public void plexClick() {
		SendJson(CommandTypes.Plex, "");
	}

	public void SendJson(CommandTypes CommandType, String Parameter) {
		TcpHelper tcpHelper = new TcpHelper();
		TransferData transferData = new TransferData();
		transferData.CommandType = CommandType.index();
		transferData.Parameter = Parameter;
		tcpHelper.Run(transferData.ToJson(), getActivity());
	}

	public void nextShutdown() {
		SendJson(CommandTypes.ShutdownSchedule, "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.plexConnectButton:
			plexConnectClick();
			break;
		case R.id.plexButton:
			plexClick();
			break;

		}
	}
}
