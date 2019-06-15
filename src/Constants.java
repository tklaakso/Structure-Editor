/**
 * Stores program-wide constants
 * @author Troy
 *
 */
public class Constants {
	
	/**
	 * Default width of the JFrame window
	 */
	public static final int FRAME_WIDTH = 640;
	
	/**
	 * Default height of the JFrame window
	 */
	public static final int FRAME_HEIGHT = 480;
	
	/**
	 * Raycast should return a block position this distance away from the origin if it didn't find any blocks
	 */
	public static final float RAYCAST_NULL_DISTANCE = 6.0f;
	
	/**
	 * World spans from -WORLD_SIZE to +WORLD_SIZE in all axes
	 */
	public static final int WORLD_SIZE = 20;
	
	/**
	 * Size of an item in a BlockList in pixels
	 */
	public static final float BLOCKLIST_ITEM_SIZE = 100;
	
}
