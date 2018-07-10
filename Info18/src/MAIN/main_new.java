package MAIN;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import kinectV1.Kinect;
import utils.DepthMap;
import utils.Logger;
import utils.Point3D;
import utils.SQL;

public class main_new {

    public static void main(String[] args) throws IOException, InterruptedException {
    	Logger.init("log.txt");
    	Logger.log("heeeeeyyyyyy");
    	System.out.println("hey");
        Kinect k = new Kinect();
        k.setTilt(30);
        Thread.sleep(5000);
        //average of ten
        DepthMap d = DepthMap.average(k.getDepthMap(), k.getDepthMap(), k.getDepthMap(), k.getDepthMap(),k.getDepthMap(), k.getDepthMap(), k.getDepthMap(), k.getDepthMap(), k.getDepthMap(), k.getDepthMap());
        SQL.setErrorReporting(SQL.ERROR_EXIT);
        SQL.connect("localhost", 3306, "data", "root", "");
        SQL.init();
		SQL.save(d.getPointCloud(new Point3D(0, 0, 0), 0, 0.0F));
        SQL.disconnect();
        Logger.log("SQL\n----------------------------------\n"+SQL.getLastException());
        Logger.flush();
    	System.out.println("hey");
    	k.exit();
    }
    
}