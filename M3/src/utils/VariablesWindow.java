package utils;

public interface VariablesWindow {
	// frame parameters
	public final String FRAME_TITLE = "Space Wars";
	public final String FRAME_TITLE_BUY = "Space Wars - Buy Units";
	public final String URL_LOGO = "./src/art/logo.png";
	public final String BASE_URL = "./src/art/";
	public final String BACKGROUND_IMAGE = "./src/art/universe_background.jpg";
	public final int FRAME_WIDTH = 1200;
	public final int FRAME_HEIGHT = 675;
	
	// Reset Position
	public final int[] RESET_POSITION = { -1000, 0 };
	
	// timer parameters
	public final int APPROACH_TIME = 1800;
	public final int APPROACH_STEPS = 900;
	public final int APPROACH_DELAY = 0;
	
	// Ships Initial Rotation
	public final int INITAL_PLAYER_SHIP_ROTATION = 270;
	public final int INITAL_ENEMY_SHIP_ROTATION = 90;
	
	// Player Ships Positions
	public final int[][] PLAYER_SHIPS_POSITIONS = {
			{ 400, 223 },
			{ 400, 351 },
			{ 330, 45 },
			{ 330, 539 },
			{ 165, 100 },
			{ 220, 257 },
			{ 180, 417 }
	};
	
	// Enemy Ships Position
	public final int[][] ENEMY_SHIPS_POSITIONS = {
			{ 140, 337 - 64 - 50 },
			{ 140, 337 + 64 - 50 },
			{ 210, 337 - 64 * 3 - 50 * 2 },
			{ 210, 337 + 64 * 3 + 10 }
	};
	
	public final int[] SHIPS_SIZES = {
			64, 64, 64, 64, 96, 128, 128
	};
	
	// MAIN PANEL Strings
	public final int [][] MAIN_PANEL_STRING_POSITION = {
		{ 350, 180 },
		{ 500, (int)(FRAME_HEIGHT / 2) }
	};
	
	public final int MAIN_PANEL_FONT_SIZE_LARGE = 64;
	public final int MAIN_PANEL_FONT_SIZE_SMALL = 48;
	
	// Explosion animation
	public final int EXPLOSION_PANEL_SIZE = 64;
	public final int ANIMATION_EXPLOSION_SLEEP = 10;
	
	// Bullet Panel
	public final int BULLET_PANEL_SIZE = 24;
	public final int BULLET_SPEED = 500;
	
	// Battle
	public final int BATTLE_SLEEP_TIME = 50;
	
	// Selectors
	public final int SELECTOR_PANEL_SIZE = 48;
}
