package utils;

public class Stopwatch {
	
	public static long time = 0;
	private static long startTime = 0;
	private static Thread counter;
	
	public static void start() {
		startTime = System.currentTimeMillis();
		counter = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					time = System.currentTimeMillis() - startTime;
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		counter.start();
	}
	
	public static void stop() {
		counter.stop();
	}

}
