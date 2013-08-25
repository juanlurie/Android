package com.example.android.effectivenavigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class uTorrent extends Fragment {

	private SimpleAdapter simpleAdpt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.utorrent, container, false);

		ListView lv = (ListView) rootView.findViewById(R.id.listView);

		initList();

		// We get the ListView component from the layout

		// This is a simple adapter that accepts as parameter
		// Context
		// Data list
		// The row layout that is used during the row creation
		// The keys used to retrieve the data
		// The View id used to show the data. The key number and the view id
		// must match
		simpleAdpt = new SimpleAdapter(getActivity(), planetsList,
				R.layout.simplecrap, new String[] { "planet","1" },
				new int[] { R.id.name,R.id.slogan });

		lv.setAdapter(simpleAdpt);

		// React to user clicks on item
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {

				// We know the View is a TextView so we can cast it
				TextView clickedView = (TextView) view;

				Toast.makeText(
						getActivity(),
						"Item with id [" + id + "] - Position [" + position
								+ "] - Planet [" + clickedView.getText() + "]",
						Toast.LENGTH_SHORT).show();

			}
		});
		registerForContextMenu(lv);

		return rootView;
	}

	// This method is called when user selects an Item in the Context menu
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		// Implements our logic
		Toast.makeText(getActivity(), "Item id [" + itemId + "]",
				Toast.LENGTH_SHORT).show();
		return true;
	}

	// We want to create a context Menu when the user long click on an item
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

		// We know that each row in the adapter is a Map
		HashMap map = (HashMap) simpleAdpt.getItem(aInfo.position);

		menu.setHeaderTitle("Options for " + map.get("planet"));
		menu.add(1, 1, 1, "Details");
		menu.add(1, 2, 2, "Delete");

	}

	// The data to show
	List<Map<String, String>> planetsList = new ArrayList<Map<String, String>>();

	private void initList() {
		// We populate the planets
		for (UtorrentData item : ConsumeData.UtorrentDataList) {
			planetsList.add(createPlanet("planet", item.Name));
		}

	}

	private HashMap<String, String> createPlanet(String key, String name) {
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key, name);
		planet.put("1", "12345");
		

		return planet;
	}

}