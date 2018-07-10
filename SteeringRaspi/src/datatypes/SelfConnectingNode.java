package datatypes;

import java.util.ArrayList;
import java.util.Collection;

import lejos.robotics.pathfinding.Node;

public class SelfConnectingNode extends Node {

	float distance;

	static ArrayList<SelfConnectingNode> list = new ArrayList<>();

	public SelfConnectingNode(double x, double y, float distance) {
		super((float) x, (float) y);
		list.add(this);
		this.distance = distance;
		for (SelfConnectingNode n : list) {
			double xDiff = Math.abs(n.x - this.x);
			double yDiff = Math.abs(n.y - this.y);
			double totalDiff = Math.sqrt((yDiff * yDiff) + (xDiff + xDiff));
			if (totalDiff < distance) {
				n.addNeighbor(this);
				this.addNeighbor(n);
			}
		}
	}

	public String toString() {
		return "Node(" + x + "|" + y + ")";
	}
}
