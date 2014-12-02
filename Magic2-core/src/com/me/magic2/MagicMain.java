package com.me.magic2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class MagicMain extends Game {
	
	L10nManager l10n;
	Settings settings;

	
	public Settings getSettings(){
		return settings;
	}


	
	public L10nManager getL10n(){
		return l10n;
	}
	
	boolean inited;
	IActivityRequestHandler natiff;
	 boolean error = false;
	
	public IActivityRequestHandler getNative(){
		return natiff;
	}
	
	public MagicMain(IActivityRequestHandler platform){
		this.natiff = platform;
	}
	
	public static final boolean devmode = false;
	public static final boolean positionMenuButtons = false;
	public static final boolean disableLevel = false;
	public static final boolean lockExtras = true;
	  	
	GameScreen game2Screen;


	public GameScreen getGameScreen() {
		return game2Screen;
	}

	

	
	ResourcesManager manager ;
	
	void setManager(ResourcesManager m){
		manager = m;
	}
	
	public ResourcesManager getManager(){
		return manager;
	}
	
	
	private SoundManager soundManager ;
	public SoundManager getSoundManager(){
		return soundManager;
	}
	
	void clearPrefs(){
		Preferences p = Gdx.app.getPreferences(getClass().getName());
		p.clear();
	}

	@Override
	public void create() {
		
		//clearPrefs();
			
		Gdx.app.log(MagicMain.class.getName(), "game started");		
		l10n = new L10nManager("en", "ru");
		manager = new ResourcesManager();
		manager.loadResources();
		manager.finishLoading();
		manager.init();
		

		settings = new Settings(this);
		settings.read();
		
		soundManager = new SoundManager(this);


		l10n.setLanguage(settings.getLanguage());
		
		//l10n.setLanguage("en");

		game2Screen = new GameScreen(this);
		setScreen(game2Screen);
	}


	@Override
	public void dispose () {
		if (getScreen() != null) getScreen().hide();
		
		game2Screen.dispose();

		
		if(manager!=null){
			manager.dispose();
		}
	}
	




	@Override
	public void pause () {
		if (getScreen() != null) getScreen().pause();
		

	}

	

}
