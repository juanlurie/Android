package com.example.android.effectivenavigation;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.List;

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
