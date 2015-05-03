package com.me.skifun.model;

/**
 * Platform is one of the game's obstacles.
 */

public class Platform extends GameObject {
	public static float WIDTH = 0.15f;
	public static float HEIGHT=0.05f;
	public  float width = 0.15f;
	public  float height=0.05f;
    public Platform() {
    	super(0,0,WIDTH,HEIGHT);
        this.bounds.height = height;
        this.bounds.width = width;
    	this.alive=false;

    }
    public Platform(float x, float y) {
		super(x, y, WIDTH, HEIGHT);
        this.bounds.height = height;
        this.bounds.width = width;
        alive=true;
	}
    public void init(float posX, float posY) {
        position.set(posX,  posY);
        width=WIDTH;
        height=HEIGHT;
        alive=true;
    }
	public void reset() {
        position.set(0,0);
        alive=false;

		
	}

	public void update (float delta) {
		}




}
