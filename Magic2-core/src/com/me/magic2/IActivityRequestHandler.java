package com.me.magic2;



public interface IActivityRequestHandler {

	public void showAdd(int id, boolean show);
	
	public void rate();
	
	public void showInterstitial();
	
	public void checkInternetConnection(ConnectionStatusCallback callback);
	
	public static interface ConnectionStatusCallback{
		
		void online();
		
		void offline();
	}

}
