package com.me.skifun.model;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextWrapper{
	  private String text;
	  private Vector2 position;
	  private int width;
	  private int height;

	  public TextWrapper(String text,Vector2 position){
	      this.text = text;
	      this.position = position;
	  }

	  public void draw(SpriteBatch sp,BitmapFont fnt){
	      width=(int)fnt.getBounds(text).width; //Get the width of the text we draw using the current font
	      height=(int)fnt.getBounds(text).height; //Get the height of the text we draw using the current font
	      fnt.draw(sp,text,position.x-width/2, // Get center value in x direction
	              position.y+height/2 //Get center value in y direction
	              );
	  }

	  public String getText() {
	      return text;
	  }

	  public void setText(String text) {
	      this.text = text;
	  }

	  public Vector2 getPosition() {
	      return position;
	  }

	  public void setPosition(Vector2 position) {
	      this.position = position;
	  }

	  public int getWidth() {
	      return width;
	  }

	  public int getHeight() {
	      return height;
	  }

	}