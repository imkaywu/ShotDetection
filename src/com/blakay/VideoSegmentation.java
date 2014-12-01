package com.blakay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jfree.ui.RefineryUtilities;

public class VideoSegmentation implements Runnable{
	private MainFrame _mainFrame;
	private File [] _input;
	private String _folder;
	
	public VideoSegmentation(MainFrame mainFrame, File [] input, String folder) {
		_mainFrame = mainFrame;
		_input = input;
		_folder = folder;
	}

	/*
	 * 1. create several threads to calcalate the distance between neighboring frames,
	 * each thread process (frame_num + 1) frames, get (frame_num) distances.
	 * 2. draw the distance in a dot-line plot(to determine the threshold)
	 * 3. extract key frames
	 * 4. update UI - add the processed video and corresponding key frames to JScollPane
	 */
	public void run() {
		// TODO Auto-generated method stub
		int frame_num = 500;
		int num = _input.length / (frame_num + 1) + 1;
		float [] dist = new float[_input.length - 1];
		Thread [] shotDetect = new Thread [num];
		for(int i = 0; i < num; i++){
			shotDetect[i] = new Thread(new ShotDetect(_mainFrame, _input, dist, i * frame_num, frame_num));
			shotDetect[i].start();
		}
		for(Thread t : shotDetect) {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int i = 1;
		for(float d : dist) {
			System.out.println("dist" + i + ": " + d);
			i++;
		}
		
		final DistChart distChart = new DistChart("Distance between neighboring frames", dist);
		distChart.pack();
		RefineryUtilities.centerFrameOnScreen(distChart);
		distChart.setVisible(true);
		
		write2File(dist);
		
		//new KeyFrameExtraction(_mainFrame, dist, _folder).keyFrameExtract();
		
		//_mainFrame.updateUI(_folder);
	}
	
	private void write2File(float [] dist) {
		int i = 0;
		
		Path path = Paths.get("D:/Videos/key frames/" + _folder + "/" + _mainFrame.getValue(1) + "/" + _mainFrame.getValue(2));
		File folder = new File(path.toString());
		if(!Files.exists(path))
			folder.mkdirs();
		
		File file = new File(path.toString() + "/dist.txt");
		BufferedWriter bw = null;
		if(file.exists()) {
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
				while(i < dist.length) {
					bw.write("" + dist[i++]);
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
			try {
				bw = new BufferedWriter(new FileWriter(path.toString() + "/dist.txt"));
				while(i < dist.length) {
					bw.write("" + dist[i++]);
					bw.newLine();
					bw.flush();
				}
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
