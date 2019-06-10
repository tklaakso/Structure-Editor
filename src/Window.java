import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;


public class Window implements GLEventListener, MouseListener, MouseMotionListener{
	
	private ArrayList<Block> blocks;
	
	private GLU glu = new GLU();
	
	private int width = Main.FRAME_WIDTH;
	private int height = Main.FRAME_HEIGHT;
	
	private float fovy = 45.0f;
	private float fovx = calculateFovX();
	
	public Window(){
		blocks = new ArrayList<Block>();
	}
	
	public void addBlock(Block block){
		blocks.add(block);
	}
	
	private float calculateFovX(){
		return 2.0f * (float)Math.atan(Math.tan(fovy * 0.5) * ((float)width / (float)height));
	}
	
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		Block.initTextures(gl);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glEnable(GL2.GL_TEXTURE_2D);
	}

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
	public Block getBlock(int x, int y, int z){
		for (int i = 0; i < blocks.size(); i++){
			Block current = blocks.get(i);
			if (current.x == x && current.y == y && current.z == z){
				return current;
			}
		}
		return null;
	}

	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		for (int i = 0; i < blocks.size(); i++){
			blocks.get(i).render(gl);
		}
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		fovx = calculateFovX();
		final GL2 gl = drawable.getGL().getGL2();
		if(height <= 0)
	         height = 1;
		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(fovy, h, 0.1, 100.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private Block blockCast(Vector3 origin, Vector3 dir){
		Vector3 pos = origin.clone();
		int signX = (int)Math.signum(dir.x);
		int signY = (int)Math.signum(dir.y);
		int signZ = (int)Math.signum(dir.z);
		while (pos.sub(origin).length() < 100.0f){
			Vector3 posX = null, posY = null, posZ = null;
			if (signX != 0){
				int targetPosX = signX == -1 ? (int)Math.floor(pos.x) : (int)Math.ceil(pos.x);
				if (targetPosX == pos.x){
					targetPosX += signX;
				}
				posX = pos.add(dir.multScalar(Math.abs(pos.x - targetPosX) / dir.x));
			}
			if (signY != 0){
				int targetPosY = signY == -1 ? (int)Math.floor(pos.y) : (int)Math.ceil(pos.y);
				if (targetPosY == pos.y){
					targetPosY += signY;
				}
				posY = pos.add(dir.multScalar(Math.abs(pos.y - targetPosY) / dir.y));
			}
			if (signZ != 0){
				int targetPosZ = signZ == -1 ? (int)Math.floor(pos.z) : (int)Math.ceil(pos.z);
				if (targetPosZ == pos.z){
					targetPosZ += signZ;
				}
				posZ = pos.add(dir.multScalar(Math.abs(pos.z - targetPosZ) / dir.z));
			}
			if (posX != null && (int)Math.floor(posX.y) == (int)Math.floor(pos.y) && (int)Math.floor(posX.z) == (int)Math.floor(pos.z)){
				pos = posX;
			}
			else if (posY != null && (int)Math.floor(posY.x) == (int)Math.floor(pos.x) && (int)Math.floor(posY.z) == (int)Math.floor(pos.z)){
				pos = posY;
			}
			else if (posZ != null && (int)Math.floor(posZ.x) == (int)Math.floor(pos.x) && (int)Math.floor(posZ.y) == (int)Math.floor(pos.y)){
				pos = posZ;
			}
			Block b = getBlock((int)Math.floor(pos.x), (int)Math.floor(pos.y), (int)Math.floor(pos.z));
			if (b != null){
				return b;
			}
		}
		return null;
	}

	public void mouseMoved(MouseEvent arg0) {
		float normalX = arg0.getX() / (float)Math.max(1, width - 1);
		float normalY = arg0.getY() / (float)Math.max(1, height - 1);
		float angleX = MathUtil.lerp(-fovx / 2.0f, fovx / 2.0f, normalX);
		float angleY = MathUtil.lerp(fovy / 2.0f, -fovy / 2.0f, normalY);
		float x = (float)Math.sin(angleX);
		float y = (float)Math.sin(angleY);
		assert(1 - x * x - y * y > 0);
		float z = (float)Math.sqrt(1 - x * x - y * y);
		Vector3 dir = new Vector3(x, y, z);
		Vector3 origin = new Vector3(0, 0, 0);
		Block target = blockCast(origin, dir);
		if (target != null){
			System.out.println(target.x + ", " + target.y + ", " + target.z);
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
