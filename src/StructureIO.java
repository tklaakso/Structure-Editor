import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Program-wide class used for handling the saving and loading of structures
 * @author Troy
 *
 */
public class StructureIO {
	
	/**
	 * Saves the current world state to a file
	 * @param f
	 * @throws FileNotFoundException
	 */
	public static void save(File f) throws FileNotFoundException{
		
		ArrayList<Block> blocks = World.getBlocks();
		
		FileOutputStream fos = new FileOutputStream(f);
		
		try{
			
			fos.write((blocks.size() + "\n").getBytes());
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
		for (int i = 0; i < blocks.size(); i++){
			
			Block b = blocks.get(i);
			
			String line = b.id + "," + b.x + "," + b.y + "," + b.z + "\n";
			
			try{
				
				fos.write(line.getBytes());
				
			}
			catch(IOException e){
				
				e.printStackTrace();
				
			}
			
		}
		
		try{
			
			fos.close();
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * Load a structure file and update the world with its data
	 * @param f
	 */
	public static void load(File f) throws FileNotFoundException{
		
		FileInputStream fis = new FileInputStream(f);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		int count = 0;
		
		try{
			
			count = Integer.parseInt(reader.readLine());
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
		for (int i = 0; i < count; i++){
			
			try{
				
				String[] block = reader.readLine().split(",");
				
				if (block.length != 4){
					
					throw new InvalidStructureFileException("Couldn't parse comma-delimited block entries");
					
				}
				
				int id = Integer.parseInt(block[0]);
				
				int x = Integer.parseInt(block[1]);
				
				int y = Integer.parseInt(block[2]);
				
				int z = Integer.parseInt(block[3]);
				
				blocks.add(new Block(id, x, y, z));
				
			}
			catch(IOException e){
				
				e.printStackTrace();
				
			}
			catch(InvalidStructureFileException e){
				
				System.out.println("Error: invalid structure file");
				
			}
			
		}
		
		World.setBlocks(blocks);
		
		try{
			
			reader.close();
			
		}
		catch(IOException e){
			
			e.printStackTrace();
			
		}
		
	}
	
}
