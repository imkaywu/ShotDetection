package com.blakay;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFileChooser;

public class LoadImage {
	private MainFrame _mainFrame;
	private File[] _input;
	
	public LoadImage(MainFrame mainFrame) {
		_mainFrame = mainFrame;
	}
	
	/**
     * Loads a picture to the workplace.
     */
    public void loadImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Please Choose JPG to Open");
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            _input = chooser.getSelectedFiles();

            sortFile(_input);
            
            for(File f : _input) {
        		System.out.println(f.getName());
        	}
        }
        else // if cancel
        {
            System.out.println("User canceled LOAD");
            _input = null;
            return;
        }
    }
    
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
