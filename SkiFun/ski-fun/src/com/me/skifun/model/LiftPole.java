package com.me.skifun.model;

/**
 * LiftPole is one of the game's obstacles.
 */

public class LiftPole extends GameObject {
	public  static float HEIGHT = 0.5f;
	public  static float WIDTH = 0.3f;
	public   float height = 0.5f;
	public   float width = 0.3f;

    public LiftPole()
    {
    	super(0,0,WIDTH,HEIGHT);
        this.bounds.height = height-0.1f;
        this.bounds.width = width-0.2f;
    	this.alive=false;
    }

    public LiftPole(float x, float y) {
		super(x, y, WIDTH, HEIGHT);
        this.bounds.height = height-0.05f;
        this.bounds.width = width-0.2f;
        this.alive=true;
        width=WIDTH;
        height=HEIGHT;

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
