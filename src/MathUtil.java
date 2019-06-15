/**
 * Stores various math functions for program-wide use
 * @author Troy
 *
 */
public class MathUtil {
	
	/**
	 * Linearly interpolate between a and b by a factor of t, where t is in [0, 1]
	 * @param a Start value (t = 0)
	 * @param b End value (t = 1)
	 * @param t Interpolation factor from 0 to 1
	 * @return
	 */
	public static float lerp(float a, float b, float t){
		
		return a * (1.0f - t) + b * t;
		
	}
	
	/**
	 * Clamp f between a and b <br/>
	 * Requires: a < b
	 * @param f Value to clamp
	 * @param a Lower bound
	 * @param b Upper bound
	 * @return
	 */
	public static float clamp(float f, float a, float b){
		
		// Can't clamp between a and b when a >= b
		assert(a < b);
		
		if (f < a){
			
			f = a;
			
		}
		
		if (f > b){
			
			f = b;
			
		}
		
		return f;
		
	}
	
	/**
	 * Raycasting algorithm specific to block positions
	 * @param origin Start position
	 * @param dir Direction of the raycast
	 * @return A raycast info object containing the block's position if a block was found, otherwise the position as determined by RAYCAST_NULL_DISTANCE, as well as a block add position which neighbors the block position if a block was found
	 */
	public static RayCastInfo rayCast(Vector3f origin, Vector3f dir){
		
		Vector3f pos = origin.clone();
		
		Vector3i nullPosition = null;
		
		while (pos.sub(origin).length() < 20.0f){
			
			if (nullPosition == null && pos.sub(origin).length() > Constants.RAYCAST_NULL_DISTANCE){
				
				nullPosition = new Vector3i((int)Math.floor(pos.x), (int)Math.floor(pos.y), (int)Math.floor(pos.z));
				
			}
			
			pos = pos.add(dir.multScalar(0.025f));
			
			Block atPosition = null;
			
			try{
				
				atPosition = World.getBlock((int)Math.floor(pos.x), (int)Math.floor(pos.y), (int)Math.floor(pos.z));
				
			}
			catch(BlockPositionOutOfBoundsException e){
				
				// If raycast has gone out of world bounds, return the furthest block within world bounds or the null position if it exists
				
				if (nullPosition == null){
					
					pos = pos.sub(dir.multScalar(0.025f));
					
					Vector3i furthestPosition = new Vector3i((int)pos.x, (int)pos.y, (int)pos.z);
					
					return new RayCastInfo(furthestPosition, furthestPosition);
					
				}
				
				return new RayCastInfo(nullPosition, nullPosition);
				
			}
			
			// If a block was found, return its position and its closest neighbor's position
			if (atPosition != null){
				
				Vector3f avgPos = pos.sub(dir.multScalar(0.025f)).add(pos).multScalar(0.5f);
				
				return new RayCastInfo(new Vector3i((int)Math.floor(pos.x), (int)Math.floor(pos.y), (int)Math.floor(pos.z)), atPosition.getClosestNeighbor(avgPos));
				
			}
			
		}
		
		// If block was not found, return the null position
		return new RayCastInfo(nullPosition, nullPosition);
		
	}
	
	/**
	 * Modulo operator that is asymmetrical about 0 (always produces positive result)
	 * @param x
	 * @param modulo
	 * @return
	 */
	public static int fullMod(int x, int modulo){
		
		return ((x % modulo) + modulo) % modulo;
		
	}
	
}
