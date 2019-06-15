/**
 * Program-wide class for storing time information
 * @author Troy
 *
 */
public class Time {
	
	private static float deltaTime = 0.0f;
	
	private static long prevTime = System.currentTimeMillis();
	
	private static long startTime = System.currentTimeMillis();
	
	public static void tick(){
		
		deltaTime = (System.currentTimeMillis() - prevTime) / 1000.0f;
		
	}
	
	/**
	 * Get the change in time (seconds) between the previous frame and the current one
	 * @return
	 */
	public static float getDeltaTime(){
		
		return deltaTime;
		
	}
	
	/**
	 * Get the current time in seconds
	 * @return
	 */
	public static float getTimeSeconds(){
		
		return (System.currentTimeMillis() - startTime) / 1000.0f;
		
	}
	
}
