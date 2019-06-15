import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;


public class Camera {
	
	private Vector3f pos;
	
	private float yaw = 0, pitch = 0;
	
	private float moveSpeed = 0.2f;
	
	private float rotateSpeed = 0.3f;
	
	private float slowDownFactor = 0.6f;
	
	private Window window;
	
	public Camera(Window window){
		pos = Vector3f.zero;
		this.window = window;
	}
	
	public void tick(){
		Block closest = window.getClosestBlock(pos, 6);
		Vector3f closestDistance = null;
		float blockSlowDown = 1.0f;
		if (closest != null){
			closestDistance = closest.getClosestPoint(pos).sub(pos);
			blockSlowDown = Math.min(1.0f, Math.max(0.0f, closestDistance.length() * slowDownFactor));
		}
		if (Input.mouseDown(MouseEvent.BUTTON1)){
			yaw += Input.getMouseXDelta() * rotateSpeed;
			pitch += Input.getMouseYDelta() * rotateSpeed;
		}
		Vector3f dir = Vector3f.zero;
		if (Input.keyDown(KeyEvent.VK_W)){
			dir = dir.add(new Vector3f(0, 0, -1).rotatePitch(-pitch).rotateYaw(-yaw));
		}
		if (Input.keyDown(KeyEvent.VK_S)){
			dir = dir.add(new Vector3f(0, 0, 1).rotatePitch(-pitch).rotateYaw(-yaw));
		}
		if (Input.keyDown(KeyEvent.VK_A)){
			dir = dir.add(new Vector3f(-1, 0, 0).rotatePitch(-pitch).rotateYaw(-yaw));
		}
		if (Input.keyDown(KeyEvent.VK_D)){
			dir = dir.add(new Vector3f(1, 0, 0).rotatePitch(-pitch).rotateYaw(-yaw));
		}
		if (Input.keyDown(KeyEvent.VK_SPACE)){
			dir = dir.add(new Vector3f(0, 1, 0).rotatePitch(-pitch).rotateYaw(-yaw));
		}
		if (Input.keyDown(KeyEvent.VK_SHIFT)){
			dir = dir.add(new Vector3f(0, -1, 0).rotatePitch(-pitch).rotateYaw(-yaw));
		}
		if (dir != Vector3f.zero){
			dir = dir.normalized();
			if (closestDistance == null || dir.dot(closestDistance) < 0){
				pos = pos.add(dir.multScalar(moveSpeed));
			}
			else{
				pos = pos.add(dir.multScalar(moveSpeed * blockSlowDown));
			}
		}
	}
	
	public void render(GL2 gl){
		gl.glRotatef(pitch, 1, 0, 0);
		gl.glRotatef(yaw, 0, 1, 0);
		gl.glTranslatef(-pos.x, -pos.y, -pos.z);
	}
	
	public Vector3f getWorldDirection(){
		return new Vector3f(0.0f, 0.0f, -1.0f).rotatePitch(getPitch()).rotateYaw(getYaw());
	}
	
	public float getPitch(){
		return -pitch;
	}
	
	public float getYaw(){
		return -yaw;
	}
	
	public Vector3f getPosition(){
		return pos;
	}
	
}
