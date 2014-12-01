package com.blakay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainFrame extends JFrame{
	private static final int WIDTH = 740, HEIGHT = 362;
	private static MainFrame _mainFrame;
	private String _method = "Global Pixel2Pixel Difference", _colorModel = "YIQ";
	private String _folder = null, _size = null, _keyframeSize;
	private JLabel thresholdL, binL;
	private JTextField thresholdT, binT;
	private JButton loadVideoB, keyFrameB;
	private JPanel inputP, panel;
	private JScrollPane jScrollPane;
	private BufferedReader readVideo;
	private ArrayList<HashMap<String, String>> videoDir;
	
	public MainFrame() {
		setLookAndFeel();
		init();
		init_component();
		init_scollpane();
	}
	
	/*
	 * set the attributes of the JFrame window
	 */
	private void init() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int h = (int) d.getHeight() / 4;
		int w = (int) d.getWidth() / 4;
		this.setLocation(w, h);
		this.setTitle("Video indexing and browsing");
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//this.pack();
	}
	
	/*
	 * create and initialize the components of the GUI
	 */
	private void init_component(){
		this.setLayout(new BorderLayout());
		
		inputP = new JPanel();
		inputP.setLayout(new GridLayout());
		this.add(inputP, BorderLayout.NORTH);
		
		loadVideoB = new JButton("Load Video");
		inputP.add(new JLabel(""));
		inputP.add(loadVideoB);
		
		keyFrameB = new JButton("Extract KeyFrame");
		inputP.add(keyFrameB);
		inputP.add(new JLabel(""));
		
		thresholdL = new JLabel("Threshold: ", SwingConstants.RIGHT);
		inputP.add(thresholdL);
		thresholdT = new JTextField(SwingConstants.CENTER);
		thresholdT.setText("0.0125");
		inputP.add(thresholdT);
		
		binL = new JLabel("Bin Number: ", SwingConstants.RIGHT);
		inputP.add(binL);
		binT = new JTextField(SwingConstants.CENTER);
		binT.setText("16");
		inputP.add(binT);
	}
	
	/*
	 * create and initialize the JScrollPane
	 */
	private void init_scollpane() {
		jScrollPane = new JScrollPane();
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setPreferredSize(new Dimension(WIDTH, 240));
		//jScrollPane.setBorder(null);
		this.add(jScrollPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 0, 10));
		jScrollPane.setViewportView(panel);
		
		readVideos();
		dispVideos();
	}
	
	/*
	 * set the listener for JMenuBar, "load video" button and "parse video" button.
	 * CAN NOT put in the Constructor because there is no instance of this class, 
	 * will stuck in an infinite loop
	 */
	public void setListener() {
		this.setJMenuBar(new MyMenu(MainFrame.getSharedMainFrame()).getMenuBar());
		loadVideoB.addActionListener(new LoadVideoListener(MainFrame.getSharedMainFrame()));
		keyFrameB.addActionListener(new KeyFrameExtractListener(MainFrame.getSharedMainFrame()));
	}
	
	/*
     * Setting the Look and Feel to the System Look and Feel and to the Cross platform
     * Look and feel if the current system is not available
     */
    private static void setLookAndFeel() {
        boolean windowsLandF = true;
        JFrame.setDefaultLookAndFeelDecorated(true);
        /*Setting Look and Feel to the system default */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc) {
            windowsLandF = false;
            System.err.println("Error loading L&F: " + exc);
        }
        if (!windowsLandF)
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception exc) {
                System.err.println("Error loading L&F: " + exc);
                System.exit(0);
            }
    }
	
    /*
     *  singleton design, return only ONE instance of the MainFrame class
     */
	public static MainFrame getSharedMainFrame() {
		if(_mainFrame == null){
			_mainFrame = new MainFrame();
		}
		return _mainFrame;
	}
	
	/*
	public void setValue(String method, String colorModel) {
		_method = method;
		if(_method.compareToIgnoreCase("Global Pixel2Pixel Difference") == 0 || _method.compareToIgnoreCase("Cumulative Pixel2Pixel Difference") == 0)
			_colorModel = "YIQ";
		else
			_colorModel = colorModel;
	}
	
	public void setAttr(String folder, String size) {
		_folder = folder;
		_size = size;
	}
	*/
	
	/*
	 * set the method and color model, used in MyMenu.class
	 * set the value of folder and size
	 */
	public void setValue(int ind, String val) {
		if(ind == 0)
			_folder = val;
		if(ind == 1)
			_method = val;
		else if(ind == 2)
			_colorModel = val;
		else if(ind == 3)
			_size = val;
		else if(ind == 4) 
			_keyframeSize = val;
	}
	
	/*
	 * get the method, color model, threshold (for pixel2pixel), histogram bin, used in ShotDetect.class
	 */
	public String getValue(int i) {
		String str = null;
		if(i == 0)
			str = _folder;
		else if(i == 1)
			str = _method;
		else if(i == 2)
			str = _colorModel;
		else if(i == 3)
			str = _size;
		else if(i == 4)
			str = _keyframeSize;
		else if(i == 5)
			str = thresholdT.getText();
		else if(i == 6)
			str = binT.getText();
		
		return str;
	}
    
	/*
	 * update the UI after adding and parsing the video, used in VideoSegmentation.class
	 */
    public void updateUI(String new_videoName) {
    	VideoPanel vPanel = new VideoPanel(new_videoName, _method, _colorModel, _keyframeSize);
    	panel.add(vPanel);
    	panel.revalidate();
		panel.repaint();
		
		write2File(new_videoName);
    }
    
    /*
     * read the list of parsed videos
     */
	private void readVideos() {
		int ind_1, ind_2;
		String line;
		videoDir = new ArrayList<HashMap<String, String>>();
		HashMap map;
		try {
			readVideo = new BufferedReader(new FileReader("D:/Videos/videos.txt"));
			
			while ((line = readVideo.readLine()) != null) {
				map = new HashMap<String, String>();
				ind_1 = line.indexOf("/", 0);
				map.put("videoName", line.substring(0, ind_1));
				ind_2 = line.indexOf("/", ind_1 + 1);
				map.put("method", line.substring(ind_1 + 1, ind_2));
				map.put("colorModel", line.substring(ind_2 + 1));
				videoDir.add(map);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (readVideo != null)
					readVideo.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/*
	 * display the parsed videos and corresponding key frames
	 */
	private void dispVideos() {
		Iterator<HashMap<String, String>> iter = videoDir.iterator();
		HashMap<String, String> map = new HashMap<String, String>();
		while(iter.hasNext()) {
			map = iter.next();
			VideoPanel vPanel = new VideoPanel(map.get("videoName"), map.get("method"), map.get("colorModel"));
			panel.add(vPanel);
			panel.revalidate();
			panel.repaint();
		}
	}
	
	/*
	 * after parsing the video, add the title of the video to the file
	 */
	private void write2File(String new_videoName) {
		File file = new File("D:/Videos/videos.txt");
		BufferedWriter bw = null;
		if(file.exists()) {
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
				bw.write(new_videoName + "/" + _method + "/" + _colorModel);
				bw.newLine();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			FileWriter fw;
			try {
				fw = new FileWriter("D:/Videos/videos.txt");
				bw = new BufferedWriter(fw);
				bw.write(new_videoName + "/" + _method + "/" + _colorModel);
				bw.newLine();
				bw.flush();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
    
    public static void main(String [] args) {
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	_mainFrame = new MainFrame();
        		_mainFrame.setListener();
            }
        });
	}
}
