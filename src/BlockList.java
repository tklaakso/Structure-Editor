import com.jogamp.opengl.GL2;

/**
 * Graphical element for displaying and selecting block types
 * @author Troy
 *
 */
public class BlockList {
	
	private int width, height;
	
	private int xPos, yPos;
	
	private int selectedBlockId = 0;
	
	// Controls whether the block list is enabled / disabled
	private boolean enabled = true;
	
	private float opacity = 1.0f;
	
	// Smaller values -> takes longer to fade in / out
	private float fadeSpeed = 6.0f;
	
	public BlockList(int x, int y, int width, int height){
		
		assert(width > 0 && height > 0);
		
		this.xPos = x;
		
		this.yPos = y;
		
		this.width = width;
		
		this.height = height;
		
	}
	
	public void tick(){
		
		if (!enabled && opacity > 0.0f){
			
			opacity = Math.max(0.0f, opacity - fadeSpeed * Time.getDeltaTime());
			
		}
		else if (enabled && opacity < 1.0f){
			
			opacity = Math.min(1.0f, opacity + fadeSpeed * Time.getDeltaTime());
			
		}
		
	}
	
	public void render(GL2 gl){
		
		if (opacity != 0.0f){
			
			float normalX = (float)xPos / World.width - 1.0f;
			
			float normalY = (float)-yPos / World.height + 1.0f;
			
			float normalWidth = Constants.BLOCKLIST_ITEM_SIZE / World.width;
			
			float normalHeight = Constants.BLOCKLIST_ITEM_SIZE / World.height;
			
			gl.glPushMatrix();
			{
				
				gl.glTranslatef(normalX, normalY, 0);
				
				// Draw background
				gl.glBegin(GL2.GL_QUADS);
				
				gl.glColor4f(0.2f, 0.2f, 0.2f, 0.5f * opacity);
				
				gl.glVertex2f(0, 0);
				gl.glVertex2f(width * normalWidth, 0);
				gl.glVertex2f(width * normalWidth, -height * normalHeight);
				gl.glVertex2f(0, -height * normalHeight);
				
				gl.glEnd();
				
				// Draw grid lines
				gl.glBegin(GL2.GL_LINES);
				
				gl.glColor4f(0.5f, 0.5f, 0.5f, opacity);
				
				for (int x = 1; x < width; x++){
					
					gl.glVertex2f(x * normalWidth, 0);
					gl.glVertex2f(x * normalWidth, -height * normalHeight);
					
				}
				
				for (int y = 1; y < height; y++){
					
					gl.glVertex2f(0, -y * normalHeight);
					gl.glVertex2f(width * normalWidth, -y * normalHeight);
					
				}
				
				gl.glEnd();
				
				// Draw blocks
				for (int i = 0; i < Block.textures.length; i++){
					
					gl.glBindTexture(GL2.GL_TEXTURE_2D, Block.textures[i]);
					
					gl.glColor4f(1.0f, 1.0f, 1.0f, opacity);
					
					gl.glBegin(GL2.GL_QUADS);
					
					int x = i % width;
					
					int y = i / width;
					
					float unit = 0.33333333333f;
					
					gl.glTexCoord2f(0, 0);
					gl.glVertex2f(x * normalWidth, -(y + 1.0f) * normalHeight);
					gl.glTexCoord2f(unit, 0);
					gl.glVertex2f((x + 1.0f) * normalWidth, -(y + 1.0f) * normalHeight);
					gl.glTexCoord2f(unit, unit);
					gl.glVertex2f((x + 1.0f) * normalWidth, -y * normalHeight);
					gl.glTexCoord2f(0, unit);
					gl.glVertex2f(x * normalWidth, -y * normalHeight);
					
					gl.glEnd();
					
					if (i == selectedBlockId){
						
						gl.glColor4f(0.0f, 0.0f, 1.0f, (float)(Math.sin(Time.getTimeSeconds() * 10.0f) / 8.0f + 0.125f) * opacity);
						
						gl.glBegin(GL2.GL_QUADS);
						
						gl.glVertex2f(x * normalWidth, -(y + 1.0f) * normalHeight);
						gl.glVertex2f((x + 1.0f) * normalWidth, -(y + 1.0f) * normalHeight);
						gl.glVertex2f((x + 1.0f) * normalWidth, -y * normalHeight);
						gl.glVertex2f(x * normalWidth, -y * normalHeight);
						
						gl.glEnd();
						
					}
					
					gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
					
				}
				
			}
			gl.glPopMatrix();
			
		}
		
	}
	
	/**
	 * Shifts the selected block by a factor of cycles
	 * @param cycles
	 */
	public void cycleSelectedBlockId(int cycles){
		
		selectedBlockId += cycles;
		
		selectedBlockId = MathUtil.fullMod(selectedBlockId, Block.textures.length);
		
	}
	
	public int getSelectedBlockId(){
		
		return selectedBlockId;
		
	}
	
	/**
	 * Shows or hides the block list, which results in a fade-in or fade-out animation and the enabling/disabling of controls
	 * @param hidden
	 */
	public void setEnabled(boolean enabled){
		
		this.enabled = enabled;
		
	}
	
	public boolean isEnabled(){
		
		return enabled;
		
	}
	
}
