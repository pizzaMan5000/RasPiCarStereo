package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.swampsoft.carstereo.CarStereo;

public class RadioScreen {
	
	int screenWidth;
	int screenHeight;
	
	MainScreen mainScreen;
	
	JFrame frame;
	JPanel panel;
	JLabel label;
	JButton backButton;
	JButton fm1Button;
	JButton fm2Button;
	JButton fm3Button;
	JButton fm4Button;
	JButton fm5Button;
	JButton fm6Button;
	JButton fm7Button;
	JButton fm8Button;
	
	long longPressTimer;
	long delay = 750;
	
	BufferedImage backButtonIcon;
	BufferedImage radioIcon;
	
	//Process radioProcess;
	
	public RadioScreen(final MainScreen mainScreen){
		this.mainScreen = mainScreen;
		
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		Font font = new Font("TimesRoman", Font.PLAIN, 30);
		
		frame = new JFrame("FM Radio");
		frame.setBackground(Color.BLACK);

		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		label = new JLabel("FM Radio");
		label.setForeground(Color.WHITE);
		label.setFont(font);
		label.setSize(180, 20);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(screenWidth/2-label.getWidth()/2, screenHeight/15, label.getWidth(), label.getHeight());
		
		try {
			backButtonIcon = ImageIO.read(getClass().getResource("/images/back-small.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backButton = new JButton(new ImageIcon(backButtonIcon));
		backButton.setBackground(Color.BLACK);
		//backButton.setForeground(Color.WHITE);
		backButton.setBorder(BorderFactory.createEmptyBorder());
		//backButton.setFont(font);
		//backButton.setText("BACK");
		//backButton.setToolTipText("Back");
		backButton.setSize(100, 100);
		backButton.setBounds(screenWidth/2-backButton.getWidth()/2, screenHeight - 120, backButton.getWidth(), backButton.getHeight());
		
		try {
			radioIcon = ImageIO.read(getClass().getResource("/images/radio-small.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fm1Button = new JButton(new ImageIcon(radioIcon));
		fm1Button.setFont(font);
		fm1Button.setText(CarStereo.fm1);
		fm1Button.setBackground(Color.BLACK);
		fm1Button.setForeground(Color.WHITE);
		fm1Button.setBorder(BorderFactory.createEmptyBorder());
		if (CarStereo.fm1 == "" || CarStereo.fm1 == null){
			fm1Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm1Button.setSize(radioIcon.getWidth(), 135);
		}
		fm1Button.setHorizontalTextPosition(JButton.CENTER);
		fm1Button.setVerticalTextPosition(JButton.BOTTOM);
		fm1Button.setBounds(screenWidth/5-fm1Button.getWidth()/2, screenHeight/8, fm1Button.getWidth(), fm1Button.getHeight());
		
		fm2Button = new JButton(new ImageIcon(radioIcon));
		fm2Button.setFont(font);
		fm2Button.setText(CarStereo.fm2);
		fm2Button.setBackground(Color.BLACK);
		fm2Button.setForeground(Color.WHITE);
		fm2Button.setBorder(BorderFactory.createEmptyBorder());
		if (CarStereo.fm2 == "" || CarStereo.fm2 == null){
			fm2Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm2Button.setSize(radioIcon.getWidth(), 135);
		}
		fm2Button.setHorizontalTextPosition(JButton.CENTER);
		fm2Button.setVerticalTextPosition(JButton.BOTTOM);
		fm2Button.setBounds(screenWidth/5*2-fm2Button.getWidth()/2, screenHeight/8, fm2Button.getWidth(), fm2Button.getHeight());
		
		fm3Button = new JButton(new ImageIcon(radioIcon));
		fm3Button.setFont(font);
		fm3Button.setText(CarStereo.fm3);
		fm3Button.setBackground(Color.BLACK);
		fm3Button.setForeground(Color.WHITE);
		fm3Button.setBorder(BorderFactory.createEmptyBorder());
		if (CarStereo.fm3 == "" || CarStereo.fm3 == null){
			fm3Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm3Button.setSize(radioIcon.getWidth(), 135);
		}
		fm3Button.setHorizontalTextPosition(JButton.CENTER);
		fm3Button.setVerticalTextPosition(JButton.BOTTOM);
		fm3Button.setBounds(screenWidth/5*3-fm3Button.getWidth()/2, screenHeight/8, fm3Button.getWidth(), fm3Button.getHeight());
		
		fm4Button = new JButton(new ImageIcon(radioIcon));
		fm4Button.setFont(font);
		fm4Button.setText(CarStereo.fm4);
		fm4Button.setBackground(Color.BLACK);
		fm4Button.setForeground(Color.WHITE);
		fm4Button.setBorder(BorderFactory.createEmptyBorder());
		if (CarStereo.fm4 == "" || CarStereo.fm4 == null){
			fm4Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm4Button.setSize(radioIcon.getWidth(), 135);
		}
		fm4Button.setHorizontalTextPosition(JButton.CENTER);
		fm4Button.setVerticalTextPosition(JButton.BOTTOM);
		fm4Button.setBounds(screenWidth/5*4-fm4Button.getWidth()/2, screenHeight/8, fm4Button.getWidth(), fm4Button.getHeight());
		
		fm5Button = new JButton(new ImageIcon(radioIcon));
		fm5Button.setFont(font);
		fm5Button.setText(CarStereo.fm5);
		fm5Button.setBackground(Color.BLACK);
		fm5Button.setForeground(Color.WHITE);
		fm5Button.setBorder(BorderFactory.createEmptyBorder());
		if (CarStereo.fm5 == "" || CarStereo.fm5 == null){
			fm5Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm5Button.setSize(radioIcon.getWidth(), 135);
		}
		fm5Button.setHorizontalTextPosition(JButton.CENTER);
		fm5Button.setVerticalTextPosition(JButton.BOTTOM);
		fm5Button.setBounds(screenWidth/5-fm5Button.getWidth()/2, screenHeight/8*4, fm5Button.getWidth(), fm5Button.getHeight());
		
		fm6Button = new JButton(new ImageIcon(radioIcon));
		fm6Button.setFont(font);
		fm6Button.setText(CarStereo.fm6);
		fm6Button.setBackground(Color.BLACK);
		fm6Button.setForeground(Color.WHITE);
		fm6Button.setBorder(BorderFactory.createEmptyBorder());
		if (CarStereo.fm6 == "" || CarStereo.fm6 == null){
			fm6Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm6Button.setSize(radioIcon.getWidth(), 135);
		}
		fm6Button.setHorizontalTextPosition(JButton.CENTER);
		fm6Button.setVerticalTextPosition(JButton.BOTTOM);
		fm6Button.setBounds(screenWidth/5*2-fm6Button.getWidth()/2, screenHeight/8*4, fm6Button.getWidth(), fm6Button.getHeight());
		
		fm7Button = new JButton(new ImageIcon(radioIcon));
		fm7Button.setFont(font);
		fm7Button.setText(CarStereo.fm7);
		fm7Button.setBackground(Color.BLACK);
		fm7Button.setForeground(Color.WHITE);
		fm7Button.setBorder(BorderFactory.createEmptyBorder());
		if (CarStereo.fm7 == "" || CarStereo.fm7 == null){
			fm7Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm7Button.setSize(radioIcon.getWidth(), 135);
		}
		fm7Button.setHorizontalTextPosition(JButton.CENTER);
		fm7Button.setVerticalTextPosition(JButton.BOTTOM);
		fm7Button.setBounds(screenWidth/5*3-fm7Button.getWidth()/2, screenHeight/8*4, fm7Button.getWidth(), fm7Button.getHeight());
		
		fm8Button = new JButton(new ImageIcon(radioIcon));
		fm8Button.setFont(font);
		fm8Button.setText(CarStereo.fm8);
		fm8Button.setBackground(Color.BLACK);
		fm8Button.setForeground(Color.WHITE);
		fm8Button.setBorder(BorderFactory.createEmptyBorder());
		if (CarStereo.fm8 == "" || CarStereo.fm8 == null){
			fm8Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm8Button.setSize(radioIcon.getWidth(), 135);
		}
		fm8Button.setHorizontalTextPosition(JButton.CENTER);
		fm8Button.setVerticalTextPosition(JButton.BOTTOM);
		fm8Button.setBounds(screenWidth/5*4-fm8Button.getWidth()/2, screenHeight/8*4, fm8Button.getWidth(), fm8Button.getHeight());
		
		
		panel.add(label);
		panel.add(backButton);
		panel.add(fm1Button);
		panel.add(fm2Button);
		panel.add(fm3Button);
		panel.add(fm4Button);
		panel.add(fm5Button);
		panel.add(fm6Button);
		panel.add(fm7Button);
		panel.add(fm8Button);
		
		frame.add(panel);
		frame.setSize(screenWidth, screenHeight);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		backButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				//System.exit(0);
				//frame.dispose();
				//CarStereo.device.setFullScreenWindow(mainScreen.frame);
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				closeThisWindow();
			}
			
		});
		
		fm1Button.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				longPressTimer = System.currentTimeMillis() + delay;
			}

			public void mouseReleased(MouseEvent arg0) {
				
				if (longPressTimer < System.currentTimeMillis()){
					// set station ****
					new RadioFrequencyChooserScreen(mainScreen, 1);
				} else {
					if (CarStereo.fm1 != "" && CarStereo.fm1 != null){
						CarStereo.killAllProcesses();
						closeThisWindow();
						startRadio(CarStereo.fm1);
					} else {
						// set station ****
						new RadioFrequencyChooserScreen(mainScreen, 1);
					}
				}
			}
			
		});
		
		fm2Button.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				longPressTimer = System.currentTimeMillis() + delay;
			}

