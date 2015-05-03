package com.me.skifun.view;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.skifun.SkiFun;
import com.me.skifun.model.Bob;
import com.me.skifun.model.CableCar;
import com.me.skifun.model.GameObject;
import com.me.skifun.model.Hill;
import com.me.skifun.model.LiftPole;
import com.me.skifun.model.Platform;
import com.me.skifun.model.PlayerInfoPackage;
import com.me.skifun.model.TextWrapper;
import com.me.skifun.model.World;
import com.me.skifun.model.Rocks.Rock;
import com.me.skifun.model.Trees.PineHigh;
import com.me.skifun.model.Trees.PineLow;
import com.me.skifun.model.Trees.PineMid;
import com.me.skifun.model.Trees.Tree;

/**
 * WorldRenderer is the renderer class of the Game's world.
 */
public class WorldRenderer {

	/** Constants **/
	private static final float CAMERA_WIDTH = 5f;
	private static final float CAMERA_HEIGHT = 7f;
	private final World world;
	private final OrthographicCamera cam;
	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();
	/** Textures **/
	private Texture bobTexture;
	private final BitmapFont textOverPlayers;
	private final SpriteBatch spriteBatch;

	public void setSize (int w, int h) {
		int height = h;
		float ppuY = height / CAMERA_HEIGHT;
		float scale= 1/ ppuY;
		scale*=0.4f;
		textOverPlayers.setScale(scale);
	}

	public WorldRenderer(World world,SkiFun game, boolean debug) {
		this.world = world;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.spriteBatch=game.batcher;
		textOverPlayers=game.textOverPlayers;
		textOverPlayers.setUseIntegerPositions(false);
		textOverPlayers.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bobTexture= new Texture(Gdx.files.internal("data/Tiles/bob/ski_down.png"));

		loadTextures();
	}

	private void loadTextures() {
		Bob bob=world.getBob();
		bobTexture=Assets.bob;
		switch (Bob.state)
		{
		case DEAD:
			bobTexture=Assets.bob_dead;
			break;
		case DYING:
			bobTexture=Assets.bob_died;
			break;
		case IDLE:
			bobTexture=Assets.bob;
			break;
		case JUMPING:
			bobTexture=Assets.bob_jump;
			break;
		case SKIING:
			if (bob.facingDown)
			{
				if (bob.facingLeft)
				{
					bobTexture=Assets.bob_dleft;
				}
				else if (bob.facingRight)
				{
					bobTexture=Assets.bob_dright;
				}
				else
				{
					bobTexture=Assets.bob;
				}
			}
			else if (bob.facingLeft)
			{
				bobTexture=Assets.bob_left;
			}
			else if (bob.facingRight)
			{
				bobTexture=Assets.bob_right;
			}
			break;
		default: {
			break;
		}
		}
	}

	public void render() {
		if (world.getBob().getPosition().y < cam.position.y)
		{

			cam.position.y = world.getBob().getPosition().y;
		}
		cam.update();
		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.enableBlending();
		spriteBatch.begin();
		renderObjects();
		renderOpponents();
		drawBob();
		spriteBatch.end();
	}

