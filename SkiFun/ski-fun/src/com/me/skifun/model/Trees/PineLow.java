package com.me.skifun.model.Trees;

/**
 * Kind of a tree.
 */
public class PineLow extends Tree {
    public  static float HEIGHT=0.8f;
    public  static float WIDTH=0.34f;

    public PineLow()
    {
    	super(WIDTH,HEIGHT);
    }
    public PineLow(float x, float y) {
		super(WIDTH,HEIGHT,x,y);
	}
}
