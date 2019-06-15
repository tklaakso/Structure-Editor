import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import com.jogamp.opengl.awt.GLCanvas;

/**
 * Program-wide class for receiving user input
 * @author Troy
 *
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener{
	
	private static ArrayList<Integer> keysDown = new ArrayList<Integer>();
	
	private static ArrayList<Integer> keysPressed = new ArrayList<Integer>();
	
	private static ArrayList<Integer> keysReleased = new ArrayList<Integer>();
	
	private static ArrayList<Integer> mouseButtonsDown = new ArrayList<Integer>();
	
	private static ArrayList<Integer> mouseButtonsPressed = new ArrayList<Integer>();
	
	private static ArrayList<Integer> mouseButtonsReleased = new ArrayList<Integer>();
	
	private static int mouseXPrev = 0, mouseYPrev = 0;
	
	private static int mouseX = 0, mouseY = 0;
	
	private static int mouseXDelta = 0, mouseYDelta = 0;
	
	private static Input instance = new Input();
	
	/**
	 * Check whether a key has been pressed in this frame
	 * @param keyCode
	 * @return
	 */
	public static boolean keyPressed(int keyCode){
		
		return keysPressed.contains(keyCode);
		
	}
	
	/**
	 * Check whether a key has been released in this frame
	 * @param keyCode
	 * @return
	 */
	public static boolean keyReleased(int keyCode){
		
		return keysReleased.contains(keyCode);
		
	}
	
	/**
	 * Check whether a key is currently down
	 * @param keyCode
	 * @return
	 */
	public static boolean keyDown(int keyCode){
		
		return keysDown.contains(keyCode);
		
	}
	
	/**
	 * Check whether a mouse button has been pressed in this frame
	 * @param mouseButton
	 * @return
	 */
	public static boolean mousePressed(int mouseButton){
		
		return mouseButtonsPressed.contains(mouseButton);
		
	}
	
	/**
	 * Check whether a mouse button has been released in this frame
	 * @param mouseButton
	 * @return
	 */
	public static boolean mouseReleased(int mouseButton){
		
		return mouseButtonsReleased.contains(mouseButton);
		
	}
	
	/**
	 * Check whether a mouse button is currently down
	 * @param mouseButton
	 * @return
	 */
	public static boolean mouseDown(int mouseButton){
		
		return mouseButtonsDown.contains(mouseButton);
		
	}
	
	/**
	 * Get current mouse X position on canvas
	 * @return
	 */
	public static int getMouseX(){
		
		return mouseX;
		
	}
	
	/**
	 * Get current mouse Y position on canvas
	 * @return
	 */
	public static int getMouseY(){
		
		return mouseY;
		
	}
	
	/**
	 * Get change in mouse X position from last frame to current frame
	 * @return
	 */
	public static int getMouseXDelta(){
		
		return mouseXDelta;
		
	}
	
	/**
	 * Get change in mouse Y position from last frame to current frame
	 * @return
	 */
	public static int getMouseYDelta(){
		
		return mouseYDelta;
		
	}
	
	/**
	 * Add key listener, mouse listener and mouse motion listener to canvas
	 * @param canvas
	 */
	public static void initialize(GLCanvas canvas){
		
		canvas.addKeyListener(instance);
		
		canvas.addMouseListener(instance);
		
		canvas.addMouseMotionListener(instance);
		
	}
	
	public static void tick(){
		
		// Record whether keys have been pressed or released in this frame
		for (int i = 0; i < keysPressed.size(); i++){
			
			int code = keysPressed.get(i);
			
			if (!keysDown.contains(code)){
				
				keysDown.add(code);
				
			}
			
		}
		
		for (int i = 0; i < keysReleased.size(); i++){
			
			int code = keysReleased.get(i);
			
			if (keysDown.contains(code)){
				
				keysDown.remove(new Integer(code));
				
			}
			
		}
		
		// Record whether mouse buttons have been pressed or released in this frame
		for (int i = 0; i < mouseButtonsPressed.size(); i++){
			
			int button = mouseButtonsPressed.get(i);
			
			if (!mouseButtonsDown.contains(button)){
				
				mouseButtonsDown.add(button);
				
			}
			
		}
		
		for (int i = 0; i < mouseButtonsReleased.size(); i++){
			
			int button = mouseButtonsReleased.get(i);
			
			if (mouseButtonsDown.contains(button)){
				
				mouseButtonsDown.remove(new Integer(button));
				
			}
			
		}
		
		// Record mouse X and Y deltas
		mouseXDelta = mouseX - mouseXPrev;
		
		mouseYDelta = mouseY - mouseYPrev;
		
		mouseXPrev = mouseX;
		
		mouseYPrev = mouseY;
		
	}
	
	public static void postTick(){
		
		keysPressed.clear();
		
		keysReleased.clear();
		
		mouseButtonsPressed.clear();
		
		mouseButtonsReleased.clear();
		
	}

	public void mouseDragged(MouseEvent arg0) {
		
		mouseX = arg0.getX();
		
		mouseY = arg0.getY();
		
	}

	public void mouseMoved(MouseEvent arg0) {
		
		mouseX = arg0.getX();
		
		mouseY = arg0.getY();
		
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
		
		if (!mouseButtonsPressed.contains(arg0.getButton())){
			
			mouseButtonsPressed.add(arg0.getButton());
			
		}
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
		if (!mouseButtonsReleased.contains(arg0.getButton())){
			
			mouseButtonsReleased.add(arg0.getButton());
			
		}
		
	}

	public void keyPressed(KeyEvent arg0) {
		
		if (!keysPressed.contains(arg0.getKeyCode())){
			
			keysPressed.add(arg0.getKeyCode());
			
		}
		
	}

	public void keyReleased(KeyEvent arg0) {
		
		if (!keysReleased.contains(arg0.getKeyCode())){
			
			keysReleased.add(arg0.getKeyCode());
			
		}
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
