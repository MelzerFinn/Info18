package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import javax.imageio.ImageIO;

public class RGBImage {

	int maxY;
	int maxX;
	
	int[][] myR;
	int[][] myG;
	int[][] myB;
	
	public RGBImage(int[][] red, int[][] green, int[][] blue){
		myR = red;
		myG = green;
		myB = blue;
		maxY = red[0].length;
		maxX = red.length;
	}

	public int getmaxY() {
		return maxY;
	}

	public void setmaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getmaxX() {
		return maxX;
	}

	public void setmaxX(int maxX) {
		this.maxX = maxX;
	}

	public int[][] getRed() {
		return myR;
	}

	public void setRed(int[][] red) {
		this.myR = red;
	}

	public int[][] getGreen() {
		return myG;
	}

	public void setGreen(int[][] green) {
		this.myG = green;
	}

	public int[][] getBlue() {
		return myB;
	}

	public void setBlue(int[][] blue) {
		this.myB = blue;
	}
	
	public BufferedImage getImage() throws IOException {
		BufferedImage tmp = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < maxY; y++) {
			for (int x = 0; x < maxX; x++) {
				Color c = new Color(myR[x][y], myG[x][y], myB[x][y]);
				tmp.setRGB(x, y, c.getRGB());
			}
		}
		return tmp;
	}
	
}