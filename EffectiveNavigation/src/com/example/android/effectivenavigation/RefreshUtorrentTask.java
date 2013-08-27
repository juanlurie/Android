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

public class RefreshUtorrentTask extends AsyncTask<Object, Object, TransferData> {

    public Activity Context;
    List<Map<String, String>> utorrentList = new ArrayList<Map<String, String>>();
    private ListView lv;

    private TransferData runCommand(Object... commands) {
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
            modifiedSentence = inFromServer.readLine();

            clientSocket.close();
            return deserializeReceived(modifiedSentence);
        } catch (Exception ex) {
            TransferData transferData = new TransferData();
            transferData.Message = ex.getMessage();
            return transferData;
        }

    }

    protected TransferData deserializeReceived(String data) throws JSONException {
        TransferData transferData = new Gson().fromJson(data,
                TransferData.class);
        return transferData;

    }

    protected void onPostExecute(TransferData transferData) {
        lv = (ListView) Context.findViewById(R.id.listView);
        for (UtorrentData item : transferData.UtorrentDataList) {
            utorrentList.add(createPlanet(item));
        }
        uTorrent.simpleAdpt = new SimpleAdapter(Context, utorrentList,
                R.layout.utorrentview, new String[]{"name", "progress", "speed", "remaining", "eta"},
                new int[]{R.id.name, R.id.progress, R.id.speed, R.id.remaining, R.id.eta});
        lv.setAdapter(uTorrent.simpleAdpt);

    }

    @Override
    protected TransferData doInBackground(Object... params) {
        Context = (Activity) params[1];
        final TransferData transferData = runCommand(params);


        return transferData;
    }

    private HashMap<String, String> createPlanet(UtorrentData data) {
        HashMap<String, String> planet = new HashMap<String, String>();
        planet.put("id", data.Id);
        planet.put("hash", data.Hash);
        planet.put("name", data.Name);
        planet.put("progress", data.Progress);
        planet.put("speed", data.Speed);
        planet.put("eta", data.Remaining);
        planet.put("remaining", data.Eta);
        return planet;
    }

}