	public void renderObstacles()
	{
		renderTrees();
		renderRocks();
		renderLiftPoles();
		renderCableCars();

	}
	public void renderObjects()
	{
		int len= world.objects.size();
		for (int i=0;i<len;i++)
		{
			GameObject object= world.objects.get(i);
			if (object instanceof Tree)
			{
				renderTree((Tree) object);
			}
			else if (object instanceof Rock)
			{
				renderRock((Rock) object);
			}
			else if (object instanceof Platform)
			{
				renderPlatform((Platform) object);
			}
			else if (object instanceof LiftPole)
			{
				renderLiftPole((LiftPole) object);
			}
			else if (object instanceof CableCar)
			{
				renderCableCar((CableCar) object);
			}
		}
	}
	public void renderTree(Tree tree)
	{
		TextureRegion keyFrame;
		if (tree instanceof PineLow)
		{
			keyFrame=Assets.pineGreenLow;
		}
		else if (tree instanceof PineMid)
		{
			keyFrame=Assets.pineGreenMid;

		}
		else if (tree instanceof PineHigh)
		{
			keyFrame=Assets.pineGreenHigh;
		}
		else
			keyFrame=Assets.pineGreenLow;
		if (tree.position.y-world.getBob().getPosition().y>2.3f)
		{
			tree.width/=1.2f;
			tree.height/=1.2f;
		}
		spriteBatch.draw(keyFrame, tree.position.x -0.05f , tree.position.y-0.1f ,tree.width, tree.height);

	}
	public void renderTrees ()
	{
		int len = world.trees.size();
		for (int i = 0; i < len; i++) {
			Tree tree = world.trees.get(i);
			TextureRegion keyFrame;
			if (tree instanceof PineLow)
			{
				keyFrame=Assets.pineGreenLow;
			}
			else if (tree instanceof PineMid)
			{
				keyFrame=Assets.pineGreenMid;

			}
			else if (tree instanceof PineHigh)
			{
				keyFrame=Assets.pineGreenHigh;
			}
			else
				keyFrame=Assets.pineGreenLow;
			if (tree.position.y-world.getBob().getPosition().y>2.3f)
			{
				tree.width/=1.2f;
				tree.height/=1.2f;
			}
			spriteBatch.draw(keyFrame, tree.position.x -0.05f , tree.position.y-0.1f ,tree.width, tree.height);
		}

	}
	public void renderRock(Rock rock)
	{
		TextureRegion keyFrame;
		switch (rock.numberType)
		{
		case 0:
			keyFrame=Assets.rockSnow_1;
			break;
		case 1:
			keyFrame=Assets.rockSnow_2;
			break;
		case 2:
			keyFrame=Assets.rockSnow_3;
			break;
		default:
			keyFrame=Assets.rockSnow_1;
			break;
		}
		if (rock.position.y-world.getBob().getPosition().y>2.3f)
		{
			rock.width/=1.2f;
			rock.height/=1.2f;
		}

		spriteBatch.draw(keyFrame, rock.position.x-0.03f , rock.position.y-0.05f ,rock.width, rock.height);

	}
	public void renderRocks ()
	{
		int len = world.rocks.size();
		for (int i = 0; i < len; i++) {
			Rock rock = world.rocks.get(i);
			TextureRegion keyFrame;
			switch (rock.numberType)
			{
			case 0:
				keyFrame=Assets.rockSnow_1;
				break;
			case 1:
				keyFrame=Assets.rockSnow_2;
				break;
			case 2:
				keyFrame=Assets.rockSnow_3;
				break;
			default:
				keyFrame=Assets.rockSnow_1;
				break;
			}
			if (rock.position.y-world.getBob().getPosition().y>2.3f)
			{
				rock.width/=1.2f;
				rock.height/=1.2f;
			}

			spriteBatch.draw(keyFrame, rock.position.x-0.03f , rock.position.y-0.05f ,rock.width, rock.height);
		}

	}

	public void renderHill (Hill hill)
	{
		TextureRegion keyFrame = Assets.hillSnow;
		if (hill.position.y-world.getBob().getPosition().y>2.3f)
		{
			hill.width/=1.2f;
			hill.height/=1.2f;
		}
		spriteBatch.draw(keyFrame, hill.position.x , hill.position.y ,hill.width, hill.height);

	}
	public void renderHills ()
	{
		int len = world.hills.size();
		for (int i = 0; i < len; i++) {
			Hill hill= world.hills.get(i);
			TextureRegion keyFrame = Assets.hillSnow;
			if (hill.position.y-world.getBob().getPosition().y>2.3f)
			{
				hill.width/=1.2f;
				hill.height/=1.2f;
			}
			spriteBatch.draw(keyFrame, hill.position.x , hill.position.y ,hill.width, hill.height);
		}

	}

	/***/
	public void renderLiftPole(LiftPole pole)
	{
		Texture keyFrame = Assets.pole;
		if (pole.position.y-world.getBob().getPosition().y>2.3f)
		{
			pole.width/=1.2f;
			pole.height/=1.2f;
		}

		spriteBatch.draw(keyFrame, pole.position.x-0.07f , pole.position.y ,pole.width, pole.height);

	}
	public void renderLiftPoles ()
	{
		int len = world.poles.size();
		for (int i = 0; i < len; i++) {
			LiftPole pole= world.poles.get(i);
			Texture keyFrame = Assets.pole;
			if (pole.position.y-world.getBob().getPosition().y>2.3f)
			{
				pole.width/=1.2f;
				pole.height/=1.2f;
			}

			spriteBatch.draw(keyFrame, pole.position.x-0.07f , pole.position.y ,pole.width, pole.height);
		}

	}
	public void renderCableCar(CableCar cable)
	{
		Texture keyFrame = Assets.cable;
		if (cable.position.y-world.getBob().getPosition().y>2.3f)
		{
			cable.width/=1.2f;
			cable.height/=1.2f;
		}
		spriteBatch.draw(keyFrame, cable.position.x-0.08f , cable.position.y ,cable.width, cable.height);

	}
	public void renderCableCars()
	{
		int len = world.cableCars.size();
		for (int i = 0; i < len; i++) {
			CableCar cable= world.cableCars.get(i);
			Texture keyFrame = Assets.cable;
			if (cable.position.y-world.getBob().getPosition().y>2.3f)
			{
				cable.width/=1.2f;
				cable.height/=1.2f;
			}
			spriteBatch.draw(keyFrame, cable.position.x-0.08f , cable.position.y ,cable.width, cable.height);
		}

	}

