Lazarus: The WiFi Monitoring Software (version 1)
===================

Lazarus is a WiFi/Access Points monitoring software develped and tested in Ubuntu 10.04. By making use of native Linux network commands like ifconfig, iwconfig, iwlist, etc, we are able to extract general network information and present it through the command line.<br><br>
Each interface/wireless interface we find is being treated as a seperate thread in the program, and using a Markov Chain implementation we change the sleep time of these threads. The longer we find no new differences in threads (new info about the interfaces, changes in the results), the longer the threads sleep. Everything is calculated in runtime<br><br>

How to Use
===================
Just download the <b>MainThread.jar</b> and the <b>exe.sh</b> files from the folder and execute the exe.sh file. Works ONLY in Linux (tested in Ubuntu 10.04) as it makes heavy use of linux terminal commands. Some sample pics from the program in action can be found at the included documentation .pdf file.


Lazarus Software Complete
===================

The code in this repository was implemented as a first, more general approach of the final project. However I uploaded it on github as it can perfectly stand on its own as a well presented and working monitoring software.<br><br>
However the code for the complete project can be found here as well:<br>
<ul>

<li> <b>Lazarus: The WiFi Monitoring Software (version 2)</b> <br>
A "tinkered" version of the first project for Web Services communication(through SOAP).
</li>
<li> <b>Lazarus Web Server/GUI</b> <br>
The Web Services project which communicates with the monitoring software and the android application. It includes database connectivity for information storage and a GUI for presenting the info.
</li>
<li> <b>Lazarus4Android</b> <br>
A simple android application which connects to the Web Service (using ksoap 2) and sends general mobile information like GPS Longitude/Latitude, Battery level, etc.
</li>
</ul>

