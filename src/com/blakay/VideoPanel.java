package com.blakay;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VideoPanel extends JPanel{
	private static final int WIDTH = 740, HEIGHT = 300, JUMPLENGTH = 1000;
	private JPanel dispPanel, videoPlayerPanel, videoPanel, videoControlPanel, imagePanel, imageControlPanel;
	private JButton startB, playB, forwardB, backwardB, nextB, prevB;
	private JLabel titleLabel, imageLabel;
	private int ind, _keyframeSize;
	private String _folder, _method, _colorModel, path;
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private EmbeddedMediaPlayer embeddedMediaPlayer;
	
	public VideoPanel(String folder, String method, String colorModel) {
		ind = 1;
		_folder = folder;
		_method = method;
		_colorModel = colorModel;
		File frame_file = new File("D:/Videos/key frames/" + _folder + "/" + _method + "/" + _colorModel);
		final File [] frames = frame_file.listFiles();
		_keyframeSize = frames.length - 1;
		
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
		this.setLayout(new BorderLayout(10,10));
		
		titleLabel = new JLabel("Method: " + method + ", Color Model: " + colorModel, SwingConstants.CENTER);
		dispPanel = new JPanel();
		this.add(titleLabel, BorderLayout.NORTH);
		this.add(dispPanel, BorderLayout.CENTER);
		
		/*
		 * key frame display panel
		 */
		imagePanel = new JPanel();
		imagePanel.setLayout(new BorderLayout());
		
		// display key frames
		path = "D:/Videos/key frames/" + _folder + "/" + _method + "/" + _colorModel + "/frame_1.jpg";
		
		imageLabel = new JLabel();
		imageLabel.setIcon(new ImageIcon(path));
		imagePanel.add(imageLabel, BorderLayout.CENTER);
		
		// image control button panel
		imageControlPanel = new JPanel();
		imageControlPanel.setLayout(new GridLayout(1, 4));
		imagePanel.add(imageControlPanel, BorderLayout.SOUTH);
		
		// previous button
		prevB = new JButton("Prev");
		prevB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(ind > 1) {
					ind--;
					path = "D:/Videos/key frames/" + _folder + "/" + _method + "/" + _colorModel + "/frame_" + ind + ".jpg";
					imageLabel.setIcon(new ImageIcon(path));
					revalidate();
		            repaint();
				}
			}
			
		});
		imageControlPanel.add(new JLabel(""));
		imageControlPanel.add(prevB);
		
		// next buttons
		nextB = new JButton("Next");
		nextB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(ind < _keyframeSize) {
					ind++;
					path = "D:/Videos/key frames/" + _folder + "/" + _method + "/" + _colorModel + "/frame_" + ind + ".jpg";
					imageLabel.setIcon(new ImageIcon(path));
					revalidate();
		            repaint();
				}
			}
			
		});
		imageControlPanel.add(nextB);
		imageControlPanel.add(new JLabel(""));
		
		/*
		 * video player panel
		 */
		// video player panel
		videoPlayerPanel = new JPanel();
		videoPlayerPanel.setLayout(new BorderLayout());
		
		dispPanel.setLayout(new BorderLayout(10,10));
		dispPanel.add(videoPlayerPanel, BorderLayout.WEST);
		dispPanel.add(imagePanel, BorderLayout.CENTER);
		
		// video control buttons panel
		videoControlPanel = new JPanel();
		videoControlPanel.setLayout(new GridLayout(1,4));
		videoPlayerPanel.add(videoControlPanel, BorderLayout.SOUTH);
		
		// video start button
		startB = new JButton("Start");
		startB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						embeddedMediaPlayer = mediaPlayerComponent.getMediaPlayer();
						embeddedMediaPlayer.playMedia("D:\\Videos\\videos\\" + _folder + ".avi");
					}
				});
			}
			
		});
		videoControlPanel.add(startB);
		
		// video play/pause button
		playB = new JButton("Play/Pause");
		playB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(embeddedMediaPlayer.isPlaying())
					embeddedMediaPlayer.setPause(true);
				else
					embeddedMediaPlayer.play();
			}
			
		});
		videoControlPanel.add(playB);
		
		// video backward button
		backwardB = new JButton("Backward");
		backwardB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				embeddedMediaPlayer.setTime((long) (embeddedMediaPlayer.getTime() - JUMPLENGTH));
			}
			
		});
		videoControlPanel.add(backwardB);
		
		// video forward button
		forwardB = new JButton("Forward");
		forwardB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				embeddedMediaPlayer.setTime((long) (embeddedMediaPlayer.getTime() + JUMPLENGTH));
			}
			
		});
		videoControlPanel.add(forwardB);
	
		// video player component panel, LibVLC(VLCJ) API
		videoPanel = new JPanel();
		videoPanel.setLayout(new BorderLayout());
		videoPlayerPanel.add(videoPanel, BorderLayout.CENTER);
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), System.getProperty("user.dir") + "/lib");//"D:/Program Files/VideoLAN/VLC"
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		videoPanel.add(mediaPlayerComponent, BorderLayout.CENTER);
	}
	
	public VideoPanel(String folder, String method, String colorModel, String keyframeSize) {
		this(folder, method, colorModel);
		_keyframeSize = Integer.parseInt(keyframeSize);
	}
}
