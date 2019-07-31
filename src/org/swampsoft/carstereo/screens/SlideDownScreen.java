package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.swampsoft.carstereo.CarStereo;

public class SlideDownScreen {
	
	int screenWidth;
	int screenHeight;
	
	//JFrame frame;
	JDialog dialog;
	JPanel panel;
	
	JButton playPauseButton;
	BufferedImage playPauseIcon;
	
	MainScreen mainScreen;
	
	public SlideDownScreen(MainScreen mainScreen){
		this.mainScreen = mainScreen;
		this.screenWidth = mainScreen.screenWidth;
		this.screenHeight = mainScreen.screenHeight;
		
		//frame = new JFrame("Control");
		//frame.setBackground(Color.BLACK);
		dialog = new JDialog();
		dialog.setBackground(Color.BLACK);
		
		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		try {
			playPauseIcon = ImageIO.read(getClass().getResource("/images/play_pause_small.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		playPauseButton = new JButton(new ImageIcon(playPauseIcon));
		playPauseButton.setBorder(BorderFactory.createEmptyBorder());
		playPauseButton.setBackground(Color.BLACK);
		playPauseButton.setSize(playPauseIcon.getWidth(), playPauseIcon.getHeight());
		playPauseButton.setBounds(0, 0, playPauseButton.getWidth(), playPauseButton.getHeight());
		
		panel.add(playPauseButton);
		
		/*
		frame.add(panel);
		frame.setSize(80, 80);
		frame.setLocation(screenWidth/2-40,0);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		*/
		dialog.add(panel);
		dialog.setSize(80, 80);
		dialog.setLocation(screenWidth/2-40,0);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setUndecorated(true);
		dialog.setVisible(true);
		dialog.setModal (true);
		dialog.setAlwaysOnTop(true);
		dialog.setModalityType (ModalityType.APPLICATION_MODAL);
		
		playPauseButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (CarStereo.playMode == 1){
					startStopRadio();
				} else if (CarStereo.playMode == 2){
					playPauseMP3();
				}
			}
			
		});
	}
	
	void playPauseMP3(){
		CarStereo.writer.write("p");
		CarStereo.writer.flush();
		//CarStereo.writer.close();
		
		if (CarStereo.mp3IsPlaying){
			// if mp3 is already playing
			CarStereo.mp3IsPlaying = false;
			mainScreen.labelInfo2.setText("= PAUSED =");
		} else {
			// not already playing
			CarStereo.mp3IsPlaying = true;
			mainScreen.labelInfo1.setText("Song Name Here");
			mainScreen.labelInfo2.setText("Artist Name Here");
		}
	}

	void startStopRadio(){
		if (CarStereo.radioIsPlaying){
			// if radio is already playing
			CarStereo.radioIsPlaying = false;
			mainScreen.labelInfo2.setText("(Radio Stopped)");
			CarStereo.stopRadio();
		} else {
			// if its not playing already
			CarStereo.radioIsPlaying = true;
			String[] command = {"/bin/sh", "-c", "rtl_fm -f 104.1M -M fm -s 200000 -r 48000 | aplay -r 48000 -f S16_LE"};
			try {
				CarStereo.radioProcess = Runtime.getRuntime().exec(command);
				CarStereo.playMode = 1;
				// set info texts
				mainScreen.labelInfo1.setText("FM 104.1 MHz");
				mainScreen.labelInfo2.setText("Real Radio");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
