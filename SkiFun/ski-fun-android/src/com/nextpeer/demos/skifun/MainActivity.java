package com.nextpeer.demos.skifun;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.me.skifun.SkiFun;

public class MainActivity extends AndroidApplication {
	private AndroidTournaments mTournaments = null;
	@Override

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize the tournament instance with the Context
		mTournaments = new AndroidTournaments(this);

		// Pass the Android tournaments instance to the game
		SkiFun skifun = new SkiFun(mTournaments);
		initialize(skifun);
	}
	// Nextpeer integration: Let Nextpeer know that the user session has started
	@Override
	protected void onStart() {
		super.onStart();

		// Notify the beginning of a user session.
		if (mTournaments != null) {
			mTournaments.onStart();
		}
	}

	// Nextpeer integration: Let Nextpeer know that the user session has ended while in tournament
	@Override
	public void onStop() {
		super.onStop();

		// If there is an on-going tournament make sure to forfeit it
		if (mTournaments != null && mTournaments.isCurrentlyInTournament()) {
			mTournaments.reportForfeitForCurrentTournament();
		}
	}

	// Nextpeer integration: In case that the on back pressed and we still in game, we wish to forfeit the current game
	/** The user pressed on the back button */
	@Override
	public void onBackPressed() {
		// If the game is in tournament mode -> forfeit the tournament.
		if (mTournaments != null && mTournaments.isCurrentlyInTournament()) {
			mTournaments.reportForfeitForCurrentTournament();
		}

		super.onBackPressed();
	}
}