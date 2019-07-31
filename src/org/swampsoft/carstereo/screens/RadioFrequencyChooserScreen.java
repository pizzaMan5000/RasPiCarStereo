package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.swampsoft.carstereo.CarStereo;

public class RadioFrequencyChooserScreen {
	int screenWidth;
	int screenHeight;
	
	int frequency;
	String stationNumber;
	int buttonNumber;

	JFrame frame;
	JPanel panel;
	JLabel labelTitle;
	JLabel labelFrequency;
	
	JButton backButton;
	JButton fastLeftButton;
	JButton leftButton;
	JButton selectFrequencyButton;
	JButton rightButton;
	JButton fastRightButton;
	
	BufferedImage backButtonIcon;
	BufferedImage fastLeftIcon;
	BufferedImage leftIcon;
	BufferedImage selectFrequencyIcon;
	BufferedImage rightIcon;
	BufferedImage fastRightIcon;
	
	public RadioFrequencyChooserScreen(final MainScreen mainScreen, int button){
		this.buttonNumber = button;
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		frequency = 980;
		if (buttonNumber == 1 && CarStereo.fm1 != null && CarStereo.fm1 != "") frequency = (int)(Double.parseDouble(CarStereo.fm1) * 10);
		if (buttonNumber == 2 && CarStereo.fm2 != null && CarStereo.fm2 != "") frequency = (int)(Double.parseDouble(CarStereo.fm2) * 10);
		if (buttonNumber == 3 && CarStereo.fm3 != null && CarStereo.fm3 != "") frequency = (int)(Double.parseDouble(CarStereo.fm3) * 10);
		if (buttonNumber == 4 && CarStereo.fm4 != null && CarStereo.fm4 != "") frequency = (int)(Double.parseDouble(CarStereo.fm4) * 10);
		if (buttonNumber == 5 && CarStereo.fm5 != null && CarStereo.fm5 != "") frequency = (int)(Double.parseDouble(CarStereo.fm5) * 10);
		if (buttonNumber == 6 && CarStereo.fm6 != null && CarStereo.fm6 != "") frequency = (int)(Double.parseDouble(CarStereo.fm6) * 10);
		if (buttonNumber == 7 && CarStereo.fm7 != null && CarStereo.fm7 != "") frequency = (int)(Double.parseDouble(CarStereo.fm7) * 10);
		if (buttonNumber == 8 && CarStereo.fm8 != null && CarStereo.fm8 != "") frequency = (int)(Double.parseDouble(CarStereo.fm8) * 10);
		
		
		Font font = new Font("TimesRoman", Font.PLAIN, 30);
		Font font2 = new Font("TimesRoman", Font.PLAIN, 60);
		
		frame = new JFrame("FM Radio Chooser");
		frame.setBackground(Color.BLACK);
		
		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		labelTitle = new JLabel("Select an FM Frequency");
		labelTitle.setForeground(Color.WHITE);
		labelTitle.setFont(font);
		labelTitle.setSize(350, 30);
		labelTitle.setHorizontalAlignment(JLabel.CENTER);
		labelTitle.setBounds(screenWidth/2-labelTitle.getWidth()/2, screenHeight/15, labelTitle.getWidth(), labelTitle.getHeight());
		
		labelFrequency = new JLabel("FM "+frequency/10+"."+frequency%10+" MHz");
		labelFrequency.setForeground(Color.WHITE);
		labelFrequency.setFont(font2);
		labelFrequency.setSize(400, 50);
		labelFrequency.setHorizontalAlignment(JLabel.CENTER);
		labelFrequency.setBounds(screenWidth/2-labelFrequency.getWidth()/2, screenHeight/15*3, labelFrequency.getWidth(), labelFrequency.getHeight());
		
		
		try {
			backButtonIcon = ImageIO.read(getClass().getResource("/images/back-small.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backButton = new JButton(new ImageIcon(backButtonIcon));
		backButton.setBackground(Color.BLACK);
		backButton.setBorder(BorderFactory.createEmptyBorder());
		backButton.setSize(100, 100);
		backButton.setBounds(screenWidth/2-backButton.getWidth()/2, screenHeight - 120, backButton.getWidth(), backButton.getHeight());

		try {
			fastLeftIcon = ImageIO.read(getClass().getResource("/images/reverse.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fastLeftButton = new JButton(new ImageIcon(fastLeftIcon));
		fastLeftButton.setBackground(Color.BLACK);
		fastLeftButton.setBorder(BorderFactory.createEmptyBorder());
		fastLeftButton.setSize(128, 128);
		fastLeftButton.setBounds(screenWidth/6-fastLeftButton.getWidth()/2, screenHeight/5*2, fastLeftButton.getWidth(), fastLeftButton.getHeight());

		try {
			leftIcon = ImageIO.read(getClass().getResource("/images/play-reversed.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		leftButton = new JButton(new ImageIcon(leftIcon));
		leftButton.setBackground(Color.BLACK);
		leftButton.setBorder(BorderFactory.createEmptyBorder());
		leftButton.setSize(128, 128);
		leftButton.setBounds(screenWidth/6*2-leftButton.getWidth()/2, screenHeight/5*2, leftButton.getWidth(), leftButton.getHeight());

		try {
			selectFrequencyIcon = ImageIO.read(getClass().getResource("/images/antenna.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selectFrequencyButton = new JButton(new ImageIcon(selectFrequencyIcon));
		selectFrequencyButton.setBackground(Color.BLACK);
		selectFrequencyButton.setBorder(BorderFactory.createEmptyBorder());
		selectFrequencyButton.setSize(128, 128);
		selectFrequencyButton.setBounds(screenWidth/6*3-selectFrequencyButton.getWidth()/2, screenHeight/5*2, selectFrequencyButton.getWidth(), selectFrequencyButton.getHeight());

		try {
			rightIcon = ImageIO.read(getClass().getResource("/images/play.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rightButton = new JButton(new ImageIcon(rightIcon));
		rightButton.setBackground(Color.BLACK);
		rightButton.setBorder(BorderFactory.createEmptyBorder());
		rightButton.setSize(128, 128);
		rightButton.setBounds(screenWidth/6*4-rightButton.getWidth()/2, screenHeight/5*2, rightButton.getWidth(), rightButton.getHeight());

		try {
			fastRightIcon = ImageIO.read(getClass().getResource("/images/forward.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fastRightButton = new JButton(new ImageIcon(fastRightIcon));
		fastRightButton.setBackground(Color.BLACK);
		fastRightButton.setBorder(BorderFactory.createEmptyBorder());
		fastRightButton.setSize(128, 128);
		fastRightButton.setBounds(screenWidth/6*5-fastRightButton.getWidth()/2, screenHeight/5*2, fastRightButton.getWidth(), fastRightButton.getHeight());
		
		panel.add(labelTitle);
		panel.add(labelFrequency);
		panel.add(backButton);
		panel.add(fastLeftButton);
		panel.add(leftButton);
		panel.add(selectFrequencyButton);
		panel.add(rightButton);
		panel.add(fastRightButton);
		
		frame.add(panel);
		frame.setSize(screenWidth, screenHeight);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		backButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				//System.exit(0);
				frame.dispose();
				mainScreen.radioScreen.loadStations();
				CarStereo.device.setFullScreenWindow(mainScreen.radioScreen.frame);
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			
		});
		
		fastLeftButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				frequency = frequency - 10;
				if (frequency < 880) frequency = 880;
				labelFrequency.setText("FM "+frequency/10+"."+frequency%10+" MHz");
			}
			
		});
		
		leftButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				frequency = frequency - 1;
				if (frequency < 880) frequency = 880;
				labelFrequency.setText("FM "+frequency/10+"."+frequency%10+" MHz");
			}
			
		});
		
		selectFrequencyButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (buttonNumber == 1) CarStereo.fm1 = frequency/10 + "." + frequency%10;
				if (buttonNumber == 2) CarStereo.fm2 = frequency/10 + "." + frequency%10;
				if (buttonNumber == 3) CarStereo.fm3 = frequency/10 + "." + frequency%10;
				if (buttonNumber == 4) CarStereo.fm4 = frequency/10 + "." + frequency%10;
				if (buttonNumber == 5) CarStereo.fm5 = frequency/10 + "." + frequency%10;
				if (buttonNumber == 6) CarStereo.fm6 = frequency/10 + "." + frequency%10;
				if (buttonNumber == 7) CarStereo.fm7 = frequency/10 + "." + frequency%10;
				if (buttonNumber == 8) CarStereo.fm8 = frequency/10 + "." + frequency%10;

				CarStereo.saveProperties();
				frame.dispose();
				mainScreen.radioScreen.loadStations();
				CarStereo.device.setFullScreenWindow(mainScreen.radioScreen.frame);
			}
			
		});
		
		rightButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				frequency = frequency + 1;
				if (frequency > 1080) frequency = 1080;
				labelFrequency.setText("FM "+frequency/10+"."+frequency%10+" MHz");
			}
			
		});
		
		fastRightButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				frequency = frequency + 10;
				if (frequency > 1080) frequency = 1080;
				labelFrequency.setText("FM "+frequency/10+"."+frequency%10+" MHz");
			}
			
		});
	}
	
}
