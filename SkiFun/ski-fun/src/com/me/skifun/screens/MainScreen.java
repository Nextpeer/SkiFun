package com.me.skifun.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.skifun.NextpeerPlugin;
import com.me.skifun.SkiFun;
import com.me.skifun.controller.Settings;
import com.me.skifun.model.OverlapTester;
import com.me.skifun.screens.GameScreen;
import com.me.skifun.view.Assets;

/**
 * The MainScreen is one the game's screen.
 * It shows up when the game is created.
 */

public class MainScreen implements Screen {
	SkiFun game;
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle soundBounds;
	Rectangle trainingBounds;
	Rectangle playBounds;
	Rectangle aboutBounds;
	TextureRegion backgroundRegion;
	Vector3 touchPoint;
	boolean musicOn;

	public MainScreen (SkiFun game) {
		/**
		 * Constructor
		 */
		this.game = game;
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		batcher = game.batcher;
		soundBounds = new Rectangle(260, 25, 50, 40);
		trainingBounds = new Rectangle(90, 480-200-30, 147, 41);
		aboutBounds= new Rectangle(90,480-306-30,147,41);
		playBounds= new Rectangle(90,480-252-30,147,41);
		touchPoint = new Vector3();
		backgroundRegion = Assets.backgroundRegion;
		Settings.load();
		if (Settings.soundEnabled)
		{
			musicOn=true;
			Assets.playMusic();
		}
		else
		{
			musicOn=false;
		}

		System.out.println("main screen created");
	}

	public void update (float deltaTime) {
		/**
		 * Update the screen
		 */
		if (Gdx.input.justTouched()) {

			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(trainingBounds, touchPoint.x, touchPoint.y)) {

                if (Settings.soundEnabled) Assets.clickSound.play(1);
				game.setScreen(new GameScreen(game));
				return;
			}
			if (OverlapTester.pointInRectangle(aboutBounds,touchPoint.x,touchPoint.y))
			{
                if (Settings.soundEnabled) Assets.clickSound.play(1);
				game.setScreen(new CreditsScreen(game));
			}
			if (OverlapTester.pointInRectangle(soundBounds, touchPoint.x, touchPoint.y)) {

				Settings.soundEnabled = !Settings.soundEnabled;

                if (Settings.soundEnabled) Assets.clickSound.play(1);
				musicOn=!musicOn;
				Settings.enableSound(musicOn);
				Settings.save();
				if (musicOn)
					Assets.playMusic();
				else
					Assets.music.pause();
			}
			if (OverlapTester.pointInRectangle(playBounds,touchPoint.x,touchPoint.y)){

                if (Settings.soundEnabled) Assets.clickSound.play(1);
				if (NextpeerPlugin.isAvailable()) {
					NextpeerPlugin.launch();
				}
				// Else, we don't have tournament mode, run the game normally
				else {
					game.setScreen(new GameScreen(game));
				}
				// Nextpeer integration 
				return;

			}

		}
	}
	public void draw (float deltaTime)
	{
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		batcher.enableBlending();
		batcher.draw(backgroundRegion, 0, 0, 320, 480);
		batcher.draw(Assets.training,trainingBounds.x,trainingBounds.y,trainingBounds.width,trainingBounds.height);
		batcher.draw(Assets.play,playBounds.x,playBounds.y,playBounds.width,playBounds.height);
		batcher.draw(Assets.about,aboutBounds.x,aboutBounds.y,aboutBounds.width,aboutBounds.height);
		if (musicOn)
			batcher.draw(Assets.soundOn,soundBounds.x,soundBounds.y,50,44);
		else
			batcher.draw(Assets.soundOff,soundBounds.x,soundBounds.y,50,44);
		batcher.end();
	}
	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		backgroundRegion=null;
		Assets.music.dispose();

	}

}
