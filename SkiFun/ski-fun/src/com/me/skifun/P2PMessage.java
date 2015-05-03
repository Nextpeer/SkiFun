package com.me.skifun;
import java.nio.ByteBuffer;


/*  P2P (Player to Player) message used for sending messages over the wire.
 * 
 *  Right now the protocol demonstrates a way to pack the height position of each player,
 *  but you can extend it to send information about the player position, velocity and acceleration.
 */

public abstract class P2PMessage {
	
	// General protocol constants
	private static class Constants {
		private static final byte INT_SIZE = 4;
		private static final byte FLOAT_SIZE = 4; 
	}
	
	// Available message types
	public static class Types {
		public static final byte PLAYER_POSITION = 1; 
		public static final byte PLAYER_SCORE=3;
	}
	
	// The type of the message
	public final byte type;
	
	// CTOR, initialize the message with its type
	private P2PMessage(byte type) {
		this.type = type;
	}
	
	// Try to decode the given message byte array into a P2PMessage.
	// @return null in case decode fail (invalid version, invalid size), Otherwise returns the P2PMessage object
	public static P2PMessage tryToDecodeOrNull(byte[] message) {
		if (message == null) return null;
		int msgType=ByteBuffer.wrap(message).getInt(0);
		P2PMessage decodedMessage = null;
		
		// Decode the message by its type if you can
		// Kind of Factory if you will.
		switch (msgType) {
			case Types.PLAYER_POSITION : {
				decodedMessage = PlayerPosition.tryToDecodeFromDataOrNull(message);
				break;
			}
			case Types.PLAYER_SCORE: {
				decodedMessage=PlayerScore.tryToDecodeFromDataOrNull(message);
			}
			default:
				break;
		}
		
		return decodedMessage;
	}
	
	// [byte][byte][int][*]
	// [version][type][data length][data]
	public byte[] encode() {
		byte[] data = getData();
		return data;
	}
	
	// Abstract. Get the data of the subclass's members as byte array
	protected abstract byte[] getData();
	
	/*
	 *  P2P Messages
	 */
	
	/*
	 * Player movement message
	 */
	public static class PlayerScore extends P2PMessage{
		public final int score;
		public PlayerScore(int score)
		{
			super(Types.PLAYER_SCORE);
			this.score=score;
		}
		protected byte[] getData()
		{
			return ByteBuffer.allocate(12).putInt(this.type).putInt(this.score).array();
		}
		public static PlayerScore tryToDecodeFromDataOrNull(byte [] data)
		{
			if (data==null)
			{
				return null;
			}
			int dataLength=data.length;
			if (dataLength <  Constants.INT_SIZE)
			{
				//System.out.println("data is too small for me dude");
				return null;
			}
			int score = ByteBuffer.wrap(data).getInt(4);
			return new PlayerScore(score);

		}
	}
	public static class PlayerPosition extends P2PMessage {
		public final float x;
		public final float y;
		public final float state; // 0- alive 1- hit 2- dead 
				
		// CTOR - initialize the player height message with heightSoFar
		public PlayerPosition(float x, float y, float state) {
			super(Types.PLAYER_POSITION);

			this.x = x;
			this.y= y;
			this.state=state;
		}


		@Override
		// [float]
		// [heightSoFar]
		protected byte[] getData() {
					
			return ByteBuffer.allocate(70).putInt(this.type).putFloat(this.x).putFloat(this.y).putFloat(this.state).array();
		}
		
		// Decode the given data array into a valid message.
		// Return null in case the data length is invalid.
		public static PlayerPosition tryToDecodeFromDataOrNull(byte[] data) {

			if (data == null)
				{
					System.out.println("data is null, its not my fault");
					return null;
				}
			
			int dataLength = data.length;
			
			// The minimum data type 
			if (dataLength <  Constants.FLOAT_SIZE)
				{
					System.out.println("data is too small for me dude");
					return null;
				}

			// Position
			float x = ByteBuffer.wrap(data).getFloat(4);
			float y = ByteBuffer.wrap(data).getFloat(8);
			float state=ByteBuffer.wrap(data).getFloat(12);
			return new PlayerPosition(x,y,state);
		}
	}
}
