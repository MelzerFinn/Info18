package datatypes;

import java.util.ArrayList;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Node;
import utils.Converter;

public class NXT_COLLAB {

	public NXT nxt1;
	public NXT nxt2;

	boolean hasFinished1 = false;
	boolean hasFinished2 = false;

	public boolean isTurning = false;

	private Pose currentPose;

	public NXT_COLLAB(String name1, String name2) throws InterruptedException {
		nxt1 = new NXT();
		nxt2 = new NXT();

		nxt1.connect(name1);
		nxt2.connect(name2);

		currentPose = new Pose(0, 0, 0);
		
	}

	public void driveTo(Waypoint wp) {
		double xDist = wp.x - currentPose.getX();
		double yDist = wp.y - currentPose.getY();
		System.out.println("" + xDist);
		System.out.println("" + yDist);
		double angle = Math.toDegrees(Math.atan2(xDist, yDist));
		System.out.println("" + angle);
		if (angle != 0)
			turnTo(angle);
		nxt1.rotateMotorByDegrees("C",
				Converter.centimetersToMotorRotation(Math.sqrt((xDist * xDist) + (yDist * yDist))));
		nxt2.rotateMotorByDegrees("C",
				Converter.centimetersToMotorRotation(Math.sqrt((xDist * xDist) + (yDist * yDist))));
		waitTillActionFinished();
		if (angle != 0)
			turnTo(-angle);
		currentPose.setLocation(wp.x, wp.y);
	}

	public void turnTo(double degrees) {
		isTurning = true;
		double degrees45 = Converter.degreeToSteeringRotation(45);

		nxt1.rotateMotorByDegrees("A", degrees45);
		nxt1.rotateMotorByDegrees("B", -degrees45);
		nxt2.rotateMotorByDegrees("A", degrees45);
		nxt2.rotateMotorByDegrees("B", -degrees45);

		double currentAngle = currentPose.getHeading();
		double toTurn1 = currentAngle - degrees;
		double toTurn2 = degrees - currentAngle;
		double toTurn;
		if (Math.abs(toTurn1) > Math.abs(toTurn2))
			toTurn = toTurn2;
		else
			toTurn = toTurn1;

		waitTillActionFinished();

		nxt1.rotateMotorByDegrees("C", Converter.degreeToRobotRotation(toTurn));
		nxt2.rotateMotorByDegrees("C", Converter.degreeToRobotRotation(-toTurn));
		waitTillActionFinished();

		nxt1.rotateMotorByDegrees("A", -degrees45);
		nxt2.rotateMotorByDegrees("B", degrees45);
		nxt1.rotateMotorByDegrees("A", -degrees45);
		nxt2.rotateMotorByDegrees("B", degrees45);

		waitTillActionFinished();

		isTurning = false;
	}

	public ArrayList<Node> readNewDataSet() {
		ArrayList<Node> list = new ArrayList<>();
		// 1.1 : x-9 y+15
		// 1.2 : x+9 y+15
		// 1.3 : x-15 y+9
		// 1.4 : x+15 y+9
		// 2.1 : x+9 y-15
		// 2.2 : x-9 y-15
		// 2.3 : x+15 y-9
		// 2.4 : x-15 y-9
		int val1[] = nxt1.getUltrasonicDistance();
		int val2[] = nxt2.getUltrasonicDistance();
		System.out.println("1.1: " + val1[1]);
		System.out.println("1.2: " + val1[2]);
		System.out.println("1.3: " + val1[3]);
		System.out.println("1.4: " + val1[4]);
		System.out.println("2.1: " + val2[1]);
		System.out.println("2.2: " + val2[2]);
		System.out.println("2.3: " + val2[3]);
		System.out.println("2.4: " + val2[4]);
		double x = currentPose.getX();
		double y = currentPose.getY();
		double heading = currentPose.getHeading();
		double[] coords = new double[2];
		if (val1[1] > 0 && val1[1] < 230)
			coords=Converter.rotatePoint(-9, +15+val1[1], heading);
			list.add(new SelfConnectingNode(x+coords[0],y+coords[1], 10F));
		if (val1[2] > 0 && val1[2] < 230)
			coords=Converter.rotatePoint(+9, +15+val1[2], heading);
		list.add(new SelfConnectingNode(x+coords[0],y+coords[1], 10F));
		if (val1[3] > 0 && val1[3] < 230)
			coords=Converter.rotatePoint(-15-val1[3], +9, heading);
		list.add(new SelfConnectingNode(x+coords[0],y+coords[1], 10F));
		if (val1[4] > 0 && val1[4] < 230)
			coords=Converter.rotatePoint(+15+val1[4], +9, heading);
		list.add(new SelfConnectingNode(x+coords[0],y+coords[1], 10F));
		if (val2[1] > 0 && val2[1] < 230)
			coords=Converter.rotatePoint(+9, -15-val2[1], heading);
		list.add(new SelfConnectingNode(x+coords[0],y+coords[1], 10F));
		if (val2[2] > 0 && val2[2] < 230)
			coords=Converter.rotatePoint(-9, -15-val2[2], heading);
		list.add(new SelfConnectingNode(x+coords[0],y+coords[1], 10F));
		if (val2[3] > 0 && val2[3] < 230)
			coords=Converter.rotatePoint(+15+val2[3], -9, heading);
		list.add(new SelfConnectingNode(x+coords[0],y+coords[1], 10F));
		if (val2[4] > 0 && val2[4] < 230)
			coords=Converter.rotatePoint(-15-val2[4], -9, heading);
		list.add(new SelfConnectingNode(x+coords[0],y+coords[1], 10F));

		return list;
	}

	public Pose getPose() {
		return currentPose;
	}

	public void waitTillActionFinished() {
		while (true) {
			if (nxt1.hasFinished && nxt2.hasFinished)
				break;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("NXT has finished action.");
		printTrace();
	}

	private void printTrace() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (int i = 1; i < elements.length; i++) {
			StackTraceElement s = elements[i];
			System.out.println("\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":"
					+ s.getLineNumber() + ")");
		}
	}
}
