import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import com.jogamp.opengl.awt.GLCanvas;


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
	
	private static Input instance = null;
	
	public static boolean keyPressed(int keyCode){
		return keysPressed.contains(keyCode);
	}
	
	public static boolean keyReleased(int keyCode){
		return keysReleased.contains(keyCode);
	}
	
	public static boolean keyDown(int keyCode){
		return keysDown.contains(keyCode);
	}
	
	public static boolean mousePressed(int mouseButton){
		return mouseButtonsPressed.contains(mouseButton);
	}
	
	public static boolean mouseReleased(int mouseButton){
		return mouseButtonsReleased.contains(mouseButton);
	}
	
	public static boolean mouseDown(int mouseButton){
		return mouseButtonsDown.contains(mouseButton);
	}
	
	public static int getMouseX(){
		return mouseX;
	}
	
	public static int getMouseY(){
		return mouseY;
	}
	
	public static int getMouseXDelta(){
		return mouseXDelta;
	}
	
	public static int getMouseYDelta(){
		return mouseYDelta;
	}
	
	public static void initialize(GLCanvas canvas){
		instance = new Input();
		canvas.addKeyListener(instance);
		canvas.addMouseListener(instance);
		canvas.addMouseMotionListener(instance);
	}
	
	public static void update(){
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
		mouseXDelta = mouseX - mouseXPrev;
		mouseYDelta = mouseY - mouseYPrev;
		mouseXPrev = mouseX;
		mouseYPrev = mouseY;
	}
	
	public static void postUpdate(){
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
