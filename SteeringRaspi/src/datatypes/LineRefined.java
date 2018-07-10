package datatypes;

import lejos.geom.Line;

public class LineRefined extends Line {

	public LineRefined(float x1, float y1, float x2, float y2) {
		super(x1, y1, x2, y2);
	}
	
	public String toString() {
		return "Line: [from: ("+x1+"|"+y1+"), to: ("+x2+"|"+y2+") ]";
	}

}
