package com.me.magic2.desktop;

import java.awt.Desktop;
import java.net.URI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.me.magic2.IActivityRequestHandler;
import com.me.magic2.MagicMain;

public class DesktopLauncher implements IActivityRequestHandler{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 400;
		config.height =800;
		new LwjglApplication(new MagicMain(new DesktopLauncher()), config);
	}

	@Override
	public void showAdd(int id, boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rate() {
		if(Desktop.isDesktopSupported())
		{
			try{
				Desktop.getDesktop().browse(new URI("http://www.google.com/"));
			}
			catch(Exception ex){
				System.err.println(ex);
			}
		}
	
	}

	@Override
	public void showInterstitial() {
		// TODO Auto-generated method stub
		System.out.println("SHOWING INTERSTITIAL");
	}

	@Override
	public void checkInternetConnection(OnlineStatusCallback callback) {
		callback.call(true);
		
	}
}
