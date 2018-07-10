package utils;

public class Converter {
	
	public static double degreeToSteeringRotation(double degree) {
		return 20160/360*degree;
	}
	
	public static double degreeToRobotRotation(double degree) {
		return centimetersToMotorRotation((27.2*Math.PI)/360*degree);
	}
	
	public static double centimetersToMotorRotation(double centimeters) {
		return 190.98593171*centimeters;
	}
	
	public static double[] rotatePoint(int x, int y, double rotation){
		double [] coords = new double [2];
		coords[0] = x*Math.cos(Math.toRadians(rotation))-y*Math.sin(Math.toRadians(rotation));
		coords[1] = y*Math.cos(Math.toRadians(rotation))+x*Math.sin(Math.toRadians(rotation));
		return coords;
	}
	
}
