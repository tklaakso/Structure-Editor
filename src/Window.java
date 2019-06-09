import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;


public class Window implements GLEventListener{
	
	public Window(){
		
	}
	
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0.5f, -0.5f, 0);
		gl.glVertex3f(-0.5f, 0.5f, 0);
		gl.glEnd();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
