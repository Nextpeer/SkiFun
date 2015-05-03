package com.me.skifun.model.Trees;
import com.me.skifun.model.GameObject;

/**
 * Tree is one of the game's obstacles.
 */

public class Tree extends GameObject {
    public static  float HEIGHT;
    public static  float WIDTH;
    public   float height;
    public   float width;

    public Tree(float width,float height)
    {
    	super(0,0,width,height);
    	HEIGHT=height;
    	this.height=HEIGHT;
    	WIDTH=width;
    	this.width=WIDTH;
        this.bounds.height = HEIGHT-0.5f;
        this.bounds.width = WIDTH-0.1f;
    	this.alive=false;
    }
    public Tree(float width, float height, float x, float y) {
		super(x, y, width,height);
    	HEIGHT=height;
    	this.height=HEIGHT;
    	WIDTH=width;
    	this.width=WIDTH;

        this.bounds.height = HEIGHT-0.5f;
        this.bounds.width = WIDTH-0.1f;
        alive=true;

	}
    public void init(float posX, float posY) {
        position.set(posX,  posY);
        alive=true;
        width=WIDTH;
        height=HEIGHT;

    }

	public void update (float delta) {
		}
	@Override
	public void reset() {
        position.set(0,0);
        alive=false;

		
	}




}
