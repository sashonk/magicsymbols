package com.me.magic2;

import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	private MagicMain _game;
	SoundManager(MagicMain game){
		_game = game;
	}
	
	public void play(String sound){
		
		Settings s = _game.getSettings();
		
		if(s.isSoundEnabled()){
			Sound snd = _game.getManager().getSound(sound);
			snd.play(s.getSoundVolume());
		}
	} 
}
