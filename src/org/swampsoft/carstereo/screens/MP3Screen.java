package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicScrollBarUI;

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
	BufferedImage upOneLevelIcon;
	
	String MEDIA_FOLDER_PATH;
	String MEDIA_FOLDER_PATH_ABSOLUTE;
	File file;
	File[] allSubFiles;
	FileEntry fileEntry[];
	String playFile;
	ArrayList<String> fileNameList;
	ArrayList<Integer> fileTypeList;
	
	File tempFile;
	String tempString;
	String fileExtension;
	
	long listClickTime = 0;
	Point lastClickedPoint = new Point();
	
	final MainScreen mainScreen;
	
	public MP3Screen(final MainScreen mainScreen){
		this.mainScreen = mainScreen;
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		Font font = new Font("TimesRoman", Font.PLAIN, 30);
		
		MEDIA_FOLDER_PATH=System.getProperty("user.home")+"/Media";
		file=new File(MEDIA_FOLDER_PATH);
		MEDIA_FOLDER_PATH_ABSOLUTE = file.getAbsolutePath();
		getFileList(file, false); // GET FILE LIST
		
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
		
		try {
			upOneLevelIcon = ImageIO.read(getClass().getResource("/images/up-arrow-small.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//list = new JList(fileNameList.toArray());
		list = new JList(fileEntry);
		list.setCellRenderer(new CustomCellRenderer());
		list.setBackground(Color.BLACK);
		list.setForeground(Color.WHITE);
		//list.setSelectionBackground(Color.WHITE);
		//list.setSelectionForeground(Color.BLACK);
		//list.setFont(font);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(7);
		//list.setSize(screenWidth-250, screenHeight - 226);
		//list.setBounds(50,100,list.getWidth(), list.getHeight());
		list.setSelectedIndex(0);
		
		if (fileTypeList.get(list.getSelectedIndex()) == 0) playButton.setIcon(new ImageIcon(playButtonIcon));
		else if (fileTypeList.get(list.getSelectedIndex()) == 1) playButton.setIcon(new ImageIcon(folderOpenIcon));
		else if (fileTypeList.get(list.getSelectedIndex()) == 2) playButton.setIcon(new ImageIcon(upOneLevelIcon));
		
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
		scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
					this.thumbColor = Color.WHITE;
			}
			@Override
	        protected JButton createDecreaseButton(int orientation) {
	            return createHiddenButton();
	        }

	        @Override    
	        protected JButton createIncreaseButton(int orientation) {
	            return createHiddenButton();
	        }
	        
	        private JButton createHiddenButton() {
	            JButton jbutton = new JButton();
	            jbutton.setPreferredSize(new Dimension(0, 0));
	            jbutton.setMinimumSize(new Dimension(0, 0));
	            jbutton.setMaximumSize(new Dimension(0, 0));
	            return jbutton;
	        }
		});
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
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
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
				playPressed();
			}
		});
		
		addButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				addButtonPressed();
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
					// up one level
					playButton.setIcon(new ImageIcon(upOneLevelIcon));
				}
				if (System.currentTimeMillis() - listClickTime < 900){
					if (Math.abs(e.getX() - lastClickedPoint.getX()) < 50 && Math.abs(e.getY() - lastClickedPoint.getY()) < 50){
						// list was double clicked
						System.out.println("Double Clicked: " + list.getSelectedIndex());
						if (fileTypeList.get(list.getSelectedIndex()) == 1) {
							openSelectedFolder();
						} else if (fileTypeList.get(list.getSelectedIndex()) == 2) {
							openFolderOneLevelUp();
						} else {
							addButtonPressed();
						}
					}
				}
				lastClickedPoint.setLocation(e.getX(), e.getY());
				listClickTime = System.currentTimeMillis();
			}
		});
	}
	
	void getFileList(File file, boolean showBackOption){
		
		//file=new File(MEDIA_FOLDER_PATH);
		allSubFiles=file.listFiles();
		int counter = 0;
		
		fileNameList = new ArrayList();
		fileTypeList = new ArrayList();
		
		// new code
		if (!showBackOption){
			fileEntry = new FileEntry[allSubFiles.length];
		} else {
			fileEntry = new FileEntry[allSubFiles.length + 1];
		}
		
		
		if (showBackOption){
			fileNameList.add("  <<  LAST FOLDER  <<");
	        fileTypeList.add(2);
	        File[] temp = allSubFiles.clone();
	        
	        allSubFiles = new File[allSubFiles.length + 1];
	        allSubFiles[0] = null;
	        
	        for (int i = 0; i < temp.length; i++){
	        	allSubFiles[i+1] = temp[i];
	        }
	        
	        // new code:
	        fileEntry[0] = new FileEntry("UP ONE FOLDER", "/images/up-arrow-tiny.png");
	        counter++;
		}
		
		for (File files : allSubFiles) {
			if (files != null){
				if (files.isDirectory()){
					//System.out.println(files.getName()+" is directory");
					fileNameList.add("(FOLDER) "+files.getName());
					fileTypeList.add(1);
					
					fileEntry[counter] = new FileEntry(files.getName(), "/images/folder-open-tiny.png");
					counter++;
				} else {
					//System.out.println(files.getName()+" is file");
					fileNameList.add(""+files.getName());
					fileTypeList.add(0);
					
					// set image to music, then check if its music and change it if its not
					String path = "/images/music-tiny.png"; 
					
					String extension = files.getName().substring(files.getName().lastIndexOf(".")+1);
					//System.out.println("Get File:" + extension);
					if (extension.toLowerCase().equals("mp4") || extension.toLowerCase().equals("avi") || extension.toLowerCase().equals("mov") || 
							extension.toLowerCase().equals("mkv") || extension.toLowerCase().equals("mov") || extension.toLowerCase().equals("ogv") || 
							extension.toLowerCase().equals("mpeg") || extension.toLowerCase().equals("wmv") || extension.toLowerCase().equals("webm")){
						path = "/images/video-tiny.png";
					} else if (extension.toLowerCase().equals("m3u")){
						path = "/images/playlist-tiny.png";
					}
					
					
					fileEntry[counter] = new FileEntry(files.getName(), path);
					counter++;
				}
			}
		    
		}
	}
	
	void playFile(){
		String[] command = {"/bin/sh", "-c", "omxplayer -o local --no-osd --win " + 0 + "," + 10 + "," + (screenWidth-50) + 
				"," + (screenHeight-20) + " "+playFile};
		
		try {
			CarStereo.mp3Process = Runtime.getRuntime().exec(command);
			CarStereo.stream = CarStereo.mp3Process.getOutputStream();
			CarStereo.writer = new OutputStreamWriter(CarStereo.stream); // , "UTF-8"
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void startPlayThread(final File tempFile){
		CarStereo.killAllProcesses();
		CarStereo.playMode = 2;
		CarStereo.mp3IsPlaying = true;
		
		System.out.println("CarStereo.mp3PlayerThread is NULL");
		
		final MP3Screen tempScreen = this; // this is so the mp3 thread can change things in the main thread 
		
		CarStereo.mp3PlayerThread = new Thread(){
			public void run(){
				System.out.println("Add to list...");
				if (tempFile != null) addFileToPlaylist(tempFile);
				
				while(CarStereo.mediaPlaylist.size() > 0 && CarStereo.mp3IsPlaying){
					//this LOOP keeps playing music until the playlist is empty
					//System.out.println(" Playlist size = " + CarStereo.mediaPlaylist.size());
					System.out.println("Play Thread Started");
					playFile = CarStereo.mediaPlaylist.get(0).getAbsolutePath();
					
					boolean isVideo = false;
					
					if (playFile.contains(".")) {
						fileExtension = playFile.substring(playFile.lastIndexOf(".")+1);
						if (fileExtension.toLowerCase().equals("mp4")){
							isVideo = true;
							CarStereo.fullScreenControls = new FullScreenControls(tempScreen, mainScreen);
						} else if (fileExtension.toLowerCase().equals("avi")){
							isVideo = true;
							CarStereo.fullScreenControls = new FullScreenControls(tempScreen, mainScreen);
						} else if (fileExtension.toLowerCase().equals("webm")){
							isVideo = true;
							CarStereo.fullScreenControls = new FullScreenControls(tempScreen, mainScreen);
						} else if (fileExtension.toLowerCase().equals("ogv")){
							isVideo = true;
							CarStereo.fullScreenControls = new FullScreenControls(tempScreen, mainScreen);
						} else if (fileExtension.toLowerCase().equals("wmv")){
							isVideo = true;
							CarStereo.fullScreenControls = new FullScreenControls(tempScreen, mainScreen);
						} else if (fileExtension.toLowerCase().equals("mov")){
							isVideo = true;
							CarStereo.fullScreenControls = new FullScreenControls(tempScreen, mainScreen);
						} else if (fileExtension.toLowerCase().equals("mkv")){
							isVideo = true;
							CarStereo.fullScreenControls = new FullScreenControls(tempScreen, mainScreen);
						} else if (fileExtension.toLowerCase().equals("mpeg")){
							isVideo = true;
							CarStereo.fullScreenControls = new FullScreenControls(tempScreen, mainScreen);
						}
					} else {
						CarStereo.fullScreenControls.closeScreen();
						CarStereo.fullScreenControls = null;
					}
					
					System.out.println("Playing: " + playFile);
					playFile = "'" + playFile + "'";
					playFile();
					
					if (!isVideo){
						getFileInfo();
					}
					CarStereo.mediaPlaylist.remove(0);
					
					// wait for song/video to finish playing and then start loop over again
					try {
						CarStereo.mp3Process.waitFor();
						// remove full screen if its there
						if (CarStereo.fullScreenControls != null){
							CarStereo.fullScreenControls.closeScreen();
							CarStereo.fullScreenControls = null;
						}
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
				} // end while loop
				
			}
		};
		CarStereo.mp3PlayerThread.start();
	}
	
	void addFileToPlaylist(File file){
		CarStereo.mediaPlaylist.add(file);
		//System.out.println(CarStereo.mediaPlaylist.size() + " - " + CarStereo.mediaPlaylist.get(0).getName());
	}
	
	void addFileToPlaylist(int position, File file){
		CarStereo.mediaPlaylist.add(position, file);
		//System.out.println(CarStereo.mediaPlaylist.size() + " - " + CarStereo.mediaPlaylist.get(0).getName());
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
					if (CarStereo.infoText1 != null){
						mainScreen.labelInfo1.setText(CarStereo.infoText1);
					} else {
						mainScreen.labelInfo1.setText(playFile.substring(playFile.lastIndexOf("/")+1, playFile.lastIndexOf(".")-1));
					}
					if (CarStereo.infoText2 != null){
						mainScreen.labelInfo2.setText(CarStereo.infoText2);
					} else {
						mainScreen.labelInfo2.setText(playFile.substring(playFile.lastIndexOf(".")+1));
					}
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
	
	void addM3UtoPlaylist(File file, boolean addToStartOfPlaylist){
		BufferedReader tempBR;
    	String st;
    	String stAltered;
    	int index = 0;
    	
    	try {
    		tempBR = new BufferedReader(new FileReader(file));
			while ((st = tempBR.readLine()) != null) {
				// get file name
				if (!st.startsWith("#")) {
					stAltered = st.replace("\\", "/");
					tempString = file.getParent() + "/" + st;
					tempFile = new File(tempString);
					
					System.out.println("File: " + tempString + " - Exists: " + tempFile.exists());
					
					if (addToStartOfPlaylist){
						addFileToPlaylist(index, tempFile);
					} else {
						addFileToPlaylist(tempFile);
					}
					index++;
				}
			}
			tempBR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void playPressed(){
		if (list.getSelectedIndex() >= 0){
			// check if file or folder
			
			if (fileTypeList.get(list.getSelectedIndex()) == 0){
				// if selected is file...
				if (CarStereo.mediaPlaylist == null){
					CarStereo.mediaPlaylist = new ArrayList<File>();
				}
				
				if (CarStereo.mp3PlayerThread == null || !CarStereo.mp3PlayerThread.isAlive()){
					// if thread is dead
					if (allSubFiles[list.getSelectedIndex()].getName().endsWith(".m3u")){
						// play playlist file
						addM3UtoPlaylist(allSubFiles[list.getSelectedIndex()], true);
						startPlayThread(null);
					} else {
						// play everything else
						startPlayThread(allSubFiles[list.getSelectedIndex()]);
					}
				} else {
					// if thread is alive
					if (allSubFiles[list.getSelectedIndex()].getName().endsWith(".m3u")){
						// play playlist file - add songs to start of playlist 
						addM3UtoPlaylist(allSubFiles[list.getSelectedIndex()], true);
						try {
							CarStereo.writer.write("q");
							CarStereo.writer.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					} else {
						// play everything else
						addFileToPlaylist(0, allSubFiles[list.getSelectedIndex()]);
						try {
							CarStereo.writer.write("q");
							CarStereo.writer.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			
			} else if (fileTypeList.get(list.getSelectedIndex()) == 1){
				// if selected is folder...
				openSelectedFolder();
			} else if (fileTypeList.get(list.getSelectedIndex()) == 2){
				// if selected is back button on top of list ("up one level")
				openFolderOneLevelUp();
			}
		}
	}
	
	void openSelectedFolder(){
		// if selected is folder...
		file = allSubFiles[list.getSelectedIndex()];
		getFileList(file, true);
		//list.setListData(fileNameList.toArray());
		list.setListData(fileEntry);
		list.setSelectedIndex(0);
		playButton.setIcon(new ImageIcon(upOneLevelIcon));
	}
	
	void openFolderOneLevelUp(){
		// if selected is back button on top of list ("up one level")
		file = file.getParentFile();
		String fileName = file.getAbsolutePath();
		boolean showBack = true;
		if (fileName.equals(MEDIA_FOLDER_PATH_ABSOLUTE)) showBack = false;
		getFileList(file, showBack);
		//list.setListData(fileNameList.toArray());
		list.setListData(fileEntry);
		list.setSelectedIndex(0);
		if (fileTypeList.get(list.getSelectedIndex()) == 0) playButton.setIcon(new ImageIcon(playButtonIcon));
		else if (fileTypeList.get(list.getSelectedIndex()) == 1) playButton.setIcon(new ImageIcon(folderOpenIcon));
		else if (fileTypeList.get(list.getSelectedIndex()) == 2) playButton.setIcon(new ImageIcon(upOneLevelIcon));
	}
	
	void addButtonPressed(){
		// add music to quick list
		if (CarStereo.mediaPlaylist == null){
			CarStereo.mediaPlaylist = new ArrayList<File>();
		}
		if (list.getSelectedIndex() >= 0){
			if (fileTypeList.get(list.getSelectedIndex()) == 0){
				if (CarStereo.mp3PlayerThread == null || !CarStereo.mp3PlayerThread.isAlive()){
					// if mp3 thread is not playing
					if (allSubFiles[list.getSelectedIndex()].getName().endsWith("m3u")){
						addM3UtoPlaylist(allSubFiles[list.getSelectedIndex()], false);
						startPlayThread(null);
					} else {
						startPlayThread(allSubFiles[list.getSelectedIndex()]);
					}
				} else{
					// if mp3 thread is playing
					if (allSubFiles[list.getSelectedIndex()].getName().endsWith("m3u")){
						addM3UtoPlaylist(allSubFiles[list.getSelectedIndex()], false);
					} else {
						addFileToPlaylist(allSubFiles[list.getSelectedIndex()]);
					}
				}	
			} else if (fileTypeList.get(list.getSelectedIndex()) == 1){
				// if selected is a folder, then add the contents, but not of folders inside it
				File tempFile = allSubFiles[list.getSelectedIndex()];
				File[] tempList = tempFile.listFiles();
				for (int i = 0; i < tempList.length; i++){
					if (tempList[i].isFile() && !tempList[i].getName().endsWith("m3u")){
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
}

class CustomCellRenderer extends JLabel implements ListCellRenderer {

	  public CustomCellRenderer() {
	    setOpaque(true);
	    setIconTextGap(20);
	    setFont(new Font("TimesRoman", Font.PLAIN, 30));
	  }

	  public Component getListCellRendererComponent(JList list, Object value,
	      int index, boolean isSelected, boolean cellHasFocus) {
	    FileEntry entry = (FileEntry) value;
	    setText(entry.getTitle());
	    setIcon(entry.getImage());
	    if (isSelected) {
	      setBackground(Color.white);
	      setForeground(Color.black);
	    } else {
	      setBackground(Color.black);
	      setForeground(Color.white);
	    }
	    return this;
	  }
}

class FileEntry {
	  private final String title;

	  private final String imagePath;

	  private ImageIcon image;

	  public FileEntry(String title, String imagePath) {
	    this.title = title;
	    this.imagePath = imagePath;
	  }

	  public String getTitle() {
	    return title;
	  }

	  public ImageIcon getImage() {
	    if (image == null) {
	      //image = new ImageIcon(imagePath);
	    	image = new ImageIcon(Image.class.getResource(imagePath));
	    }
	    return image;
	  }

	  // Override standard toString method to give a useful result
	  public String toString() {
	    return title;
	  }
}
