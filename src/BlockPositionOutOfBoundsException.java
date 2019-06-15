/**
 * Thrown when a block position was outside of the world bounds
 * @author Troy
 *
 */
public class BlockPositionOutOfBoundsException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public BlockPositionOutOfBoundsException(String err){
		
		super(err);
		
	}
	
}
