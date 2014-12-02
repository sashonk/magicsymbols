package com.me.magic2;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

public class Settings {
	private Defaults defaults;
	
	MagicMain _game;
	public Settings(MagicMain game){
		_game = game;
		defaults = new Defaults();
	}
	
	public Defaults defaults(){
		return defaults;
	}

	public void toDefaults(){
		setSoundEnabled(defaults.isSoundEnabled());
		setSoundVolume(defaults.getSoundVolume());
		setLanguage(defaults.getLanguage());

	}
	
	public void write(){
		Preferences prefs = Gdx.app.getPreferences(_game.getClass().getName());
		prefs.putBoolean("soundEnabled", soundEnabled);
		prefs.putFloat("soundVolume", soundVolume);
		prefs.putString("language", language);
		prefs.flush();
	}


	public void read(){
		Preferences prefs = Gdx.app.getPreferences(_game.getClass().getName());
		soundEnabled = prefs.getBoolean("soundEnabled", defaults.isSoundEnabled());
		soundVolume = prefs.getFloat("soundVolume", defaults.getSoundVolume());
		language = prefs.getString("language", defaults.getLanguage());
	}




	public boolean isSoundEnabled() {
		return soundEnabled;
	}

	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}



	boolean soundEnabled;
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	float soundVolume;
	
	
	String language;

	
	public  class Defaults{

		public String getLanguage() {
			return Locale.getDefault().getLanguage();
		}
		
		public float getMusicVolume() {
			return 0.5f;
		}
		
		public float getSoundVolume() {
			return 0.5f;
		}
		
		public boolean isMusicEnabled() {
			return true;
		}

		public boolean isSoundEnabled() {
			return true;
		}
	}
}
