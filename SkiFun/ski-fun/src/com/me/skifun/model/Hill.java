package com.me.skifun.model;

/**
 * Hill is one the game's obstacles.
 */

public class Hill extends GameObject {
	public static  float HEIGHT = 0.2f; 
	public static  float WIDTH = 0.4f;
	public   float height = 0.2f;
	public   float width = 0.4f;

    public Hill() {
    	super(0,0,WIDTH,HEIGHT);
        this.bounds.height = height;
        this.bounds.width = width;
    	this.alive=false;

    }
    public Hill(float x, float y) {
		super(x, y, WIDTH, HEIGHT);
        this.bounds.height = height;
        this.bounds.width = width;
        alive=true;

	}
    public void init(float posX, float posY) {
        position.set(posX,  posY);
        alive=true;
        height=HEIGHT;
        width=WIDTH;

    }
	public void reset() {
        position.set(0,0);
        alive=false;

		
	}

	public void update (float delta) {
	}




}
