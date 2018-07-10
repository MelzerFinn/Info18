package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import datatypes.LineMapRefined;
import datatypes.NXT_COLLAB;
import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.pc.comm.NXTCommException;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.PathFinder;
import utils.SQL;

public class MAIN {

	static LineMapRefined map = new LineMapRefined();

	static double minX = -100;
	static double maxX = 100;
	static double minY = -100;
	static double maxY = 100;
	static Waypoint wp;

	public static void main(String[] args)
			throws DestinationUnreachableException, IOException, NXTCommException, InterruptedException {

		NXT_COLLAB robot = new NXT_COLLAB("NXT-06", "NXTCore2");

		SQL.connect("localhost", 3306, "data", "robot", "InsaneSecurePassword123");
		SQL.init();

		new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean foundSuitableWaypoint = false;
				wp = new Waypoint(0, 0);
				while (true) {
					if (robot.isTurning) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					System.out.println("1");
					updateLineMap(updateSQL(robot.readNewDataSet()));
					System.out.println("2");
					PathFinder pf = new NodePathFinder(new AstarSearchAlgorithm(), new FourWayGridMesh(map, 10F, 30F));
					System.out.println("3");
					Path p = null;
					while (!foundSuitableWaypoint) {
						try {
							System.out.println("4");
							if (wp.distance(robot.getPose().getX(), robot.getPose().getY())<1)
								wp = getNextWaypoint();
							System.out.println(wp);
							p = pf.findRoute(robot.getPose(), wp);
							foundSuitableWaypoint = true;
							System.out.println("true");
						} catch (DestinationUnreachableException e) {
							foundSuitableWaypoint = false;
							System.out.println("false");
							wp = getNextWaypoint();
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					Waypoint currentWaypoint = p.get(1);
					System.out.println("Go to: " + currentWaypoint);
					robot.driveTo(currentWaypoint);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		System.out.println("0");
	}

	private static Waypoint getNextWaypoint() {
		Random r = new Random();
		System.out.println("Choosing between:");
		System.out.println("   Min Max");
		System.out.println("x: "+minX+" "+maxX);
		System.out.println("y: "+minY+" "+maxY);
		System.out.println("-----------------------");
		double x = minX + r.nextInt((int) (maxX - minX + 1));
		double y =  minX + r.nextInt((int) (maxX - minX + 1));
		System.out.println("Result: x: "+x+" y: "+y);
		return new Waypoint(x,y);
	}

	private static void updateLineMap(ArrayList<Node> newNodes) {

		ArrayList<Line> newLines = new ArrayList<>();
		for (Node n1 : newNodes) {
			newLines.add(new Line(n1.x, n1.y+0.1F, n1.x, n1.y+0.1F));
			maxX = Math.max(n1.x, maxX);
			maxY = Math.max(n1.y, maxY);
			minX = Math.min(n1.x, minX);
			minY = Math.min(n1.y, minY);
			for (Node n2 : n1.getNeighbors()) {
				newLines.add(new Line(n1.x, n1.y, n2.x, n2.y));
				maxX = Math.max(n1.x, maxX);
				maxY = Math.max(n1.y, maxY);
				minX = Math.min(n1.x, minX);
				minY = Math.min(n1.y, minY);
			}
		}
		map = new LineMapRefined(newLines.toArray(new Line[0]),
				new Rectangle((float) minX, (float) minY, (float) (maxX - minX), (float) (maxY - minY)));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					map.createSVGFile("outfile" + System.currentTimeMillis() + ".svg");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private static ArrayList<Node> updateSQL(ArrayList<Node> cloud) {
		for (Node dot : cloud) {
			SQL.save(dot.x, dot.y);
		}
		return cloud;
	}
}
