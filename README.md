# RasPiCarStereo
RasPiCarStereo is an app that turns a Raspberry Pi 2 or 3 (maybe 1 too) into a car stereo complete with touchscreen, GPS navigation, bluetooth, MP3 player, FM radio and access to the vehicle's computer.

It's still being worked on, but most everything works. The bluetooth stuff could be done better.

To make a RasPiCarStereo, you need a Raspberry Pi of course. I made this for a Raspberry Pi 3 but it should work for 2 as well. The Raspberry Pi 1 probably wont be fast enough for what we want. Think of the load times everytime you start your car, faster is better. Install Raspbian Desktop edition, not the lite edition. Also set up any bluetooth devices you will be using in your car while your working on it to make things easier, not that it can't be done after its installed, its just a pain right now. To install the app, copy RasPiCarStereo.jar to the home folder (/home/pi/) and make a folder in the home folder called "Media". That's where all the media should go. You will have to install and setup more software listed below.

You will also need:

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
 - UBEC DC/DC Step-Down (Buck) Converter - 5V @ 3A output - to convert the car's power to what the Raspberry Pi wants
 
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
 - raspbery pi as a bluetooth speaker - https://www.instructables.com/id/Turn-your-Raspberry-Pi-into-a-Portable-Bluetooth-A/
 - make filesystem read-only so it can be powered off without proper shutdown - https://hallard.me/raspberry-pi-read-only/
 
Things i'd like to do:

 - finish bluetooth stuff - it hurts my head
 - make buttons/text change colors so it can match car's interior lights
 - better MP3 player
 - improved FM quality - currently seems to have noise, could be raspberry pi's analog out too
 - bluetooth audio is choppy - could be hardware issue, raspberry pi 3's bluetooth/wifi sucks. I turned off wifi to improve
 - could boot faster - need to remove unused apps, but its not too bad
 - custom OBD2 reader
 - can play movies, but there is no way to pause/stop player (omxplayer) without keyboard, just turn off car or wait
 - redsea app only gets the FM station name sometimes and doesn't seem to get song info either. I want more info!
 
I will put more info up on the actual wiring done to connect it to the car when i actually do it. I just need to order the OBD2 to usb wire and stepdown converter and hook it up. The plan is to hook up the converter to the switched-power and the raspberry pi. The amplifier will be powered through the constant power, but switched on/off with a relay connected to the switched-power so it is also switched. I think its better this way than have the amp and Rasp Pi connected to same power source. The amp should be able to regulate the voltage itself, if you bought the right one. A car can produce 12V with engine off and 14V with it running so get one that can do less than 12V and more than 14V.
 
Random info:

This is just a big wrapper for omxplayer media player, rtl-sdr software, navit navigator, and scantool OBD2 app.

The bult-in wifi seems to have serious problems in the Raspberry Pi 3. Mine suddenly stopped working for no reason. After many hours of research and troubleshooting, determined it to be a hardware failure. Its recognized by linux, but will not do anything. It also has a problem with bluetooth. The same radio is used for both bluetooth and wifi which is a problem if you have both on. So even if my wifi was working I'd have problems with it anyways. So just buy a cheap usb dongle that works with raspbian and disable the built-in wifi. If you don't know which usb wifi to buy just get a "WiPi". Its a usb wifi dongle made just for raspberry pi. It'll just work, no drivers to install.

This could be made with diffrerent computers other than Raspberry Pi, but you will have to change the code to play MP3s/media with another player than omxplayer, because its made for Raspberry Pi only. Also the screen I used was made to plug into a Raspberry Pi's GPIO. You will have to make sure it works with your computer or simply get a more expensive one without the GPIO connector that has usb instead. Make sure you get a TOUCH screen and not just a screen.
