package MAIN;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.openkinect.processing.Kinect;
import org.openkinect.processing.Kinect2;

import kinect_frame.kinect_frame;
import utils.AverageOfTenBufferedImagesList;
import utils.Logger;
import utils.PPMReadWrite;
import utils.RGBImage;
import utils.Stopwatch;

public class main extends JFrame {

	static BufferedImage img;
	static kinect_frame frame;
	static AverageOfTenBufferedImagesList avgList;

	public static void main(String[] args) throws IOException {

		Logger.init("log.txt");

		avgList = new AverageOfTenBufferedImagesList();

		frame = new kinect_frame();

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				try {
					capture();
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		});
	}

	public static void capture() throws Exception {
		/*
		 * for(int i = 0; i<10; i++) { File f = new File("outfile"+i+".pgm");
		 * f.delete(); f = new File("outfile"+i+".pgm"); f.delete(); }
		 * Runtime.getRuntime().
		 * exec("streamer -c /dev/video1 -b 16 -o outfile0.pgm -t 10");
		 */for (int k = 0; k < 10; k++) {
			File f = new File("outfile" + k + ".pgm");
			while (!f.canRead())
				Thread.sleep(10);
			avgList.add(PPMReadWrite.readPGM("outfile" + k + ".pgm"));
		}
		frame.setImg(avgList.getAvg());
		frame.repaint();
		/*int mIterations = 20;
		int lIterations = 20;
		BufferedImage image = new BufferedImage(320 * mIterations + (mIterations+1) * 20, 240 * lIterations + (lIterations+1) * 20, BufferedImage.TYPE_INT_RGB);
		BufferedImage image2 = new BufferedImage(320 * mIterations + (mIterations+1) * 20, 240 * lIterations + (lIterations+1) * 20, BufferedImage.TYPE_INT_RGB);
		for (int l = 1; l <= lIterations; l++) {
			BufferedImage img = avgList.getAvg();
			BufferedImage[] res = {img,img};
			for (int m = 1; m <= mIterations; m++) {
				res = avgList.fillGaps(res[0], l, res[1]);
				image.createGraphics().drawImage(res[0], 20 + (m - 1) * (20 + img.getWidth()),
						20 + (l - 1) * (20 + img.getHeight()), null);
				image2.createGraphics().drawImage(res[1], 20 + (m - 1) * (20 + img.getWidth()),
						20 + (l - 1) * (20 + img.getHeight()), null);
				System.out.println(l + " " + m);
			}
		}

		ImageIO.write(image, "png", new File("output_1_test" + new Random().nextInt() + ".png"));
		ImageIO.write(image2, "png", new File("output_2_test" + new Random().nextInt() + ".png"));
		*/
		ImageIO.write(avgList.getAvg(), "pgm", new File("refined_output.pgm"));
		System.out.println("finished");
	}
}
