package com.example.tcpclient;

import org.json.JSONObject;

public class TransferData {
	public String Message = "";
	public int CommandType;
	public String Parameter;

	public TransferData(String dataToDeserialize) {
		JSONObject jsonObject = new JSONObject(dataToDeserialize);
		CommandType = (Integer) jsonObject.get("CommandType");
		Parameter = jsonObject.get("Parameter").toString();
		Message = jsonObject.get("Message").toString();
	}

	public TransferData() {

	}

	public String ToJson() {
		return new JSONObject().put("CommandType", CommandType)
				.put("Parameter", Parameter).toString();
	}

}
