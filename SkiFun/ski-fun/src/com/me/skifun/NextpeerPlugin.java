package com.me.skifun;

import com.nextpeer.libgdx.Tournaments;

public final class NextpeerPlugin {

	private static NextpeerPlugin _sInstance = null;

	public final Tournaments tournaments;
	public long lastKnownTournamentRandomSeed = 0;

	/***
	 * Private methods
	 */

	/**
	 * Private CTOR for the NextpeerPlugin class
	 * @param tournaments Holding the implementation of the NextpeerPlugin
	 */
	private NextpeerPlugin(Tournaments tournaments) {
		this.tournaments = tournaments;

	}

	/**
	 * Load the NextpeerPlugin instance with the Game object
	 * @param tournaments Holding the implementation of the NextpeerPlugin
	 * @return The initialized NextpeerPlugin object
	 */
	public synchronized static NextpeerPlugin load(Tournaments tournaments) {
		if (_sInstance == null) {
			_sInstance = new NextpeerPlugin(tournaments);
		}

		return _sInstance;
	}

	/**
	 * Gets the NextpeerPlugin instance if loaded, null if not
	 * @return The NextpeerPlugin instance if loaded, null if not
	 */
	public static NextpeerPlugin instance() {
		return _sInstance;
	}

	/**
	 * Convenience method. Return true if the Tournaments instance if available, false if not
	 * @return true Tournaments instance if available, false if not
	 */
	public static boolean isAvailable() {
		return (instance() != null);
	}

	/**
	 * Convenience method. Gets the Tournaments instance if available, null if not
	 * @return The Tournaments instance if available, null if not
	 */
	private static Tournaments tournaments() {
		if (!NextpeerPlugin.isAvailable()) {
			return null;
		}

		return instance().tournaments;
	}

	/**
	 * Returns true if there is an active tournament.
	 * @return true if there is any tournament running at this moment, false otherwise.
	 * @throws IllegalArgumentException if {@code userId} is empty or null.
	 * @throws IllegalArgumentException if {@code timeDelta} is negative.
	 */
	public static final boolean isCurrentlyInTournament() {
		Tournaments t = tournaments();
        return t != null && t.isCurrentlyInTournament();

    }

	/**
	 * Call this method to report the current score for the tournament. This allows Nextpeer to send
	 * various notifications about the players' scores.
	 * @param score The current game score to be reported.
	 * @throws IllegalArgumentException if {@code score} is negative.
	 */
	public static final void reportScoreForCurrentTournament(int score) {

		Tournaments t = tournaments();
		if (t == null) return;

		t.reportScoreForCurrentTournament(score);
	}

	/**
	 * This method is used to push a buffer to the other players.
	 * Unlike the pushDataToOtherPlayers method, buffers sent through this function are not guaranteed to reach 
	 * the other players. However, the buffers sent via this method arrive on the other end much more quickly.
	 * This can potentially be used notify other players of changes in the game state. 
	 * The buffer will be sent to the other players and will activate the 
	 * {@code NextpeerListener::onReceiveUnreliableTournamentCustomMessage:} method on their listener.
	 * @param data The byte array to send to the other connected players.
	 * @throws IllegalArgumentException if {@code data} is empty.
	 */
	public static final void unreliablePushDataToOtherPlayers(byte[] data) {

		if (!isCurrentlyInTournament()) return;

		Tournaments t = tournaments();
		if (t == null) return;

		t.unreliablePushDataToOtherPlayers(data);
	}

	/**
	 * Call this method when your game manages the current tournament and the player just died (a.k.a. 'Last Man Standing').
	 * <p><strong>Note:</strong> The method will act only if the current tournament is from a 'GameControlled' tournament type.</p>
	 * @param score The current game score to be reported.
	 * @throws IllegalArgumentException if {@code score} is negative.
	 */
	public static final void reportControlledTournamentOverWithScore(int score) {
		if (!isCurrentlyInTournament()) return;

		Tournaments t = tournaments();
		if (t == null) return;

		t.reportControlledTournamentOverWithScore(score);
	}

	/**
	 * Call this method when the user wishes to exit the current tournament.
	 */
	public static final void reportForfeitForCurrentTournament() {
		if (!isCurrentlyInTournament()) return;

		Tournaments t = tournaments();
		if (t == null) return;

		t.reportForfeitForCurrentTournament();
	}

    /**
	 * Launches the Nextpeer console.
	 */
    public static final void launch() {
		Tournaments t = tournaments();
		if (t == null) return;

		t.launch();
    }
}
