package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.swampsoft.carstereo.CarStereo;

public class BluetoothScreen {
	int screenWidth;
	int screenHeight;
	
	MainScreen mainScreen;
	
	JFrame frame;
	JPanel panel;
	JLabel labelTitle;
	JLabel labelDiscovery;
	JLabel labelVisible;
	JButton backButton;
	JButton discoveryButton;
	JButton unpairButton;
	
	JList list;
	
	BufferedImage backButtonImage;
	BufferedImage discoveryButtonImage;
	
	public BluetoothScreen(final MainScreen mainScreen){
		this.mainScreen = mainScreen;
		
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		//Font font = new Font("TimesRoman", Font.PLAIN, 30);
		Font font = new Font("RobotoCondensed", Font.PLAIN, 30);
		
		frame = new JFrame("Bluetooth Options");
		frame.setBackground(Color.BLACK);

		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		labelTitle = new JLabel("Bluetooth Options");
		labelTitle.setForeground(Color.WHITE);
		labelTitle.setFont(font);
		labelTitle.setSize(300, 30);
		labelTitle.setHorizontalAlignment(JLabel.CENTER);
		labelTitle.setBounds(screenWidth/2-labelTitle.getWidth()/2, screenHeight/10, labelTitle.getWidth(), labelTitle.getHeight());
		
		labelDiscovery = new JLabel("Discovery");
		labelDiscovery.setForeground(Color.WHITE);
		labelDiscovery.setFont(font);
		labelDiscovery.setSize(300, 30);
		labelDiscovery.setHorizontalAlignment(JLabel.CENTER);
		labelDiscovery.setBounds(screenWidth/5-labelDiscovery.getWidth()/2, screenHeight/3*2, labelDiscovery.getWidth(), labelDiscovery.getHeight());
		
		labelVisible = new JLabel("Visible Devices:");
		labelVisible.setForeground(Color.WHITE);
		labelVisible.setFont(font);
		labelVisible.setSize(300, 30);
		labelVisible.setHorizontalAlignment(JLabel.CENTER);
		labelVisible.setBounds(screenWidth/5*4-labelVisible.getWidth()/2, screenHeight/4, labelVisible.getWidth(), labelVisible.getHeight());
		
		list = new JList(mainScreen.blueToothClientNames.toArray());
		list.setForeground(Color.WHITE);
		list.setBackground(Color.BLACK);
		list.setFont(font);
		list.setSize(180, 150);
		list.setBounds(screenWidth/5*4-list.getWidth()/2, screenHeight/2-list.getHeight()/2, list.getWidth(), list.getHeight());
		
		setDiscoverableText();
		
		try {
			backButtonImage = ImageIO.read(getClass().getResource("/images/back-small.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backButton = new JButton(new ImageIcon(backButtonImage));
		backButton.setBackground(Color.BLACK);
		//backButton.setForeground(Color.WHITE);
		backButton.setBorder(BorderFactory.createEmptyBorder());
		backButton.setSize(100, 100);
		backButton.setBounds(screenWidth/2-backButton.getWidth()/2, screenHeight - 120, backButton.getWidth(), backButton.getHeight());

		try {
			discoveryButtonImage = ImageIO.read(getClass().getResource("/images/discovery.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		discoveryButton = new JButton(new ImageIcon(discoveryButtonImage));
		discoveryButton.setBackground(Color.BLACK);
		//backButton.setForeground(Color.WHITE);
		discoveryButton.setBorder(BorderFactory.createEmptyBorder());
		discoveryButton.setSize(128, 128);
		discoveryButton.setBounds(screenWidth/5-discoveryButton.getWidth()/2, screenHeight/3, discoveryButton.getWidth(), discoveryButton.getHeight());
		
		unpairButton = new JButton("Unpair Device");
		unpairButton.setFont(font);
		unpairButton.setBackground(Color.BLACK);
		unpairButton.setForeground(Color.WHITE);
		//pairedButton.setBorder(BorderFactory.createEmptyBorder());
		unpairButton.setSize(250, 35);
		unpairButton.setBounds(screenWidth/5*4-unpairButton.getWidth()/2, screenHeight/6*4, unpairButton.getWidth(), unpairButton.getHeight());
		
		panel.add(labelTitle);
		panel.add(labelDiscovery);
		panel.add(labelVisible);
		panel.add(backButton);
		panel.add(discoveryButton);
		panel.add(unpairButton);
		panel.add(list);
		
		frame.add(panel);
		frame.setSize(screenWidth, screenHeight);
		//frame.setLocation(0,0);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		backButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				/*
				try {
					mainScreen.blueToothWriter.write("discoverable off" + System.lineSeparator());
					mainScreen.blueToothWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				*/
				frame.dispose();
				CarStereo.device.setFullScreenWindow(mainScreen.frame);
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			
		});
		
		discoveryButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO make discoverable or not here
				if (mainScreen.blueToothIsDiscoverable){
					try {
						mainScreen.blueToothWriter.write("discoverable off" + System.lineSeparator());
						mainScreen.blueToothWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mainScreen.blueToothIsDiscoverable = false;
				} else {
					try {
						mainScreen.blueToothWriter.write("discoverable on" + System.lineSeparator());
						mainScreen.blueToothWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mainScreen.blueToothIsDiscoverable = true;
				}
				setDiscoverableText();
			}
			
		});
		
		unpairButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() >= 0){
					try {
						mainScreen.blueToothWriter.write("remove "+mainScreen.blueToothClientMacs.get(list.getSelectedIndex())+System.lineSeparator());
						mainScreen.blueToothWriter.flush();
						mainScreen.blueToothClientMacs.remove(list.getSelectedIndex());
						mainScreen.blueToothClientNames.remove(list.getSelectedIndex());
						mainScreen.blueToothClientConnStatus.remove(list.getSelectedIndex());
						list.setListData(mainScreen.blueToothClientNames.toArray());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		
		setDiscoverableText();
	}
	
	void setDiscoverableText(){
		if (mainScreen.blueToothIsDiscoverable){
			labelDiscovery.setText("Discoverable: True");
		} else {
			labelDiscovery.setText("Discoverable: False");
		}
	}
}
