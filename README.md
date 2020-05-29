# RasPiCarStereo
RasPiCarStereo is an app that turns a Raspberry Pi 2 or 3 (maybe 1 too) into a car stereo complete with touchscreen, GPS navigation, bluetooth, MP3 player, FM radio and access to the vehicle's computer.

It's still being worked on, but most everything works. The bluetooth stuff could be done better. Some bluetooth stuff is broken, but it should be able to pair a new device. It can connect to devices that have already been paired fine.

For more info, screenshots and instructions:
http://www.swampsoft.org/raspberrypi/RasPiCarStereo/RasPiCarStereo.php

To use the stereo should be pretty straight forward, mostly. To change a radio station preset thats already set, press and hold for a second then let go. If there is no station set yet, just a regular tap will do. The MP3 player is set up like a juke box. To play a song, find it on the list and press the play button. To add a song to the play list, select the song on the list and press the add button. If there is no song playing, the add button will play the song instead. Currently playing songs are not listed on the playlist. To view the playlist, press the playlist button. You can also shuffle the list and remove songs here too. If there is no songs on the list, the playlist button wont do anything. In the playlist, if the play button is pressed while a song is playing, it will stop the song and play the new one while keeping the playlist if there is one. I plan on adding support for M3U playlists so you can add or play a list the same way you would an individual file.

To make a RasPiCarStereo, you need a Raspberry Pi of course. I made this for a Raspberry Pi 3 but it should work for 2 as well. The Raspberry Pi 1 probably wont be fast enough for what we want. Think of the load times everytime you start your car, faster is better. Install Raspbian Desktop edition, not the lite edition. Also set up any bluetooth devices you will be using in your car while your working on it to make things easier, not that it can't be done after its installed, its just a pain right now. To install the app, copy RasPiCarStereo.jar to the home folder (/home/pi/) and make a folder in the home folder called "Media" with a capitol "M". That's where all the media should go. You will have to install and setup more software listed below. RasPiCarStereo will also need to start up automatically on boot without the rest of the GUI apps to save time and resources.

Hardware you will need:

 - Touchscreen - 5 inch - can be found for $30 online - plugs directly into GPIO
 - Amplifier - can be found online for $15-20 (small board, not like the kind for subwoofers)
 - GPS reciever - usb can be online for $20 - get one with long wire, not a little dongle thing
 - RTL-SDR reciever - usb FM radio - found online for $20 (search for RTL2832U or R820T) - no AM, i think
 - USB drive - for storing MP3s and stuff
 - ScanTool OBDLink - usb connection to car's OBD2 computer - found online for $30 - needed to look at engine info
 - mini sdcard for raspbery pi - about 8GB or more
 - usb hub to make things easier to assemble
 - WIFI usb dongle $10 - even if you have a Raspberry Pi 3 which comes with wifi, built in one has many problems, disable it
 - Bluetooth dongle if you dont have the raspberry pi 3
 - USB car cigarette lighter adapter to convert the 12V down to 5V. 2 ports, 2.4A each port
 
You will also need to install the following software:
 
 - rtl-sdr for the FM radio decoder - (sudo apt-get install rtl-sdr)
 - navit gps navigator - free gps app that uses free maps from OpenStreetMap.org - (sudo apt-get install navit)
 - redsea to decode FM radio text - (https://github.com/windytan/redsea)
 - scantool app to connect to car's computer (sudo apt-get install scantool)
 - install pulse audio if its not already installed - (sudo apt-get install pulseaudio)
 - GPSd to read the GPS data - (sudo apt-get install gpsd)
 - NTPd to set the time from GPS data  (sudo apt-get install ntp)
 - there could be more, this is what I can remember

You will need to setup a few things after all the hardware and software is installed:

 - NTPd that gets its time from the GPS data - https://blog.webernetz.net/ntp-server-via-gps-on-a-raspberry-pi/
 - raspberry pi as bluetooth speaker - https://www.instructables.com/id/Turn-your-Raspberry-Pi-into-a-Portable-Bluetooth-A/
 - make filesystem read-only so it can be powered off without proper shutdown - https://hallard.me/raspberry-pi-read-only/
 - setup navi app with map(s) and set it up for fullscreen
 - setup scantool for fullscreen
 
Things I'd like to do:

 - finish bluetooth stuff - it hurts my head
 - make buttons/text change colors so it can match car's interior lights
 - better MP3 player
 - improved FM quality - currently seems to have noise, I've been testing indoors though, don't know if that matters
 - bluetooth audio is choppy - could be hardware issue, raspberry pi 3's bluetooth/wifi sucks. I turned off wifi to improve
 - custom OBD2 reader
 - redsea app only gets the FM station name sometimes and doesn't seem to get song info either. I want more info!
 
I will put more info up on the actual wiring done to connect it to the car when i actually do it. The plan is to hook up the  raspberry pi to the switched-power and the amplifier will be powered through the constant power, but switched on/off with a relay connected to the switched-power so it is also switched, unless the switched power can handle all the devices together. The amp should be able to handle 12V and have either 2 or 4 speaker outputs. Some have 2 inputs, you can combine them with a y-splitter and then plug into the raspberry pi.
 
Random info:

This is just a big wrapper for omxplayer media player, rtl-sdr software, navit navigator, and scantool OBD2 app.

The bult-in wifi seems to have serious problems in the Raspberry Pi 3. Mine suddenly stopped working for no reason. After many hours of research and troubleshooting, determined it to be a hardware failure. Its recognized by linux, but will not do anything. It also has a problem with bluetooth. The same radio is used for both bluetooth and wifi which is a problem if you have both on. So even if my wifi was working I'd have problems with it anyways. So just buy a cheap usb dongle that works with raspbian and disable the built-in wifi. If you don't know which usb wifi to buy just search for a WIFI that is "Raspberry Pi compatible". USB adapters based on the Ralink RT5370 chip work just fine without any setup, just plug it in and it works.

This could be made with different computers other than Raspberry Pi, but you will have to change the code to play MP3s/media with another player than omxplayer, because its made for Raspberry Pi only. Also the screen I used was made to plug into a Raspberry Pi's GPIO. You will have to get one that has usb instead. Make sure you get a TOUCH screen and not just a screen.
