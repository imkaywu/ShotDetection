package com.blakay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ShotDetect implements Runnable {
	private static File [] _file;
	private ImageDistance _imageDist;
	private float [] _dist;
	private int _start_ind;
	private int _frame_num;
	
	public ShotDetect(MainFrame mainFrame, File[] files, float[] dist, int ind, int frame_num) {
		_file = files;
		_dist = dist;
		_start_ind = ind;
		_frame_num = frame_num;
		_imageDist = new ImageDistance(mainFrame.getValue(1), mainFrame.getValue(2), Integer.parseInt(mainFrame.getValue(4)));// multi-thread?
	}
	
	/*
	 * calculate the distance between neighboring frames using the selected method and color model
	 */
	public void run() {
		// TODO Auto-generated method stub
		if(_file.length - _start_ind != 1) { //not last frame
			if(_start_ind + _frame_num > _file.length) {
				_frame_num = _file.length - _start_ind - 1;
			}
			
			//_imageDist = new ImageDistance(_mainFrame.getValue(1), _mainFrame.getValue(2));//multi-thread?
			BufferedImage img1 = null, img2 = null;
			try {
				img1 = ImageIO.read(_file[_start_ind]);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i = _start_ind + 1; i <= _start_ind + _frame_num; i++) {
				try {
					img2 = ImageIO.read(_file[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				_dist[i - 1] = _imageDist.distanceValue(img1, img2);
				img1 = img2;
			}
		}
	}
}
