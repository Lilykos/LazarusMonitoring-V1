/* Prints all the relevant data on the command line. */


package mainthread;
import java.util.ArrayList;

public class DataPrinter implements Runnable {
    
    private volatile boolean checkAlive = true;
    Object lock = new Object();
    
    public void setAlive(boolean checkAlive) {
        this.checkAlive = checkAlive; }
    
    
    
    // Overloading the printData method....
    
    public void printData(Interface myInterface, boolean changesFound) {
        synchronized (lock) {
            if (changesFound) {
                System.out.println("Interface Name is " + myInterface.getName());
                System.out.println("\tMAC               : " + myInterface.getMAC());
                System.out.println("\tIP                : " + myInterface.getIP());
                System.out.println("\tBcast Address     : " + myInterface.getBcastAddr());
                System.out.println("\tDefault Gateway   : " + myInterface.getDefaultGateway());
                System.out.println("\tMask              : " + myInterface.getMask());
                System.out.println("\tMTU               : " + myInterface.getMTU());
                System.out.println("\tNetwork Adr       : " + myInterface.getNetworkAddress());
                System.out.println("\tPacket Error Rate : " + myInterface.getPacketErrorRate());
                System.out.println("\tBrcast Rate       : " + myInterface.getBroadcastRate() + "\n");
            }
            else System.out.println(myInterface.getName() + " had no changes!");
    }}
    
    
    public void printData(InterfaceWireless myInterface, boolean changesFound) {
        synchronized (lock) {
            if (changesFound) {
                System.out.println("Interface Name is " + myInterface.getName());
                System.out.println("\tMAC               : " + myInterface.getMAC());
                System.out.println("\tIP                : " + myInterface.getIP());
                System.out.println("\tBcast Address     : " + myInterface.getBcastAddr());
                System.out.println("\tDefault Gateway   : " + myInterface.getDefaultGateway());
                System.out.println("\tMask              : " + myInterface.getMask());
                System.out.println("\tMTU               : " + myInterface.getMTU());
                System.out.println("\tNetwork Adr       : " + myInterface.getNetworkAddress());
                System.out.println("\tPacket Error Rate : " + myInterface.getPacketErrorRate());
                System.out.println("\tBrcast Rate       : " + myInterface.getBroadcastRate() + "\n");
                
                System.out.println("\tBS MAC            : " + myInterface.getBaseStationMAC());
                System.out.println("\tBS ESSID          : " + myInterface.getBaseStationESSID());
                System.out.println("\tChannel           : " + myInterface.getChannel());
                System.out.println("\tAccess Point Mode : " + myInterface.getAccessPointMode());
                System.out.println("\tLink Quality      : " + myInterface.getLinkQuality());
                System.out.println("\tSignal level      : " + myInterface.getSignalLevel());
                System.out.println("\tTx-Power          : " + myInterface.getTxPower());
                System.out.println("\tNoise Level       : " + myInterface.getNoiseLevel());
                System.out.println("\tMissed Beacon Num : " + myInterface.getMissedBeacon());
            }
            else System.out.println(myInterface.getName() + " had no changes!");
    }}
    
    
    public void printData(String name, ArrayList<AccessPoint> apList, boolean changesFound) {
       synchronized (lock) {
           if (changesFound) {
               System.out.println(apList.size() + " Access Points Found by " + name + " wireless interface.");      
               for(int i = 0; i < apList.size(); i++) {
                   System.out.println("\tESSID : " + apList.get(i).getESSID() + "\tMAC : " +apList.get(i).getMAC());
                   System.out.println("\tChannel : " + apList.get(i).getChannel() + "\tAP Mode : " + apList.get(i).getMode());
                   System.out.println("\tSignal level : " + apList.get(i).getSignalLevel() + "\n");
           }}
           else System.out.println("No new Access Points Found by " + name + " wireless interface.");
    }}

    
    // Keeping the threadalive....
    @Override
    public void run() {
        while(this.checkAlive) {}
        System.out.println("Data Printer and all threads closed. Program finished. Thank you!");
    }
}