import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;


public class Main {
	
	public static void main(String[] args){
		
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		
		GLCapabilities capabilities = new GLCapabilities(profile);
		
		final GLCanvas canvas = new GLCanvas(capabilities);
		
		World.initialize(canvas);
		
		try{
			
			World.addBlock(new Block(1, -5, 0, 5));
			
			World.addBlock(new Block(1, 5, 0, 5));
			
			World.addBlock(new Block(1, 5, 0, -5));
			
			World.addBlock(new Block(1, -5, 0, -5));
			
		}
		catch(BlockPositionOutOfBoundsException e){
			
			e.printStackTrace();
			
		}
		
		canvas.setSize(400, 400);
		
		Input.initialize(canvas);
		
		final JFrame frame = new JFrame("Structure Editor");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(canvas);
		
		frame.setSize(640, 480);
		
		frame.setVisible(true);
		
		final FPSAnimator anim = new FPSAnimator(canvas, 60, true);
		
		anim.start();
		
	}
	
}
