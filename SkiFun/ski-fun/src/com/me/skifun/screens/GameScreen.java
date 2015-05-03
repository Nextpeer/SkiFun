package com.me.skifun.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.skifun.NextpeerPlugin;
import com.me.skifun.SkiFun;
import com.me.skifun.controller.Settings;
import com.me.skifun.controller.WorldController;
import com.me.skifun.model.Bob;
import com.me.skifun.model.Bob.State;
import com.me.skifun.model.OverlapTester;
import com.me.skifun.model.World;
import com.me.skifun.view.Assets;
import com.me.skifun.view.WorldRenderer;

/**
 * The GameScreen is one the game's screen.
 * It shows up while the player is playing the game.
 */
public class GameScreen implements Screen, InputProcessor  {
	SkiFun game;
	/**
	 * States
	 */
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_OVER = 4;
	int state;

	Vector3 touchPoint;
	boolean dragged=false;
	public World 			world;
	private WorldRenderer 	renderer;
	private WorldController	controller;
	private final OrthographicCamera guiCam;
	private final SpriteBatch batcher;
	private String scoreString="";
	private float readyTime;
	private float highscoreTime;
	private float notesTime;
	int lastScore;
	int lastReportedScore=1;
	private boolean highscore;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;
	Rectangle arrowRight, arrowLeft, arrowDown;
	float animationFrameIndex=1;
	boolean enlargeAni=true;
	/*
	 * TUTORIAL
	 */
	private boolean isTutorial=true;
	private boolean isTilt=true;
	private boolean isTouchTheScreen=true;
	private boolean isTakePlatform=true;
	private boolean dontCrash=true;
	private float dontCrashTimer=0;
	private float tutorialTimer=0;
	private boolean havefun=true;
	///
	String strs[]= new String[3];
	private boolean isMultiPlayer=false;


