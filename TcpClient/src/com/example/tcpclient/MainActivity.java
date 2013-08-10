package com.example.tcpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tcpclient.R;
import com.example.tcpclient.MainActivity.DrawerItemClickListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	public static final String PREFS_NAME = "MySettings";

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mMenuItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mMenuItems = getResources().getStringArray(R.array.environment_array);
		// set a custom shadow that overlays the main content when the drawer
		// opens
		// mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
		// GravityCompat.START);
		// set up the drawer's list view with items and click listener
		// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mMenuItems));
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle("Hello");
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Hello");
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return false;

	}

	/* The click listner for ListView in the navigation drawer */
	public class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Bundle args = new Bundle();
		switch (position) {
		case 0:
			Fragment arduinoFragment = new ArduinoFragment();

			args.putInt(PcFragment.ARG_PLANET_NUMBER, position);
			arduinoFragment.setArguments(args);

			FragmentManager arduinoFragmentManager = getFragmentManager();
			arduinoFragmentManager.beginTransaction()
					.replace(R.id.content_frame, arduinoFragment).commit();
			break;

		case 1:
			Fragment pcFragment = new PcFragment();

			args.putInt(PcFragment.ARG_PLANET_NUMBER, position);
			pcFragment.setArguments(args);

			FragmentManager pcFragmentManager = getFragmentManager();
			pcFragmentManager.beginTransaction()
					.replace(R.id.content_frame, pcFragment).commit();
			break;
		case 2:

			Fragment preferenceFragment = new AppPreferences();

			args.putInt(PcFragment.ARG_PLANET_NUMBER, position);
			preferenceFragment.setArguments(args);

			FragmentManager preferenceFragmentManager = getFragmentManager();
			preferenceFragmentManager.beginTransaction()
					.replace(R.id.content_frame, preferenceFragment).commit();

			break;
		default:
			break;
		}

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mMenuItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public void arduinoClick(View view) {
		ArduinoTcpHelper tcpHelper = new ArduinoTcpHelper();
		tcpHelper.execute("0");

	}

	public void ShowToast(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.show();
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
