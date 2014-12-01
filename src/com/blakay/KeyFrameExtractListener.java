package com.blakay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KeyFrameExtractListener implements ActionListener{
	
	private MainFrame _mainFrame;
	private float [] dist;
	
	public KeyFrameExtractListener(MainFrame mainFrame) {
		_mainFrame = mainFrame;
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String folder = _mainFrame.getValue(0);
		
		int size;
		if(_mainFrame.getValue(3) != null) {
			size = Integer.parseInt(_mainFrame.getValue(3));
		
			dist = new float[size];
			readDist(dist, folder);
			
			new KeyFrameExtraction(_mainFrame, dist, folder).keyFrameExtract();
			
			_mainFrame.updateUI(folder);
		}
		
	}
	
	private void readDist(float [] dist, String folder) {
		BufferedReader readD = null;
		
		String method, colorModel, line;
		method = _mainFrame.getValue(1);
		colorModel = _mainFrame.getValue(2);
		
		int i = 0;
		
		try {
			readD = new BufferedReader(new FileReader("D:/Videos/key frames/" + folder + "/" + method + "/" + colorModel + "/" + "dist.txt"));
			
			while ((line = readD.readLine()) != null) {
				dist[i++] = Float.parseFloat(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (readD != null)
					readD.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
