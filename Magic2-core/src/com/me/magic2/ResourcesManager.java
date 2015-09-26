package com.me.magic2;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sun.font.TrueTypeFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.StringBuilder;

public class ResourcesManager {


	
	AssetManager manager;
	
	public void finishLoading(){
		manager.finishLoading();
	}
	
	public boolean update(){
		return manager.update();
	}
	
	public boolean update(int millis){
		return manager.update(millis);
	}

	public String getProgressAsString(){	
		return "Загрузка...";
		//BigDecimal bd = BigDecimal.valueOf(100* manager.getProgress()).setScale(0, RoundingMode.HALF_UP);
		//return new StringBuilder("Загрузка ").append(bd.toString()).append("%").toString();
		
		
	}
	
	public Sound getSound(String name){
		return manager.get(name);
	}
	
	public AssetManager getAssetManager(){
		return manager;
	}
	
	
	ResourcesManager() {

			manager = new AssetManager();
		//	manager.setLoader(BitmapFontBundle.class, new BitmapFontBundleLoader(new InternalFileHandleResolver()));
	}
	
	public void loadResources(){
		Gdx.app.debug(this.getClass().getName(), "begin loading resources");
	

		manager.load("data/hd.atlas", TextureAtlas.class);
		
		manager.load("data/fonts/comic18.fnt", BitmapFont.class);
		manager.load("data/fonts/comic18y.fnt", BitmapFont.class);
		manager.load("data/fonts/comic24.fnt", BitmapFont.class);
		manager.load("data/fonts/comic32.fnt", BitmapFont.class);
		manager.load("data/fonts/comic48.fnt", BitmapFont.class);
		manager.load("data/fonts/comic72.fnt", BitmapFont.class);
		manager.load("data/fonts/comic102.fnt", BitmapFont.class);
		manager.load("data/fonts/comic102b.fnt", BitmapFont.class);
		manager.load("data/fonts/glaga72.fnt", BitmapFont.class);
		manager.load("data/fonts/glaga36.fnt", BitmapFont.class);
		manager.load("data/fonts/glaga180.fnt", BitmapFont.class);


	}
	
	
	
	public void init(){
		Gdx.app.debug(this.getClass().getName(), "initializing");
	

		
		skin = new Skin();
		
skin.add("comic18", manager.get("data/fonts/comic18.fnt"));
skin.add("comic18y", manager.get("data/fonts/comic18y.fnt"));
skin.add("comic24", manager.get("data/fonts/comic24.fnt"));
skin.add("comic32", manager.get("data/fonts/comic32.fnt"));
skin.add("comic48",manager.get("data/fonts/comic48.fnt"));
skin.add("comic72", manager.get("data/fonts/comic72.fnt"));
skin.add("comic102", manager.get("data/fonts/comic102.fnt"));
skin.add("comic102b", manager.get("data/fonts/comic102b.fnt"));
skin.add("glaga36", manager.get("data/fonts/glaga36.fnt"));
skin.add("glaga180", manager.get("data/fonts/glaga180.fnt"));

	LabelStyle lsXSize = new LabelStyle();
	lsXSize.font = skin.getFont("glaga180");
	lsXSize.fontColor = Color.BLACK.cpy();
	skin.add("xsize", lsXSize);

 	LabelStyle lsGlaga36 = new LabelStyle();
 	lsGlaga36.font = skin.getFont("glaga36");
 	lsGlaga36.fontColor = Color.BLACK.cpy();
	skin.add("glaga_mini", lsGlaga36);


/*	TextButtonStyle bigStyle = new TextButtonStyle();
	bigStyle.font = skin.getFont("comic72");
	bigStyle.fontColor = Color.WHITE;
	bigStyle.pressedOffsetX = 4;
	bigStyle.pressedOffsetY = - 4;	
	skin.add("big", bigStyle);*/
	
		
	TextButtonStyle medStyle = new TextButtonStyle();
	medStyle.font = skin.getFont("comic72");
	medStyle.fontColor = Color.WHITE;
	medStyle.pressedOffsetX = 3;
	medStyle.pressedOffsetY = - 3;	
	skin.add("medium", medStyle);
	
	
	TextButtonStyle smallStyle = new TextButtonStyle();
	smallStyle.font = skin.getFont("comic32");
	smallStyle.fontColor = Color.WHITE;
	smallStyle.pressedOffsetX = 2;
	smallStyle.pressedOffsetY = - 2;	
	skin.add("small", smallStyle);
	
	
	TextButtonStyle hugeStyle = new TextButtonStyle();
	hugeStyle.font = skin.getFont("comic102");
	hugeStyle.fontColor = Color.WHITE;
	hugeStyle.pressedOffsetX = 4;
	hugeStyle.pressedOffsetY = - 4;	
	skin.add("huge", hugeStyle);
	
	
	LabelStyle lsmallStyle = new LabelStyle();
	lsmallStyle.font = skin.getFont("comic24");	
	skin.add("small", lsmallStyle);
	
	
/*	Pixmap pmap = new Pixmap(1,1, Format.RGBA8888);
	pmap.setColor(Color.RED);
	pmap.fill();
	Texture tx = new Texture(pmap);
	skin.add("red", tx);*/
	
	LabelStyle lsHuge = new LabelStyle();
	lsHuge.font = skin.getFont("comic102b");	
//	lsHuge.background = skin.getDrawable("red");
	skin.add("huge", lsHuge);

	

	
	LabelStyle lStyleTiny = new LabelStyle();
	lStyleTiny.font = skin.getFont("comic18");
	//lStyleTiny.background = skin.getDrawable("red");
	skin.add("tiny", lStyleTiny);
	
	LabelStyle lStyleTiny2 = new LabelStyle();
	lStyleTiny2.font = skin.getFont("comic18y");
	//lStyleTiny.background = skin.getDrawable("red");
	skin.add("tiny2", lStyleTiny2);
	
	
	NinePatch dialogNP = new NinePatch(getAtlas().findRegion("dialog"), 5, 6, 5, 6);
	skin.add("dialog", dialogNP);
	
	WindowStyle def = new WindowStyle();
	def.titleFont = skin.getFont("comic18");
	def.titleFontColor = Color.BLACK;
	skin.add("default", def);
	
	skin.add("flag_ru", getAtlas().findRegion("flag_ru"), TextureRegion.class) ;
	skin.add("flag_en", getAtlas().findRegion("flag_en"), TextureRegion.class) ;

	ImageButtonStyle flag_enStyle = new ImageButtonStyle();
	flag_enStyle.up = skin.getDrawable("flag_en");
	flag_enStyle.pressedOffsetX = 2;
	flag_enStyle.pressedOffsetY = -2;
	skin.add("flag_en", flag_enStyle);
	
	ImageButtonStyle flag_ruStyle = new ImageButtonStyle();
	flag_ruStyle.up = skin.getDrawable("flag_ru");
	flag_ruStyle.pressedOffsetX = 2;
	flag_ruStyle.pressedOffsetY = -2;
	skin.add("flag_ru", flag_ruStyle);
	
	
	}
	

	
	
	public Skin getSkin(){
		return skin;
	}
	
	public TextureAtlas getAtlas(){
		return manager.get("data/hd.atlas");
	}


	private Skin skin;



	private void disposeInternal(){
		

		

		if(skin!=null)
		skin.dispose();
		//nickolLetter.dispose();

		
/*		for(Sound snd : sounds.values()){
			snd.dispose();
		}*/

		if(manager!=null){
			manager.dispose();

		}
	}

	public void dispose(){

		disposeInternal();
	}
}
