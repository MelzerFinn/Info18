package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.PathFinder;

public class trystuffoutmain {

	public static void main(String[] args) throws IOException, DestinationUnreachableException {

		Line [] lines = new Line[5];
		lines [0] = new Line(75f, 100f, 100f, 100f);
		lines [1] = new Line(100f, 100f, 87f, 75f);
		lines [2] = new Line(87f, 75f, 75f, 100f);
		lines[3] = new Line(0,100,100,100);
		lines [4] = new Line(100, 0, 100, 100);
		lejos.geom.Rectangle bounds = new Rectangle(-50, -50, 250, 250);
		LineMap myMap = new LineMap(lines, bounds);
		
		// Use a regular grid of node points. Grid space = 20. Clearance = 15:
		FourWayGridMesh grid = new FourWayGridMesh(myMap, 10, 10);
		
		// Use A* search:
		AstarSearchAlgorithm alg = new AstarSearchAlgorithm();
		
		// Give the A* search alg and grid to the PathFinder:
		PathFinder pf = new NodePathFinder(alg, grid);
		
		myMap.createSVGFile("file.svg");
		
		Path p = pf.findRoute(new Pose(0, 0, 0), new Waypoint(190,150));
		
		for (Waypoint wp : p) {
			System.out.println(wp);
		}

		BufferedImage img = ImageIO.read(new File("file.png"));
		for (Waypoint wp : p) {
			img.setRGB((int)wp.x+50, (int)wp.y+50, Color.BLUE.getRGB());
		}
		ImageIO.write(img, "png", new File("file.png"));
		
		
		
	}

}
