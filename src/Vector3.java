
public class Vector3 {
	
	public float x, y, z;
	
	public static final Vector3 zero = new Vector3(0, 0, 0);
	
	public Vector3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3 clone(){
		return new Vector3(x, y, z);
	}
	
	public Vector3 normalized(){
		float len = length();
		return new Vector3(x / len, y / len, z / len);
	}
	
	public Vector3 add(Vector3 other){
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3 sub(Vector3 other){
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	public Vector3 multScalar(float s){
		return new Vector3(x * s, y * s, z * s);
	}
	
	public Vector3 rotateYaw(float yaw){
		float cos = (float)Math.cos(Math.toRadians(yaw));
		float sin = (float)Math.sin(Math.toRadians(yaw));
		float _x = x * cos + z * sin;
		float _y = y;
		float _z = -x * sin + z * cos;
		return new Vector3(_x, _y, _z);
	}
	
	public Vector3 rotatePitch(float pitch){
		float cos = (float)Math.cos(Math.toRadians(pitch));
		float sin = (float)Math.sin(Math.toRadians(pitch));
		float _x = x;
		float _y = y * cos - z * sin;
		float _z = y * sin + z * cos;
		return new Vector3(_x, _y, _z);
	}
	
	public float dot(Vector3 other){
		return x * other.x + y * other.y + z * other.z;
	}
	
	public float length(){
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	@Override
	public String toString(){
		return x + ", " + y + ", " + z;
	}
	
	public boolean equals(Vector3 other){
		return x == other.x && y == other.y && z == other.z;
	}
	
}
