package com.example.android.effectivenavigation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtorrentCommandTask extends AsyncTask<Object, Object, Boolean> {

    public Activity Context;
    List<Map<String, String>> planetsList = new ArrayList<Map<String, String>>();
    private ListView lv;

    private Boolean runCommand(Object... commands) {
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
            clientSocket.close();
            return true;
        } catch (Exception ex) {
            TransferData transferData = new TransferData();
            transferData.Message = ex.getMessage();
            return false;
        }

    }

    protected void onPostExecute(TransferData transferData) {


    }

    @Override
    protected Boolean doInBackground(Object... params) {
        Context = (Activity) params[1];

        String json = new Gson().toJson(params[0]);

        runCommand(json);


        return true;
    }



}