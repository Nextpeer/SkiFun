package com.me.skifun.view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.skifun.model.Animation;

/**
 * Assets class. Contains all the textures, fonts, etc.
 */
public class Assets {
	public static Texture background;
	/*
	 * BOB TEXTURES */
	public static Texture bob;
	public static Texture bob_down;
	public static Texture bob_left;
	public static Texture bob_right;
	public static Texture bob_dright;
	public static Texture bob_dleft;
	public static Texture bob_o;
	public static Texture bob_left_o;
	public static Texture bob_right_o;
	public static Texture bob_dright_o;
	public static Texture bob_dleft_o;
	public static Texture bob_died_o;

	public static Animation bob_g;
	public static Animation bob_left_g;
	public static Animation bob_right_g;
	public static Animation bob_dright_g;
	public static Animation bob_dleft_g;
	public static Texture bob_died;
	public static Texture bob_dead;
	public static Texture bob_sit;
	public static Texture bob_jump;
	/*
	 * TREES TEXTURES
	 */
	public static TextureRegion pineGreenLow;
	public static TextureRegion pineGreenMid;
	public static TextureRegion pineGreenHigh;

	/*
	 * ROCKS TEXTURES
	 */
	public static TextureRegion rockSnow_2;
	public static TextureRegion rockSnow_1;
	public static TextureRegion rockSnow_3;

	/*
	 * PLATFORMS
	 */
	public static Texture platform;
	/*
	 * CABLE CARS
	 */
	public static Texture cable;
	public static Texture pole;

	/*******/
	/*
	 * HILLS
	 */
	public static TextureRegion hillSnow;
	public static TextureRegion largeHill;
	/*
	 * GAME SCREENS
	 */
	public static Texture nHighscore;
	public static Animation newHighscore;
	public static Texture items;
	public static Texture score;
	public static TextureRegion mainMenu;
	public static TextureRegion pauseMenu;
	public static TextureRegion backgroundRegion;
	public static TextureRegion gameOver;
	public static TextureRegion highScoresRegion;
	public static TextureRegion creditsScreen;
	/*
	 * TUTORIAL
	 */
	public static Texture tilt;
	public static Texture touchTheScreen;
	public static Texture takePlatform;
	public static Texture dontCrash;
	public static Texture havefun;

	/*
	 * BUTTONS
	 */
	public static Texture play;
	public static Texture about;
	public static Texture training;
	public static Texture [] lives;
	public static Texture infLives;
	public static TextureRegion pause;
	public static TextureRegion nothing;
	public static TextureRegion logo;
	public static Texture soundOn;
	public static Texture soundOff;
	public static Texture devCredits;
	public static Texture musicCredits;

