package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.Font;
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
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
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
	JButton nextTrackButton;
	
	//JButton exitButton; // TODO remove or hide when done
	
	BufferedImage radioIcon;
	BufferedImage navigationIcon;
	BufferedImage playPauseIcon;
	BufferedImage nextTrackIcon;
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
	
	Thread guiThread;
	public Thread blueToothMonitorThread;
	
	public ArrayList<String> blueToothClientNames;
	ArrayList<String> blueToothClientMacs;
	ArrayList<Boolean> blueToothClientConnStatus;
	boolean blueToothIsOn;
	boolean blueToothIsDiscoverable;
	public Writer blueToothWriter;
	
	public RadioScreen radioScreen;
	public MP3Screen mp3Screen;
	
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
		
		Font font = new Font("RobotoCondensed", Font.PLAIN, 80);
		Font font2 = new Font("RobotoCondensed", Font.PLAIN, 30);
		
		labelTime = new JLabel("Car Stereo");
		labelTime.setForeground(Color.WHITE);
		labelTime.setFont(font);
		labelTime.setSize(420, 100);
		labelTime.setHorizontalAlignment(JLabel.CENTER);
		labelTime.setBounds(screenWidth/4-labelTime.getWidth()/2, screenHeight/8*1, labelTime.getWidth(), labelTime.getHeight());
	
		labelInfo1 = new JLabel(dayFormatter.format(time));
		labelInfo1.setForeground(Color.WHITE);
		labelInfo1.setFont(font2);
		labelInfo1.setSize(450, 32);
		labelInfo1.setHorizontalAlignment(JLabel.CENTER);
		labelInfo1.setBounds(screenWidth/4-labelInfo1.getWidth()/2, screenHeight/8*3, labelInfo1.getWidth(), labelInfo1.getHeight());
		
		labelInfo2 = new JLabel(monthFormatter.format(time));
		labelInfo2.setForeground(Color.WHITE);
		labelInfo2.setFont(font2);
		labelInfo2.setSize(450, 32);
		labelInfo2.setHorizontalAlignment(JLabel.CENTER);
		labelInfo2.setBounds(screenWidth/4-labelInfo2.getWidth()/2, screenHeight/8*4, labelInfo2.getWidth(), labelInfo2.getHeight());		 
		
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
		playPauseButton.setVisible(false);

		try {
			nextTrackIcon = ImageIO.read(getClass().getResource("/images/forward.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		nextTrackButton = new JButton(new ImageIcon(nextTrackIcon));
		nextTrackButton.setBorder(BorderFactory.createEmptyBorder());
		nextTrackButton.setBackground(Color.BLACK);
		nextTrackButton.setSize(nextTrackIcon.getWidth(), nextTrackIcon.getHeight());
		nextTrackButton.setBounds(screenWidth/4+20, screenHeight/8*6-nextTrackButton.getHeight()/2, nextTrackButton.getWidth(), nextTrackButton.getHeight());
		nextTrackButton.setVisible(false);
		
		panel.add(labelTime);
		//panel.add(exitButton);
		panel.add(radioButton);
		panel.add(navigationButton);
		panel.add(bluetoothButton);
		panel.add(mp3Button);
		panel.add(optionsButton);
		panel.add(engineButton);
		panel.add(labelInfo1);
		panel.add(labelInfo2);
		panel.add(playPauseButton);
		panel.add(nextTrackButton);
		
		frame.add(panel);
		frame.setSize(screenWidth, screenHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		// BUTTON LISTENERS
		loadButtonlisteners();
		
		// this thread updates all the mainScreen gui stuff
		if (CarStereo.debug) System.out.println("Starting GUI thread...");
		guiThread = new Thread(){
			public void run(){
				while(true){
					time = LocalDateTime.now();
					labelTime.setText(timeFormatter.format(time));
					
					// play modes 0=off 1=FM/AM 2=MP3 3=BlueTooth
					if (CarStereo.playMode == 0){
						playPauseButton.setBounds(screenWidth/4-playPauseButton.getWidth()/2, screenHeight/8*6-playPauseButton.getHeight()/2, playPauseButton.getWidth(), playPauseButton.getHeight());
						playPauseButton.setVisible(false);
						nextTrackButton.setVisible(false);
						
						labelInfo1.setText(dayFormatter.format(time));
						labelInfo2.setText(monthFormatter.format(time));
					} else if (CarStereo.playMode == 1){
						playPauseButton.setBounds(screenWidth/4-playPauseButton.getWidth()/2, screenHeight/8*6-playPauseButton.getHeight()/2, playPauseButton.getWidth(), playPauseButton.getHeight());
						playPauseButton.setVisible(true);
						nextTrackButton.setVisible(false);
						
						labelInfo1.setText(CarStereo.infoText1);
						if (CarStereo.radioIsPlaying){
							labelInfo2.setText(CarStereo.infoText2);
						} else {
							labelInfo2.setText("Radio Stopped");
						}
					} else if (CarStereo.playMode == 2){
						playPauseButton.setBounds(screenWidth/4-playPauseButton.getWidth()-20, screenHeight/8*6-playPauseButton.getHeight()/2, playPauseButton.getWidth(), playPauseButton.getHeight());
						playPauseButton.setVisible(true);
						nextTrackButton.setVisible(true);
						
						if (CarStereo.mp3Process == null){
							CarStereo.playMode = 0;
						} else if (!CarStereo.mp3Process.isAlive() && !CarStereo.mp3PlayerThread.isAlive() && CarStereo.mediaPlaylist.isEmpty()){
							CarStereo.playMode = 0;
						}
						
					} else if (CarStereo.playMode == 3){
						playPauseButton.setBounds(screenWidth/4-playPauseButton.getWidth()/2, screenHeight/8*6-playPauseButton.getHeight()/2, playPauseButton.getWidth(), playPauseButton.getHeight());
						playPauseButton.setVisible(false);
						nextTrackButton.setVisible(false);
						
						if (CarStereo.bluetoothIsPlaying){
							labelInfo1.setText("Bluetooth Connected");
							labelInfo2.setText(CarStereo.infoText2);
						} else {
							labelInfo1.setText("Bluetooth Paused");
							labelInfo2.setText(CarStereo.infoText2);
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		guiThread.start();
		if (CarStereo.debug) System.out.println("GUI thread started");
		
		// this thread starts a bluetooth listener to see when someone connects and disconnects
		if (CarStereo.debug) System.out.println("Starting bluetooth thread...");
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
					
					// get devices list from bluetoothctl
					blueToothWriter.write("devices" + System.lineSeparator());
					blueToothWriter.flush();
					inputText = "";
					boolean keepLooping = true;
					if (CarStereo.debug) System.out.println("Saved Bluetooth Device List:");
					while (keepLooping) {
						inputText = br.readLine();
						if (inputText.contains("Device")) {
							blueToothClientNames.add(inputText.substring(inputText.lastIndexOf(" ") + 1));
							blueToothClientMacs.add(inputText.substring(inputText.indexOf(" ") + 1, 17));
							blueToothClientConnStatus.add(false);
							if (CarStereo.debug) System.out.println("" + inputText);
						} else {
							keepLooping = false;
						}
						
					}
					if (CarStereo.debug) System.out.println("End List");
					
					// turn on bluetooth discovery
					blueToothWriter.write("discoverable on" + System.lineSeparator());
					blueToothWriter.flush();
					if (CarStereo.debug) System.out.println("BT set to discoverable");
							
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
							
							if (CarStereo.debug) System.out.println("BT DEVICE FOUND MAC: "+mac+ " Name: " + name);
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
							
							if (CarStereo.debug) System.out.println("BT CONTROLLER FOUND MAC: "+mac+ " Name: " + name);
						}
						if (inputText.contains("CHG") && inputText.contains("Controller") && inputText.contains("Powered")){
							// get powered state
							temp = inputText.lastIndexOf(":");
							if (inputText.substring(temp+1).contains("yes")){
								blueToothIsOn = true;
							} else {
								blueToothIsOn = false;
							}
							
							if (CarStereo.debug) System.out.println("BlueTooth is on: " + blueToothIsOn);
						}
						if (inputText.contains("agent") && inputText.contains("Confirm passkey")){
							// accept any request to pair
							blueToothWriter.write("yes"+System.lineSeparator());
							blueToothWriter.flush();
							
							if (CarStereo.debug) System.out.println("BlueTooth is on: " + blueToothIsOn);
						}
						if (inputText.contains("CHG") && inputText.contains("Controller") && inputText.contains("Discoverable")){
							// get discoverable state
							temp = inputText.lastIndexOf(":");
							if (inputText.substring(temp+1).contains("yes")){
								blueToothIsDiscoverable = true;
							} else {
								blueToothIsDiscoverable = false;
							}
							if (CarStereo.debug) System.out.println("BlueTooth is discoverable: " + blueToothIsOn);
						}
						if (inputText.contains("CHG") && inputText.contains("Device") && inputText.contains("Connected")){
							
							temp = inputText.lastIndexOf(":");
							temp2 = inputText.lastIndexOf("Device");
							mac = inputText.substring(0, temp-10).substring(temp2+7);
							name = "";
							
							if (inputText.substring(temp+1).contains("yes")){
								// start bluetooth audio
								if (CarStereo.playMode != 3) CarStereo.killAllProcesses();
								CarStereo.bluetoothIsPlaying = true;
								CarStereo.playMode = 3;
								CarStereo.currentlyConnectedBluetooth = mac;
								
								boolean nameIsKnown = false;
								for (int i = 0; i < blueToothClientMacs.size(); i++){
									if (blueToothClientMacs.get(i).contains(mac)) {
										name = blueToothClientNames.get(i);
										blueToothClientConnStatus.set(i, true);
										CarStereo.infoText2 = "Device: " + name;
										nameIsKnown = true;
									}
								}
								// if the name is not in blueToothClientMacs use the MAC for CarStereo.infoText2 - something needs to go there
								if (!nameIsKnown) {
									CarStereo.infoText2 = "Mac: " + mac;
								}
							} else {
								// stop bluetooth audio
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
							if (CarStereo.debug) System.out.println("BT device connected: " + CarStereo.bluetoothIsPlaying + " Mac: "+ mac + " Name " + name);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		blueToothMonitorThread.start();
		if (CarStereo.debug) System.out.println("Bluetooth thread started");
		
		if (CarStereo.wasUsingRadioLastTime) {
			if (CarStereo.debug) System.out.println("MainScreen: Radio was on " + CarStereo.lastRadioStation + " last time, auto starting radio...");
			String[] command = {"/bin/sh", "-c", "rtl_fm -f "+CarStereo.lastRadioStation+"M -s 171000 -g 30 -F 9 | redsea -u -e | aplay -r 171000 -f S16_LE"};
			CarStereo.infoText1 = "FM "+CarStereo.lastRadioStation+" MHz";
			CarStereo.infoText2 = "Radio";
			labelInfo1.setText(CarStereo.infoText1);
			labelInfo2.setText(CarStereo.infoText2);
			CarStereo.startRadio(command);
		}
	}
	
	void loadButtonlisteners(){
		radioButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (CarStereo.debug) System.out.println("MainScreen: Radio button pressed");
				openRadioScreen();
				
			}
			
		});
		
		navigationButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (CarStereo.debug) System.out.println("MainScreen: Navigation button pressed");
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
				if (CarStereo.debug) System.out.println("MainScreen: Play/Pause pressed");
				if (CarStereo.playMode == 1){
					startStopRadio();
				} else if (CarStereo.playMode == 2){
					if (CarStereo.mp3PlayerThread != null && !CarStereo.mp3PlayerThread.isAlive() && !CarStereo.mediaPlaylist.isEmpty()){
						mp3Screen.startPlayThread(null);
					} else {
						playPauseMP3();
					}
					
				} else if (CarStereo.playMode == 3){
					// right now the button doesn't even show while in bluetooth mode
					/*
					String command = "dbus-send --system --print-reply --dest=org.bluez /org/bluez/hci0/dev_" + CarStereo.currentlyConnectedBluetooth + " org.bluez.MediaControl1.Play";
					try {
						Process sendCommand = Runtime.getRuntime().exec(command);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
				}
			}
			
		});
		
		mp3Button.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (CarStereo.debug) System.out.println("MainScreen: MP3 button pressed");
				// mp3 button pressed
				
				String MEDIA_FOLDER_PATH=System.getProperty("user.home")+"/Media";
				//String MEDIA_FOLDER_PATH="/home/pi/Media";
				File file=new File(MEDIA_FOLDER_PATH);
				File[] allSubFiles=file.listFiles();
				
				if (CarStereo.debug) System.out.println("*** Media path: " + MEDIA_FOLDER_PATH);
				
				if (allSubFiles != null) {
					openMP3Screen();
				}
				
			}
			
		});
		
		engineButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (CarStereo.debug) System.out.println("MainScreen: OBD2 button pressed");
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
				if (CarStereo.debug) System.out.println("MainScreen: Options button pressed");
				openOptionsScreen();
			}
			
		});
		
		bluetoothButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (CarStereo.debug) System.out.println("MainScreen: Bluetooth button pressed");
				openBluetoothScreen();
			}
			
		});
		
		nextTrackButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (CarStereo.debug) System.out.println("MainScreen: Next track button pressed");
				try {
						CarStereo.writer.write("q");
						CarStereo.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
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
		mp3Screen = new MP3Screen(this);
	}
	
	void playPauseMP3(){
		try {
			CarStereo.writer.write("p");
			CarStereo.writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//CarStereo.writer.close();
		
		if (CarStereo.mp3IsPlaying){
			// if mp3 is already playing
			if (CarStereo.debug) System.out.println("Paused MP3");
			CarStereo.mp3IsPlaying = false;
			labelInfo2.setText("= PAUSED =");
		} else {
			// not already playing
			if (CarStereo.debug) System.out.println("Playing MP3 again");
			CarStereo.mp3IsPlaying = true;
			labelInfo1.setText(CarStereo.infoText1);
			labelInfo2.setText(CarStereo.infoText2);
		}
	}

	void startStopRadio(){
		
		if (CarStereo.radioIsPlaying){
			// if radio is already playing
			if (CarStereo.debug) System.out.println("Stopping Radio");
			CarStereo.radioIsPlaying = false;
			CarStereo.stopRadio();
		} else {
			// if its not playing already
			if (CarStereo.debug) System.out.println("Starting radio again");
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
