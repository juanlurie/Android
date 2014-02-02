package com.example.android.effectivenavigation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONException;

public class utorrentSettingsDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    public boolean haveInternet(Context ctx) {
        ConnectivityManager info = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo wifi = info.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi.isAvailable())
            return true;

        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .create();
        alert.setTitle("Error");
        alert.setMessage("No Wifi Connected!");
        alert.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // closing the application
                getActivity().finish();
            }
        });
        alert.show();

        return false;
    }

    public void SendJsonCommand(String hash, int command) throws JSONException {
        if (haveInternet(getActivity())) {
            UtorrentCommandTask utorrentCommandTask = new UtorrentCommandTask();
            UtorrentCommand utorrentCommand = new UtorrentCommand();
            utorrentCommand.Hash = hash;
            utorrentCommand.command = command;
            utorrentCommandTask.execute(utorrentCommand, getActivity());
        } else {
            Toast.makeText(getActivity(), "Not connected To Wifi", Toast.LENGTH_SHORT).show();
        }
    }

    int downloadLimit = 0;
    int uploadLimit = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utorrentsettings, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setMessage("Utorrent Settings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {


                            SendJsonCommand(String.valueOf(downloadLimit) + "," + String.valueOf(uploadLimit), 6);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();


        SeekBar downloadSeekBar = (SeekBar) view.findViewById(R.id.downloadLimit);
        downloadSeekBar.setOnSeekBarChangeListener(this);

        SeekBar uploadSeekBar = (SeekBar) view.findViewById(R.id.uploadLimit);
        uploadSeekBar.setOnSeekBarChangeListener(this);

        return dialog;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(i == 0)
            i = 1;
        switch (seekBar.getId())
        {
            case R.id.downloadLimit:downloadLimit = i;
            case R.id.uploadLimit:uploadLimit = i;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}