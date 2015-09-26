package com.me.magic2;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.magic2.IActivityRequestHandler.OnlineStatusCallback;



public class GameScreen extends BaseScreen{

	boolean gestureEnabled;
	boolean created;
	GestureDetector detector;
	
	InputListener onButtonExit(){
		return new InputListener(){
			public boolean keyDown (InputEvent event, int keycode) {
				
				if(keycode == Keys.BACK  || keycode==Keys.ESCAPE){
					
					Actor rd = getStage().getRoot().findActor("rate_dialog");
					if(rd!=null && rd.isVisible()){
						return true;
					}
					
					
					final com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences(getGame().getClass().getName());
					final boolean rated = prefs.getBoolean(Values.rated, false);
					final int exitClickCount = prefs.getInteger(Values.exit_click_count, 0);
					final boolean played = prefs.getBoolean(Values.played, false);
					
					
					if(!rated && played && ( exitClickCount%3==0) && created){
						getGame().getNative().checkInternetConnection(new OnlineStatusCallback() {
							
							@Override
							public void call(final boolean online) {
								
								Gdx.app.postRunnable(new Runnable() {
									
									@Override
									public void run() {
										if(online){
												final Dialog dialog = new Dialog("", getGame().getManager().getSkin(), "default");
												dialog.setName("rate_dialog");
												dialog.getButtonTable().defaults().pad(20).padTop(0);
												dialog.getContentTable().defaults().align(Align.center).pad(10);
												dialog.setClip(false);
												dialog.setBackground("dialog");
												dialog.setModal(true);
												dialog.setMovable(false);
												Label label = new L10nLabel(getGame().getL10n(), "do_you_mind_rate", getGame().getManager().getSkin(), "tiny");
												label.setAlignment(Align.center);
												label.setWrap(true);
												dialog.getContentTable().add(label).width(200);
												
												Button yes = new L10nButton(getGame().getL10n(), "yes_rate", getGame().getManager().getSkin(), "small");
												Button no = new L10nButton(getGame().getL10n(), "no_rate", getGame().getManager().getSkin(), "small");
												dialog.getButtonTable().add(yes).padRight(30);
												dialog.getButtonTable().add(no);
												no.addListener(new ClickListener(){
													public void clicked (InputEvent event, float x, float y) {																			
														Gdx.app.exit();
													}
												});
												
												yes.addListener(new ClickListener(){
													public void clicked (InputEvent event, float x, float y) {
														getGame().getNative().rate();
														dialog.hide();
														prefs.putBoolean(Values.rated, true);
														prefs.flush();
													}
												});									
												dialog.show(getStage());														

										}
										else{
											Gdx.app.exit();
										}
										
									}
								});
								
								
		
								
							}
						});
					}
					else{
						Gdx.app.exit();
					}
					
					
					if(played){
						prefs.putInteger(Values.exit_click_count, exitClickCount+1);
						prefs.flush();
					}

					

				}
				
				return false;
			}
		};
	}
	
	private  void constrainPosition(OrthographicCamera cam, Stage s){
		
		float vw = cam.viewportWidth*.5f*cam.zoom;
		float vh = cam.viewportHeight*.5f*cam.zoom;
		
		if(cam.position.x - vw<0){
			cam.position.x = vw;
		}
		if(cam.position.y - vh< 0){
			cam.position.y = vh;
		}
		if(cam.position.x + vw > s.getWidth()){
			cam.position.x = s.getWidth() - vw;
		}
		if(cam.position.y + vh > s.getHeight()){
			cam.position.y = s.getHeight() - vh;
		}
	}
	
	float Zmin = 0.25f;
	private  void constrainZoom(OrthographicCamera cam){
		if(cam.zoom<Zmin){
			cam.zoom = Zmin;
		}
		if(cam.zoom>1){
			cam.zoom = 1;
		}
	}
	
	public GameScreen(MagicMain rm) {
		super(rm);	
		
		final OrthographicCamera camera = (OrthographicCamera) getStage().getCamera();
		 detector = new GestureDetector(new GestureListener() {
			
			float initialScale = 1;
			
			@Override
			public boolean zoom(float originalDistance, float currentDistance) {
				if(!gestureEnabled ){
					return false;
				}
				
				float ratio = originalDistance / currentDistance;
				float zm = initialScale * ratio;
				
				camera.zoom = zm > 1 ? camera.zoom : zm;
				
				constrainPosition(camera, getStage());
				constrainZoom(camera);
				return false;
			}
			
			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				
				
				initialScale = camera.zoom;
				return false;
			}
			
			@Override
			public boolean tap(float x, float y, int count, int button) {
				return false;
			}
			
			@Override
			public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
					Vector2 pointer1, Vector2 pointer2) {
				return false;
			}
			
			@Override
			public boolean panStop(float x, float y, int pointer, int button) {
				return false;
			}
			
			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				if(!gestureEnabled){
					return false;
				}
				
				camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
				constrainPosition(camera, getStage());
				constrainZoom(camera);
				return false;
			}
			
