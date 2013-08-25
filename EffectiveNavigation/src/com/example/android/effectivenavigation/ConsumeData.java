package com.example.android.effectivenavigation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public final class ConsumeData {

	List<String> Messages;
	static List<UtorrentData> UtorrentDataList;

	public ConsumeData(String dataToDeserialize) throws JSONException {

		RefreshData refreshData = new Gson().fromJson(dataToDeserialize,
				RefreshData.class);

		Messages = refreshData.Messages;
		UtorrentDataList = refreshData.UtorrentDataList;

	}

	public ConsumeData() {
		
	}

}
