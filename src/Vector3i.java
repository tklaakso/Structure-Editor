
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
		float len = length();
		return new Vector3i((int)(x / len), (int)(y / len), (int)(z / len));
	}
	
	public Vector3i add(Vector3i other){
		return new Vector3i(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3i sub(Vector3i other){
		return new Vector3i(x - other.x, y - other.y, z - other.z);
	}
	
	public Vector3i multScalar(float s){
		return new Vector3i((int)(x * s), (int)(y * s), (int)(z * s));
	}
	
	public Vector3i rotateYaw(float yaw){
		float cos = (float)Math.cos(Math.toRadians(yaw));
		float sin = (float)Math.sin(Math.toRadians(yaw));
		float _x = x * cos + z * sin;
		float _y = y;
		float _z = -x * sin + z * cos;
		return new Vector3i((int)_x, (int)_y, (int)_z);
	}
	
	public Vector3i rotatePitch(float pitch){
		float cos = (float)Math.cos(Math.toRadians(pitch));
		float sin = (float)Math.sin(Math.toRadians(pitch));
		float _x = x;
		float _y = y * cos - z * sin;
		float _z = y * sin + z * cos;
		return new Vector3i((int)_x, (int)_y, (int)_z);
	}
	
	public float dot(Vector3i other){
		return x * other.x + y * other.y + z * other.z;
	}
	
	public float length(){
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	@Override
	public String toString(){
		return x + ", " + y + ", " + z;
	}
	
	public boolean equals(Vector3i other){
		return x == other.x && y == other.y && z == other.z;
	}
	
}
