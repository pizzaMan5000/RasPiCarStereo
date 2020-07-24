package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.swampsoft.carstereo.CarStereo;

public class FullScreenControls {
	
	int screenWidth;
	int screenHeight;
	
	boolean isFrameDisposable = false;
	
	JFrame frame;
	JPanel panel;
	
	JButton playPauseButton;
	BufferedImage playPauseIcon;
	JButton cancelButton;
	BufferedImage cancelIcon;
	JButton fastForwardButton;
	BufferedImage fastForwardIcon;
	JButton reverseButton;
	BufferedImage reverseIcon;
	
	JLabel labelTime;
	Thread timeThread;
	DateTimeFormatter timeFormatter;
	
	MP3Screen mp3Screen;
	MainScreen mainScreen;
	
	public FullScreenControls(MP3Screen mp3Screen, MainScreen mainScreen){
		this.mp3Screen = mp3Screen;
		this.mainScreen = mainScreen;
		
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
		Font font = new Font("RobotoCondensed", Font.PLAIN, 18);
		
		frame = new JFrame("Control");
		frame.setBackground(Color.BLACK);
		
		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		// play/pause button
		try {
			playPauseIcon = ImageIO.read(getClass().getResource("/images/play_pause_smallest.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		playPauseButton = new JButton(new ImageIcon(playPauseIcon));
		playPauseButton.setBorder(BorderFactory.createEmptyBorder());
		playPauseButton.setBackground(Color.BLACK); 
		playPauseButton.setSize(playPauseIcon.getWidth(), playPauseIcon.getHeight());
		playPauseButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		playPauseButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
		//playPauseButton.setBounds(0, screenHeight/3-playPauseButton.getHeight()/2, playPauseButton.getWidth(), playPauseButton.getHeight());
		playPauseButton.setBounds(screenWidth-50, screenHeight/4, 50, screenHeight/4);
		playPauseButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				playPauseVideo();
			}
			
		});
		panel.add(playPauseButton);
		
		// cancel button
		try {
			cancelIcon = ImageIO.read(getClass().getResource("/images/cancel-smallest.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cancelButton = new JButton(new ImageIcon(cancelIcon));
		cancelButton.setBorder(BorderFactory.createEmptyBorder());
		cancelButton.setBackground(Color.BLACK);
		cancelButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cancelButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
		cancelButton.setSize(cancelIcon.getWidth(), cancelIcon.getHeight());
		cancelButton.setBounds(screenWidth-50, screenHeight/2, 50, screenHeight/4);
		cancelButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				closeScreen();
				stopVideo();
			}
			
		});
		panel.add(cancelButton);
		
		// fastForward button
		try {
			fastForwardIcon = ImageIO.read(getClass().getResource("/images/forward-smallest.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fastForwardButton = new JButton(new ImageIcon(fastForwardIcon));
		fastForwardButton.setBorder(BorderFactory.createEmptyBorder());
		fastForwardButton.setBackground(Color.BLACK);
		fastForwardButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		fastForwardButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
		fastForwardButton.setSize(cancelIcon.getWidth(), cancelIcon.getHeight());
		fastForwardButton.setBounds(screenWidth-50, 0, 50, screenHeight/4);
		fastForwardButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				fastForward();
			}
			
		});
		panel.add(fastForwardButton);
		
		// reverse button
		try {
			reverseIcon = ImageIO.read(getClass().getResource("/images/reverse-smallest.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reverseButton = new JButton(new ImageIcon(reverseIcon));
		reverseButton.setBorder(BorderFactory.createEmptyBorder());
		reverseButton.setBackground(Color.BLACK);
		reverseButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		reverseButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
		reverseButton.setSize(cancelIcon.getWidth(), cancelIcon.getHeight());
		reverseButton.setBounds(screenWidth-50, screenHeight/4*3, 50, screenHeight/4);
		reverseButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				reverse();
			}
			
		});
		panel.add(reverseButton);
		
		// time text on bottom
		labelTime = new JLabel("");
		labelTime.setSize(200,20);
		labelTime.setForeground(Color.white);
		labelTime.setFont(font);
		labelTime.setHorizontalAlignment(JLabel.CENTER);
		labelTime.setBounds((screenWidth-50)/2-labelTime.getWidth()/2, screenHeight-19, labelTime.getWidth(), labelTime.getHeight());
		panel.add(labelTime);
		
		// WINDOW (frame)
		frame.add(panel);
		frame.setSize(screenWidth, screenHeight);
		//frame.setLocation(screenWidth-50,0);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.addWindowFocusListener(new WindowFocusListener(){

			public void windowGainedFocus(WindowEvent arg0) {
				// do nothing
			}

			public void windowLostFocus(WindowEvent arg0) {
				// if frame is not on top, bring it back up
				if (!isFrameDisposable) {
					frame.setVisible(true);
				}
			}
			
		});
		frame.setVisible(true); // do this last
		startTimeThread(); // and this last too
	}
	
	void playPauseVideo(){
		try {
			CarStereo.writer.write("p");
			CarStereo.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//CarStereo.writer.close();
		
		if (CarStereo.mp3IsPlaying){
			// if mp3 is already playing
			CarStereo.mp3IsPlaying = false;
		} else {
			// not already playing
			CarStereo.mp3IsPlaying = true;
		}
	}

	void stopVideo(){
		CarStereo.mp3IsPlaying = false; // stops the playlist loop
		
		try {
			CarStereo.writer.write("q");
			CarStereo.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!CarStereo.mediaPlaylist.isEmpty()){
			mainScreen.labelInfo1.setText(CarStereo.mediaPlaylist.get(0).getName().substring(0, CarStereo.mediaPlaylist.get(0).getName().lastIndexOf(".")));
			mainScreen.labelInfo2.setText("= PAUSED =");
		}
	}
	
	void fastForward(){
		try {
			CarStereo.writer.write("\u001b[C"); // UP = "\u001b[A"  -- RIGHT = "\u001b[C"
			CarStereo.writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void reverse(){
		try {
			CarStereo.writer.write("\u001b[D"); // UP = "\u001b[B"  -- RIGHT = "\u001b[D"
			CarStereo.writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void startTimeThread() {
		timeThread = new Thread(){
			public void run(){
				while (true) {
					labelTime.setText(timeFormatter.format(LocalDateTime.now()));
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		};
		timeThread.start();
	}
	
	void closeScreen(){
		isFrameDisposable = true;
		frame.setVisible(false);
		frame.dispose();
	}
}