	/*
	 * OTHER
	 */
	public static Music music;
	public static Sound highJumpSound;
	public static Sound hitSound;
	public static Sound clickSound;
	public static BitmapFont font;
	public static BitmapFont font2;
	public static TextureRegion ready;
	public static boolean musicOn=false;


	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load()
	{
		//	Texture.setEnforcePotImages(false);

		items = loadTexture("data/items.png");
		nothing=new TextureRegion(items,50,50,0,0);
		backgroundRegion=new TextureRegion(new Texture(Gdx.files.internal("data/screen_main.png")), 0, 0, 720, 1280);
		creditsScreen = new TextureRegion(new Texture(Gdx.files.internal("data/screen_credits.png")),0,0,720,1280);
		/*
		 * BOB
		 */
		bob= new Texture(Gdx.files.internal("data/Tiles/bob/ski_down.png"));
		bob_g = new Animation (0.5f, new TextureRegion (bob,0,0,32,32), nothing);
		bob_dleft= new Texture(Gdx.files.internal("data/Tiles/bob/ski_dleft.png"));
		bob_dleft_g = new Animation (0.5f, new TextureRegion (bob_dleft,0,0,32,32), nothing);
		bob_dright= new Texture(Gdx.files.internal("data/Tiles/bob/ski_dright.png"));
		bob_dright_g = new Animation (0.5f, new TextureRegion (bob_dright,0,0,32,32), nothing);
		bob_left= new Texture(Gdx.files.internal("data/Tiles/bob/ski_left.png"));
		bob_left_g = new Animation (0.5f, new TextureRegion (bob_left,0,0,32,32), nothing);
		bob_right= new Texture(Gdx.files.internal("data/Tiles/bob/ski_right.png"));
		bob_right_g = new Animation (0.5f, new TextureRegion (bob_right,0,0,32,32), nothing);
		bob_down= new Texture(Gdx.files.internal("data/Tiles/bob/ski_down.png"));
		bob_died=new Texture(Gdx.files.internal("data/Tiles/bob/ski_crash.png"));
		bob_sit=new Texture(Gdx.files.internal("data/Tiles/bob/ski_sit.png"));
		bob_jump= new Texture(Gdx.files.internal("data/Tiles/bob/ski_jump.png"));
		bob_dead= new Texture(Gdx.files.internal("data/Tiles/bob/ski_sit.png"));

		bob_o= new Texture(Gdx.files.internal("data/ski_down_o.png"));
		bob_left_o= new Texture(Gdx.files.internal("data/ski_left_o.png"));
		bob_right_o= new Texture(Gdx.files.internal("data/ski_right_o.png"));
		bob_died_o= new Texture(Gdx.files.internal("data/ski_crash_o.png"));
		bob_dleft_o= new Texture(Gdx.files.internal("data/ski_dleft_o.png"));
		bob_dright_o= new Texture(Gdx.files.internal("data/ski_dright_o.png"));

		/*
		 * TREES
		 */
		pineGreenLow= new TextureRegion(new Texture(Gdx.files.internal("data/Tiles/Trees/pineGreen_low.png")),0,0,32,79);
		pineGreenMid= new TextureRegion(new Texture(Gdx.files.internal("data/Tiles/Trees/pineGreen_mid.png")),0,0,32,110);
		pineGreenHigh= new TextureRegion(new Texture(Gdx.files.internal("data/Tiles/Trees/pineGreen_high.png")),0,0,32,110);

		/*
		 * 
		 * ROCKS
		 */
		rockSnow_1= new TextureRegion(new Texture(Gdx.files.internal("data/Tiles/Rocks/rockSnow_1.png")),0,0,64,69);
		rockSnow_2= new TextureRegion(new Texture(Gdx.files.internal("data/Tiles/Rocks/rockSnow_2.png")),0,0,64,69);
		rockSnow_3= new TextureRegion(new Texture(Gdx.files.internal("data/Tiles/Rocks/rockSnow_3.png")),0,0,64,69);

		/*
		 * 
		 * PLATFORMS
		 */
		platform = new Texture(Gdx.files.internal("data/obs_wall.png"));
		/*
		 * CABLE CARS
		 */
		cable= new Texture(Gdx.files.internal("data/obs_liftempty.png"));
		pole= new Texture(Gdx.files.internal("data/obs_another_tree.png"));

		/*
		 * HILLS
		 */
		largeHill= new TextureRegion(new Texture(Gdx.files.internal("data/Tiles/Hills/largeHill.png")),0,0,1011,367);
		hillSnow= new TextureRegion (new Texture(Gdx.files.internal("data/Tiles/Hills/hillSnow.png")),0,0,37,16);

		/*
		 * GAME SCREENS
		 */
		gameOver= new TextureRegion(new Texture(Gdx.files.internal("data/game_over_title.png")));
		highScoresRegion = new TextureRegion(items, 0, 262, 300, 110 / 3);
		nHighscore= new Texture(Gdx.files.internal("data/high_score_title.png"));
		newHighscore= new Animation(0.7f, new TextureRegion(new Texture(Gdx.files.internal("data/high_score_title.png"))), nothing);
		ready = new TextureRegion(items, 320, 224, 192, 32);
		lives= new Texture [3];
		infLives= new Texture(Gdx.files.internal("data/lifeinf.png"));
		lives[2]= new Texture(Gdx.files.internal("data/life3.png"));
		lives[1]= new Texture(Gdx.files.internal("data/life2.png"));
		lives[0]= new Texture(Gdx.files.internal("data/life1.png"));
		about= new Texture(Gdx.files.internal("data/main_menu_about_button.png"));
		play= new Texture(Gdx.files.internal("data/main_menu_play_button.png"));
		training= new Texture(Gdx.files.internal("data/main_menu_training_button.png"));

		score= new Texture(Gdx.files.internal("data/score.png"));
		pause= new TextureRegion(new Texture(Gdx.files.internal("data/pause.png")),0,0,512,256);
		logo = new TextureRegion(items, 0, 352, 274, 142);
		mainMenu = new TextureRegion(items, 0, 224, 300, 110);
		pauseMenu = new TextureRegion(new Texture(Gdx.files.internal("data/pause_menu.png")), 0, 0, 320, 172);
		devCredits= new Texture (Gdx.files.internal("data/credit.png"));
		musicCredits=new Texture(Gdx.files.internal("data/music_credit.png"));

		/*
		 * FONTS
		 */
		font = new BitmapFont(Gdx.files.internal("data/font3.fnt"), Gdx.files.internal("data/font3.png"), false);
		font2= new BitmapFont(Gdx.files.internal("data/hobo.fnt"), Gdx.files.internal("data/hobo.png"),false);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.setScale(.4f);
		font2.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font2.setScale(.25f);

		/*
		 * Music
		 */
		soundOn= new Texture(Gdx.files.internal("data/menu_button_sound_on.png"));
		soundOff=new Texture(Gdx.files.internal("data/menu_button_sound_off.png"));

		highJumpSound= Gdx.audio.newSound(Gdx.files.internal("data/Audio/highjumpSoundEffect.wav"));
		clickSound= Gdx.audio.newSound(Gdx.files.internal("data/Audio/clickSoundEffect.ogg"));
		hitSound= Gdx.audio.newSound(Gdx.files.internal("data/Audio/hitSoundEffect.ogg"));

		// Some devices/emulators do not support various mp3. Avoid crashing.
		try {
			music = Gdx.audio.newMusic(Gdx.files.internal("data/Audio/themeMusic.mp3"));
		}
		catch(Exception e) {
			music = Gdx.audio.newMusic(Gdx.files.internal("data/Audio/highjumpSoundEffect.wav"));
		}

		/*
		 * TUTORIAL
		 */
		tilt= new Texture(Gdx.files.internal("data/tilt.png"));
		touchTheScreen= new Texture(Gdx.files.internal("data/touchthescreen.png"));
		takePlatform= new Texture(Gdx.files.internal("data/takeplatform.png"));
		dontCrash= new Texture(Gdx.files.internal("data/dontcrash.png"));
		havefun= new Texture(Gdx.files.internal("data/havefun.png"));
	}
	public static void playMusic()
	{
		music.setLooping(true);
		music.setVolume(0.5f);
		music.play();
	}
}