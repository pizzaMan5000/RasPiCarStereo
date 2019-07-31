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
import java.util.Random;

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
	
	JButton backButton;
	JButton minusButton;
	JButton shuffleButton;
	
	BufferedImage backButtonImage;
	BufferedImage minusButtonImage;
	BufferedImage shuffleButtonImage;
	
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
		labelTitle.setBounds(screenWidth/2-labelTitle.getWidth()/2, screenHeight/10, labelTitle.getWidth(), labelTitle.getHeight());
		
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
			minusButtonImage = ImageIO.read(getClass().getResource("/images/minus-small.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		minusButton = new JButton(new ImageIcon(minusButtonImage));
		minusButton.setBackground(Color.BLACK);
		minusButton.setSize(100, 100);
		minusButton.setBorder(BorderFactory.createEmptyBorder());
		minusButton.setBounds(screenWidth-100-minusButton.getWidth()/2, screenHeight/2-minusButton.getHeight()/2, minusButton.getWidth(), minusButton.getHeight());
		
		try {
			shuffleButtonImage = ImageIO.read(getClass().getResource("/images/shuffle-small.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		shuffleButton = new JButton(new ImageIcon(shuffleButtonImage));
		shuffleButton.setBackground(Color.BLACK);
		shuffleButton.setSize(100, 100);
		shuffleButton.setBorder(BorderFactory.createEmptyBorder());
		shuffleButton.setBounds(screenWidth-100-shuffleButton.getWidth()/2, screenHeight/4-shuffleButton.getHeight()/2, shuffleButton.getWidth(), shuffleButton.getHeight());

		playlistCopy = new ArrayList(CarStereo.mediaPlaylist);
		
		//list = new JList(CarStereo.mediaPlaylist.toArray());
		list = new JList();
		list.setBackground(Color.BLACK);
		list.setForeground(Color.WHITE);
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
		scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		panel.add(backButton);
		panel.add(labelTitle);
		panel.add(scrollPane);
		panel.add(minusButton);
		panel.add(shuffleButton);
		
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
	}
	
	void refreshList(){
		listNames = new ArrayList();
		for (int i = 0; i < CarStereo.mediaPlaylist.size(); i++){
			listNames.add(CarStereo.mediaPlaylist.get(i).getName());
		}
		list.setListData(listNames.toArray());
	}
}
