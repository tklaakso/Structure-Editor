import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;


public class Window implements GLEventListener{
	
	public static final float BLOCK_CAST_NULL_DISTANCE = 6.0f;
	
	public static final int WORLD_SIZE = 20;
	
	private Block[][][] blocks;
	private ArrayList<Block> blockList;
	
	private GLU glu = new GLU();
	
	private int width = Main.FRAME_WIDTH;
	private int height = Main.FRAME_HEIGHT;
	
	private float fovy = 45.0f;
	private float fovx = calculateFovX();
	
	private Camera cam;
	
	private Vector3i selectedBlock = Vector3i.zero;
	
	public Window(){
		blocks = new Block[WORLD_SIZE * 2][WORLD_SIZE * 2][WORLD_SIZE * 2];
		blockList = new ArrayList<Block>();
		cam = new Camera(this);
	}
	
	public Block getClosestBlock(Vector3f point, int searchRadius){
		int xPos = (int)point.x;
		int yPos = (int)point.y;
		int zPos = (int)point.z;
		Block closest = null;
		for (int x = Math.max(-WORLD_SIZE, xPos - searchRadius); x < Math.min(xPos + searchRadius, WORLD_SIZE); x++){
			for (int y = Math.max(-WORLD_SIZE, yPos - searchRadius); y < Math.min(yPos + searchRadius, WORLD_SIZE); y++){
				for (int z = Math.max(-WORLD_SIZE, zPos - searchRadius); z < Math.min(zPos + searchRadius, WORLD_SIZE); z++){
					if (getBlock(x, y, z) != null && (closest == null || closest.getClosestPoint(point).sub(point).length() > getBlock(x, y, z).getClosestPoint(point).sub(point).length())){
						closest = getBlock(x, y, z);
					}
				}
			}
		}
		return closest;
	}
	
	private boolean inWorldRange(int x, int y, int z){
		return x >= -WORLD_SIZE && x < WORLD_SIZE && y >= -WORLD_SIZE && y < WORLD_SIZE && z >= -WORLD_SIZE && z < WORLD_SIZE;
	}
	
	public void addBlock(Block block){
		if (inWorldRange(block.x, block.y, block.z)){
			if (getBlock(block.x, block.y, block.z) == null){
				setBlock(block);
				blockList.add(block);
			}
			else{
				blockList.remove(getBlock(block.x, block.y, block.z));
				blockList.add(block);
				setBlock(block);
			}
		}
		else{
			System.out.println("Error: Tried to add block outside of world bounds");
		}
	}
	
	public Block getBlock(int x, int y, int z){
		if (!inWorldRange(x, y, z)){
			return null;
		}
		return blocks[x + WORLD_SIZE][y + WORLD_SIZE][z + WORLD_SIZE];
	}
	
	private void setBlock(Block b){
		assert(b != null);
		assert(inWorldRange(b.x, b.y, b.z));
		blocks[b.x + WORLD_SIZE][b.y + WORLD_SIZE][b.z + WORLD_SIZE] = b;
	}
	
