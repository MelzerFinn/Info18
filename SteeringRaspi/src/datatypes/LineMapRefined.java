package datatypes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import lejos.geom.*;

import lejos.robotics.mapping.LineMap;

public class LineMapRefined extends LineMap {

	public LineMapRefined(Line[] linesNew, Rectangle boundingRect) {
		super(linesNew, boundingRect);
	}

	public LineMapRefined() {
		super();
	}

	public LineMapRefined addLines(Line[] l) {
		Line[] lines = getLines();
		Line[] linesNew = concat(lines, l);
		return new LineMapRefined(linesNew, getBoundingRect());
	}

	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	@Override
	public void createSVGFile(String fileName) throws IOException {
		File mapFile = new File(fileName);
		FileOutputStream fos = new FileOutputStream(mapFile);
		PrintStream ps = new PrintStream(fos);
		ps.println("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + getBoundingRect().width + "px\" height=\""
				+ getBoundingRect().height + "px\" viewBox=\"" + getBoundingRect().x + " " + getBoundingRect().y + " "
				+ getBoundingRect().width + " " + getBoundingRect().height + "\">");
		ps.println("<g>");
		for (int i = 0; i < getLines().length; i++) {
			ps.println("<line stroke=\"#000000\" x1=\"" + getLines()[i].x1 + "\" y1=\"" + getLines()[i].y1 + "\" x2=\""
					+ getLines()[i].x2 + "\" y2=\"" + getLines()[i].y2 + "\"/>");
		}
		ps.println("<rect width=\"30\" height=\"30\" x=\"-15\" y=\"-15\" />");
		ps.println("</g>");
		ps.println("</svg>");
		ps.close();
		fos.close();
	}

}