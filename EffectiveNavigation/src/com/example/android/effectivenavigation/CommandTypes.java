package com.example.android.effectivenavigation;

public enum CommandTypes {
	Plex(1), Shutdown(2), Abort(3), PlexConnect(4), ShutdownSchedule(5),Utorrent(6);

	private final int index;

	CommandTypes(int index) {
		this.index = index;
	}

	public int index() {
		return index;
	}
}