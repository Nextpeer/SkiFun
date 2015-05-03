package com.me.skifun.model;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.me.skifun.NextpeerPlugin;
import com.me.skifun.P2PMessage;
import com.me.skifun.P2PMessage.PlayerPosition;
import com.me.skifun.P2PMessage.PlayerScore;
import com.me.skifun.model.Bob.State;
import com.me.skifun.model.Rocks.Rock;
import com.me.skifun.model.Trees.PineHigh;
import com.me.skifun.model.Trees.PineLow;
import com.me.skifun.model.Trees.PineMid;
import com.me.skifun.model.Trees.Tree;
import com.nextpeer.libgdx.NextpeerTournamentCustomMessage;

/**
 * The main game's world. The total game's environment.
 */

public class World {
	/** The player **/
	Bob bob;
	/** Constants **/
	public static  float WORLD_WIDTH = 5;
	public static  float WORLD_HEIGHT = 7*200 ;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_GAME_OVER = 2;
	/** The world's state **/
	public int state;
	/** The world's bounds **/
	static final float BOUND_x = 5f;
	static final float BOUND_Y = 7f;

	/** ArrayLists and Pools of objects **/
	public final ArrayList<Tree> trees;
	public final Pool<Tree> treesPool;
	public final ArrayList<Rock> rocks;
	public final Pool<Rock> rocksPool;
	public final ArrayList<Platform> platforms;
	public final Pool<Platform> platformsPool;
	public final ArrayList<CableCar> cableCars;
	public final Pool<CableCar> cableCarsPool;
	public final ArrayList<LiftPole> poles;
	public final Pool<LiftPole> polesPool;
	public final ArrayList<Hill> hills=null;
	public final Pool<Hill> hillsPool=null;

	public final ArrayList<GameObject> objects;
	/** The position last updated **/
	public float lastUpdatePosition;
	/** Random class **/
	public final Random rand;
	/** The player's score **/
	public int score;
	/** How often to send data to the other players
	 * on the multiplayer game
	 */
	public float deltaUpdateInterval=0.2f;
	public float interval=0; // The timer used in this section.
	/** The score of the player when the last yeti showed up **/
	public double lastYeti=0;
	/** Maps of the opponents players **/
	public  Map<String, PlayerInfoPackage> opponents; // Contains the opponents positions at first
	public Map<String,PlayerInfoPackage> opponents2; // Contains the opponents positions after deltaUpdateInterval time
	/** Opponents flag. Used to know to which map we should insert the position. **/
	public int flagOp;
	/** notesString is a vector of strings which contains data of the player on the multiplayer mode
	 * being showed on the screen.
	 */
	public Vector<String> notesString;

	public float heartBeatTime=0;
	public boolean gameShouldBeOver=false;
	public float stateTime=0;
	public int lastMeters=0;

	/// 5 X 7 world

	public Bob getBob() {
		return bob;
	}

	public World() {
		/**
		 * Constructor
		 */
		createDemoWorld();
		opponents= new ConcurrentHashMap<String,PlayerInfoPackage>();
		opponents2= new ConcurrentHashMap<String,PlayerInfoPackage>();
		flagOp=0;
		notesString=new Vector<String>();
		objects= new ArrayList<GameObject>();
		this.trees=null;
		treesPool= new Pool<Tree>() {
			@Override
			protected Tree newObject() {
				int x= rand.nextInt(3);
				if (x==0)
					return new PineLow();
				else if (x==1)
					return new PineMid();
				else
					return new PineHigh();
			}
		};
		this.rocks=null;
		rocksPool= new Pool<Rock>() {
			@Override
			protected Rock newObject() {
				int x= rand.nextInt(3);
				return new Rock(x);
			}
		};
		this.platforms=null;
		platformsPool= new Pool<Platform>() {
			@Override
			protected Platform newObject() {
				return new Platform();
			}
		};
		this.cableCars=null;
		cableCarsPool= new Pool<CableCar>() {
			@Override
			protected CableCar newObject() {
				return new CableCar();
			}
		};
		this.poles=null;
		polesPool= new Pool<LiftPole>() {
			@Override
			protected LiftPole newObject() {
				return new LiftPole();
			}
		};

		if (NextpeerPlugin.isAvailable()) {
			rand= new Random(NextpeerPlugin.instance().lastKnownTournamentRandomSeed);
		}
		else
		{
			rand= new Random();
		}
		generateLevel(-4); // generate the world objects
		score=0;
	}


