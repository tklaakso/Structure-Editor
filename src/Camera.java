import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;


public class Camera {
	
	private Vector3 pos;
	
	private float yaw = 0, pitch = 0, roll = 0;
	
	private float moveSpeed = 0.5f;
	
	private float rotateSpeed = 0.4f;
	
	private float slowDownFactor = 0.1f;
	
	public Camera(){
		pos = Vector3.zero;
	}
	
	public void update(GL2 gl){
		if (Input.mouseDown(MouseEvent.BUTTON1)){
			yaw += Input.getMouseXDelta() * rotateSpeed;
			pitch += Input.getMouseYDelta() * rotateSpeed;
		}
		if (Input.keyDown(KeyEvent.VK_W)){
			Vector3 dir = new Vector3(0, 0, 1);
			pos = pos.add(dir.multScalar(moveSpeed * Math.min(1.0f, pos.length() * slowDownFactor)));
		}
		if (Input.keyDown(KeyEvent.VK_S)){
			Vector3 dir = new Vector3(0, 0, -1);
			pos = pos.add(dir.multScalar(moveSpeed));
		}
		gl.glTranslatef(pos.x, pos.y, pos.z);
		gl.glRotatef(pitch, 1, 0, 0);
		gl.glRotatef(yaw, 0, 1, 0);
	}
	
}
