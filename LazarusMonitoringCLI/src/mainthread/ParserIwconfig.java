/* Iwconfig Parser*/


package mainthread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserIwconfig {
    
    Process proc;
    BufferedReader iwconfigInput;
    String line;
    
    public void setIwconfigProperties(InterfaceWireless myInterface) {
        String bsMAC;
        String essid;
        String mode;
        String link;
        String signal;
        String txPower;
        
        try {
            proc = Runtime.getRuntime().exec("sudo iwconfig " + myInterface.getName());
            iwconfigInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            
            while ((line = iwconfigInput.readLine()) != null) {
                
                ///// BASE STATION MAC /////
                int macIndex = line.indexOf("Access Point:");
                if (macIndex != -1) {
                    bsMAC = line.substring(macIndex  + "Access Point:".length());
                    bsMAC = bsMAC.trim();
                    myInterface.setBaseStationMAC(bsMAC);
                }
                
                
                ///// BASE STATION ESSID /////
                int essidIndex = line.indexOf("ESSID:");
                if (essidIndex != -1) {
                    essid = line.substring(essidIndex  + "ESSID:".length());
                    essid = essid.trim();
                    myInterface.setBaseStationESSID(essid);
                }
                
                
                ///// ACCESS POINT MODE /////
                int modeIndex = line.indexOf("Mode:");
                int frequencyIndex = line.indexOf("Frequency:");
                if (modeIndex != -1) {
                    mode = line.substring(modeIndex  + "Mode:".length(), frequencyIndex - 1);
                    mode = mode.trim();
                    myInterface.setAccessPointMode(mode);
                }
                
                ///// LINK QUALITY /////
                int linkIndex = line.indexOf("Link Quality=");
                int signalIndex = line.indexOf("Signal level=");
                if (linkIndex != -1) {
                    link = line.substring(linkIndex  + "Link Quality=".length(), signalIndex - 1);
                    link = link.trim();
                    myInterface.setLinkQuality(link);
                }
                
                
                ///// Signal Level /////
                if (signalIndex != -1) {
                    signal = line.substring(signalIndex  + "Signal level=".length());
                    signal = signal.trim();
                    myInterface.setSignalLevel(signal);
                }
                
                
                ///// TX POWER ////
                int txIndex = line.indexOf("Tx-Power=");
                if (txIndex != -1) {
                    txPower = line.substring(txIndex  + "Tx-Power=".length());
                    txPower = txPower.trim();
                    myInterface.setTxPower(txPower);
                }
            }
        } catch (IOException ex) { ex.printStackTrace();
        } finally {
            try {
                iwconfigInput.close();
                proc.destroy();
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
    
    // Checks whether an interface is wireless
    // if it is not we do nothing as the output is at stderr not at the stdout
    public boolean iwConfigCheck(String name) {
        boolean isWireless = false;
        
        try {
            proc = Runtime.getRuntime().exec("sudo iwconfig " + name);
            iwconfigInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            if ((line = iwconfigInput.readLine()) != null)
                isWireless = true;    
        
        } catch (IOException ex) { ex.printStackTrace();
        } finally {
            try { iwconfigInput.close(); }
            catch (IOException ex) { ex.printStackTrace(); }
        } 
        return isWireless;
    }
}