			@Override
			public boolean longPress(float x, float y) {
				return false;
			}
			
			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				return false;
			}
		});
		
		
		Gdx.input.setCatchBackKey(true);
		getStage().setKeyboardFocus(getStage().getRoot());
		getStage().addListener(onButtonExit());
		
	
		
		Skin skin = rm.getManager().getSkin();

		final Table menu = new Table();
		final Button begin = new L10nButton(rm.getL10n(), "begin", skin, "huge");
		menu.add(begin).row();
		final Button lang = new L10nButton(rm.getL10n(), "language", skin, "huge");
		menu.add(lang).row();		
/*		final Label glaga = new Label("abcdefghij", skin, "glaga");
		menu.add(glaga).row();
		final Label glaga2 = new Label("klmnopqrs", skin, "glaga");
		menu.add(glaga2).row();
		final Label glaga3 = new Label("tuqvwxyz", skin, "glaga");
		menu.add(glaga3).row();*/

		
		menu.pack();
		getStage().addActor(menu);
		Util.center(menu);
		
		
		begin.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
			
				
				menu.remove();
				createGame();
			}
		});
		
		lang.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				LangDialog dialog = new LangDialog(getGame());
				dialog.show(getStage());
				
			}
		} );
	
		
		
		//p2z = new Pinch2ZoomListener2();
		//p2z.setZMin(0.25f);
	}
	

	

	static float imgCoef = .08f;
	static float cellPad = 5;
	static float squareSize = 150;
	//Pinch2ZoomListener2 p2z;
	int previosValue ;
	
	
	void listeners(){
		getStage().addListener(onButtonExit());
		//p2z = new Pinch2ZoomListener2();

		//getStage().addListener(p2z);
	}
	
	
	void showInterstitial(){
		getGame().getNative().checkInternetConnection(new OnlineStatusCallback() {
			
			@Override
			public void call(boolean online) {
				if(online && (Gdx.app.getType()!=ApplicationType.Desktop)){
					getGame().getNative().showInterstitial();					
				}
				else{
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {						
							if(adScreen==null){
								adScreen = new AdScreen(getGame(), GameScreen.this);								
							}
							getGame().setScreen(adScreen);
							
						}
					});
				}
				
			}
		});
	}
	
	AdScreen adScreen;
	
	Actor number(int num){
		Skin skin = getGame().getManager().getSkin();
		//Group gp = new Group();
		Label number = new Label(Integer.toString(num), skin, "tiny");
		//gp.addActor(number);
		//gp.setSize(number.getWidth(), number.getHeight());
		//float scale = 0.66f;
		//gp.setScale(scale);
		//gp.setSize(number.getWidth()*scale, number.getHeight()*scale);
		return number;
	}

	void createGame(){
		created = true;
		gestureEnabled = true;
		
		final Map<Integer, String> symbolMap = new HashMap<Integer, String>();
		symbolMap.put(Integer.valueOf(1), "a");
		symbolMap.put(Integer.valueOf(2), "b");
		symbolMap.put(Integer.valueOf(3), "c");
		symbolMap.put(Integer.valueOf(4), "d");
		symbolMap.put(Integer.valueOf(5), "e");
		symbolMap.put(Integer.valueOf(6), "i");
		symbolMap.put(Integer.valueOf(7), "g");
		symbolMap.put(Integer.valueOf(8), "m");
		symbolMap.put(Integer.valueOf(9), "s");
		symbolMap.put(Integer.valueOf(10), "x");
		getStage().clear();
		listeners();
		background();
		
		final Table main = new Table();		
		final Skin skin = getGame().getManager().getSkin();
		
		Table instructions = new Table();
		instructions.pad(0);
		instructions.defaults().align(Align.center);
		
		Label instruction1 = new L10nLabel(getGame().getL10n(), "line1", skin, "small");
		Label instruction2 = new L10nLabel(getGame().getL10n(), "line2", skin, "small");
		Label instruction3 = new L10nLabel(getGame().getL10n(), "line3", skin, "small");
		Label instruction4 = new L10nLabel(getGame().getL10n(), "line4", skin, "small");
		Label instruction5 = new L10nLabel(getGame().getL10n(), "line5", skin, "small");
		Label instruction6 = new L10nLabel(getGame().getL10n(), "line6", skin, "small");
		Label instruction7 = new L10nLabel(getGame().getL10n(), "line7", skin, "small");
		Label instruction8 = new L10nLabel(getGame().getL10n(), "line8", skin, "small");

		instructions.add(instruction1).row();
		instructions.add(instruction2).row();
		instructions.add(instruction3).row();
		instructions.add(instruction4).row();
		instructions.add(instruction5).row();
		instructions.add(instruction6).row();
		instructions.add(instruction7).row();
		instructions.add(instruction8).row();
		
		instructions.pack();		
		main.add(instructions).row();
		Table table = new Table();		
		table.defaults().padTop(20).padLeft(cellPad).padRight(cellPad);				
		Random rnd = new Random(System.currentTimeMillis());
		
		int currentValue = rnd.nextInt(10) +1;
		while(currentValue==previosValue){
			currentValue = rnd.nextInt(10) +1;
		}
		previosValue= currentValue;		
		final int valueOfInterest = currentValue;						
		previosValue = valueOfInterest;
		
		int height = 7;
		int width = 15;
		int maxIndex = 99;
		outer: for(int i = 0; i < height ; i++){
			for(int j = 0; j < width ; j++){
				int index = j+i*width;
				if(index > maxIndex) {
					break outer;
				}
				
				int value = rnd.nextInt(10) +1;
				
				// HERE IS THE SQUARE MAGIC!!
				if(index%9==0 && index >= 9){
					value = valueOfInterest;
				}				
				
				Table cell = new Table();	
				String symbol = symbolMap.get(value);
				Label glag = new Label(symbol, skin, "glaga_mini");
				cell.add(glag).row();
				Actor number = number(index);
				cell.add(number);				
				cell.pack();				
				table.add(cell);
			}
			
			table.row();
		}
		
	
		table.pack();
		main.add(table).row();
		

		

		Image blackSquare = new Image(getGame().getManager().getAtlas().findRegion("magic"));
		blackSquare.setSize(squareSize, squareSize);
		main.add(blackSquare).padTop(20).width(squareSize).height(squareSize);
		

		
		main.pack();
		getStage().addActor(main);
		Util.center(main);
		
		main.getColor().a = 0;
		main.addAction(Actions.fadeIn(1));
		
		final com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences(getGame().getClass().getName());

		
		blackSquare.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				gestureEnabled = false;
				final OrthographicCamera cam=  (OrthographicCamera) getStage().getCamera();
				cam.zoom = 1;
				constrainPosition(cam, getStage());
			//	cam.position.set(0,0,0);
				
				boolean played = prefs.getBoolean(Values.played, false);
				Random rnd = new Random(System.currentTimeMillis());
				if(rnd.nextInt(100) < 25 && played){
					
					showInterstitial();
					//getGame().getNative().showInterstitial();
				}
				
				
				event.getListenerActor().removeListener(this);
				//getStage().addAction( Util.zoomTo(1f, 0, null));
				//p2z.setCanPan(false);
				//p2z.setCanZoom(false);
				
				Preferences  preferences = Gdx.app.getPreferences(getGame().getClass().getName());
				preferences.putBoolean(Values.played, true);
				preferences.flush();
				
				
				main.addAction(Actions.sequence(Actions.fadeOut(1), Actions.removeActor()));
				getStage().getRoot().addAction(Actions.sequence(Actions.delay(1), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						final String symbolOfInterest = symbolMap.get(valueOfInterest);
						//final Image spot = new Image(getGame().getManager().getAtlas().findRegion(Integer.toString(valueOfInterest)));
						final Group spot = new Group();
						final Label spotLabel = new Label(symbolOfInterest, skin, "xsize");
						spot.addActor(spotLabel);
						final float scale = 2;
						spot.setScale(scale);
						spot.setSize(spotLabel.getWidth() * spot.getScaleX(), spotLabel.getHeight() * spot.getScaleY());
						spot.getColor().a = 0;
						getStage().addActor(spot);
						spot.addAction(Actions.sequence(Actions.fadeIn(1)));
						float mul = 1;
						spot.setSize(spot.getWidth()*mul,spot.getHeight()*mul);
						Util.center(spot);
						
						
						/////// RESTART //////////
							final TextButton restart = new L10nButton(getGame().getL10n(), "restart", getGame().getManager().getSkin(), "medium");
							restart.setVisible(false);
							getStage().addActor(restart);
							
							restart.addListener(new ClickListener(){
								public void clicked (InputEvent event, float x, float y) {
	
									
									createGame();
								}
							});
							
							Util.center(restart);
							restart.moveBy(0, -250);
							
							restart.addAction(blink());
						
						
						getStage().addAction(Actions.sequence(Actions.delay(5f), Actions.run(new Runnable() {
							
							@Override
							public void run() {
								restart.setVisible(true);

							}
						})));
						
						
						int playsCount = prefs.getInteger(Values.playsCount, 1);
						prefs.putInteger(Values.playsCount, playsCount+1);
						prefs.flush();
						
						////// GUESSS //////
						if(playsCount>1){
								getStage().addAction(Actions.sequence(Actions.delay(2), Actions.run(new Runnable() {
									
									@Override
									public void run() {
										
										 TextButton guess = new L10nButton(getGame().getL10n(), "how_it_guess", getGame().getManager().getSkin(), "medium");
										 guess.align(Align.center);
										//getStage().addActor(guess);
										 TextButton howto = new L10nButton(getGame().getL10n(), "howto", getGame().getManager().getSkin(), "small");
										 howto.align(Align.center);
										 
										final Table uplabel = new Table();
										getStage().addActor(uplabel);
										uplabel.defaults().align(Align.center);								
										uplabel.add(guess).row();
										uplabel.add(howto);
										uplabel.pack();
										
										uplabel.addListener(new ClickListener(){
											public void clicked (InputEvent event, float x, float y) {
												gestureEnabled = true;
												
												boolean rated = prefs.getBoolean(Values.rated, false);
												if(!rated){
													getGame().getNative().rate();
													prefs.putBoolean(Values.rated, true);
													prefs.flush();
												}
		
			
												
												final Dialog dialog = new Dialog("", getGame().getManager().getSkin(), "default");
												dialog.getButtonTable().defaults().pad(20);
												dialog.setClip(false);
												dialog.setBackground("dialog");
												dialog.setModal(true);
												dialog.setMovable(false);
												Label label = new L10nLabel(getGame().getL10n(), "secret", getGame().getManager().getSkin(), "tiny");
												label.setAlignment(Align.center);
												label.setWrap(true);
												dialog.getContentTable().add(label).width(400).pad(15);
												Button close = new L10nButton(getGame().getL10n(), "ok", getGame().getManager().getSkin(), "medium");
												dialog.button(close);
												close.addListener(new ClickListener(){
													public void clicked (InputEvent event, float x, float y) {
														final int interstitialShowCount =  prefs.getInteger(Values.guess_click_count, 0);
														if(interstitialShowCount % 3 == 0){																																										
															showInterstitial();
														}
														 prefs.putInteger(Values.guess_click_count, interstitialShowCount+1);
														 prefs.flush();
														
														
														
														dialog.hide();
														
														cam.zoom = 1;
														constrainPosition(cam, getStage());
													}
												});
												
												Label superWantRate = new L10nLabel(getGame().getL10n(), "super_want_rate", getGame().getManager().getSkin(), "tiny2");
												superWantRate.setAlignment(Align.center);
												//dialog.getButtonTable().add(superWantRate);
												superWantRate.addListener(new ClickListener(){
													public void clicked (InputEvent event, float x, float y) {
														getGame().getNative().rate();
														prefs.putBoolean(Values.rated, true);
														prefs.flush();
														
														dialog.hide();
														
												
													}
												});
												
												dialog.show(getStage());
												
		
											}
										});
										
										Util.center(uplabel);
										uplabel.setPosition(uplabel.getX(), getStage().getHeight()+5);
										
										float targetY = (getStage().getHeight() + getStage().getHeight()/2+spot.getHeight()/2)/2-40;
										uplabel.addAction(Actions.sequence(Actions.moveTo(uplabel.getX(), targetY, 1)/*, blink()*/));
									}
								})));
						
						}
						
						
					}
				})));
			}
		});
	}

	void offlineDialog(){


		final Dialog dialog = new Dialog("", getGame().getManager().getSkin(), "default");
		dialog.getButtonTable().defaults().pad(20);
		dialog.setClip(false);
		dialog.setBackground("dialog");
		dialog.setModal(true);
		dialog.setMovable(false);
		Label label = new L10nLabel(getGame().getL10n(), "internet", getGame().getManager().getSkin(), "tiny");
		label.setAlignment(Align.center);
		label.setWrap(true);
		dialog.getContentTable().add(label).width(400).pad(15);
		
		Button close = new L10nButton(getGame().getL10n(), "ok", getGame().getManager().getSkin(), "small");
		dialog.button(close);
		close.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				dialog.hide();
				
			//	p2z.setCanPan(false);
			///	p2z.setCanZoom(false);
			//	getStage().addAction(Util.zoomTo(1f, 0, null));
			}
		});
		dialog.show(getStage());
	
	
	}
	
	@Override
	public void show() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(detector);	
		multiplexer.addProcessor(stage);
			
		Gdx.input.setInputProcessor(multiplexer);
	
	}

	static Action blink(){
		return Actions.forever(Actions.sequence(Actions.fadeIn(0.3f), Actions.fadeOut(0.3f)));
	}
	
	@Override
	public String getName() {
	
		return "game";
	}
	
}