	private void createDemoWorld() {
		/**
		 * Create the world.
		 */
		float x;
		if (NextpeerPlugin.isCurrentlyInTournament())
		{ // if its a multiplayer mode then the player will be drawn on random position of the x axis
			Random randPositionX= new Random();
			x= randPositionX.nextInt(5)+0.5f;
		}
		else
		{ // else, it will be drawn on the middle of the screen
			x=2.5f;
		}
		bob = new Bob(new Vector2(x, 5f));
		PlayerPosition msg = new PlayerPosition(bob.getPosition().x,bob.getPosition().y,0);
		try{
			// Nextpeer has two different means to push data to the other players, reliable and unreliable.
			// Unreliable is faster, but you'll usually have around 1-5% message lost.
			// Reliable is slower, but each message that you'll send will get to the other players.
			// You should use the unreliable protocol for messages that your game can do without for a few frames. For example, player positions.
			// Use the reliable protocol for messages that your game cannot do without, for example information about the name of the player.
			NextpeerPlugin.unreliablePushDataToOtherPlayers(msg.encode());
		}
		catch(Exception e) {
			e.printStackTrace();
		}


	}

	public void update(float deltaTime, float accelerometerX) {
		/**
		 * Updates the game's world.
		 */
		interval+=deltaTime;
		heartBeatTime+=deltaTime;
		if (heartBeatTime>10 && NextpeerPlugin.isCurrentlyInTournament())
			gameShouldBeOver=true;
		if (!bob.isBobDead())
		{checkCollisions();}
		score=bob.getMeters();

		deleteObjects(false);
		updateObjects(deltaTime);
		if (!(Bob.state==Bob.State.DYING||Bob.state==Bob.State.DEAD))
		{
			bob.setState(State.SKIING);
			float vel=-accelerometerX/4*Bob.SPEED;
			if (Math.abs(vel)<0.05f)
				bob.velocity.x=0;
			else
			{
				if (stateTime<4)
					bob.velocity.x = -accelerometerX/5  * Bob.SPEED;
				else
					bob.velocity.x= -accelerometerX/4*Bob.SPEED;
			}
			if (bob.velocity.x>0.3f)
			{
				bob.setFacingRight(true);
				bob.setFacingLeft(false);

			}
			else if (bob.velocity.x<-0.3f)
			{
				bob.setFacingRight(false);
				bob.setFacingLeft(true);
			}
			else if (Math.abs(bob.velocity.x)<0.1f)
			{
				bob.setFacingRight(false);
				bob.setFacingLeft(false);
			}
		}
		updateOpponents(deltaTime);
		updateBob(deltaTime);
		stateTime+=deltaTime;

	}

