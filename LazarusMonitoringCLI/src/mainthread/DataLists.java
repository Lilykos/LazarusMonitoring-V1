/* Class containing the wired/wireless interfaces lists, as well as the
 * access points lists.
 */


package mainthread;
import java.util.ArrayList;

public class DataLists {
    
    private ArrayList<Interface> myInterfaces;
    private ArrayList<InterfaceWireless> myWirelessInterfaces;
    private ArrayList<AccessPoint> myAccessPoints;
    
    Object lock = new Object();
    
    public DataLists() {
        myInterfaces = new ArrayList<>();
        myWirelessInterfaces = new ArrayList<>();
        myAccessPoints = new ArrayList<>();    
    }
    
    
    public Interface getMyInterfaces(int i) {
        return myInterfaces.get(i); }
    
    public InterfaceWireless getMyWirelessInterfaces(int i) {
        return myWirelessInterfaces.get(i); }
    
    
    public int getMyInterfacesSize() {
        synchronized (lock) {
            return myInterfaces.size();
    }}
    
    public int getMyWirelessInterfacesSize() {
        return myWirelessInterfaces.size(); }

    public int getMyAccessPointsSize() {
        return myAccessPoints.size(); }
    
    
    public void addMyInterfaces(Interface myInterface) {
        myInterfaces.add(myInterface); }
    
    public void addMyWirelessInterfaces(InterfaceWireless myInterface) {
        myWirelessInterfaces.add(myInterface); }
    
    
    public void removeMyInterfaces(int i) {
        myInterfaces.remove(i); }
    
    public void removeMyWirelessInterfaces(int i) {
        myWirelessInterfaces.remove(i); }
}