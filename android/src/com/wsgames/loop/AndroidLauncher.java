package com.wsgames.loop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mygdx.game.*;

public class AndroidLauncher extends AndroidApplication implements AdHandler {
	private static String TAG = "AndroidLauncher";
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;

	protected AdView adView;

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what){
				case SHOW_ADS:
					adView.setVisibility(adView.VISIBLE);
					break;

				case HIDE_ADS:
					adView.setVisibility(View.GONE);
					break;

			}
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RateHelper.setContext(getApplicationContext());

		RelativeLayout layout = new RelativeLayout(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;


		View gameView = initializeForView(new Loop(new Rate(new RateHelper() {
		})),config);
		layout.addView(gameView);

		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				Log.i(TAG, "Ad Loaded");
			}
		});

		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId("ca-app-pub-5814997810466192/6524411269");



		AdRequest.Builder builder = new AdRequest.Builder();
	    builder.addTestDevice("93C289971D848371C4962AE14C9C34ED");
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT

		);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


		layout.addView(adView,adParams);
		adView.loadAd(builder.build());

		setContentView(layout);


		//initialize(new Loop(), config);
	}

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}
