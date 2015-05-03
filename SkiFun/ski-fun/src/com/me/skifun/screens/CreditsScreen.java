package com.me.skifun.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.skifun.SkiFun;
import com.me.skifun.view.Assets;

/**
 * The CreditsScreen is one the game's screen.
 * It shows information about the game and it's creators.
 */

public class CreditsScreen implements Screen , InputProcessor{
	SkiFun game; // The game itself
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle backBounds;
	Vector3 touchPoint;
	TextureRegion bg;
	public CreditsScreen (SkiFun game) {
		/**
		 *  Constructor
		 */
		this.game = game;
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		backBounds = new Rectangle(0, 0, 64, 64);
		touchPoint = new Vector3();
		batcher = game.batcher;
		bg = Assets.creditsScreen;
	    Gdx.input.setCatchBackKey(true);

	}

	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		}
	}

	public void draw (float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(bg, 0, 0, 320, 480);
		batcher.end();
		batcher.enableBlending();
	}

	@Override
	public void render (float delta) {
		update(delta);
		draw(delta);
	}
	public boolean keyDown(int keycode) {
        if(keycode == Keys.BACK) {    	
        	dispose();
            game.setScreen(new MainScreen(game));
        }

		return true;
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
		Gdx.input.setInputProcessor(this);

	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}


}