	public void pause ()
	{
		deleteObjects(false);
	}
	public void updateOpponents (float delta)
	{
		/**
		 * Updating the Opponents data, in order to draw
		 * a vector between the first and last position of the opponents.
		 */
		Map<String,PlayerInfoPackage> map= opponents;
		Map<String,PlayerInfoPackage> map2= opponents2;
		for (Map.Entry<String, PlayerInfoPackage> entry : map.entrySet())
		{
			/**
			 * Iterates over the two maps entries
			 */
			for (Map.Entry<String,PlayerInfoPackage> entry2: map2.entrySet())
			{
				if (entry.getKey().equals(entry2.getKey())) // searching for the player who appears in both.
				{
					/**
					 * Get the location of the opponent
					 */
					float x1=entry.getValue().getPosition().x;
					float x2=entry2.getValue().getPosition().x;
					float y1=entry.getValue().getPosition().y;
					float y2=entry2.getValue().getPosition().y;
					/**
					 * Calculate the velocity of the opponent
					 */
					float deltaY=y2-y1;
					float speedY=deltaY/(deltaUpdateInterval);
					float deltaX=x2-x1;
					float speedX=deltaX/(deltaUpdateInterval);
					/**
					 * Booleans
					 */
					boolean right=false;
					boolean left=false;
					/*boolean downSpeed=false;
					if (Math.abs(speedY)>1.7f)
					{
						downSpeed=true;
					}*/
					if (x2>x1+0.1f)
					{
						right=true;
						left=false;
					}
					else if (x2<x1-0.1f)
					{
						right=false;
						left=true;
					}
					else
					{
						right=false; left=false;
					}

					Vector2 newPos= new Vector2(x1,y1); // The last position of the opponent
					Vector2 velocity= new Vector2(speedX,speedY); // The velocity of the opponent
					if (x1!=x2||y1!=y2)
					{
						newPos.add(velocity.cpy().scl(delta)); // update the position
					}
					/** Create an info package **/
					PlayerInfoPackage p;
					p= new PlayerInfoPackage(newPos, entry.getValue().getState(),right,left,false,entry.getValue().playerName);
					/** Put the updated info package in the first map **/
					opponents.put(entry.getKey(),p);

				}
			}
		}
	}
	public void updateObjects (float delta)
	{
		/**
		 * Update the game objects
		 */
		int len= objects.size();
		for (int i=len-1;i>=0;i--)
		{
			GameObject object= objects.get(i);
			object.update(delta);
			if (!object.alive)
			{
				objects.remove(i);
				if (object instanceof Tree)
				{
					treesPool.free((Tree) object);
				}
				else if (object instanceof Rock)
				{
					rocksPool.free((Rock) object);
				}
				else if (object instanceof Platform)
				{
					platformsPool.free((Platform) object);
				}
				else if (object instanceof LiftPole)
				{
					polesPool.free((LiftPole)object);
				}
				else if (object instanceof Hill)
				{
					hillsPool.free((Hill)object);
				}
				else if (object instanceof CableCar)
				{
					cableCarsPool.free((CableCar)object);
				}
			}
		}
	}

