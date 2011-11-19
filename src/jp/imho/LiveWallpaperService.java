package jp.imho;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.runnable.RunnableHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class LiveWallpaperService extends BaseLiveWallpaperService implements ITimerCallback {
	// ===========================================================
	// Constants
	// ===========================================================
	
	private int screenWidth;
	private int screenHeight;
	
	int dX = 2;
	int dY = 2;
	
	Sprite ballSprite;

	// ===========================================================
	// Fields
	// ===========================================================
	
	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TextureRegion mFaceTextureRegion;

	private ScreenOrientation mScreenOrientation;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public org.anddev.andengine.engine.Engine onLoadEngine() {
		this.getDisplaySize(); //getting devices display resolution
		
		Camera camera = new Camera(0, 0, this.screenWidth, this.screenHeight);
		EngineOptions engineOptions = new EngineOptions(true, this.mScreenOrientation, new FillResolutionPolicy(), camera);
		return new org.anddev.andengine.engine.Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 0);
		this.getEngine().getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		this.getEngine().registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		final int centerX = (this.screenWidth - this.mFaceTextureRegion.getWidth()) / 2;
		final int centerY = (this.screenHeight - this.mFaceTextureRegion.getHeight()) / 2;
		this.ballSprite = new Sprite((float)centerX, (float)centerY, this.mFaceTextureRegion);
		TimerHandler timerHandler = new TimerHandler(0.05f, true, this);
		scene.registerUpdateHandler(timerHandler);
		RunnableHandler runnableHandler = new RunnableHandler();
		scene.registerUpdateHandler(runnableHandler);

		scene.attachChild(ballSprite);
		return scene;
	}

	@Override
	public void onLoadComplete() {
		
	}

	@Override
	public void onUnloadResources() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPauseGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResumeGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		// TODO Auto-generated method stub
		Log.d("onTimePassed", ""+pTimerHandler.getTimerSeconds());
		if(this.ballSprite.getX()+this.ballSprite.getWidth()>this.screenWidth||this.ballSprite.getX()<0) {
			this.dX = this.dX * -1;
		}
		if(this.ballSprite.getY()+this.ballSprite.getHeight()>this.screenHeight||this.ballSprite.getY()<0) {
			this.dY = this.dY * -1;
		}
		
		this.ballSprite.setPosition(this.ballSprite.getX()+dX, this.ballSprite.getY()+dY);
	}
	
	private void getDisplaySize() {
		Display d = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		this.screenWidth = d.getWidth();
		this.screenHeight = d.getHeight();
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}