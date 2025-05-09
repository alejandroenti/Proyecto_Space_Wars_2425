package utils;

public interface VariablesWindow {
	// frame parameters
	public final String FRAME_TITLE = "Space Wars";
	public final String URL_LOGO = "./src/art/logo.png";
	public final String BASE_URL = "./src/art/";
	public final String BACKGROUND_IMAGE = "./src/art/universe_background.jpg";
	public final int FRAME_WIDTH = 1200;
	public final int FRAME_HEIGHT = 675;
	
	// timer parameters
	public final int APPROACH_TIME = 180000;
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
	
	public final int[] PLAYER_SHIPS_SIZES = {
			64, 64, 64, 64, 96, 128, 128
	};
}
