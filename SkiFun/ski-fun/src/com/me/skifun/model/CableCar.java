package com.me.skifun.model;

/**
 * CableCar is an a dynamic game object.
 * The class represents an obstacle in the ski fun world.
 */

public class CableCar extends DynamicGameObject {
	public static float WIDTH = 0.5f;
	public static float HEIGHT=0.35f;
	public  float width = 0.5f;
	public  float height=0.35f;
	public static final float VELOCITY = 0.5f;

	public CableCar() {
		super(0,0,WIDTH,HEIGHT);
		this.bounds.height = height-0.1f;
		this.bounds.width = width-0.18f;
		this.alive=false;
		velocity.y = VELOCITY;
	}
	public CableCar(float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		this.bounds.height = height-0.1f;
		this.bounds.width = width-0.18f;
		alive=true;
		velocity.y = VELOCITY;
	}
	@Override
	public void init(float posX, float posY) {
		position.set(posX,  posY);
		alive=true;
		width=WIDTH;
		height=HEIGHT;

	}
	@Override
	public void reset() {
		position.set(0,0);
		alive=false;
	}

	@Override
	public void update (float delta) {
		position.add(0, velocity.y * delta);
	}

}
