package com.me.skifun.model;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.skifun.controller.Settings;
import com.me.skifun.view.Assets;

/**
 * The main player's class.
 * One of the game's objects.
 */


public class Bob extends GameObject {
	public enum State {
		SKIING(0), JUMPING(1), DYING(2), IDLE(3), DEAD(4), PAUSED(5);

		/**
		 * IDLE: Bob's skiing down
		 * PAUSED: Bob is not moving
		 * DYING: Bob got hit
		 * DEAD: Bob has no more lives
		 */
		final int value;

		private State(int value) {
			this.value = value;
		}
	}

	public static final float SPEED = 1.25f;  // Unit per second. Bob's velocity on the X axis.
	public static  float DOWN_SPEED=0.5f; // Bob's velocity on the Y axis.
	public static float DOWN_MULTIPLIER=1.7f; // Multiplier for the Bob's velocity (for acceleration).
	public static final float JUMP_VELOCITY = 1f; // Bob's velocity on the Y axis while jumping.
	public static final float SIZE = 0.5f; // Bob's size.
	public static final float GOD_SPEED_MULTIPLIER=5f; // Multiplier for the Bob's velocity (for acceleration).
	public int lives; // Bob's lives.
	private int accelerationMode =0; // Bob's acceleration mode.
	public int meters; // Meters that Bob crossed.
	Vector2     position = new Vector2(); // The position of Bob
	Vector2     acceleration = new Vector2();
	Vector2     velocity = new Vector2(); // Bob's velocity
	Rectangle   bounds = new Rectangle(); // Bob's bounds
	public static State       state ; // Bob's state
	public boolean     facingLeft = false; // is Bob facing left
	public boolean		facingRight = false; // is Bob facing right
	public boolean		facingDown = false; // is Bob facing accelerating down
	public boolean godmode; // is Bob in a god mode
	private float godmodeTime=0;
	private float godmodeTimeRunning=5f;
	private float jumpmodeTime=0;
	private boolean jumpmode=false; // is Bob on jump mode
	private boolean jumpmodegodmode=false; // is Bob on jump mode&godmode.
	public float stateTime;
	public float lastVelocityUpdateTime=0;
	public boolean letMetersGrow=true; // if true then the meters grow as bob ski


