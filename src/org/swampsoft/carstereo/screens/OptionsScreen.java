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
import javax.swing.JPanel;

import org.swampsoft.carstereo.CarStereo;

public class OptionsScreen {
	int screenWidth;
	int screenHeight;
	
	MainScreen mainScreen;
	
	JFrame frame;
	JPanel panel;
	//JLabel labelTitle;
	JLabel labelReboot;
	JLabel labelShutdown;
	JButton backButton;
	JButton rebootButton;
	JButton shutdownButton;
	JButton fileManagerButton;
	
	BufferedImage backButtonImage;
	BufferedImage rebootButtonImage;
	BufferedImage shutdownButtonImage;
	BufferedImage fileManagerIcon;
	
	Process fileManagerProcess;
	
	public OptionsScreen(final MainScreen mainScreen){
		this.mainScreen = mainScreen;
		
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		Font font = new Font("TimesRoman", Font.PLAIN, 30);
		
		frame = new JFrame("Options");
		frame.setBackground(Color.BLACK);

		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		/*
		labelTitle = new JLabel("Options");
		labelTitle.setForeground(Color.WHITE);
		labelTitle.setFont(font);
		labelTitle.setSize(180, 30);
		labelTitle.setHorizontalAlignment(JLabel.CENTER);
		labelTitle.setBounds(screenWidth/2-labelTitle.getWidth()/2, screenHeight/10, labelTitle.getWidth(), labelTitle.getHeight());
		*/
		
		labelReboot = new JLabel("Reboot");
		labelReboot.setForeground(Color.WHITE);
		labelReboot.setFont(font);
		labelReboot.setSize(180, 30);
		labelReboot.setHorizontalAlignment(JLabel.CENTER);
		labelReboot.setBounds(screenWidth/5-labelReboot.getWidth()/2, screenHeight/5*3, labelReboot.getWidth(), labelReboot.getHeight());

		labelShutdown = new JLabel("Shutdown");
		labelShutdown.setForeground(Color.WHITE);
		labelShutdown.setFont(font);
		labelShutdown.setSize(180, 30);
		labelShutdown.setHorizontalAlignment(JLabel.CENTER);
		labelShutdown.setBounds(screenWidth/5*4-labelShutdown.getWidth()/2, screenHeight/5*3, labelShutdown.getWidth(), labelShutdown.getHeight());
		
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
			rebootButtonImage = ImageIO.read(getClass().getResource("/images/reboot.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rebootButton = new JButton(new ImageIcon(rebootButtonImage));
		rebootButton.setBackground(Color.BLACK);
		//backButton.setForeground(Color.WHITE);
		rebootButton.setBorder(BorderFactory.createEmptyBorder());
		rebootButton.setSize(128, 128);
		rebootButton.setBounds(screenWidth/5-rebootButton.getWidth()/2, screenHeight/4, rebootButton.getWidth(), rebootButton.getHeight());

		try {
			shutdownButtonImage = ImageIO.read(getClass().getResource("/images/power.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shutdownButton = new JButton(new ImageIcon(shutdownButtonImage));
		shutdownButton.setBackground(Color.BLACK);
		//backButton.setForeground(Color.WHITE);
		shutdownButton.setBorder(BorderFactory.createEmptyBorder());
		shutdownButton.setSize(128, 128);
		shutdownButton.setBounds(screenWidth/5*4-shutdownButton.getWidth()/2, screenHeight/4, shutdownButton.getWidth(), shutdownButton.getHeight());
		

		try {
			fileManagerIcon = ImageIO.read(getClass().getResource("/images/folder.png"));
			//navigationIcon = ImageIO.read(new File("images/navigation.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fileManagerButton = new JButton(new ImageIcon(fileManagerIcon));
		fileManagerButton.setBackground(Color.black);
		fileManagerButton.setBorder(BorderFactory.createEmptyBorder());
		//fileManagerButton.setText("File Manager");
		fileManagerButton.setSize(128,128);
		fileManagerButton.setBounds((int)(screenWidth/5*2.5)-fileManagerButton.getWidth()/2, screenHeight/4, fileManagerButton.getWidth(), fileManagerButton.getHeight());
		
		
		//panel.add(labelTitle);
		panel.add(labelReboot);
		panel.add(labelShutdown);
		panel.add(backButton);
		panel.add(rebootButton);
		panel.add(shutdownButton);
		panel.add(fileManagerButton);
		
		frame.add(panel);
		frame.setSize(screenWidth, screenHeight);
		//frame.setLocation(0,0);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		backButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				//System.exit(0);
				frame.dispose();
				CarStereo.device.setFullScreenWindow(mainScreen.frame);
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			
		});
		
		rebootButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// reboot
				frame.dispose();
				CarStereo.killAllProcesses();
				try {
					Process rebootProcess = Runtime.getRuntime().exec("sudo reboot");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			
		});
		
		shutdownButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// shutdown
				frame.dispose();
				CarStereo.killAllProcesses();
				try {
					Process shutdownProcess = Runtime.getRuntime().exec("sudo shutdown -h now");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			
		});
		
		fileManagerButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// filemanager button pressed
				try {
					fileManagerProcess = Runtime.getRuntime().exec("pcmanfm --no-desktop ~/Media");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}
}
