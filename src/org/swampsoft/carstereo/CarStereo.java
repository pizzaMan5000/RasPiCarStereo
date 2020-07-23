package org.swampsoft.carstereo;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Properties;

import org.swampsoft.carstereo.screens.MainScreen;
import org.swampsoft.carstereo.screens.FullScreenControls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CarStereo {
	public static double version = 1.00;
	public static boolean debug = false;
	
	public static boolean radioIsPlaying = false;
	public static boolean mp3IsPlaying = false;
	public static boolean bluetoothIsPlaying = false;
	
	// play modes 0=off 1=FM/AM 2=MP3 3=BlueTooth
	public static int playMode = 0;
	
	public static Process radioProcess;
	public static Process mp3Process;
	
	public static Thread mp3PlayerThread;;
	
	public static Thread radioTextThread;
	
	public static OutputStream stream;
	public static OutputStreamWriter writer;
	
	// strings for info text on mainscreen
	public static String infoText1;
	public static String infoText2;
	
	public static String currentlyConnectedBluetooth = "";
	
	// properties
	static Properties properties;
	static String PROPERTIES_PATH;
	public static String lastRadioStation = "";
	public static boolean wasUsingRadioLastTime = false;
	public static String fm1 = "";
	public static String fm2 = "";
	public static String fm3 = "";
	public static String fm4 = "";
	public static String fm5 = "";
	public static String fm6 = "";
	public static String fm7 = "";
	public static String fm8 = "";
	
	public static ArrayList<File> mediaPlaylist;
	
	public static FullScreenControls fullScreenControls;
	
	public static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	static MainScreen mainScreen;
	
	public static void main(String[] args) {
		// read arguments
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-debug")) {
				debug = true;
				System.out.println("Debug Messages On");
			}
		}
		
		// create properties if it doesn't exists
		properties = new Properties();
		PROPERTIES_PATH=System.getProperty("user.home")+"/CarStereoConfig.properties";
		File propertiesFile = new File(PROPERTIES_PATH);
		boolean fileExists = propertiesFile.exists();
		if (!fileExists){
			properties.setProperty("lastRadioStation", "104.1");
			
			saveProperties();
			if (debug) System.out.println("Properties file not found, new one created.");
		}
		
		// load properties
		loadProperties();
		
		mainScreen = new MainScreen();
	}
	
	public static void saveProperties(){
		if (lastRadioStation != null) properties.setProperty("lastRadioStation", lastRadioStation);
		if (fm1 != null) properties.setProperty("fm1", fm1);
		if (fm2 != null) properties.setProperty("fm2", fm2);
		if (fm3 != null) properties.setProperty("fm3", fm3);
		if (fm4 != null) properties.setProperty("fm4", fm4);
		if (fm5 != null) properties.setProperty("fm5", fm5);
		if (fm6 != null) properties.setProperty("fm6", fm6);
		if (fm7 != null) properties.setProperty("fm7", fm7);
		if (fm8 != null) properties.setProperty("fm8", fm8);
		if (wasUsingRadioLastTime) {
			properties.setProperty("wasUsingRadioLastTime", "true");
		} else {
			properties.setProperty("wasUsingRadioLastTime", "false");
		}
		
		
		try {
			FileOutputStream out = new FileOutputStream(PROPERTIES_PATH);
			properties.store(out, null);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (debug) System.out.println("Properties saved");
	}
	
	public static void loadProperties(){
		try {
			FileInputStream in;
			in = new FileInputStream(PROPERTIES_PATH);
			properties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		lastRadioStation = properties.getProperty("lastRadioStation");
		fm1 = properties.getProperty("fm1");
		fm2 = properties.getProperty("fm2");
		fm3 = properties.getProperty("fm3");
		fm4 = properties.getProperty("fm4");
		fm5 = properties.getProperty("fm5");
		fm6 = properties.getProperty("fm6");
		fm7 = properties.getProperty("fm7");
		fm8 = properties.getProperty("fm8");
		wasUsingRadioLastTime = Boolean.parseBoolean(properties.getProperty("wasUsingRadioLastTime"));
	}
	
	public static void stopMP3Player(){
		if (debug) System.out.println("Stopping omxplayer...");
		if (writer != null){
			//mediaPlaylist.clear();
			try {
				writer.write("q");
				writer.flush();
				//writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp3PlayerThread.interrupt();
			mp3PlayerThread = null;
		}
		
		// make sure omxplayer is dead
		try {
			Process process;
			process = Runtime.getRuntime().exec("pgrep omxplayer");
			BufferedReader br = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
			String tempText;
			while ((tempText = br.readLine()) != null) {
				process = Runtime.getRuntime().exec("kill -9 " + Integer.parseInt(tempText));
				if (debug) System.out.println("Stopping process with PID: " + tempText);
			}
			br.close();
			process.waitFor();
			process.destroy();
			
			playMode = 0;
			mp3IsPlaying = false;
			if (debug) System.out.println("OMXPlayer killed");
			
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startRadio(String[] command){
		if (debug) System.out.println("Starting radio with command:\n");
		
		for (int i = 0; i < command.length; i++) {
			if (debug) System.out.print(command[i] + " ");
		}
		if (debug) System.out.println("");
		
		try {
			radioProcess = Runtime.getRuntime().exec(command);
			playMode = 1;
			radioIsPlaying = true;
			
			radioTextThread = new Thread(){
				public void run(){
					InputStream infoStream = radioProcess.getErrorStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(infoStream));
					String tempText;
					int counter = 0;
					try {
						while ((tempText = br.readLine()) != null && counter < 35){
							//System.out.println(tempText);
							if (tempText.contains("callsign") || tempText.contains("callsign_uncertain")) {
								JSONObject jsonObj = JSON.parseObject(tempText);
								
								String callsign = jsonObj.getString("callsign");
								String callsign_uncertain = jsonObj.getString("callsign_uncertain");
								String prog_type = jsonObj.getString("prog_type");
								String radioText = jsonObj.getString("radiotext");
								
								if (callsign != null && !callsign.contains("null")){
									infoText1 = "FM " + lastRadioStation + " " + callsign;
								}
								
								if (callsign_uncertain != null && !callsign_uncertain.contains("null")){
									infoText1 = "FM " + lastRadioStation + " MHz";
								}
								
								if (prog_type != null && !prog_type.contains("null")){
									infoText2 = "" + prog_type;
								}
								
								if (radioText != null && !radioText.contains("null")){
									infoText2 = "" + radioText;
								}
							}
							counter++;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("THE FOLLOWING ERROR IS PROBABLY NOT A PROBLEM:");
						e.printStackTrace();
					}
				}
			};
			radioTextThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (debug) System.out.println("Radio started. PID: " + radioTextThread.getId());
	}
	
	public static void stopRadio(){
		if (debug) System.out.println("Stopping radio...");
		wasUsingRadioLastTime = false;
		saveProperties();
		try {
			Process process = Runtime.getRuntime().exec("pgrep rtl_fm");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String tempText;
			while ((tempText = br.readLine()) != null) {
				process = Runtime.getRuntime().exec("kill -9 " + Integer.parseInt(tempText));
			}
			process.waitFor();
			br.close();
			process.destroy();
			process = Runtime.getRuntime().exec("pgrep aplay -a");
			br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			tempText = "";
			while ((tempText = br.readLine()) != null) {
				if (tempText.contains("bluealsa")) {
					// don't kill this process, it has to do with the bluetooth audio player
				} else {
					// kill it!
					process = Runtime.getRuntime().exec("kill -9 " + Integer.parseInt(tempText.substring(0, tempText.indexOf(" "))));
					if (debug) System.out.println("Stopping process with PID: " + Integer.parseInt(tempText.substring(0, tempText.indexOf(" "))));
				}
			}
			process.waitFor();
			br.close();
			process.destroy();
			radioProcess.destroy();
			radioIsPlaying = false;
			//playMode = 0; // dont' do this, play/pause button will disappear
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (debug) System.out.println("Radio stopped");
	}
	
	public static void disconnectBluetooth() {
		if (debug) System.out.println("Disconnecting BT device...");
		// disconnect all bluetooth devices
		if (mainScreen.blueToothMonitorThread != null) {
			if (mainScreen.blueToothMonitorThread.isAlive()) {
				if (mainScreen.blueToothWriter != null && !currentlyConnectedBluetooth.equals("")) {
					if (debug) System.out.println("Disconnecting " + currentlyConnectedBluetooth);
					try {
						mainScreen.blueToothWriter.write("disconnect " + currentlyConnectedBluetooth + System.lineSeparator());
						mainScreen.blueToothWriter.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
		playMode = 0;
		
		if (debug) System.out.println("BT device disconnected");
	}

	public static void killAllProcesses(){
		if (debug) System.out.println("Kill all processes - Playmode: " + playMode);
		// kill radio processes
		if (playMode == 1){
			stopRadio();
		} else if (playMode == 2){
			//stop mp3s/movies
			stopMP3Player();
		} else if (playMode == 3){
			// stop bluetooth
			disconnectBluetooth();
		}
	}
}
