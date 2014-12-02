package com.me.magic2;



public interface IActivityRequestHandler {

	public void showAdd(int id, boolean show);
	
	public void rate();
	
	public void showInterstitial();
	
	public void checkInternetConnection(OnlineStatusCallback callback);
	
	public static interface OnlineStatusCallback{
		public void call(boolean online);
	}

}
