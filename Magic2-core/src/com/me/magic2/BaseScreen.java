package com.me.magic2;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseScreen implements Screen{
	protected MagicMain game;
	protected Stage stage;
protected static final Color background = Color.WHITE.cpy();
protected static final Color borders = Color.WHITE.cpy();
	
	
	FPSLogger logger = new FPSLogger();
	
	public Stage getStage(){
		return stage;
	}
	


	public abstract String getName();

	
	public MagicMain getGame(){
		return game;
	}
	
	public BaseScreen(MagicMain g){
		 game = g;
		 
		 //Viewport view =  new StretchViewport(768, 1024);
		 //Viewport viewport= new FitViewport(640, 1100);
		 Viewport viewport = new ScalingViewport(Scaling.fit, 680, 1100);
		 
		// Viewport view = new ExtendViewport(640, 800) ;
		 //view.set
		 stage = new Stage(viewport);
		 //stage.getCamera().position.set(0, 0, 0);
		stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());
		

		
		//stage.getRoot().getColor().set(Color.WHITE);
		//OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
		//camera.zoom = 2f;
		// stage.getRoot().setCullingArea(new Rectangle(0, 0, stage.getWidth(), stage.getHeight()));
		Pixmap pmap = new Pixmap(1,1, Format.RGBA8888);
		pmap.setColor(background.cpy());
		pmap.fill();
		
		tex = new Texture(pmap); 
		
		background();
	}

	Texture tex;
	public void background(){

		Image background = new Image(tex);
		background.setFillParent(true);
		stage.getRoot().addActor(background);
	}
	
	public void render (float delta){
		
		Gdx.gl.glClearColor(borders.r, borders.g, borders.b, borders.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		stage.act();
		stage.draw();
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		 stage.getViewport().update(width, height, false);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	//	Gdx.app.log("java heap", "jh:"+Gdx.app.getJavaHeap());
	//	Gdx.app.log("native heap","nh:"+Gdx.app.getJavaHeap());
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		//stage.get
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		if(tex!=null){
			tex.dispose();
		}
		
		if(stage!=null)
		stage.dispose();
	}

	

	
}
