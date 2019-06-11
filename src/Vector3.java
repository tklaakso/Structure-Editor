
public class Vector3 {
	
	public float x, y, z;
	
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
	
	public float dot(Vector3 other){
		return x * other.x + y * other.y + z * other.z;
	}
	
	public float length(){
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
}
