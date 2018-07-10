package kinect_frame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class kinect_frame extends JFrame{
	
	BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
	
	public kinect_frame() {
		super();
		paint_panel pp = new paint_panel();
		Dimension d = new Dimension(400, 800);
		pp.setSize(d);
		pp.setPreferredSize(d);
		pp.setMinimumSize(d);
		add(pp);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	public void setImg(BufferedImage img) {
		this.img = img;
	}

	
	class paint_panel extends JPanel{
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(img, 20, 20, this);
		}
	}
	
}