	public void removeBlock(int x, int y, int z){
		if (getBlock(x, y, z) != null){
			blockList.remove(getBlock(x, y, z));
			blocks[x + WORLD_SIZE][y + WORLD_SIZE][z + WORLD_SIZE] = null;
		}
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
	
	private void renderSelectedBlock(GL2 gl){
		
		gl.glLineWidth(3.0f);
		
		gl.glPushMatrix();
		{
			
			gl.glTranslatef(selectedBlock.x, selectedBlock.y, selectedBlock.z);
			
			gl.glColor3f(0.5f, 0.5f, 0.5f);
			
			gl.glBegin(GL2.GL_LINES);
			
			// Front
			gl.glVertex3f(0, 0, 0);
			gl.glVertex3f(1, 0, 0);
			
			gl.glVertex3f(1, 0, 0);
			gl.glVertex3f(1, 1, 0);
			
			gl.glVertex3f(1, 1, 0);
			gl.glVertex3f(0, 1, 0);
			
			gl.glVertex3f(0, 1, 0);
			gl.glVertex3f(0, 0, 0);
			
			// Left
			gl.glVertex3f(0, 0, 0);
			gl.glVertex3f(0, 0, 1);
			
			gl.glVertex3f(0, 0, 1);
			gl.glVertex3f(0, 1, 1);
			
			gl.glVertex3f(0, 1, 1);
			gl.glVertex3f(0, 1, 0);
			
			// Right
			gl.glVertex3f(1, 0, 0);
			gl.glVertex3f(1, 0, 1);
			
			gl.glVertex3f(1, 0, 1);
			gl.glVertex3f(1, 1, 1);
			
			gl.glVertex3f(1, 1, 1);
			gl.glVertex3f(1, 1, 0);
			
			// Back
			gl.glVertex3f(0, 0, 1);
			gl.glVertex3f(1, 0, 1);
			
			gl.glVertex3f(0, 1, 1);
			gl.glVertex3f(1, 1, 1);
			
			gl.glEnd();
			
		}
		gl.glPopMatrix();
		
	}
	
	private void renderAxes(GL2 gl){
		
		gl.glLineWidth(3.0f);
		
		gl.glBegin(GL2.GL_LINES);		
		
		// X axis
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(-WORLD_SIZE, 0, 0);
		gl.glVertex3f(WORLD_SIZE, 0, 0);
		
		// Y axis
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(0, -WORLD_SIZE, 0);
		gl.glVertex3f(0, WORLD_SIZE, 0);
		
		// Z axis
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(0, 0, -WORLD_SIZE);
		gl.glVertex3f(0, 0, WORLD_SIZE);
				
		gl.glEnd();
		
		gl.glLineWidth(1.0f);
		
		gl.glBegin(GL2.GL_LINES);
		
		// Draw grid
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		for (int x = -WORLD_SIZE; x < WORLD_SIZE + 1; x++){
			if (x != 0){
				gl.glVertex3f(x, 0.0f, -WORLD_SIZE);
				gl.glVertex3f(x, 0.0f, WORLD_SIZE);
			}
		}
		
		for (int y = -WORLD_SIZE; y < WORLD_SIZE + 1; y++){
			if (y != 0){
				gl.glVertex3f(-WORLD_SIZE, 0.0f, y);
				gl.glVertex3f(WORLD_SIZE, 0.0f, y);
			}
		}
		
		gl.glEnd();
		
	}
	
	private void updateSelectedBlock(){
		float normalX = Input.getMouseX() / (float)Math.max(1, width - 1);
		float normalY = Input.getMouseY() / (float)Math.max(1, height - 1);
		float angleX = MathUtil.lerp(-fovx / 2.0f, fovx / 2.0f, normalX);
		float angleY = MathUtil.lerp(fovy / 2.0f, -fovy / 2.0f, normalY);
		float x = (float)Math.sin(Math.toRadians(angleX));
		float y = (float)Math.sin(Math.toRadians(angleY));
		assert(1 - x * x - y * y > 0);
		float z = (float)Math.sqrt(1 - x * x - y * y);
		Vector3f dir = new Vector3f(x, y, -z).rotatePitch(cam.getPitch()).rotateYaw(cam.getYaw());
		Vector3f origin = cam.getPosition();
		selectedBlock = blockCast(origin, dir);
	}
	
	private void tick(){
		Input.update();
		cam.tick();
		updateSelectedBlock();
		if (Input.mousePressed(MouseEvent.BUTTON3)){
			removeBlock(selectedBlock.x, selectedBlock.y, selectedBlock.z);
		}
	}
	
	private void postTick(){
		Input.postUpdate();
	}
	
	private void render(GL2 gl){
		gl.glLoadIdentity();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		cam.render(gl);
		renderAxes(gl);
		for (int i = 0; i < blockList.size(); i++){
			blockList.get(i).render(gl);
		}
		renderSelectedBlock(gl);
	}

	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		tick();
		render(gl);
		postTick();
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
	
	private Vector3i blockCast(Vector3f origin, Vector3f dir){
		Vector3f pos = origin.clone();
		int signX = (int)Math.signum(dir.x);
		int signY = (int)Math.signum(dir.y);
		int signZ = (int)Math.signum(dir.z);
		Vector3i nullPosition = null;
		while (pos.sub(origin).length() < 20.0f){
			if (nullPosition == null && pos.sub(origin).length() > BLOCK_CAST_NULL_DISTANCE){
				nullPosition = new Vector3i((int)pos.x, (int)pos.y, (int)pos.z);
			}
			Vector3f posX = null, posY = null, posZ = null;
			if (signX != 0){
				int targetPosX = signX == -1 ? floor(pos.x) : ceil(pos.x);
				posX = pos.add(dir.multScalar(Math.abs(pos.x - targetPosX) / Math.abs(dir.x)));
				posX.x = Math.round(posX.x);
			}
			if (signY != 0){
				int targetPosY = signY == -1 ? floor(pos.y) : ceil(pos.y);
				posY = pos.add(dir.multScalar(Math.abs(pos.y - targetPosY) / Math.abs(dir.y)));
				posY.y = Math.round(posY.y);
			}
			if (signZ != 0){
				int targetPosZ = signZ == -1 ? floor(pos.z) : ceil(pos.z);
				posZ = pos.add(dir.multScalar(Math.abs(pos.z - targetPosZ) / Math.abs(dir.z)));
				posZ.z = Math.round(posZ.z);
			}
			if (posX != null && round(signY, posX.y) == round(signY, pos.y) && round(signZ, posX.z) == round(signZ, pos.z)){
				pos = posX.clone();
			}
			else if (posY != null && round(signX, posY.x) == round(signX, pos.x) && round(signZ, posY.z) == round(signZ, pos.z)){
				pos = posY.clone();
			}
			else if (posZ != null && round(signX, posZ.x) == round(signX, pos.x) && round(signY, posZ.y) == round(signY, pos.y)){
				pos = posZ.clone();
			}
			Block b = getBlock(round(signX, pos.x), round(signY, pos.y), round(signZ, pos.z));
			if (b != null){
				return new Vector3i(b.x, b.y, b.z);
			}
		}
		return nullPosition;
	}

}
