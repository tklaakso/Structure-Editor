import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;


public class Camera {
	
	private Vector3 pos;
	
	private Quaternion rot;
	
	private float moveSpeed = 0.1f;
	
	private float rotateSpeed = 0.4f;
	
	public Camera(){
		pos = Vector3.zero;
		rot = Quaternion.identity;
	}
	
	public void update(GL2 gl){
		if (Input.mouseDown(MouseEvent.BUTTON1)){
			Quaternion deltaRotY = Quaternion.identity;//Quaternion.angleAxis(Input.getMouseYDelta() * rotateSpeed, new Vector3(1, 0, 0));
			Quaternion deltaRotX = Quaternion.angleAxis(-Input.getMouseXDelta() * rotateSpeed, new Vector3(0, 1, 0));
			rot = rot.times(deltaRotX).times(deltaRotY);
		}
		System.out.println(pos.x + ", " + pos.y + ", " + pos.z);
		if (Input.keyDown(KeyEvent.VK_W)){
			Vector3 dir = new Vector3(0, 0, 1).mult(rot);
			pos = pos.add(dir.multScalar(moveSpeed));
		}
		if (Input.keyDown(KeyEvent.VK_A)){
			Vector3 dir = new Vector3(1, 0, 0).mult(rot);
			pos = pos.add(dir.multScalar(moveSpeed));
		}
		if (Input.keyDown(KeyEvent.VK_S)){
			Vector3 dir = new Vector3(0, 0, -1).mult(rot);
			pos = pos.add(dir.multScalar(moveSpeed));
		}
		if (Input.keyDown(KeyEvent.VK_D)){
			Vector3 dir = new Vector3(-1, 0, 0).mult(rot);
			pos = pos.add(dir.multScalar(moveSpeed));
		}
		if (Input.keyDown(KeyEvent.VK_SPACE)){
			Vector3 dir = new Vector3(0, -1, 0).mult(rot);
			pos = pos.add(dir.multScalar(moveSpeed));
		}
		if (Input.keyDown(KeyEvent.VK_SHIFT)){
			Vector3 dir = new Vector3(0, 1, 0).mult(rot);
			pos = pos.add(dir.multScalar(moveSpeed));
		}
		float yaw = rot.getYaw();
		float pitch = rot.getPitch();
		System.out.println("Yaw: " + yaw + ", pitch: " + pitch);
		gl.glRotatef(-yaw, 0, 1, 0);
		gl.glRotatef(-pitch, 1, 0, 0);
		gl.glTranslatef(pos.x, pos.y, pos.z);
	}
	
}
