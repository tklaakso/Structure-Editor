import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;


public class Block implements Renderable{
	
	public int x, y, z;
	
	public int id;
	
	public static int[] textures = null;
	
	public Block(int id, int x, int y, int z){
		assert(textures != null && textures.length > id);
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void initTextures(GL2 gl){
		if (textures == null){
			int numTextures = 0;
			while(new File("res/" + numTextures + ".png").exists()){
				numTextures++;
			}
			textures = new int[numTextures];
			for (int i = 0; i < numTextures; i++){
				try {
					File texFile = new File("res/" + i + ".png");
					Texture texture = TextureIO.newTexture(texFile, true);
					texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
					texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
					textures[i] = texture.getTextureObject(gl);
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public Vector3 middle(){
		return new Vector3(x + 1.0f, y + 1.0f, z + 1.0f);
	}
	
	private float clamp(float f, float a, float b){
		assert(a < b);
		if (f < a){
			f = a;
		}
		if (f > b){
			f = b;
		}
		return f;
	}
	
	public Vector3 getClosestPoint(Vector3 point){
		return new Vector3(clamp(point.x, x - 0.15f, x + 1.15f), clamp(point.y, y - 0.15f, y + 1.15f), clamp(point.z, z - 0.15f, z + 1.15f));
	}
	
	public void render(GL2 gl) {
		
		gl.glPushMatrix();
		{
			gl.glTranslatef((float)x, (float)y, (float)z);
			
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[id]);
			gl.glBegin(GL2.GL_QUADS);
			
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			
			float unit = 0.33333333333f;
			
			// FRONT
			gl.glTexCoord2f(0, 0);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(unit, 0);
			gl.glVertex3f(1.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(unit, unit);
			gl.glVertex3f(1.0f, 1.0f, 0.0f);
			gl.glTexCoord2f(0, unit);
			gl.glVertex3f(0.0f, 1.0f, 0.0f);
			
			// BACK
			gl.glTexCoord2f(unit * 2, 0);
			gl.glVertex3f(0.0f, 0.0f, 1.0f);
			gl.glTexCoord2f(unit * 3, 0);
			gl.glVertex3f(1.0f, 0.0f, 1.0f);
			gl.glTexCoord2f(unit * 3, unit);
			gl.glVertex3f(1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(unit * 2, unit);
			gl.glVertex3f(0.0f, 1.0f, 1.0f);
			
			// LEFT
			gl.glTexCoord2f(unit * 2, unit);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(unit * 2, unit * 2);
			gl.glVertex3f(0.0f, 1.0f, 0.0f);
			gl.glTexCoord2f(unit, unit * 2);
			gl.glVertex3f(0.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(unit, unit);
			gl.glVertex3f(0.0f, 0.0f, 1.0f);
			
			// RIGHT
			gl.glTexCoord2f(unit * 2, unit);
			gl.glVertex3f(1.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(unit * 2, unit * 2);
			gl.glVertex3f(1.0f, 1.0f, 0.0f);
			gl.glTexCoord2f(unit * 3, unit * 2);
			gl.glVertex3f(1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(unit * 3, unit);
			gl.glVertex3f(1.0f, 0.0f, 1.0f);
			
			// TOP
			gl.glTexCoord2f(unit, 0);
			gl.glVertex3f(0.0f, 1.0f, 0.0f);
			gl.glTexCoord2f(unit * 2, 0);
			gl.glVertex3f(1.0f, 1.0f, 0.0f);
			gl.glTexCoord2f(unit * 2, unit);
			gl.glVertex3f(1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(unit, unit);
			gl.glVertex3f(0.0f, 1.0f, 1.0f);
			
			// BOTTOM
			gl.glTexCoord2f(unit, unit);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(0, unit);
			gl.glVertex3f(1.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(0, unit * 2);
			gl.glVertex3f(1.0f, 0.0f, 1.0f);
			gl.glTexCoord2f(unit, unit * 2);
			gl.glVertex3f(0.0f, 0.0f, 1.0f);
			
			gl.glEnd();
		}
		gl.glPopMatrix();
		
	}

}
