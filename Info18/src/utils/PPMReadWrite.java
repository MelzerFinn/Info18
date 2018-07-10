package utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;


public class PPMReadWrite {
	
	public static BufferedImage readPGM(String filename) throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		for (int i = 0; i < 15; i++)fis.read();
		Dimension dimension = new Dimension(320,240);
		BufferedImage tmp = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		int c = 0;
		int x = -1;
		int y = 0;
		int b = 0;
		while ((b=fis.read())!=-1) {
			x++;
			if (x>=dimension.width) {
				x = 0;
				y++;
			}
			int gray = b;
			Color col = new Color(gray, gray, gray);
			tmp.setRGB(x, y, col.getRGB());
			c++;
		}
		return tmp;
	}
	
}