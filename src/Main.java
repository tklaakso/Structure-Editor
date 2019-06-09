import java.awt.Frame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;


public class Main {
	
	public static void main(String[] args){
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		final GLCanvas canvas = new GLCanvas(capabilities);
		Window window = new Window();
		canvas.addGLEventListener(window);
		canvas.setSize(400, 400);
		final Frame frame = new Frame("Structure Editor");
		frame.add(canvas);
		frame.setSize(640, 480);
		frame.setVisible(true);
	}
	
}
