package utils;

public class Point3D {

	private double x = 0;
	private double y = 0;
	private double z = 0;
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(Point3D base, double radius, double theta, double phi) {
		theta = Math.toRadians(theta);
		phi = Math.toRadians(phi);
		double translateX = radius* Math.sin(theta)*Math.cos(phi)*2;
		double translateY = radius*Math.cos(theta)*2;
		double translateZ = radius*Math.sin(theta)*Math.sin(phi)*2;
		this.x = base.x+translateX;
		this.y = base.y+translateY;
		this.z = base.z+translateZ;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public String toString() {
		return "Point3D [ x="+x+" ; y="+y+" ; z="+z+" ]";
	}
	
	public String toJSONString() {
		return "{\"x\": "+x+", \"y\": "+y+", \"z\": "+z+"}";
	}
	
}