	public GameScreen (SkiFun game) {
		/**
		 * Constructor
		 */
		this.game=game;
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		batcher=game.batcher;
		touchPoint = new Vector3();
		notesTime=0;
		Gdx.input.setCatchBackKey(true);
		lastScore=0;
		state=GAME_READY;
		readyTime=0;
		highscoreTime=0;
		pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);
		resumeBounds = new Rectangle(190, 460-20, 192, 20);
		quitBounds = new Rectangle(190, 460-40, 192, 20);
		arrowRight= new Rectangle (80,2,2,2);
		arrowLeft= new Rectangle (30,2,2,2);
		arrowDown= new Rectangle (250,2,2,2);
		highscore=false;
		/** Loads the ski fun settings **/
		Settings.load();
		isTutorial=Settings.isTutorial; // Check if the tutorial should be shown
		if (NextpeerPlugin.isCurrentlyInTournament())
		{ // if multiplayer- no tutorial
			isMultiPlayer=true;
			isTutorial=false;
		}
		System.out.println("game screen created");


	}

	public void update (float deltaTime) {

		if (deltaTime > 0.1f) deltaTime = 0.1f;
		readyTime+=deltaTime;
		switch (state) {
		case GAME_READY:
			updateReady(deltaTime);
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}
	private void updatePaused () {
		/**
		 * Updates the game screen while paused
		 */
		world.getBob();
		if (Bob.state!=State.DYING) // if bob was hit and we pressed pause, we want to keep him dying
			world.getBob().setState(Bob.State.PAUSED);
		/**
		 * The pause menu
		 */
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(resumeBounds, touchPoint.x, touchPoint.y)) {

				if (Settings.soundEnabled) Assets.clickSound.play(1);
				world.getBob();
				if (Bob.state!=State.DYING)
					world.getBob().setState(Bob.State.IDLE);
				state = GAME_RUNNING;
				return;
			}
			if (OverlapTester.pointInRectangle(quitBounds, touchPoint.x, touchPoint.y)) {

				if (Settings.soundEnabled) Assets.clickSound.play(1);
				highscoreTime=0;
				NextpeerPlugin.reportControlledTournamentOverWithScore(lastScore);

				dispose();
				System.out.println("a");
				game.setScreen(new MainScreen(game));
				return;
			}
		}
	}

	private void updateReady(float deltaTime)
	{
		/**
		 * Updating the game screen while ready
		 */
		controller.downReleased();
		controller.rightReleased();
		controller.leftReleased();
		if (readyTime>=3)
		{ // after 3 seconds we start to run
			state=GAME_RUNNING;
			world.getBob().begin();
		}
	}
	private void updateGameOver()
	{
		/**
		 * Update the game screen while game  is over
		 */
		highscoreTime=0;
		NextpeerPlugin.reportControlledTournamentOverWithScore(lastScore);

		if (Gdx.input.justTouched()) {
			world.getBob().reset();
			dispose();
			game.setScreen(new GameOverScreen(game,lastScore));
		}

	}
	private void updateRunning(float deltaTime) {
		/**
		 * Update the game screen while the game is running
		 */
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(pauseBounds, touchPoint.x, touchPoint.y) &&
					(!NextpeerPlugin.isCurrentlyInTournament())) {
				state = GAME_PAUSED;
				return;
			}
		}
		world.update(deltaTime, Gdx.input.getAccelerometerX());
		if (world.score>15000&& !isTutorial &&!isMultiPlayer)
		{
			Settings.isTutorial=false;
			Settings.save();
		}
		if (world.score != lastScore) {
			lastScore = world.score;
			scoreString = "SCORE: " + lastScore;
		}
		if (lastReportedScore<lastScore-500||lastReportedScore==-1)
		{
			/**
			 * Reporting nextpeer of scores
			 */
			// Nextpeer integration: Report on the new score
			NextpeerPlugin.reportScoreForCurrentTournament(lastScore);

			// Nextpeer integration
			lastReportedScore=lastScore;
		}
		if (world.state == World.WORLD_STATE_GAME_OVER){
			/**
			 * If The game is over
			 */
			state = GAME_OVER;

			scoreString = "SCORE: "+lastScore ;
			if (highscore)
				scoreString = "SCORE: " + lastScore;
			else
				scoreString = "SCORE: " + lastScore;
			Settings.addScore(lastScore);
			Settings.save();

		}

		if (lastScore >= Settings.highscores[0])
		{
			/**
			 * In order to show up a highscore title when it beats the record.
			 */
			if (!highscore)
				highscoreTime=0;
			highscore=true;
		}

	}
	@Override
	public void show() {
		world= new World();
		renderer = new WorldRenderer(world,game, false);
		controller = new WorldController(world);
		scoreString = "SCORE: 0";
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255f, 255f, 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batcher.begin();
		batcher.draw(Assets.largeHill,0,480-80,320,100);
		batcher.end();
		controller.update(delta);
		renderer.render();
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.begin();
		switch (state) {
		case GAME_RUNNING:
			presentRunning(delta);
			break;
		case GAME_READY:
			presentGameReady();
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		case GAME_OVER:
			presentGameOver();
			break;
		}
		batcher.end();

		update(delta);
	}

	private void presentGameOver () {

		batcher.draw(Assets.gameOver, 160 - 160 / 2 -10, 310, 200, 80);

		float scoreWidth = Assets.font.getBounds(scoreString).width;
		Assets.font.draw(batcher, scoreString, 160 - scoreWidth / 2, 480 - 20);

		NextpeerPlugin.reportControlledTournamentOverWithScore(lastScore);
	}
	private void presentRunning (float delta) {
		highscoreTime+=delta;
		if (!isMultiPlayer)
			batcher.draw(Assets.pause, 320 - 60, 480 - 45, 64, 28);
		batcher.draw(Assets.score,80,480-50,80,35);
		Assets.font.draw(batcher, Integer.toString(lastScore), 140, 480 - 20);
		/**
		 * Tutorial
		 */
		if (isTutorial)
		{
			world.getBob().godmode=true;
			world.getBob().letMetersGrow=false;
			tutorialTimer+=delta;
			if (isTilt&&(Math.abs(world.getBob().getVelocity().x)<1f||tutorialTimer<3))
			{
				batcher.draw(Assets.tilt,25,480-130-84,273,84);
			}
			else
			{
				isTilt=false;
			}
			if (!isTilt)
			{
				if (isTouchTheScreen&& !world.getBob().facingDown)
				{
					batcher.draw(Assets.touchTheScreen,80,480-130-84,273,84);
				}
				else
				{
					isTouchTheScreen=false;
				}
				if (!isTouchTheScreen)
				{
					if (isTakePlatform&& !world.getBob().isJumpMode())
					{
						batcher.draw(Assets.takePlatform,80,480-130-84,273,84);
					}
					else
					{
						isTakePlatform=false;
					}
					if (!isTakePlatform)
					{
						dontCrashTimer+=delta;
						if (dontCrash&&dontCrashTimer<5)
							batcher.draw(Assets.dontCrash,85,480-130-84,273,84);
						else
						{
							dontCrash=false;
						}
						if (!dontCrash)
						{
							dontCrashTimer+=delta;
							if (havefun&&dontCrashTimer<10)
							{
								batcher.draw(Assets.havefun,95,480-130-84,273,84);
							}
							else
							{
								havefun=false;
								dontCrashTimer=0;
								isTutorial=false;
								world.getBob().godmode=false;
								world.getBob().godmode(3,false);
								world.getBob().letMetersGrow=true;

							}
						}
					}
				}
			}
		}
		if (world.notesString.size()>0)
		{
			/**
			 * Presenting information of the opponents in a multiplayer mode
			 */
			try
			{
				notesTime+=delta;

				for (int i=world.notesString.size()-1;i>=Math.max(0,world.notesString.size()-4);i--)
				{

					String str=world.notesString.get(i);
					Assets.font2.draw(batcher, str , 9, 480 - 60 -i*20);
				}
				if (notesTime>3)
				{
					world.notesString.clear();
					notesTime=0;
				}
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}

		if (world.getBob().lives>=1)
		{
			batcher.draw(Assets.lives[world.getBob().lives-1],1,480-50, 80,35);
		}
		if (highscore&&highscoreTime<5&&!isMultiPlayer)
		{
			//TextureRegion keyframe=Assets.newHighscore.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);
			Texture keyframe=Assets.nHighscore;
			batcher.draw(keyframe, 160 - 160 / 2 -10, 320, animationFrameIndex*200/6, animationFrameIndex*80/6);
			if (animationFrameIndex==6&&highscoreTime>3)
				enlargeAni=false;
			if (animationFrameIndex<6&&enlargeAni==true)
			{
				animationFrameIndex+=0.5f;
			}
			else if (animationFrameIndex>0&&enlargeAni==false)
				animationFrameIndex-=0.5f;

		}
		else
		{
			enlargeAni=true;
		}

	}
	private void presentPaused () {
		if (isMultiPlayer)
		{
			state=GAME_RUNNING;
		}

		batcher.draw(Assets.pauseMenu, 190, 410, 120, 55);
		batcher.draw(Assets.score,80,480-50,80,35);
		Assets.font.draw(batcher, Integer.toString(lastScore), 140, 480 - 20);
		batcher.draw(Assets.lives[world.getBob().lives-1],1,480-50, 80,35);
	}

	private void presentGameReady () {
		if (readyTime<1)
		{
			Assets.font.draw(batcher,Integer.toString(3),150, 320);
		}
		else if (readyTime<2)
		{
			Assets.font.draw(batcher,Integer.toString(2),150, 320);
		}
		else if (readyTime<3)
		{
			Assets.font.draw(batcher,Integer.toString(1),150, 320);
		}
	}

	@Override
	public void resize(int width, int height) {
		renderer.setSize(width, height);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		if (state == GAME_RUNNING) state = GAME_PAUSED;
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

	// * InputProcessor methods ***************************//

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT)
			controller.leftPressed();
		if (keycode == Keys.RIGHT)
			controller.rightPressed();
		if (keycode == Keys.DOWN)
		{
			controller.downPressed();
		}
		if(keycode == Keys.BACK) {
			if (isMultiPlayer) {
				NextpeerPlugin.reportForfeitForCurrentTournament();
			}

			dispose();
			System.out.println("c");
			game.setScreen(new MainScreen(game));
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT)
			controller.leftReleased();
		if (keycode == Keys.RIGHT)
			controller.rightReleased();
		if (keycode == Keys.DOWN)
			controller.downReleased();

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchDown(int x,int y,int pointer,int button)
	{
		controller.downPressed();
		return true;
	}
	@Override
	public boolean touchUp(int x,int y,int pointer,int button)
	{
		controller.downReleased();
		return true;
	}
	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
