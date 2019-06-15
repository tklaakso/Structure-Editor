import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;


public class Window implements GLEventListener{
	
	public static final float BLOCK_CAST_NULL_DISTANCE = 6.0f;
	
	public static final int WORLD_SIZE = 20;
	
	private Block[][][] blocks;
	private ArrayList<Block> blockList;
	
	private GLU glu = new GLU();
	
	public static int width = Main.FRAME_WIDTH;
	public static int height = Main.FRAME_HEIGHT;
	
	public static float fovy = 45.0f;
	public static float fovx = calculateFovX();
	
	private Camera cam;
	
	private Vector3i selectedBlock = Vector3i.zero;
	
	private BlockModifyMode mode = BlockModifyMode.CREATE;
	
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
	
	private static float calculateFovX(){
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
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void render3d(GL2 gl){
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(60, 1, 0.1, 1000);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	private void render2d(GL2 gl){
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-1.0, 1.0, -1.0, 1.0, 0.0, 30.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
	private void renderSelectedBlock(GL2 gl){
		
		if (selectedBlock != null){
			
			gl.glLineWidth(2.0f);
			
			gl.glPushMatrix();
			{
				
				gl.glTranslatef(selectedBlock.x, selectedBlock.y, selectedBlock.z);
				
				if (mode == BlockModifyMode.CREATE){
					gl.glColor4f(0.0f, 1.0f, 0.0f, (float)(Math.sin(Time.getTimeSeconds() * 5.0f) / 2.0f + 0.5f));
				}
				else if (mode == BlockModifyMode.DESTROY){
					gl.glColor4f(1.0f, 0.0f, 0.0f, (float)(Math.sin(Time.getTimeSeconds() * 5.0f) / 2.0f + 0.5f));
				}
				
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
	
	private void renderCrosshairs(GL2 gl){
		
		float sizeX = 10.0f / width;
		float sizeY = 10.0f / height;
		
		gl.glLineWidth(1.0f);
		
		gl.glBegin(GL2.GL_LINES);
		
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		gl.glVertex2f(-sizeX, 0);
		gl.glVertex2f(sizeX, 0);
		
		gl.glVertex2f(0, -sizeY);
		gl.glVertex2f(0, sizeY);
		
		gl.glEnd();
		
	}
	
	private void updateSelectedBlock(){
		Vector3f dir = cam.getWorldDirection();
		Vector3f origin = cam.getPosition();
		RayCastInfo info = rayCast(origin, dir);
		if (mode == BlockModifyMode.CREATE){
			Vector3i blockPos = info.addPosition;
			if (blockPos != null){
				selectedBlock = blockPos;
			}
		}
		else if (mode == BlockModifyMode.DESTROY){
			Vector3i blockPos = info.blockPosition;
			if (blockPos != null){
				selectedBlock = blockPos;
			}
		}
	}
	
	private void tick(){
		Input.update();
		cam.tick();
		updateSelectedBlock();
		if (Input.mousePressed(MouseEvent.BUTTON3)){
			if (mode == BlockModifyMode.CREATE){
				addBlock(new Block(1, selectedBlock.x, selectedBlock.y, selectedBlock.z));
			}
			else if (mode == BlockModifyMode.DESTROY){
				removeBlock(selectedBlock.x, selectedBlock.y, selectedBlock.z);
			}
		}
		if (Input.keyPressed(KeyEvent.VK_R)){
			if (mode == BlockModifyMode.CREATE){
				mode = BlockModifyMode.DESTROY;
			}
			else{
				mode = BlockModifyMode.CREATE;
			}
		}
	}
	
	private void postTick(){
		Input.postUpdate();
	}
	
	private void render(GL2 gl){
		render3d(gl);
		gl.glLoadIdentity();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		cam.render(gl);
		renderAxes(gl);
		for (int i = 0; i < blockList.size(); i++){
			blockList.get(i).render(gl);
		}
		renderSelectedBlock(gl);
		render2d(gl);
		renderCrosshairs(gl);
	}

	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		tick();
		render(gl);
		postTick();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		Window.width = width;
		Window.height = height;
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
	
	private int floor(float x){
		return (int)Math.floor(x);
	}
	
	private int round(float x, int sign){
		if (sign == -1){
			return (int)Math.floor(x);
		}
		else if (sign == 1){
			return (int)Math.ceil(x);
		}
		return 0;
	}
	
	private RayCastInfo rayCast(Vector3f origin, Vector3f dir){
		Vector3f pos = origin.clone();
		Vector3i nullPosition = null;
		while (pos.sub(origin).length() < 20.0f){
			if (nullPosition == null && pos.sub(origin).length() > BLOCK_CAST_NULL_DISTANCE){
				nullPosition = new Vector3i(floor(pos.x), floor(pos.y), floor(pos.z));
			}
			pos = pos.add(dir.multScalar(0.025f));
			Block atPosition = getBlock(floor(pos.x), floor(pos.y), floor(pos.z));
			if (atPosition != null){
				Vector3f avgPos = pos.sub(dir.multScalar(0.025f)).add(pos).multScalar(0.5f);
				return new RayCastInfo(new Vector3i(floor(pos.x), floor(pos.y), floor(pos.z)), atPosition.getClosestFace(avgPos));
			}
		}
		return new RayCastInfo(nullPosition, nullPosition);
	}

}
