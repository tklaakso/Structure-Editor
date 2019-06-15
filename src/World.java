import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.awt.GLCanvas;

/**
 * Handles storage and rendering of the world
 * @author Troy
 *
 */
public class World implements GLEventListener{
	
	// Canvas width and height
	public static int width = Constants.FRAME_WIDTH;
	public static int height = Constants.FRAME_HEIGHT;
	
	// Blocks are stored in an array and an arraylist to optimize searching as well as rendering
	private static Block[][][] blocks = new Block[Constants.WORLD_SIZE * 2][Constants.WORLD_SIZE * 2][Constants.WORLD_SIZE * 2];
	private static ArrayList<Block> blockList = new ArrayList<Block>();
	
	private static GLU glu = new GLU();
	
	// Field of view for Y direction
	private static float fovy = 45.0f;
	
	// Currently selected block
	private static Vector3i selectedBlock = Vector3i.zero;
	
	// Controls whether the user is creating or destroying blocks
	private static BlockModifyMode mode = BlockModifyMode.CREATE;
	
	// Need an instance to get events from GLEventListener
	private static World instance = new World();
	
	/**
	 * Add the world's GLEventListener to the canvas
	 * @param canvas
	 */
	public static void initialize(GLCanvas canvas){
		
		canvas.addGLEventListener(instance);
		
	}
	
	/**
	 * Find closest block within a cubic searchRadius distance of a point
	 * @param point Position to search around
	 * @param searchRadius Half-width of the cube being searched
	 * @return
	 */
	public static Block getClosestBlock(Vector3f point, int searchRadius){
		
		int xPos = (int)point.x;
		
		int yPos = (int)point.y;
		
		int zPos = (int)point.z;
		
		Block closest = null;
		
		try{
			
			// Take extra care to ensure we don't go out of the world range
			for (int x = Math.max(-Constants.WORLD_SIZE, xPos - searchRadius); x < Math.min(xPos + searchRadius, Constants.WORLD_SIZE); x++){
				
				for (int y = Math.max(-Constants.WORLD_SIZE, yPos - searchRadius); y < Math.min(yPos + searchRadius, Constants.WORLD_SIZE); y++){
					
					for (int z = Math.max(-Constants.WORLD_SIZE, zPos - searchRadius); z < Math.min(zPos + searchRadius, Constants.WORLD_SIZE); z++){
						
						if (getBlock(x, y, z) != null && (closest == null || closest.getClosestPoint(point, 0.15f).sub(point).length() > getBlock(x, y, z).getClosestPoint(point, 0.15f).sub(point).length())){
							
							closest = getBlock(x, y, z);
							
						}
						
					}
					
				}
				
			}
			
		}
		catch(BlockPositionOutOfBoundsException e){
			
			e.printStackTrace();
			
		}
		
		return closest;
		
	}
	
	/**
	 * Check if a given block position is within the world's range
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private static boolean inWorldRange(int x, int y, int z){
		
		return x >= -Constants.WORLD_SIZE && x < Constants.WORLD_SIZE && y >= -Constants.WORLD_SIZE && y < Constants.WORLD_SIZE && z >= -Constants.WORLD_SIZE && z < Constants.WORLD_SIZE;
		
	}
	
	/**
	 * Add a block to the world, or replace an existing block <br/>
	 * Requires: Block position is within world bounds
	 * @param block
	 * @throws BlockPositionOutOfBoundsException
	 */
	public static void addBlock(Block block) throws BlockPositionOutOfBoundsException{
		
		// Make sure block is in world range
		if (inWorldRange(block.x, block.y, block.z)){
			
			if (getBlock(block.x, block.y, block.z) == null){
				
				// Update both block array and block arraylist
				setArrayBlock(block);
				
				blockList.add(block);
				
			}
			else{
				
				// Remove current existing block from block array and block arraylist before updating block array and block arraylist
				removeBlock(block.x, block.y, block.z);
				
				blockList.add(block);
				
				setArrayBlock(block);
				
			}
		}
		else{
			
			throw new BlockPositionOutOfBoundsException("Tried to add a block outside world bounds");
			
		}
	}
	
	/**
	 * Get an existing block from the world, or null if the block does not exist <br/>
	 * Requires: Block position is within world bounds
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 * @throws BlockPositionOutOfBoundsException
	 */
	public static Block getBlock(int x, int y, int z) throws BlockPositionOutOfBoundsException{
		
		if (inWorldRange(x, y, z)){
			
			return blocks[x + Constants.WORLD_SIZE][y + Constants.WORLD_SIZE][z + Constants.WORLD_SIZE];
			
		}
		else{
			
			throw new BlockPositionOutOfBoundsException("Tried to get a block outside world bounds");
			
		}
		
	}
	
	/**
	 * Set a block in the block array <br/>
	 * Requires: b is not null and b's position is in the world range
	 * @param b
	 * @throws BlockPositionOutOfBoundsException
	 */
	private static void setArrayBlock(Block b) throws BlockPositionOutOfBoundsException{
		
		// Can't set a null block
		assert(b != null);
		
		if (inWorldRange(b.x, b.y, b.z)){
			
			blocks[b.x + Constants.WORLD_SIZE][b.y + Constants.WORLD_SIZE][b.z + Constants.WORLD_SIZE] = b;
			
		}
		else{
			
			throw new BlockPositionOutOfBoundsException("Tried to set a block outside world bounds");
			
		}
		
	}
	
