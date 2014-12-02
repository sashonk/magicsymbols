package com.me.magic2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class Pinch2ZoomListener2 extends InputListener {

	
	public Pinch2ZoomListener2(){
	
	}
	
	
	Vector2 pointer1 = new Vector2();
	private final Vector2 pointer2 = new Vector2();
	private final Vector2 initialPointer1 = new Vector2();
	private final Vector2 initialPointer2 = new Vector2();

	
	private float tapSquareSize;
	private boolean inTapSquare;
	private boolean pinching;
	
	 float initialDistance = 0;
	private final VelocityTracker tracker = new VelocityTracker();
	private float tapSquareCenterX, tapSquareCenterY;
	private long gestureStartTime;
	private float initialZoom ;
	private boolean canPan;
	private boolean canPanForce = true;
	private boolean canZoom;
	
	public void setCanZoom(boolean cz){
		canZoom = cz;
	}
	
	private float Zmin = 0.25f;
	
	public void setCanPan(boolean value){
		canPanForce = value;
	}
	
	public void setZMin(float value){
		Zmin = value;
	}

	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
						
		Camera c = event.getStage().getCamera();
		Vector3 v = new Vector3(x, y, 0);
		c.project(v);
		x= v.x;
		y = v.y;
		
		if (pointer > 1) return false;
		
		if (pointer == 0) {
			pointer1.set(x, y);
			gestureStartTime = Gdx.input.getCurrentEventTime();
			tracker.start(x, y, gestureStartTime);
			if (Gdx.input.isTouched(1)) {
				// Start pinch.
				inTapSquare = false;
				pinching = true;
				initialPointer1.set(pointer1);
				initialPointer2.set(pointer2);
				initialZoom = ((OrthographicCamera)c).zoom;
				canPan = false;
			} else {
				// Normal touch down.
				inTapSquare = true;
				pinching = false;
				tapSquareCenterX = x;
				tapSquareCenterY = y;
				canPan = true;
			}
		} else {
			// Start pinch.
			inTapSquare = false;
			pointer2.set(x, y);
			pinching = true;
			initialPointer1.set(pointer1);
			initialPointer2.set(pointer2);
			initialZoom = ((OrthographicCamera)c).zoom;
			canPan = false;
		}
		
		return true;
	}


	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	
		
		if (pointer > 1) return ;

		// check if we are still tapping.
		if (inTapSquare && !isWithinTapSquare(x, y, tapSquareCenterX, tapSquareCenterY)) inTapSquare = false;


	
			// handle pinch end
		pinching = false;
			//panning = true;
			
		
	}
	
	boolean debug = true;

	 void pinchToZoom(Vector2 initialPointer1,Vector2 initialPointer2, Vector2 pointer1,Vector2 pointer2, Stage s){
		 
		 if(!canZoom){
			 return;
		 }

		 float initialDistance = initialPointer2.dst(initialPointer1);
		 float distance = pointer2.dst(pointer1);
		 float factor = distance / initialDistance;
		 
	
		OrthographicCamera cam =  (OrthographicCamera) s.getCamera();
		cam.zoom = initialZoom/factor;		 
		constrainZoom(cam);
			
/*		 Vector2 c = pointer1.cpy().add(pointer2).div(2);
		 Vector3 c3 = new Vector3(c.x, c.y, 0);
		 cam.unproject(c3);
		 c.set(c3.x, c3.y);
		 Vector2 screenCoors = new Vector2(c.x, Gdx.graphics.getHeight()-c.y);
		 Vector2 localc = actor.screenToLocalCoordinates(screenCoors);
		 
		 
			
			float w2 = actor.getStage().getWidth()/2;
			float h2 = actor.getStage().getHeight()/2;
			cam.position.set((w2*cam.zoom-localc.x*cam.zoom+localc.x-w2*Zmin)/(1- Zmin), (h2*cam.zoom-localc.y*cam.zoom+localc.y-h2*Zmin)/(1-Zmin), 0);
			*/
			constrainPosition(cam, s);
		 

	}

	public void touchDragged (InputEvent event, float x, float y, int pointer) {
		Camera c = event.getStage().getCamera();
		Vector3 v = new Vector3(x, y, 0);
		c.project(v);
		//c.pro
		x= v.x;
		y = v.y;
		
		if (pinching) {
			if (pointer == 0)
				pointer1.set(x, y);
			else{
				pointer2.set(x, y);
			}
		
			pinchToZoom(initialPointer1, initialPointer2, pointer1, pointer2, event.getStage());
			
		}
		
		
		// update tracker
		if(!pinching)
		tracker.update(x, y, Gdx.input.getCurrentEventTime());
		
		

		// check if we are still tapping.
		if (inTapSquare && !isWithinTapSquare(x, y, tapSquareCenterX, tapSquareCenterY)) {
			inTapSquare = false;
		}

		// if we have left the tap square, we are panning
		if (!inTapSquare && canPan) {
			 pan(x, y, tracker.deltaX, tracker.deltaY, event.getStage());
		}
		
	}
	
	private boolean isWithinTapSquare (float x, float y, float centerX, float centerY) {
		return Math.abs(x - centerX) < tapSquareSize && Math.abs(y - centerY) < tapSquareSize;
	}
	
	private void pan(float x, float y, float dx, float dy, Stage s){
		
		if(!pinching && canPanForce){
		OrthographicCamera c =  (OrthographicCamera) s.getCamera();
		c.translate(-dx, -dy);
		
		constrainPosition(c, s);
		}
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
	
	private  void constrainZoom(OrthographicCamera cam){
		if(cam.zoom<Zmin){
			cam.zoom = Zmin;
		}
		if(cam.zoom>1){
			cam.zoom = 1;
		}
	}
	
	
	static class VelocityTracker {
		int sampleSize = 10;
		float lastX, lastY;
		float deltaX, deltaY;
		long lastTime;
		int numSamples;
		float[] meanX = new float[sampleSize];
		float[] meanY = new float[sampleSize];
		long[] meanTime = new long[sampleSize];

		public void start (float x, float y, long timeStamp) {
			lastX = x;
			lastY = y;
			deltaX = 0;
			deltaY = 0;
			numSamples = 0;
			for (int i = 0; i < sampleSize; i++) {
				meanX[i] = 0;
				meanY[i] = 0;
				meanTime[i] = 0;
			}
			lastTime = timeStamp;
		}

		public void update (float x, float y, long timeStamp) {
			long currTime = timeStamp;
			deltaX = x - lastX;
			deltaY = y - lastY;
			lastX = x;
			lastY = y;
			long deltaTime = currTime - lastTime;
			lastTime = currTime;
			int index = numSamples % sampleSize;
			meanX[index] = deltaX;
			meanY[index] = deltaY;
			meanTime[index] = deltaTime;
			numSamples++;
		}

		public float getVelocityX () {
			float meanX = getAverage(this.meanX, numSamples);
			float meanTime = getAverage(this.meanTime, numSamples) / 1000000000.0f;
			if (meanTime == 0) return 0;
			return meanX / meanTime;
		}

		public float getVelocityY () {
			float meanY = getAverage(this.meanY, numSamples);
			float meanTime = getAverage(this.meanTime, numSamples) / 1000000000.0f;
			if (meanTime == 0) return 0;
			return meanY / meanTime;
		}

		private float getAverage (float[] values, int numSamples) {
			numSamples = Math.min(sampleSize, numSamples);
			float sum = 0;
			for (int i = 0; i < numSamples; i++) {
				sum += values[i];
			}
			return sum / numSamples;
		}

		private long getAverage (long[] values, int numSamples) {
			numSamples = Math.min(sampleSize, numSamples);
			long sum = 0;
			for (int i = 0; i < numSamples; i++) {
				sum += values[i];
			}
			if (numSamples == 0) return 0;
			return sum / numSamples;
		}

		private float getSum (float[] values, int numSamples) {
			numSamples = Math.min(sampleSize, numSamples);
			float sum = 0;
			for (int i = 0; i < numSamples; i++) {
				sum += values[i];
			}
			if (numSamples == 0) return 0;
			return sum;
		}
	}

}
