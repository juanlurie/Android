package com.example.android.effectivenavigation;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TransferData {
	public String Message = "";
	public int CommandType;
	public String Parameter;
    public List<UtorrentData> UtorrentDataList;


	public TransferData() {

	}

	public String ToJson() throws JSONException {
		return new JSONObject().put("CommandType", CommandType)
				.put("Parameter", Parameter).toString();
	}

}
