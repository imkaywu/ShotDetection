package com.blakay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class KeyFrameExtraction {
	private MainFrame _mainFrame;
	private float [] _dist;
	private String _folder;
	private int _ind = 1;
	private float _threshold;
	
	public KeyFrameExtraction(MainFrame mainFrame, float [] dist, String folder) {
		_mainFrame = mainFrame;
		_dist = dist;
		_folder = folder;
		_threshold = Float.parseFloat(mainFrame.getValue(5));
	}
	
	/*
	 * get the frames passing a certain threshold as key frames 
	 */
	public void keyFrameExtract(){
		getKeyFrame(1);
		//global: 0.0125, Cumulative: 0.056, Simple Histogram: 0.056
		System.out.println("The index of the key frames are:");
		for(int i = 2; i <= _dist.length; i++) {
			if(_dist[i - 1] >= _threshold) {
				getKeyFrame(i + 1);
				System.out.println("" + (i + 1));
			}
		}
		_mainFrame.setValue(4, Integer.toString(_ind));
	}
	
	/*
	 * store the key frames in the designated directory
	 */
	private void getKeyFrame(int i) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("D:/Videos/video frames/" + _folder + "/frame_" + i + ".jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Path path = Paths.get("D:\\Videos\\key frames\\" + _folder + "\\" + _mainFrame.getValue(1) + "\\" + _mainFrame.getValue(2));
		/*
		File folder = new File(path.toString());
		if(!Files.exists(path))
			folder.mkdirs();
		*/
		File outputfile = new File(path.toString() + "/frame_" + _ind + ".jpg");
		
		try {
			ImageIO.write(img, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_ind++;
	}
}