			public void mouseReleased(MouseEvent arg0) {
				
				if (longPressTimer < System.currentTimeMillis()){
					// set station ****
					new RadioFrequencyChooserScreen(mainScreen, 2);
				} else {
					if (CarStereo.fm2 != "" && CarStereo.fm2 != null){
						CarStereo.killAllProcesses();
						closeThisWindow();
						startRadio(CarStereo.fm2);
					} else {
						// set station ****
						new RadioFrequencyChooserScreen(mainScreen, 2);
					}
				}
			}
			
		});
		
		fm3Button.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				longPressTimer = System.currentTimeMillis() + delay;
			}

			public void mouseReleased(MouseEvent arg0) {
				
				if (longPressTimer < System.currentTimeMillis()){
					// set station ****
					new RadioFrequencyChooserScreen(mainScreen, 3);
				} else {
					if (CarStereo.fm3 != "" && CarStereo.fm3 != null){
						CarStereo.killAllProcesses();
						closeThisWindow();
						startRadio(CarStereo.fm3);
					} else {
						// set station ****
						new RadioFrequencyChooserScreen(mainScreen, 3);
					}
				}
			}
			
		});
		
		fm4Button.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				longPressTimer = System.currentTimeMillis() + delay;
			}

			public void mouseReleased(MouseEvent arg0) {
				
				if (longPressTimer < System.currentTimeMillis()){
					// set station ****
					new RadioFrequencyChooserScreen(mainScreen, 4);
				} else {
					if (CarStereo.fm4 != "" && CarStereo.fm4 != null){
						CarStereo.killAllProcesses();
						closeThisWindow();
						startRadio(CarStereo.fm4);
					} else {
						// set station ****
						new RadioFrequencyChooserScreen(mainScreen, 4);
					}
				}
			}
			
		});
		
		fm5Button.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				longPressTimer = System.currentTimeMillis() + delay;
			}

			public void mouseReleased(MouseEvent arg0) {
				
				if (longPressTimer < System.currentTimeMillis()){
					// set station ****
					new RadioFrequencyChooserScreen(mainScreen, 5);
				} else {
					if (CarStereo.fm5 != "" && CarStereo.fm5 != null){
						CarStereo.killAllProcesses();
						closeThisWindow();
						startRadio(CarStereo.fm5);
					} else {
						// set station ****
						new RadioFrequencyChooserScreen(mainScreen, 5);
					}
				}
			}
			
		});
		
		fm6Button.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				longPressTimer = System.currentTimeMillis() + delay;
			}

			public void mouseReleased(MouseEvent arg0) {
				
				if (longPressTimer < System.currentTimeMillis()){
					// set station ****
					new RadioFrequencyChooserScreen(mainScreen, 6);
				} else {
					if (CarStereo.fm6 != "" && CarStereo.fm6 != null){
						CarStereo.killAllProcesses();
						closeThisWindow();
						startRadio(CarStereo.fm6);
					} else {
						// set station ****
						new RadioFrequencyChooserScreen(mainScreen, 6);
					}
				}
			}
			
		});
		
		fm7Button.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				longPressTimer = System.currentTimeMillis() + delay;
			}

			public void mouseReleased(MouseEvent arg0) {
				
				if (longPressTimer < System.currentTimeMillis()){
					// set station ****
					new RadioFrequencyChooserScreen(mainScreen, 7);
				} else {
					if (CarStereo.fm7 != "" && CarStereo.fm7 != null){
						CarStereo.killAllProcesses();
						closeThisWindow();
						startRadio(CarStereo.fm7);
					} else {
						// set station ****
						new RadioFrequencyChooserScreen(mainScreen, 7);
					}
				}
			}
			
		});
		
		fm8Button.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				longPressTimer = System.currentTimeMillis() + delay;
			}

			public void mouseReleased(MouseEvent arg0) {
				
				if (longPressTimer < System.currentTimeMillis()){
					// set station ****
					new RadioFrequencyChooserScreen(mainScreen, 8);
				} else {
					if (CarStereo.fm8 != "" && CarStereo.fm8 != null){
						CarStereo.killAllProcesses();
						closeThisWindow();
						startRadio(CarStereo.fm8);
					} else {
						// set station ****
						new RadioFrequencyChooserScreen(mainScreen, 8);
					}
				}
			}
			
		});
	}
	
	void startRadio(String radioFrequency){
		CarStereo.killAllProcesses();
		CarStereo.lastRadioStation = radioFrequency;
		String[] command = {"/bin/sh", "-c", "rtl_fm -f "+CarStereo.lastRadioStation+"M -s 171000 -g 30 -F 9 | redsea -u -e | aplay -r 171000 -f S16_LE"};
		CarStereo.infoText1 = "FM "+CarStereo.lastRadioStation+" MHz";
		CarStereo.infoText2 = "Radio";
		mainScreen.labelInfo1.setText(CarStereo.infoText1);
		mainScreen.labelInfo2.setText(CarStereo.infoText2);
		CarStereo.startRadio(command);
		CarStereo.saveProperties();
	}
	
	void closeThisWindow(){
		frame.dispose();
		CarStereo.device.setFullScreenWindow(mainScreen.frame);
	}

	public void loadStations(){
		fm1Button.setText(CarStereo.fm1);
		if (CarStereo.fm1 == "" || CarStereo.fm1 == null){
			fm1Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm1Button.setSize(radioIcon.getWidth(), 135);
		}
		
		fm2Button.setText(CarStereo.fm2);
		if (CarStereo.fm2 == "" || CarStereo.fm2 == null){
			fm2Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm2Button.setSize(radioIcon.getWidth(), 135);
		}
		
		fm3Button.setText(CarStereo.fm3);
		if (CarStereo.fm3 == "" || CarStereo.fm3 == null){
			fm3Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm3Button.setSize(radioIcon.getWidth(), 135);
		}
		
		fm4Button.setText(CarStereo.fm4);
		if (CarStereo.fm4 == "" || CarStereo.fm4 == null){
			fm4Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm4Button.setSize(radioIcon.getWidth(), 135);
		}
		
		fm5Button.setText(CarStereo.fm5);
		if (CarStereo.fm5 == "" || CarStereo.fm5 == null){
			fm5Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm5Button.setSize(radioIcon.getWidth(), 135);
		}
		
		fm6Button.setText(CarStereo.fm6);
		if (CarStereo.fm6 == "" || CarStereo.fm6 == null){
			fm6Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm6Button.setSize(radioIcon.getWidth(), 135);
		}
		
		fm7Button.setText(CarStereo.fm7);
		if (CarStereo.fm7 == "" || CarStereo.fm7 == null){
			fm7Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm7Button.setSize(radioIcon.getWidth(), 135);
		}
		
		fm8Button.setText(CarStereo.fm8);
		if (CarStereo.fm8 == "" || CarStereo.fm8 == null){
			fm8Button.setSize(radioIcon.getWidth(),radioIcon.getHeight());
		} else {
			fm8Button.setSize(radioIcon.getWidth(), 135);
		}
	}
}