	public void renderBonuses()
	{
		renderPlatforms();
	}
	public void renderPlatform (Platform plat)
	{
		Texture keyFrame = Assets.platform;
		if (plat.position.y-world.getBob().getPosition().y>2f)
		{
			plat.width/=1.2f;
			plat.height/=1.2f;
		}

		spriteBatch.draw(keyFrame, plat.position.x-0.05f , plat.position.y-0.1f , plat.width+0.15f, plat.height+0.05f);

	}
	public void renderPlatforms()
	{
		int len = world.platforms.size();
		for (int i = 0; i < len; i++) {
			Platform plat = world.platforms.get(i);
			Texture keyFrame = Assets.platform;
			if (plat.position.y-world.getBob().getPosition().y>2f)
			{
				plat.width/=1.2f;
				plat.height/=1.2f;
			}

			spriteBatch.draw(keyFrame, plat.position.x-0.05f , plat.position.y-0.1f , plat.width+0.15f, plat.height+0.05f);
		}

	}
	private void renderOpponents()
	{

		Map<String,PlayerInfoPackage> map= world.opponents;
		Map<String,PlayerInfoPackage> map2=world.opponents2;
		for (Map.Entry<String, PlayerInfoPackage> entry : map.entrySet())
		{
			/**
			 * Iterates over the opponents
			 */
			for (Map.Entry<String,PlayerInfoPackage> entry2: map2.entrySet())
			{
				if (entry!=null&&entry2!=null&&entry.getKey().equals(entry2.getKey()))
				{
					float x1=entry.getValue().getPosition().x;
					float y1=entry.getValue().getPosition().y;
					if (entry2.getValue().getState()==1) // hit
					{
						spriteBatch.draw(Assets.bob_died_o,x1-0.1f,
								y1,Bob.SIZE,Bob.SIZE);
					}
					else if (entry2.getValue().getState()==2)
					{
						spriteBatch.draw(Assets.bob_dead,x1-0.1f,
								y1,Bob.SIZE,Bob.SIZE);

					}
					else
					{
						boolean right=entry.getValue().right;
						boolean left= entry.getValue().left;
						boolean downSpeed=entry.getValue().downSpeed;
						if (right)
						{
							if (downSpeed)
							{
								spriteBatch.draw(Assets.bob_dright_o,x1-0.1f,
										y1,Bob.SIZE,Bob.SIZE);

							}
							else
							{
								spriteBatch.draw(Assets.bob_right_o,x1-0.1f,
										y1,Bob.SIZE,Bob.SIZE);

							}
						}
						else if (left)
						{
							if (downSpeed)
							{
								spriteBatch.draw(Assets.bob_dleft_o,x1-0.1f,
										y1,Bob.SIZE,Bob.SIZE);

							}
							else
							{
								spriteBatch.draw(Assets.bob_left_o,x1-0.1f,
										y1,Bob.SIZE,Bob.SIZE);

							}
						}
						else
						{
							spriteBatch.draw(Assets.bob_o,x1-0.1f,
									y1,Bob.SIZE,Bob.SIZE);

						}
					}
					String name=entry.getValue().playerName;
					if (name.length()>8)
					{ // shorten the name
						name=name.substring(0,5);
						name+="..";
					}
					if (y1<world.getBob().getPosition().y-5f)
					{
						/**
						 * If the opponent is too far from bob and is not shown on screen
						 * draw an indication for its distance from bob
						 */
						TextWrapper text= new TextWrapper(name,new Vector2(x1-0.05f,world.getBob().getPosition().y-2.5f));
						float diff= world.getBob().getPosition().y-y1;
						diff=(int)(diff*1000);
						diff=diff/1000;
						TextWrapper text2=new TextWrapper(Float.toString(diff),new Vector2(x1-0.05f,world.getBob().getPosition().y-2.8f));

						text.draw(spriteBatch,textOverPlayers);
						text2.draw(spriteBatch,textOverPlayers);

					}
					else
					{
						/**
						 * Draw the name of the opponent over his texture
						 */
						TextWrapper text= new TextWrapper(name,new Vector2(x1-0.05f,y1+0.7f));
						text.draw(spriteBatch,textOverPlayers);
					}
				}
			}
		}

	}
	private void drawBob() {
		Bob bob = world.getBob();
		loadTextures();
		spriteBatch.draw(bobTexture, bob.getPosition().x-0.1f , bob.getPosition().y , Bob.SIZE , Bob.SIZE );
	}
	public void dispose()
	{
	}

	private void drawDebug() {
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);
		// render Bob
		Bob bob = world.getBob();
		Rectangle rect = bob.getBounds();
		float x1 = bob.getPosition().x + rect.x;
		float y1 = bob.getPosition().y + rect.y;
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(x1, y1, rect.width, rect.height);
		int len = world.objects.size();
		for (int i=0;i<len;i++)
		{
			GameObject object=world.objects.get(i);
			Rectangle rectObj= object.bounds;
			float xx1=object.position.x+rectObj.x;
			float yy1=object.position.y+rectObj.y;
			debugRenderer.setColor(new Color(0, 1, 0, 1));
			debugRenderer.rect(xx1, yy1, rectObj.width, rectObj.height);

		}
		debugRenderer.end();
	}}