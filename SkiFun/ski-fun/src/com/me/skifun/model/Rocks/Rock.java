package com.me.skifun.model.Rocks;
import com.me.skifun.model.GameObject;

/**
 * Rock is one of the game's obstacles.
 */

public class Rock extends GameObject {
	public static  float HEIGHT = 0.4f; // half a unit
	public static  float WIDTH = 0.5f;
	public   float height = 0.4f; // half a unit
	public   float width = 0.5f;

    public int numberType;

    public Rock(int numberType)
    {
    	super(0,0,WIDTH,HEIGHT);
        this.bounds.height = height-0.2f;
        this.bounds.width = width-0.1f;
    	this.alive=false;
    	this.numberType=numberType;
    }

    public Rock(float x, float y, int numberType) {
		super(x, y, WIDTH, HEIGHT);
        this.bounds.height = height-0.2f;
        this.bounds.width = width-0.1f;
        this.alive=true;
    	this.numberType=numberType;

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
