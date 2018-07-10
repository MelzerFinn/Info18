package datatypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;

public class NXT {

	private NXTComm nxt;

	private DataInputStream in;
	private DataOutputStream out;

	public boolean hasFinished;

	public NXT() {
		try {
			nxt = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
		hasFinished = false;
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeInt(-666);
					in.close();
					out.close();
					nxt.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
	}

	public void connect(String name) {
		try {
			while (nxt.search(name).length<1) Thread.sleep(100);
			nxt.open(nxt.search(name)[0]);
			in = new DataInputStream(nxt.getInputStream());
			out = new DataOutputStream(nxt.getOutputStream());
		} catch (NXTCommException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int[] getUltrasonicDistance() {
		try {
			out.writeInt(2);
			out.flush();
			int vals[] = new int[5];
			vals[1] = in.readInt();
			vals[2] = in.readInt();
			vals[3] = in.readInt();
			vals[4] = in.readInt();
			return vals;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void rotateMotorByDegrees(String motor, double degree) {
		try {
			out.writeInt(1);
			out.flush();
			if (motor.toUpperCase()=="A") out.writeInt(1);
			if (motor.toUpperCase()=="B") out.writeInt(2);
			if (motor.toUpperCase()=="C") out.writeInt(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void waitTillFinished() {
		try {
			out.writeInt(3);
			out.flush();
			while (in.available()<1) {
				Thread.sleep(50);
			}
			in.readInt();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
