package org.swampsoft.carstereo.screens;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.swampsoft.carstereo.CarStereo;

public class PlaylistScreen {
	int screenWidth;
	int screenHeight;

	JFrame frame;
	JPanel panel;
	JScrollPane scrollPane;
	JLabel labelTitle;
	
	JList list;
	
	List playlistCopy;
	ArrayList<String> listNames;
	FileEntry fileEntry[];
	
	JButton backButton;
	JButton minusButton;
	JButton shuffleButton;
	JButton clearAllButton;
	JButton moveUpButton;
	JButton moveDownButton;
	
	BufferedImage backButtonImage;
	BufferedImage minusButtonImage;
	BufferedImage shuffleButtonImage;
	BufferedImage clearAllButtonImage;
	BufferedImage upButtonImage;
	BufferedImage downButtonImage;
	
	MP3Screen mp3Screen;
	
	public PlaylistScreen(final MP3Screen mp3Screen){
		this.mp3Screen = mp3Screen;
		screenWidth = CarStereo.device.getDisplayMode().getWidth();
		screenHeight = CarStereo.device.getDisplayMode().getHeight();
		
		Font font = new Font("TimesRoman", Font.PLAIN, 30);
		Font fontBig = new Font("TimesRoman", Font.PLAIN, 40);
		
		frame = new JFrame("Playlist Screen");
		frame.setBackground(Color.BLACK);

		panel = new JPanel();
		//panel.setLayout(new FlowLayout());
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		labelTitle = new JLabel("Next to Play:");
		labelTitle.setForeground(Color.WHITE);
		labelTitle.setFont(fontBig);
		labelTitle.setSize(350, 45);
		labelTitle.setHorizontalAlignment(JLabel.CENTER);
		labelTitle.setBounds(screenWidth/2-labelTitle.getWidth()/2, screenHeight/11, labelTitle.getWidth(), labelTitle.getHeight());

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
			upButtonImage = ImageIO.read(getClass().getResource("/images/up-arrow-smaller.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		moveUpButton = new JButton(new ImageIcon(upButtonImage));
		moveUpButton.setBackground(Color.BLACK);
		//moveUpButton.setForeground(Color.WHITE);
		moveUpButton.setBorder(BorderFactory.createEmptyBorder());
		moveUpButton.setSize(100, 100);
		moveUpButton.setBounds(screenWidth-100-moveUpButton.getWidth()/2, screenHeight/6*1-moveUpButton.getHeight()/2, moveUpButton.getWidth(), moveUpButton.getHeight());
		
		try {
			downButtonImage = ImageIO.read(getClass().getResource("/images/down-arrow-smaller.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		moveDownButton = new JButton(new ImageIcon(downButtonImage));
		moveDownButton.setBackground(Color.BLACK);
		//moveDownButton.setForeground(Color.WHITE);
		moveDownButton.setBorder(BorderFactory.createEmptyBorder());
		moveDownButton.setSize(100, 100);
		moveDownButton.setBounds(screenWidth-100-moveDownButton.getWidth()/2, screenHeight/6*2-moveDownButton.getHeight()/2, moveDownButton.getWidth(), moveDownButton.getHeight());
		
		try {
			minusButtonImage = ImageIO.read(getClass().getResource("/images/minus-smaller.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		minusButton = new JButton(new ImageIcon(minusButtonImage));
		minusButton.setBackground(Color.BLACK);
		minusButton.setSize(100, 100);
		minusButton.setBorder(BorderFactory.createEmptyBorder());
		//minusButton.setBounds(screenWidth-100-minusButton.getWidth()/2, screenHeight/2-minusButton.getHeight()/2, minusButton.getWidth(), minusButton.getHeight());
		minusButton.setBounds(screenWidth-100-minusButton.getWidth()/2, screenHeight/6*3-minusButton.getHeight()/2, minusButton.getWidth(), minusButton.getHeight());
		
		try {
			shuffleButtonImage = ImageIO.read(getClass().getResource("/images/shuffle-smaller.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		shuffleButton = new JButton(new ImageIcon(shuffleButtonImage));
		shuffleButton.setBackground(Color.BLACK);
		shuffleButton.setSize(100, 100);
		shuffleButton.setBorder(BorderFactory.createEmptyBorder());
		//shuffleButton.setBounds(screenWidth-100-shuffleButton.getWidth()/2, screenHeight/4-shuffleButton.getHeight()/2, shuffleButton.getWidth(), shuffleButton.getHeight());
		shuffleButton.setBounds(screenWidth-100-shuffleButton.getWidth()/2, screenHeight/6*4-shuffleButton.getHeight()/2, shuffleButton.getWidth(), shuffleButton.getHeight());

		try {
			clearAllButtonImage = ImageIO.read(getClass().getResource("/images/cancel-smaller.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		clearAllButton = new JButton(new ImageIcon(clearAllButtonImage));
		clearAllButton.setBackground(Color.BLACK);
		clearAllButton.setSize(100, 100);
		clearAllButton.setBorder(BorderFactory.createEmptyBorder());
		//clearAllButton.setBounds(screenWidth-100-clearAllButton.getWidth()/2, screenHeight/2+clearAllButton.getHeight()-clearAllButton.getHeight()/4, clearAllButton.getWidth(), clearAllButton.getHeight());
		clearAllButton.setBounds(screenWidth-100-clearAllButton.getWidth()/2, screenHeight/6*5-clearAllButton.getHeight()/2, clearAllButton.getWidth(), clearAllButton.getHeight());
		
		playlistCopy = new ArrayList(CarStereo.mediaPlaylist);
		
		//list = new JList(CarStereo.mediaPlaylist.toArray());
		list = new JList();
		list.setCellRenderer(new CustomCellRenderer());
		list.setBackground(Color.BLACK);
		list.setForeground(Color.WHITE);
		list.setSelectionBackground(Color.WHITE);
		list.setSelectionForeground(Color.BLACK);
		list.setFont(font);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(7);
		
		refreshList();
		
		scrollPane = new JScrollPane(list);
		scrollPane.setSize(screenWidth-250, screenHeight - 226);
		scrollPane.setBounds( 50, 100, scrollPane.getWidth(), scrollPane.getHeight());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(40,0));
		scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		scrollPane.getViewport().setViewPosition(new Point(0, 0));
		scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
		//scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createLineBorder(Color.WHITE));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
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
		
		panel.add(backButton);
		panel.add(labelTitle);
		panel.add(scrollPane);
		panel.add(minusButton);
		panel.add(shuffleButton);
		panel.add(clearAllButton);
		panel.add(moveUpButton);
		panel.add(moveDownButton);
		
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
				CarStereo.device.setFullScreenWindow(mp3Screen.frame);
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			
		});
		
		moveUpButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() >= 1){// equal or greater than 1 so can't move the first (0)
					int selected = list.getSelectedIndex();
					Collections.swap(CarStereo.mediaPlaylist, list.getSelectedIndex(), list.getSelectedIndex() - 1);
					refreshList();
					list.setSelectedIndex(selected - 1);
				}
			}
		});
		
		moveDownButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() >= 0 && list.getSelectedIndex() < CarStereo.mediaPlaylist.size()){
					int selected = list.getSelectedIndex();
					Collections.swap(CarStereo.mediaPlaylist, list.getSelectedIndex(), list.getSelectedIndex() + 1);
					refreshList();
					list.setSelectedIndex(selected + 1);
				}
			}
		});
		
		minusButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() >= 0){
					CarStereo.mediaPlaylist.remove(list.getSelectedIndex());
					//list.setListData(CarStereo.mediaPlaylist.toArray());
					refreshList();
				}
			}
		});
		
		shuffleButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				playlistCopy = new ArrayList(CarStereo.mediaPlaylist);
				Collections.shuffle(playlistCopy);
				CarStereo.mediaPlaylist = new ArrayList(playlistCopy);
				//list.setListData(CarStereo.mediaPlaylist.toArray());
				refreshList();
				playlistCopy = null;
			}
		});
		
		clearAllButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				CarStereo.mediaPlaylist.clear();
				refreshList();
			}
		});
	}
	
	void refreshList(){
		listNames = new ArrayList();
		fileEntry = new FileEntry[CarStereo.mediaPlaylist.size()];
		
		for (int i = 0; i < CarStereo.mediaPlaylist.size(); i++){
			listNames.add(CarStereo.mediaPlaylist.get(i).getName());
			
			String path = "/images/music-tiny.png";
			String extension = CarStereo.mediaPlaylist.get(i).getName().substring(CarStereo.mediaPlaylist.get(i).getName().lastIndexOf(".")+1);
			System.out.println("File extension: " + extension);
			if (extension.toLowerCase().equals("mp4") || extension.toLowerCase().equals("avi") || extension.toLowerCase().equals("mov") || 
					extension.toLowerCase().equals("mkv") || extension.toLowerCase().equals("mov") || extension.toLowerCase().equals("ogv") || 
					extension.toLowerCase().equals("mpeg") || extension.toLowerCase().equals("wmv") || extension.toLowerCase().equals("webm")){
				path = "/images/video-tiny.png";
			} else if (extension.toLowerCase().equals("m3u")){
				path = "/images/playlist-tiny.png";
			}
			
			fileEntry[i] = new FileEntry(CarStereo.mediaPlaylist.get(i).getName(), path);
		}
		
		//list.setListData(listNames.toArray());
		list.setListData(fileEntry);
	}
}
