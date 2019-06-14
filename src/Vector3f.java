
public class Vector3f {
	
	public float x, y, z;
	
	public static final Vector3f zero = new Vector3f(0, 0, 0);
	
	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f clone(){
		return new Vector3f(x, y, z);
	}
	
	public Vector3f normalized(){
		float len = length();
		return new Vector3f(x / len, y / len, z / len);
	}
	
	public Vector3f add(Vector3f other){
		return new Vector3f(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3f sub(Vector3f other){
		return new Vector3f(x - other.x, y - other.y, z - other.z);
	}
	
	public Vector3f multScalar(float s){
		return new Vector3f(x * s, y * s, z * s);
	}
	
	public Vector3f rotateYaw(float yaw){
		float cos = (float)Math.cos(Math.toRadians(yaw));
		float sin = (float)Math.sin(Math.toRadians(yaw));
		float _x = x * cos + z * sin;
		float _y = y;
		float _z = -x * sin + z * cos;
		return new Vector3f(_x, _y, _z);
	}
	
	public Vector3f rotatePitch(float pitch){
		float cos = (float)Math.cos(Math.toRadians(pitch));
		float sin = (float)Math.sin(Math.toRadians(pitch));
		float _x = x;
		float _y = y * cos - z * sin;
		float _z = y * sin + z * cos;
		return new Vector3f(_x, _y, _z);
	}
	
	public float dot(Vector3f other){
		return x * other.x + y * other.y + z * other.z;
	}
	
	public float length(){
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	@Override
	public String toString(){
		return x + ", " + y + ", " + z;
	}
	
	public boolean equals(Vector3f other){
		return x == other.x && y == other.y && z == other.z;
	}
	
}
