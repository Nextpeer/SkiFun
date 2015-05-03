package com.me.skifun.model.Trees;

/**
 * Kind of a tree.
 */
public class PineHigh extends Tree {
    public  static float HEIGHT=0.9f;
    public  static float WIDTH=0.34f;
    public PineHigh()
    {
    	super(WIDTH,HEIGHT);
    }
    public PineHigh(float x, float y) {
		super(WIDTH,HEIGHT,x,y);
	}

}
