package com.example.android.effectivenavigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;

import java.util.HashMap;

public class uTorrent extends Fragment implements
        Button.OnClickListener, View.OnLongClickListener {
    public static SimpleAdapter simpleAdpt;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.utorrent, container, false);
        lv = (ListView) rootView.findViewById(R.id.listView);

        final Button buttonAll = (Button) rootView.findViewById(R.id.buttonAll);
        final Button buttonActive = (Button) rootView.findViewById(R.id.buttonActive);
        final Button buttonStartAll = (Button) rootView.findViewById(R.id.buttonStartAll);
        final Button buttonStopAll = (Button) rootView.findViewById(R.id.buttonStopAll);
        final Button buttonRemoveComplete = (Button) rootView.findViewById(R.id.buttonRemoveComplete);
        buttonStartAll.setOnClickListener(this);
        buttonStopAll.setOnClickListener(this);
        buttonRemoveComplete.setOnClickListener(this);
        buttonAll.setOnClickListener(this);
        buttonActive.setOnClickListener(this);

        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view,
                                    int position, long id) {

                // We know the View is a TextView so we can cast it


                Toast.makeText(
                        getActivity(),
                        "Item with id [" + id + "] - Position [" + position
                                + "] - Planet []",
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
        String hash = "";
        for (int i = 0;i <simpleAdpt.getCount();i++ )
        {
            HashMap map = (HashMap) simpleAdpt.getItem(i);
            if( map.get("id").toString() == Integer.toString(itemId))
            {
                hash = map.get("hash").toString();
            }
        }



        try {
            SendJsonCommand(hash,item.getOrder());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    // We want to create a context Menu when the user long click on an item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        HashMap map = (HashMap) simpleAdpt.getItem(aInfo.position);

        menu.setHeaderTitle("Options for " + map.get("name"));
        menu.add(1,aInfo.position, 1, "Start");
        menu.add(1, aInfo.position, 2, "Stop");
    }

    public void SendJson(CommandTypes CommandType, String Parameter) throws JSONException {
        RefreshUtorrentTask refreshUtorrentTask = new RefreshUtorrentTask();
        TransferData transferData = new TransferData();
        transferData.CommandType = CommandType.index();
        transferData.Parameter = Parameter;
        refreshUtorrentTask.execute(transferData.ToJson(), getActivity());

    }

    public void SendJsonCommand(String hash, int command) throws JSONException {
        UtorrentCommandTask utorrentCommandTask = new UtorrentCommandTask();
        UtorrentCommand utorrentCommand = new UtorrentCommand();
        utorrentCommand.Hash = hash;
        utorrentCommand.command = command;
        utorrentCommandTask.execute(utorrentCommand, getActivity());

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.buttonActive:
                    SendJson(CommandTypes.Utorrent, "1");
                    break;
                case R.id.buttonAll:
                    SendJson(CommandTypes.Utorrent, "2");
                    break;
                case R.id.buttonStartAll:
                    SendJsonCommand("", 3);
                    break;
                case R.id.buttonStopAll:
                    SendJsonCommand("", 4);
                    break;
                case R.id.buttonRemoveComplete:
                    SendJsonCommand("", 5);
                    break;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}