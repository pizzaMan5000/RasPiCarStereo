package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.swampsoft.carstereo.CarStereo;

public class MainScreen {
	
	int screenWidth;
	int screenHeight;
	
	JFrame frame;
	JPanel panel;
	JLabel labelTime;
	JLabel labelInfo1;
	JLabel labelInfo2;
	
	JButton radioButton;
	JButton navigationButton;
	JButton mp3Button;
	JButton bluetoothButton;
	JButton engineButton;
	JButton optionsButton;
	JButton playPauseButton;
	
	JButton exitButton; // todo remove when done
	
	BufferedImage radioIcon;
	BufferedImage navigationIcon;
	BufferedImage playPauseIcon;
	BufferedImage mp3Icon;
	BufferedImage bluetoothIcon;
	BufferedImage engineIcon;
	BufferedImage optionsIcon;
	
	Process naviProcess;
	Process engineProcess;
	Process blueToothProcess;
	
	LocalDateTime time;
	DateTimeFormatter timeFormatter;
	DateTimeFormatter dayFormatter;
	DateTimeFormatter monthFormatter;
	
	Thread timeThread;
	Thread blueToothMonitorThread;
	
	public ArrayList<String> blueToothClientNames;
	ArrayList<String> blueToothClientMacs;
	ArrayList<Boolean> blueToothClientConnStatus;
	boolean blueToothIsOn;
	boolean blueToothIsDiscoverable;
	public Writer blueToothWriter;
	
	public RadioScreen radioScreen;
	
