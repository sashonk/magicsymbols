package com.me.magic2.android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.me.magic2.IActivityRequestHandler;
import com.me.magic2.MagicMain;


public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
	
	  private static final String AD_UNIT_ID_I =	"ca-app-pub-5651934775071578/8678151646";
	  private static final String GIRLFRIENDs_HUAWEY = "C1E23BAF9DEC590B1ADF948CF0B72E47";
	  private static final String EXPLAY = "02BD43C728CE8855D954F242C3C36266";
	  protected AdView adView;
	  protected InterstitialAd interstitial;
	  private static final String appId = "com.me.magic2.android";
	  protected View gameView;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
  	  
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MagicMain(this), config);
/*
	    AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
	    cfg.useAccelerometer = false;
	    cfg.useCompass = false;

	    // Do the stuff that initialize() would do for you
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

	    RelativeLayout layout = new RelativeLayout(this);
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	    layout.setLayoutParams(params);

	    AdView admobView = createAdView();
	    layout.addView(admobView);
	    View gameView = createGameView(cfg);
	    layout.addView(gameView);

	    setContentView(layout);
	    adView.loadAd(buildRequest());*/
	    
	    
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(AD_UNIT_ID_I);
		interstitial.setAdListener(new AdListener() {
   		@Override
			public void onAdClosed() {
				AdRequest adRequest = new AdRequest.Builder().build();
				interstitial.loadAd(adRequest);
			}
		});
	    interstitial.loadAd(buildRequest());
	    

}
	

	
/*    private AdView createAdView() {
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setId(12345); // this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.BLACK);
        return adView;
      }*/

/*      private View createGameView(AndroidApplicationConfiguration cfg) {
        gameView = initializeForView(new MagicMain(this), cfg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, adView.getId());
        gameView.setLayoutParams(params);
        return gameView;
      }*/
 
      @Override
      public void showInterstitial(){
    	 		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				 if (interstitial.isLoaded()) {
			          interstitial.show();
			        }	
				 else{
					 AndroidLauncher.this.log(appId, "ad has not yet been loaded...");
					 
				 }
			}
		});
      }
      
      private AdRequest buildRequest(){
         return  new AdRequest.Builder().
        		     addTestDevice(AdRequest.DEVICE_ID_EMULATOR).
        		     addTestDevice(EXPLAY).
          		build();
      }


      
      @Override
      public void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
      }

      @Override
      public void onPause() {
        if (adView != null) adView.pause();
        super.onPause();
      }

      @Override
      public void onDestroy() {
        if (adView != null) adView.destroy();
        super.onDestroy();
      }
	

	@Override
	public void showAdd(int id, boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rate() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
		
		        AndroidLauncher.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId)));
     
				
			}
		});
		
	}

	@Override
	public void checkInternetConnection(final ConnectionStatusCallback callback) {
	
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ConnectivityManager cm =
				        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				    NetworkInfo netInfo = cm.getActiveNetworkInfo();
				    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				    	callback.online();
				    }
				    else {
				    	callback.offline();
				    }
				
			}
		});
		
		    
		    
	}
}
