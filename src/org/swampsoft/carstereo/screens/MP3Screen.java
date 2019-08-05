package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.swampsoft.carstereo.CarStereo;

public class MP3Screen {
	int screenWidth;
	int screenHeight;
	
	JFrame frame;
	JPanel panel;
	JScrollPane scrollPane;
	JLabel label;
	JList list;
	JButton backButton;
	JButton playButton;
	JButton playlistButton;
	JButton addButton;
	
	BufferedImage backButtonIcon;
	BufferedImage playButtonIcon;
	BufferedImage playlistButtonIcon;
	BufferedImage addButtonIcon;
	BufferedImage folderOpenIcon;
	
	String MEDIA_FOLDER_PATH;
	String MEDIA_FOLDER_PATH_ABSOLUTE;
	File file;
	File[] allSubFiles;
	String playFile;
	ArrayList<String> fileNameList;
	ArrayList<Integer> fileTypeList;
	
	MainScreen mainScreen;
	
	public MP3Screen(final MainScreen mainScreen){
		this.mainScreen = mainScreen;
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		Font font = new Font("TimesRoman", Font.PLAIN, 30);
		
		MEDIA_FOLDER_PATH=System.getProperty("user.home")+"/Media";
		file=new File(MEDIA_FOLDER_PATH);
		MEDIA_FOLDER_PATH_ABSOLUTE = file.getAbsolutePath();
		getFileList(file, false);
		
		frame = new JFrame("Media List");
		frame.setBackground(Color.BLACK);

		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		label = new JLabel("Media List");
		label.setForeground(Color.WHITE);
		label.setFont(font);
		label.setSize(180, 20);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(screenWidth/2-label.getWidth()/2, screenHeight/10, label.getWidth(), label.getHeight());
		
		try {
			backButtonIcon = ImageIO.read(getClass().getResource("/images/back-small.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		backButton = new JButton(new ImageIcon(backButtonIcon));
		backButton.setBackground(Color.BLACK);
		//backButton.setForeground(Color.WHITE);
		backButton.setBorder(BorderFactory.createEmptyBorder());
		backButton.setSize(100, 100);
		backButton.setBounds(screenWidth/2-backButton.getWidth()/2, screenHeight - 120, backButton.getWidth(), backButton.getHeight());
		
		try {
			playButtonIcon = ImageIO.read(getClass().getResource("/images/play.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		playButton = new JButton(new ImageIcon(playButtonIcon));
		//playButton.setFont(font);
		//playButton.setText("Play");
		playButton.setBackground(Color.BLACK);
		//playButton.setForeground(Color.WHITE);
		playButton.setSize(128, 128);
		playButton.setBorder(BorderFactory.createEmptyBorder());
		playButton.setBounds(screenWidth-100-playButton.getWidth()/2, screenHeight/4-playButton.getHeight()/2, playButton.getWidth(), playButton.getHeight());

		try {
			playlistButtonIcon = ImageIO.read(getClass().getResource("/images/playlist-small.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		playlistButton = new JButton(new ImageIcon(playlistButtonIcon));
		playlistButton.setBackground(Color.BLACK);
		playlistButton.setSize(100, 100);
		playlistButton.setBorder(BorderFactory.createEmptyBorder());
		playlistButton.setBounds(screenWidth-100-playlistButton.getWidth()/2, screenHeight/4*3-playlistButton.getHeight()/2, playlistButton.getWidth(), playlistButton.getHeight());
		
		try {
			addButtonIcon = ImageIO.read(getClass().getResource("/images/plus-small.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addButton = new JButton(new ImageIcon(addButtonIcon));
		addButton.setBackground(Color.BLACK);
		addButton.setSize(100, 100);
		addButton.setBorder(BorderFactory.createEmptyBorder());
		addButton.setBounds(screenWidth-100-addButton.getWidth()/2, screenHeight/2-addButton.getHeight()/2, addButton.getWidth(), addButton.getHeight());
		
		try {
			folderOpenIcon = ImageIO.read(getClass().getResource("/images/folder-open.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		list = new JList(fileNameList.toArray());
		list.setBackground(Color.BLACK);
		list.setForeground(Color.WHITE);
		list.setFont(font);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(7);
		//list.setSize(screenWidth-250, screenHeight - 226);
		//list.setBounds(50,100,list.getWidth(), list.getHeight());
		list.setSelectedIndex(0);
		if (fileTypeList.get(list.getSelectedIndex()) == 0) playButton.setIcon(new ImageIcon(playButtonIcon));
		else if (fileTypeList.get(list.getSelectedIndex()) == 1) playButton.setIcon(new ImageIcon(folderOpenIcon));
		else if (fileTypeList.get(list.getSelectedIndex()) == 2) playButton.setIcon(new ImageIcon(backButtonIcon));
		
		scrollPane = new JScrollPane(list);
		scrollPane.setSize(screenWidth-250, screenHeight - 226);
		scrollPane.setBounds( 50, 100, scrollPane.getWidth(), scrollPane.getHeight());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(40,0));
		scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		scrollPane.getViewport().setViewPosition(new Point(0, 0));
		scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
		scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		panel.add(label);
		panel.add(backButton);
		panel.add(scrollPane);
		panel.add(playButton);
		panel.add(playlistButton);
		panel.add(addButton);
		
		frame.add(panel);
		frame.setSize(screenWidth, screenHeight);
		//frame.setLocation(0,0);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//CarStereo.device.setFullScreenWindow(frame); // full screen
		//CarStereo.device.setFullScreenWindow(null); // windowed
		
		backButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				//System.exit(0);
				frame.dispose();
				CarStereo.device.setFullScreenWindow(mainScreen.frame);
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			
		});
		
		playButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() >= 0){
					// check if file or folder
					
					if (fileTypeList.get(list.getSelectedIndex()) == 0){
						// if selected is file...
						if (CarStereo.mediaPlaylist == null){
							CarStereo.mediaPlaylist = new ArrayList<File>();
						}
						
						if (CarStereo.mp3PlayerThread == null || !CarStereo.mp3PlayerThread.isAlive()){
							// if thread is dead
							startPlayThread(allSubFiles[list.getSelectedIndex()]);
						} else {
							// if thread is alive
							addFileToPlaylist(0, allSubFiles[list.getSelectedIndex()]);
							CarStereo.writer.write("q");
							CarStereo.writer.flush();
						}
					
					} else if (fileTypeList.get(list.getSelectedIndex()) == 1){
						// if selected is folder...
						file = allSubFiles[list.getSelectedIndex()];
						getFileList(file, true);
						list.setListData(fileNameList.toArray());
						list.setSelectedIndex(0);
						playButton.setIcon(new ImageIcon(backButtonIcon));
					} else if (fileTypeList.get(list.getSelectedIndex()) == 2){
						// if selected is back button on top of list
						file = file.getParentFile();
						String fileName = file.getAbsolutePath();
						boolean showBack = true;
						if (fileName.equals(MEDIA_FOLDER_PATH_ABSOLUTE)) showBack = false;
						getFileList(file, showBack);
						list.setListData(fileNameList.toArray());
						list.setSelectedIndex(0);
						if (fileTypeList.get(list.getSelectedIndex()) == 0) playButton.setIcon(new ImageIcon(playButtonIcon));
						else if (fileTypeList.get(list.getSelectedIndex()) == 1) playButton.setIcon(new ImageIcon(folderOpenIcon));
						else if (fileTypeList.get(list.getSelectedIndex()) == 2) playButton.setIcon(new ImageIcon(backButtonIcon));
					}
				}
			}
		});
		
		addButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// add music to quick list
				if (CarStereo.mediaPlaylist == null){
					CarStereo.mediaPlaylist = new ArrayList<File>();
				}
				if (list.getSelectedIndex() >= 0){
					if (fileTypeList.get(list.getSelectedIndex()) == 0){
						if (CarStereo.mp3PlayerThread == null || !CarStereo.mp3PlayerThread.isAlive()){
							startPlayThread(allSubFiles[list.getSelectedIndex()]);
						} else{
							addFileToPlaylist(allSubFiles[list.getSelectedIndex()]);
						}	
					} else if (fileTypeList.get(list.getSelectedIndex()) == 1){
						// if selected is a folder, then add the contents, but not of folders inside it
						File tempFile = allSubFiles[list.getSelectedIndex()];
						File[] tempList = tempFile.listFiles();
						for (int i = 0; i < tempList.length; i++){
							if (tempList[i].isFile()){
								if (CarStereo.mp3PlayerThread == null || !CarStereo.mp3PlayerThread.isAlive()){
									startPlayThread(tempList[i]);
									System.out.println("Playing song...");
								} else {
									addFileToPlaylist(tempList[i]);
									System.out.println("Added song #" + i);
								}
							}
						
						}
					
					}
				}
				
			}
			
		});
		
		playlistButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// show playlist screen
				if (CarStereo.mediaPlaylist != null) {
					if (CarStereo.mediaPlaylist.size() > 0) openPlaylistScreen();
				}
			}
			
		});
		
		list.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				System.out.println("Selected: " + list.getSelectedIndex());
				if (fileTypeList.get(list.getSelectedIndex()) == 0) {
					// file
					playButton.setIcon(new ImageIcon(playButtonIcon));
				} else if (fileTypeList.get(list.getSelectedIndex()) == 1) {
					// folder
					playButton.setIcon(new ImageIcon(folderOpenIcon));
				} else if (fileTypeList.get(list.getSelectedIndex()) == 2) {
					// back button on top of list
					playButton.setIcon(new ImageIcon(backButtonIcon));
				}
			}
		});
	}
	
	void getFileList(File file, boolean showBackOption){
		
		//file=new File(MEDIA_FOLDER_PATH);
		allSubFiles=file.listFiles();
		
		fileNameList = new ArrayList();
		fileTypeList = new ArrayList();
		
		if (showBackOption){
			fileNameList.add("  <<  LAST FOLDER  <<");
	        fileTypeList.add(2);
	        File[] temp = allSubFiles.clone();
	        
	        allSubFiles = new File[allSubFiles.length + 1];
	        allSubFiles[0] = null;
	        for (int i = 0; i < temp.length; i++){
	        	allSubFiles[i+1] = temp[i];
	        }
		}
		
		for (File files : allSubFiles) {
			if (files != null){
				if (files.isDirectory()){
					//System.out.println(files.getName()+" is directory");
					fileNameList.add("(FOLDER) "+files.getName());
					fileTypeList.add(1);
				} else {
					//System.out.println(files.getName()+" is file");
					fileNameList.add(""+files.getName());
					fileTypeList.add(0);
				}
			}
		    
		}
	}
	
	void playFile(){
		//playFile = allSubFiles[list.getSelectedIndex()].getName();
		//playFile = "'" + playFile + "'";
		//String[] command = {"/bin/sh", "-c", "omxplayer -o local --no-osd ~/Media/"+playFile};
		String[] command = {"/bin/sh", "-c", "omxplayer -o local --no-osd "+playFile};
		try {
			//CarStereo.killAllProcesses();
			CarStereo.mp3Process = Runtime.getRuntime().exec(command);
			CarStereo.stream = CarStereo.mp3Process.getOutputStream();
			CarStereo.writer = new PrintWriter(CarStereo.stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void startPlayThread(final File tempFile){
		CarStereo.killAllProcesses();
		CarStereo.playMode = 2;
		CarStereo.mp3IsPlaying = true;
		
		System.out.println("CarStereo.mp3PlayerThread is NULL");
		CarStereo.mp3PlayerThread = new Thread(){
			public void run(){
				System.out.println("Add to list...");
				addFileToPlaylist(tempFile);
				while(CarStereo.mediaPlaylist.size() > 0 && CarStereo.mp3IsPlaying){
					//playFile = allSubFiles[list.getSelectedIndex()].getAbsolutePath();
					//addFileToPlaylist(allSubFiles[list.getSelectedIndex()]);
					System.out.println(" Playlist size = " + CarStereo.mediaPlaylist.size());
					System.out.println("Play Thread Started");
					playFile = CarStereo.mediaPlaylist.get(0).getAbsolutePath();
					playFile = "'" + playFile + "'";
					playFile();
					getFileInfo();
					CarStereo.mediaPlaylist.remove(0);
					try {
						CarStereo.mp3Process.waitFor();
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
					
				}
				
			}
		};
		CarStereo.mp3PlayerThread.start();
	}
	
	void addFileToPlaylist(File file){
		CarStereo.mediaPlaylist.add(file);
		System.out.println(CarStereo.mediaPlaylist.size() + " - " + CarStereo.mediaPlaylist.get(0).getName());
	}
	
	void addFileToPlaylist(int position, File file){
		CarStereo.mediaPlaylist.add(position, file);
		System.out.println(CarStereo.mediaPlaylist.size() + " - " + CarStereo.mediaPlaylist.get(0).getName());
	}
	
	void getFileInfo(){
		Thread infoThread = new Thread(){
			public void run(){
				Process infoProcess;
				try {
					//String[] infoCommand = {"/bin/sh", "-c", "ffprobe -loglevel error -show_entries format_tags=artist -of default=noprint_wrappers=1:nokey=1 ~/Media/" + playFile};
					String[] infoCommand = {"/bin/sh", "-c", "ffprobe -loglevel error -show_entries format_tags=artist -of default=noprint_wrappers=1:nokey=1 " + playFile};
					infoProcess = Runtime.getRuntime().exec(infoCommand);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(infoProcess.getInputStream()));
					CarStereo.infoText2 = br.readLine();
					infoProcess.waitFor();
					infoProcess.destroy();
					//infoCommand = new String[]{"/bin/sh", "-c", "ffprobe -loglevel error -show_entries format_tags=title -of default=noprint_wrappers=1:nokey=1 ~/Media/" + playFile};
					infoCommand = new String[]{"/bin/sh", "-c", "ffprobe -loglevel error -show_entries format_tags=title -of default=noprint_wrappers=1:nokey=1 " + playFile};
					infoProcess = Runtime.getRuntime().exec(infoCommand);
					br = new BufferedReader(
							new InputStreamReader(infoProcess.getInputStream()));
					CarStereo.infoText1 = br.readLine();
					infoProcess.waitFor();
					infoProcess.destroy();
					//	set info texts
					mainScreen.labelInfo1.setText(CarStereo.infoText1);
					mainScreen.labelInfo2.setText(CarStereo.infoText2);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		infoThread.start();
	}
	
	void openPlaylistScreen(){
		new PlaylistScreen(this);
	}
}
