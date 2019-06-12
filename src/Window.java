import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;


public class Window implements GLEventListener{
	
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
		return 2.0f * (float)Math.toDegrees(Math.atan(Math.tan(Math.toRadians(fovy) * 0.5) * ((float)width / (float)height)));
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
		Input.update();
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		for (int i = 0; i < blocks.size(); i++){
			blocks.get(i).render(gl);
		}
		if (Input.mousePressed(MouseEvent.BUTTON1)){
			System.out.println("reached");
			float normalX = Input.getMouseX() / (float)Math.max(1, width - 1);
			float normalY = Input.getMouseY() / (float)Math.max(1, height - 1);
			System.out.println("FOV X: " + fovx);
			float angleX = MathUtil.lerp(-fovx / 2.0f, fovx / 2.0f, normalX);
			float angleY = MathUtil.lerp(fovy / 2.0f, -fovy / 2.0f, normalY);
			float x = (float)Math.sin(Math.toRadians(angleX));
			float y = (float)Math.sin(Math.toRadians(angleY));
			assert(1 - x * x - y * y > 0);
			float z = (float)Math.sqrt(1 - x * x - y * y);
			Vector3 dir = new Vector3(x, y, -z);
			Vector3 origin = new Vector3(0, 0, 0);
			Block target = blockCast(origin, dir);
			if (target != null){
				System.out.println("BLOCK CAST SUCCEEDED");
				System.out.println(target.x + ", " + target.y + ", " + target.z);
			}
			else{
				System.out.println("Block cast failed");
			}
		}
		Input.postUpdate();
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
	
	private int round(int sign, float val){
		if (sign >= 0){
			return (int)Math.floor(val);
		}
		return (int)Math.ceil(val);
	}
	
	private int floor(float x){
		if ((int)x == x){
			return (int)(x - 1);
		}
		return (int)Math.floor(x);
	}
	
	private int ceil(float x){
		if ((int)x == x){
			return (int)(x + 1);
		}
		return (int)Math.ceil(x);
	}
	
	private Block blockCast(Vector3 origin, Vector3 dir){
		Vector3 pos = origin.clone();
		int signX = (int)Math.signum(dir.x);
		int signY = (int)Math.signum(dir.y);
		int signZ = (int)Math.signum(dir.z);
		while (pos.sub(origin).length() < 20.0f){
			//System.out.println("--------------------");
			Vector3 posX = null, posY = null, posZ = null;
			if (signX != 0){
				int targetPosX = signX == -1 ? floor(pos.x) : ceil(pos.x);
				posX = pos.add(dir.multScalar(Math.abs(pos.x - targetPosX) / Math.abs(dir.x)));
				posX.x = Math.round(posX.x);
				//System.out.println("PosX: " + posX.x + ", " + posX.y + ", " + posX.z);
			}
			if (signY != 0){
				int targetPosY = signY == -1 ? floor(pos.y) : ceil(pos.y);
				posY = pos.add(dir.multScalar(Math.abs(pos.y - targetPosY) / Math.abs(dir.y)));
				posY.y = Math.round(posY.y);
				//System.out.println("PosY: " + posY.x + ", " + posY.y + ", " + posY.z);
			}
			if (signZ != 0){
				int targetPosZ = signZ == -1 ? floor(pos.z) : ceil(pos.z);
				posZ = pos.add(dir.multScalar(Math.abs(pos.z - targetPosZ) / Math.abs(dir.z)));
				posZ.z = Math.round(posZ.z);
				//System.out.println("PosZ: " + posZ.x + ", " + posZ.y + ", " + posZ.z);
			}
			//System.out.println("Pos: " + pos.x + ", " + pos.y + ", " + pos.z);
			//System.out.println("Dir: " + dir.x + ", " + dir.y + ", " + dir.z);
			//System.out.println(round(signY, posX.y) + ", " + round(signZ, posX.z));
			if (posX != null && round(signY, posX.y) == round(signY, pos.y) && round(signZ, posX.z) == round(signZ, pos.z)){
				//System.out.println(posX.x + ", " + posX.y + ", " + posX.z);
				pos = posX.clone();
			}
			else if (posY != null && round(signX, posY.x) == round(signX, pos.x) && round(signZ, posY.z) == round(signZ, pos.z)){
				//System.out.println(posY.x + ", " + posY.y + ", " + posY.z);
				pos = posY.clone();
			}
			else if (posZ != null && round(signX, posZ.x) == round(signX, pos.x) && round(signY, posZ.y) == round(signY, pos.y)){
				pos = posZ.clone();
			}
			Block b = getBlock(round(signX, pos.x), round(signY, pos.y), round(signZ, pos.z));
			if (pos.length() > 10){
				addBlock(new Block(0, round(signX, pos.x), round(signY, pos.y), round(signZ, pos.z)));
			}
			if (b != null){
				return b;
			}
		}
		return null;
	}

}