	public void updateBob(float delta)
	{
		/**
		 * Update the player
		 */
		bob.update(delta);
		if (bob.getPosition().y<=lastUpdatePosition-94)
		{ // check if we should generate new obstacles
			generateLevel(6);
			deleteObjects(false);

		}
		if (bob.meters-lastMeters>5000 && NextpeerPlugin.isCurrentlyInTournament())
		{
			/**
			 * Send info of the player's score every a few meters
			 */
			PlayerScore msg= new PlayerScore(bob.meters-bob.meters%1000);
			try{
				// Nextpeer has two different means to push data to the other players, reliable and unreliable.
				// Unreliable is faster, but you'll usually have around 1-5% message lost.
				// Reliable is slower, but each message that you'll send will get to the other players.
				// You should use the unreliable protocol for messages that your game can do without for a few frames. For example, player positions.
				// Use the reliable protocol for messages that your game cannot do without, for example information about the name of the player.
				NextpeerPlugin.unreliablePushDataToOtherPlayers(msg.encode());

			}
			catch(Exception e) {
				e.printStackTrace();
			}
			lastMeters=bob.meters;

		}

		float state1;
		if (Bob.state==Bob.State.DEAD)
			state1=2;
		else if (Bob.state==Bob.State.DYING)
			state1=1;
		else if (bob.isGodMode())
			state1=3;
		else
			state1=0;
		/**
		 * Send info of the player position every deltaUpdateInterval seconds.
		 */
		PlayerPosition msg = new PlayerPosition(bob.getPosition().x,bob.getPosition().y,state1);
		if (interval>=deltaUpdateInterval)
		{

			try{
				// Nextpeer has two different means to push data to the other players, reliable and unreliable.
				// Unreliable is faster, but you'll usually have around 1-5% message lost.
				// Reliable is slower, but each message that you'll send will get to the other players.
				// You should use the unreliable protocol for messages that your game can do without for a few frames. For example, player positions.
				// Use the reliable protocol for messages that your game cannot do without, for example information about the name of the player.
				NextpeerPlugin.unreliablePushDataToOtherPlayers(msg.encode());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			interval=0;
		}
		/**
		 * If bob is dead send an update
		 */
		if (Bob.state==Bob.State.DEAD)
		{
			try{
				// Nextpeer has two different means to push data to the other players, reliable and unreliable.
				// Unreliable is faster, but you'll usually have around 1-5% message lost.
				// Reliable is slower, but each message that you'll send will get to the other players.
				// You should use the unreliable protocol for messages that your game can do without for a few frames. For example, player positions.
				// Use the reliable protocol for messages that your game cannot do without, for example information about the name of the player.
				NextpeerPlugin.unreliablePushDataToOtherPlayers(msg.encode());
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			state=WORLD_STATE_GAME_OVER;
		}

	}
	private void deleteObjects (boolean all)
	{
		/**
		 * Delete unused objects
		 */
		int len= objects.size();
		for (int i=0;i<len;i++)
		{
			GameObject object= objects.get(i);
			if (all||bob.position.y<object.position.y-2.4f)
			{
				object.alive=false;
			}
		}
	}

	private void generateLevel (int p) {
		/**
		 * Generate the game's objects every P distance from the last bob update position
		 */
		generateBonuses(p);
		generateObstacles(p);
		lastUpdatePosition=bob.getPosition().y;
	}
	public void generateBonuses(int p)
	{
		/**
		 * Generate the game's bonuses
		 */
		generatePlatforms(p);
	}
	public void generatePlatforms(int p)
	{
		/**
		 * Generate the game's platforms
		 */

		float y = bob.position.y-p;
		while (y > bob.position.y - 100 && y<300) {
			float x = rand.nextFloat() * (WORLD_WIDTH - PineLow.WIDTH);
			Platform platform= platformsPool.obtain();
			platform.init(x,y);
			objects.add(platform);
			y -=  4.8f;
			y += rand.nextFloat() * (0.3);
		}
	}
	private void generateObstacles(int p)
	{
		/**
		 * Generate the game's obstacles
		 */

		generateRocks(p);
		generateTrees(p);
		generateCableCars(p);
		generateLiftPoles(p);

	}
	private void generateTrees (int p)
	{
		/**
		 * Generate the game's treess
		 */
		float y = bob.position.y-p;
		if (y>5.3f)
		{
			y=5.3f;
		}
		while (y > bob.position.y - 100) {
			float x1 = rand.nextFloat() * (WORLD_WIDTH - PineLow.WIDTH);// + Tree.SIZE / 2;
			float x2 = rand.nextFloat() * (WORLD_WIDTH - PineLow.WIDTH);// + Tree.SIZE / 2;
			Tree tree=treesPool.obtain();
			Tree tree2= treesPool.obtain();
			tree.init(x1,y);
			tree2.init(x2+0.2f,y);
			objects.add(tree); objects.add(tree2);
			y -=  1.9f;
			y += rand.nextFloat() * (0.3);
		}
	}
	private void generateRocks (int p)
	{
		/**
		 * Generate the rocks
		 */
		float y = bob.position.y-p;
		if (y>5.3f)
		{
			y=5.3f;
		}

		while (y > bob.position.y - 100) {
			float x = rand.nextFloat() * (WORLD_WIDTH - Rock.WIDTH);// + Tree.SIZE / 2;
			Rock rock=rocksPool.obtain();
			rock.init(x,y);
			objects.add(rock);
			//
			y -=  2.7f;
			y += rand.nextFloat() * (0.3);
		}
	}

	private void generateLiftPoles(int p)
	{
		/**
		 * Generate the lift poles
		 */
		float y = bob.position.y-p;
		if (y>5.3f)
		{
			y=5.3f;
		}

		while (y > bob.position.y - 100) {
			float x = rand.nextFloat() * (WORLD_WIDTH - LiftPole.WIDTH);
			LiftPole pole=polesPool.obtain();
			pole.init(x,y);
			objects.add(pole);
			y -=  7f;
			y += rand.nextFloat() * (0.3);
		}
	}

	/***/
	private void generateCableCars (int p)
	{
		/**
		 * Generate the cable cars
		 */
		float y = bob.position.y-p;
		if (y>5.3f)
		{
			y=5.3f;
		}

		while (y > bob.position.y - 100) {
			float x = rand.nextFloat() * (WORLD_WIDTH - CableCar.WIDTH);
			CableCar cable=cableCarsPool.obtain();
			cable.init(x,y);
			objects.add(cable);
			y -=  15f;
			y += rand.nextFloat() * (0.3);
		}

	}
	private void checkObjectsCollisions()
	{
		/**
		 * Check for collisions between bob and the game's obstacles
		 */
		int len =objects.size();
		Rectangle bobRect= new Rectangle(bob.position.x,bob.position.y,bob.bounds.width,bob.bounds.height);
		Rectangle objectRect= new Rectangle();
		for (int i=0;i<len;i++)
		{
			/**
			 * Iterate over the obstacles ArrayList
			 */

			GameObject object= objects.get(i);
			objectRect.x=object.position.x; objectRect.y=object.position.y; objectRect.width=object.bounds.width; objectRect.height=object.bounds.height;
			if (OverlapTester.overlapRectangles(bobRect,objectRect)) {
				{ // if there is a collision
					if (object instanceof Platform)
					{
						if (!bob.isJumpMode())
							bob.jumpMode();
						else
						{
							break;
						}
					}
					else
					{
						bob.hit();
					}
					break;
				}
			}
		}
	}
	private void checkCollisions()
	{
		/**
		 * Check for collisions
		 */
		checkObjectsCollisions();
	}

	public void onReceiveTournamentCustomMessage(NextpeerTournamentCustomMessage message) {

		// Try to decode the custom message
		P2PMessage msg = P2PMessage.tryToDecodeOrNull(message.customMessage);
		if (msg != null)
		{
			String name=message.playerName;
			if (name.length()>8)
			{ // shorten the name of the player
				name=name.substring(0,5);
				name+="..";
			}

			switch(msg.type)
			{
			case P2PMessage.Types.PLAYER_POSITION: {
				PlayerPosition positionMsg = (PlayerPosition)msg;
				Vector2 opponentPosition=new Vector2 (positionMsg.x,positionMsg.y);
				float state= positionMsg.state;
				/** Creates a pack according to the message **/
				PlayerInfoPackage pack= new PlayerInfoPackage(opponentPosition,state,false,false,false,message.playerName);
				if (opponents.get(message.playerId) == null)
					opponents.put(message.playerId,pack);
				else
				{
					if (opponents2.get(message.playerId)!=null)
					{
						Vector2 op=opponents2.get(message.playerId).getPosition();
						if (!(op.x==opponentPosition.x&&op.y==opponentPosition.y))	{

							if (state==1) // hit
							{

								notesString.add(name+" got hit\n");
							}
							else if (state==2) // dead
							{
								notesString.add(name+" died\n");

							}

						}
					}
					opponents2.put(message.playerId,pack);
				}
				heartBeatTime=0;
				flagOp=1;
			}
			break;
			case P2PMessage.Types.PLAYER_SCORE: {
				PlayerScore playerScore= (PlayerScore)msg;
				int score= playerScore.score;
				notesString.add(name+":"+score+" ps");
			}
			break;
			default:
				break;
			}
		}
	}

}
