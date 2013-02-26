Lazarus: The WiFi Monitoring Software (version 1)
===================

Lazarus is a WiFi/Access Points monitoring software develped and tested in Ubuntu 10.04. By making use of native Linux network commands like ifconfig, iwconfig, iwlist, etc, we are able to extract general network information and present it through the command line.<br><br>
Each interface/wireless interface we find is being treated as a seperate thread in the program, and using a Markov Chain implementation we change the sleep time of these threads. The longer we find no new differences in threads (new info about the interfaces, changes in the results), the longer the threads sleep.<br><br>

How to Use
===================
Just download the <b>MainThread.jar</b> and the <b>exe.sh</b> files from the folder and execute the exe.sh file. Works ONLY in Linux (tested in Ubuntu 10.04) as it makes heavy use of linux terminal commands. 
