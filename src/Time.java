
public class Time {
	
	private static float deltaTime = 0.0f;
	
	private static long prevTime = System.currentTimeMillis();
	
	private static long startTime = System.currentTimeMillis();
	
	public static void update(){
		deltaTime = (System.currentTimeMillis() - prevTime) / 1000.0f;
	}
	
	public static float getDeltaTime(){
		return deltaTime;
	}
	
	public static float getTimeSeconds(){
		return (System.currentTimeMillis() - startTime) / 1000.0f;
	}
	
}