	public Bob(Vector2 position) {
		/**
		 * Constructor. Creates bob in the position it gets as a parameter.
		 */
		super(position.x, position.y, SIZE/2, SIZE);
		stateTime=0;
		this.position = position;
		this.bounds.height = SIZE-0.1f;
		this.bounds.width = SIZE/2;
		Bob.DOWN_SPEED=0.5f;
		setState(State.PAUSED);
	}
	public void begin()
	{
		/**
		 * Start Bob's movement
		 */
		Bob.DOWN_SPEED=0.5f;
		setState(State.IDLE);
		this.velocity.y=-DOWN_SPEED;
		this.lives=3;
		this.meters=0;
		godmode(5,false); // start a god mode for 5 seconds

	}
	@Override
	public void reset()
	{
		/**
		 * reset Bob
		 */
		setState(State.PAUSED);
		this.accelerationMode=0;
		this.velocity.y=0;
		this.velocity.x=0;
		this.lives=3;
		this.meters=0;
		Bob.DOWN_SPEED=0.5f;
	}
	/** Getters */
	public Vector2 getPosition() {
		return position;
	}
	public Vector2 getVelocity() {
		return velocity;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	public Vector2 getAcceleration() {
		return acceleration;
	}
	/** Setters */
	public void setFacingLeft(boolean b) {
		facingLeft=b;
	}
	public void setFacingRight(boolean b) {
		facingRight=b;
	}
	public void setFacingDown(boolean b) {
		facingDown=b;
	}

	public void setState(State state_to_assign) {
		state=state_to_assign;

	}

	@Override
	public void update(float delta) {
		/**
		 * Update Bob
		 */

		if (state==State.PAUSED) // if Bob is paused do not update
			return;

		if (isGodMode())
			godmodeTime+=delta;

		jumpmodeTime+=delta;

		if (jumpmode&&jumpmodeTime>0.3)
		{
			/**
			 * After 0.3 seconds that bob goes back when he jumps over a platform
			 * let bob ski down in a god mode, with a god mode acceleration for 5 seconds.
			 */
			godmode(5,true); // start a god mode for 5 seconds
			jumpmodeTime=0; // reseting the jump conditions
			jumpmode=false; // reseting the jump conditions
			velocity.y=-GOD_SPEED_MULTIPLIER*DOWN_SPEED; // acceleration for the jump

		}

		if (godmodeTime>godmodeTimeRunning)
		{
			/**
			 * Checks if the god mode should be canceled.
			 */

			if (velocity.y<=-GOD_SPEED_MULTIPLIER*DOWN_SPEED)
				// if the velocity of bob is of jumping mode, then
				// set the velocity to the idle one.
				velocity.y=-DOWN_SPEED;
			jumpmodegodmode=false;

		}
		if (godmodeTime>godmodeTimeRunning+2)
		{
			/**
			 * Two seconds after the velocity returned to
			 * its idle state, we cancel the god mode.
			 * The reason is that we wish to give the player
			 * 2 seconds of god mode in a normal velocity after
			 * he returns from the jump. So if we jumped
			 * for 5 seconds (and being in a god mode while we jumped)
			 * we give the player another 2 seconds of god mode
			 * in an idle velocity rate.
			 */
			godmodeTime=0;
			godmode=false;
		}
		/**
		 * Updates the position according to the velocity.
		 */
		Vector2 tempPsition= new Vector2(position);
		tempPsition.add(velocity.cpy().scl(delta));
		if (!(tempPsition.x>4.7||tempPsition.x<0))
		{ // if the new position is not out of the limits
			if (tempPsition.y<position.y&&letMetersGrow)
			{ // update metrage
				meters+=1*velocity.y*-2;
			}
			position.add(velocity.cpy().scl(delta));
		}
		else
		{ // if the new position is out of the limits
			velocity.x=0;
			position.add(velocity.cpy().scl(delta));
		}
		stateTime += delta;
		if (stateTime-lastVelocityUpdateTime>10&&DOWN_SPEED<1.5f&&!isJumpMode())
		{ // updates the velocity every 10 seconds
			if (DOWN_SPEED<0.8f)
				accelerationMode=0;
			else if (DOWN_SPEED<1f)
				accelerationMode=1;
			else if (DOWN_SPEED<1.5f)
				accelerationMode=2;

			lastVelocityUpdateTime=stateTime;
			DOWN_SPEED+=0.05f;
			velocity.y=-DOWN_SPEED;

		}
	}
	public void hit()
	{
		/**
		 * This method is called when bob is being hit by an obstacle.
		 */
		if (!godmode)
		{ // if in god mode then do nothing

			if (Settings.soundEnabled) Assets.hitSound.play(1);
			velocity.x=0;
			velocity.y=0;
			setState(State.DYING);
			lives--;
			if (lives==0)
			{
				setState(State.DEAD);
			}
		}
	}
	public void jumpMode()
	{
		/**
		 * This method is called when bob should be jumping
		 */

		if (Settings.soundEnabled) Assets.highJumpSound.play(1);

		godmode=true; // when jumping bob can not be hit
		velocity.y=1.5f;
		jumpmodeTime=0;
		jumpmodegodmode=true;
		jumpmode=true;
	}
	public void godmode(float godmodeTimeToRun, boolean jumpmodegodmode) {
		/**
		 * This method is called when bob should be in a god mode.
		 * While in god mode, bob cannot be hit by obstacles.
		 * godmodeTimeToRun: num of seconds bob should be on god mode
		 * jumpmodegodmode: is this god mode is for jumping too
		 */
		godmode=true;
		godmodeTime=0;
		godmodeTimeRunning=godmodeTimeToRun;
		this.jumpmodegodmode=jumpmodegodmode;

	}
	public boolean isBobDead()
	{
		/**
		 * Returns true if bob is being hit or if bob is dead
		 */
		return (state==Bob.State.DYING||state==Bob.State.DEAD);
	}
	public boolean isGodMode ()
	{
		/**
		 * Returns true if bob is in god mode but not jumping
		 */
		return (godmode&&!jumpmode);
	}
	public boolean isJumpMode()
	{
		/**
		 * Returns true if bob is jumping
		 */
		return jumpmode||jumpmodegodmode;
	}
	public int getAccelerationMode()
	{
		/**
		 * Returns the acceleration rate of bob
		 */
		return accelerationMode;
	}
	public int getMeters ()
	{
		/**
		 * Returns the number of meters bob crossed until now
		 */
		return meters;
	}


}
