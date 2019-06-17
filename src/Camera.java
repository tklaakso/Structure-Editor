import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;

/**
 * Program-wide class for handling camera movement
 * @author Troy
 *
 */
public class Camera {
	
	// Camera position
	private static Vector3f pos = Vector3f.zero;
	
	// Yaw and pitch camera rotation
	private static float yaw = 0, pitch = 0;
	
	// Movement speed scaling constant
	private static float moveSpeed = 0.2f;
	
	// Rotation speed scaling constant
	private static float rotateSpeed = 0.3f;
	
	// The lower this is, the slower the player gets while approaching a block
	private static float slowDownFactor = 0.6f;
	
	public static void tick(){
		
		// Find closest block to camera and calculate slow down factor to avoid running into block
		Block closest = World.getClosestBlock(pos, 6);
		
		Vector3f closestDistance = null;
		
		float blockSlowDown = 1.0f;
		
		if (closest != null){
			
			closestDistance = closest.getClosestPoint(pos, 0.15f).sub(pos);
			
			blockSlowDown = Math.min(1.0f, Math.max(0.0f, closestDistance.length() * slowDownFactor));
			
		}
		
		// Change yaw and pitch rotation according to mouse movement
		if (Input.mouseDown(MouseEvent.BUTTON1)){
			
			yaw += Input.getMouseXDelta() * rotateSpeed;
			
			pitch += Input.getMouseYDelta() * rotateSpeed;
			
		}
		
		// Calculate movement direction based on keys pressed
		Vector3f dir = Vector3f.zero;
		
		if (Input.keyDown(KeyEvent.VK_W)){
			
			dir = dir.add(new Vector3f(0, 0, -1).rotate(-pitch, -yaw));
			
		}
		
		if (Input.keyDown(KeyEvent.VK_S)){
			
			dir = dir.add(new Vector3f(0, 0, 1).rotate(-pitch, -yaw));
			
		}
		
		if (Input.keyDown(KeyEvent.VK_A)){
			
			dir = dir.add(new Vector3f(-1, 0, 0).rotate(-pitch, -yaw));
			
		}
		
		if (Input.keyDown(KeyEvent.VK_D)){
			
			dir = dir.add(new Vector3f(1, 0, 0).rotate(-pitch, -yaw));
			
		}
		
		if (Input.keyDown(KeyEvent.VK_SPACE)){
			
			dir = dir.add(new Vector3f(0, 1, 0).rotate(-pitch, -yaw));
			
		}
		
		if (Input.keyDown(KeyEvent.VK_SHIFT)){
			
			dir = dir.add(new Vector3f(0, -1, 0).rotate(-pitch, -yaw));
			
		}
		
		if (dir != Vector3f.zero){
			
			dir = dir.normalized();
			
			// Make the user slow down if and only if they're heading towards the block closest them
			if (closestDistance == null || dir.dot(closestDistance) < 0){
				
				pos = pos.add(dir.multScalar(moveSpeed));
				
			}
			else{
				
				pos = pos.add(dir.multScalar(moveSpeed * blockSlowDown));
				
			}
			
		}
		
		pos.x = MathUtil.clamp(pos.x, -Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		pos.y = MathUtil.clamp(pos.y, -Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		pos.z = MathUtil.clamp(pos.z, -Constants.WORLD_SIZE, Constants.WORLD_SIZE);
		
	}
	
	public static void render(GL2 gl){
		
		// Transform to camera position and rotation
		gl.glRotatef(pitch, 1, 0, 0);
		
		gl.glRotatef(yaw, 0, 1, 0);
		
		gl.glTranslatef(-pos.x, -pos.y, -pos.z);
		
	}
	
	/**
	 * Get vector direction of camera in world
	 * @return
	 */
	public static Vector3f getWorldDirection(){
		
		return new Vector3f(0.0f, 0.0f, -1.0f).rotate(getPitch(), getYaw());
		
	}
	
	/**
	 * Get current camera pitch rotation
	 * @return
	 */
	public static float getPitch(){
		
		return -pitch;
		
	}
	
	/**
	 * Get current camera yaw rotation
	 * @return
	 */
	public static float getYaw(){
		
		return -yaw;
		
	}
	
	/**
	 * Get current camera position
	 * @return
	 */
	public static Vector3f getPosition(){
		
		return pos;
		
	}
	
}
