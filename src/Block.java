import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * Stores a block's data as well as a few useful block methods
 * @author Troy
 *
 */
public class Block {
	
	public int x, y, z;
	
	public int id;
	
	public static int[] textures = null;
	
	/**
	 * Loads block textures from file, must call this once before adding any blocks to the world
	 * @param gl
	 */
	public static void initTextures(GL2 gl){
		
		if (textures == null){
			
			int numTextures = 0;
			
			// Check how many textures exist on file
			while(new File("res/" + numTextures + ".png").exists()){
				
				numTextures++;
				
			}
			
			textures = new int[numTextures];
			
			// Load texture files and create opengl texture objects
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
	
	public Block(int id, int x, int y, int z){
		
		// Can't create a block before initTextures has been called
		assert(textures != null && textures.length > id);
		
		this.id = id;
		
		this.x = x;
		
		this.y = y;
		
		this.z = z;
		
	}
	
	/**
	 * Returns the point in the block closest to the parameter point
	 * @param point The point we want to get close to
	 * @param buffer Extrude outward in all directions by buffer when finding points in the block (buffer = 0 corresponds to only checking inside the actual block)
	 * @return
	 */
	public Vector3f getClosestPoint(Vector3f point, float buffer){
		
		return new Vector3f(MathUtil.clamp(point.x, x - buffer, x + 1.0f + buffer), MathUtil.clamp(point.y, y - buffer, y + 1.0f + buffer), MathUtil.clamp(point.z, z - buffer, z + 1.0f + buffer));
		
	}
	
	/**
	 * Find closest block position which neighbors this block's position to a given point
	 * @param point The point we want the neighboring position to be close to
	 * @return
	 */
	public Vector3i getClosestNeighbor(Vector3f point){
		
		Vector3f pos = new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f);
		
		// Make vectors in the middle of each of this block's faces
		Vector3f left = pos.add(new Vector3f(-0.5f, 0.0f, 0.0f));
		
		Vector3f right = pos.add(new Vector3f(0.5f, 0.0f, 0.0f));
		
		Vector3f top = pos.add(new Vector3f(0.0f, 0.5f, 0.0f));
		
		Vector3f bottom = pos.add(new Vector3f(0.0f, -0.5f, 0.0f));
		
		Vector3f front = pos.add(new Vector3f(0.0f, 0.0f, -0.5f));
		
		Vector3f back = pos.add(new Vector3f(0.0f, 0.0f, 0.5f));
		
		Vector3i closestBlock = new Vector3i(x - 1, y, z);
		
		Vector3f closestFace = left;
		
		// Record the closest face and the neighboring block corresponding to that face
		if (point.sub(right).length() < point.sub(closestFace).length()){
			
			closestBlock = new Vector3i(x + 1, y, z);
			
			closestFace = right;
			
		}
		
		if (point.sub(top).length() < point.sub(closestFace).length()){
			
			closestBlock = new Vector3i(x, y + 1, z);
			
			closestFace = top;
			
		}
		
		if (point.sub(bottom).length() < point.sub(closestFace).length()){
			
			closestBlock = new Vector3i(x, y - 1, z);
			
			closestFace = bottom;
			
		}
		
		if (point.sub(front).length() < point.sub(closestFace).length()){
			
			closestBlock = new Vector3i(x, y, z - 1);
			
			closestFace = front;
			
		}
		
		if (point.sub(back).length() < point.sub(closestFace).length()){
			
			closestBlock = new Vector3i(x, y, z + 1);
			
			closestFace = back;
			
		}
		
		return closestBlock;
		
	}
	
	public void render(GL2 gl) {
		
		gl.glPushMatrix();
		{
			
			gl.glTranslatef(x, y, z);
			
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