	/**
	 * Remove a block from the block array and block arraylist
	 * @param x
	 * @param y
	 * @param z
	 * @throws BlockPositionOutOfBoundsException
	 */
	public static void removeBlock(int x, int y, int z) throws BlockPositionOutOfBoundsException{
		
		if (inWorldRange(x, y, z)){
			
			if (getBlock(x, y, z) != null){
				
				blockList.remove(getBlock(x, y, z));
				
				blocks[x + Constants.WORLD_SIZE][y + Constants.WORLD_SIZE][z + Constants.WORLD_SIZE] = null;
				
			}
			
		}
		else{
			
			throw new BlockPositionOutOfBoundsException("Tried to remove a block outside world bounds");
			
		}
		
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
	
	/**
	 * Change rendering mode to 3D
	 * @param gl
	 */
	private static void setRender3d(GL2 gl){
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		
		gl.glLoadIdentity();
		
		glu.gluPerspective(fovy, (float)width / (float)height, 0.1, 1000);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		gl.glLoadIdentity();
		
	}
	
	/**
	 * Change rendering mode to 2D
	 * @param gl
	 */
	private static void setRender2d(GL2 gl){
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		
		gl.glLoadIdentity();
		
		gl.glOrtho(-1.0, 1.0, -1.0, 1.0, 0.0, 30.0);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		gl.glLoadIdentity();
		
	}

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Draw wireframe around currently selected block
	 * @param gl
	 */
	private static void renderSelectedBlock(GL2 gl){
		
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
	
	/**
	 * Draw XYZ axes as well as grid on XZ plane
	 * @param gl
	 */
	private static void renderAxes(GL2 gl){
		
		gl.glLineWidth(3.0f);
		
		gl.glBegin(GL2.GL_LINES);		
		
		// X axis
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(-Constants.WORLD_SIZE, 0, 0.5f);
		gl.glVertex3f(Constants.WORLD_SIZE, 0, 0.5f);
		
		// Y axis
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.5f, -Constants.WORLD_SIZE, 0.5f);
		gl.glVertex3f(0.5f, Constants.WORLD_SIZE, 0.5f);
		
		// Z axis
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.5f, 0, -Constants.WORLD_SIZE);
		gl.glVertex3f(0.5f, 0, Constants.WORLD_SIZE);
				
		gl.glEnd();
		
		gl.glLineWidth(1.0f);
		
		gl.glBegin(GL2.GL_LINES);
		
		// Draw grid
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		
		for (int x = -Constants.WORLD_SIZE; x < Constants.WORLD_SIZE + 1; x++){
			
			gl.glVertex3f(x, 0.0f, -Constants.WORLD_SIZE);
			gl.glVertex3f(x, 0.0f, Constants.WORLD_SIZE);
			
		}
		
		for (int y = -Constants.WORLD_SIZE; y < Constants.WORLD_SIZE + 1; y++){
			
			gl.glVertex3f(-Constants.WORLD_SIZE, 0.0f, y);
			gl.glVertex3f(Constants.WORLD_SIZE, 0.0f, y);
			
		}
		
		gl.glEnd();
		
	}
	
	/**
	 * Draw crosshairs in middle of screen
	 * @param gl
	 */
	private static void renderCrosshairs(GL2 gl){
		
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
	
	/**
	 * Change the currently selected block according to camera position and rotation
	 */
	private static void updateSelectedBlock(){
		
		Vector3f dir = Camera.getWorldDirection();
		
		Vector3f origin = Camera.getPosition();
		
		RayCastInfo info = MathUtil.rayCast(origin, dir);
		
		if (mode == BlockModifyMode.CREATE){
			
			// If in create mode pick a block neighboring the block under the crosshairs
			Vector3i blockPos = info.addPosition;
			
			if (blockPos != null){
				
				selectedBlock = blockPos;
				
			}
			
		}
		else if (mode == BlockModifyMode.DESTROY){
			
			// If in destroy mode pick the block under the crosshairs
			Vector3i blockPos = info.blockPosition;
			
			if (blockPos != null){
				
				selectedBlock = blockPos;
				
			}
			
		}
		
	}
	
	private static void tick(){
		
		Time.tick();
		
		Input.tick();
		
		Camera.tick();
		
		updateSelectedBlock();
		
		if (Input.mousePressed(MouseEvent.BUTTON3)){
			
			try{
				
				if (mode == BlockModifyMode.CREATE){
					
					addBlock(new Block(1, selectedBlock.x, selectedBlock.y, selectedBlock.z));
					
				}
				else if (mode == BlockModifyMode.DESTROY){
					
					removeBlock(selectedBlock.x, selectedBlock.y, selectedBlock.z);
					
				}
				
			}
			catch(BlockPositionOutOfBoundsException e){
				
				// If the user tried to modify a block outside of world bounds, don't do anything
				
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
	
	private static void postTick(){
		
		Input.postTick();
		
	}
	
	private static void render(GL2 gl){
		
		setRender3d(gl);
		
		gl.glLoadIdentity();
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		Camera.render(gl);
		
		renderAxes(gl);
		
		for (int i = 0; i < blockList.size(); i++){
			
			blockList.get(i).render(gl);
			
		}
		
		renderSelectedBlock(gl);
		
		setRender2d(gl);
		
		renderCrosshairs(gl);
		
	}

	public void display(GLAutoDrawable drawable) {
		
		final GL2 gl = drawable.getGL().getGL2();
		
		tick();
		
		render(gl);
		
		postTick();
		
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		
		World.width = width;
		
		World.height = height;
		
		final GL2 gl = drawable.getGL().getGL2();
		
		if(height <= 0)
	         height = 1;
		
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		
		gl.glLoadIdentity();
		
		glu.gluPerspective(fovy, (float)width / (float)height, 0.1, 1000);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		gl.glLoadIdentity();
		
	}
	
	public static World getInstance(){
		
		return instance;
		
	}

}
