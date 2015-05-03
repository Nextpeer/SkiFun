package com.me.skifun.model.Trees;

/**
 * Kind of a tree.
 */
public class PineMid extends Tree{
    public final static  float HEIGHT=0.8f;
    public final static  float WIDTH=0.34f;

    public PineMid()
    {
    	super(WIDTH,HEIGHT);
    }
    public PineMid(float x, float y) {
		super(WIDTH,HEIGHT,x,y);
	}

}
