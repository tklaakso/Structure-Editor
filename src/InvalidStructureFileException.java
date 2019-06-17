/**
 * Thrown when there was an error parsing a structure file
 * @author Troy
 *
 */
public class InvalidStructureFileException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public InvalidStructureFileException(String err){
		
		super(err);
		
	}
	
}
