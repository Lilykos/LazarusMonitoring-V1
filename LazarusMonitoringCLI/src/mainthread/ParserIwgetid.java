/* Iwgetid Parser*/


package mainthread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserIwgetid {
    
    Process proc;
    BufferedReader iwgetidInput;
    
    public void setChannelProperties(InterfaceWireless myInterface) {
        String channel;
        
        try {
            proc = Runtime.getRuntime().exec("sudo iwgetid " + myInterface.getName() + " -c");
            iwgetidInput =  new BufferedReader(new InputStreamReader(proc.getInputStream()));;
            
            String line = iwgetidInput.readLine();
            int index = line.indexOf("Channel:");
            if (index != -1) {
                channel = line.substring(index  + "Channel:".length());
                channel = channel.trim();
                myInterface.setChannel(channel);
            }
        
        } catch (IOException ex) { ex.printStackTrace();
        } finally {
            try {
                iwgetidInput.close();
                proc.destroy();
            } catch (IOException ex) { ex.printStackTrace(); }
        }
}}