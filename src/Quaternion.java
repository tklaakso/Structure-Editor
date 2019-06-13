/******************************************************************************
 *  Compilation:  javac Quaternion.java
 *  Execution:    java Quaternion
 *
 *  Data type for quaternions.
 *
 *  http://mathworld.wolfram.com/Quaternion.html
 *
 *  The data type is "immutable" so once you create and initialize
 *  a Quaternion, you cannot change it.
 *
 *  % java Quaternion
 *
 ******************************************************************************/

public class Quaternion {
	
    public final float x, y, z, w;
    
    public static final Quaternion identity = new Quaternion(0, 0, 0, 1);

    // create a new object with the given components
    public Quaternion(float x0, float x1, float x2, float x3) {
        this.x = x0;
        this.y = x1;
        this.z = x2;
        this.w = x3;
    }

    // return the quaternion norm
    public float norm() {
        return (float)Math.sqrt(x*x + y*y + z*z + w*w);
    }

    // return the quaternion conjugate
    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    // return a new Quaternion whose value is (this + b)
    public Quaternion plus(Quaternion b) {
        Quaternion a = this;
        return new Quaternion(a.x+b.x, a.y+b.y, a.z+b.z, a.w+b.w);
    }


    // return a new Quaternion whose value is (this * b)
    public Quaternion times(Quaternion b) {
        Quaternion a = this;
        float y0 = a.w*b.w - a.x*b.x - a.y*b.y - a.z*b.z;
        float y1 = a.w*b.x + a.x*b.w + a.y*b.z - a.z*b.y;
        float y2 = a.w*b.y - a.x*b.z + a.y*b.w + a.z*b.x;
        float y3 = a.w*b.z + a.x*b.y - a.y*b.x + a.z*b.w;
        return new Quaternion(y1, y2, y3, y0);
    }

    // return a new Quaternion whose value is the inverse of this
    public Quaternion inverse() {
        float d = x*x + y*y + z*z + w*w;
        return new Quaternion(-x/d, -y/d, -z/d, w/d);
    }


    // return a / b
    // we use the definition a * b^-1 (as opposed to b^-1 a)
    public Quaternion divides(Quaternion b) {
        Quaternion a = this;
        return a.times(b.inverse());
    }
    
    /**
     * Produces a quaternion which rotates a Vector3 angle degrees about axis
     * @param angle Angle to rotate, in degrees
     * @param axis Axis to rotate about
     * @return
     */
    public static Quaternion angleAxis(float angle, Vector3 axis){
    	float sinAngle = (float)Math.sin(Math.toRadians(angle / 2));
    	float cosAngle = (float)Math.cos(Math.toRadians(angle / 2));
    	return new Quaternion(axis.x * sinAngle, axis.y * sinAngle, axis.z * sinAngle, cosAngle);
    }
    
    public float getPitch(){
    	return (float)Math.toDegrees(Math.atan2(2.0 * (y * z + w * x), w * w - x * x - y * y + z * z));
    }
    
    public float getYaw(){
    	return (float)Math.toDegrees(Math.asin(-2.0 * (x * z - w * y)));
    }
    
    public float getRoll(){
    	return (float)Math.toDegrees(Math.atan2(2.0 * (x * y + w * z), w * w + x * x - y * y - z * z));
    }

}