	public MainScreen(){
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		time = LocalDateTime.now();
		timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
		dayFormatter = DateTimeFormatter.ofPattern("EEEE");
		monthFormatter = DateTimeFormatter.ofPattern("MMMM dd");
		
		frame = new JFrame("Car Stereo");
		frame.setBackground(Color.BLACK);

		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		Font font = new Font("TimesRoman", Font.PLAIN, 80);
		Font font2 = new Font("TimesRoman", Font.PLAIN, 30);
		
		labelTime = new JLabel("Car Stereo");
		labelTime.setForeground(Color.WHITE);
		labelTime.setFont(font);
		labelTime.setSize(330, 100);
		labelTime.setHorizontalAlignment(JLabel.CENTER);
		labelTime.setBounds(screenWidth/4-labelTime.getWidth()/2, screenHeight/8*1, labelTime.getWidth(), labelTime.getHeight());
	
		labelInfo1 = new JLabel(dayFormatter.format(time));
		labelInfo1.setForeground(Color.WHITE);
		labelInfo1.setFont(font2);
		labelInfo1.setSize(350, 32);
		labelInfo1.setHorizontalAlignment(JLabel.CENTER);
		labelInfo1.setBounds(screenWidth/4-labelInfo1.getWidth()/2, screenHeight/8*3, labelInfo1.getWidth(), labelInfo1.getHeight());
		
		labelInfo2 = new JLabel(monthFormatter.format(time));
		labelInfo2.setForeground(Color.WHITE);
		labelInfo2.setFont(font2);
		labelInfo2.setSize(350, 32);
		labelInfo2.setHorizontalAlignment(JLabel.CENTER);
		labelInfo2.setBounds(screenWidth/4-labelInfo2.getWidth()/2, screenHeight/8*4, labelInfo2.getWidth(), labelInfo2.getHeight());
		
		timeThread = new Thread(){
			public void run(){
				while(true){
					time = LocalDateTime.now();
					labelTime.setText(timeFormatter.format(time));
					if (CarStereo.playMode == 0){
						labelInfo1.setText(dayFormatter.format(time));
						labelInfo2.setText(monthFormatter.format(time));
					} else if (CarStereo.playMode == 1){
						labelInfo1.setText(CarStereo.infoText1);
						if (CarStereo.radioIsPlaying){
							labelInfo2.setText(CarStereo.infoText2);
						} else {
							labelInfo2.setText("Radio Stopped");
						}
					} else if (CarStereo.playMode == 2){
						if (!CarStereo.mp3Process.isAlive() && !CarStereo.mp3PlayerThread.isAlive()){
							CarStereo.playMode = 0;
						}
						
					} else if (CarStereo.playMode == 3){
						if (CarStereo.bluetoothIsPlaying){
							labelInfo1.setText("Bluetooth Connected");
							labelInfo2.setText(CarStereo.infoText2);
						} else {
							labelInfo1.setText("Bluetooth Paused");
							labelInfo2.setText(CarStereo.infoText2);
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		timeThread.start();
		
		blueToothMonitorThread = new Thread(){
			public void run(){
				try {
					blueToothProcess = Runtime.getRuntime().exec("bluetoothctl");
					InputStream monitorStream = blueToothProcess.getInputStream();
					OutputStream writerStream = blueToothProcess.getOutputStream();
					blueToothWriter = new OutputStreamWriter(writerStream);
					BufferedReader br = new BufferedReader(new InputStreamReader(monitorStream));
					String inputText;
					int temp;
					int temp2;
					int temp3;
					String mac;
					String name;
					boolean tempBoolean;
					if (blueToothClientNames == null || blueToothClientMacs == null){
						blueToothClientNames = new ArrayList<String>();
						blueToothClientMacs = new ArrayList<String>();
						blueToothClientConnStatus = new ArrayList<Boolean>();
					}
					
					// turn of bluetooth discovery
					blueToothWriter.write("discoverable on" + System.lineSeparator());
					blueToothWriter.flush();
							
					while (true){
						inputText = br.readLine();
						//System.out.println(inputText);
						if (inputText.contains("Device") && inputText.contains("NEW")){
							temp = inputText.lastIndexOf(":");
							temp2 = inputText.lastIndexOf("Device");
							mac = inputText.substring(0, temp+3).substring(temp2+7);
							name = inputText.substring(temp+4);
							
							boolean isAlreadyInList = false;
							for (int i = 0; i < blueToothClientMacs.size(); i++){
								if (blueToothClientMacs.get(i).contains(mac)) {
									isAlreadyInList = true;
								}
							}
							
							if (!isAlreadyInList){
								blueToothClientNames.add(name);
								blueToothClientMacs.add(mac);
								blueToothClientConnStatus.add(false);
							}
							
							System.out.println("BT DEVICE FOUND MAC: "+mac+ " Name: " + name);
						}
						if (inputText.contains("Controller") && inputText.contains("NEW")){
							temp = inputText.lastIndexOf(":");
							temp2 = inputText.lastIndexOf("Controller");
							mac = inputText.substring(0, temp+3).substring(temp2+11);
							name = inputText.substring(temp+4);
							if (name.contains("default")) {
								temp3 = name.lastIndexOf("default");
								name = name.substring(0, temp3-2);
							}
							
							System.out.println("BT CONTROLLER FOUND MAC: "+mac+ " Name: " + name);
						}
						if (inputText.contains("CHG") && inputText.contains("Controller") && inputText.contains("Powered")){
							// get powered state
							temp = inputText.lastIndexOf(":");
							if (inputText.substring(temp+1).contains("yes")){
								blueToothIsOn = true;
							} else {
								blueToothIsOn = false;
							}
							
							System.out.println("BlueTooth is on: " + blueToothIsOn);
						}
						if (inputText.contains("agent") && inputText.contains("Confirm passkey")){
							// accept any request to pair
							blueToothWriter.write("yes"+System.lineSeparator());
							blueToothWriter.flush();
							
							System.out.println("BlueTooth is on: " + blueToothIsOn);
						}
						if (inputText.contains("CHG") && inputText.contains("Controller") && inputText.contains("Discoverable")){
							// get discoverable state
							temp = inputText.lastIndexOf(":");
							if (inputText.substring(temp+1).contains("yes")){
								blueToothIsDiscoverable = true;
							} else {
								blueToothIsDiscoverable = false;
							}
							System.out.println("BlueTooth is discoverable: " + blueToothIsOn);
						}
						if (inputText.contains("CHG") && inputText.contains("Device") && inputText.contains("Connected")){
							
							temp = inputText.lastIndexOf(":");
							temp2 = inputText.lastIndexOf("Device");
							mac = inputText.substring(0, temp-10).substring(temp2+7);
							name = "";
							
							if (inputText.substring(temp+1).contains("yes")){
								if (CarStereo.playMode != 3) CarStereo.killAllProcesses();
								CarStereo.bluetoothIsPlaying = true;
								CarStereo.playMode = 3;
								CarStereo.currentlyConnectedBluetooth = mac;
								for (int i = 0; i < blueToothClientMacs.size(); i++){
									if (blueToothClientMacs.get(i).contains(mac)) {
										name = blueToothClientNames.get(i);
										blueToothClientConnStatus.set(i, true);
										CarStereo.infoText2 = "Device: " + name;
									}
								}
							} else {
								boolean anyDevicesConnected = false;
								for (int i = 0; i < blueToothClientMacs.size(); i++){
									if (blueToothClientMacs.get(i).contains(mac)) {
										name = blueToothClientNames.get(i);
										blueToothClientConnStatus.set(i, false);
										//System.out.println("Mac FOUND - Name: " + name + " removed");
									}
									if (blueToothClientConnStatus.get(i) == true) anyDevicesConnected = true;
								}
								if (!anyDevicesConnected){
									CarStereo.bluetoothIsPlaying = false;
									CarStereo.playMode = 0;
								}
							}
							System.out.println("BT device connected: " + CarStereo.bluetoothIsPlaying + " Mac: "+ mac + " Name " + name);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		blueToothMonitorThread.start();
		
		exitButton = new JButton();
		exitButton.setFont(font2);
		exitButton.setText("x");
		exitButton.setBackground(Color.BLACK);
		exitButton.setForeground(Color.WHITE);
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		//exitButton.setToolTipText("EXIT");
		exitButton.setSize(50, 40);
		exitButton.setBounds(screenWidth/2-exitButton.getWidth()/2, screenHeight - 70, exitButton.getWidth(), exitButton.getHeight());		 
		
		try {
			radioIcon = ImageIO.read(getClass().getResource("/images/radio-small.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		radioButton = new JButton(new ImageIcon(radioIcon));
		radioButton.setBorder(BorderFactory.createEmptyBorder());
		radioButton.setBackground(Color.BLACK);
		radioButton.setSize(100, 100);
		radioButton.setBounds(screenWidth/6*4-radioButton.getWidth()/2, screenHeight/4*1-radioButton.getHeight()/2, radioButton.getWidth(), radioButton.getHeight());
		
		try {
			mp3Icon = ImageIO.read(getClass().getResource("/images/playlist-small.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mp3Button = new JButton(new ImageIcon(mp3Icon));
		mp3Button.setBackground(Color.black);
		mp3Button.setBorder(BorderFactory.createEmptyBorder());
		//mp3Button.setText("MP3s");
		mp3Button.setSize(100,100);
		mp3Button.setBounds(screenWidth/6*5-mp3Button.getWidth()/2, screenHeight/4*1-mp3Button.getHeight()/2, mp3Button.getWidth(), mp3Button.getHeight());
		
		try {
			bluetoothIcon = ImageIO.read(getClass().getResource("/images/bluetooth-small.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		bluetoothButton = new JButton(new ImageIcon(bluetoothIcon));
		bluetoothButton.setBackground(Color.black);
		bluetoothButton.setBorder(BorderFactory.createEmptyBorder());
		//bluetoothButton.setText("Bluetooth");
		bluetoothButton.setSize(100,100);
		bluetoothButton.setBounds(screenWidth/6*5-bluetoothButton.getWidth()/2, screenHeight/4*2-bluetoothButton.getHeight()/2, bluetoothButton.getWidth(), bluetoothButton.getHeight());
		
		
		try {
			navigationIcon = ImageIO.read(getClass().getResource("/images/navigation-small.png"));
			//navigationIcon = ImageIO.read(new File("images/navigation.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		navigationButton = new JButton(new ImageIcon(navigationIcon));
		navigationButton.setBackground(Color.black);
		navigationButton.setBorder(BorderFactory.createEmptyBorder());
		//navigationButton.setText("Navigation");
		navigationButton.setSize(100,100);
		navigationButton.setBounds(screenWidth/6*4-navigationButton.getWidth()/2, screenHeight/4*2-navigationButton.getHeight()/2, navigationButton.getWidth(), navigationButton.getHeight());
		
		try {
			engineIcon = ImageIO.read(getClass().getResource("/images/engine-small.png"));
			//navigationIcon = ImageIO.read(new File("images/navigation.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		engineButton = new JButton(new ImageIcon(engineIcon));
		engineButton.setBackground(Color.black);
		engineButton.setBorder(BorderFactory.createEmptyBorder());
		//fileManagerButton.setText("File Manager");
		engineButton.setSize(100,100);
		engineButton.setBounds(screenWidth/6*4-engineButton.getWidth()/2, screenHeight/4*3-engineButton.getHeight()/2, engineButton.getWidth(), engineButton.getHeight());
		
		try {
			optionsIcon = ImageIO.read(getClass().getResource("/images/options-small.png"));
			//navigationIcon = ImageIO.read(new File("images/navigation.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		optionsButton = new JButton(new ImageIcon(optionsIcon));
		optionsButton.setBackground(Color.black);
		optionsButton.setBorder(BorderFactory.createEmptyBorder());
		//fileManagerButton.setText("File Manager");
		optionsButton.setSize(100,100);
		optionsButton.setBounds(screenWidth/6*5-optionsButton.getWidth()/2, screenHeight/4*3-optionsButton.getHeight()/2, optionsButton.getWidth(), optionsButton.getHeight());
		
		
		try {
			playPauseIcon = ImageIO.read(getClass().getResource("/images/play_pause.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		playPauseButton = new JButton(new ImageIcon(playPauseIcon));
		playPauseButton.setBorder(BorderFactory.createEmptyBorder());
		playPauseButton.setBackground(Color.BLACK);
		playPauseButton.setSize(playPauseIcon.getWidth(), playPauseIcon.getHeight());
		playPauseButton.setBounds(screenWidth/4-playPauseButton.getWidth()/2, screenHeight/8*6-playPauseButton.getHeight()/2, playPauseButton.getWidth(), playPauseButton.getHeight());
		
		panel.add(labelTime);
		panel.add(exitButton);
		panel.add(radioButton);
		panel.add(navigationButton);
		panel.add(bluetoothButton);
		panel.add(mp3Button);
		panel.add(optionsButton);
		panel.add(engineButton);
		panel.add(labelInfo1);
		panel.add(labelInfo2);
		panel.add(playPauseButton);
		
		frame.add(panel);
		frame.setSize(screenWidth, screenHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		loadButtonlisteners();
		// set volume
		String command = "amixer sset Master,0 100%";
		try {
			Process setVolume = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void loadButtonlisteners(){
		exitButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// kill processes
				CarStereo.killAllProcesses();
				
				// EXIT EVERYTHING
				System.exit(0);
			}
			
		});
		
		radioButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				openRadioScreen();
				
			}
			
		});
		
		navigationButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				try {
					naviProcess = Runtime.getRuntime().exec("navit");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		playPauseButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (CarStereo.playMode == 1){
					startStopRadio();
				} else if (CarStereo.playMode == 2){
					playPauseMP3();
				} else if (CarStereo.playMode == 3){
					String command = "dbus-send --system --print-reply --dest=org.bluez /org/bluez/hci0/dev_" + CarStereo.currentlyConnectedBluetooth + " org.bluez.MediaControl1.Play";
					try {
						Process sendCommand = Runtime.getRuntime().exec(command);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		
		mp3Button.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// mp3 button pressed
				openMP3Screen();
			}
			
		});
		
		engineButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// engine button pressed
				try {
					//engineProcess = Runtime.getRuntime().exec("pcmanfm --no-desktop ~/Media");
					engineProcess = Runtime.getRuntime().exec("scantool");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		optionsButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				openOptionsScreen();
			}
			
		});
		
		bluetoothButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				openBluetoothScreen();
			}
			
		});
	}
	
	void openBluetoothScreen(){
		new BluetoothScreen(this);
	}
	
	void openOptionsScreen(){
		new OptionsScreen(this);
	}
	
	void openRadioScreen(){
		radioScreen = new RadioScreen(this);
	}
	
	void openMP3Screen(){
		new MP3Screen(this);
	}
	
	void playPauseMP3(){
		CarStereo.writer.write("p");
		CarStereo.writer.flush();
		//CarStereo.writer.close();
		
		if (CarStereo.mp3IsPlaying){
			// if mp3 is already playing
			CarStereo.mp3IsPlaying = false;
			labelInfo2.setText("= PAUSED =");
		} else {
			// not already playing
			CarStereo.mp3IsPlaying = true;
			labelInfo1.setText(CarStereo.infoText1);
			labelInfo2.setText(CarStereo.infoText2);
		}
	}

	void startStopRadio(){
		if (CarStereo.radioIsPlaying){
			// if radio is already playing
			CarStereo.radioIsPlaying = false;
			CarStereo.stopRadio();
		} else {
			// if its not playing already
			CarStereo.radioIsPlaying = true;
			String[] command = {"/bin/sh", "-c", "rtl_fm -f "+ CarStereo.lastRadioStation +"M -M fm -s 171000 | aplay -r 171000 -f S16_LE"};
			try {
				CarStereo.radioProcess = Runtime.getRuntime().exec(command);
				CarStereo.playMode = 1;
				// set info texts
				//labelInfo1.setText("FM "+CarStereo.lastRadioStation+" MHz");
				//labelInfo2.setText("Radio");
				labelInfo1.setText(CarStereo.infoText1);
				labelInfo2.setText(CarStereo.infoText2);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
