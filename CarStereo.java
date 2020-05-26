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
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;

import org.swampsoft.carstereo.screens.MainScreen;
import org.swampsoft.carstereo.screens.FullScreenControls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CarStereo {
	public static double version = 1.00;
	
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
	
	public static String currentlyConnectedBluetooth;
	
	// properties
	static Properties properties;
	static String PROPERTIES_PATH;
	public static String lastRadioStation = "";
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
	
	public static void main(String[] args) {
		// create properties if it doesn't exists
		properties = new Properties();
		PROPERTIES_PATH=System.getProperty("user.home")+"/CarStereoConfig.properties";
		File propertiesFile = new File(PROPERTIES_PATH);
		boolean fileExists = propertiesFile.exists();
		if (!fileExists){
			properties.setProperty("lastRadioStation", "104.1");
			
			saveProperties();
		}
		
		// load properties
		loadProperties();
		
		MainScreen mainScreen = new MainScreen();
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
		
		try {
			FileOutputStream out = new FileOutputStream(PROPERTIES_PATH);
			properties.store(out, null);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	}
	
	public static void stopMP3Player(){
		if(CarStereo.writer != null){
			//CarStereo.mediaPlaylist.clear();
			CarStereo.mp3PlayerThread.interrupt();
			CarStereo.mp3PlayerThread = null;
			try {
				CarStereo.writer.write("q");
				//CarStereo.writer.flush();
				//CarStereo.writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CarStereo.mp3IsPlaying = false;
			CarStereo.playMode = 0;
		}
	}
	
	public static void startRadio(String[] command){
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
							System.out.println(tempText);
							if (tempText.contains("callsign") || tempText.contains("callsign_uncertain")) {
								JSONObject jsonObj = JSON.parseObject(tempText);
								
								String callsign = jsonObj.getString("callsign");
								String callsign_uncertain = jsonObj.getString("callsign_uncertain");
								String prog_type = jsonObj.getString("prog_type");
								
								if (callsign != null && !callsign.contains("null")){
									infoText1 = "FM " + lastRadioStation + " " + callsign;
								}
								
								if (callsign_uncertain != null && !callsign_uncertain.contains("null")){
									//infoText1 = callsign_uncertain + " (Error " + lastRadioStation + ")";
									infoText1 = "FM " + lastRadioStation + " MHz";
								}
								
								if (prog_type != null && !prog_type.contains("null")){
									infoText2 = "" + prog_type;
								} else {
									//infoText2 = "Radio";
								}
							}
							counter++;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			radioTextThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void stopRadio(){
		try {
			Process process = Runtime.getRuntime().exec("pgrep rtl_fm");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String rtlFMpid = br.readLine();
			process.waitFor();
			process.destroy();
			process = Runtime.getRuntime().exec("pgrep aplay");
			br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String aplayPid = br.readLine();
			process.waitFor();
			process.destroy();
			radioProcess.destroy();
			process = Runtime.getRuntime().exec("kill -9 "+aplayPid);
			process = Runtime.getRuntime().exec("kill -9 "+rtlFMpid);
			radioIsPlaying = false;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void killAllProcesses(){
		// kill radio processes
		if (playMode == 1){
			stopRadio();
		} else if (playMode == 2){
			//stop mp3s/movies
			stopMP3Player();
		} else if (playMode == 3){
			// stop bluetooth
		}
	}
}
