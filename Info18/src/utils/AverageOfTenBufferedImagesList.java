package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AverageOfTenBufferedImagesList {
	
	private ArrayList<BufferedImage> list;
	
	public AverageOfTenBufferedImagesList() {
		list = new ArrayList<>();
	}
	
	public void add(BufferedImage img) {
		if (list.size() > 9) {
			list.remove(0);
		}
		list.add(img);
	}
	
	public BufferedImage getAvg() {
		if (list.size()<1) return null;
		BufferedImage tmp = new BufferedImage(list.get(0).getWidth(), list.get(0).getHeight(), list.get(0).getType());
		for (int y = 0; y < tmp.getHeight(); y++) {
			for (int x = 0; x < tmp.getWidth(); x++) {
				int darkest = 255;
				for (int i = 0; i < list.size(); i++) {
					darkest = Math.min(Color.decode(""+list.get(i).getRGB(x, y)).getRed(), darkest);
				}
				tmp.setRGB(x, y, new Color(darkest, darkest, darkest).getRGB());
			}
		}
		return tmp;
	}
	
	public BufferedImage[] fillGaps(BufferedImage img, int radius, BufferedImage marked) {
		BufferedImage tmp = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int y = 1; y < img.getHeight()-1; y++) {
			for (int x = 1; x < img.getWidth()-1; x++) {
				tmp.setRGB(x, y, img.getRGB(x, y));
				if(marked.getRGB(x, y)!=Color.MAGENTA.getRGB()) marked.setRGB(x, y, img.getRGB(x, y));
				if (Color.decode(""+img.getRGB(x, y)).getRed()>250) {
					ArrayList<Color> cols = new ArrayList<>();
					for (int yOffset = -radius; yOffset <= radius; yOffset++) {
						for (int xOffset = -3; xOffset < 4; xOffset++) {
							try {
								Color c = Color.decode(""+img.getRGB(x+xOffset, y+yOffset));
								cols.add(c);
							} catch (ArrayIndexOutOfBoundsException e) {}
						}
					}
					int darkCounter = 0;
					int lightCounter = 0;
					for (int i = 0; i < cols.size(); i++) {
						if (cols.get(i).getRed()>250) lightCounter++;
						else darkCounter++;
					}
					if(darkCounter>lightCounter) {
						int avg = 0;
						for (int i = 0; i < cols.size(); i++) {
							if(!(cols.get(i).getRed()>250))
							avg += cols.get(i).getRed();
						}
						avg /= darkCounter;
						tmp.setRGB(x, y, (new Color(avg, avg, avg).getRGB()));
						marked.setRGB(x, y, Color.MAGENTA.getRGB());
					}
				}
			}
		}
		BufferedImage[] arr = {tmp,marked};
		return arr;
	}

}
