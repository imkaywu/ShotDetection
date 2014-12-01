package com.blakay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFileChooser;

import org.jfree.ui.RefineryUtilities;

public class LoadVideoListener implements ActionListener{
	private MainFrame _mainFrame;
	private File[] _input;
	private String _folder;

	public LoadVideoListener(MainFrame mainFrame) {
		_mainFrame = mainFrame;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		loadImage();
		if(_input != null) {
			new Thread(new VideoSegmentation(_mainFrame, _input, _folder)).start();// VideoSegmentation.class implements Runnable, the GUI won't freeze when processing the video.
		}
	}
	
	/*
	 * choose the frames of a certain video
	 */
	private void loadImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Please Choose JPG to Open");
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            _input = chooser.getSelectedFiles();
            
            String path = chooser.getSelectedFile().getAbsolutePath();
            int ind = path.indexOf("frame_");
            _folder = path.substring(23, ind - 1);//23 is the start character of the folder
            _mainFrame.setValue(0, _folder);
            _mainFrame.setValue(3, Integer.toString(_input.length - 1));
       
            sortFile(_input);
            /*
            for(File f : _input) {
        		System.out.println(f.getName());
        	}*/
        }
        else // if cancel
        {
            System.out.println("User canceled LOAD");
            _input = null;
            return;
        }
    }
    
	/*
	 * set the order of the files in NUMERIC, not ALPHABETIC order
	 */
    private void sortFile(File [] files) {
    	Arrays.sort(files, new Comparator<File>() {

			public int compare(File arg0, File arg1) {
				// TODO Auto-generated method stub
				int ind1 = getInd(arg0.getName());
				int ind2 = getInd(arg1.getName());
				return ind1 - ind2;
			}
			
			private int getInd(String name) {
				int i = 0;
				try{
					int s = name.indexOf('_') + 1;
					int e = name.lastIndexOf('.');
					String number = name.substring(s, e);
					i = Integer.parseInt(number);
				} catch(Exception e) {
					i = 0;
				}
				return i;
			}
    	});
    }

}
