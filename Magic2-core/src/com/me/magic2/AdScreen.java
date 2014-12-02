package com.me.magic2;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AdScreen extends BaseScreen{
	
	GameScreen _gameScreen;

	public AdScreen(MagicMain g , GameScreen gameScreen) {
		super(g);
		_gameScreen = gameScreen;
		

					
	}
	
	@Override
	public void show(){
		super.show();
		
		AssetManager m = getGame().getManager().getAssetManager();
		String bannerPath = "data/static.png";
		if(!m.isLoaded(bannerPath)){
			m.load(bannerPath, Texture.class);
			m.finishLoading();
		}
		
		getStage().clear();		
		background();
		Stage inter = getStage();
		
		Image img = new Image(getGame().getManager().getAssetManager().get(bannerPath, Texture.class));
		img.setFillParent(true);					
		inter.addActor(img);
		
		
		Image close = new Image(getGame().getManager().getAtlas().findRegion("close"));
		close.setSize(60, 60);
		inter.addActor(close);
		close.setPosition(inter.getWidth()-close.getWidth(), inter.getHeight()-close.getHeight());
		close.addListener(new ClickListener(){

			public void clicked (InputEvent event, float x, float y) {
				getGame().setScreen(_gameScreen);
				
			}
		});
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ad";
	}

	 

}
