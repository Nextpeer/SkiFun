package com.me.skifun.controller;
import java.util.HashMap;
import java.util.Map;

import com.me.skifun.model.Bob;
import com.me.skifun.model.World;
import com.me.skifun.model.Bob.State;

/**
 * The Controller class controls the player's movement. 
 */


public class WorldController {

	enum Keys {
		LEFT, RIGHT, DOWN
	}

	private Bob 	bob; // the player
	private float haltBob=0; // timer in order to halt the movement of bob in some cases
	static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.DOWN, false);
	}

	public WorldController(World world) {
		/**
		 * Constructor. Gets the world of the game.
		 */
		this.bob = world.getBob();
	}

	/**
	 * Keys Pressed
	 */
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}
	public void downPressed() {
		keys.get(keys.put(Keys.DOWN, true));
	}
	/**
	 * Keys released
	 */
	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
		bob.setFacingLeft(false);
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
		bob.setFacingRight(false);

	}
	public void downReleased() {
		/**
		 * When down is released bob returns to the idle state
		 * and to the idle velocity.
		 */
		keys.get(keys.put(Keys.DOWN, false));
		bob.setFacingDown(false);
		if (Bob.state!=Bob.State.DYING&&!bob.isGodMode()&&Bob.state!=Bob.State.DEAD&&Bob.state!=State.PAUSED)
		{
			bob.getVelocity().y=-Bob.DOWN_SPEED;
			bob.setState(State.IDLE);
		}
		else if (bob.isGodMode()&&!bob.isJumpMode())
		{
			bob.getVelocity().y=-Bob.DOWN_SPEED;
			bob.setState(State.IDLE);
		}

	}


	/** The main update method **/
	public void update(float delta) {
		if (Bob.state==Bob.State.PAUSED)
			return;
		if (Bob.state==Bob.State.DEAD)
		{
			bob.getVelocity().y=0;
			return;
		}
		if (Bob.state==Bob.State.DYING)
		{
			bob.getVelocity().y=0;
			haltBob+=delta;
			if (haltBob>2)//&&keys.get(Keys.DOWN))
			{
				bob.setState(State.IDLE);
				bob.godmode(5,false);
				bob.getVelocity().y=-Bob.DOWN_SPEED;
				haltBob=0;
			}
			return;
		}
		processInput();
		bob.update(delta);
	}

	/** Change Bob's state and parameters based on input controls **/
	private void processInput() {
		if (Bob.state==Bob.State.PAUSED)
			return;
		if (keys.get(Keys.LEFT)) {
			/**
			 * Left is pressed
			 */
			bob.setFacingRight(false);
			bob.setFacingLeft(true);
			bob.setState(State.SKIING);
			bob.getVelocity().x = -Bob.SPEED;
		}
		if (keys.get(Keys.RIGHT)) {
			/**
			 * Right is pressed
			 */
			bob.setFacingLeft(false);
			bob.setFacingRight(true);
			bob.setState(State.SKIING);
			bob.getVelocity().x = Bob.SPEED;

		}
		if (keys.get(Keys.DOWN))
		{
			/**
			 * Down is pressed
			 */
			bob.setFacingDown(true);
			bob.setState(State.SKIING);
			if (!bob.isJumpMode()&&bob.getVelocity().y>-Bob.GOD_SPEED_MULTIPLIER*Bob.DOWN_SPEED)
			{	
				/**
				 * Check if bob's speed is in the idle state and if so accelerate it.
				 */
				if (bob.getAccelerationMode()==1)
				{
					Bob.DOWN_MULTIPLIER=1.7f;
				}
				else if (bob.getAccelerationMode()==2)
				{
					Bob.DOWN_MULTIPLIER=1.5f;
				}
				else
				{
					Bob.DOWN_MULTIPLIER=1.7f;
				}
				bob.getVelocity().y=-Bob.DOWN_SPEED*Bob.DOWN_MULTIPLIER;

			}
		}
	}
}
