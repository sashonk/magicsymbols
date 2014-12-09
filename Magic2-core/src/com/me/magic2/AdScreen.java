package com.me.magic2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AdScreen extends BaseScreen{
	
	GameScreen _gameScreen;
	
	static String[] banners = {"static.png"};


	public AdScreen(MagicMain g , GameScreen gameScreen) {
		super(g);
		_gameScreen = gameScreen;
		

	}
	
	static interface BannerPolicy{
		String queryBannerName(String[] banners);
	}
	
	static class RandomBannerPolicy implements BannerPolicy{

		@Override
		public String queryBannerName(String[] banners) {
			return banners[new Random(System.currentTimeMillis()).nextInt(banners.length)];
		}
		
	}
	
	static class FareBannerPolicy implements BannerPolicy{
		
		static List<String> shownAds;
		static{
			shownAds = new LinkedList<String>();
		}
		
		
		@Override
		public String queryBannerName(String[] banners) {
			List<String> list = Arrays.asList(banners);
			list.removeAll(shownAds);
			
			if(list.isEmpty()){
				shownAds.clear();
				list.addAll(Arrays.asList(banners));
			}
			
			return list.get(new Random(System.currentTimeMillis()).nextInt(list.size()));
		}
				
	}
	
	@Override
	public void show(){
		super.show();
		
		AssetManager m = getGame().getManager().getAssetManager();
		
		String bannerPath = "data/adv/"+ new FareBannerPolicy().queryBannerName(banners);
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
