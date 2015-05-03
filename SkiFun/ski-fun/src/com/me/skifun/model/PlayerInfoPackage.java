package com.me.skifun.model;
import com.badlogic.gdx.math.Vector2;

/**
 * PlayerInfoPackage is a set of information
 * used in the multiplayer version of ski fun in order
 * to transfer data between the players.
 */

public class PlayerInfoPackage {
	private final Vector2 position;
	private final float state;
	public boolean right; // is the player going right
	public boolean left; // is the player going left
	public boolean downSpeed; // is the player accelerating down
	public String playerName;
	public PlayerInfoPackage(Vector2 position, float state2, boolean right, boolean left, boolean downSpeed,String playerName)
	{
		this.position=position;
		this.state=state2;
		this.right=right;
		this.left=left;
		this.downSpeed=downSpeed;
		this.playerName=playerName;
	}
	public Vector2 getPosition()
	{
		return position;
	}
	public float getState()
	{
		return state;
	}
}
