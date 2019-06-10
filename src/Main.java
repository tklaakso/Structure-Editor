import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;


public class Main {
	
	public static final int FRAME_WIDTH = 640;
	
	public static final int FRAME_HEIGHT = 480;
	
	public static void main(String[] args){
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		final GLCanvas canvas = new GLCanvas(capabilities);
		Window window = new Window();
		window.addBlock(new Block(0, 0, 0, -5));
		window.addBlock(new Block(1, 1, 0, -5));
		canvas.addGLEventListener(window);
		canvas.addMouseListener(window);
		canvas.addMouseMotionListener(window);
		canvas.setSize(400, 400);
		final JFrame frame = new JFrame("Structure Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.setSize(640, 480);
		frame.setVisible(true);
		final FPSAnimator anim = new FPSAnimator(canvas, 60, true);
		anim.start();
	}
	
}
