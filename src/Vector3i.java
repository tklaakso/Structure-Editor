/**
 * 3D vector of ints
 * @author Troy
 *
 */
public class Vector3i {
	
	public int x, y, z;
	
	public static final Vector3i zero = new Vector3i(0, 0, 0);
	
	public Vector3i(int x, int y, int z){
		
		this.x = x;
		
		this.y = y;
		
		this.z = z;
		
	}
	
	public Vector3i clone(){
		
		return new Vector3i(x, y, z);
		
	}
	
	public Vector3i normalized(){
		
		int len = length();
		
		return new Vector3i(x / len, y / len, z / len);
		
	}
	
	public Vector3i add(Vector3i other){
		
		return new Vector3i(x + other.x, y + other.y, z + other.z);
		
	}
	
	public Vector3i sub(Vector3i other){
		
		return new Vector3i(x - other.x, y - other.y, z - other.z);
		
	}
	
	public Vector3i multScalar(int s){
		
		return new Vector3i(x * s, y * s, z * s);
		
	}
	
	/**
	 * Rotate this vector by pitch degrees in the pitch axis and yaw degrees in the yaw axis
	 * @param pitch
	 * @param yaw
	 * @return
	 */
	public Vector3i rotate(int pitch, int yaw){
		
		return rotatePitch(pitch).rotateYaw(yaw);
		
	}
	
	/**
	 * Rotate this vector by yaw degrees in the yaw axis
	 * @param yaw
	 * @return
	 */
	public Vector3i rotateYaw(int yaw){
		
		float cos = (float)Math.cos(Math.toRadians(yaw));
		
		float sin = (float)Math.sin(Math.toRadians(yaw));
		
		float _x = x * cos + z * sin;
		
		float _y = y;
		
		float _z = -x * sin + z * cos;
		
		return new Vector3i((int)_x, (int)_y, (int)_z);
		
	}
	
	/**
	 * Rotate this vector by pitch degrees in the pitch axis
	 * @param pitch
	 * @return
	 */
	public Vector3i rotatePitch(int pitch){
		
		float cos = (float)Math.cos(Math.toRadians(pitch));
		
		float sin = (float)Math.sin(Math.toRadians(pitch));
		
		float _x = x;
		
		float _y = y * cos - z * sin;
		
		float _z = y * sin + z * cos;
		
		return new Vector3i((int)_x, (int)_y, (int)_z);
		
	}
	
	public int dot(Vector3i other){
		
		return x * other.x + y * other.y + z * other.z;
		
	}
	
	public int length(){
		
		return (int)Math.sqrt(x * x + y * y + z * z);
		
	}
	
	@Override
	public String toString(){
		
		return x + ", " + y + ", " + z;
		
	}
	
}
