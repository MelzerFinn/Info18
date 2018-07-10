package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

/**
 * the main class
 */
public class Main {

	/**
	 * the main methods
	 * 
	 * @param args
	 *            unused
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				System.exit(1);
			}

			@Override
			public void buttonPressed(Button b) {
			}
		});

		USBConnection conn = USB.waitForConnection();
		System.out.println("connected");
		DataOutputStream dOut = conn.openDataOutputStream();
		DataInputStream dIn = conn.openDataInputStream();
		System.out.println("Entering loop");
		UltrasonicSensor s1 = new UltrasonicSensor(SensorPort.S1);
		UltrasonicSensor s2 = new UltrasonicSensor(SensorPort.S2);
		UltrasonicSensor s3 = new UltrasonicSensor(SensorPort.S3);
		UltrasonicSensor s4 = new UltrasonicSensor(SensorPort.S4);
		s1.continuous();
		s2.continuous();
		s3.continuous();
		s4.continuous();
		while (true) {
			while(dIn.available()<1) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int msg = dIn.readInt();
			System.out.println(msg);
			switch (msg) {
			case 1:
				int motor= dIn.readInt();
				int degree = dIn.readInt();
				if(motor==1) Motor.A.rotate(degree, true);
				if(motor==2) Motor.B.rotate(degree, true);
				if(motor==3) Motor.C.rotate(degree, true);
				break;
			case 2:
				dOut.writeInt(s1.getDistance());
				dOut.flush();
				dOut.writeInt(s2.getDistance());
				dOut.flush();
				dOut.writeInt(s3.getDistance());
				dOut.flush();
				dOut.writeInt(s4.getDistance());
				dOut.flush();
				System.out.println("sent!");
				break;
			case 3:
				while ((Motor.A.isMoving() & !Motor.A.isStalled()) || (Motor.B.isMoving() & !Motor.B.isStalled())
						|| (Motor.C.isMoving() & !Motor.C.isStalled())) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				dOut.writeInt(1);
				break;
			case -666:
				dIn.close();
				dOut.close();
				conn.close();
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
				break;
			default:
				break;
			}
			dOut.flush();
			System.out.println("done");
		}
	}
}
