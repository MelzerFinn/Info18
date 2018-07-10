package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class DepthMap {
	
	private float[][] map;
	
	private boolean v2 = false;
	
	private final Dimension ImgV1 = new Dimension(640, 480);
	private final Dimension ImgV2 = new Dimension(1920, 1080);
	
	private final double HorizontalAnglePerPixelV1 = 58.5D/ImgV1.width;
	private final double VerticalAnglePerPixelV1 = 46.6D/ImgV1.height;
	
	private final double HorizontalAnglePerPixelV2 = 70.6D/ImgV2.width;
	private final double VerticalAnglePerPixelV2 = 60D/ImgV2.height;
	
	private final double HorizontalAnglePerPixel;
	private final double VerticalAnglePerPixel;
	private final Dimension DepthImageDimension;


	public static DepthMap average(int[]... maps) {
		float[] tmpMap = new float[maps[0].length];
		
		for(int c = 0; c<maps[0].length;c++) {
			int nearest = Integer.MAX_VALUE;
			for (int[] map : maps) {
				nearest = Math.min(map[c], nearest);
			}
			tmpMap[c]=nearest;
		}
		
		return new DepthMap(tmpMap);
	}
	
	public static DepthMap average(float[]... maps) {
		
		float[] tmpMap = new float[maps[0].length];
		
		for(int c = 0; c<maps[0].length;c++) {
			float nearest = Float.MAX_VALUE;
			for (float[] map : maps) {
				nearest = Math.min(map[c], nearest);
			}
			tmpMap[c]=nearest;
		}
		
		return new DepthMap(tmpMap);
	}
	
	public DepthMap(float[] map) {
		this(map, false);
	}
	
	public DepthMap(float[] map, boolean isV2) {
		v2 = isV2;
		if(isV2) VerticalAnglePerPixel = VerticalAnglePerPixelV2;
		else VerticalAnglePerPixel = VerticalAnglePerPixelV1;
		if(isV2) HorizontalAnglePerPixel = HorizontalAnglePerPixelV2;
		else HorizontalAnglePerPixel = HorizontalAnglePerPixelV1;
		if(isV2) DepthImageDimension = ImgV2;
		else DepthImageDimension = ImgV1;
		
		this.map = new float[DepthImageDimension.width][DepthImageDimension.height];
		
		int x = 0;
		int y = 0;
		
		BufferedImage img = new BufferedImage(DepthImageDimension.width, DepthImageDimension.height, 1);
		float max = 0;
		for (float i : map) {
			this.map[x][y] = i;
			max = Math.max(i, max);
			img.setRGB(x, y, ColorFromDepth(i));
			x++;
			if(x>=DepthImageDimension.width) {
				y++;
				x = 0;
			}
			if(y>DepthImageDimension.height) {
				System.err.println("Expected Array with "+DepthImageDimension.width*DepthImageDimension.height+" values. Got "+map.length+". Skipping remaining values");
				break;
			}
		}
		
		try {
			ImageIO.write(img, "png", new File("output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(max);
	}
	
	private int ColorFromDepth(float depth) {
		return Color.HSBtoRGB(depth, 1.0F, 1.0F);
	}
	
	private float[][] getArea(int x, int y, int radius){
		float[][] tmp = new float[radius*2][radius*2];
		for (int i = -radius; i<=radius; i++) {
			for (int j = -radius; j<=radius; j++) {
				try {
					tmp[i+radius][j+radius] = map[x+i][y+j];
				} catch (ArrayIndexOutOfBoundsException e) {
					tmp[i+radius][j+radius] = 0;
				} 
			}
		}
		return tmp;
	}
	
	private void refineEdges(){
		final int radius = 2;
		for (int x = radius-1; x < DepthImageDimension.getWidth()-2; x++) {
			for (int y = radius-1; y < DepthImageDimension.getHeight()-2; y++) {
				float[][] tmp = getArea(x, y, radius);
				
			}
		}
	}
	
	public Point3D[] getPointCloud(Point3D position, double centerAngle, double rotation) {
		ArrayList<Point3D> cloud = new ArrayList<>();
		/*for (int y = 0; y < DepthImageDimension.getHeight(); y+=20) {
			double rowAngle = 90-(centerAngle+(0.5*DepthImageDimension.getHeight()*VerticalAnglePerPixel)-(y*VerticalAnglePerPixel));
			double VerticalCorrectionAngle = (-0.5D*DepthImageDimension.getHeight()*VerticalAnglePerPixel)+(VerticalAnglePerPixel*y);
			double VerticalCorrectionFactor = 1D/Math.cos(Math.toRadians(VerticalCorrectionAngle));
			System.out.println(VerticalCorrectionFactor);
			for (int x = 0; x < DepthImageDimension.getWidth(); x+=20) {
				double columnAngle = ((-0.5*DepthImageDimension.getWidth()*HorizontalAnglePerPixel)+(x*HorizontalAnglePerPixel)+rotation)+90;
				double HorizontalCorrectionAngle = (-0.5D*DepthImageDimension.getWidth()*HorizontalAnglePerPixel)+(HorizontalAnglePerPixel*y);
				double HorizontalCorrectionFactor = 1D/Math.cos(Math.toRadians(HorizontalCorrectionAngle));
				if(map[x][y]>0&&map[x][y]<4) {
					double correctedRadius = map[x][y];//*HorizontalCorrectionFactor*VerticalCorrectionFactor;
					Point3D p = new Point3D(position, correctedRadius, rowAngle, columnAngle);
					cloud.add(p);
				}
			}
		}*/
		
		for (int y = 0; y < DepthImageDimension.getHeight(); y+=20) {
			double yAngleInPicture = (-0.5*DepthImageDimension.getHeight()*VerticalAnglePerPixel)+(y*VerticalAnglePerPixel);
			double yCorrectionFactor = cos(yAngleInPicture);
			for (int x = 0; x < DepthImageDimension.getWidth(); x+=20) {
				if(map[x][y]>0&&map[x][y]<4) {
					double xAngleInPicture = (-0.5*DepthImageDimension.getWidth()*HorizontalAnglePerPixel)+(x*HorizontalAnglePerPixel);
					double xCorrectionFactor = cos(xAngleInPicture);
					double correctedRadius = map[x][y]*yCorrectionFactor*xCorrectionFactor;
					Point3D p = new Point3D(position, correctedRadius, yAngleInPicture, xAngleInPicture);
					cloud.add(p);
				}
			}
		}
		return cloud.toArray(new Point3D[0]);
	}
	
	public double cos(double degree) {
		return Math.cos(Math.toRadians(degree));
	}
	
}
