package com.me.skifun;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.skifun.controller.Settings;
import com.me.skifun.screens.GameScreen;
import com.me.skifun.screens.MainScreen;
import com.me.skifun.view.Assets;
import com.nextpeer.libgdx.NextpeerTournamentCustomMessage;
import com.nextpeer.libgdx.Tournaments;
import com.nextpeer.libgdx.TournamentsCallback;

/**
 * Main Game class.
 */

public class SkiFun extends Game {
	boolean firstTimeCreate = true;
	FPSLogger fps;
	public SpriteBatch batcher;
	public BitmapFont textOverPlayers;
	Tournaments tournaments = null;
	public SkiFun() {
		this(null);
	}

	public SkiFun(Tournaments tournaments) {

		// If we have a supported tournaments object, set the game as callback
		if (tournaments != null && tournaments.isSupported()) {
			tournaments.setTournamentsCallback(mNextpeerTournamentsCallback);
			NextpeerPlugin.load(tournaments);
		}
	}

	@Override
	public void create() {
		batcher = new SpriteBatch();
		textOverPlayers= new BitmapFont(Gdx.files.internal("data/hobo.fnt"),Gdx.files.internal("data/hobo.png"),false,false);
		Settings.load();
		Assets.load();
		// Load the TournamentsCore if we have a valid implementation of it
		if (this.tournaments != null) {
			NextpeerPlugin.load(this.tournaments);
		}
		setScreen(new MainScreen(this));
		fps = new FPSLogger();

	}
	@Override
	public void render() {
		super.render();
		//	fps.log();
	}

	/**
	 * TournamentsCallback implementation
	 * Responsible to answer on certain tournament events such as start tournament & end tournament.
	 */
	private final TournamentsCallback mNextpeerTournamentsCallback = new TournamentsCallback() {

		@Override
		public void onTournamentStart(long tournamentRandomSeed) {
			// Start the game scene
			System.out.println("starting tournament and switching to GAMESCREEN");
			NextpeerPlugin.instance().lastKnownTournamentRandomSeed = tournamentRandomSeed;
			GameScreen gs= new GameScreen(SkiFun.this);
			while (getScreen() instanceof MainScreen)
			{
				setScreen(gs);
			}
		}

		@Override
		public void onTournamentEnd() {
			// End the game scene, switch to main menu
			NextpeerPlugin.instance().lastKnownTournamentRandomSeed = 0;
			setScreen(new MainScreen(SkiFun.this));
		}


		@Override
		public void onReceiveTournamentCustomMessage(NextpeerTournamentCustomMessage message) {
			Screen screen = SkiFun.this.getScreen();

			// Act only if the current scene is the game screen
			if (screen instanceof GameScreen) {
				GameScreen gameScreen = (GameScreen)screen;

				// Pass the received data to the world instance which responsible on the game updates
				gameScreen.world.onReceiveTournamentCustomMessage(message);
			}
		}

		@Override
		public void onReceiveUnreliableTournamentCustomMessage(NextpeerTournamentCustomMessage message) {

			try
			{
				Screen screen = SkiFun.this.getScreen();
				if (screen==null)
					return;
				// Act only if the current scene is the game screen
				if (screen instanceof GameScreen) {
					GameScreen gameScreen = (GameScreen)screen;

					// Pass the received data to the world instance which responsible on the game updates
					if (message==null)
						return;
					if (gameScreen.world==null)
						return;
					gameScreen.world.onReceiveTournamentCustomMessage(message);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	};
}
