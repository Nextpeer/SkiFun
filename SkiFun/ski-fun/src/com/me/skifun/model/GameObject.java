package com.me.skifun.model;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * GameObject is a class represents an object of the ski fun world.
 */
public class GameObject implements Poolable {
	public final Vector2 position;
	public final Rectangle bounds;
	public boolean alive;
	public GameObject (float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle();
		this.alive=true;
	}
	@Override
	public void reset() {

	}
	public void update(float delta)
	{

	}
	public void init(float x,float y)
	{

	}

}
